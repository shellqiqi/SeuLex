package seu.io;

import seu.dfa.DFA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeFile {

    private BufferedWriter writer;
    private LexFile lexFile;
    private DFA dfa;

    public CodeFile(LexFile lexFile, DFA dfa) {
        this.lexFile = lexFile;
        this.dfa = dfa;
    }

    public void writeFile(String filePath) throws IOException {
        writer = new BufferedWriter(new FileWriter(filePath));
        writeHeaders();
        writer.write(new CodeFileUtil(dfa).generate());
        writeUserSeg();
        writer.close();
    }

    private void writeHeaders() throws IOException {
        writer.write(lexFile.headers.toString());
    }

    private void writeUserSeg() throws IOException {
        writer.write(lexFile.userSeg.toString());
    }

}
