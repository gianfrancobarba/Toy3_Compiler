package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class WriteOp extends StatementOp {
    private List<ExprOp> listExpr;
    private Character newLine; // 'null' per OUT, '\n' per OUTNL

    public WriteOp(List<ExprOp> listExpr, Character newLine) {

        super("WriteOp");

        this.listExpr = new ArrayList<>(listExpr);
        this.newLine = newLine;

        // Aggiungiamo i nodi figli per la visualizzazione grafica

        for (ExprOp expr : listExpr) {
            super.add(expr);
        }

        if (newLine != null) {
            super.add(new DefaultMutableTreeNode("OUTNL"));
        }
        else {
            super.add(new DefaultMutableTreeNode("OUT"));
        }
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
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
