package main.nodes.expr;

import main.visitor.NodeInterface;
import main.visitor.Visitor;

public interface ExprOp extends NodeInterface {
    void accept(Visitor visitor);
}
