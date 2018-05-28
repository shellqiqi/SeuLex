package seu.dfa;

import seu.nfa.IntegratedNFA;
import seu.nfa.NFA;
import seu.nfa.NFAUtil;

import java.util.*;

public class DFA {

    /* The transition table of the DFA */
    public Vector<Vector<Integer>> transitionTable = new Vector<>();
    /* State to start */
    public int start = 0;
    /* State to accept and action */
    public HashMap<Integer, String> acceptAction = new HashMap<>();
    /* The closure table of dfa */
    public Vector<TreeSet<Integer>> closureTable = new Vector<>();

    public DFA(IntegratedNFA nfa) {
        closureTable.add(getFirstClosureTableByNFA(nfa));
        while (closureTable.size() > transitionTable.size()) {
            fillRowOfTransitionTable(nfa, transitionTable.size());
        }

        fillActionTable(nfa);
    }

    /**
     * Get a closure of states which a NFA state can reach by 'ε's
     *
     * @param nfa a NFA
     * @return output states
     */
    private TreeSet<Integer> getFirstClosureTableByNFA(IntegratedNFA nfa) {
        TreeSet<Integer> startSet = new TreeSet<>();
        startSet.add(NFA.start);
        return getClosureTableByNFA(nfa, startSet);
    }

    /**
     * Get a closure of states which some NFA states can reach by 'ε's
     *
     * @param nfa         a NFA
     * @param statesOfNFA some input state
     * @return output states
     */
    private TreeSet<Integer> getClosureTableByNFA(IntegratedNFA nfa, TreeSet<Integer> statesOfNFA) {
        Stack<Integer> unCalcStates = new Stack<>();
        TreeSet<Integer> result = new TreeSet<>(statesOfNFA);
        unCalcStates.addAll(statesOfNFA);
        while (!unCalcStates.empty()) {
            int unCalcState = unCalcStates.pop();
            TreeSet<Integer> closureOfUnCalcState = new TreeSet<>(nfa.transitionTable.elementAt(unCalcState).elementAt(NFAUtil.EPSILON));
            for (Integer state : closureOfUnCalcState) {
                if (!unCalcStates.contains(state)) {
                    result.add(state);
                    unCalcStates.add(state);
                }
            }
        }
        return result;
    }

    /**
     * Get a closure of states which some NFA states can reach by a character and any 'ε's
     *
     * @param nfa         a NFA
     * @param statesOfNFA input states
     * @param ch          a character
     * @return output states
     */
    private TreeSet<Integer> moveByNFA(IntegratedNFA nfa, TreeSet<Integer> statesOfNFA, Character ch) {
        TreeSet<Integer> out = new TreeSet<>();
        for (Integer i : statesOfNFA)
            out.addAll(nfa.transitionTable.elementAt(i).elementAt(ch));
        return getClosureTableByNFA(nfa, out);
    }

    /**
     * Fill a row of the transition table
     *
     * @param nfa        a NFA
     * @param stateOfDFA a DFA state whose row in transition table needs to be added
     * @return new DFA states produced
     */
    private void fillRowOfTransitionTable(IntegratedNFA nfa, int stateOfDFA) {
        Vector<Integer> rowOfTransitionTable = new Vector<>();

        for (char i = 0; i < DFAUtil.COLUMNS; i++) {
            TreeSet<Integer> dfaState = new TreeSet<>(moveByNFA(nfa, closureTable.get(stateOfDFA), i));//nfa states
            if (dfaState.isEmpty()) {
                rowOfTransitionTable.add(null);
                continue;
            }
            // dfaState 是用来存经过i变换之后的，之后调用函数加上闭包整个的集合
            // 集合与之前的进行比较判断是否是新状态
            if (!closureTable.contains(dfaState)) {
                rowOfTransitionTable.add(closureTable.size());
                closureTable.add(dfaState);
            } else
                rowOfTransitionTable.add(closureTable.indexOf(dfaState));
        }
        transitionTable.add(rowOfTransitionTable);
    }

    /**
     * Fill action table from NFA to DFA by connection of closureTable,
     * when a DFA state refers to more than one NFA accept state, take the one who come first.
     *
     * @param nfa a NFA
     */
    private void fillActionTable(IntegratedNFA nfa) {
        for (int i = 0; i < closureTable.size(); i++) {
            TreeSet<Integer> dfaStates = new TreeSet<>(closureTable.get(i));
            for (Integer i1 : dfaStates) {
                if (nfa.accept.get(i1) != null) {
                    acceptAction.put(i, nfa.accept.get(i1));
                    break;
                }
            }
        }
    }

    /**
     * Get a table of DFA transition table.
     *
     * @return String of the message.
     */
    public String debugMessage() {
        return DFAUtil.transitionTableDebugMessage(transitionTable) + "accept: " + acceptAction.toString() + '\n';
    }

}
