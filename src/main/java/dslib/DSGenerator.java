package dslib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DSGenerator {
    static Random r = new Random(System.currentTimeMillis());

    public static DSSet generateSet(int minElements, int maxElements, DSElement[] elements) {
        List<DSElement> e = new ArrayList<>();

        if (minElements>maxElements) return new DSSet();
        int j = r.nextInt(maxElements);

        for (int i=0; i<minElements+j; i++) {
            e.add(elements[r.nextInt(elements.length)].copy());
        }
        return new DSSet(e);
    }
}
