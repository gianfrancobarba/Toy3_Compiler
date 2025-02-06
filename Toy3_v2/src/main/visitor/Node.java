package main.visitor;

import main.visitor.scoping.Scope;

public abstract class Node{
    private String type = "notype";
    private Scope scope;

    public void accept(Visitor visitor) {
    }

    public void setScope(Scope currentScope){
        this.scope = currentScope;
    }
}
