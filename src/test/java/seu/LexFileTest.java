package seu;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class LexFileTest {

    private LexFile lexFile;

    @Before
    public void constructor() throws Exception {
        lexFile = new LexFile("resource/example.lex");
    }

    @Test
    public void readHeaders() {
        assertEquals("#include <iostream>\n" +
                "#define IF 5\n" +
                "#define ID 12\n" +
                "#define INTEGER 13\n", lexFile.headers.toString());
    }

    @Test
    public void readMacros() {
        Vector<Pair<String, String>> expected = new Vector<>();
        expected.add(new Pair<>("ws", "[ \\t\\n]+"));
        expected.add(new Pair<>("letter", "[A-Za-z]"));
        expected.add(new Pair<>("digit", "[0-9]"));
        expected.add(new Pair<>("id", "{letter}({letter}|{digit})*"));
        expected.add(new Pair<>("integer", "{digit}+"));
        assertEquals(expected, lexFile.macros);
    }
}