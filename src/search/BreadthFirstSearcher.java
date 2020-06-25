package search;

import java.util.HashSet;
import java.util.List;

public class BreadthFirstSearcher extends CommonSearcher<List<State>> {

    public BreadthFirstSearcher() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<State> search(Searchable s) {
        State initial =  s.getInitialState();
        addToOpenList(initial);
        HashSet<State> closedSet = new HashSet<>();

        HashSet<State> reachedGoalStates = new HashSet<>();

        while(openList.size() > 0) {
            State n = popOpenList();
            closedSet.add(n);

            if(s.isGoalState(n)) {
                reachedGoalStates.add(n);
            }

            List<Tuple<State, Double>> tuples = s.getAllPossibleStates(n);
            for(Tuple<State, Double> tuple : tuples) {
                State state = tuple.getElement1();
                double originalCost = tuple.getElement2();

                if(!closedSet.contains(state) && !openList.contains(state)) {
                    // No one got to state yet
                    state.setPreviousState(n);
                    state.setCost(state.getCost() + n.getCost());
                    addToOpenList(state);

                } else if(n.getCost() + originalCost < state.getCost()) {
                    // Better path to state found
                    state.setPreviousState(n);
                    state.setCost(n.getCost() + originalCost);
                    openList.remove(state);
                    openList.add(state);
                }
            }
        }

        State minimal = null;
        for(State other : reachedGoalStates) {
            if(minimal == null || other.getCost() < minimal.getCost())
                minimal = other;
        }
        return backtrace(minimal, initial);
    }
}
