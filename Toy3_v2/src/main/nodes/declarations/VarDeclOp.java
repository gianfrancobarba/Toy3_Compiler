package main.nodes.declarations;
import main.visitor.ASTVisitor;
import main.nodes.expr.ExprOp;
import main.nodes.types.TypeOp;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class VarDeclOp extends DefaultMutableTreeNode {
    private List<VarOptInitOp> listVarOptInit;
    private Object typeOrConstant;

    public VarDeclOp(List<VarOptInitOp> listVarOptInit, Object typeOrConstant) {

        super("VarDeclOp");

        this.listVarOptInit = new ArrayList<>(listVarOptInit);
        this.typeOrConstant = typeOrConstant;

        for (VarOptInitOp varOptInit : this.listVarOptInit) {
            super.add(varOptInit);
        }
        if (typeOrConstant instanceof TypeOp) {
            super.add((TypeOp) typeOrConstant);
        } else if (typeOrConstant instanceof ExprOp) {
            super.add((ExprOp) typeOrConstant);
        }
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
            super.add(varOptInit);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}