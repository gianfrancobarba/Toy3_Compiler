package main.nodes.declarations;
import main.nodes.common.Identifier;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class PVarOp extends DefaultMutableTreeNode {

    private Identifier id;
    private boolean isRef;

    public PVarOp(Identifier id, boolean isRef) {

        super("PVarOp");

        this.id = id;
        this.isRef = isRef;

        super.add(id);

        if(isRef) {
            super.add(new DefaultMutableTreeNode("Ref"));
        }
    }

    public Identifier getId() {
        return id;
    }

    public boolean isRef() {
        return isRef;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
