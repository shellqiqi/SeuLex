package seu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import static seu.NFAUtil.COLUMNS;

public class IntegratedNFA {

    /* The transition table of the NFA */
    public Vector<Vector<HashSet<Integer>>> transitionTable = new Vector<>();
    /* State to start always 0 */
    public final static int start = 0;
    /* State to accept with action */
    public HashMap<Integer, String> accept = new HashMap<>();

    public IntegratedNFA() {
        Vector<HashSet<Integer>> startStateRow = NFAUtil.initStateRow();
        transitionTable.add(startStateRow);
    }

    public void integrate(NFA nfa, String action) {
        NFAUtil.addTransition(transitionTable.firstElement(), NFAUtil.EPSILON, transitionTable.size());
        transitionTable.addAll(nfa.increasedStateNumber(transitionTable.size()));
        accept.put(transitionTable.size() - 1, action);
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
