package main.nodes.types;

import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;

public class ConstOp extends ExprOp {

    private Object value;
    private String[] typeConst = {"int", "double", "char", "string", "bool"};

    public ConstOp(Object value){
        super("Const: "+ value.toString());
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

    public String toString() {return super.toString();}

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
