package main.nodes.expr;
import main.visitor.Visitor;
import main.visitor.Node;

public class UnaryExprOp extends Node implements ExprOp {

    private ExprOp expr;
    private String op;

    public UnaryExprOp(String op, ExprOp expr) {
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

    @Override
    public String toString() {
        return "UnaryExprOp{" +
                "expr=" + expr +
                ", op='" + op + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor){ visitor.visit(this); }
}
