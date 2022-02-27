package dslib;

import java.lang.annotation.ElementType;
import java.util.*;

public class DSSet implements DSElement {
    private final List<DSElement> elements;

    public DSSet() {
        elements = new ArrayList<>();
    }

    public DSSet(DSElement... elements) {
        this(List.of(elements));
    }

    public DSSet(List<DSElement> elements) {
        this();

        elements.forEach(e -> {
            if (!this.elements.contains(e)) {
                this.elements.add((e.copy()));
            }
        });
    }

    public DSSet(String ...elements) {
        this();

        Arrays.stream(elements)
                .forEach(e -> {
                    DSValue v = new DSValue(e);
                    if (!this.elements.contains(v)) {
                        this.elements.add((v.copy()));
                    }
                });
    }

    @Override
    public DSType getType() {
        return DSType.DS_SET;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder result = new StringBuilder("{");
        elements.forEach(e -> {
            result.append(e.toString());
            result.append(",");
        });
        result.deleteCharAt(result.length() - 1);
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;

        if (other instanceof DSSet) {
            DSSet test = (DSSet) other;
            for (DSElement e : elements) {
                boolean found = false;
                for (DSElement f : test.elements) {
                    if (e.equals(f)) {
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
    public DSSet copy() {
        if (isEmpty()) {
            return new DSSet();
        } else {
            List<DSElement> elements = new ArrayList<>();
            this.elements.forEach(e->{
                   elements.add(e.copy());
            });
            return new DSSet(elements.toArray(new DSElement[1]));
        }
    }

    public DSSet intersect(DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        for (DSElement e : this.elements) {
            for (DSElement o : other.elements) {
                if (e.equals(o)) {
                    elements.add((DSElement) e.copy());
                }
                ;
            }
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

    public DSSet union(DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        elements.addAll(this.elements);

        for (DSElement o : other.elements) {
            for (DSElement e : elements) {
                if (e.equals(o)) {
                    continue;
                }
            }
            elements.add((DSElement) o.copy());
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

    public DSSet subtract (DSSet other) {
        List<DSElement> elements = new ArrayList<>();

        this.elements.forEach(e->{
            elements.add(e.copy());
        });

        for (DSElement o : other.elements) {
            elements.removeIf(e -> e.equals(o));
        }

        if (elements.isEmpty()) return new DSSet();

        return new DSSet(elements.toArray(new DSElement[1]));
    }

    public DSSet complement(DSSet whole) {
        return whole.subtract(this);
    }

    public DSSet symmetricDifference(DSSet other) {
        List<DSElement> elements = new ArrayList<>();
        for (DSElement e : this.elements) {
            boolean found = false;
            for (DSElement o : other.elements) {
                if (o.equals(e)) {
                    found=true;
                    break;
                }
            }
            if (!found) {
                elements.add(e.copy());
            }
        }

        for (DSElement o: other.elements) {
            boolean found = false;
            for (DSElement e : this.elements) {
                if (o.equals(e)) {
                    found=true;
                    break;
                }
            }
            if (!found) {
                elements.add(o.copy());
            }
        }

        if (elements.isEmpty()) {
            return new DSSet();
        } else {
            return new DSSet(elements);
        }
    }

    public DSElement getElement(int position) {
        return elements.get(position).copy();
    }

    public int getElementCount() {
        return elements.size();
    }

    public DSSet powerSet() {
        List<DSElement> e = new ArrayList<>();

        long p = (long) Math.pow(2, this.elements.size());
        for (int i=0; i<p; i++) {
            List<DSElement> f = new ArrayList<>();
            for (int k=0; k<this.elements.size(); k++) {
                if ((1<<k & i) == (1<<k)) {
                    f.add(this.elements.get(k));
                }
            }

            if (f.isEmpty()) {
                e.add(new DSSet());
            } else {
                e.add(new DSSet(f));
            }
        }

        if (e.isEmpty()) {
            return new DSSet();
        } else {
            return new DSSet(e);
        }

    };
}