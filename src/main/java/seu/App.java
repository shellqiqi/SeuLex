package seu;

import seu.dfa.DFA;
import seu.dfa.DFAUtil;
import seu.io.CodeFile;
import seu.io.LexFile;
import seu.nfa.IntegratedNFA;

public class App {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Please input a file.");
                return;
            }
            LexFile lexFile = new LexFile(args[0]);
            IntegratedNFA nfa = new IntegratedNFA(lexFile.regExps);
            DFA dfa = new DFA(nfa);
            dfa = DFAUtil.minimizeDFA(dfa);
            CodeFile codeFile = new CodeFile(lexFile, dfa);
            codeFile.writeFile(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
