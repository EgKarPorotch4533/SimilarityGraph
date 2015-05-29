package ru.task.socialpraph.mapreduce;

import java.util.List;
import java.util.Queue;

import ru.task.socialpraph.sample.PermutarorOfTwo;
import ru.task.socialpraph.structures.PersonCircle;
import ru.task.socialpraph.structures.SimilarityMatrix;

public class ReducerSimilarityWorker implements Runnable {
	
	private final Queue<PersonCircle> circlesQueue;
	private final SimilarityMatrix similarityMaxtrix;
	
	public ReducerSimilarityWorker(final Queue<PersonCircle> circlesQueue, final SimilarityMatrix similarityMaxtrix) {
		this.circlesQueue = circlesQueue;
		this.similarityMaxtrix = similarityMaxtrix;
	}

	@Override
	public void run() {
		while (!circlesQueue.isEmpty()) {
			PersonCircle circle = circlesQueue.poll();
			// diagonal cell
			similarityMaxtrix.put(circle.getPerson(), circle.getPerson(), circle.getFriends().size());
			// side cells
			List<int[]> tripleConnections = PermutarorOfTwo.getPermutaions(circle.getFriends());
			for (int[] pair : tripleConnections) {
				similarityMaxtrix.put(pair[0], pair[1], 1);
			}
		}
	}

}
