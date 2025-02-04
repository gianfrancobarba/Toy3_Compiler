package main.nodes.expr;

import main.visitor.ASTVisitor;

public class UnaryExprOp extends ExprOp {

    private ExprOp expr;
    private String op;

    public UnaryExprOp(String op, ExprOp expr) {
        super("UnaryOp: " + op);
        super.add(expr);

        this.expr=expr;
        this.op=op;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public String getOp() {
        return op;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
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
