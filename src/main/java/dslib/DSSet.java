package dslib;

import java.util.*;

public class DSSet implements DSElement {
    List<DSElement> elements;

    public DSSet() {
        elements = new ArrayList<>();
    }

    public DSSet(DSElement... elements) {
        this();

        Arrays.stream(elements)
                .forEach(e -> {
                    if (!this.elements.contains(e)) {
                        this.elements.add((DSElement) e.makeCopy());
                    }
                });
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String asString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder result = new StringBuilder("{");
        elements.forEach(e -> {
            result.append(e.asString());
            result.append(",");
        });
        result.deleteCharAt(result.length() - 1);
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equalsTo(Object other) {
        if (other == null) return false;

        if (other instanceof DSSet) {
            DSSet test = (DSSet) other;
            for (DSElement e : elements) {
                boolean found = false;
                for (DSElement f : test.elements) {
                    if (e.equalsTo(f)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object makeCopy() {
        if (isEmpty()) {
            return new DSSet();
        } else {
            return new DSSet(elements.toArray(new DSElement[1]));
        }
    }

    @Override
    public String toString() {
        return asString();
    }

    public static boolean isSetRepresentation(String s) {
        s = s.strip();
        if (s.startsWith("{") && (s.endsWith("}"))) {
            return true;
        } else {
            return false;
        }
    }

    public static DSSet parse(String s) {
        List<String> elements = new ArrayList<>();

        s = s.strip();
        if (!isSetRepresentation(s)) {
            throw new IllegalArgumentException("Invalid string representation of a set: " + s);
        }

        s = new StringBuilder(s).deleteCharAt(s.length() - 1).deleteCharAt(0).toString().strip();

        if (s.isBlank()) {
            return new DSSet();
        }

        Deque<Character> bracket = new LinkedList<>();
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) == '(') || (s.charAt(i) == '{') || (s.charAt(i) == '[') || (s.charAt(i) == '"')) {
                bracket.push(s.charAt(i));
            }

            if (s.charAt(i) == ')') {
                if (bracket.isEmpty() || bracket.pop() != '(') {
                    throw new IllegalArgumentException("Ivalid string representation of a set: " + s);
                }
            }

            if (s.charAt(i) == '}') {
                if (bracket.isEmpty() || bracket.pop() != '{') {
                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
                }
            }

            if (s.charAt(i) == ']') {
                if (bracket.isEmpty() || bracket.pop() != '[') {
                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
                }
            }

            if (s.charAt(i) == '"') {
                if (bracket.isEmpty() || bracket.pop() != '"') {
                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
                }
            }

            if (s.charAt(i) == ',' && bracket.isEmpty()) {
                elements.add(b.toString().strip());
                b = new StringBuilder();
                continue;
            }

            b.append(s.charAt(i));
        }

        elements.add(b.toString().strip());

        //Mapping logic

        List<DSElement> transformed = elements.stream()
                .map(a -> {
                    if (DSSet.isSetRepresentation(a)) {
                        return DSSet.parse(a);
                    }
                    if (DSPair.isPairRepresentation(a)) {
                        return DSPair.<String, String>parse(a, (q) -> q, (p) -> p);
                    }
                    return new DSValue<String>(a);
                }).toList();


        return new DSSet(transformed.toArray(new DSElement[1]));
    }

    public DSSet intersect(DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        for (DSElement e : this.elements) {
            for (DSElement o : other.elements) {
                if (e.equalsTo(o)) {
                    elements.add((DSElement) e.makeCopy());
                }
                ;
            }
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

    public DSSet join(DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        elements.addAll(this.elements);

        for (DSElement o : other.elements) {
            for (DSElement e : elements) {
                if (e.equalsTo(o)) {
                    continue;
                }
            }
            elements.add((DSElement) o.makeCopy());
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

    public DSSet subtract (DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        elements.addAll(this.elements);

        for (DSElement o : other.elements) {
            elements.removeIf(e -> e.equalsTo(o));
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

}