package main.nodes.statements;

import main.visitor.NodeInterface;
import main.visitor.Visitor;

public interface StatementOp extends NodeInterface {

    void accept(Visitor visitor);
    void setFunLabel(String funLabel);
    String getFunLabel();
}
