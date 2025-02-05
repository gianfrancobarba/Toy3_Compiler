package main.nodes.declarations;

import main.nodes.common.Identifier;
import main.nodes.statements.BodyOp;
import main.nodes.types.TypeOp;
import main.visitor.Visitor;
import main.visitor.Node;
import java.util.ArrayList;
import java.util.List;

public class FunDeclOp extends Node {
    private Identifier id;
    private List<ParDeclOp> params;
    private TypeOp optType; // Tipo opzionale
    private BodyOp body;

    public FunDeclOp(Identifier id, List<ParDeclOp> params, TypeOp optType, BodyOp body) {
        this.optType = optType;
        this.id = id;

        if(params != null) {
            this.params = new ArrayList<>(params);
        }

        this.body = body;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
