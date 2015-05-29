package ru.task.socialpraph.structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendsHashMap {
	
	Map<Integer, Set<Integer>> map;
	
	public FriendsHashMap() {
		map = new HashMap<Integer, Set<Integer>>();
	}
	
	public int size() {
		return map.size();
	}
	
	public void addConnection(int first, int second) {
		// for the first
		if (map.containsKey(first)) {
			Set<Integer> friends = map.get(first);
			if (!friends.contains(second)) {
				friends.add(second);
			}
		} else {
			Set<Integer> newFriends = new HashSet<Integer>();
			newFriends.add(second);
			map.put(first, newFriends);
		}
		// for the second
		if (map.containsKey(second)) {
			Set<Integer> friends = map.get(second);
			if (!friends.contains(first)) {
				friends.add(first);
			}
		} else {
			Set<Integer> newFriends = new HashSet<Integer>();
			newFriends.add(first);
			map.put(second, newFriends);
		}
	}
	
	public void addConnections(int person, Set<Integer> friends) {
		if (map.containsKey(person)) {
			map.get(person).addAll(friends);
		} else {
			map.put(person, friends);
		}
	}
	
	public boolean containsKey(int key) {
		return map.containsKey(key);
	}
	
	public Set<Integer> getFriends(int key) {
		return map.get(key);
	}
	
	public Set<Integer> getMapKeySet() {
		return map.keySet();
	}
	
	public Set<Map.Entry<Integer, Set<Integer>>> getMapEntrySet() {
		return map.entrySet();
	}
	
	/*
	 * This method for flushing process 'pair by pair'
	 */
	public void flushResults(FriendsHashMap dest) {
		for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
			if (dest.containsKey(entry.getKey())) {
				Set<Integer> destFriends = dest.getFriends(entry.getKey());
				Set<Integer> friendsToAdd = entry.getValue();
				destFriends.addAll(friendsToAdd);
			} else {
				dest.addConnections(entry.getKey(), entry.getValue());
			}
		}
	}

	/*
	 * This method sets blocking on dest hashmap. Use for flushing to common destination by multiple threads
	 */
	public void blockingFlushResults(FriendsHashMap dest) {
		for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
			if (dest.containsKey(entry.getKey())) {
				Set<Integer> destFriends = dest.getFriends(entry.getKey());
				Set<Integer> friendsToAdd = entry.getValue();
				synchronized (destFriends) {
					destFriends.addAll(friendsToAdd);
				}
			}
		}
	}
}
