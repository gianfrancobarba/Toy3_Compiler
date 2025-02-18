package main.visitor.tables;
import java.util.Objects;

public class Pair {
    private final String type1;
    private final String type2;

    public Pair(String type1, String type2) {
        this.type1 = type1;
        this.type2 = type2;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(type1, pair.type1) && Objects.equals(type2, pair.type2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type1, type2);
    }

    @Override
    public String toString() {
        return "(" + type1 + ", " + type2 + ")";
    }
}