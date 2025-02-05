package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

import java.util.ArrayList;
import java.util.List;

public class WriteOp extends Node implements StatementOp {
    private List<ExprOp> listExpr;
    private Character newLine; // 'null' per OUT, '\n' per OUTNL

    public WriteOp(List<ExprOp> listExpr, Character newLine) {
        this.listExpr = new ArrayList<>(listExpr);
        this.newLine = newLine;
    }

    public List<ExprOp> getExprList() {
        return listExpr;
    }

    public void setExprList(List<ExprOp> listExpr) {
        this.listExpr = listExpr;
    }

    public Character getNewLine() {
        return newLine;
    }

    public void setNewLine(Character newLine) {
        this.newLine = newLine;
    }

    public String toString() {
        return "WriteOp{ " +
                " listExpr=" + listExpr +
                ", newLine=" + newLine +
                '}';
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
