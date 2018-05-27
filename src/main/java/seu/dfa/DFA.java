package seu.dfa;

import seu.nfa.IntegratedNFA;
import seu.nfa.NFA;
import seu.nfa.NFAUtil;

import java.util.*;

import static seu.nfa.NFAUtil.transitionTableDebugMessage;

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

    private TreeSet<Integer> getClosureTableByNFA(IntegratedNFA nfa, int stateOfNFA) {
        TreeSet<Integer> v = new TreeSet<>();
        v.add(stateOfNFA);
        return getClosureTableByNFA(nfa, v);
    }

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

    private TreeSet<Integer> moveByNFA(IntegratedNFA nfa, TreeSet<Integer> statesOfNFA, Character ch) {
        TreeSet<Integer> in = getClosureTableByNFA(nfa, statesOfNFA);
        TreeSet<Integer> out = new TreeSet<>();
        for (Integer i : in) {
            out.addAll(nfa.transitionTable.elementAt(i).elementAt(ch));
        }
        return getClosureTableByNFA(nfa, out);
    }

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

    public void fillActionTable(IntegratedNFA nfa) {
        for (int i = 0; i < closureTable.size(); i++) {
            TreeSet<Integer> dfaStates = new TreeSet();
            dfaStates.addAll(closureTable.get(i));
            for (Integer i1 : dfaStates) {
                if (nfa.accept.get(i1) != null) {
                    acceptAction.put(i, nfa.accept.get(i1));
                    break;
                }
            }
        }
    }

    public String debugMessage() {
        return DFAUtil.transitionTableDebugMessage(transitionTable) + "accept: " + acceptAction.toString() + '\n';
    }


}
