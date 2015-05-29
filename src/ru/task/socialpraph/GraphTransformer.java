package ru.task.socialpraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ru.task.socialpraph.mapreduce.MapperWorker;
import ru.task.socialpraph.mapreduce.ReducerMergeWorker;
import ru.task.socialpraph.mapreduce.ReducerSimilarityWorker;
import ru.task.socialpraph.sample.PraphGenerator;
import ru.task.socialpraph.sample.Timer;
import ru.task.socialpraph.sample.TraceHelper;
import ru.task.socialpraph.structures.FriendsHashMap;
import ru.task.socialpraph.structures.Pair;
import ru.task.socialpraph.structures.PersonCircle;
import ru.task.socialpraph.structures.SimilarityMatrix;

/**
 * Implements a transformation from input Graph description to Similarity Matrix.
 * @author ekorotchenko
 *
 */
public class GraphTransformer {
	
	// (I have exactly 4 cores on my laptop)
	private static final int FIXED_THREAD_POOL_SIZE = 4;
	private static final int MAX_MAPPER_WORKERS = 4;
	private static final int MONITORING_QUEUE_TIMEOUT = 5000;
	private static final TimeUnit AWAIT_TERMINATION_UNIT = TimeUnit.SECONDS;
	private static final int AWAIT_TERMINATION_COUNT = 30;
	private static final int SERVICE_DELAY = 500;
	
	private static final int TEST_GRAPH_DIMENTION = 100;
	private static final int TEST_GRAPH_DENSITY_PERCENT = 30;

	private static PrintStream logger = System.err;
	private static TraceHelper traceHelper = new TraceHelper(logger);
	
	private final Queue<Pair> sourceQueue;
	private List<MapperWorker> mapperWorkers;
	private int highValue;
	private static Timer timer = new Timer();
	
	public GraphTransformer() {
		sourceQueue = new LinkedBlockingQueue<Pair>();
		mapperWorkers = new ArrayList<MapperWorker>();
	}
	
	public static void main(String ... args) throws IOException {
		File tempDir = new File("temp");
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		File inputFile = new File(tempDir, "input.txt");
		if (inputFile.exists()) {
			inputFile.delete();
		}
		inputFile.createNewFile();
		PrintStream printStream = new PrintStream(inputFile);
		int dimention = TEST_GRAPH_DIMENTION;
		int density = TEST_GRAPH_DENSITY_PERCENT;
		logger.printf("Generating pseudo-random graph of size %d and density %d%% ..%n", dimention, density);
		
		PraphGenerator.getMatrix(printStream, dimention, density);

		logger.printf("Running MapReduce..");
		transform(new FileInputStream(inputFile), logger);
	}
	
