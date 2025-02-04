package main.nodes.statements;

import main.nodes.declarations.VarDeclOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class BodyOp extends DefaultMutableTreeNode {
    private List<VarDeclOp> varDecls;
    private List<StatementOp> listStatement;

    public BodyOp(List<VarDeclOp> varDecls, List<StatementOp> listStatement) {

        super("BodyOp");

        this.varDecls = new ArrayList<>(varDecls);
        this.listStatement = new ArrayList<>(listStatement);

        for (VarDeclOp var : this.varDecls) {
            super.add(var);
        }

        for (StatementOp stmt : this.listStatement) {
            super.add(stmt);
        }
    }

    public BodyOp() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public List<VarDeclOp> getVarDecls() {
        return varDecls;
    }

    public List<StatementOp> getStatements() {
        return listStatement;
    }

    public void addVarDecl(VarDeclOp var) {
        if (var != null) {
            varDecls.add(var);
            super.add(var);
        }
    }

    public void addListVar(List<VarDeclOp> vars) {
        if (vars != null) {
            for (VarDeclOp var : vars) {
                addVarDecl(var);
            }
        }
    }

    public void addStatement(StatementOp statement) {
        if (statement != null) {
            listStatement.add(statement);
            super.add(statement);
        }
    }

    public void addStatements(List<StatementOp> listStatement) {
        if (listStatement != null) {
            for (StatementOp stmt : listStatement) {
                addStatement(stmt);
            }
        }
    }

    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
