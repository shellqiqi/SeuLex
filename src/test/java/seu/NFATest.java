package seu;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class NFATest {

    @Test
    @Ignore
    public void constructor() throws Exception {
        System.out.println(new NFA().debugMessage());
        System.out.println(new NFA('a').debugMessage());
        System.out.println(new NFA('a', 'z').debugMessage());
    }

    @Test
    @Ignore
    public void increasedStateNumber() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = nfa1.increasedStateNumber(3);
        System.out.println(nfa1.debugMessage());
        System.out.println(nfa2.debugMessage());
    }

    @Test
    @Ignore
    public void concat() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        NFA nfa3 = NFA.concat(nfa1, nfa2);
        System.out.println(nfa3.debugMessage());
        nfa3 = NFA.concat(nfa3, nfa3);
        System.out.println(nfa3.debugMessage());
    }
}