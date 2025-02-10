package main.nodes.statements;

import main.visitor.Visitor;

public interface StatementOp {
    String type = "notype";
    String getType();
    void setType(String type);
    void accept(Visitor visitor);
    void setFunLabel(String funLabel);
    String getFunLabel();
}
