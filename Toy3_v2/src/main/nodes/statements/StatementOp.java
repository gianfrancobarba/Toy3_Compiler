package main.nodes.statements;

import main.nodes.types.TypeOp;
import main.visitor.Visitor;

public interface StatementOp {
    TypeOp type = new TypeOp("notype");
    TypeOp getType();
    void setType(TypeOp type);
    void accept(Visitor visitor);
    void setFunLabel(String funLabel);
    String getFunLabel();
}
