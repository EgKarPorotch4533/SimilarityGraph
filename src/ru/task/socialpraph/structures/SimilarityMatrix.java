package ru.task.socialpraph.structures;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.Map;

import ru.task.socialpraph.sample.HashUtil;

public class SimilarityMatrix {
	
	public class Key {
		private int i;
		private int j;
		private long longHashCode;
		
		public Key(int i, int j) {
			if (i < j) {
				this.i = i;
				this.j = j;
			} else {
				this.i = j;
				this.j = i;
			}
			longHashCode = HashUtil.hashcode64bits("" + this.i + " " + this.j);
		}
		
		public int getI() {
			return i;
		}
		
		public int getJ() {
			return j;
		}
		
		public long longHashCode() {
			return longHashCode;
		}
	}
	
	public class Cell {
		
		private Key key;
		private int connections;
		
		
		public Cell(Key key, int initialConnections) {
			this.key = key;
			this.connections = initialConnections;
		}
		
		public void incrementConnections(int incrementBy) {
			connections += incrementBy;
		}

		public Key getKey() {
			return key;
		}

		public int getConnections() {
			return connections;
		}
	}
	
	private int dimention;
	private Long2ObjectOpenHashMap<Cell> cellMappings;
	
	public SimilarityMatrix(int dimention) {
		this.dimention = dimention;
		cellMappings = new Long2ObjectOpenHashMap<Cell>();
	}
	
	public synchronized void put(int i, int j, int newConnections) {
		
		Key key = new Key(i, j);
		
		if (cellMappings.containsKey(key.longHashCode())) {
			cellMappings.get(key.longHashCode()).incrementConnections(newConnections);
		} else {
			cellMappings.put(key.longHashCode(), new Cell(key, newConnections));
		}
	}

	public Long2ObjectOpenHashMap<Cell> getCellMappings() {
		return cellMappings;
	}
	
	public int[][] toTable() {
		int[][] table = new int[dimention][dimention];
		for (Map.Entry<Long, Cell> entry : cellMappings.entrySet()) {
			Key key = entry.getValue().getKey();
			if (key.i == key.j) {
				table[key.i - 1][key.i - 1] = entry.getValue().getConnections();
			} else {
				table[key.i - 1][key.j - 1] = entry.getValue().getConnections();
				table[key.j - 1][key.i - 1] = entry.getValue().getConnections();
			}
		}
		return table;
	}
	
	//public String toString();
}
