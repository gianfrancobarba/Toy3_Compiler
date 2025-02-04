package main.test;

import main.nodes.declarations.*;
import main.nodes.common.Identifier;
import main.nodes.expr.BinaryExprOp;
import main.nodes.expr.FunCallOp;
import main.nodes.program.BeginEndOp;
import main.nodes.program.ProgramOp;
import main.nodes.statements.*;
import main.nodes.types.ConstOp;
import main.nodes.types.TypeOp;

import javax.swing.*;
import java.util.List;

public class TestGUIVisitor {

    public static void main(String[] args) {

        // Creazione della GUI con JTree
        JFrame frame = new JFrame("AST Test");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Conversione dell'AST in un albero per JTree
        JTree tree = new JTree(testProgram());

        // Creazione del pannello con scroll
        JScrollPane scrollPane = new JScrollPane(tree);
        frame.add(scrollPane);

        // Mostra la finestra
        frame.setVisible(true);
    }


    static ProgramOp testProgram() {
        Identifier id1 = new Identifier("id1");
        Identifier id2 = new Identifier("id2");
        Identifier id3 = new Identifier("id3");
        Identifier id4 = new Identifier("id4");

        Identifier funId = new Identifier("funId");
        Identifier parId1 = new Identifier("parId1");
        Identifier parId2 = new Identifier("parId2");
        Identifier parId3 = new Identifier("parId3");

        PVarOp pVarOp1 = new PVarOp(parId1, true);
        PVarOp pVarOp2 = new PVarOp(parId2, false);
        PVarOp pVarOp3 = new PVarOp(parId3, true);

        List<PVarOp> pVarOps = List.of(pVarOp1, pVarOp2, pVarOp3);

        TypeOp typeOp = new TypeOp("int");

        ParDeclOp parDeclOp = new ParDeclOp(pVarOps, typeOp);

        List<ParDeclOp> parDeclOps = List.of(parDeclOp);

        TypeOp typeOp2 = new TypeOp("void");

        FunDeclOp funDeclOp = new FunDeclOp(funId, parDeclOps, typeOp2, null);

        List<Identifier> ids = List.of(id1, id2);

        TypeOp type = new TypeOp("int");

        ConstOp constOp = new ConstOp(5);
        ConstOp constOp2 = new ConstOp(10);
        VarOptInitOp varOptInitOp = new VarOptInitOp(id4, constOp);
        VarOptInitOp varOptInitOp2 = new VarOptInitOp(id2, constOp2);
        VarOptInitOp varOptInitOp3 = new VarOptInitOp(id3, null);

        List<VarOptInitOp> listVarOpt = List.of(varOptInitOp, varOptInitOp2, varOptInitOp3);

        VarDeclOp varDeclOp = new VarDeclOp(listVarOpt, type);

        FunCallOpStat funCallOp = new FunCallOpStat(new FunCallOp( funId, List.of(new ConstOp(5), new ConstOp(10))));

        ReadOp readOp = new ReadOp(ids);
        WriteOp writeOp = new WriteOp(List.of(new ConstOp("ciao mamma")), null);

        List<StatementOp> list = List.of(readOp, writeOp, funCallOp);

        BeginEndOp beginEndOp = new BeginEndOp(List.of(varDeclOp), list);

        return new ProgramOp(List.of(funDeclOp), beginEndOp);
    }

    static IfThenElseOp testIf() {
        BinaryExprOp binaryExprOp = new BinaryExprOp(new Identifier("x"), "AND", new Identifier("y"));
        //crea una list di Statement
        VarOptInitOp varOptInitOp = new VarOptInitOp(new Identifier("x"), new ConstOp(5));
        VarDeclOp varDeclOp = new VarDeclOp(List.of(varOptInitOp), new TypeOp("int"));
        List<VarDeclOp> listVarDecl = List.of(varDeclOp);
        List<StatementOp> list = List.of(new IfThenOp(binaryExprOp, null), new IfThenElseOp(binaryExprOp, null, null));

        BodyOp bodyOp = new BodyOp(listVarDecl, list);

        return new IfThenElseOp( binaryExprOp, bodyOp, bodyOp);
    }

    static AssignOp testAssign() {

        Identifier id = new Identifier("fun");

        Identifier parId1 = new Identifier("parId1");
        Identifier parId2 = new Identifier("parId2");

        PVarOp pVarOp1 = new PVarOp(parId1, true);
        PVarOp pVarOp2 = new PVarOp(parId2, false);

        List<PVarOp> pVarOps = List.of(pVarOp1, pVarOp2);

        TypeOp typeOp = new TypeOp("int");

        ParDeclOp parDeclOp = new ParDeclOp(pVarOps, typeOp);

        List<ParDeclOp> parDeclOps = List.of(parDeclOp);

        TypeOp typeOp2 = new TypeOp("void");

        FunDeclOp funDeclOp = new FunDeclOp(id, parDeclOps, typeOp, null);

        List<Identifier> ids = List.of(id);

        Identifier x = new Identifier("x");

        FunCallOp funCallOp = new FunCallOp(id, List.of(new ConstOp(5), new ConstOp(10)));

        AssignOp assignOp = new AssignOp(List.of(x), List.of(funCallOp));

        return assignOp;
    }
}