package main.nodes.common;

import main.nodes.expr.ExprOp;
import main.visitor.Visitor;
import main.visitor.Node;

public class Identifier extends Node implements ExprOp {

    private String lessema;

    public Identifier(String lessema){
        this.lessema = lessema;
    }

    public String getLessema() {
        return lessema;
    }

    public void setLessema(String lessema) {
        this.lessema = lessema;
    }

    public String toString() {return "identifier: " + lessema;}

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
