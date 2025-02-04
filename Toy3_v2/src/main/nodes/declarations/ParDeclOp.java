package main.nodes.declarations;

import main.nodes.types.TypeOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class ParDeclOp extends DefaultMutableTreeNode {
    private List<PVarOp> pVars; // Lista di parametri (possono includere REF ID)
    private TypeOp type;

    public ParDeclOp(List<PVarOp> pVars, TypeOp type) {

        super("ParDeclOp");

        this.pVars = new ArrayList<>(pVars);
        this.type = type;

        for (PVarOp param : this.pVars) {
            super.add(param);
        }
        super.add(type);
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

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
