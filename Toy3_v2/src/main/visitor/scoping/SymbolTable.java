package main.visitor.scoping;

public class SymbolTable {
    private Scope currentScope;

    public void enterScope() {
        currentScope = new Scope(currentScope);
    }

    public void addId(Kind kind, String id, String type) {
        this.currentScope.getScope().get(kind).put(id, type);
    }

    public String lookup(Kind kind, String id) {
        for (Scope scope = currentScope; scope != null; scope = scope.getParent()) {
            if (scope.getScope().get(kind).containsKey(id)) {
                return scope.getScope().get(kind).get(id);
            }
        }
        return null;
    }

    public boolean probe(Kind kind, String id) {
        return currentScope.getScope().get(kind).containsKey(id);
    }

    public void printTable() {
        for (Scope scope = currentScope; scope != null; scope = scope.getParent()) {
            System.out.println(scope.getScope());
        }
    }

    public void exitScope() {
        currentScope = currentScope.getParent();
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(Scope scope) {
        this.currentScope = scope;
    }

}
