package seu.dfa;

import seu.nfa.IntegratedNFA;

import java.util.HashSet;
import java.util.Vector;

public class DFAUtil {

    /* Total columns of transition table */
    public final static int COLUMNS = 128;

    public static DFA integratedNFAtoDFA(IntegratedNFA nfa){
        return new DFA(nfa);
    }

    /**
     * Minimize a DFA
     *
     * @param dfa a DFA to be minimized
     */
    public void minimizeDFA(DFA dfa){
        //TODO: Finish the function
    }

    public static String transitionTableDebugMessage(Vector<Vector<Integer>> transitionTable) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < transitionTable.size(); i++) {
            builder.append(i).append(": ");
            for (int ch = 0; ch < COLUMNS; ch++) {
                if (ch >= 32 && ch <= 126) {
                    builder.append('\'').append((char) ch).append('\'');
                } else {
                    builder.append(ch);
                }
                builder.append("[");
                if(transitionTable.elementAt(i).elementAt(ch) != null)
                    builder.append(transitionTable.elementAt(i).elementAt(ch));
                builder.append("] ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
