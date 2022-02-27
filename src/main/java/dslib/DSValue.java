package dslib;

public final class DSValue implements DSElement {
    private final String value;

    public DSValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public DSType getType() {
        return DSType.DS_VALUE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof DSValue) {
            return ((DSValue)o).value.equals(this.value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public DSValue copy() {
        return new DSValue(value);
    }
}
