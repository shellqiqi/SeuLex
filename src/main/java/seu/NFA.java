package seu;

import java.util.HashSet;
import java.util.Vector;

public class NFA {
    public final int EPSILON = 128;
    public final int COLUMNS = 129;
    public Vector<Vector<HashSet<Integer>>> transitionTable = new Vector<>();

    public final int start = 0;
    public int accept;

    public NFA() {
        accept = 0;
        transitionTable.add(initStateRow());
    }

    public NFA(char ch) {
        Vector<HashSet<Integer>> startStateRow = initStateRow(ch, 1);
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
        transitionTable.add(startStateRow);
        transitionTable.add(acceptStateRow);
        accept = 1;
    }

    public NFA(char from, char to) throws Exception {
        if (from <= to) {
            char[] cs = new char[to - from + 1];
            for (int i = 0; i < to - from + 1; i++) cs[i] = ((char) (from + i));
            Vector<HashSet<Integer>> startStateRow = initStateRow(cs, 1);
            Vector<HashSet<Integer>> acceptStateRow = initStateRow();
            transitionTable.add(startStateRow);
            transitionTable.add(acceptStateRow);
            accept = 1;
        } else throw new Exception("Lex syntax error - In [x-y] x must small than y");
    }

    public NFA(NFA nfa) {
        accept = nfa.accept;
        for (Vector<HashSet<Integer>> stateRow : nfa.transitionTable) {
            Vector<HashSet<Integer>> newStateRow = new Vector<>();
            for (HashSet<Integer> transitionCell : stateRow) {
                newStateRow.add(new HashSet<>(transitionCell));
            }
            transitionTable.add(newStateRow);
        }
    }

    private Vector<HashSet<Integer>> initStateRow() {
        Vector<HashSet<Integer>> stateRow = new Vector<>();
        for (int i = 0; i < COLUMNS; i++) stateRow.add(new HashSet<>());
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char ch, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, ch, transition);
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char ch, int[] transitions) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, ch, transitions);
        return stateRow;
    }

    private Vector<HashSet<Integer>> initStateRow(char[] chars, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, chars, transition);
        return stateRow;
    }

    private void addTransition(int index, char ch, int transition) {
        transitionTable.elementAt(index).elementAt(ch).add(transition);
    }

    private void addTransition(int index, char ch, int[] transitions) {
        for (int transition : transitions) addTransition(index, ch, transition);
    }

    private void addTransition(int index, char[] chars, int transition) {
        for (char ch : chars) addTransition(index, ch, transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char ch, int transition) {
        stateRow.elementAt(ch).add(transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char ch, int[] transitions) {
        for (int transition : transitions) addTransition(stateRow, ch, transition);
    }

    private void addTransition(Vector<HashSet<Integer>> stateRow, char[] chars, int transition) {
        for (char ch : chars) addTransition(stateRow, ch, transition);
    }

    public NFA increasedStateNumber(int value) {
        NFA nfa = new NFA(this);
        for (Vector<HashSet<Integer>> stateRow : nfa.transitionTable) {
            for (HashSet<Integer> transitionCell : stateRow) {
                for (Integer transition : transitionCell) {
                    transitionCell.remove(transition);
                    transitionCell.add(transition + value);
                }
            }
        }
        return nfa;
    }

    public static NFA concat(NFA... nfas) {
        NFA result = new NFA();
        for (NFA nfa : nfas) {
            int dValue = result.accept;
            result.accept += nfa.accept;
            result.transitionTable.remove(result.transitionTable.lastElement());
            result.transitionTable.addAll(nfa.increasedStateNumber(dValue).transitionTable);
        }
        return result;
    }

    public String debugMessage() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < transitionTable.size(); i++) {
            buffer.append(i).append(": ");
            for (int ch = 0; ch < COLUMNS; ch++) {
                if (ch >= 32 && ch <= 126) {
                    buffer.append('\'').append((char) ch).append('\'');
                } else if (ch == 128) {
                    buffer.append("\'ε\'");
                } else {
                    buffer.append(ch);
                }
                buffer.append("[");
                boolean first = true;
                for (Integer transition : transitionTable.elementAt(i).elementAt(ch)) {
                    buffer.append(transition);
                    if (!first) buffer.append(',');
                    first = false;
                }
                buffer.append("] ");
            }
            buffer.append('\n');
        }
        buffer.append("accept: ").append(accept).append('\n');
        return buffer.toString();
    }
}
