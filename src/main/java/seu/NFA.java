package seu;

import java.util.HashSet;
import java.util.Vector;

public class NFA {
    /* Definition of ε */
    public final static char EPSILON = 128;
    /* Total columns of transition table */
    public final static int COLUMNS = 129;
    /* The transition table of the NFA */
    public Vector<Vector<HashSet<Integer>>> transitionTable = new Vector<>();
    /* State to start always 0 */
    public final static int start = 0;
    /* State to accept */
    public int accept;

    public NFA() {
        this(false);
    }

    public NFA(boolean emptyTable) {
        accept = 0;
        if (!emptyTable) transitionTable.add(initStateRow());
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
            Vector<Character> chars = new Vector<>();
            for (int i = 0; i < to - from + 1; i++) chars.add((char) (from + i));
            Vector<HashSet<Integer>> startStateRow = initStateRow(chars, 1);
            Vector<HashSet<Integer>> acceptStateRow = initStateRow();
            transitionTable.add(startStateRow);
            transitionTable.add(acceptStateRow);
            accept = 1;
        } else throw new Exception("Lex syntax error - In [x-y] x must be small than y");
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

    /**
     * Init a state row with no transition.
     *
     * @return A state row.
     */
    private static Vector<HashSet<Integer>> initStateRow() {
        Vector<HashSet<Integer>> stateRow = new Vector<>();
        for (int i = 0; i < COLUMNS; i++) stateRow.add(new HashSet<>());
        return stateRow;
    }

