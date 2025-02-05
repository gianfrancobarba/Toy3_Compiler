package main.nodes.statements;
import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class IfThenElseOp extends Node implements StatementOp {
    private ExprOp condition;
    private BodyOp thenBranch;
    private BodyOp elseBranch;

    public IfThenElseOp(ExprOp condition, BodyOp thenBranch, BodyOp elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ExprOp getCondition() {
        return condition;
    }

    public BodyOp getThenBranch() {
        return thenBranch;
    }

    public BodyOp getElseBranch() {
        return elseBranch;
    }

    public boolean hasElseBranch() {
        return elseBranch != null;
    }

    public String toString() {
        return "IfThenElseOp{" +
                "condition=" + condition +
                ", thenBranch=" + thenBranch +
                ", elseBranch=" + elseBranch +
                '}';
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}