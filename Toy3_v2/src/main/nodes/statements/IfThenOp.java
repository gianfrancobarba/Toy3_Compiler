package main.nodes.statements;
import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

public class IfThenOp extends StatementOp {
    private ExprOp condition;
    private BodyOp thenBranch;

    public IfThenOp(ExprOp condition, BodyOp thenBranch) {
        super("IfOp");
        this.condition = condition;
        this.thenBranch = thenBranch;

        super.add(condition);

        if(thenBranch != null) {
            super.add(thenBranch);
        }
    }

    public ExprOp getCondition() {
        return condition;
    }

    public BodyOp getThenBranch() {
        return thenBranch;
    }

    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}