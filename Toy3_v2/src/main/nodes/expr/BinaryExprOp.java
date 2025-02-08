package main.nodes.expr;
import main.visitor.Visitor;
import main.visitor.Node;

public class BinaryExprOp extends Node implements ExprOp {

    private ExprOp left;
    private ExprOp right;
    private String op;

    public BinaryExprOp(ExprOp left, String op, ExprOp right) {
        this.left=left;
        this.right=right;
        this.op=op;
    }

    public ExprOp getLeft() {
        return left;
    }

    public ExprOp getRight() {
        return right;
    }

    public String getOp() {
        return op;
    }

    public void setLeft(ExprOp left) {
        this.left = left;
    }

    public void setRight(ExprOp right) {
        this.right = right;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String toString() {return super.toString();}
}
