package main.visitor;

import main.nodes.declarations.*;
import main.nodes.expr.*;
import main.nodes.common.Identifier;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.ConstOp;
import main.nodes.types.TypeOp;


public interface Visitor {

    void visit(UnaryExprOp unaryExprOp);

    void visit(Identifier identifier);

    void visit(BinaryExprOp binaryExprOp);

    void visit(VarDeclOp varDeclOp);

    void visit(AssignOp assignOp);

    void visit(IfThenOp ifThenOp);

    void visit(BodyOp bodyOp);

    void visit(FunDeclOp funDeclOp);

    void visit(FunCallOp funCallOp);

    void visit(ProgramOp programOp);

    void visit(ReturnOp returnOp);

    void visit(WhileOp whileOp);

    void visit(ParDeclOp parDeclOp);

    void visit(ReadOp readOp);

    void visit(WriteOp writeOp);

    void visit(IfThenElseOp ifThenElseOp);

    void visit(PVarOp pVarOp);

    void visit(VarOptInitOp varOptInitOp);

    void visit(BeginEndOp beginEndOp);

    void visit(ConstOp constOp);

    void visit(TypeOp typeOp);
}
