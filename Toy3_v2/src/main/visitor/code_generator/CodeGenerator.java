package main.visitor.code_generator;

import main.nodes.common.Identifier;
import main.nodes.declarations.*;
import main.nodes.expr.BinaryExprOp;
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
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator implements Visitor {
    private final StringBuilder code;
    private final BufferedWriter bufferedWriter;
    private Map<String, String> functionParameters = new HashMap<>();

    public CodeGenerator() throws IOException {
        code = new StringBuilder();
        bufferedWriter = new BufferedWriter(new FileWriter("file_tester/output.c"));
    }

    @Override
    public void visit(VarDeclOp varDeclOp) {

    }

    @Override
    public void visit(IfThenOp ifThenOp) {

    }

    @Override
    public void visit(BodyOp bodyOp) {

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
    public void visit(WhileOp whileOp) {

    }

    @Override
    public void visit(ParDeclOp parDeclOp) {

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

    }

    @Override
    public void visit(AssignOp assignOp) {

    }

    @Override
    public void visit(BinaryExprOp binaryExprOp) {

    }

    @Override
    public void visit(ReturnOp returnOp) {

    }

    @Override
    public void visit(WriteOp writeOp) {

    }

    @Override
    public void visit(ReadOp readOp) {

    }

    @Override
    public void visit(UnaryExprOp unaryExprOp) {

    }

    @Override
    public void visit(Identifier identifier) {

    }

    @Override
    public void visit(ConstOp constOp) {

    }
}