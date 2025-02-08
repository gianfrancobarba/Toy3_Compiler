package main.visitor.typechecking;

import main.nodes.declarations.*;
import main.nodes.expr.FunCallOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.BodyOp;
import main.nodes.statements.IfThenElseOp;
import main.nodes.statements.IfThenOp;
import main.nodes.statements.WhileOp;
import main.visitor.Visitor;
import main.visitor.scoping.SymbolTable;

public class TypeChecking implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();

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
        whileOp.getCondition().accept(this);
        whileOp.getBody().accept(this);
        // controllo che il type della condizione sia bool
        if(whileOp.getCondition().getType().equals("bool")
            && whileOp.getBody().getType().equals("notype")){
            whileOp.setType("notype");
        }
        else {
            System.err.print("Invalid type in While statement");
            System.exit(1);
        }
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {

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
    public void visit(VarDeclOp varDeclOp) {}
}