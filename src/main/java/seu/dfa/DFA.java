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
    public  HashMap<Integer, String> acceptAction = new HashMap<>();
    /* The closure table of dfa */
    public  Vector<TreeSet<Integer>> closureTable = new Vector<>();

    public DFA(IntegratedNFA nfa) {
        closureTable.add(getClosureTableByNFA(nfa, NFA.start));
        while (closureTable.size() > transitionTable.size()) {
            Vector<TreeSet<Integer>> newClosureSets = fillRowOfTransitionTable(nfa, transitionTable.size());
            closureTable.addAll(newClosureSets);
        }

        fillActionTable(nfa);
    }

    public static TreeSet<Integer> getClosureTableByNFA(IntegratedNFA nfa, int stateOfNFA) {
        TreeSet<Integer> result = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        result.add(0);
        stack.push(0);
        while (!stack.empty()) {
            int xState = stack.pop();
            TreeSet<Integer> middleSet = new TreeSet<>();
            middleSet.addAll(nfa.transitionTable.elementAt(xState).elementAt(NFAUtil.EPSILON));
            for (Integer i : middleSet
                    ) {
                if (!stack.contains(i)) {
                    result.add(i);
                    stack.add(i);
                }
            }
        }

        return result;
    }

    /*private TreeSet<Integer> getClosureTableByDFA(int number) {
        return null;
    }*/

    private Vector<TreeSet<Integer>> fillRowOfTransitionTable(IntegratedNFA nfa, int stateOfDFA) {
        Vector<TreeSet<Integer>> newClosureSets = new Vector<>();
        //上面这个不知道是啥
        TreeSet<Integer> middle = new TreeSet();

        for (char i = 0; i < DFAUtil.COLUMNS; i++) {
            middle.addAll(nfa.transitionTable.elementAt(stateOfDFA).elementAt(i));
            for (Integer i1 : middle) {
                middle.addAll(getClosureTableByNFA(nfa, i1));
            }
            // middle 是用来存经过i变换之后的，之后调用函数加上闭包整个的集合
            // 集合与之前的进行比较判断是否是新状态
            while (!middle.equals(closureTable))
                closureTable.add(middle);
        }
        return newClosureSets;
    }

    public  void fillActionTable(IntegratedNFA nfa) {
        for(int i = 0; i < closureTable.size(); i++) {
            TreeSet<Integer> dfaStates = new TreeSet();
            dfaStates.addAll(closureTable.get(i));
            for (Integer i1 : dfaStates
                    ) {
                if(!nfa.accept.get(i1).equals(null)) {
                    acceptAction.put(i, nfa.accept.get(i1));
                    break;
                }
            }
        }
    }

}
