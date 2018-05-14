package seu;

import java.util.HashSet;
import java.util.Vector;

public class NFA {
    public final int EPSILON = 128;
    public final int COLUMNS = 129;
    public Vector<Vector<HashSet<Integer>>> transitionTable = new Vector<>();

    public final int start = 0;
    private int accept;

    public int getAccept() {
        return accept;
    }

    public NFA() {
        Vector<HashSet<Integer>> initStateRow = new Vector<>();
        for (int i = 0; i < COLUMNS; i++) {
            initStateRow.add(new HashSet<>());
        }
        accept = 0;
        transitionTable.add(initStateRow);
    }

    private void increaseStateNumber(int value) { //TODO: consider whether it need to change the object
        for (Vector<HashSet<Integer>> stateRow : transitionTable) {
            for (HashSet<Integer> transitionCell : stateRow) {
                for (Integer transition: transitionCell) {
                    transitionCell.remove(transition);
                    transitionCell.add(transition + value);
                }
            }
        }
    }

    public NFA concat(NFA o) {
        int dValue = accept;
        accept += o.getAccept();
        o.increaseStateNumber(dValue);
        for (Vector<HashSet<Integer>> stateRow : o.transitionTable) {
            //TODO
        }
        return this;
    }
}
