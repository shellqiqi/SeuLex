package seu.io

import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Ignore
import seu.dfa.DFA
import seu.dfa.DFAUtil
import seu.nfa.IntegratedNFA
import seu.nfa.NFA
import seu.nfa.NFAUtil
import java.util.*

class CodeFileUtilKtTest {

    companion object {
        lateinit var dfa: DFA
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val nfas = Vector<NFA>()
            nfas.add(NFAUtil.regExpToNFA("i*f"))
            nfas.add(NFAUtil.regExpToNFA("if*"))
            val infa = IntegratedNFA()
            infa.integrate(nfas[0], "printf(\"i*f\\n\");")
            infa.integrate(nfas[1], "printf(\"if*\\n\");")
            dfa = DFAUtil.integratedNFAtoDFA(infa)
        }
    }

    @Test
    @Ignore
    fun acceptFunctionsTest() {
        print(acceptFunctions(dfa))
    }

    @Test
    @Ignore
    fun transitionTableTest() {
        print(transitionTable(dfa))
    }

    @Test
    @Ignore
    fun actionMapTest() {
        print(actionMap(dfa))
    }

    @Test
    @Ignore
    fun generateTest() {
        print(generate(dfa))
    }
}