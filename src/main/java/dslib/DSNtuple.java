package dslib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DSNtuple implements DSElement {
    private final List<DSElement> elements;

    public DSNtuple(DSElement element1, DSElement element2, DSElement... elements) {
        this.elements = new ArrayList<>();

        this.elements.add(element1.copy());
        this.elements.add(element2.copy());

        Arrays.stream(elements)
                .forEach(e->this.elements.add(e.copy()));
    }

    public DSNtuple(String element1, String element2) {
        this(new DSValue(element1), new DSValue(element2));
    }


    public DSNtuple(String element1, String element2, String ...elements) {
        this(new DSValue(element1), new DSValue(element2),
                Arrays.stream(elements)
                        .map(DSValue::new)
                        .toList().toArray(new DSValue[1]));
    }

    @Override
    public String toString() {
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

        if (other instanceof DSNtuple) {

            if (((DSNtuple)other).elements.size() != elements.size()) {
                return false;
            }

            for (DSElement e : elements) {
                for (DSElement o : elements) {
                    if (!o.equals(e)) return false;
                }
            }

            return true;

        } else {
            return false;
        }
    }

    @Override
    public DSElement copy(){
        List<DSElement> elements = new ArrayList<>();
        this.elements.forEach(e->elements.add(e.copy()));
        if (elements.size()==2) {
            return new DSNtuple(elements.get(0), elements.get(1));
        } else {
            return new DSNtuple(elements.get(0), elements.get(1), elements.stream().skip(2).toList().toArray(new DSElement[1]));
        }
    }

    @Override
    public DSType getType() {
        return DSType.DS_NTUPLE;
    }
}
