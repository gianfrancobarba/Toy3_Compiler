package main.visitor.scoping;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Scope {
    private Map<Kind, Map<String, String>> scope;
    private Scope parent;

    public Scope(Scope parent) {
        this.scope = new EnumMap<>(Kind.class);
        this.parent = parent;

        for (Kind kind : Kind.values()) {
            this.scope.put(kind, new HashMap<>());
        }
    }

    public Map<Kind, Map<String, String>> getScope() {
        return scope;
    }

    public void setScope(Map<Kind, Map<String, String>> scope) {
        this.scope = scope;
    }

    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Scope{" +
                "scope=" + scope +
                ", parent=" + parent +
                '}';
    }
}
