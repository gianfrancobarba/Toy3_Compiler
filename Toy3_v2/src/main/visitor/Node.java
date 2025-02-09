package main.visitor;

import main.nodes.expr.ExprOp;
import main.nodes.statements.StatementOp;
import main.nodes.types.TypeOp;
import main.visitor.scoping.Scope;

public abstract class Node {
    private TypeOp type = new TypeOp("notype");
    private Scope scope;


    public void accept(Visitor visitor) {}

    public Scope getScope() { return scope; }

    public void setScope(Scope scope) { this.scope = scope; }

    public TypeOp getType() {
        return type;
    }

    public void setType(TypeOp type) { this.type = type; }
}
