package main.nodes.types;

import main.visitor.Visitor;
import main.visitor.Node;

public class TypeOp extends Node {
    private String typeName;  // Nome del tipo (int, bool, double, etc.)

    public TypeOp(String typeName)
    {
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "TypeOp{ " +
                ", typeName= '" + typeName + '\'' +
                " }";
    }
}
