package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.nodes.common.Identifier;
import main.visitor.Node;

import java.util.ArrayList;
import java.util.List;

public class AssignOp extends Node implements StatementOp {
    private List<Identifier> listId;
    private List<ExprOp> listExpr;


    public AssignOp(List<Identifier> listId, List<ExprOp> listExpr) {
        this.listId = new ArrayList<>(listId);
        this.listExpr = new ArrayList<>(listExpr);
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
        return "AssignOp{ " +
                "listId= " + listId +
                ", listExpr= " + listExpr +
                " }";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
