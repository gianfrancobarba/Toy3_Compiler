package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class WhileOp extends Node implements StatementOp {
    private ExprOp condition;
    private BodyOp body;
    private String funLabel;

    public WhileOp(ExprOp condition, BodyOp body) {
        this.condition = condition;
        this.body = body;
    }

    public ExprOp getCondition() {
        return condition;
    }

    public BodyOp getBody() {
        return body;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setFunLabel(String funLabel) {
        this.funLabel = funLabel;
    }

    public String getFunLabel() {
        return funLabel;
    }

    @Override
    public String toString() {
        return "WhileOp{ " +
                "condition= " + condition +
                ", body= " + body +
                " }";
    }
}
