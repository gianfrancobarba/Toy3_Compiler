package main.visitor.type_checking;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.ExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.visitor.Visitor;
import main.visitor.scoping.SymbolTable;

import java.util.ArrayList;

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
    public void visit(AssignOp assignOp) {
        ArrayList<ExprOp> exprList = (ArrayList<ExprOp>) assignOp.getExpressions();
        ArrayList<Identifier> idList = (ArrayList<Identifier>) assignOp.getIdentfiers();

        // Non si puo fare un assegnamento di una chiamata a funzione in un multiple assign
        if(exprList.size() > 1){
            for(ExprOp exprOp : exprList){
                if(exprOp instanceof FunCallOp){
                    System.err.println("Cannot assign a function call to a variable in a multiple assign statement");
                    System.exit(1);
                }
            }
        }

        // #Identifiers == #expressions negli assegnamenti, altrimenti errore
        if(idList.size() != exprList.size()){
            System.err.println("Number of identifiers and expressions do not match in assignment");
            System.exit(1);
        }
    }

    @Override
    public void visit(PVarOp pVarOp) {}

    @Override
    public void visit(VarDeclOp varDeclOp) {}
}