package main.nodes.expr;

import main.visitor.Visitor;

public interface ExprOp {
    String type = "notype";
    String getType();
    void setType(String type);
    void accept(Visitor visitor);
}
