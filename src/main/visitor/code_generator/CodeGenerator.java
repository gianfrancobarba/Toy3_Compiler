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

    public CodeGenerator(String outputFileName) throws IOException {
        this.code = new StringBuilder();
        this.writer = new BufferedWriter(new FileWriter("test_files/c_out/"+ outputFileName + ".c"));
    }

    @Override
    public void visit(ProgramOp programOp) {
        code.append("#include <stdio.h>\n");
        code.append("#include <string.h>\n");
        code.append("#include <stdlib.h>\n");
        code.append("#include <stdbool.h>\n\n");
        code.append("\n\n // DICHIARAZIONI FUNZIONI E VARIABILI DI SERVIZIO\n\n");
        code.append("#define INITIAL_SIZE 32\n");
        code.append("#define INCREMENT_SIZE 32\n");
        code.append("#define MAX_CONCAT_SIZE 1024\n\n");

        code.append("char* tempString;\n");
        code.append("char* arrayConcat[MAX_CONCAT_SIZE];\n");
        code.append("int currentIndex = 0;\n\n");

        printAllocateStringFun();
        printReallocateStringFun();
        printSafeStrcpyFun();
        printSafeStrcatFun();
        printSafeScanfFun();
        printToStringFun();

        code.append("\n\n\n// INIZIO DEL CODICE GENERATO\n\n");

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
        code.append("allocate_string(&tempString, 1);\n");

        globalStringAllocate();

        code.append("for (int i = 0; i < MAX_CONCAT_SIZE; i++) {\n");
        code.append("    allocate_string(&arrayConcat[i], 256);\n");
        code.append("}\n\n");

        List<VarDeclOp> listVarDecl = beginEndOp.getVarDeclList();
        listVarDecl.forEach(varDeclOp -> varDeclOp.accept(this));
        beginEndOp.getStmtList().forEach(statementOp -> {
            setStmt(statementOp, true);
            statementOp.accept(this);
        });

        listStringGlobal.forEach(varDeclOp -> {
            varDeclOp.getListVarOptInit().forEach(varOpt -> {
                code.append("free(").append(varOpt.getId().getLessema()).append(");\n");
            });
        });

        code.append("\n\n\n");
        listVarDecl.forEach(varDecl -> {
            if(varDecl.getType().equals("string")) {
                varDecl.getListVarOptInit().forEach(varOpt -> {
                    code.append("free(").append(varOpt.getId().getLessema()).append(");\n");
                });
            }
        });
        // Libera la memoria allocata per le stringhe e buffer di temp
        code.append("free(tempString);\n");
        code.append("for (int i = 0; i < MAX_CONCAT_SIZE; i++) {\n");
        code.append("    free(arrayConcat[i]);\n");
        code.append("}\n");
        code.append("\nreturn 0;\n}");
    }

    private void globalStringAllocate() {
        listStringGlobal.forEach(varDeclOp -> {
            varDeclOp.getListVarOptInit().forEach(varOpt -> {
                code.append("allocate_string(&").append(varOpt.getId().getLessema()).append(", 256);\n");
                if(varOpt.getExprOp() != null) {
                    code.append("safe_strcpy(&").append(varOpt.getId().getLessema()).append(", ");
                    setStmt(varOpt.getExprOp(), false);
                    varOpt.getExprOp().accept(this);
                    code.append(");\n");
                }
            });
            if (varDeclOp.getTypeOrConstant() instanceof ConstOp con) {
                code.append("safe_strcpy(&").append(varDeclOp.getListVarOptInit().get(0).getId().getLessema()).append(", ");
                con.accept(this);
                code.append(");\n");
            }
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
                    code.append("safe_strcpy(&").append(varDeclOp.getListVarOptInit().get(0).getId().getLessema()).append(", ");
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
        List<VarDeclOp> listVarDecl = bodyOp.getVarDecls();
        listVarDecl.forEach(varDecl -> varDecl.accept(this));
        bodyOp.getStatements().forEach(stmt -> {
            setStmt(stmt, true);
            stmt.accept(this);
        });

        listVarDecl.forEach(varDecl -> {
            if(varDecl.getType().equals("string")) {
                varDecl.getListVarOptInit().forEach(varOpt -> {
                    code.append("free(").append(varOpt.getId().getLessema()).append(");\n");
                });
            }
        });

    }

    @Override
    public void visit(FunDeclOp funDeclOp) {
        code.append(typeConvert(extractType(funDeclOp.getType())));
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

        // Salviamo il valore originale di isStmt
        boolean originalIsStmt = isStmt;

        if (funConflictMap.containsKey(funCallOp.getId().getLessema()))
            funCallOp.getId().setLessema(funConflictMap.get(funCallOp.getId().getLessema()));

        code.append(funCallOp.getId().getLessema()).append("(");

        if (!args.isEmpty()) {
            AtomicInteger i = new AtomicInteger();
            args.forEach(arg -> {
                setStmt(arg, false); // Gli argomenti NON sono istruzioni
                if (funSignatureMap.get(funCallOp.getId().getLessema()) != null) {
                    if (funSignatureMap.get(funCallOp.getId().getLessema()).get(i.get()).contains("*") && !arg.getType().equals("string")) {
                        code.append("&");
                    }
                }
                i.getAndIncrement();
                arg.accept(this);
                code.append(", ");
            });

            code.deleteCharAt(code.length() - 2);
            code.setCharAt(code.length() - 1, ')');
        } else {
            code.append(")");
        }

        // Ripristiniamo il valore originale di isStmt
        isStmt = originalIsStmt;

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
            typeTemp = typeConvert(parDeclOp.getType());
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
        String id = varOptInitOp.getId().getLessema();
        if(varOptInitOp.getType().equals("string")) {
            code.append('*').append(id);
            if(!isGlobal) {
                code.append(";\nallocate_string(&").append(id).append(", 256)");
                if (varOptInitOp.getExprOp() != null) {
                    code.append(";\nsafe_strcpy(&").append(id).append(", ");
                    setStmt(varOptInitOp.getExprOp(), false);
                    varOptInitOp.getExprOp().accept(this);
                    code.append(")");
                }
            }
        }
        else {
            code.append(id);
            if (varOptInitOp.getExprOp() != null) {
                code.append(" = ");
                setStmt(varOptInitOp.getExprOp(), false);
                varOptInitOp.getExprOp().accept(this);
            }
        }
    }

    @Override
    public void visit(AssignOp assignOp) {
        List<Identifier> idList = assignOp.getIdentfiers();
        List<ExprOp> exprList = assignOp.getExpressions();

        for (int i = 0; i < idList.size(); i++) {
            if(exprList.get(i).getType().equals("string")) {
                code.append("safe_strcpy(&");
                idList.get(i).accept(this);
                code.append(", ");
                setStmt(exprList.get(i), false);
                exprList.get(i).accept(this);
                code.append(" ), ");
            }
            else {
                idList.get(i).accept(this);
                code.append(" = ");

                setStmt(exprList.get(i), false);

                exprList.get(i).accept(this);
                code.append(", ");
            }
        }

        code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        code.setCharAt(code.length() - 1, ';');
        code.append("\n");
    }

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
        boolean isLeftString = binaryExprOp.getLeft().getType().equals("string");
        boolean isRightString = binaryExprOp.getRight().getType().equals("string");

        // Se uno dei due operandi è una stringa, allora si tratta di una concatenazione
        if (isLeftString || isRightString) {
            if (binaryExprOp.getType().equals("bool")) { // Se il tipo è booleano, allora si tratta di un confronto tra stringhe
                // Evita di aggiungere ulteriori elementi dopo il confronto
                getStringComparison(binaryExprOp.getLeft(), binaryExprOp.getOp(), binaryExprOp.getRight());
                return;
            } else { // Altrimenti si tratta di una concatenazione di stringhe
                code.append("strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(");

                if (!isLeftString) { // Se il primo operando non è una stringa, lo converte in stringa
                    toStringType(binaryExprOp.getLeft());
                    binaryExprOp.getLeft().accept(this);
                    code.append("},\"").append(binaryExprOp.getLeft().getType()).append("\")");
                } else { // Altrimenti visita normalmente il primo operando
                    binaryExprOp.getLeft().accept(this);
                }

                code.append(", ");

                if (!isRightString) {
                    toStringType(binaryExprOp.getRight());
                    binaryExprOp.getRight().accept(this);
                    code.append("}, \"").append(binaryExprOp.getRight().getType()).append("\")");
                } else { // Altrimenti visita normalmente il secondo operando
                    binaryExprOp.getRight().accept(this);
                }
                code.append("))");
            }
        } else { // Altrimenti si tratta di una normale operazione binaria
            code.append("(");
            setStmt(binaryExprOp.getLeft(), false);
            binaryExprOp.getLeft().accept(this);
            code.append(" ").append(convertOp(binaryExprOp.getOp())).append(" ");

            setStmt(binaryExprOp.getRight(), false);
            binaryExprOp.getRight().accept(this);
            code.append(")");
        }
    }

    private void toStringType(ExprOp expr) {
        if (expr instanceof ConstOp) {
            switch (expr.getType()) {
                case "int" -> code.append("to_string((int[]){");
                case "double" -> code.append("to_string((double[]){");
                case "char" -> code.append("to_string((char[]){");
                case "bool" -> code.append("to_string((bool[]){");
            }
        } else if (expr instanceof BinaryExprOp || expr instanceof UnaryExprOp) {
            if (!expr.getType().equals("string")) {
                switch (expr.getType()) {
                    case "int" -> code.append("to_string((int[]){");
                    case "double" -> code.append("to_string((double[]){");
                    case "char" -> code.append("to_string((char[]){");
                    case "bool" -> code.append("to_string((bool[]){");
                }
            }
        }
        else if (expr instanceof FunCallOp funCall) {
            switch (funCall.getType()) {
                case "int" -> code.append("to_string((int[]){");
                case "double" -> code.append("to_string((double[]){");
                case "char" -> code.append("to_string((char[]){");
                case "bool" -> code.append("to_string((bool[]){");
            }
        }
        else if (expr instanceof Identifier id) {
            switch (id.getType()) {
                case "int" -> code.append("to_string((int[]){");
                case "double" -> code.append("to_string((double[]){");
                case "char" -> code.append("to_string((char[]){");
                case "bool" -> code.append("to_string((bool[]){");
            }
        }
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
        exprList.forEach(expr -> {
            getFormatSpecifier(typeConvert(expr.getType()));
        });
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
        idList.forEach(id -> {
            if(id.getType().equals("string")) {
                code.append("safe_scanf(&");
                id.accept(this);
            }
            else {
                code.append("scanf(\"");
                getFormatSpecifier(typeConvert(id.getType()));
                code.append("\", &");
                id.accept(this);
            }
            code.append(");\n");
        });
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

            code.append(typeConvert((extractType(functionOp.getType()))))
                    .append(" ")
                    .append(functionOp.getId().getLessema())
                    .append("(");

            String paramList = buildParameterString(functionOp.getParams());
            code.append(paramList);

            // creiamo la mappa signature con il nome della funzione e la lista dei parametri
            String funName = functionOp.getId().getLessema(); // dovrebbe essere gia sistemato con _fun in teoria
            funSignatureMap.put(funName, extractParameterType(paramList));
            // Completa la firma della funzione
            code.append(");\n");
        }
    }

    private String typeConvert(String type) {
        // se la stringa inizia con ref allora resituisce la stringa senza ref e con *
        if(type.equals("string") || type.equals("ref string")) {
            return "char*";
        }
        else if(type.startsWith("ref")) {
            return type.concat("*").replace("ref ", "");
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
            case "int", "bool", "int*", "bool*" -> code.append("%d");
            case "float", "float*" -> code.append("%f");
            case "char" -> code.append("%c");
            case "double", "double*" -> code.append("%lf");
            case "char*" -> code.append("%s");
        }
    }

    private String convertOp(String op){
        return switch (op) {
            case "and" -> "&&";
            case "or" -> "||";
            case "not" -> "!";
            case "<>" -> "!=";
            default -> op;
        };
    }

    private void getStringComparison(ExprOp left, String op, ExprOp right) {
        code.append("(strcmp(");
        setStmt(left, false);
        left.accept(this);
        code.append(", ");
        setStmt(right, false);
        right.accept(this);
        code.append(") ");
        switch (op) {
            case "==" -> code.append("== 0");
            case "<>" -> code.append("!= 0");
            case "<" -> code.append("< 0");
            case "<=" -> code.append("<= 0");
            case ">" -> code.append("> 0");
            case ">=" -> code.append(">= 0");
        }
        code.append(")");
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

    private String extractType(String type) {
        return type.split("\\(")[0]; // Prende tutto prima di "("
    }

    private void setStmt(Object obj, boolean flag) {
        if(obj instanceof FunCallOp)
            isStmt = flag;
    }

    void printAllocateStringFun() {
        code.append("// Funzione per allocare dinamicamente una stringa\n");
        code.append("void allocate_string(char** str, size_t size) {\n");
        code.append("    *str = (char *) malloc(size);\n");
        code.append("    if (*str == NULL) {\n");
        code.append("        printf(\"Errore di allocazione!\\n\");\n");
        code.append("        exit(1);\n");
        code.append("    }\n");
        code.append("    (*str)[0] = '\\0'; // Inizializza la stringa vuota\n");
        code.append("}\n\n");
    }


    void printReallocateStringFun() {
        code.append("// Funzione per riallocare dinamicamente una stringa\n");
        code.append("char* reallocate_string(char* str, size_t new_size) {\n");
        code.append("    char* temp = (char*) realloc(str, new_size);\n");
        code.append("    if (temp == NULL) {\n");
        code.append("        printf(\"Errore di riallocazione!\\n\");\n");
        code.append("        free(str);\n");
        code.append("        exit(1);\n");
        code.append("    }\n");
        code.append("    return temp;\n");
        code.append("}\n\n");
    }


    void printSafeStrcpyFun() {
        code.append("// Funzione per copiare una stringa in modo sicuro con gestione dinamica della memoria\n");
        code.append("void safe_strcpy(char** dest, const char* src) {\n");
        code.append("    if (!src) return;\n");
        code.append("    size_t src_len = strlen(src) + 1;\n");
        code.append("    *dest = (char*) realloc(*dest, src_len);\n");
        code.append("    if (*dest == NULL) {\n");
        code.append("        printf(\"Errore di allocazione in safe_strcpy!\\n\");\n");
        code.append("        exit(1);\n");
        code.append("    }\n");
        code.append("    strcpy(*dest, src);\n");
        code.append("}\n\n");
    }


    void printSafeStrcatFun() {
        code.append("// Funzione per concatenare due stringhe in modo sicuro con gestione dinamica della memoria\n");
        code.append("char* safe_strcat(const char* s1, const char* s2) {\n");
        code.append("    if (!s1 && !s2) return NULL;\n");
        code.append("    if (!s1) return strdup(s2);\n");
        code.append("    if (!s2) return strdup(s1);\n");
//        code.append("    printf(\"%s -> %s \\n \", s1,s2);\n");
        code.append("    size_t len1 = strlen(s1);\n");
        code.append("    size_t len2 = strlen(s2);\n");

        code.append("    char* new_temp;\n");
        code.append("    allocate_string(&new_temp, len1 + len2 + 1);\n");

        code.append("    strcpy(new_temp, s1);\n");
        code.append("    strcat(new_temp, s2);\n");

        code.append("    safe_strcpy(&tempString, new_temp);\n");
        code.append("    free(new_temp);\n");

        code.append("    return tempString;\n");
        code.append("}\n\n");
    }


    void printSafeScanfFun() {
        code.append("// Funzione per leggere una stringa dinamica evitando problemi di buffer\n");
        code.append("void safe_scanf(char** dest) {\n");
        code.append("    size_t size = INITIAL_SIZE;\n");

        code.append("    if (*dest == NULL) {\n");
        code.append("        allocate_string(dest, size);\n");
        code.append("    } else {\n");
        code.append("        *dest = (char*) realloc(*dest, size);\n");
        code.append("    }\n");

        code.append("    if (*dest == NULL) {\n");
        code.append("        printf(\"Errore di allocazione!\\n\");\n");
        code.append("        exit(1);\n");
        code.append("    }\n");

        code.append("    size_t len = 0;\n");
        code.append("    int c;\n");

        code.append("    while ((c = getchar()) == '\\n'){}\n");
        code.append("    if (c != EOF) {\n");
        code.append("        (*dest)[len++] = (char)c;\n");
        code.append("    }\n");

        code.append("    while ((c = fgetc(stdin)) != '\\n' && c != EOF) {\n");
        code.append("        if (len + 1 >= size) {\n");
        code.append("            size += INCREMENT_SIZE;\n");
        code.append("            *dest = (char*) realloc(*dest, size);\n");
        code.append("        }\n");
        code.append("        (*dest)[len++] = (char)c;\n");
        code.append("    }\n");

        code.append("    (*dest)[len] = '\\0'; // Termina correttamente la stringa\n");
        code.append("}\n\n");
    }


    void printToStringFun() {
        code.append("// Funzione per convertire diversi tipi in stringa\n");
        code.append("const char* to_string(void* value, const char* type) {\n");
        code.append("    static char buffer[INITIAL_SIZE]; // Buffer statico condiviso\n");

        code.append("    if (strcmp(type, \"int\") == 0) {\n");
        code.append("        snprintf(buffer, INITIAL_SIZE, \"%d\", *(int*)value);\n");

        code.append("    } else if (strcmp(type, \"double\") == 0) {\n");
        code.append("        snprintf(buffer, INITIAL_SIZE, \"%.6f\", *(double*)value);\n");

        code.append("    } else if (strcmp(type, \"bool\") == 0) {\n");
        code.append("        return *(int*)value ? \"true\" : \"false\";\n");

        code.append("    } else if (strcmp(type, \"char\") == 0) {\n");
        code.append("        buffer[0] = *(char*)value;\n");
        code.append("        buffer[1] = '\\0';\n");

        code.append("    } else if (strcmp(type, \"string\") == 0) {\n");
        code.append("        return (char*)value;\n");

        code.append("    } else {\n");
        code.append("        return \"UNKNOWN\";\n");
        code.append("    }\n");
        code.append("    return buffer;\n");
        code.append("}\n\n");
    }
}