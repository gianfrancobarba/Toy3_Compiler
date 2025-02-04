package main.nodes.statements;

import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class StatementOp extends DefaultMutableTreeNode {

    public StatementOp(String nodeName) {
        super(nodeName);
    }

    public void accept(ASTVisitor visitor) {    }
}