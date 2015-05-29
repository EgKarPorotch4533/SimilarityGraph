package ru.task.socialpraph.structures;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PersonCircle {
	
	private int person;
	private LinkedList<Integer> friends;
	
	public PersonCircle(int person, LinkedList<Integer> friends) {
		this.person = person;
		this.friends = friends;
	}
	
	public PersonCircle(int person, Set<Integer> friendsSet) {
		this.person = person;
		this.friends = new LinkedList<Integer>();
		this.friends.addAll(friendsSet);
	}

	public int getPerson() {
		return person;
	}

	public LinkedList<Integer> getFriends() {
		return friends;
	}
}
