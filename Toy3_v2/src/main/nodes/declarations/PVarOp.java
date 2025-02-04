package main.nodes.declarations;
import main.nodes.common.Identifier;
import main.visitor.Visitor;
import main.visitor.Node;

public class PVarOp extends Node {

    private Identifier id;
    private boolean isRef;

    public PVarOp(Identifier id, boolean isRef) {
        this.id = id;
        this.isRef = isRef;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
