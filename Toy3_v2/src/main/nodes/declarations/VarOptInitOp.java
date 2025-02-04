package main.nodes.declarations;

import main.nodes.common.Identifier;
import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class VarOptInitOp extends DefaultMutableTreeNode {

    private Identifier id;
    private ExprOp exprOp;

    public VarOptInitOp(Identifier id, ExprOp exprOp) {

        super("VarOptInitOp");

        this.id = id;
        this.exprOp = exprOp;

        super.add(id);

        if(exprOp != null) {
            super.add(exprOp);
        }
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

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
