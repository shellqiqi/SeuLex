package seu.dfa;

import javafx.util.Pair;
import seu.nfa.IntegratedNFA;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class DFAUtil {

    /* Total columns of transition table */
    public final static int COLUMNS = 128;

    public static DFA integratedNFAtoDFA(IntegratedNFA nfa) {
        return new DFA(nfa);
    }

    /**
     * Minimize a DFA
     *
     * @param dfa a DFA to be minimized
     */
    public static DFA minimizeDFA(DFA dfa) {
        Set<Integer> acceptSet = dfa.acceptAction.keySet();
        HashSet<Integer> allSet = new HashSet<>();
        for (int i = 0; i < dfa.transitionTable.size(); i++) {
            allSet.add(i);
        }
        int divide = 1;
        class Divider<T> {
            private Vector<Set<T>> deviderSets = new Vector<>();

            private Divider(Set<T> set, Predicate<T> filter) {
                deviderSets.add(set);
                devideAt(0, filter);
            }

            private int devideAt(int i, Predicate<T> filter) {
                Pair<Set<T>, Set<T>> pair = devide(deviderSets.get(i), filter);
                if (pair.getKey().isEmpty() || pair.getValue().isEmpty()) return -1;
                deviderSets.set(i, pair.getKey());
                deviderSets.add(pair.getValue());
                return size() - 1;
            }

            private int size() {
                return deviderSets.size();
            }

            private boolean equal(Vector<T> set1, Vector<T> set2) {
                if (set1.size() != set2.size()) return false;
                for (int i = 0; i < set1.size(); i++) {
                    if (getIndex(set1.get(i)) != getIndex(set2.get(i)))
                        return false;
                }
                return true;
            }

            private int getIndex(T element) {
                AtomicInteger index = new AtomicInteger(-1);
                deviderSets.forEach(set -> {
                    if (set.contains(element)) index.set(deviderSets.indexOf(set));
                });
                return index.get();
            }

            private Pair<Set<T>, Set<T>> devide(Set<T> set, Predicate<T> filter) {
                HashSet<T> v1 = new HashSet<>(set);
                v1.removeIf(filter);
                HashSet<T> v2 = new HashSet<>(set);
                v2.removeAll(v1);
                return new Pair<>(v1, v2);
            }
        }
        Divider<Integer> statesDivider = new Divider<>(allSet, acceptSet::contains);

        while (divide < statesDivider.size()) {
            divide = statesDivider.size();
            for (int i = 0; i < statesDivider.size(); i++) {
                int state = (int) statesDivider.deviderSets.get(i).toArray()[0];
                while (statesDivider.devideAt(i, s ->
                        statesDivider.equal(dfa.transitionTable.get(state), dfa.transitionTable.get(s))) > 0) {
                }
            }
        }
        return deleteStatesDuplicated(dfa, statesDivider.deviderSets);
    }


    public static DFA deleteStatesDuplicated(DFA dfa, Vector<Set<Integer>> deviderSets) {
        /* Key is state of origin dfa, value is a corresponding state of new dfa */
        HashMap<Integer, Integer> toReplace = new HashMap<>();

        int index = 0;
        for (Set<Integer> set : deviderSets) {
            for (Integer i : set) {
                toReplace.put(i, index);
            }
            index++;
        }

        /* States of origin dfa which we use to generate a new dfa */
        ArrayList<Integer> statesRemained = new ArrayList<>(toReplace.values());
        int start = toReplace.get(DFA.start);
        //TODO: fix the logical problem
        Vector<Vector<Integer>> newTable = new Vector<>();
        HashMap<Integer, String> newAction = new HashMap<>();
        for (Integer state : statesRemained) {
            Vector<Integer> newRow = new Vector<>();
            for (Integer cell : dfa.transitionTable.get(state)) {
                if (cell != null) newRow.add(toReplace.get(cell));
                else newRow.add(null);
            }
            newTable.add(newRow);
            if (dfa.acceptAction.containsKey(state))
                newAction.put(toReplace.get(state), dfa.acceptAction.get(state));
        }
        return new DFA(newTable, newAction, start);
    }

    private static void deleteStatesDuplicated(DFA dfa) {

    }

    public static String transitionTableDebugMessage(Vector<Vector<Integer>> transitionTable) {
        return transitionTableDebugMessage(transitionTable, false);
    }

    public static String transitionTableDebugMessage(Vector<Vector<Integer>> transitionTable, boolean verbose) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < transitionTable.size(); i++) {
            builder.append(i).append(": ");
            for (int ch = 0; ch < COLUMNS; ch++) {
                if (!verbose)
                    if (transitionTable.elementAt(i).elementAt(ch) == null)
                        continue;
                if (ch >= 32 && ch <= 126) {
                    builder.append('\'').append((char) ch).append('\'');
                } else {
                    builder.append(ch);
                }
                builder.append("[");
                if (transitionTable.elementAt(i).elementAt(ch) != null)
                    builder.append(transitionTable.elementAt(i).elementAt(ch));
                builder.append("] ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
