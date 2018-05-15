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
        accept = 0;
        transitionTable.add(initStateRow());
    }

    public NFA(char c) {
        Vector<HashSet<Integer>> startStateRow = initStateRow(c, 1);
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
        transitionTable.add(startStateRow);
        transitionTable.add(acceptStateRow);
        accept = 1;
    }

    public NFA(char from, char to) throws Exception {
        if (from <= to) {
            char[] cs = new char[to - from + 1];
            for (int i = 0; i < to - from + 1; i++) cs[i] = ((char)(from + i));
            Vector<HashSet<Integer>> startStateRow = initStateRow(cs, 1);
            Vector<HashSet<Integer>> acceptStateRow = initStateRow();
            transitionTable.add(startStateRow);
            transitionTable.add(acceptStateRow);
            accept = 1;
        } else throw new Exception("Lex syntax error - In [x-y] x must small than y");
    }

    private Vector<HashSet<Integer>> initStateRow() {
        Vector<HashSet<Integer>> stateRow = new Vector<>();
        for (int i = 0; i < COLUMNS; i++) stateRow.add(new HashSet<>());
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char c, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, c, transition);
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char c, int[] transitions) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, c, transitions);
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char[] cs, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, cs, transition);
        return stateRow;
    }

    private void addTransition(int index, char c, int transition) {
        transitionTable.elementAt(index).elementAt(c).add(transition);
    }

    private void addTransition(int index, char c, int[] transitions) {
        for (int transition : transitions) addTransition(index, c, transition);
    }

    private void addTransition(int index, char[] cs, int transition) {
        for (char c : cs) addTransition(index, c, transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char c, int transition) {
        stateRow.elementAt(c).add(transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char c, int[] transitions) {
        for (int transition : transitions) addTransition(stateRow, c, transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char[] cs, int transition) {
        for (char c : cs) addTransition(stateRow, c, transition);
    }

    private void increaseStateNumber(int value) { //TODO: consider whether it need to change the object
        for (Vector<HashSet<Integer>> stateRow : transitionTable) {
            for (HashSet<Integer> transitionCell : stateRow) {
                for (Integer transition : transitionCell) {
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

    public String debugMessage() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < transitionTable.size(); i++) {
            buffer.append(i).append(": ");
            for (int c = 0; c < COLUMNS; c++) {
                if (c >= 32 && c <= 126) {
                    buffer.append('\'').append((char)c).append('\'');
                } else if (c == 128) {
                    buffer.append("\'ε\'");
                } else {
                    buffer.append(c);
                }
                buffer.append("(");
                for (Integer transition : transitionTable.elementAt(i).elementAt(c)) {
                    buffer.append(transition).append(',');
                }
                buffer.append(") ");
            }
            buffer.append('\n');
        }
        buffer.append("accept: ").append(accept).append('\n');
        return buffer.toString();
    }
}
