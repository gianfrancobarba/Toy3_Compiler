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


public class CodeGenerator implements Visitor {

    private final StringBuilder code;
    private final BufferedWriter writer;
    private String typeTemp;

    public CodeGenerator() throws IOException {
        this.code = new StringBuilder();
        this.writer = new BufferedWriter(new FileWriter("filetester/output.c"));
    }

    @Override
    public void visit(ProgramOp programOp) {
        code.append("#include <stdio.h>\n");
        code.append("#include <string.h>\n");
        code.append("#include <stdlib.h>\n\n");

        resolveNameConflicts(programOp.getListDecls());
        appendFunctionSignatures(programOp.getListDecls());

        // Itera su tutte le operazioni del programma (dichiarazioni di variabili e funzioni)
        // e processa quelle che rappresentano dichiarazioni di variabili.
        for (Object obj : programOp.getListDecls()) {
            // Se l'oggetto è una dichiarazione di variabile, visita l'oggetto per generare il codice
            if (obj instanceof VarDeclOp varDeclOp) {
                varDeclOp.accept(this);
            }
        }

        code.append("\n");

        // Processa il blocco principale del programma (Begin-End)
        // Visitando l'oggetto BeginEndOp si genera il codice relativo al corpo del programma.
        programOp.getBeginEndOp().accept(this);
        code.append("\n\n");

        // Itera nuovamente sulle operazioni del programma per processare le dichiarazioni di funzioni.
        for (Object obj : programOp.getListDecls()) {
            // Se l'oggetto è una dichiarazione di funzione, visita l'oggetto per generare il codice
            if (obj instanceof FunDeclOp funDeclOp) {
                funDeclOp.accept(this);
                code.append("\n");
            }
        }

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
        beginEndOp.getVarDeclList().forEach(varDeclOp -> varDeclOp.accept(this));
        beginEndOp.getStmtList().forEach(statementOp -> statementOp.accept(this));
        code.append("\nreturn 0;\n}");
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {

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

    }

    @Override
    public void visit(FunDeclOp funDeclOp) {
        code.append(funDeclOp.getType());
        code.append(" ");
        code.append(funDeclOp.getId().getLessema());
        code.append("(");

        List<ParDeclOp> params = funDeclOp.getParams();
        if(!params.isEmpty()) {
            for(ParDeclOp parDecl : params) {
                parDecl.accept(this);
            }
            code.deleteCharAt(code.length() - 2);  // Rimuove l'ultima virgola
        }
        code.append(") {\n");
        funDeclOp.getBody().accept(this);
        code.append("}\n");
    }

    @Override
    public void visit(FunCallOp funCallOp) {
        List<ExprOp> args = funCallOp.getExprList();
        code.append(funCallOp.getId().getLessema()).append("(");
        if (!args.isEmpty()) {
            args.forEach(arg -> {
                arg.accept(this);
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        }
        code.append(");\n");
        System.out.println(code); // Stampiamo il risultato
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
            typeTemp = parDeclOp.getType().equals("string") ? "char*" : pVar.getType();
            pVar.accept(this);
        });
    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {
        code.append("if (");
        ifThenElseOp.getCondition().accept(this);
        code.append(") {\n");
        ifThenElseOp.getThenBranch().accept(this);
        code.append("} else {\n");
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

    }



    @Override
    public void visit(AssignOp assignOp) {
        List<Identifier> idList = assignOp.getIdentfiers();
        List<ExprOp> exprList = assignOp.getExpressions();

        for (int i = 0; i < idList.size(); i++) {
            idList.get(i).accept(this);
            code.append(" = ");
            exprList.get(i).accept(this);
            code.append(", ");
        }
        code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola

        if(!(exprList.get(0) instanceof FunCallOp)) {
            code.append(";\n");
        }
    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {

    }

    @Override
    public void visit(ReturnOp returnOp) {
        code.append("return ");
        returnOp.getExpr().accept(this);
        code.append(";\n");
    }

    @Override
    public void visit(WriteOp writeOp) {
        List<ExprOp> exprList = writeOp.getExprList();
        code.append("printf(\"");
        exprList.forEach(expr -> { getFormatSpecifier(expr.getType()); });
        code.append("\"");

        if (!exprList.isEmpty()) {
            code.append(", ");
            exprList.forEach(expr -> {
                if(expr instanceof ConstOp con) {
                    con.accept(this);
                }
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        }
        code.append(");\n");
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
                if(id.getType().equals("string")) code.append("&");
                id.accept(this);
                code.append(", ");
            });
            code.deleteCharAt(code.length() - 2); // Rimuove l'ultima virgola
        }
        code.append(");\n");
    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {

    }

    @Override
    public void visit(Identifier identifier) {

    }

    @Override
    public void visit(ConstOp constOp) {
        if (constOp.getType().equals("string"))
            code.append("\"").append(constOp.getValue()).append("\"");
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
            code.append(functionOp.getType())
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
}
