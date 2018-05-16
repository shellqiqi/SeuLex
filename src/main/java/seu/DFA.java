package seu;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Vector;

public class DFA {
    /* Total columns of transition table */
    public final static int COLUMNS = 128;
    /* The transition table of the DFA */
    public Vector<Vector<Integer>> transitionTable = new Vector<>();
    /* State to start */
    public int start = 0;
    /* State to accept and action */
    public HashMap<Integer, String> acceptAction = new HashMap<>();

    public DFA(Vector<Pair<NFA, String>> regExps) {
    }
}
