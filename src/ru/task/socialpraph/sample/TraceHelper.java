package ru.task.socialpraph.sample;

import java.io.PrintStream;

import ru.task.socialpraph.structures.SimilarityMatrix;

public class TraceHelper {
	
	private final PrintStream printStream;
	
	public TraceHelper(final PrintStream printStream) {
		this.printStream = printStream;
	}
	
	public void outSMatrix(SimilarityMatrix similarityMatrix) {
		outSMatrix(similarityMatrix, printStream);
	}
	
	public void outSMatrix(SimilarityMatrix similarityMatrix, PrintStream stream) {
		int[][] table = similarityMatrix.toTable();
		final String SEP = "\t";
		for (int ii = 0; ii < table.length; ii++) {
			for (int jj = 0; jj < table[ii].length; jj++) {
				stream.print(table[ii][jj]);
				stream.print(SEP);
			}
			stream.println();
		}
	}
}
