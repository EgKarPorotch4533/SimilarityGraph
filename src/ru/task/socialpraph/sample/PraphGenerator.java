package ru.task.socialpraph.sample;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;

public class PraphGenerator {
	
	public static class HashedEdge {
		int i;
		int j;
		public HashedEdge(int i, int j) {
			if (i < j) {
				this.i = i;
				this.j = j;
			} else {
				this.i = j;
				this.j = i;
			}
		}
		@Override
		public int hashCode() {
			return ("" + i + " " + j).hashCode();
		}
	}
	
	public static void main(String ... args) {
		getMatrix(System.out, 100, 15);
	}

	/**
	 * 
	 * @param dimention
	 * @param density - max percent of edges from node relative to the total count of nodes 
	 */
	public static void getMatrix(PrintStream stream, int dimention, int density) {
		HashSet<HashedEdge> dupSet = new HashSet<HashedEdge>();
		for (int i = 1; i <= dimention; i++) {
			int countOfEdges = new Random().nextInt(dimention * density / 100) - 1;
			if (!new Random().nextBoolean()) {
				countOfEdges *= 0;
			}
			while (countOfEdges-- > 0) {
				int neghbour = new Random().nextInt(dimention) + 1;
				HashedEdge edge = new HashedEdge(i, neghbour);
				if (!dupSet.contains(edge)) {
					if (i < neghbour) {
						stream.println(String.format("%d %d", i, neghbour));
						dupSet.add(edge);
					} else if (i > neghbour) {
						stream.println(String.format("%d %d", neghbour, i));
						dupSet.add(edge);
					} else {
						// do not consider looped edge, give more try..
						++countOfEdges;
					}
				} else {
					// give more try
					++countOfEdges;
				}
			}
		}
	}

	public static void getMatrix(PrintStream stream, int dimention) {
		getMatrix(stream, dimention, 15);
	}
}
