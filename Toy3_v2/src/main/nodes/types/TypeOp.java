package main.nodes.types;

import main.visitor.ASTVisitor;
import javax.swing.tree.DefaultMutableTreeNode;

public class TypeOp extends DefaultMutableTreeNode {
    private String typeName;  // Nome del tipo (int, bool, double, etc.)

    public TypeOp(String typeName)
    {
        super("Type: "+ typeName); // Inizializza il nodo AST con il nome del tipo
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TypeOp other = (TypeOp) obj;
        return this.typeName.equals(other.typeName);
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
