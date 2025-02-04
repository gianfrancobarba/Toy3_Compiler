package main.nodes.statements;

import main.nodes.common.Identifier;
import main.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class ReadOp extends StatementOp {
    private List<Identifier> listId; // Variabili di input

    public ReadOp(List<Identifier> listId) {
        super("ReadOp");
        this.listId = new ArrayList<>(listId);

        // Aggiungiamo i nodi figli per la visualizzazione grafica
        for (Identifier id : this.listId) {
            super.add(id);
        }
    }

    public List<Identifier> getIdenfiers() {
        return listId;
    }

    public void setIdenfiers(List<Identifier> listId) {
        this.listId = listId;
    }
    public void addId(Identifier id) {
        if (id != null) {
            listId.add(id);
            super.add(id);
        }
    }

    public void addAllIds(List<Identifier> listId) {
        if (listId != null) {
            for (Identifier id : listId) {
                addId(id);
            }
        }
    }

    public String toString() {
        return super.toString();
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
