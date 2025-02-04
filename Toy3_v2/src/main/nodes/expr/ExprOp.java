package main.nodes.expr;

import main.visitor.Visitor;

public interface ExprOp {

    void accept(Visitor visitor);
}
