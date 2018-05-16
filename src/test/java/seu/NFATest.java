package seu;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Vector;

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

    @Test
    @Ignore
    public void dot() {
        NFA nfa1 = NFA.dot();
        System.out.println(nfa1.debugMessage());
    }

    @Test
    @Ignore
    public void square() {
        Vector<Character> characters = new Vector<>();
        characters.add('a');
        characters.add('1');
        characters.add((char) 0);
        characters.add(NFA.EPSILON);
        NFA nfa1 = NFA.square(characters);
        System.out.println(nfa1.debugMessage());
    }

    @Test
    @Ignore
    public void not() {
        Vector<Character> characters = new Vector<>();
        characters.add('a');
        characters.add('1');
        characters.add((char) 0);
        characters.add(NFA.EPSILON);
        NFA nfa1 = NFA.not(characters);
        System.out.println(nfa1.debugMessage());
    }

    @Test
    @Ignore
    public void repeat() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = NFA.repeat(nfa1, 3, 5);
        System.out.println(nfa2.debugMessage());
    }

    @Test
    @Ignore
    public void question() throws Exception {
        NFA nfa1 = new NFA('0', '3');
        NFA nfa2 = new NFA('3', '5');
        System.out.println(NFA.question(nfa1).debugMessage());
        System.out.println(NFA.question(nfa2).debugMessage());
        System.out.println(NFA.question(new NFA()).debugMessage());
    }
}