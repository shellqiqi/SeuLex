package seu.nfa;

import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class NFAUtil {

    /* Definition of ε */
    public final static char EPSILON = 128;
    /* Total columns of transition table */
    public final static int COLUMNS = 129;

    /**
     * Generate a NFA from a regular expression.
     *
     * @param regExp Regular expression.
     * @return A NFA.
     */
    public static NFA regExpToNFA(String regExp) throws Exception {
        Stack<Character> stack = new Stack<>();
        Vector<NFA> nfaQuene = new Vector<>();
        Vector<Character> opQuene = new Vector<>();

        final Character QUATE = 129,    //""
                        SQUARE = 130;   //[]
        for (int i = 0; i < regExp.length(); i++) {
            char ch = regExp.charAt(i);
            switch (ch) {
                case '\\':
                    i++;
                    if (regExp.charAt(i) == 'r')
                        stack.push('\r');
                    if (regExp.charAt(i) == 'n')
                        stack.push('\n');
                    if (regExp.charAt(i) == 't')
                        stack.push('\t');
                    stack.push(regExp.charAt(i));
                    break;
                case '"':
                    if (stack.search(QUATE) != -1) {
                        NFA nfa_s = new NFA();
                        while (!stack.peek().equals(QUATE)) {
                            nfa_s = concat(new NFA(stack.pop()), nfa_s);
                        }
                        stack.pop();
                        nfaQuene.add(nfa_s);

                    } else {
                        stack.push(QUATE);
                    }
                    break;
                case '[':
                    stack.push(SQUARE);
                    break;
                case ']':
                    Vector<Character> chs = new Vector<>();
                    if (stack.search(SQUARE) != -1) {
                        NFA nfa_g = null;
                        while (!stack.peek().equals(SQUARE)) {
                            Character topChar = stack.pop();
                            if(stack.peek().equals('-')){
                                stack.pop();
                                if(stack.peek().equals(SQUARE))
                                    chs.add(topChar,'-');
                                else
                                    if(nfa_g != null)
                                        nfa_g = or(new NFA(stack.pop(),topChar), nfa_g);
                                    else
                                        nfa_g = new NFA(stack.pop(), topChar);
                            }
                        }
                        stack.pop();
                        nfaQuene.add(nfa_g);

                    } else {
                        throw new  Exception("Lex syntax error - [] mismatch");
                    }
                    break;
                    //TODO: add other functional symbols.
                default:
                    stack.push(ch);
            }
        }
        if(nfaQuene.size() != 1)
            throw new Exception("Lex syntax error - Wrong regular expression"
                                + "with " +nfaQuene.size() + " nfa uncombined");
        return nfaQuene.elementAt(0);
    }


    /**
     * Init a state row with no transition.
     *
     * @return A state row.
     */
    public static Vector<HashSet<Integer>> initStateRow() {
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
    public static Vector<HashSet<Integer>> initStateRow(char ch, int transition) {
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
    public static Vector<HashSet<Integer>> initStateRow(char ch, HashSet<Integer> transitions) {
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
    public static Vector<HashSet<Integer>> initStateRow(Vector<Character> chars, int transition) {
        Vector<HashSet<Integer>> stateRow = initStateRow();
        addTransition(stateRow, chars, transition);
        return stateRow;
    }

    public static void addTransition(Vector<HashSet<Integer>> stateRow, char ch, int transition) {
        stateRow.elementAt(ch).add(transition);
    }

    public static void addTransition(Vector<HashSet<Integer>> stateRow, char ch, HashSet<Integer> transitions) {
        stateRow.elementAt(ch).addAll(transitions);
    }

    public static void addTransition(Vector<HashSet<Integer>> stateRow, Vector<Character> chars, int transition) {
        for (char ch : chars) addTransition(stateRow, ch, transition);
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
        NFA result = new NFA();
        for (int i = 0; i < COLUMNS - 1; i++) result.addTransition(0, (char) i, 1);
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
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
        NFA result = new NFA();
        result.addTransition(0, chars, 1);
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
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
        NFA result = new NFA();
        for (char i = 0; i < COLUMNS - 1; i++)
            if (!chars.contains(i))
                result.addTransition(0, i, 1);
        Vector<HashSet<Integer>> acceptStateRow = initStateRow();
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
     * Operation question mark(?).
     *
     * @param nfa NFA.
     * @return NFA with a question mark.
     */
    public static NFA question(NFA nfa) {
        NFA result = new NFA();
        result.transitionTable.addAll(nfa.increasedStateNumber(1));
        result.transitionTable.add(initStateRow());
        result.accept += nfa.accept + 2;
        HashSet<Integer> startState = new HashSet<>();
        startState.add(1);
        startState.add(result.accept);
        addTransition(result.transitionTable.firstElement(), EPSILON, startState);
        addTransition(result.transitionTable.elementAt(result.accept - 1), EPSILON, result.accept);
        return result;
    }

    public static String transitionTableDebugMessage(Vector<Vector<HashSet<Integer>>> transitionTable) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < transitionTable.size(); i++) {
            builder.append(i).append(": ");
            for (int ch = 0; ch < COLUMNS; ch++) {
                if (ch >= 32 && ch <= 126) {
                    builder.append('\'').append((char) ch).append('\'');
                } else if (ch == 128) {
                    builder.append("\'ε\'");
                } else {
                    builder.append(ch);
                }
                builder.append("[");
                boolean first = true;
                for (Integer transition : transitionTable.elementAt(i).elementAt(ch)) {
                    if (!first) builder.append(',');
                    builder.append(transition);
                    first = false;
                }
                builder.append("] ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
