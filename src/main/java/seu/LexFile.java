package seu;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class LexFile {

    private BufferedReader reader;

    /* Definitions start with %{ and end with %} */
    public StringBuffer headers = new StringBuffer();
    /* Macros with regular definition */
    public Vector<Pair<String, String>> macros = new Vector<>();
    /* Regular expressions with action */
    public Vector<Pair<String, String>> regExps = new Vector<>();
    /* User segment */
    public StringBuffer userSeg = new StringBuffer();

    public LexFile(String filePath) throws Exception {
        reader = new BufferedReader(new FileReader(filePath));
        readMacros();
    }

    private void readMacros() throws Exception {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) {
                System.out.println("EOF");
                return;
            } else if (lineOfReader.startsWith("%%")) return;
            else if (lineOfReader.startsWith("%{")) readHeaders();
            else ;//TODO: add regular definition pairs
        }
    }

    private void readHeaders() throws Exception {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) throw new Exception("Lex format error");
            if (lineOfReader.startsWith("%}")) return;
            headers.append(lineOfReader).append("\n");
        }
    }

    private void readRegExps() {
        //TODO
    }

    private void readUserSeg() {
        //TODO
    }
}
