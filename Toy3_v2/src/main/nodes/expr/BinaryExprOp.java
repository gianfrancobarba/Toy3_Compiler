package main.nodes.expr;
import main.visitor.ASTVisitor;

public class BinaryExprOp extends ExprOp {

    private ExprOp left;
    private ExprOp right;
    private String op;

    public BinaryExprOp(ExprOp left, String op, ExprOp right) {
        super("BinaryOp: " + op); // Inizializza il nodo con il nome dell'operatore
        super.add(left); // Aggiunge left come figlio di op
        super.add(right); // Aggiunge right come figlio di op

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

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
