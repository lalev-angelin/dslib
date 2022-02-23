package dslib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DSNtuple<T> implements DSElement {
    private final List<T> elements;

    @SafeVarargs
    public DSNtuple(T... elements) {
        this.elements = new ArrayList<T>();

        Arrays.stream(elements)
                .forEach(e->this.elements.add(e));
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public boolean equalsTo(Object other) {
        return false;
    }

    @Override
    public Object makeCopy() {
        return null;
    }
}
