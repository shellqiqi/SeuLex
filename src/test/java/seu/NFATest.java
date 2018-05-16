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
    public void concat() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        NFA nfa3 = NFA.concat(nfa1, nfa2);
        System.out.println(nfa3.debugMessage());
        nfa3 = NFA.concat(nfa3, nfa3);
        System.out.println(nfa3.debugMessage());
    }

    @Test
    @Ignore
    public void or() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        NFA nfa3 = NFA.or(nfa1, nfa2);
        System.out.println(nfa3.debugMessage());
        nfa3 = NFA.or(nfa3, nfa3);
        System.out.println(nfa3.debugMessage());
    }

    @Test
    @Ignore
    public void star() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        System.out.println(NFA.star(nfa1).debugMessage());
        System.out.println(NFA.star(nfa2).debugMessage());
        System.out.println(NFA.star(new NFA()).debugMessage());
    }

    @Test
    @Ignore
    public void plus() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        System.out.println(NFA.plus(nfa1).debugMessage());
        System.out.println(NFA.plus(nfa2).debugMessage());
        System.out.println(NFA.plus(new NFA()).debugMessage());
    }
}