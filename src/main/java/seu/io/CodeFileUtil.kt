package seu.io

import seu.dfa.DFA
import seu.dfa.DFAUtil

fun generate(dfa: DFA): String {
    val head = """
        #include <iostream>
        #include <string>
        #include <unordered_map>
        using namespace std;
        string yytext;
        unsigned int yylength;
        """.trimIndent()
    return head + '\n' +
            transitionTable(dfa) + '\n' +
            acceptFunctions(dfa) + '\n' +
            actionMap(dfa) + '\n' +
            yylex() + '\n'
}

fun acceptFunctions(dfa: DFA): String {
    val builder = StringBuilder()
    for (entry in dfa.acceptAction) {
        builder.append("""
            int accept${entry.key}() {
                ${entry.value}
                return 0;
            }
            """.trimIndent())
        builder.append('\n')
    }
    builder.append("""
        int acceptDefault() {
            cout << yytext << ":unknown" << endl;
            return 0;
        }
    """.trimIndent())
    return builder.toString()
}

fun actionMap(dfa: DFA): String {
    val builder = StringBuilder("unordered_map<int, int(*)()> actionMap = {")
    for (entry in dfa.acceptAction) {
        builder.append("\n\t{${entry.key},accept${entry.key}},")
    }
    builder.deleteCharAt(builder.lastIndex)
    builder.append("\n};")
    return builder.toString()
}

fun yylex(): String { // TODO: logic error here!!!
    return """
        int yylex(istream& in) {
            int state = 0;
            char nextChar;
            yytext = "";
            yylength = 0;
            int lastAccept = -1;
            unsigned int lastLength = 0;

            while (true) {
                nextChar = in.get();
                if (nextChar == EOF) return -1;
                state = transitionTable[state][nextChar];
                yytext.push_back(nextChar);
                yylength++;
                if (actionMap.find(state) != actionMap.end()) {
                    lastAccept = state;
                    lastLength = yylength;
                }
                if (state == -1) {
                    while (yylength > lastLength + (lastAccept == -1)) {
                        in.unget();
                        yytext.pop_back();
                        yylength--;
                    }
                    if (lastAccept == -1)
                        return acceptDefault();
                    else
                        return actionMap.at(lastAccept)();
                }
            }
            return 0;
        }
        """.trimIndent()
}

fun transitionTable(dfa: DFA): String {
    val builder = StringBuilder()
    builder.append("const int transitionTable[${dfa.transitionTable.size}][${DFAUtil.COLUMNS}] = {")
    for (row in dfa.transitionTable) {
        builder.append("\n\t")
        for (state in row) {
            builder.append(state ?: -1).append(',')
        }
    }
    builder.deleteCharAt(builder.lastIndex)
    builder.append("\n};")
    return builder.toString()
}