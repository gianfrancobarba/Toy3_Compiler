package main.visitor.type_checking;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.ExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.TypeOp;
import main.visitor.Visitor;
import main.visitor.scoping.SymbolTable;

import java.util.ArrayList;
import java.util.Objects;

public class TypeChecking implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(IfThenOp ifThenOp) {
        ifThenOp.getCondition().accept(this);
        ifThenOp.getThenBranch().accept(this);
        if(ifThenOp.getCondition().getType().getTypeName().equals("bool") && ifThenOp.getThenBranch().getType().getTypeName().equals("notype"))
            ifThenOp.setType(new TypeOp("notype"));
        else{
            System.err.println("Invalid types in If-Then statement");
            System.exit(1);
        }
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
        if(whileOp.getCondition().getType().getTypeName().equals("bool")
            && whileOp.getBody().getType().getTypeName().equals("notype")){
            whileOp.setType(new TypeOp("notype"));
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
        ifThenElseOp.getCondition().accept(this);
        ifThenElseOp.getThenBranch().accept(this);
        ifThenElseOp.getElseBranch().accept(this);
        if(ifThenElseOp.getCondition().getType().getTypeName().equals("bool")
            && ifThenElseOp.getThenBranch().getType().getTypeName().equals("notype")
            && ifThenElseOp.getElseBranch().getType().getTypeName().equals("notype")){
            ifThenElseOp.setType(new TypeOp("notype"));
        }
        else{
            System.err.println("Invalid types in If-Then-Else statement");
            System.exit(1);
        }
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

        // Visita che crea la SymbolTable
        for(Identifier id : idList)
            id.accept(this);
        for(ExprOp expr : exprList)
            expr.accept(this);

        for(Identifier id : idList){
            String idType = id.getType().getTypeName();
            String exprType = String.valueOf(exprList.get(idList.indexOf(id)).getType()); // verificare funzionamento
            if(!Objects.equals(idType,exprType)){
                System.err.print("ERROR: Conflicting types in assignment: id "+ id.getLessema());
                System.err.print(" has type " + idType + " but expression has type "+ exprType);
                System.exit(1);
            }
        }

    }

    @Override
    public void visit(PVarOp pVarOp) {}

    @Override
    public void visit(VarDeclOp varDeclOp) {}
}