package main.nodes.expr;

import main.nodes.common.Identifier;
import main.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunCallOp extends ExprOp{

    private Identifier id;
    private List<ExprOp> exprList; // argomenti della funzione

    public FunCallOp(Identifier id, List<ExprOp> exprList){

        super("FunCallOp");

        this.id = id;

        // Controllo null su exprList
        if (exprList == null) {
            this.exprList = new ArrayList<>(); // Lista vuota per evitare il NullPointerException
        } else {
            this.exprList = new ArrayList<>(exprList);
        }

        super.add(id);

        for (ExprOp par : this.exprList) { // Ora siamo sicuri che exprList non sia null
            super.add(par);
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

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {return super.toString();}
}
