package main.nodes.expr;

import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class ExprOp extends DefaultMutableTreeNode {

    public ExprOp(String nodeName)
    {
        super(nodeName);
    }

    public void accept(ASTVisitor visitor) {
    }
}
