package dslib;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

public final class DSPair<T extends Comparable<T>,U extends Comparable<U>> implements DSElement, DSComparable {
    private T first;
    private U second;

    public DSPair(T first, U second) {
        testTypeParameter(first);
        testTypeParameter(second);

        this.first = first;
        this.second = second;
    }

    @Override
    public String asString() {
        return "("+first.toString()+","+second.toString()+")";
    }

    @Override
    public boolean equalsTo(Object other) {
        if (other==null) {
            return false;
        }

        if (other instanceof DSPair<?,?>) {
            return first.equals(((DSPair<T,U>)other).first) &&
                    second.equals(((DSPair<T,U>)other).second);
        } else {
            return false;
        }
    }

    @Override
    public Object makeCopy() {
        return new DSPair<>(first, second);
    }

    @Override
    public int compareTo(Object other) {
        if (other==null) return 1;

        if (other instanceof DSPair<?,?>) {
            DSPair<T,U> tother = (DSPair<T,U>)other;
            if (this.first.compareTo(tother.first) < 0) {
                return -1;
            }

            if (this.first.compareTo(tother.first) > 0) {
                return 1;
            }

            if (this.first.compareTo(tother.first)==0) {
                return this.second.compareTo(tother.second);
            }
        } else {
            throw new IllegalArgumentException("Cannot compare "+other.getClass().toGenericString() + this.getClass().toGenericString());
        }
        // Should not reach here!
        return 0;
    }

    public DSPair<U, T> invert() {
        return new DSPair<>(second, first);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public boolean equals(Object o) {
        return equalsTo(o);
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
