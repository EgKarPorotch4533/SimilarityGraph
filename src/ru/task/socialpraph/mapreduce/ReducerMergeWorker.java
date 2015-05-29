package ru.task.socialpraph.mapreduce;

import java.util.Queue;

import ru.task.socialpraph.structures.FriendsHashMap;

public class ReducerMergeWorker implements Runnable {
	
	private final Queue<FriendsHashMap> resultsQueue;
	
	public ReducerMergeWorker(final Queue<FriendsHashMap> resultsQueue) {
		this.resultsQueue = resultsQueue;
	}

	@Override
	public void run() {
		while (resultsQueue.size() > 1) {
			boolean maybeSomeoneAdds = false;
			FriendsHashMap first = null;
			FriendsHashMap second = null;
			synchronized (resultsQueue) {
				if (resultsQueue.size() > 1) {
					first = resultsQueue.poll();
					second = resultsQueue.poll();
				} else {
					maybeSomeoneAdds = true;
				}
			}
			if (!maybeSomeoneAdds) {
				first.flushResults(second);
				resultsQueue.add(second);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			} else {
				// wait some more time
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

}
