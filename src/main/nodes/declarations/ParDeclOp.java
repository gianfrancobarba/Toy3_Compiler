package main.nodes.declarations;
import main.visitor.Visitor;
import main.visitor.Node;
import java.util.ArrayList;
import java.util.List;

public class ParDeclOp extends Node {
    private List<PVarOp> pVars; // Lista di parametri (possono includere REF ID)
    private String parDeclType;

    public ParDeclOp(List<PVarOp> pVars, String parDeclType) {
        this.pVars = new ArrayList<>(pVars);
        this.parDeclType = parDeclType;
    }

    public List<PVarOp> getPVars() {
        return pVars;
    }


    public String getParDeclType(){
        return parDeclType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
