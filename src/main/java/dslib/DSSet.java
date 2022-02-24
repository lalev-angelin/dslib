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