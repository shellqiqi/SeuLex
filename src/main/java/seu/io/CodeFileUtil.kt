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
        char current;
        """.trimIndent()
    return head + '\n' +
            transitionTable(dfa) + '\n' +
            acceptFunctions(dfa) + '\n' +
            actionMap(dfa) + '\n' +
            yylex()
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
            current = in.get();
            yytext = "";
            if (current == EOF) return -1;
            while (transitionTable[state][current] != -1) {
                yytext.push_back(current);
                state = transitionTable[state][current];
                current = in.get();
            }
            if (actionMap.find(state) == actionMap.end()) {
                for (size_t i = 0; i < yytext.length(); i++)
                {
                    in.unget();
                }
                yytext.push_back(current);
                defaultAction();
            }
            else {
                in.unget();
                return actionMap.at(state)();
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