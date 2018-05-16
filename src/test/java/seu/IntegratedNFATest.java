package seu;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegratedNFATest {

    @Test
    @Ignore
    public void integrate() {
        IntegratedNFA integratedNFA = new IntegratedNFA();
        integratedNFA.integrate(NFAUtil.concat(new NFA('i'), new NFA('f')), "{Action A}");
        integratedNFA.integrate(NFAUtil.concat(new NFA('i'), NFAUtil.star(new NFA('f'))), "{Action B}");
        System.out.println(integratedNFA.debugMessage());
    }
}