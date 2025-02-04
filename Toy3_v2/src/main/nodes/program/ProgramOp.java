
package main.nodes.program;

import main.visitor.Visitor;
import main.visitor.Node;
import java.util.ArrayList;
import java.util.List;

public class ProgramOp extends Node {

    private List<Object> listDecls;
    private BeginEndOp beginEndOp;

    public ProgramOp(List<Object> listDecls, BeginEndOp beginEndOp) {
        this.listDecls = new ArrayList<>(listDecls);
        this.beginEndOp = beginEndOp;
    }

    public List<Object> getListDecls() {
        return listDecls;
    }

    public BeginEndOp getBeginEndOp() {
        return beginEndOp;
    }

    public void setBeginEndOp(BeginEndOp beginEndOp) {
        this.beginEndOp = beginEndOp;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
