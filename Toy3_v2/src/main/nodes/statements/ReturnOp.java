package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class ReturnOp extends Node implements StatementOp {
    private ExprOp expr; // Opzionale
    private String funLabel;

    public ReturnOp(ExprOp expr) {
        this.expr = expr;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public void setFunLabel(String funLabel) {
        this.funLabel = funLabel;
    }

    public String getFunLabel() {
        return funLabel;
    }

    @Override
    public String toString() {
        if (expr == null) {
            return "ReturnOp{}";
        } else {
            return "ReturnOp{ " +
                    ", expr= " + expr +
                    " }";
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
