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
        nfas.add(NFAUtil.regExpToNFA("i*f"));
        nfas.add(NFAUtil.regExpToNFA("if*"));
        IntegratedNFA infa = new IntegratedNFA();
        infa.integrate(nfas.elementAt(0), "printf(\"i*f\\n\");");
        infa.integrate(nfas.elementAt(1), "printf(\"if*\\n\");");
        DFA dfa = DFAUtil.integratedNFAtoDFA(infa);
        System.out.println(infa.debugMessage());
        System.out.println(dfa.debugMessage());
    }

    @Test
    @Ignore
    public void minimizeDFATest1() throws Exception {
        Vector<NFA> nfas = new Vector<>();
        nfas.add(NFAUtil.regExpToNFA("i*f"));
        nfas.add(NFAUtil.regExpToNFA("if*"));
        IntegratedNFA infa = new IntegratedNFA();
        infa.integrate(nfas.elementAt(0), "printf(\"i*f\\n\");");
        infa.integrate(nfas.elementAt(1), "printf(\"if*\\n\");");
        DFA dfa = DFAUtil.integratedNFAtoDFA(infa);
        System.out.println(dfa.debugMessage());

        DFAUtil.minimizeDFA(dfa);
    }

    @Test
    @Ignore
    public void minimizeDFATest2() throws Exception {
        Vector<NFA> nfas = new Vector<>();
        nfas.add(NFAUtil.regExpToNFA("(a|b)*abb"));
        IntegratedNFA infa = new IntegratedNFA();
        infa.integrate(nfas.elementAt(0), "accept");
        DFA dfa = DFAUtil.integratedNFAtoDFA(infa);
        System.out.println(dfa.debugMessage());

        DFAUtil.minimizeDFA(dfa);
    }

}