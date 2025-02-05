package main.nodes.program;

import main.nodes.declarations.VarDeclOp;
import main.nodes.statements.StatementOp;
import main.visitor.Visitor;
import main.visitor.Node;

import java.util.ArrayList;
import java.util.List;

public class BeginEndOp extends Node {
    private List<VarDeclOp> varDeclList;
    private List<StatementOp> stmtList;

    public BeginEndOp(List<VarDeclOp> varDeclList, List<StatementOp> stmtList) {
        this.varDeclList = new ArrayList<>(varDeclList);
        this.stmtList = new ArrayList<>(stmtList);
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
        return "BeginEndOp{ " +
                "varDeclList= " + varDeclList +
                ", stmtList= " + stmtList +
                '}';
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