	private void readData(InputStream stream) throws IOException {
		timer.startIo1();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			int k = 0;
		    for(String line; (line = br.readLine()) != null; k++) {
		        if (!line.isEmpty()) {
		        	String[] pair = line.split("\\s++", 2);
		        	if (pair.length != 2) {
		        		throw new IOException("Wrong input file pattern syntax near line " + k);
		        	}
		        	int left = Integer.parseInt(pair[0]);
		        	int right = Integer.parseInt(pair[1]);
		        	sourceQueue.add(new Pair(left, right));
		        	highValue = (highValue < left ? left : highValue);
		        	highValue = (highValue < right ? right : highValue);
		        }
		    }
		}
		timer.stopIo1();
	}
	
	public void init(InputStream stream) throws IOException {
		readData(stream);
	}
	
	public static void transform(InputStream inputStream, PrintStream outputStream) throws IOException {
		timer.startTotal();
		logger.println("Creating transformer..");
		timer.startProcessing();
		GraphTransformer transformer  = new GraphTransformer();
		logger.println("Reading input..");
		transformer.init(inputStream);
		logger.println("Map step..");
		timer.startMapping();
		transformer.map();
		timer.stopMapping();
		logger.println("Reduce step..");
		timer.startReducing();
		transformer.reduce(outputStream);
		timer.stopReducing();
		timer.stopProcessing();
		logger.println("\nCompleted.");
		timer.stopTotal();
		logger.printf("%n%n---%n%s", timer.toString());
	}
	
	public void map() {
		ExecutorService executorService = Executors.newFixedThreadPool(FIXED_THREAD_POOL_SIZE);
		
		try {
			
			// map & add workers
			for (int i = 0; i < MAX_MAPPER_WORKERS; i++) {
				MapperWorker newWorker = new MapperWorker(sourceQueue);
				mapperWorkers.add(newWorker);
				executorService.execute(newWorker);
			}

			Thread.sleep(SERVICE_DELAY);
			
			// Current thread wait until having empty queue
			
			while (!sourceQueue.isEmpty()) {
				Thread.sleep(MONITORING_QUEUE_TIMEOUT);
			}
			
			executorService.shutdown();
			executorService.awaitTermination(AWAIT_TERMINATION_COUNT, AWAIT_TERMINATION_UNIT);
			
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			e.printStackTrace();
		}
	}
	
	public void reduce(PrintStream outputStream) {
		FriendsHashMap totalFriendsMap = mergeResultHashSet();
		SimilarityMatrix similarityMatrix = calculateSimilarityMatrix(totalFriendsMap);
		timer.startIo2();
		logger.println();
		traceHelper.outSMatrix(similarityMatrix, outputStream);
		timer.stopIo2();
	}
	
	private SimilarityMatrix calculateSimilarityMatrix(FriendsHashMap friendsMap) {
		SimilarityMatrix similarityMatrix = new SimilarityMatrix(highValue);
		Queue<PersonCircle> circlesQueue = new LinkedBlockingQueue<PersonCircle>();
		for (Map.Entry<Integer, Set<Integer>> personEntry : friendsMap.getMapEntrySet()) {
			circlesQueue.add(new PersonCircle(personEntry.getKey(), personEntry.getValue()));
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(FIXED_THREAD_POOL_SIZE);

		try {
			
			for (int i = 0; i < MAX_MAPPER_WORKERS; i++) {
				ReducerSimilarityWorker newWorker = new ReducerSimilarityWorker(circlesQueue, similarityMatrix);
				executorService.execute(newWorker);
			}

			Thread.sleep(SERVICE_DELAY);
			
			while (!circlesQueue.isEmpty()) {
				Thread.sleep(MONITORING_QUEUE_TIMEOUT);
			}

			executorService.shutdown();
			executorService.awaitTermination(AWAIT_TERMINATION_COUNT, AWAIT_TERMINATION_UNIT);

			return similarityMatrix;
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private FriendsHashMap mergeResultHashSet() {
		Queue<FriendsHashMap> resultsQueue = new LinkedBlockingQueue<FriendsHashMap>();
		for (MapperWorker mWorker : mapperWorkers) {
			if (mWorker.hasFriendsInContainer()) {
				resultsQueue.add(mWorker.getFriendsHashMap());
			}
		}
		
		if (resultsQueue.isEmpty()) {
			return new FriendsHashMap();
		} else if (resultsQueue.size() == 1) {
			return resultsQueue.poll();
		}

		ExecutorService executorService = Executors.newFixedThreadPool(FIXED_THREAD_POOL_SIZE);

		try {
			
			for (int i = 0; i < MAX_MAPPER_WORKERS; i++) {
				ReducerMergeWorker newWorker = new ReducerMergeWorker(resultsQueue);
				executorService.execute(newWorker);
			}

			Thread.sleep(SERVICE_DELAY);
			
			while (resultsQueue.size() > 1) {
				Thread.sleep(MONITORING_QUEUE_TIMEOUT);
			}

			executorService.shutdown();
			executorService.awaitTermination(AWAIT_TERMINATION_COUNT, AWAIT_TERMINATION_UNIT);

			return resultsQueue.poll();
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
