package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;

public class ReturnOp extends StatementOp {
    private ExprOp expr; // Opzionale

    public ReturnOp(ExprOp expr) {

        super("Return");

        this.expr = expr;

        super.add(Objects.requireNonNullElseGet(expr, () -> new DefaultMutableTreeNode("Null")));
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
