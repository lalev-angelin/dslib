package dslib;

public final class DSPair implements DSElement {
    private final DSElement first;
    private final DSElement second;

    public DSPair(DSElement first, DSElement second) {
        this.first = first.copy();
        this.second = second.copy();
    }

    public DSPair(String first, String second) {
        this.first = new DSValue(first);
        this.second = new DSValue(second);
    }

    public DSElement getFirst() {
        return first.copy();
    }

    public DSElement getSecond() {
        return second.copy();
    }

    @Override
    public DSType getType() {
        return DSType.DS_PAIR;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "," + second.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof DSPair) {
            return first.equals(((DSPair) other).first) &&
                    second.equals(((DSPair) other).second);
        } else {
            return false;
        }
    }

    @Override
    public DSPair copy() {
        return new DSPair(first, second);
    }

    public DSPair invert() {
        return new DSPair(second, first);
    }
}
