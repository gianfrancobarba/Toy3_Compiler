package main.nodes.statements;

import main.nodes.common.Identifier;
import main.nodes.expr.ExprOp;
import main.nodes.expr.FunCallOp;
import main.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunCallOpStat extends StatementOp {
    private Identifier id;
    private List<ExprOp> exprList;

    public FunCallOpStat(FunCallOp funCall) {

        super("FunCallOpStat");

        this.id = funCall.getId();
        this.exprList = new ArrayList<>(funCall.getExprList());
        super.add(id);

        for (ExprOp expr : this.exprList) {
            super.add(expr);
        }
    }

    public Identifier getId() {
        return id;
    }

    public List<ExprOp> getExprList() {
        return exprList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
