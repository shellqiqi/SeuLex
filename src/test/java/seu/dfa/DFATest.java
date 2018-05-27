package seu.dfa;


import org.junit.Ignore;
import org.junit.Test;
import seu.nfa.IntegratedNFA;
import seu.nfa.NFA;
import seu.nfa.NFAUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class DFATest {

    @Test
    @Ignore
    public void getClosureTableByNFA() throws Exception {
        IntegratedNFA nfa = new IntegratedNFA();
        nfa.integrate(NFAUtil.regExpToNFA("[a-zA-Z_]?\\\"(\\\\.|[^\\\\\"])*\\\""), "");
        TreeSet t = DFA.getClosureTableByNFA(nfa, 0);
        System.out.println(nfa.debugMessage());
        System.out.println(t.toString());
    }

}