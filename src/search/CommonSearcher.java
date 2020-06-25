package search;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher<Solution> {
	protected PriorityQueue<State> openList;
	private int evaluatedNodes;
	
	public CommonSearcher() {
		this.openList = new PriorityQueue<State>();
		this.evaluatedNodes = 0;
	}
	
	final protected State popOpenList() {
		this.evaluatedNodes++;
		return openList.poll();
	}

	protected List<State> backtrace(State n, State initialState) {
		List<State> list = new ArrayList<>();
		while(n != null) {
			list.add(n);
			n = n.getPreviousState();
		}
		return list;
	}
	
	final protected void addToOpenList(State s) {
		this.openList.add(s);
	}

	final public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	abstract public Solution search(Searchable s);
}
