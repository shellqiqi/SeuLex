package seu.dfa;

import org.junit.Ignore;
import org.junit.Test;
import seu.nfa.IntegratedNFA;
import seu.nfa.NFA;
import seu.nfa.NFAUtil;

import java.util.Vector;

public class DFATest {

    @Test
    @Ignore
    public void integratedNFAtoDFA() throws Exception {
        Vector<NFA> nfas = new Vector<>();
        nfas.add(NFAUtil.regExpToNFA("\"if\""));
        nfas.add(NFAUtil.regExpToNFA("iff*"));
        nfas.add(NFAUtil.regExpToNFA("[a-zA-Z_]?\\\"(\\\\.|[^\\\\\"])*\\\""));
        IntegratedNFA infa = new IntegratedNFA();
        //infa.integrate(nfas.get(0), "if");
        //infa.integrate(nfas.get(1), "iff*");
        infa.integrate(nfas.get(2), "string");
        DFA dfa = DFAUtil.integratedNFAtoDFA(infa);
        System.out.println(infa.debugMessage());
        System.out.println(dfa.debugMessage());
    }

}