package seu;

import org.javatuples.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class LexFileTest {

    private static LexFile lexFile;

    @BeforeClass
    public static void constructor() throws Exception {
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

    @Test
    public void readRegExps() {
        Vector<Pair<String, String>> expected = new Vector<>();
        expected.add(new Pair<>("{ws}", "{}"));
        expected.add(new Pair<>("if", "{return IF;}"));
        expected.add(new Pair<>("{id}", "{return ID;}"));
        expected.add(new Pair<>("{integer}", "{return INTEGER;}"));
        assertEquals(expected, lexFile.regExps);
    }

    @Test
    public void readUserSeg() {
        assertEquals("int main() {\n" +
                "    std::cout << \"hello world\" << std::endl;\n" +
                "}\n", lexFile.userSeg.toString());
    }
}