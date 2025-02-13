package main.visitor.type_checking;

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
import main.visitor.scoping.Kind;
import main.visitor.scoping.SymbolTable;
import main.visitor.tables.BinaryOpTable;
import main.visitor.tables.UnaryOpTable;

import java.util.*;

public class TypeChecking implements Visitor {
    private final SymbolTable symbolTable = new SymbolTable();
    private boolean isFun = false;
    private String currentFunType;

    @Override
    public void visit(IfThenOp ifThenOp) {
        ifThenOp.getCondition().accept(this);
        ifThenOp.getThenBranch().accept(this);
        if(ifThenOp.getCondition().getType().equals("bool") && ifThenOp.getThenBranch().getType().equals("notype"))
            ifThenOp.setType("notype");
        else{
            System.err.print("ERROR: Invalid types in If-Then statement");
            System.exit(1);
        }
    }

    public void visit(ExprOp expr) {
        switch(expr.getClass().getSimpleName()) {
            case "BinaryExprOp":
                visit((BinaryExprOp) expr);
                break;
            case "UnaryExprOp":
                visit((UnaryExprOp) expr);
                break;
            case "ConstOp":
                visit((ConstOp) expr);
                break;
            case "Identifier":
                visit((Identifier) expr);
                break;
        }
    }

    public void visit(StatementOp stmt) {
        switch(stmt.getClass().getSimpleName()) {
            case "IfThenOp":
                visit((IfThenOp) stmt);
                break;
            case "IfThenElseOp":
                visit((IfThenElseOp) stmt);
                break;
            case "WhileOp":
                visit((WhileOp) stmt);
                break;
            case "AssignOp":
                visit((AssignOp) stmt);
                break;
            case "ReadOp":
                visit((ReadOp) stmt);
                break;
            case "WriteOp":
                visit((WriteOp) stmt);
                break;
            case "ReturnOp":
                visit((ReturnOp) stmt);
        }
    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {
        binaryExprOp.getLeft().accept(this);
        binaryExprOp.getRight().accept(this);

        String leftType = binaryExprOp.getLeft().getType();
        String rightType = binaryExprOp.getRight().getType();
        String operator = binaryExprOp.getOp();
        String result = BinaryOpTable.getResult(operator, ignoreRef(leftType), ignoreRef(rightType));
        System.out.println(binaryExprOp);
        if(result == null){
            System.out.println(binaryExprOp);
            System.err.print("ERROR: Invalid types in binary expression \"" + leftType + " " + operator + " " + rightType + "\"");
            System.exit(1);
        }

        binaryExprOp.setType(result);
    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {
        unaryExprOp.getExpr().accept(this);
        String exprType = unaryExprOp.getExpr().getType();
        String operator = unaryExprOp.getOp();
        String result = UnaryOpTable.getResult(operator, ignoreRef(exprType));
        System.out.println(unaryExprOp);
        if(result == null){
            System.err.print("ERROR: Invalid types in unary expression \"" + operator + " " + exprType + "\"");
            System.exit(1);
        }

        unaryExprOp.setType(result);
    }

    @Override
    public void visit(ConstOp constOp) {
        constOp.setType(constOp.getConstantType());
    }

    @Override
    public void visit(Identifier id) {
        String type;
        if(isFun) {
            type = symbolTable.lookup(Kind.FUN, id.getLessema());
            if(type == null){
                System.err.print("ERROR: Function " + id.getLessema() + " not declared");
                System.exit(1);
            }
            isFun = false;
        }
        else {
            type = symbolTable.lookup(Kind.VAR, id.getLessema());
            if(type == null){
                System.err.print("ERROR: Variable " + id.getLessema() + " not declared");
                System.exit(1);
            }
        }
        id.setType(type);
    }

    @Override
    public void visit(BodyOp bodyOp) {
        symbolTable.setCurrentScope(bodyOp.getScope());
        if(bodyOp.getVarDecls() != null){
            for(VarDeclOp varDeclOp : bodyOp.getVarDecls())
                varDeclOp.accept(this);
        }
        if(bodyOp.getStatements() != null){
            for(StatementOp statOp : bodyOp.getStatements())
                statOp.accept(this);
        }
    }

    @Override
    public void visit(FunDeclOp funDeclOp) {
        symbolTable.setCurrentScope(funDeclOp.getScope());
        String funType = symbolTable.lookup(Kind.FUN, funDeclOp.getId().getLessema());
        currentFunType = extractType(funType);
        funDeclOp.setType(currentFunType);

        // Controlla che se la funzione non è di tipo void allora deve contenere almeno un return statement mentre se
        // è di tipo void non deve contenere alcun return statement;
        if(funDeclOp.getType().equals("void")) {
            if(funDeclOp.getBody().getStatements() != null && funDeclOp.getBody().getStatements().stream().anyMatch( stmt -> stmt instanceof ReturnOp)) {
                System.err.print("Error: Function \"" + funDeclOp.getId().getLessema() + "\" cannot contain a return statement");
                System.exit(1);
            }
        }
        else {
            if(funDeclOp.getBody().getStatements() == null || funDeclOp.getBody().getStatements().stream().noneMatch( stmt -> stmt instanceof ReturnOp)) {
                System.err.print("Error: Function \"" + funDeclOp.getId().getLessema() + "\" requires a return statement");
                System.exit(1);
            }
        }

        Optional.ofNullable(funDeclOp.getBody().getStatements())
                .ifPresent(stmtList -> stmtList.forEach(stmt -> stmt.accept(this)));

        Optional.ofNullable(funDeclOp.getBody().getVarDecls())
                .ifPresent(varDeclList -> varDeclList.forEach(varDecl -> varDecl.accept(this)));

        Optional.ofNullable(funDeclOp.getParams())
                .ifPresent(paramDeclList -> paramDeclList.forEach(paramDecl -> paramDecl.accept(this)));

    }

    @Override
    public void visit(FunCallOp funCallOp) {

        isFun = true;
        funCallOp.getId().accept(this);
        String funDeclType = funCallOp.getId().getType();
        String[] expectedParams = extractParameters(funDeclType);
        List<ExprOp> actualParams = funCallOp.getExprList();

        if (actualParams != null)
            if(!actualParams.isEmpty()) {
            if (actualParams.size() != Objects.requireNonNull(expectedParams).length) {
                System.err.println("ERROR: Number of parameters in function call does not match the number of parameters in the function declaration: " + funCallOp.getId().getLessema());
                System.exit(1);
            }

            Map<Integer, String> expectedParamMap = new HashMap<>();
            for (int i = 0; i < Objects.requireNonNull(expectedParams).length; i++) {
                expectedParamMap.put(i, expectedParams[i].replace("ref ", ""));
            }
            //System.out.println(expectedParamMap.toString());

            for (int i = 0; i < actualParams.size(); i++) {
                actualParams.get(i).accept(this);
                if (! ignoreRef(actualParams.get(i).getType()).equals(ignoreRef(expectedParamMap.get(i)))) {
                    System.err.print("ERROR: Parameter type mismatch in function call " + funCallOp.getId().getLessema() + " at position " + (i + 1));
                    System.err.print("; expected " + expectedParamMap.get(i) + " but got " + actualParams.get(i).getType());
                    System.exit(1);
                }
            }

            for(int i = 0; i < actualParams.size(); i++){
                if(expectedParams[i].startsWith("ref")){
                    if(!(actualParams.get(i) instanceof Identifier)){
                        System.err.println("ERROR: Expected reference parameter at position " + (i + 1));
                        System.exit(1);
                    }
                }
            }
        }

        funDeclType = extractType(funDeclType);
        if(!funDeclType.equals("void"))
            funCallOp.setType(funDeclType);
    }

    @Override
    public void visit(ProgramOp programOp) {
        symbolTable.setCurrentScope(programOp.getScope());
        for(Object decl : programOp.getListDecls()) {
            if (decl instanceof VarDeclOp varDeclOp) {
                varDeclOp.accept(this);
            } else if (decl instanceof FunDeclOp funDeclOp) {
                funDeclOp.accept(this);
            }
        }
        programOp.getBeginEndOp().accept(this);
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
            System.err.print("ERROR: Invalid type in While statement");
            System.exit(1);
        }
    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {
        ifThenElseOp.getCondition().accept(this);
        ifThenElseOp.getThenBranch().accept(this);
        ifThenElseOp.getElseBranch().accept(this);

        if(ifThenElseOp.getCondition().getType().equals("bool")
            && ifThenElseOp.getThenBranch().getType().equals("notype")
            && ifThenElseOp.getElseBranch().getType().equals("notype")){
            ifThenElseOp.setType("notype");
        }
        else {
            System.err.println("ERROR: Invalid types in If-Then-Else statement");
            System.exit(1);
        }
    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        symbolTable.setCurrentScope(beginEndOp.getScope());
        Optional.ofNullable(beginEndOp.getVarDeclList())
                .ifPresent(varDeclList -> varDeclList.forEach(varDecl -> varDecl.accept(this)));

        Optional.ofNullable(beginEndOp.getStmtList())
                .ifPresent(stmtList -> stmtList.forEach(stmt -> stmt.accept(this)));
    }

    @Override
    public void visit(AssignOp assignOp) {
        ArrayList<ExprOp> exprList = (ArrayList<ExprOp>) assignOp.getExpressions();
        ArrayList<Identifier> idList = (ArrayList<Identifier>) assignOp.getIdentfiers();

        // Non si puo fare un assegnamento di una chiamata a funzione in un multiple assign
        if(exprList.size() > 1){
            for(ExprOp exprOp : exprList){
                if(exprOp instanceof FunCallOp){
                    System.err.println("ERROR: Cannot assign a function call to a variable in a multiple assign statement");
                    System.exit(1);
                }
            }
        }

        // #Identifiers == #expressions negli assegnamenti, altrimenti errore
        if(idList.size() != exprList.size()){
            System.err.println("ERROR: Number of identifiers and expressions do not match in assignment");
            System.exit(1);
        }

        // Visita che crea la SymbolTable
        for(Identifier id : idList)
            id.accept(this);
        for(ExprOp expr : exprList)
            expr.accept(this);

        for(Identifier id : idList){
            String idType = id.getType();
            String exprType = exprList.get(idList.indexOf(id)).getType();
            if(!isCompatible(idType, exprType)){
                System.err.print("ERROR: Conflicting types in assignment: id "+ id.getLessema());
                System.err.print(" has type " + idType + " but expression has type "+ exprType);
                System.exit(1);
            }
        }

    }

    @Override
    public void visit(ReturnOp returnOp) {
        returnOp.getExpr().accept(this);
        String returnType = returnOp.getExpr().getType();
        if(!returnType.equals(currentFunType)){
            System.err.print("ERROR: Conflicting return type in function " + returnOp.getExpr().getType());
            System.exit(1);
        }
    }

    @Override
    public void visit(WriteOp writeOp) {
        for(ExprOp expr : writeOp.getExprList())
            expr.accept(this);
    }

    @Override
    public void visit(ReadOp readOp) {
        for(Identifier id : readOp.getIdentifiers())
            id.accept(this);
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        if(varDeclOp.getTypeOrConstant() instanceof ConstOp c){
            c.accept(this);
        }

        ArrayList<VarOptInitOp> varOptInitList = (ArrayList<VarOptInitOp>) varDeclOp.getListVarOptInit();
        if(varOptInitList != null){
            for(VarOptInitOp varOptInit : varOptInitList)
                varOptInit.accept(this);
        }
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        varOptInitOp.getId().accept(this);
        if(varOptInitOp.getExprOp() != null)
            varOptInitOp.getExprOp().accept(this);
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {}

    @Override
    public void visit(PVarOp pVarOp) {}

    private String[] extractParameters(String type) {
        String[] parts = type.split("\\(");// Divide in base a "("
        return parts[1].replace(")", "").split(", "); // Rimuove la parentesi finale e divide in base a " "
    }

    private String extractType(String type) {
        return type.split("\\(")[0]; // Prende tutto prima di "("
    }

    private boolean isCompatible(String type1, String type2) {
        type1 = ignoreRef(type1);
        type2 = ignoreRef(type2);
        return type1.equals(type2) || type1.equals("double") && type2.equals("int");
    }

    private String ignoreRef(String type) {
        if (type.startsWith("ref ")) {
            return type.substring(4);
        }
        return type;
    }
}