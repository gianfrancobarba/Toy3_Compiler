package main.nodes.statements;
import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

public class IfThenElseOp extends StatementOp {
    private ExprOp condition;
    private BodyOp thenBranch;
    private BodyOp elseBranch;

    public IfThenElseOp(ExprOp condition, BodyOp thenBranch, BodyOp elseBranch) {

        super("IfThenElseOp");

        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;

        super.add(condition);

        if(thenBranch != null) {
            super.add(thenBranch);
        }

        if(elseBranch != null) {
            super.add(elseBranch);
        }

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
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}