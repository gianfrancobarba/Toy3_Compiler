package main.nodes.expr;

import main.nodes.common.Identifier;
import main.nodes.statements.StatementOp;
import main.visitor.Visitor;
import main.visitor.Node;

import java.util.ArrayList;
import java.util.List;

public class FunCallOp extends Node implements ExprOp, StatementOp {

    private Identifier id;
    private List<ExprOp> exprList; // argomenti della funzione

    public FunCallOp(Identifier id, List<ExprOp> exprList){
        this.id = id;

        // Controllo null su exprList
        if (exprList == null) {
            this.exprList = new ArrayList<>(); // Lista vuota per evitare il NullPointerException
        } else {
            this.exprList = new ArrayList<>(exprList);
        }
    }


    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public List<ExprOp> getExprList() {
        return exprList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
