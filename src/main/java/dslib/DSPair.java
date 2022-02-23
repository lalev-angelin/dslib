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

    public static boolean isPairRepresentation(String s) {
        s = s.strip();
        if (!(s.startsWith("(") && (s.endsWith(")")))) {
            return false;
        }

        Deque<Character> bracket = new LinkedList<>();
        int commaCount = 0;
        for (int i=1; i<s.length()-1; i++) {
            if ( (s.charAt(i)=='(') || (s.charAt(i)=='{') || (s.charAt(i)=='[') || (s.charAt(i)=='"')) {
                bracket.push(s.charAt(i));
            }

            if (s.charAt(i)==')') {
                if (bracket.isEmpty() || bracket.pop()!='(') return false;
            }

            if (s.charAt(i)=='}') {
                if (bracket.isEmpty() || bracket.pop()!='{') return false;
            }

            if (s.charAt(i)==']') {
                if (bracket.isEmpty() || bracket.pop()!='[') return false;
            }

            if (s.charAt(i)=='"') {
                if (bracket.isEmpty() || bracket.pop()!='"') return false;
            }

            if (s.charAt(i)==',' && bracket.isEmpty()) commaCount++;

        }

        return commaCount == 1 && bracket.isEmpty();
    }

    public static <T extends Comparable<T>, U extends Comparable<U>> DSPair<T,U>  parse(String s,
                                                                                        Function<String, T> convertT,
                                                                                        Function<String, U> convertU) {
        T first = null;
        U second = null;

        s = s.strip();
        if (!(s.startsWith("(") && (s.endsWith(")")))) {
            throw new IllegalArgumentException("Invalid string representation of a pair: "+s);
        }

        Deque<Character> bracket = new LinkedList<>();
        StringBuilder b = new StringBuilder();
        int commaCount = 0;

        for (int i=1; i<s.length()-1; i++) {
            if ((s.charAt(i) == '(') || (s.charAt(i) == '{') || (s.charAt(i) == '[') || (s.charAt(i) == '"')) {
                bracket.push(s.charAt(i));
            }

            if (s.charAt(i) == ')') {
                if (bracket.isEmpty() || bracket.pop() != '(') {
                    throw new IllegalArgumentException("Ivalid string representation of a pair: " + s);
                }
                continue;
            }

            if (s.charAt(i) == '}') {
                if (bracket.isEmpty() || bracket.pop() != '{') {
                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
                }
                continue;
            }

            if (s.charAt(i) == ']') {
                if (bracket.isEmpty() || bracket.pop() != '[') {
                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
                }
                continue;
            }

            if (s.charAt(i) == '"') {
                if (bracket.isEmpty() || bracket.pop() != '"') {
                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
                }
                continue;
            }

            if (s.charAt(i) == ',' && bracket.isEmpty()) {
                if (commaCount == 0) {
                    first = convertT.apply(b.toString().strip());
                    b = new StringBuilder();
                }
                if (commaCount == 1) {
                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
                }
                commaCount++;
                continue;
            }

            b.append(s.charAt(i));
        }

        if (commaCount==1) {
            second = convertU.apply(b.toString().strip());
        } else {
            throw new IllegalArgumentException("Invalid string representation of a pair");
        }

        return new DSPair<>(first, second);

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
