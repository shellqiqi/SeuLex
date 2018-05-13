package seu;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        readRegExps();
        readUserSeg();
    }

    private void readMacros() throws Exception {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) throw new Exception("Lex format error - miss macro definitions");
            else if (lineOfReader.startsWith("%%")) return;
            else if (lineOfReader.startsWith("%{")) readHeaders();
            else {
                int firstIndexOfWhiteSpace = lineOfReader.indexOf(' ');
                String macro = lineOfReader.substring(0, firstIndexOfWhiteSpace);
                String definition = lineOfReader.substring(firstIndexOfWhiteSpace).trim();
                macros.add(new Pair<>(macro, definition));
            }
        }
    }

    private void readHeaders() throws Exception {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) throw new Exception("Lex format error - miss another \"%}\"");
            if (lineOfReader.startsWith("%}")) return;
            else headers.append(lineOfReader).append("\n");
        }
    }

    private void readRegExps() throws Exception {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) throw new Exception("Lex format error - miss user segment");
            else if (lineOfReader.startsWith("%%")) return;
            else {
                int firstIndexOfWhiteSpace = lineOfReader.indexOf(' ');
                String regExp = lineOfReader.substring(0, firstIndexOfWhiteSpace);
                String action = lineOfReader.substring(firstIndexOfWhiteSpace).trim();
                regExps.add(new Pair<>(regExp, action));
            }
        }
    }

    private void readUserSeg() throws IOException {
        while (true) {
            String lineOfReader = reader.readLine();
            if (lineOfReader == null) {
                System.out.println("EOF");
                return;
            } else {
                userSeg.append(lineOfReader).append("\n");
            }
        }
    }
}
