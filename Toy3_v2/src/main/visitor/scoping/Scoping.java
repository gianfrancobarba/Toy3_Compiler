package main.visitor.scoping;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.BinaryExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.expr.UnaryExprOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.visitor.Visitor;

public class Scoping implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();


    @Override
    public void visit(UnaryExprOp unaryExprOp) {

    }

    @Override
    public void visit(Identifier identifier) {

    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {

    }

    @Override
    public void visit(VarDeclOp varDeclOp) {

    }

    @Override
    public void visit(AssignOp assignOp) {

    }

    @Override
    public void visit(IfThenOp ifThenOp) {

    }

    @Override
    public void visit(BodyOp bodyOp) {
        symbolTable.enterScope();
        bodyOp.setScope(symbolTable.getCurrentScope());
        if(bodyOp.getVarDecls() != null){
            for(VarDeclOp varDeclOp : bodyOp.getVarDecls()){
                varDeclOp.accept(this);
            }
        }
        if(bodyOp.getStatements() != null){
            for(StatementOp statOp : bodyOp.getStatements()){
                statOp.accept(this);
            }
        }
        System.out.println("Scope in BodyOp: ");
        symbolTable.printTable();
        symbolTable.exitScope();
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
    public void visit(ReturnOp returnOp) {

    }

    @Override
    public void visit(WhileOp whileOp) {

    }

    @Override
    public void visit(ParDeclOp parDeclOp) {

    }

    @Override
    public void visit(ReadOp readOp) {

    }

    @Override
    public void visit(WriteOp writeOp) {

    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {

    }

    @Override
    public void visit(PVarOp pVarOp) {

    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {

    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        symbolTable.enterScope();
        beginEndOp.setScope(symbolTable.getCurrentScope());
        if(beginEndOp.getVarDeclList() != null){
            for(VarDeclOp varDeclOp : beginEndOp.getVarDeclList()){
                varDeclOp.accept(this);
            }
        }
        if(beginEndOp.getStmtList() != null){
            for(StatementOp statOp : beginEndOp.getStmtList()){
                statOp.accept(this);
            }
        }
        System.out.println("Scope in BeginEndOp: ");
        symbolTable.printTable();
        symbolTable.exitScope();
    }
}
