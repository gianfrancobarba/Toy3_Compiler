package main.nodes.common;

import main.nodes.expr.ExprOp;
import main.visitor.ASTVisitor;

public class Identifier extends ExprOp {

    private String lessema;

    public Identifier(String lessema){
        super("ID: " + lessema);
        this.lessema = lessema;
    }

    public String getLessema() {
        return lessema;
    }

    public String toString() {return super.toString();}

     public void accept(ASTVisitor visitor) {
            visitor.visit(this);
     }
}
