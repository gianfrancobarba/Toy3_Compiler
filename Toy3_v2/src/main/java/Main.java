package main.java;

import main.nodes.program.ProgramOp;
import main.visitor.*;
import main.visitor.scoping.Scoping;
import toy3.Lexer;
import toy3.parser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {
        try (Reader fileReader = new BufferedReader(new FileReader("fileTester/input.inp"))) {
            Lexer lexer = new Lexer(fileReader);
            parser p = new parser(lexer);
            try {
                // Esegui il parsing per costruire l'AST
                ProgramOp ast = (ProgramOp) p.parse().value;

                // Applica il Visitor all'AST
                System.out.println("** Stampa dell'AST **");
                PrintVisitor printVisitor = new PrintVisitor();
                ast.accept(printVisitor);

                // Test the Scoping visitor
                System.out.println("** Testing Scoping Visitor **");
                Scoping scopingVisitor = new Scoping();
                ast.accept(scopingVisitor);
//
//                System.out.println("** Testing TypeChecking Visitor **");
//                TypeChecking typeChecking = new TypeChecking();
//                ast.accept(typeChecking);
//
//                System.out.println("** Testing Code Generation **");
//                CodeGenerator codeGenerator = new CodeGenerator();
//                ast.accept(codeGenerator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

