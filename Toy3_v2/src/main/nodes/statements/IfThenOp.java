package main.nodes.statements;
import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class IfThenOp extends Node implements StatementOp {
    private ExprOp condition;
    private BodyOp thenBranch;

    public IfThenOp(ExprOp condition, BodyOp thenBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
    }

    public ExprOp getCondition() {
        return condition;
    }

    public BodyOp getThenBranch() {
        return thenBranch;
    }

    public String toString() {
        return "IfThenOp{ " +
                "condition= " + condition +
                ", thenBranch= " + thenBranch +
                " }";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}