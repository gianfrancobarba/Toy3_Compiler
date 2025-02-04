package main.nodes.declarations;

import main.nodes.common.Identifier;
import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class VarOptInitOp extends Node {

    private Identifier id;
    private ExprOp exprOp;

    public VarOptInitOp(Identifier id, ExprOp exprOp) {
        this.id = id;
        this.exprOp = exprOp;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public ExprOp getExprOp() {
        return exprOp;
    }

    public void setExprOp(ExprOp exprOp) {
        this.exprOp = exprOp;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
