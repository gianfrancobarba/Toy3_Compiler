package main.visitor.tables;

import java.util.HashMap;
import java.util.Map;

public class UnaryOpTable {
    private static final Map<String, Map<String, String>> unaryTable = new HashMap<>();

    static {
        addEntry("-", "int", "int");
        addEntry("-", "double", "double");
        addEntry("not", "bool", "bool");
    }

    private static void addEntry(String operator, String operand, String result) {
        unaryTable.computeIfAbsent(operator, k -> new HashMap<>()).put(operand, result);
    }

    public static String getResult(String operator, String operand) {
        return unaryTable.getOrDefault(operator, Map.of()).get(operand);
    }
}
