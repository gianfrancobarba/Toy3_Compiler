package main.nodes.statements;

import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

public class WhileOp extends StatementOp {
    private ExprOp condition;
    private BodyOp body;

    public WhileOp(ExprOp condition, BodyOp body) {

        super("WhileOp");

        this.condition = condition;
        this.body = body;

        // Aggiungiamo i nodi figli per la visualizzazione grafica
        super.add(condition);

        if(body != null) {
            super.add(body);
        }
    }

    public ExprOp getCondition() {
        return condition;
    }

    public BodyOp getBody() {
        return body;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
