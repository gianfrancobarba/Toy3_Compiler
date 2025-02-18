package main.visitor;
import main.visitor.scoping.Scope;

public abstract class Node {
    private String type = "notype";
    private Scope scope;

    public void accept(Visitor visitor) {}

    public Scope getScope() { return scope; }

    public void setScope(Scope scope) { this.scope = scope; }

    public String getType() {
        return type;
    }

    public void setType(String type) { this.type = type; }
}
