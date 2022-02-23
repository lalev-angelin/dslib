package dslib;

import java.lang.reflect.Method;

public interface DSElement {
    String asString();
    boolean equalsTo(Object other);
    Object makeCopy();

    default void testTypeParameter (Object value) {
        try {
            Method method = value.getClass().getMethod("toString", null);
            if (method.getDeclaringClass().toGenericString().equals("public class java.lang.Object")) {
                throw new IllegalArgumentException("DSMember can be instantiated only type parameters, which are classes that override toString method");
            };

            method = value.getClass().getMethod("equals", Object.class);
            if (method.getDeclaringClass().toGenericString().equals("public class java.lang.Object")) {
                throw new IllegalArgumentException("DSMember can be instantiated only type parameters, which are classes that override equals method");
            };
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
