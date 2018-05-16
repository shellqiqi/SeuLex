package seu;

import java.util.HashMap;
import java.util.Vector;

public class DFA {

    /* The transition table of the DFA */
    public Vector<Vector<Integer>> transitionTable = new Vector<>();
    /* State to start */
    public int start = 0;
    /* State to accept and action */
    public HashMap<Integer, String> acceptAction = new HashMap<>();

    public DFA(IntegratedNFA nfa) {
    }
}
