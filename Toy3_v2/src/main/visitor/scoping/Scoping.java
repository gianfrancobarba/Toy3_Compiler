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

import java.util.Objects;
import java.util.StringJoiner;

public class Scoping implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();
    private String tempType;
    private String funTemp;

    @Override
    public void visit(ProgramOp programOp) {

        // System.out.println("Visit programOp");
        symbolTable.enterScope();
        programOp.setScope(symbolTable.getCurrentScope());
        if (programOp.getListDecls() != null)
            for (Object varDeclOp : programOp.getListDecls())
                if (varDeclOp instanceof VarDeclOp)
                    ((VarDeclOp) varDeclOp).accept(this);
                else if (varDeclOp instanceof FunDeclOp)
                    ((FunDeclOp) varDeclOp).accept(this);

        System.out.println("\n ---- Scope in ProgramOp ----");
        symbolTable.printTable();

        programOp.getBeginEndOp().accept(this);

        symbolTable.exitScope();
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        // System.out.println("Visit var decl");
        if (varDeclOp.getTypeOrConstant() instanceof ConstOp) {
            if (varDeclOp.getListVarOptInit().size() > 1) {
                System.err.print("ERROR: cannot declare multiple variables with a constant type");
                System.exit(1);
            }

            // se non restituisce il primo errore allora sicuramente la lista avrà al più un elemento.
            VarOptInitOp varOpt = varDeclOp.getListVarOptInit().get(0);
            if(varOpt.getExprOp() != null){
                System.err.print("ERROR: ");
                System.err.print(varOpt.getId().getLessema() + " ");
                System.err.print("is a constant and cannot be initialized");
                System.exit(1);
            }
        }

        if (varDeclOp.getListVarOptInit() != null)
            for(VarOptInitOp varOptInitOp : varDeclOp.getListVarOptInit()) {
                if (varDeclOp.getTypeOrConstant() instanceof TypeOp type)
                    tempType = type.getTypeName();
                else if (varDeclOp.getTypeOrConstant() instanceof ConstOp constant)
                    tempType = constant.getConstantType();

                varOptInitOp.accept(this);
            }
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        // System.out.println("Visit var init");
        if (symbolTable.probe(Kind.VAR, varOptInitOp.getId().getLessema())) {
            if (Objects.equals(symbolTable.lookup(Kind.VAR, varOptInitOp.getId().getLessema()), tempType)) {
                System.err.print("ERROR: Redeclaration of " + varOptInitOp.getId().getLessema());
                System.exit(1);
            }
            System.err.print("ERROR: Conflicting types for " + varOptInitOp.getId().getLessema());
            System.err.print("; Previous declaration have type " + symbolTable.lookup(Kind.VAR, varOptInitOp.getId().getLessema()));
            System.exit(1);
        }
        symbolTable.addId(Kind.VAR, varOptInitOp.getId().getLessema(), tempType);
// System.out.println("Visit  + varOptInitOp.getId().getLessema() + " " + tempType);
    }


    @Override
    public void visit(FunDeclOp funDeclOp) {
        // System.out.println("Visit fun decl");
        String funId = funDeclOp.getId().getLessema();

        if(symbolTable.probe(Kind.FUN, funId)){
            System.err.print("ERROR: Function "+ funId + " already declared with type: "+symbolTable.lookup(Kind.FUN, funId));
            System.exit(1);
        }

        String signature = functionSignature(funDeclOp);
        // aggiungiamo la firma del metodo in Globals
        symbolTable.addId(Kind.FUN, funId, signature);
// System.out.println("Visit  + funId + " " + signature);

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
                stmt.setFunLabel(funId);
                stmt.accept(this);
            }
        }

        System.out.println("\n---- Scope in DefDeclOp [" + funId + "] ----");
        symbolTable.printTable();
        
        symbolTable.exitScope();
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        // System.out.println("Visit par decl");
        tempType = parDeclOp.getType().getTypeName();

        if(parDeclOp.getPVars() != null) {
            for(PVarOp pVarOp : parDeclOp.getPVars()) {
                pVarOp.accept(this);
            }
        }
    }

    @Override
    public void visit(PVarOp pVarOp) {
        // System.out.println("Visit p var " + pVarOp.getId().getLessema());
        String varId  = pVarOp.getId().getLessema();
        if(symbolTable.probe(Kind.VAR, varId) ) {
            symbolTable.printTable();
            if(Objects.equals(symbolTable.lookup(Kind.VAR, varId), tempType)){
                System.err.print("ERROR: Redefinition of parameter " + varId);
                System.exit(1);
            }
            System.err.print("ERROR: Conflicting types for parameter " + varId);
            System.err.print("; Previous definition have type " + symbolTable.lookup(Kind.VAR, varId));
            System.exit(1);
        }

        if(pVarOp.isRef())
            tempType = "ref " + tempType;

        symbolTable.addId(Kind.VAR, varId, tempType);
        // System.out.println("Visit  + varId + " " + tempType);
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {

        beginEndOp.setFunLabel("main");

        // System.out.println("Visit begin end");
        symbolTable.enterScope();
        beginEndOp.setScope(symbolTable.getCurrentScope());
        if(beginEndOp.getVarDeclList() != null){
            for(VarDeclOp varDeclOp : beginEndOp.getVarDeclList()){
                varDeclOp.accept(this);
            }
        }

        System.out.println("\n---- Scope in BeginEndOp ----");
        symbolTable.printTable();

        if(beginEndOp.getStmtList() != null){
            for(StatementOp statOp : beginEndOp.getStmtList()){
                statOp.setFunLabel(beginEndOp.getFunLabel());
                statOp.accept(this);
            }
        }
        
        symbolTable.exitScope();
    }

    @Override
    public void visit(BodyOp bodyOp) {
        // System.out.println("Visit body");
        symbolTable.enterScope();
        bodyOp.setScope(symbolTable.getCurrentScope());
        if(bodyOp.getVarDecls() != null){
            for(VarDeclOp varDeclOp : bodyOp.getVarDecls()){
                varDeclOp.accept(this);
            }
        }

        System.out.println("\n---- Scope in BodyOp [" + bodyOp.getFunLabel() + "] ----");
        symbolTable.printTable();

        if(bodyOp.getStatements() != null){
            for(StatementOp statOp : bodyOp.getStatements()){
                statOp.setFunLabel(bodyOp.getFunLabel());
                statOp.accept(this);
            }
        }
        
        symbolTable.exitScope();
    }

    public void visit(StatementOp statementOp) {
        // System.out.println("Visit stmt");
       if (statementOp instanceof IfThenOp ifThenOp) {
           ifThenOp.setFunLabel(statementOp.getFunLabel());
           visit(ifThenOp);
        } else if (statementOp instanceof IfThenElseOp ifThenElseOp) {
           ifThenElseOp.setFunLabel(statementOp.getFunLabel());
           visit(ifThenElseOp);
        } else if (statementOp instanceof WhileOp whileOp) {
           whileOp.setFunLabel(statementOp.getFunLabel());
           visit(whileOp);
        }
    }

    @Override
    public void visit(IfThenOp ifThenOp) {
        System.out.println("\n---- Scope IfThenOp [ " + ifThenOp.getFunLabel()+"] ----");
        ifThenOp.getThenBranch().setFunLabel("ifThenBranch <- " + ifThenOp.getFunLabel());
        ifThenOp.getThenBranch().accept(this);
    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {
        System.out.println("\n---- Scope IfThenElseOp [" + ifThenElseOp.getFunLabel()+"] ----");
        ifThenElseOp.getThenBranch().setFunLabel("IfElse_IfBranch <- " + ifThenElseOp.getFunLabel());
        ifThenElseOp.getElseBranch().setFunLabel("ifElse_ElseBranch <- " + ifThenElseOp.getFunLabel());

        ifThenElseOp.getThenBranch().accept(this);
        ifThenElseOp.getElseBranch().accept(this);
    }

    @Override
    public void visit(WhileOp whileOp) {
        System.out.println("\n---- Scope WhileOp [" + whileOp.getFunLabel()+"] ----");

        whileOp.getBody().setFunLabel("WhileOp <- "+ whileOp.getFunLabel());
        whileOp.getBody().accept(this);
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

    @Override
    public void visit(FunCallOp funCallOp) {}
    @Override
    public void visit(ReturnOp returnOp) {}
    @Override
    public void visit(UnaryExprOp unaryExprOp) {}
    @Override
    public void visit(Identifier identifier) {}
    @Override
    public void visit(BinaryExprOp binaryExprOp) {}
    @Override
    public void visit(AssignOp assignOp) {}
    @Override
    public void visit(ReadOp readOp) {}
    @Override
    public void visit(WriteOp writeOp) {}
    @Override
    public void visit(ConstOp constOp) {}
    @Override
    public void visit(TypeOp typeOp) {}
}
