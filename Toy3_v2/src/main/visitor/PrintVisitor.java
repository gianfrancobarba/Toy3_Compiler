package main.visitor;

import main.nodes.declarations.*;
import main.nodes.expr.*;
import main.nodes.common.Identifier;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.ConstOp;
import main.nodes.types.TypeOp;

public class PrintVisitor implements Visitor {

    private int indentLevel = 0;  // Per gestire l'indentazione

    // Metodo per stampare l'indentazione corretta
    private void printIndent() {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print('\t');  // Due spazi per ogni livello di indentazione
        }
    }

    public void visit(TypeOp typeOp) {
        printIndent();
        System.out.println("TypeOp: " + typeOp.getTypeName());
    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {
        printIndent();
        System.out.println("UnaryOp (" + unaryExprOp.getOp() + "):");
        indentLevel++;
        unaryExprOp.getExpr().accept(this);
        indentLevel--;
    }

    @Override
    public void visit(Identifier node) {
        printIndent();
        System.out.println("id: " + node.getLessema());
    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {
        printIndent();
        System.out.println("BinaryOp (" + binaryExprOp.getOp() + "):");
        indentLevel++;
        binaryExprOp.getLeft().accept(this);
        binaryExprOp.getRight().accept(this);
        indentLevel--;

    }

    public void visit(ConstOp constOp) {
        printIndent();
        System.out.println("const: " + constOp.getValue());
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {

        printIndent();
        System.out.println("VarDeclOp: ");

        indentLevel++;
        for(VarOptInitOp varOptInitOp : varDeclOp.getListVarOptInit()) {
            varOptInitOp.accept(this);
        }

        if(varDeclOp.getTypeOrConstant() instanceof TypeOp) {
            ((TypeOp) varDeclOp.getTypeOrConstant()).accept(this);
        } else if(varDeclOp.getTypeOrConstant() instanceof ExprOp) {
            ((ExprOp) varDeclOp.getTypeOrConstant()).accept(this);
        }

        indentLevel--;
    }

    @Override
    public void visit(AssignOp assignOp) {
        printIndent();
        System.out.println("AssignOp: ");
        indentLevel++;

        for(Identifier id : assignOp.getIdentfiers()) {
            id.accept(this);
        }

        for(ExprOp expr : assignOp.getExpressions()) {
            expr.accept(this);
        }

        indentLevel--;

    }

    @Override
    public void visit(FunDeclOp funDeclOp) {

        printIndent();
        System.out.println("DefDeclOp: ");

        indentLevel++;

        funDeclOp.getId().accept(this);

        if(funDeclOp.getParams() != null)
            for(ParDeclOp parDeclOp : funDeclOp.getParams()) {
                parDeclOp.accept(this);
          }

        if(funDeclOp.getOptType() != null) {
            funDeclOp.getOptType().accept(this);
        }
        else {
            printIndent();
            System.out.println("OptType: void");
        }

        if(funDeclOp.getBody() != null) {
            funDeclOp.getBody().accept(this);
        }

        indentLevel--;

    }

    @Override
    public void visit(FunCallOp funCallOp) {

        printIndent();
        System.out.println("FunCallOp: ");

        indentLevel++;

        funCallOp.getId().accept(this);

        if(funCallOp.getExprList() != null)
            for(ExprOp expr : funCallOp.getExprList()) {
                expr.accept(this);
            }

        indentLevel--;
    }

    @Override
    public void visit(ProgramOp programOp) {

        printIndent();
        System.out.println("ProgramOp: ");

        indentLevel++;

        for(Object varDeclOp : programOp.getListDecls()) {
            if(varDeclOp instanceof VarDeclOp) {
                ((VarDeclOp) varDeclOp).accept(this);
            }
            else if(varDeclOp instanceof FunDeclOp) {
                ((FunDeclOp) varDeclOp).accept(this);
            }
        }

        programOp.getBeginEndOp().accept(this);

        indentLevel--;
    }

    @Override
    public void visit(ReturnOp returnOp) {
        printIndent();
        System.out.println("ReturnOp: ");

        indentLevel++;

        if(returnOp.getExpr() != null) {
            returnOp.getExpr().accept(this);
        }

        indentLevel--;

    }

    @Override
    public void visit(WhileOp whileOp) {

        printIndent();
        System.out.println("WhileOp: ");

        indentLevel++;

        whileOp.getCondition().accept(this);
        whileOp.getBody().accept(this);

        indentLevel--;
    }

    @Override
    public void visit(ParDeclOp parDeclOp) {
        printIndent();
        System.out.println("ParDeclOp: ");

        indentLevel++;

        if(parDeclOp.getPVars() != null)
            for (PVarOp pVarOp : parDeclOp.getPVars()) {
                pVarOp.accept(this);
            }

        if(parDeclOp.getType() != null) {
            parDeclOp.getType().accept(this);
        }

        System.out.println("Type: " + parDeclOp.getType().getTypeName());
        indentLevel--;

    }

    @Override
    public void visit(ReadOp readOp) {
        printIndent();
        System.out.println("ReadOp: ");

        indentLevel++;

        for(Identifier id : readOp.getIdenfiers()) {
            id.accept(this);
        }

        indentLevel--;

    }

    @Override
    public void visit(WriteOp writeOp) {

        printIndent();
        System.out.println("WriteOp: ");

        indentLevel++;

        for(ExprOp expr : writeOp.getExprList()) {
            expr.accept(this);
        }

        if(writeOp.getNewLine() != null) {
            printIndent();
            System.out.println("NewLine");
        }

        indentLevel--;

    }

    @Override
    public void visit(IfThenElseOp ifThenElseOp) {

        printIndent();
        System.out.println("IfThenElseOp: ");

        indentLevel++;

        ifThenElseOp.getCondition().accept(this);
        ifThenElseOp.getThenBranch().accept(this);
        ifThenElseOp.getElseBranch().accept(this);

        indentLevel--;

    }

    @Override
    public void visit(PVarOp pVarOp) {
        printIndent();
        System.out.println("PVarOp: ");

        indentLevel++;

        pVarOp.getId().accept(this);

        if(pVarOp.isRef()) {
            printIndent();
            System.out.println("IsRef");
        }

        indentLevel--;

    }

    @Override
    public void visit(VarOptInitOp varOptInitOp) {

        printIndent();
        System.out.println("VarOptInitOp: ");

        indentLevel++;

        varOptInitOp.getId().accept(this);

        if(varOptInitOp.getExprOp() != null) {
            varOptInitOp.getExprOp().accept(this);
        }

        indentLevel--;

    }

    @Override
    public void visit(BeginEndOp beginEndOp) {

        printIndent();
        System.out.println("BeginEndOp: ");

        indentLevel++;

        for(VarDeclOp varDeclOp : beginEndOp.getVarDeclList()) {
            varDeclOp.accept(this);
        }

        for(StatementOp statementOp : beginEndOp.getStmtList()) {
            statementOp.accept(this);
        }

        indentLevel--;
    }

    @Override
    public void visit(IfThenOp ifThenOp) {

        printIndent();
        System.out.println("IfThenOp: ");

        indentLevel++;

        ifThenOp.getCondition().accept(this);
        ifThenOp.getThenBranch().accept(this);

        indentLevel--;

    }

    @Override
    public void visit(BodyOp bodyOp) {

        printIndent();
        System.out.println("BodyOp: ");

        indentLevel++;

        for(VarDeclOp varDeclOp : bodyOp.getVarDecls()) {
            varDeclOp.accept(this);
        }

        for(StatementOp statementOp : bodyOp.getStatements()) {
            statementOp.accept(this);
        }

        indentLevel--;

    }
}