    /**
     * Init a state row with a char transit to another state.
     *
     * @param ch         char.
     * @param transition Another state.
     * @return A state row.
     */
    private static Vector<HashSet<Integer>> initStateRow(char ch, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, ch, transition);
        return stateRow;
    }

    /**
     * Init a state row with a char transit to other states.
     *
     * @param ch          char.
     * @param transitions Other states.
     * @return A state row.
     */
    private static Vector<HashSet<Integer>> initStateRow(char ch, HashSet<Integer> transitions) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, ch, transitions);
        return stateRow;
    }

    /**
     * Init a state row transit to another state with chars.
     *
     * @param chars      chars.
     * @param transition Another state.
     * @return A state row.
     */
    private static Vector<HashSet<Integer>> initStateRow(Vector<Character> chars, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, chars, transition);
        return stateRow;
    }

    private void addTransition(int index, char ch, int transition) {
        transitionTable.elementAt(index).elementAt(ch).add(transition);
    }

    private void addTransition(int index, char ch, HashSet<Integer> transitions) {
        transitionTable.elementAt(index).elementAt(ch).addAll(transitions);
    }

    private void addTransition(int index, Vector<Character> chars, int transition) {
        for (char ch : chars) addTransition(index, ch, transition);
    }

    private static void addTransition(Vector<HashSet<Integer>> stateRow, char ch, int transition) {
        stateRow.elementAt(ch).add(transition);
    }

    private static void addTransition(Vector<HashSet<Integer>> stateRow, char ch, HashSet<Integer> transitions) {
        stateRow.elementAt(ch).addAll(transitions);
    }

    private static void addTransition(Vector<HashSet<Integer>> stateRow, Vector<Character> chars, int transition) {
        for (char ch : chars) addTransition(stateRow, ch, transition);
    }

    /**
     * All state number add a value for every row shifting back.
     *
     * @param value Number to add.
     * @return A copy of origin transition table with state number added.
     */
    private Vector<Vector<HashSet<Integer>>> increasedStateNumber(int value) {
        Vector<Vector<HashSet<Integer>>> newTable = new Vector<>();
        for (Vector<HashSet<Integer>> stateRow : transitionTable) {
            Vector<HashSet<Integer>> newRow = new Vector<>();
            for (HashSet<Integer> transitionCell : stateRow) {
                HashSet<Integer> newCell = new HashSet<>();
                for (Integer transition : transitionCell) {
                    newCell.add(transition + value);
                }
                newRow.add(newCell);
            }
            newTable.add(newRow);
        }
        return newTable;
    }

    /**
     * Concatenate NFAs all together.
     *
     * @param nfas A set of NFAs.
     * @return NFA after concatenate.
     */
    public static NFA concat(NFA... nfas) {
        NFA result = new NFA();
        for (NFA nfa : nfas) {
            result.transitionTable.removeElementAt(result.accept);
            result.transitionTable.addAll(nfa.increasedStateNumber(result.accept));
            result.accept += nfa.accept;
        }
        return result;
    }

    /**
     * Parallel connect NFAs all together.
     *
     * @param nfas A set of NFAs.
     * @return NFA after parallel connection.
     */
    public static NFA or(NFA... nfas) {
        NFA result = new NFA();
        HashSet<Integer> starts = new HashSet<>();
        HashSet<Integer> accepts = new HashSet<>();
        int nextState = 1;
        for (NFA nfa : nfas) {
            starts.add(nextState);
            result.transitionTable.addAll(nfa.increasedStateNumber(nextState));
            accepts.add(nextState + nfa.accept);
            nextState += nfa.accept + 1;
        }
        addTransition(result.transitionTable.firstElement(), EPSILON, starts);
        result.transitionTable.add(initStateRow());
        result.accept = nextState;
        for (Integer state : accepts) {
            addTransition(result.transitionTable.elementAt(state), EPSILON, result.accept);
        }
        return result;
    }

    /**
     * Operation star(*).
     *
     * @param nfa NFA.
     * @return NFA after star.
     */
    public static NFA star(NFA nfa) {
        NFA result = new NFA();
        result.transitionTable.addAll(nfa.increasedStateNumber(1));
        result.transitionTable.add(initStateRow());
        result.accept += nfa.accept + 2;
        HashSet<Integer> startState = new HashSet<>();
        startState.add(1);
        startState.add(result.accept);
        addTransition(result.transitionTable.firstElement(), EPSILON, startState);
        HashSet<Integer> lastState = new HashSet<>();
        lastState.add(1);
        lastState.add(result.accept);
        addTransition(result.transitionTable.elementAt(result.accept - 1), EPSILON, lastState);
        return result;
    }

    /**
     * Operation plus(+).
     *
     * @param nfa NFA.
     * @return NFA after plus.
     */
    public static NFA plus(NFA nfa) {
        return concat(nfa, star(nfa));
    }

    /**
     * Operation dot(.).
     *
     * @return NFA with a dot.
     */
    public static NFA dot() {
        NFA result = new NFA(true);
        Vector<HashSet<Integer>> startStateRow = initStateRow();
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
        for (int i = 0; i < COLUMNS - 1; i++) {
            addTransition(startStateRow, (char) i, 1);
        }
        result.transitionTable.add(startStateRow);
        result.transitionTable.add(acceptStateRow);
        result.accept = 1;
        return result;
    }

    /**
     * Operation dash(from-to).
     *
     * @param from Left char.
     * @param to   Right char.
     * @return NFA with a dash.
     */
    public static NFA dash(char from, char to) throws Exception {
        return new NFA(from, to);
    }

    /**
     * Operation square brackets([]).
     *
     * @param chars A vector of characters brackets include in.
     * @return NFA with square brackets.
     */
    public static NFA square(Vector<Character> chars) {
        NFA result = new NFA(true);
        Vector<HashSet<Integer>> startStateRow = initStateRow();
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
        addTransition(startStateRow, chars, 1);
        result.transitionTable.add(startStateRow);
        result.transitionTable.add(acceptStateRow);
        result.accept = 1;
        return result;
    }

    /**
     * Operation not([^]).
     *
     * @param chars A vector of characters brackets include in.
     * @return NFA without transition of given characters.
     */
    public static NFA not(Vector<Character> chars) {
        NFA result = new NFA(true);
        Vector<HashSet<Integer>> startStateRow = initStateRow();
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
        for (char i = 0; i < COLUMNS; i++)
            if (!chars.contains(i))
                addTransition(startStateRow, i, 1);
        result.transitionTable.add(startStateRow);
        result.transitionTable.add(acceptStateRow);
        result.accept = 1;
        return result;
    }

    /**
     * Repeat NFA for times.
     *
     * @param nfa NFA.
     * @param min Minimum repeat time.
     * @param max Maximum repeat time.
     * @return Repeated NFA.
     */
    public static NFA repeat(NFA nfa, int min, int max) throws Exception {
        if (max >= min) {
            NFA result = new NFA();
            for (int i = 0; i < max; i++) result = concat(result, nfa);
            result.transitionTable.add(initStateRow());
            result.accept = nfa.accept * max + 1;
            for (int i = min; i <= max; i++) {
                result.addTransition(i * nfa.accept, EPSILON, result.accept);
            }
            return result;
        } else throw new Exception("Lex syntax error - In {x,y} x must be small than y");
    }

    /**
     * Get a table of NFA transition table.
     *
     * @return String of the message.
     */
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
                    if (!first) buffer.append(',');
                    buffer.append(transition);
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
