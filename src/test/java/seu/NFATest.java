package seu;

import org.junit.Test;

import static org.junit.Assert.*;

public class NFATest {

    @Test
    public void constructor() throws Exception {
        System.out.println(new NFA().debugMessage());
        System.out.println(new NFA('a').debugMessage());
        System.out.println(new NFA('a', 'z').debugMessage());
    }

}