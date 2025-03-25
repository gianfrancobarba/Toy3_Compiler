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
    private String tempType;

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

        if(result == null){
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
    public void visit(AssignmentCascadeOp assignmentCascadeOp) {
        assignmentCascadeOp.getIdentifier().accept(this);
        assignmentCascadeOp.getExprOp().accept(this);
        if(!assignmentCascadeOp.getIdentifier().getType().equals("int") && !assignmentCascadeOp.getIdentifier().getType().equals("double")){
            System.err.println("Error: identifier in cascade assignment must be int or double but is: "+ assignmentCascadeOp.getIdentifier().getType());
            System.exit(1);
        }
        if(!assignmentCascadeOp.getExprOp().getType().equals("int") && !assignmentCascadeOp.getExprOp().getType().equals("double")){
            System.err.println("Error: Expression in cascade assignment must be int or double but is: "+ assignmentCascadeOp.getExprOp().getType());
            System.exit(1);
        }
        if(!isCompatible(assignmentCascadeOp.getIdentifier().getType(), assignmentCascadeOp.getExprOp().getType())){
            System.err.println("Error: incompatible type in cascade assignment. identifier is type: "+ assignmentCascadeOp.getIdentifier().getType() + " but expr is type : "+ assignmentCascadeOp.getExprOp().getType() );
            System.exit(1);
        }
    }

    @Override
    public void visit(CascadeOp cascadeOp) {
        if(!cascadeOp.getInitAssignList().isEmpty() && cascadeOp.getInitAssignList() != null)
            cascadeOp.getInitAssignList().forEach(assignmentCascadeOp -> assignmentCascadeOp.accept(this));
        cascadeOp.getExpr().accept(this);
        if(!cascadeOp.getExpr().getType().equals("bool")){
            System.err.println("Error: cond-expr in Cascade statement must be bool but is: "+ cascadeOp.getExpr().getType() );
            System.exit(1);
        }
        if(!cascadeOp.getUpdateAssignList().isEmpty() && cascadeOp.getUpdateAssignList() != null)
            cascadeOp.getUpdateAssignList().forEach(assignmentCascadeOp -> assignmentCascadeOp.accept(this));
        if(!cascadeOp.getStatementOpList().isEmpty() && cascadeOp.getStatementOpList() != null)
            cascadeOp.getStatementOpList().forEach(statementOp -> statementOp.accept(this));
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
        symbolTable.setCurrentScope(symbolTable.getCurrentScope().getParent());
    }

    @Override
    public void visit(FunDeclOp funDeclOp) {
        // Imposta lo scope corrente
        symbolTable.setCurrentScope(funDeclOp.getScope());

        // Recupera il tipo della funzione dalla symbol table e lo imposta nell'AST
        String funType = symbolTable.lookup(Kind.FUN, funDeclOp.getId().getLessema());
        funDeclOp.setType(funType);
        currentFunType = extractType(funType);

        // Se la funzione è di tipo void, non deve contenere alcun return statement
        if (currentFunType.equals("void")) {
            if (funDeclOp.getBody().getStatements() != null &&
                    funDeclOp.getBody().getStatements().stream().anyMatch(stmt -> stmt instanceof ReturnOp)) {
                System.err.println("Error: Function \"" + funDeclOp.getId().getLessema() + "\" cannot contain a return statement");
                System.exit(1);
            }
        }
        // Se la funzione non è void, deve garantire che ogni percorso di esecuzione restituisca un valore
        else {
            boolean hasReturnOp = false;
            List<StatementOp> statements = funDeclOp.getBody().getStatements();

            if (statements != null) {
                // Verifica se esiste un return statement diretto nel corpo della funzione
                hasReturnOp = statements.stream().anyMatch(stmt -> stmt instanceof ReturnOp);

                // Se non c'è un return diretto, controlla se esistono degli if-then-else che garantiscono il return in entrambi i rami
                if (!hasReturnOp) {
                    for (StatementOp stmt : statements) {
                        if (stmt instanceof IfThenElseOp ifStmt) {
                            boolean thenHasReturn = ifStmt.getThenBranch() != null &&
                                    ifStmt.getThenBranch().getStatements() != null &&
                                    ifStmt.getThenBranch().getStatements().stream().anyMatch(s -> s instanceof ReturnOp);

                            boolean elseHasReturn = ifStmt.getElseBranch() != null &&
                                    ifStmt.getElseBranch().getStatements() != null &&
                                    ifStmt.getElseBranch().getStatements().stream().anyMatch(s -> s instanceof ReturnOp);

                            if (!thenHasReturn) {
                                System.err.println("Error: Missing return in the then branch of the if-then-else statement in function \"" + funDeclOp.getId().getLessema() + "\"");
                                System.exit(1);
                            } else if (!elseHasReturn) {
                                System.err.println("Error: Missing return in the else branch of the if-then-else statement in function \"" + funDeclOp.getId().getLessema() + "\"");
                                System.exit(1);
                            } else {
                                // Se entrambi i rami contengono il return, consideriamo questo percorso valido
                                hasReturnOp = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!hasReturnOp) {
                System.err.println("Error: Function \"" + funDeclOp.getId().getLessema() + "\" requires at least one return statement");
                System.exit(1);
            }
        }

        // Visita i parametri della funzione
        Optional.ofNullable(funDeclOp.getParams())
                .ifPresent(paramList -> paramList.forEach(param -> param.accept(this)));

        // Visita le dichiarazioni delle variabili nel corpo della funzione
        Optional.ofNullable(funDeclOp.getBody().getVarDecls())
                .ifPresent(varDeclList -> varDeclList.forEach(varDecl -> varDecl.accept(this)));

        // Visita gli statement del corpo della funzione
        Optional.ofNullable(funDeclOp.getBody().getStatements())
                .ifPresent(stmtList -> stmtList.forEach(stmt -> stmt.accept(this)));

        // Ripristina lo scope corrente al parent
        symbolTable.setCurrentScope(symbolTable.getCurrentScope().getParent());
    }


    @Override
    public void visit(FunCallOp funCallOp) {
        isFun = true;

        // Visita l'identificatore della funzione per determinare il suo tipo (firma)
        funCallOp.getId().accept(this);
        String functionSignature = funCallOp.getId().getType();
        // Estrai i tipi dei parametri attesi dalla firma della funzione
        List<String> expectedParamTypes;
        String paramsSection = functionSignature.substring(functionSignature.indexOf("(") + 1, functionSignature.indexOf(")")).trim();
        if (paramsSection.isEmpty()) {
            expectedParamTypes = null;
        } else {
            // La firma viene processata per ottenere la stringa dei parametri e poi divisa in una lista
            functionSignature = extractParameters(functionSignature);
            expectedParamTypes = new ArrayList<>(List.of(functionSignature.split(", ")));
        }

        // Ottieni la lista degli argomenti effettivi passati nella chiamata
        List<ExprOp> actualArguments = funCallOp.getExprList();
        // Controllo sul numero degli argomenti
        if (expectedParamTypes == null && !actualArguments.isEmpty()) {
            System.err.print("ERROR: Function " + funCallOp.getId().getLessema() + " called with " + actualArguments.size());
            System.err.println(" parameters, but it does not require parameters");
            System.exit(1);
        } else if (expectedParamTypes != null && actualArguments.isEmpty()) {
            System.err.print("ERROR: Function " + funCallOp.getId().getLessema() + " called with 0 parameters, but it requires " + expectedParamTypes.size());
            System.exit(1);
        }

        if (!actualArguments.isEmpty()) {
            if (expectedParamTypes.size() != actualArguments.size()) {
                System.err.print("ERROR: Function " + funCallOp.getId().getLessema() + " called with " + actualArguments.size());
                System.err.println(" parameters, but it requires " + expectedParamTypes.size());
                System.exit(1);
            }

            // Per ogni argomento, verifica che il tipo sia compatibile con quello atteso
            for (int i = 0; i < expectedParamTypes.size(); i++) {
                String expectedParamType = ignoreRef(expectedParamTypes.get(i));
                ExprOp argument = actualArguments.get(i);
                argument.accept(this);
                String actualArgType = ignoreRef(argument.getType());

                if (!isCompatible(expectedParamType, actualArgType)) {
                    System.err.print("ERROR: Function " + funCallOp.getId().getLessema() + " called with parameter at position ");
                    System.err.println((i + 1) + " of type " + actualArgType + " but it requires " + expectedParamTypes.get(i));
                    System.exit(1);
                }
            }

            // Se il parametro è passato per riferimento, controlla che l'argomento sia una variabile
            for (int i = 0; i < expectedParamTypes.size(); i++) {
                if (expectedParamTypes.get(i).startsWith("ref") && !(actualArguments.get(i) instanceof Identifier)) {
                    System.err.print("ERROR: Function " + funCallOp.getId().getLessema() + " called with non-variable argument at position " + (i + 1));
                    System.exit(1);
                }
            }
        }

        // Estrai il tipo della funzione (senza la parte relativa ai parametri)
        String functionType = extractType(funCallOp.getId().getType());
        if (!functionType.equals("void"))
            funCallOp.setType(functionType);
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
        if(whileOp.getCondition().getType().equals("bool") && whileOp.getBody().getType().equals("notype")){
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
            if(!isCompatible(ignoreRef(idType), ignoreRef(exprType))){
                System.err.print("ERROR: Conflicting types in assignment: id "+ id.getLessema());
                System.err.print(" has type " + idType + " but expression has type "+ exprType);
                System.exit(1);
            }
        }
        assignOp.setType("notype");
    }

    @Override
    public void visit(ReturnOp returnOp) {
        // Controlla che non sia presente un return statement in un procedura
        if (currentFunType.equals("void")) {
            System.err.print("ERROR: Return statement in a procedure");
            System.exit(1);
        }
        returnOp.getExpr().accept(this);
        // Controlla che il tipo dell'espressione sia lo stesso tipo di ritorno della funzione
        String returnType = returnOp.getExpr().getType();
        if (!isCompatible(returnType, currentFunType)) {
            System.err.print("ERROR: Invalid return type " + returnType + " for function " + returnOp.getFunLabel()+ ": "+ currentFunType);
            System.exit(1);
        }
        returnOp.setType(currentFunType);
    }

    @Override
    public void visit(WriteOp writeOp) {
        for(ExprOp expr : writeOp.getExprList()) {
            expr.accept(this);

            if(expr instanceof FunCallOp funCallOp && funCallOp.getType().equals("notype")){
                System.err.print("ERROR: Invalid use of void expression in write operation");
                System.exit(1);
            }
        }
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
            varDeclOp.setType(c.getType());
        }

        ArrayList<VarOptInitOp> varOptInitList = (ArrayList<VarOptInitOp>) varDeclOp.getListVarOptInit();
        if(varOptInitList != null){
            for(VarOptInitOp varOptInit : varOptInitList) {
                varOptInit.accept(this);
                varDeclOp.setType(varOptInit.getType());
            }
        }
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        Identifier id = varOptInitOp.getId();
        ExprOp expr = varOptInitOp.getExprOp();

        id.accept(this);
        varOptInitOp.setType(id.getType());
        if(expr != null) {
            expr.accept(this);
            if (!isCompatible(ignoreRef(id.getType()), ignoreRef(expr.getType()))) {
                System.err.print("ERROR: Conflicting types in assignment: id " + id.getLessema());
                System.err.print(" has type " + id.getType() + " but expression has type " + expr.getType());
                System.exit(1);
            }
        }
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        List<PVarOp> pVarList = parDeclOp.getPVars();

        parDeclOp.setType(parDeclOp.getParDeclType());
        tempType = parDeclOp.getType();

        pVarList.forEach(pVar -> pVar.accept(this));
    }

    @Override
    public void visit(PVarOp pVarOp) {
        if(pVarOp.isRef())
            tempType = "ref " + tempType;

        pVarOp.setType(tempType);
    }

    private String extractParameters(String type) {
        return type.substring(type.indexOf("(") + 1, type.indexOf(")"));
    }

    private String extractType(String type) {
        return type.split("\\(")[0]; // Prende tutto prima di "("
    }

    private boolean isCompatible(String type1, String type2) {
        type1 = ignoreRef(type1);
        type2 = ignoreRef(type2);
        return type1.equals(type2) || type1.equals("double") && type2.equals("int") || type1.equals("int") && type2.equals("double");
    }

    private String ignoreRef(String type) {
        if (type.startsWith("ref ")) {
            return type.substring(4);
        }
        return type;
    }
}