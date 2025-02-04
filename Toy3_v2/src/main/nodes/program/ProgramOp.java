
package main.nodes.program;

import main.nodes.declarations.VarDeclOp;
import main.nodes.declarations.FunDeclOp;
import main.nodes.statements.BodyOp;
import main.nodes.statements.StatementOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class ProgramOp extends DefaultMutableTreeNode {

    private List<Object> listDecls;
    private BeginEndOp beginEndOp;

    public ProgramOp(List<Object> listDecls, BeginEndOp beginEndOp) {

        super("ProgramOp");

        this.listDecls = new ArrayList<>(listDecls);
        this.beginEndOp = beginEndOp;

        for (Object decl : listDecls) {
            if (decl instanceof VarDeclOp) {
                super.add((VarDeclOp) decl);
            } else if (decl instanceof FunDeclOp) {
                super.add((FunDeclOp) decl);
            }
        }

        if (beginEndOp != null) {
            super.add(beginEndOp);
        }
    }

    public List<Object> getListDecls() {
        return listDecls;
    }

    public BeginEndOp getBeginEndOp() {
        return beginEndOp;
    }

    public void setBeginEndOp(BeginEndOp beginEndOp) {
        this.beginEndOp = beginEndOp;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
