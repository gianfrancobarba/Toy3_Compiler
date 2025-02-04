package main.nodes.expr;

import main.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.List;
public class ExprListOp extends ExprOp {
    private final List<ExprOp> exprList; // Lista di espressioni

    public ExprListOp() {
        super("ExprList");
        this.exprList = new ArrayList<>();
    }

    public ExprListOp(List<ExprOp> exprList) {
        super("ExprList");
        for (ExprOp expr : exprList) {
            super.add(expr);
        }
        this.exprList = exprList;
    }

    public void addExpr(ExprOp expr) {
        exprList.add(expr);
        super.add(expr);
    }

    public List<ExprOp> getExprList() {
        return exprList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
