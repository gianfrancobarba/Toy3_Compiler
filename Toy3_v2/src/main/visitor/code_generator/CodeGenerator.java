package main.visitor.code_generator;

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
import main.visitor.scoping.Scope;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class CodeGenerator implements Visitor {

    private final StringBuilder code;
    private final BufferedWriter writer;
    private String typeTemp;
    private Map<String, String> funConflictMap = new HashMap<>();
    private Map<String, List<String>> funSignatureMap = new HashMap<>();
    private boolean isStmt = false;
    private boolean isGlobal = false;
    private List<VarDeclOp> listStringGlobal = new ArrayList<>();

    public CodeGenerator() throws IOException {
        this.code = new StringBuilder();
        this.writer = new BufferedWriter(new FileWriter("filetester/output.c"));
    }

    @Override
    public void visit(ProgramOp programOp) {
        code.append("#include <stdio.h>\n");
        code.append("#include <string.h>\n");
        code.append("#include <stdlib.h>\n");
        code.append("#include <stdbool.h>\n\n");

        printMallocFun();
        printReallocFun();

        for (Object obj : programOp.getListDecls()) {
            isGlobal = true;
            // Se l'oggetto è una dichiarazione di variabile, visita l'oggetto per generare il codice
            if (obj instanceof VarDeclOp varDeclOp) {
                varDeclOp.accept(this);
                code.append("\n");
            }
        }
        isGlobal = false;
        code.append("\n");

        // Risolve i conflitti di nomi tra funzioni e variabili (assegna _fun al nome della funzione in caso di conflitto)
        resolveNameConflicts(programOp.getListDecls());
        // Aggiunge le firme delle funzioni al codice
        appendFunctionSignatures(programOp.getListDecls());
        code.append("\n");

        // Processa il blocco principale del programma (Begin-End)
        // Visitando l'oggetto BeginEndOp si genera il codice relativo al corpo del programma.
        programOp.getBeginEndOp().accept(this);
        code.append("\n\n");

        // Scrive tutti i corpi delle funzioni dopo il main
        programOp.getListDecls().forEach(obj -> {
            if (obj instanceof FunDeclOp funDeclOp) {
                funDeclOp.accept(this);
                code.append("\n");
            }
        });
        // Scrive il codice generato sul file utilizzando il BufferedWriter.
        // In caso di errore durante la scrittura, viene lanciata una RuntimeException.
        try {
            writer.append(code.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void visit(BeginEndOp beginEndOp) {
        code.append("int main(void){\n");
        globalStringAllocate();
        beginEndOp.getVarDeclList().forEach(varDeclOp -> varDeclOp.accept(this));
        beginEndOp.getStmtList().forEach(statementOp -> {
            setStmt(statementOp, true);
            statementOp.accept(this);
        });
        code.append("\nreturn 0;\n}");
    }

    private void globalStringAllocate() {
        listStringGlobal.forEach(varDeclOp -> {
//            code.append(varDeclOp.getType()).append(" ");
//            varDeclOp.getListVarOptInit().forEach(varOptInitOp -> {
//                code.append("*").append(varOptInitOp.getId().getLessema()).append(" = allocate_string(256);\n");
//            });

            varDeclOp.accept(this);
        });
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {
        List<VarOptInitOp> listVarOptInit = varDeclOp.getListVarOptInit();
        String type = varDeclOp.getType();

        if(type.equals("string")) {
            if(isGlobal) { listStringGlobal.add(varDeclOp); }

            listVarOptInit.forEach(varOpt -> {
                code.append("char").append(" ");
                varOpt.accept(this);
                code.append(";\n");
            });
            if(!isGlobal) {
                if (varDeclOp.getTypeOrConstant() instanceof ConstOp con) {
                    code.append("strcpy(").append(varDeclOp.getListVarOptInit().get(0).getId().getLessema()).append(", ");
                    con.accept(this);
                    code.append(");\n");
                }
            }
        }
        else {

            code.append(type).append(" ");

            listVarOptInit.forEach(varOpt -> {
                varOpt.accept(this);
                code.append(", ");
            });
            // rimuove l'ultima virgola e l'ultimo spazio
            code.deleteCharAt(code.length() - 2);

            // controlla se l'assegnamento è del tipo ID : constant, se è cosi si aggiunge = e la costante assegnata
            if (varDeclOp.getTypeOrConstant() instanceof ConstOp con) {
                code.append(" = ");
                con.accept(this);
                code.append(" ");
            }
            // aggiunge il punto e virgola alla fine della dichiarazione
            code.setCharAt(code.length() - 1, ';');
            code.append("\n");
        }
    }

    @Override
    public void visit(IfThenOp ifThenOp) {
        code.append("if (");
        ifThenOp.getCondition().accept(this);
        code.append(") {\n");
        ifThenOp.getThenBranch().accept(this);
        code.append("}\n");
    }

    @Override
    public void visit(BodyOp bodyOp) {
        bodyOp.getVarDecls().forEach(varDecl -> varDecl.accept(this));
        bodyOp.getStatements().forEach(stmt -> {
                setStmt(stmt, true);
                stmt.accept(this);
        });
    }

    @Override
    public void visit(FunDeclOp funDeclOp) {
        code.append(isRefConvert(funDeclOp.getType()));
        code.append(" ");
        code.append(funDeclOp.getId().getLessema());
        code.append("(");

        List<ParDeclOp> params = funDeclOp.getParams();
        if(params != null)
            if(!params.isEmpty()) {
                code.append(buildParameterString(params));
            }
        code.append(") {\n");
        funSignatureMap.put(funDeclOp.getId().getLessema(), extractParameterType(buildParameterString(params)));
        funDeclOp.getBody().accept(this);
        code.append("}\n");
    }

    @Override
    public void visit(FunCallOp funCallOp) {
        List<ExprOp> args = funCallOp.getExprList();
        if (funConflictMap.containsKey(funCallOp.getId().getLessema()))
            funCallOp.getId().setLessema(funConflictMap.get(funCallOp.getId().getLessema()));

        code.append(funCallOp.getId().getLessema()).append("(");
        if (!args.isEmpty()) {
            AtomicInteger i = new AtomicInteger(); // Contatore per la posizione dell'argomento
            args.forEach(arg -> {
                setStmt(arg, false);
                if(funSignatureMap.get(funCallOp.getId().getLessema()) != null) {
                    if (funSignatureMap.get(funCallOp.getId().getLessema()).get(i.get()).contains("*") && !arg.getType().equals("string")) {
                        code.append("&");
                    }
                }
                i.getAndIncrement();
                arg.accept(this);
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
            code.setCharAt(code.length() - 1, ')');
        }
        else {
            code.append(")");
        }

        if (isStmt)
            code.append(";\n");
    }

    @Override
    public void visit(WhileOp whileOp) {
        code.append("while (");
        whileOp.getCondition().accept(this);
        code.append(") {\n");
        whileOp.getBody().accept(this);
        code.append("}\n");
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        parDeclOp.getPVars().forEach(pVar -> {
            typeTemp = isRefConvert(parDeclOp.getType());
            pVar.accept(this);
        });
    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {
        code.append("if (");
        ifThenElseOp.getCondition().accept(this);
        code.append(") {\n");
        ifThenElseOp.getThenBranch().accept(this);
        code.append("}\nelse {\n");
        ifThenElseOp.getElseBranch().accept(this);
        code.append("}\n");
    }

    @Override
    public void visit(PVarOp pVarOp) {
        String refTemp = pVarOp.isRef() ? typeTemp.equals("string") ? "" : "*" : "";
        code.append(typeTemp).append(" ");
        code.append(refTemp).append(" ");
        code.append(pVarOp.getId().getLessema()).append(", ");
    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {
        if(varOptInitOp.getType().equals("string")) {
            code.append('*');
            code.append(varOptInitOp.getId().getLessema());
            if(!isGlobal) {
                code.append(" = allocate_string(256)");
                if (varOptInitOp.getExprOp() != null) {
                    code.append(";\nstrcpy(").append(varOptInitOp.getId().getLessema()).append(", ");
                    setStmt(varOptInitOp.getExprOp(), false);
                    varOptInitOp.getExprOp().accept(this);
                    code.append(")");
                }
            }
        }
        else {
            code.append(varOptInitOp.getId().getLessema());
            if (varOptInitOp.getExprOp() != null) {
                code.append(" = ");
                setStmt(varOptInitOp.getExprOp(), false);
                varOptInitOp.getExprOp().accept(this);
            }
        }
    }

//    public void visit(StatementOp stmt){
//        switch(stmt.getClass().getSimpleName()) {
//            case "AssignOp" -> visit((AssignOp) stmt);
//            case "IfThenOp" -> visit((IfThenOp) stmt);
//            case "IfThenElseOp" -> visit((IfThenElseOp) stmt);
//            case "WhileOp" -> visit((WhileOp) stmt);
//            case "ReturnOp" -> visit((ReturnOp) stmt);
//            case "WriteOp" -> visit((WriteOp) stmt);
//            case "ReadOp" -> visit((ReadOp) stmt);
//            case "FunCallOp" -> visit((FunCallOp) stmt);
//        }
//    }

    @Override
    public void visit(AssignOp assignOp) {
        List<Identifier> idList = assignOp.getIdentfiers();
        List<ExprOp> exprList = assignOp.getExpressions();

        for (int i = 0; i < idList.size(); i++) {
            idList.get(i).accept(this);
            code.append(" = ");

            setStmt(exprList.get(i), false);

            exprList.get(i).accept(this);
            code.append(", ");
        }
//        if(!(exprList.get(0) instanceof FunCallOp)) {
//            code.append(";\n");
//        }
        code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        code.append(";\n");
    }

//    public void visit(ExprOp expr) {
//        switch(expr.getClass().getSimpleName()) {
//            case "BinaryExprOp" -> visit((BinaryExprOp) expr);
//            case "UnaryExprOp" -> visit((UnaryExprOp) expr);
//            case "ConstOp" -> visit((ConstOp) expr);
//            case "Identifier" -> visit((Identifier) expr);
//            case "FunCallOp" -> visit((FunCallOp) expr);
//        }
//    }

    @Override
    public void visit(Identifier identifier) {
        if(identifier.getType().startsWith("ref") && !identifier.getType().equals("ref string")) {
            code.append("*");
        }
        code.append(identifier.getLessema());
    }

    @Override
    public void visit(ConstOp constOp) {
        if (constOp.getType().equals("string")) {
            code.append("\"").append(constOp.getValue()).append("\"");
        }
        else
            code.append(constOp.getValue());
    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {
        code.append("(");
        setStmt(binaryExprOp.getLeft(), false);
        binaryExprOp.getLeft().accept(this);

        code.append(" ").append(convertOp(binaryExprOp.getOp())).append(" ");

        setStmt(binaryExprOp.getRight(), false);
        binaryExprOp.getRight().accept(this);
        code.append(")");
    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {
        code.append(convertOp(unaryExprOp.getOp()));
        setStmt(unaryExprOp.getExpr(), false);
        unaryExprOp.getExpr().accept(this);
    }

    @Override
    public void visit(ReturnOp returnOp) {
        code.append("return ");
        setStmt(returnOp.getExpr(), false);
        returnOp.getExpr().accept(this);
        code.append(";\n");
    }

    @Override
    public void visit(WriteOp writeOp) {
        List<ExprOp> exprList = writeOp.getExprList();
        code.append("printf(\"");
        exprList.forEach(expr -> { getFormatSpecifier(expr.getType()); });
        if(writeOp.getNewLine() != null)
            code.append("%c");

        code.append("\"");

        if (!exprList.isEmpty()) {
            code.append(", ");
            exprList.forEach(expr -> {
                setStmt(expr, false);
                expr.accept(this);
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        }
        if(writeOp.getNewLine() != null) {
            code.append(", ");
            code.append("'\\n\'").append("\'");
        }
        code.setCharAt(code.length() - 1, ')');
        code.append(";\n");
    }

    @Override
    public void visit(ReadOp readOp) {
        List<Identifier> idList = readOp.getIdentifiers();
        code.append("scanf(\"");
        idList.forEach(id -> { getFormatSpecifier(id.getType()); });
        code.append("\"");

        if (!idList.isEmpty()) {
            code.append(", ");
            idList.forEach(id -> {
                if(!id.getType().equals("string")) code.append("&");
                id.accept(this);
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        }
        code.setCharAt(code.length() - 1, ')');
        code.append(";\n");
    }

    private void resolveNameConflicts(List<Object> listDecls) {
        for (Object op1 : listDecls) {
            // Considera solo le operazioni che rappresentano una funzione
            if (!(op1 instanceof FunDeclOp functionOp)) {
                continue;
            }

            for (Object op2 : listDecls) {
                // Considera solo le operazioni che rappresentano una variabile
                if (!(op2 instanceof VarDeclOp variableOp)) {
                    continue;
                }

                // Per ogni inizializzazione della variabile, controlla il nome
                for (VarOptInitOp varOptInitOp : variableOp.getListVarOptInit()) {
                    String functionName = functionOp.getId().getLessema();
                    String variableName = varOptInitOp.getId().getLessema();

                    // Se i nomi coincidono, modifica il nome della funzione
                    if (functionName.equals(variableName)) {
                        functionOp.getId().setLessema(functionName + "_fun");
                        // aggiunge alla mappa il vecchio nome della funzione e il nuovo nome
                        funConflictMap.put(functionName, functionName + "_fun");
                        //functionOp.accept(this);
                    }
                }
            }
        }
    }

    private void appendFunctionSignatures(List<Object> listDecls) {
        // Itera su tutte le operazioni (dichiarazioni di variabili o funzioni)
        for (Object obj : listDecls) {
            // Considera solo gli oggetti che sono dichiarazioni di funzione
            if (!(obj instanceof FunDeclOp functionOp)) {
                continue;
            }

            // Appende la parte iniziale della firma: [tipo di ritorno] [nome funzione](
            code.append(isRefConvert(functionOp.getType()))
                    .append(" ")
                    .append(functionOp.getId().getLessema())
                    .append("(");

            // Usa il nuovo metodo per ottenere la lista dei parametri formattata
            String paramList = buildParameterString(functionOp.getParams());
            code.append(paramList);

            // Completa la firma della funzione
            code.append(");\n");
        }
    }

    private String isRefConvert(String type) {
        // se la stringa inizia con ref allora resituisce la stringa senza ref e con *
        if(type.startsWith("ref")) {
            return type.substring(3) + "*";
        }
        else if(type.equals("string")) {
            return "char*";
        }
        return type;
    }

    private String buildParameterString(List<ParDeclOp> params) {
        StringJoiner paramJoiner = new StringJoiner(", ");

        if (params != null) {
            params.forEach(parDeclOp ->
                    parDeclOp.getPVars().forEach(pVarOp -> {
                        // Se il parametro è di tipo "string", lo mappiamo a "char*"
                        String paramType = parDeclOp.getType().equals("string") ? "char*" : parDeclOp.getType();

                        // Costruiamo la stringa per il parametro
                        StringBuilder paramBuilder = new StringBuilder(paramType);

                        // Se il parametro viene passato per riferimento, aggiungiamo "*" salvo il caso di stringa
                        if (pVarOp.isRef()) {
                            // Per i parametri di tipo "string" si mantiene solo lo spazio, gia abbiamo messo "char*"
                            paramBuilder.append(parDeclOp.getType().equals("string") ? " " : "* ");
                        } else {
                            paramBuilder.append(" ");
                        }

                        // Aggiungiamo il nome del parametro
                        paramBuilder.append(pVarOp.getId().getLessema());

                        // Inseriamo il parametro nello StringJoiner
                        paramJoiner.add(paramBuilder.toString());
                    })
            );
        }

        return paramJoiner.toString();
    }


    private void getFormatSpecifier(String type) {
        switch (type) {
            case "int", "bool" -> code.append("%d");
            case "float" -> code.append("%f");
            case "char" -> code.append("%c");
            case "double" -> code.append("%lf");
            case "string" -> code.append("%s");
        }
    }

    private String convertOp(String op){
        return switch (op) {
            case "and" -> "&&";
            case "or" -> "||";
            case "not" -> "!";
            default -> op;
        };
    }

    private List<String> extractParameterType(String parameters) {
        List<String> types = new ArrayList<>();

        // Rimuove eventuali spazi bianchi all'inizio e alla fine
        parameters = parameters.trim();

        // Divide la stringa usando la virgola come separatore
        String[] paramArray = parameters.split(",");

        for (String param : paramArray) {
            // Rimuove gli spazi bianchi interni
            param = param.trim();

            // Divide per lo spazio per separare il tipo dal nome
            String[] parts = param.split(" ");

            if (parts.length >= 2) {
                // Ricompone il tipo (gestione di puntatori es. "double*")
                StringBuilder typeBuilder = new StringBuilder();

                for (int i = 0; i < parts.length - 1; i++) {
                    if (i > 0) typeBuilder.append(" "); // Aggiunge spazio tra i tipi multipli
                    typeBuilder.append(parts[i]);
                }

                types.add(typeBuilder.toString());
            }
        }

        return types;
    }

    private void setStmt(Object obj, boolean flag) {
        if(obj instanceof FunCallOp)
            isStmt = flag;
    }

    void printMallocFun() {
        // **Definizione delle funzioni di gestione delle stringhe**
        code.append("// Funzione per allocare dinamicamente una stringa\n");
        code.append("char* allocate_string(size_t size) {\n");
        code.append("    char* str = (char*)malloc(size * sizeof(char));\n");
        code.append("    if (str == NULL) {\n");
        code.append("        printf(\"Errore di allocazione!\\n\");\n");
        code.append("        exit(1);\n");
        code.append("    }\n");
        code.append("    str[0] = '\\0';  // Inizializza la stringa vuota\n");
        code.append("    return str;\n");
        code.append("}\n\n");
    }

    void printReallocFun() {
        code.append("// Funzione per riallocare dinamicamente una stringa\n");
        code.append("char* reallocate_string(char* str, size_t new_size) {\n");
        code.append("    char* temp = (char*)realloc(str, new_size * sizeof(char));\n");
        code.append("    if (temp == NULL) {\n");
        code.append("        printf(\"Errore di riallocazione!\\n\");\n");
        code.append("        exit(1);\n");
        code.append("    }\n");
        code.append("    return temp;\n");
        code.append("}\n\n");
    }
}
