package main.nodes.declarations;

import main.visitor.Visitor;
import main.visitor.Node;
import java.util.ArrayList;
import java.util.List;

public class VarDeclOp extends Node {
    private List<VarOptInitOp> listVarOptInit;
    private Object typeOrConstant;

    public VarDeclOp(List<VarOptInitOp> listVarOptInit, Object typeOrConstant) {
        this.listVarOptInit = new ArrayList<>(listVarOptInit);
        this.typeOrConstant = typeOrConstant;
    }

    public List<VarOptInitOp> getListVarOptInit() {
        return listVarOptInit;
    }

    public Object getTypeOrConstant() {
        return typeOrConstant;
    }

    public void addVarOptInit(VarOptInitOp varOptInit) {
        if (varOptInit != null) {
            listVarOptInit.add(varOptInit);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}