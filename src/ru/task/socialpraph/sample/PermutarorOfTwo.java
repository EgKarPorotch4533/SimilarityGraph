package ru.task.socialpraph.sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * All permutations are ordered. Suppose having no duplicates.
 * @author ekorotchenko
 *
 */
public class PermutarorOfTwo {
	
	public static List<int[]> getPermutaions(LinkedList<Integer> list) {
		if (list.size() < 2) {
			return new ArrayList<int[]>();
		}
		List<int[]> perms = new ArrayList<int[]>();
		while(!list.isEmpty()) {
			int first = list.poll();
			for (int other : list) {
				if (first < other) {
					perms.add(new int[] {first, other});
				} else {
					perms.add(new int[] {other, first});
				}
			}
		}
		return perms;
	}
}
