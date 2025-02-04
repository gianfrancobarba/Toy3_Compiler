package main.test;

import main.nodes.program.ProgramOp;
import main.visitor.PrintVisitor;
import toy3.Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import toy3.parser;


public class TestVisitor {

    public static void main(String[] args) {
        try (Reader fileReader = new BufferedReader(new FileReader("fileTester/valid4/valid4.txt"))) {
            Lexer lexer = new Lexer(fileReader);
            parser p = new parser(lexer);
            try {
                // Esegui il parsing per costruire l'AST
                ProgramOp ast = (ProgramOp) p.parse().value;

                if(ast == null) {
                    System.out.println("AST is null");
                    return;
                }

                // Applica il Visitor all'AST
                System.out.println("** Stampa dell'AST **");
                PrintVisitor printVisitor = new PrintVisitor();
                ast.accept(printVisitor);

//                // Test the Scoping visitor
//                System.out.println("** Testing Scoping Visitor **");
//                Scoping scopingVisitor = new Scoping();
//                ast.accept(scopingVisitor);
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