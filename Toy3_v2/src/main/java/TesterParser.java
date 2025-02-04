package main.java;

import toy3.Lexer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import toy3.parser;

public class TesterParser {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            System.err.println("Usage: java TesterParser <absolute_path_to_input_file>");
            System.exit(1);
        }

        File input = new File(args[0]);

        if (!input.exists() || !input.isFile()) {
            System.err.println("File non trovato: " + args[0]);
            System.exit(1);
        }

        parser p = new parser(new Lexer(new FileReader(input)));
//        try {
//            // Avvia il parsing
//            p.debug_parse();
//            System.out.println("Parsing completato senza errori!");
//        } catch (Exception e) {
//            System.err.println("Errore durante il parsing:");
//            e.printStackTrace();
//        }

        JTree tree;

        try {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
            tree = new JTree(root);

            JFrame framePannello = new JFrame("Parser Tree");
            framePannello.setSize(600, 600);
            framePannello.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JScrollPane treeView = new JScrollPane(tree);
            framePannello.add(treeView);
            framePannello.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante il parsing: " + e.getMessage());
            System.exit(1);
        }

    }
}
