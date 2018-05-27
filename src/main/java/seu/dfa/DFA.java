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
        closureTable.add(getClosureTableByNFA(nfa, NFA.start));
        while (closureTable.size() > transitionTable.size()) {
            Vector<TreeSet<Integer>> newClosureSets = fillRowOfTransitionTable(nfa, transitionTable.size());
            closureTable.addAll(newClosureSets);
        }

        fillActionTable(nfa);
    }

    /**
     * Get a closure of states which a NFA state can reach by 'ε's
     *
     * @param nfa a NFA
     * @param stateOfNFA an input state
     * @return output states
     */
    private TreeSet<Integer> getClosureTableByNFA(IntegratedNFA nfa, int stateOfNFA) {
        TreeSet<Integer> v = new TreeSet<>();
        v.add(stateOfNFA);
        return getClosureTableByNFA(nfa, v);
    }

    /**
     * Get a closure of states which some NFA states can reach by 'ε's
     *
     * @param nfa a NFA
     * @param statesOfNFA some input state
     * @return output states
     */
    private TreeSet<Integer> getClosureTableByNFA(IntegratedNFA nfa, TreeSet<Integer> statesOfNFA) {
        Stack<Integer> stack = new Stack<>();
        TreeSet<Integer> result = new TreeSet<>(statesOfNFA);
        stack.addAll(statesOfNFA);
        while (!stack.empty()) {
            int xState = stack.pop();
            TreeSet<Integer> middleSet = new TreeSet<>(nfa.transitionTable.elementAt(xState).elementAt(NFAUtil.EPSILON));
            for (Integer i : middleSet) {
                if (!stack.contains(i)) {
                    result.add(i);
                    stack.add(i);
                }
            }
        }
        return result;
    }

    /**
     * Get a closure of states which some NFA states can reach by a character and any 'ε's
     *
     * @param nfa a NFA
     * @param statesOfNFA input states
     * @param ch a character
     * @return output states
     */
    private TreeSet<Integer> moveByNFA(IntegratedNFA nfa, TreeSet<Integer> statesOfNFA, Character ch) {
        TreeSet<Integer> in = getClosureTableByNFA(nfa, statesOfNFA);
        TreeSet<Integer> out = new TreeSet<>();
        for (Integer i : in) {
            out.addAll(nfa.transitionTable.elementAt(i).elementAt(ch));
        }
        return getClosureTableByNFA(nfa, out);
    }

    /**
     * Fill a row of the transition table
     *
     * @param nfa a NFA
     * @param stateOfDFA a DFA state whose row in transition table needs to be added
     * @return new DFA states produced
     */
    private Vector<TreeSet<Integer>> fillRowOfTransitionTable(IntegratedNFA nfa, int stateOfDFA) {
        Vector<TreeSet<Integer>> newClosureSets = new Vector<>();
        Vector<Integer> rowOfTransitionTable = new Vector<>();

        for (char i = 0; i < DFAUtil.COLUMNS; i++) {
            TreeSet<Integer> dfaState = new TreeSet<>(moveByNFA(nfa, closureTable.get(stateOfDFA),i));
            if(dfaState.isEmpty()){
                rowOfTransitionTable.add(-1);
                continue;
            }
            // dfaState 是用来存经过i变换之后的，之后调用函数加上闭包整个的集合
            // 集合与之前的进行比较判断是否是新状态
            if (!closureTable.contains(dfaState) && !newClosureSets.contains(dfaState)) {
                rowOfTransitionTable.add(closureTable.size() + newClosureSets.size());
                newClosureSets.add(dfaState);
            } else if (newClosureSets.contains(dfaState)) {
                rowOfTransitionTable.add(closureTable.size() + newClosureSets.indexOf(dfaState));
            } else {
                rowOfTransitionTable.add(closureTable.indexOf(dfaState));
            }

        }
        transitionTable.add(rowOfTransitionTable);
        return newClosureSets;
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
