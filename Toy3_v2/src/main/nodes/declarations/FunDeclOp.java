package main.nodes.declarations;

import main.nodes.common.Identifier;
import main.nodes.statements.BodyOp;
import main.nodes.types.TypeOp;
import main.visitor.ASTVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunDeclOp extends DefaultMutableTreeNode {
    private Identifier id;
    private List<ParDeclOp> params;
    private TypeOp optType; // Tipo opzionale
    private BodyOp body;

    public FunDeclOp(Identifier id, List<ParDeclOp> params, TypeOp optType, BodyOp body) {

        super("DefDeclOp");

        this.optType = optType;
        super.add(Objects.requireNonNullElseGet(optType, () -> new TypeOp("void")));

        this.id = id;
        super.add(id);

        if(params != null) {
            this.params = new ArrayList<>(params);
            for (ParDeclOp parDecl : params) {
                super.add(parDecl);
            }
        }

        this.body = body;
        if (body != null) {
            super.add(body);
        }
    }

    public FunDeclOp(Identifier id, List<ParDeclOp> params, BodyOp body) {
        this(id, params, null, body);
    }

    public Identifier getId() {
        return id;
    }

    public List<ParDeclOp> getParams() {
        return params;
    }

    public TypeOp getOptType() {
        return optType;
    }

    public BodyOp getBody() {
        return body;
    }
    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
