package main.visitor.tables;

import java.util.HashMap;
import java.util.Map;

public class BinaryOpTable {
    // Mappa principale: operatore -> mappa (Pair(tipo1, tipo2) -> tipo risultante)
    private static final Map<String, Map<Pair, String>> binaryTable = new HashMap<>();

    // Blocco statico per inizializzare la tabella
    static {
        addEntry("+", "int", "int", "int");
        addEntry("+", "double", "double", "double");
        addEntry("+", "int", "double", "double");
        addEntry("+", "double", "int", "double");
        addEntry("+", "string", "string", "string");
        addEntry("+", "string", "int", "string");
        addEntry("+", "int", "string", "string");
        addEntry("+", "string", "double", "string");
        addEntry("+", "double", "string", "string");
        addEntry("-", "int", "int", "int");
        addEntry("-", "double", "double", "double");
        addEntry("-", "int", "double", "double");
        addEntry("-", "double", "int", "double");
        addEntry("*", "int", "int", "int");
        addEntry("*", "double", "double", "double");
        addEntry("*", "int", "double", "double");
        addEntry("*", "double", "int", "double");
        addEntry("/", "int", "int", "double");
        addEntry("/", "double", "double", "double");
        addEntry("/", "int", "double", "double");
        addEntry("/", "double", "int", "double");
        addEntry("and", "bool", "bool", "bool");
        addEntry("or", "bool", "bool", "bool");
        addEntry(">", "int", "int", "bool");
        addEntry(">", "double", "double", "bool");
        addEntry(">", "int", "double", "bool");
        addEntry(">", "double", "int", "bool");
        addEntry("<", "int", "int", "bool");
        addEntry("<", "double", "double", "bool");
        addEntry("<", "int", "double", "bool");
        addEntry("<", "double", "int", "bool");
        addEntry(">=", "int", "int", "bool");
        addEntry(">=", "double", "double", "bool");
        addEntry(">=", "int", "double", "bool");
        addEntry(">=", "double", "int", "bool");
        addEntry("<=", "int", "int", "bool");
        addEntry("<=", "double", "double", "bool");
        addEntry("<=", "int", "double", "bool");
        addEntry("<=", "double", "int", "bool");
        addEntry("==", "int", "int", "bool");
        addEntry("==", "double", "double", "bool");
        addEntry("==", "int", "double", "bool");
        addEntry("==", "string", "string", "bool");
        addEntry("==", "double", "int", "bool");
        addEntry("==", "char", "char", "bool");
        addEntry("<>", "int", "int", "bool");
        addEntry("<>", "double", "double", "bool");
        addEntry("<>", "int", "double", "bool");
        addEntry("<>", "double", "int", "bool");
        addEntry("<>", "string", "string", "bool");
        addEntry("<>", "char", "char", "bool");
    }

    /*
     * Aggiunge una nuova regola alla tabella.
     *
     * @param operator L'operatore binario.
     * @param type1    Il tipo del primo operando.
     * @param type2    Il tipo del secondo operando.
     * @param result   Il tipo risultante.
     */
    private static void addEntry(String operator, String type1, String type2, String result) {
        // Se l'operatore non esiste nella mappa principale, creiamo una nuova mappa associata
        binaryTable.computeIfAbsent(operator, k -> new HashMap<>())
                .put(new Pair(type1, type2), result);
    }

    /*
     * Restituisce il tipo risultante di un'operazione binaria.
     *
     * @param operator L'operatore binario.
     * @param type1    Il tipo del primo operando.
     * @param type2    Il tipo del secondo operando.
     * @return Il tipo risultante, o null se la combinazione non è valida.
     */
    public static String getResult(String operator, String type1, String type2) {
        return binaryTable.getOrDefault(operator, new HashMap<>())
                .get(new Pair(type1, type2));
    }
}