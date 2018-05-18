package seu.dfa;

import seu.nfa.IntegratedNFA;
import seu.nfa.NFA;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class DFA {

    /* The transition table of the DFA */
    public Vector<Vector<Integer>> transitionTable = new Vector<>();
    /* State to start */
    public int start = 0;
    /* State to accept and action */
    public HashMap<Integer, String> acceptAction = new HashMap<>();

    public DFA(IntegratedNFA nfa) {
        Vector<TreeSet<Integer>> closureTable = new Vector<>();

        closureTable.add(getClosureTableByNFA(NFA.start));

        while (closureTable.size() > transitionTable.size()) {
            Vector<TreeSet<Integer>> newClosureSets = fillRowOfTransitionTable(transitionTable.size());
            closureTable.addAll(newClosureSets);
        }

        fillActionTable(closureTable);
    }

    private TreeSet<Integer> getClosureTableByNFA(int stateOfNFA) {
        return null;
    }

    private TreeSet<Integer> getClosureTableByDFA(int stateOfDFA) {
        return null;
    }

    private Vector<TreeSet<Integer>> fillRowOfTransitionTable(int stateOfDFA) {
        Vector<TreeSet<Integer>> newClosureSets = new Vector<>();
        for (char i = 0; i < DFAUtil.COLUMNS; i++) {

        }
        return newClosureSets;
    }

    private void fillActionTable(Vector<TreeSet<Integer>> closureTable) {

    }
}
