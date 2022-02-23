package dslib;

public final class DSValue<T> implements DSElement {
    private final T value;

    public DSValue(T value) {
        testTypeParameter(value);
        this.value = value;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public boolean equalsTo(Object other) {
        if (other==null) return false;
        if (other instanceof DSValue<?>) {
            return value.equals(((DSValue<T>) other).value);
        } else {
            return false;
        }
    }

    @Override
    public DSValue<T> makeCopy() {
        return new DSValue<>(value);
    }
}
