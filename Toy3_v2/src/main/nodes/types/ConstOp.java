package main.nodes.types;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class ConstOp extends Node implements ExprOp {

    private Object value;
    private String[] typeConst = {"int", "double", "char", "string", "bool"};

    public ConstOp(Object value){
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    public String getConstantType() {
        if (this.getValue() instanceof Integer) {
            return typeConst[0];
        } else if (this.getValue() instanceof Double) {
            return typeConst[1];
        } else if (this.getValue() instanceof Character) {
            return typeConst[2];
        } else if (this.getValue() instanceof String) {
            return typeConst[3];
        } else if (this.getValue() instanceof Boolean) {
            return typeConst[4];
        }
        return null;
    }

    public String toString() {
        return "Const{ " +
                ", value= " + value +
                " }";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
