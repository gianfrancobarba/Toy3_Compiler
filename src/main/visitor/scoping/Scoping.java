package main.visitor.scoping;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.BinaryExprOp;
import main.nodes.expr.ExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.expr.UnaryExprOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.ConstOp;
import main.visitor.Visitor;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Scoping implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();
    private String tempType;
    private String funTemp;
    private String isFun;

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

        ////System.out.println("\n ---- Scope in ProgramOp ----");
        ////symbolTable.printTable();

        programOp.getBeginEndOp().accept(this);

        ////symbolTable.exitScope();
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
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
                if (varDeclOp.getTypeOrConstant() instanceof String type)
                    tempType = type;
                else if (varDeclOp.getTypeOrConstant() instanceof ConstOp constant)
                    tempType = constant.getConstantType();

                varOptInitOp.accept(this);
            }
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        // Controllo se la variabile è già stata dichiarata nello scope corrente
        if (symbolTable.probe(Kind.VAR, varOptInitOp.getId().getLessema())) {
            // controllo se il tipo della variabile è uguale a quello dichiarato
            if (Objects.equals(symbolTable.lookup(Kind.VAR, varOptInitOp.getId().getLessema()), tempType)) {
                System.err.print("ERROR: Redeclaration of " + varOptInitOp.getId().getLessema());
                System.exit(1);
            }
            System.err.print("ERROR: Conflicting types for " + varOptInitOp.getId().getLessema());
            System.err.print("; Previous declaration have type " + symbolTable.lookup(Kind.VAR, varOptInitOp.getId().getLessema()));
            System.exit(1);
        }
        symbolTable.addId(Kind.VAR, varOptInitOp.getId().getLessema(), tempType);
        //varOptInitOp.getId().setType(tempType); // setto il tipo della variabile
    }


    @Override
    public void visit(FunDeclOp funDeclOp) {
        String funId = funDeclOp.getId().getLessema();
        if(symbolTable.probe(Kind.FUN, funId)){
            System.err.print("ERROR: Function "+ funId + " already declared with type: "+ symbolTable.lookup(Kind.FUN, funId));
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
                stmt.setFunLabel(funId);
                stmt.accept(this);
            }
        }

        ////System.out.println("\n---- Scope in DefDeclOp [" + funId + "] ----");
        ////symbolTable.printTable();

        symbolTable.exitScope();
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        tempType = parDeclOp.getParDeclType();

        if(parDeclOp.getPVars() != null) {
            for(PVarOp pVarOp : parDeclOp.getPVars()) {
                pVarOp.accept(this);
            }
        }
    }

    @Override
    public void visit(PVarOp pVarOp) {
        String varId  = pVarOp.getId().getLessema();
        if(symbolTable.probe(Kind.VAR, varId) ) {
            ////symbolTable.printTable();
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
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        beginEndOp.setFunLabel("main");

        symbolTable.enterScope();
        beginEndOp.setScope(symbolTable.getCurrentScope());
        if(beginEndOp.getVarDeclList() != null){
            for(VarDeclOp varDeclOp : beginEndOp.getVarDeclList()){
                varDeclOp.accept(this);
            }
        }

        ////System.out.println("\n---- Scope in BeginEndOp ----");
        ////symbolTable.printTable();

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
        symbolTable.enterScope();
        bodyOp.setScope(symbolTable.getCurrentScope());
        if(bodyOp.getVarDecls() != null){
            for(VarDeclOp varDeclOp : bodyOp.getVarDecls()){
                varDeclOp.accept(this);
            }
        }

        ////System.out.println("\n---- Scope in BodyOp [" + bodyOp.getFunLabel() + "] ----");
        ////symbolTable.printTable();

        if(bodyOp.getStatements() != null){
            for(StatementOp statOp : bodyOp.getStatements()){
                statOp.setFunLabel(bodyOp.getFunLabel());
                statOp.accept(this);
            }
        }

        symbolTable.exitScope();
    }

    public void visit(StatementOp statementOp) {
        switch (statementOp.getClass().getSimpleName()) {
            case "IfThenOp" -> {
                statementOp.setFunLabel(statementOp.getFunLabel());
                visit((IfThenOp) statementOp);
            }
            case "IfThenElseOp" -> {
                statementOp.setFunLabel(statementOp.getFunLabel());
                visit((IfThenElseOp) statementOp);
            }
            case "WhileOp" -> {
                statementOp.setFunLabel(statementOp.getFunLabel());
                visit((WhileOp) statementOp);
            }
        }
    }

    @Override
    public void visit(IfThenOp ifThenOp) {
        ////System.out.println("\n---- Scope IfThenOp [ " + ifThenOp.getFunLabel()+"] ----");
        ifThenOp.getThenBranch().setFunLabel("ifThenBranch <- " + ifThenOp.getFunLabel());
        ifThenOp.getThenBranch().accept(this);
    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {
        ////System.out.println("\n---- Scope IfThenElseOp [" + ifThenElseOp.getFunLabel()+"] ----");
        ifThenElseOp.getThenBranch().setFunLabel("IfElse_IfBranch <- " + ifThenElseOp.getFunLabel());
        ifThenElseOp.getElseBranch().setFunLabel("ifElse_ElseBranch <- " + ifThenElseOp.getFunLabel());

        ifThenElseOp.getThenBranch().accept(this);
        ifThenElseOp.getElseBranch().accept(this);
    }

    @Override
    public void visit(WhileOp whileOp) {
        ////System.out.println("\n---- Scope WhileOp [" + whileOp.getFunLabel()+"] ----");
        whileOp.getBody().setFunLabel("WhileOp <- "+ whileOp.getFunLabel());

        whileOp.getBody().accept(this);
    }

    @Override
    public void visit(FunCallOp funCallOp) {
        funTemp = funCallOp.getId().getLessema();
        if(symbolTable.lookup(Kind.FUN, funTemp) == null){
            System.err.print("ERROR: Function " + funTemp + " not declared");
            System.exit(1);
        }

        funCallOp.getExprList().forEach(expr -> expr.accept(this));
    }

    @Override
    public void visit(AssignOp assignOp) {
        assignOp.getIdentfiers().forEach(id -> id.accept(this));
    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {
        binaryExprOp.getLeft().accept(this);
        binaryExprOp.getRight().accept(this);
    }

    @Override
    public void visit(ReturnOp returnOp) {
        returnOp.getExpr().accept(this);
    }

    @Override
    public void visit(WriteOp writeOp) {
        writeOp.getExprList().forEach(expr -> expr.accept(this));
    }

    @Override
    public void visit(ReadOp readOp) {
        readOp.getIdentifiers().forEach(id -> id.accept(this));
    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {
        unaryExprOp.getExpr().accept(this);
    }

    @Override
    public void visit(Identifier identifier) {
        if(symbolTable.lookup(Kind.VAR, identifier.getLessema()) == null) {
            System.err.print("ERROR: Variable " + identifier.getLessema() + " not declared");
            System.exit(1);
        }
    }

    @Override
    public void visit(ConstOp constOp) {
    }

    @Override
    public void visit(AssignmentCascadeOp assignmentCascadeOp) {
        assignmentCascadeOp.getIdentifier().accept(this);
        assignmentCascadeOp.getExprOp().accept(this);
    }

    @Override
    public void visit(CascadeOp cascadeOp) {
        if(!cascadeOp.getInitAssignList().isEmpty() && cascadeOp.getInitAssignList() != null)
            cascadeOp.getInitAssignList().forEach(assignmentCascadeOp -> assignmentCascadeOp.accept(this)); // non puo essere nulla
        cascadeOp.getExpr().accept(this);
        if(!cascadeOp.getUpdateAssignList().isEmpty() && cascadeOp.getUpdateAssignList() != null)
            cascadeOp.getUpdateAssignList().forEach(assignmentCascadeOp -> assignmentCascadeOp.accept(this));
        if(!cascadeOp.getStatementOpList().isEmpty() && cascadeOp.getStatementOpList() != null)
            cascadeOp.getStatementOpList().forEach(statementOp -> statementOp.accept(this));
    }

    private String functionSignature(FunDeclOp funDeclOp) {

        StringBuilder sb = new StringBuilder();
        String type;
        if (funDeclOp.getOptType() != null) {
            type = funDeclOp.getOptType();
            sb.append(type);
        } else {
            sb.append("void");
        }
        sb.append("(");

        if (funDeclOp.getParams() != null) {
            StringJoiner joiner = new StringJoiner(", ");
            for (ParDeclOp parDecl : funDeclOp.getParams()) {
                for (PVarOp pVarOp : parDecl.getPVars()) {
                    String param = (pVarOp.isRef() ? "ref " : "") + parDecl.getParDeclType();
                    joiner.add(param);
                }
            }
            sb.append(joiner);
        }

        sb.append(")");
        return sb.toString();
    }
}
