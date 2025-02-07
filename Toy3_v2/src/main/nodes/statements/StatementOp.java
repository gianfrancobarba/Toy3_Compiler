package main.nodes.statements;

import main.visitor.Visitor;

public interface StatementOp {

    void accept(Visitor visitor);
    void setFunLabel(String funLabel);
    String getFunLabel();
}
