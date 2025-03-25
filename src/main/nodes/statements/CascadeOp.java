package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Node;
import main.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class CascadeOp extends Node implements StatementOp{
    private List<AssignmentCascadeOp> initAssignList;
    private ExprOp expr;
    private List<AssignmentCascadeOp> updateAssignList;
    private List<StatementOp> statementOpList;

    public CascadeOp(List<AssignmentCascadeOp> initAssignList, ExprOp expr, List<AssignmentCascadeOp> updateAssignList, List<StatementOp> statementOpList) {
        this.initAssignList = new ArrayList<>(initAssignList);
        this.expr = expr;
        this.updateAssignList = new ArrayList<>(updateAssignList);
        this.statementOpList = new ArrayList<>(statementOpList);
    }

    @Override
    public void setFunLabel(String funLabel) {

    }

    @Override
    public String getFunLabel() {
        return "";
    }

    @Override
    public String toString() {
        return "CascadeOp{" +
                "initAssignList=" + initAssignList +
                ", expr=" + expr +
                ", updateAssignList=" + updateAssignList +
                ", statementOpList=" + statementOpList +
                '}';
    }

    public List<AssignmentCascadeOp> getInitAssignList() {
        return initAssignList;
    }

    public void setInitAssignList(List<AssignmentCascadeOp> initAssignList) {
        this.initAssignList = initAssignList;
    }

    public ExprOp getExpr() {
        return expr;
    }

    public void setExpr(ExprOp expr) {
        this.expr = expr;
    }

    public List<AssignmentCascadeOp> getUpdateAssignList() {
        return updateAssignList;
    }

    public void setUpdateAssignList(List<AssignmentCascadeOp> updateAssignList) {
        this.updateAssignList = updateAssignList;
    }

    public List<StatementOp> getStatementOpList() {
        return statementOpList;
    }

    public void setStatementOpList(List<StatementOp> statementOpList) {
        this.statementOpList = statementOpList;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
