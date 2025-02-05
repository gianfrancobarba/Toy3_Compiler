package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class WhileOp extends Node implements StatementOp {
    private ExprOp condition;
    private BodyOp body;

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

    @Override
    public String toString() {
        return "WhileOp{ " +
                "condition= " + condition +
                ", body= " + body +
                " }";
    }
}
