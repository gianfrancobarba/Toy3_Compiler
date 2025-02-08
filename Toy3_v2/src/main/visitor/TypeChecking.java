package main.visitor;

import main.nodes.declarations.*;
import main.nodes.expr.FunCallOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.BodyOp;
import main.nodes.statements.IfThenElseOp;
import main.nodes.statements.IfThenOp;
import main.nodes.statements.WhileOp;
import main.visitor.scoping.SymbolTable;

public class TypeChecking implements Visitor {

    SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(VarDeclOp varDeclOp) {

    }

    @Override
    public void visit(IfThenOp ifThenOp) {

    }

    @Override
    public void visit(BodyOp bodyOp) {

    }

    @Override
    public void visit(FunDeclOp funDeclOp) {

    }

    @Override
    public void visit(FunCallOp funCallOp) {

    }

    @Override
    public void visit(ProgramOp programOp) {

    }

    @Override
    public void visit(WhileOp whileOp) {

    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {

    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {

    }

    @Override
    public void visit(BeginEndOp beginEndOp) {

    }

    @Override
    public void visit(PVarOp pVarOp) {}

    @Override
    public void visit(ParDeclOp parDeclOp) {}
}
