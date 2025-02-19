package main.java;

import toy3.sym;
import toy3.Lexer;
import java_cup.runtime.Symbol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class TesterLexer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java TesterLexer <input_file>");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("File not found: " + inputFile.getAbsolutePath());
            System.exit(1);
        }

        try (Reader reader = new FileReader(inputFile)) {
            Lexer lexer = new Lexer(reader);
            Symbol token;

            System.out.println("Analisi lessicale del file: " + inputFile.getName() + "\n");

            while ((token = lexer.next_token()) != null) {
                if (token.sym == sym.EOF) {
                    System.out.println("<EOF>");
                    break;
                }

                String tokenName = sym.terminalNames[token.sym];

                if (token.value != null) {
                    System.out.printf("<%s, %s> (Linea: %d, Colonna: %d)%n",
                            tokenName, token.value, token.left + 1, token.right + 1);
                } else {
                    System.out.printf("<%s> (Linea: %d, Colonna: %d)%n",
                            tokenName, token.left + 1, token.right + 1);
                }
            }

            System.out.println("\nAnalisi lessicale completata!");

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Errore durante l'analisi lessicale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
