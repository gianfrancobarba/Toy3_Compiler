package main.nodes.program;

import main.nodes.declarations.VarDeclOp;
import main.nodes.statements.StatementOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class BeginEndOp extends DefaultMutableTreeNode {
    private List<VarDeclOp> varDeclList;
    private List<StatementOp> stmtList;

    public BeginEndOp(List<VarDeclOp> varDeclList, List<StatementOp> stmtList) {

        super("BeginEndOp");
        this.varDeclList = new ArrayList<>(varDeclList);
        this.stmtList = new ArrayList<>(stmtList);

        for (VarDeclOp varDeclOp : this.varDeclList) {
            super.add(varDeclOp);
        }

        for (StatementOp statOp : this.stmtList) {
            super.add(statOp);
        }

    }

    public List<VarDeclOp> getvarDeclList() {
        return varDeclList;
    }

    public void setvarDeclList(List<VarDeclOp> varDeclList) {
        this.varDeclList = varDeclList;
    }

    public List<StatementOp> getStmtList() {
        return stmtList;
    }

    public void setStmtList(List<StatementOp> statOps) {
        this.stmtList = statOps;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
