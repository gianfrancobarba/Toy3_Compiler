package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class ReturnOp extends Node implements StatementOp {
    private ExprOp expr; // Opzionale

    public ReturnOp(ExprOp expr) {
        this.expr = expr;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
