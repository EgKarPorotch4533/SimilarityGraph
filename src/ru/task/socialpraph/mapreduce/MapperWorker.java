package ru.task.socialpraph.mapreduce;

import java.util.Queue;

import ru.task.socialpraph.structures.FriendsHashMap;
import ru.task.socialpraph.structures.Pair;

public class MapperWorker implements Runnable {
	
	private final Queue<Pair> queue;
	private final FriendsHashMap friendsHashMap;
	
	public MapperWorker(final Queue<Pair> queue) {
		this.queue = queue;
		this.friendsHashMap = new FriendsHashMap();
	}

	@Override
	public void run() {
		while(!queue.isEmpty()) {
			Pair currentPair = queue.poll();
			if (currentPair != null) {
				friendsHashMap.addConnection(currentPair.getLeft(), currentPair.getRight());
			}
		}
	}
	
	public FriendsHashMap getFriendsHashMap() {
		return friendsHashMap;
	}
	
	public boolean hasFriendsInContainer() {
		return friendsHashMap.size() > 0;
	}

}
