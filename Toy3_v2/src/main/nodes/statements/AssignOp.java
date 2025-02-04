package main.nodes.statements;

import main.visitor.ASTVisitor;
import main.nodes.expr.ExprOp;
import main.nodes.common.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AssignOp extends StatementOp {
    private List<Identifier> listId;
    private List<ExprOp> listExpr;


    public AssignOp(List<Identifier> listId, List<ExprOp> listExpr) {
        super("AssignOp");
        this.listId = new ArrayList<>(listId);
        this.listExpr = new ArrayList<>(listExpr);

        for (Identifier id : listId) {
            super.add(id);
        }
        
        for (ExprOp expr : listExpr) {
            super.add(expr);
        }
    }

    public List<Identifier> getIdentfiers() {
        return listId;
    }

    public void setIdentifiers(List<Identifier> listId) {
        this.listId = listId;
    }

    public List<ExprOp> getExpressions() {
        return listExpr;
    }

    public void setExpressions(List<ExprOp> listExpr) {
        this.listExpr = listExpr;
    }
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
