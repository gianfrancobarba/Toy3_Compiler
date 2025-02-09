package main.nodes.expr;

import main.nodes.types.TypeOp;
import main.visitor.Visitor;

public interface ExprOp {
    TypeOp type = new TypeOp("notype");
    TypeOp getType();
    void setType(TypeOp type);
    void accept(Visitor visitor);
}
