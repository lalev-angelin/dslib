package dslib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DSGenerator {
    static Random r = new Random(System.currentTimeMillis());

    public static void generate(int numElements, DSElement[] elements) {
        List<DSElement> e = new ArrayList<>();

        for (int i=0; i<numElements; i++) {
            e.add(elements[r.nextInt(elements.length)]);
        }
    }
}
