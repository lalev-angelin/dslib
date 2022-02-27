package dslib;

public interface DSElement {
    String toString();
    boolean equals(Object o);
    DSElement copy();
    DSType getType();
}
