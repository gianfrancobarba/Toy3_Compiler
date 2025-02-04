package main.nodes.declarations;

import main.nodes.types.TypeOp;
import main.visitor.Visitor;
import main.visitor.Node;
import java.util.ArrayList;
import java.util.List;

public class ParDeclOp extends Node {
    private List<PVarOp> pVars; // Lista di parametri (possono includere REF ID)
    private TypeOp type;

    public ParDeclOp(List<PVarOp> pVars, TypeOp type) {
        this.pVars = new ArrayList<>(pVars);
        this.type = type;
    }

    public List<PVarOp> getPVars() {
        return pVars;
    }

    public TypeOp getType() {
        return type;
    }

    public String toString() {
        return super.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
