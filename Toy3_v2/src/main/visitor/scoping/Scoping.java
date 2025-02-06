package main.visitor.scoping;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.BinaryExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.expr.UnaryExprOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.ConstOp;
import main.nodes.types.TypeOp;
import main.visitor.Visitor;
import java.util.StringJoiner;

public class Scoping implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();
    private int indentLevel = 0;
    private String tempType;

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
        if (varDeclOp.getTypeOrConstant() instanceof ConstOp) {
            if (varDeclOp.getListVarOptInit().size() > 1) {
                System.err.print("Error: cannot declare multiple variables with a constant type");
                System.exit(1);
            }

            // se non restituisce il primo errore allora sicuramente la lista avrà al più un elemento.
            VarOptInitOp varOpt = varDeclOp.getListVarOptInit().get(0);
            if(varOpt.getExprOp() != null){
                System.err.print("Error: ");
                System.err.print(varOpt.getId().getLessema() + " ");
                System.err.print("is a constant and cannot be initialized");
                System.exit(1);
            }
        }

        if (varDeclOp.getListVarOptInit() != null)
            varDeclOp.getListVarOptInit().forEach(varOptInitOp -> {
                if (varDeclOp.getTypeOrConstant() instanceof String type)
                    tempType = type;
                else if (varDeclOp.getTypeOrConstant() instanceof ConstOp constant)
                    tempType = constant.getConstantType();
                varOptInitOp.accept(this);
            });
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
        String funId = funDeclOp.getId().getLessema();
        if(symbolTable.probe(Kind.FUN, funId)){
            System.err.print("Function "+ funId + " already declared with type: "+symbolTable.lookup(Kind.FUN, funId));
            System.exit(1);
        }

        String signature = functionSignature(funDeclOp);
        // aggiungiamo la firma del metodo in Globals
        symbolTable.addId(Kind.FUN, funId, signature);

        // nuovo scope per il body della funzione
        symbolTable.enterScope();
        funDeclOp.setScope(symbolTable.getCurrentScope());
        // ridefinizione delle variabili prese in input (se presenti)
        if(funDeclOp.getParams() != null){
            for(ParDeclOp parDecl : funDeclOp.getParams()){
                parDecl.accept(this);
            }
        }
        if(funDeclOp.getBody().getVarDecls() != null){
            for(VarDeclOp varDecl : funDeclOp.getBody().getVarDecls()){
                varDecl.accept(this);
            }
        }
        if(funDeclOp.getBody().getStatements() != null){
            for(StatementOp stmt : funDeclOp.getBody().getStatements()){
                stmt.accept(this);
            }
        }

        System.out.println("Scope in DefDeclOp: ");
        symbolTable.printTable();
        symbolTable.exitScope();
    }

    private String functionSignature(FunDeclOp funDeclOp) {
        StringBuilder sb = new StringBuilder();
        String type;
        if (funDeclOp.getOptType() != null) {
            type = funDeclOp.getOptType().getTypeName();
            sb.append(type);
        } else {
            sb.append("void");
        }
        sb.append("(");

        if (funDeclOp.getParams() != null) {
            StringJoiner joiner = new StringJoiner(", ");
            for (ParDeclOp parDecl : funDeclOp.getParams()) {
                for (PVarOp pVarOp : parDecl.getPVars()) {
                    String param = (pVarOp.isRef() ? "ref " : "") + parDecl.getType().getTypeName();
                    joiner.add(param);
                }
            }
            sb.append(joiner);
        }

        sb.append(")");
        return sb.toString();
    }

    private void printIndent() {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("\t");
        }
    }

    @Override
    public void visit(FunCallOp funCallOp) {

    }

    @Override
    public void visit(ProgramOp programOp) {
        indentLevel++;

        symbolTable.enterScope();
        programOp.setScope(symbolTable.getCurrentScope());
        if (programOp.getListDecls() != null)
            for (Object varDeclOp : programOp.getListDecls())
                if (varDeclOp instanceof VarDeclOp)
                    ((VarDeclOp) varDeclOp).accept(this);
                else if (varDeclOp instanceof FunDeclOp)
                    ((FunDeclOp) varDeclOp).accept(this);

        System.out.println("Scope in ProgramOp: ");
        printIndent();
        symbolTable.printTable();
        programOp.getBeginEndOp().accept(this);

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
    public void visit(ConstOp constOp) {

    }

    @Override
    public void visit(TypeOp typeOp) {

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
