package main.nodes.statements;

import main.nodes.common.Identifier;
import main.visitor.Visitor;
import main.visitor.Node;

import java.util.ArrayList;
import java.util.List;

public class ReadOp extends Node implements StatementOp {
    private List<Identifier> listId; // Variabili di input
    private String funLabel;

    public ReadOp(List<Identifier> listId) {
        this.listId = new ArrayList<>(listId);
    }

    public List<Identifier> getListId() {
        return listId;
    }

    public void setListId(List<Identifier> listId) {
        this.listId = listId;
    }
    public void addId(Identifier id) {
        if (id != null) {
            listId.add(id);
        }
    }

    public void addAllIds(List<Identifier> listId) {
        if (listId != null) {
            for (Identifier id : listId) {
                addId(id);
            }
        }
    }
    @Override
    public void setFunLabel(String funLabel) {
        this.funLabel = funLabel;
    }

    @Override
    public String getFunLabel() {
        return funLabel;
    }

    public List<Identifier> getIdentifiers() {
        return listId;
    }

    public String toString() {
        return "ReadOp{ " +
                ", listId=" + listId +
                " }";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
