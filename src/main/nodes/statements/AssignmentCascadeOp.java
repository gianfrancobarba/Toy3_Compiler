package main.nodes.statements;

import main.nodes.common.Identifier;
import main.nodes.expr.ExprOp;
import main.visitor.Node;
import main.visitor.Visitor;

public class AssignmentCascadeOp extends Node implements StatementOp{
    private Identifier identifier;
    private ExprOp exprOp;

    @Override
    public String toString() {
        return "AssignmentCascadeOp{" +
                "identifier=" + identifier +
                ", exprOp=" + exprOp +
                '}';
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public ExprOp getExprOp() {
        return exprOp;
    }

    public void setExprOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    public AssignmentCascadeOp(Identifier identifier, ExprOp exprOp) {
        this.identifier = identifier;
        this.exprOp = exprOp;
    }

    @Override
    public void setFunLabel(String funLabel) {

    }

    @Override
    public String getFunLabel() {
        return "";
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
