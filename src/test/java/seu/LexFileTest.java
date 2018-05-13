package seu;

import org.junit.Before;
import org.junit.Test;

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
}