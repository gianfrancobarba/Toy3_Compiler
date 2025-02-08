package main.visitor;

import main.visitor.scoping.Scope;

public interface NodeInterface {
    void accept(Visitor visitor);

    Scope getScope();

    void setScope(Scope scope);

    void setType(String type);

    String getType();
}
