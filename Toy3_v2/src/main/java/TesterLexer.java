package main.java;

import toy3.sym;
import java_cup.runtime.Symbol;
import toy3.Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TesterLexer {
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length < 1) {
            System.err.println("Usage: java TesterLexer <input_file>");
            System.exit(1);
        }

        // Specifica il percorso del file di input
        File input = new File(System.getProperty("user.dir") + "/" + args[0]);
        Lexer lexicalAnalyzer = new Lexer(new FileReader(input));

        // Token definiti nella specifica JFlex
        final String[] tokenName = {"PROGRAM", "BEGIN", "END", "SEMI", "COLON", "COMMA", "DEF", "LPAR", "RPAR",
                "LBRAC", "RBRAC", "INT", "BOOL", "DOUBLE", "STRING", "CHAR", "TRUE", "FALSE", "RETURN",
                "IF", "THEN", "ELSE", "WHILE", "DO", "PLUS", "MINUS", "TIMES", "DIV", "ASSIGN", "ASSIGNDECL",
                "GT", "GE", "LT", "LE", "EQ", "NE", "NOT", "AND", "OR", "IN", "OUT", "OUTNL", "REF", "PIPE",
                "INT_CONST", "DOUBLE_CONST", "STRING_CONST", "CHAR_CONST", "ID"};

        Symbol token;
        try {
            while ((token = lexicalAnalyzer.next_token()) != null) {
                if (token.sym == sym.EOF) {
                    break;
                }

                if (token.value != null) {
                    System.out.println("<" + tokenName[token.sym - 2] + ", " + token.value + ">");
                } else {
                    System.out.println("<" + tokenName[token.sym - 2] + ">");
                }

            }
        } catch (Exception e) {
            System.err.println("Errore durante l'analisi lessicale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}