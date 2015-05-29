package ru.task.socialpraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.task.socialpraph.sample.PraphGenerator;

public class GraphTransformerTest {
	
	File taskGraphFile = new File("temp/task_file.txt");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransform_TaskFile() throws FileNotFoundException, IOException {
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(taskGraphFile), System.out);
	}

	@Test
	public void testTransform_Random100Density15() throws FileNotFoundException, IOException {
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
		int dimention = 100;
		int density = 15;
		PraphGenerator.getMatrix(printStream, dimention, density);
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(inputFile), System.out);
	}

	@Test
	public void testTransform_Random100Density30() throws FileNotFoundException, IOException {
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
		int dimention = 100;
		int density = 30;
		PraphGenerator.getMatrix(printStream, dimention, density);
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(inputFile), System.out);
	}

	@Test
	public void testTransform_Random100Density50() throws FileNotFoundException, IOException {
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
		int dimention = 100;
		int density = 50;
		PraphGenerator.getMatrix(printStream, dimention, density);
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(inputFile), System.out);
	}

	@Test
	public void testTransform_Random1000Density50() throws FileNotFoundException, IOException {
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
		int dimention = 1000;
		int density = 50;
		PraphGenerator.getMatrix(printStream, dimention, density);
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(inputFile), System.out);
	}

	@Test
	public void testTransform_Random5000Density50() throws FileNotFoundException, IOException {
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
		int dimention = 5000;
		int density = 50;
		PraphGenerator.getMatrix(printStream, dimention, density);
		GraphTransformer graphTransformer = new GraphTransformer();
		graphTransformer.transform(new FileInputStream(inputFile), System.out);
	}

}
