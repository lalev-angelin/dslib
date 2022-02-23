package dslib;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    /**
     * We test if our DSMember class will accept classes that do not override equals
     * and toString methods. If it does, this is an error, because we rely on these
     * classes to compare values not references, and in addition we need toString to
     * implement further comparisons.
     */
    @Test
    public void TestDSMemberTypeInstantiation() {
        class Test { }

        assertThrows(IllegalArgumentException.class, ()->{
            DSValue<Test> d = new DSValue<Test>(new Test());
        });

        class SecondTest {
            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        }
        assertThrows(IllegalArgumentException.class, ()->{
            DSValue<SecondTest> d = new DSValue<SecondTest>(new SecondTest());
        });

        class ThirdTest {
            @Override
            public String toString() {
                return super.toString();
            }
        }
        assertThrows(IllegalArgumentException.class, ()->{
            DSValue<ThirdTest> d = new DSValue<ThirdTest>(new ThirdTest());
        });


        class FourthTest {
            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        }
        assertDoesNotThrow(()->{
            DSValue<FourthTest> d = new DSValue<>(new FourthTest());
        });


    }
    /**
     * Test DSElement equals and copy methods
     */
    @Test
    public void testDSMemberCopyAndEquals() {
        DSValue<Integer> i = new DSValue<>(5);
        DSValue<Integer> j = i.makeCopy();

        assertTrue(i.equalsTo(j));

        DSValue<Integer> k = new DSValue<>(6);
        assertFalse(i.equalsTo(k));

        DSValue<String> s = new DSValue<>("test");
        DSValue<String> t = s.makeCopy();
        assertTrue(s.equalsTo(t));
    }

    /**
     * Tests equal method of DSSet
     */
    @Test
    public void testDSSetEquals() {
        DSSet s = new DSSet(new DSValue<>("alpha"), new DSValue<>(5), new DSPair<>("beta",6));
        DSSet p = (DSSet) s.makeCopy();
        DSSet q = new DSSet(new DSSet());
        assertTrue(s.equalsTo(p));
        assertTrue(q.equalsTo(q));
        assertFalse(q.equalsTo(s));
        DSSet a = new DSSet();
        DSSet b = new DSSet();
        assertTrue(a.equalsTo(b));
        DSSet c = new DSSet(new DSSet());
        assertFalse (c.equalsTo(a));
        DSSet d = new DSSet(new DSValue<>('a'), new DSValue<>('b'), new DSValue<>('a'));
        DSSet e = new DSSet(new DSValue<>('b'), new DSValue<>('a'));
        assertTrue(d.equalsTo(e));

    }

    @Test
    public void testDSPairCompare() {
        var a = new DSPair<>(1,2);
        var b = new DSPair<>(1,2);
        assertEquals(0, a.compareTo(b));

        a = new DSPair<>(2,2);
        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));

        a = new DSPair<>(0, 1);
        b = new DSPair<>(0, 2);
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));

    }

    @Test
    public void testDSSetIsSetRepresentation() {
        assertFalse(DSSet.isSetRepresentation("{"));
        assertTrue(DSSet.isSetRepresentation("{}"));
        assertFalse(DSSet.isSetRepresentation("}"));
    }

    @Test
    public void testDSPairIsPairRepresentation() {
        assertTrue(DSPair.isPairRepresentation("(1,2)"));
        assertFalse(DSPair.isPairRepresentation("(1,2,3)"));
        assertTrue(DSPair.isPairRepresentation("({1,2},{})"));
        assertFalse(DSPair.isPairRepresentation("({1,2}, {1"));
        assertFalse(DSPair.isPairRepresentation("({1,2}, {1}"));
        assertFalse(DSPair.isPairRepresentation("({1,2}, {1)"));
        // Should we have tuples with empty elements ?
        assertTrue(DSPair.isPairRepresentation("(,)"));
        assertTrue(DSPair.isPairRepresentation("(1,)"));
        assertTrue(DSPair.isPairRepresentation("(,1)"));
    }

    @Test
    public void testDSPairInverse() {
        var a = new DSPair<>(1,2);
        var b = new DSPair<>(2,1);
        assertTrue(a.equalsTo(b.invert()));
    }

    @Test
    public void testDSPairParse() {
        DSPair<Integer, String> a;
        DSPair<Integer, String> b = new DSPair<>(1, "abds");
        assertDoesNotThrow(()->{
            DSPair<Integer, String> c = DSPair.parse("(1, abds)", Integer::parseInt, (s)->s);
        });

        a = DSPair.parse("(1, abds)", Integer::parseInt, (s)->s);
        assertTrue(a.equalsTo(b));
    }

    @Test
    public void testDSSetParse() {
        DSSet s = DSSet.parse("{a,b,c}");
        DSSet stest = new DSSet(new DSValue<>("a"), new DSValue<>("b"), new DSValue<>("c"));
        assertTrue(s.equalsTo(stest));

        s = DSSet.parse("{a, b, c}");
        assertTrue(s.equalsTo(stest));

        s = DSSet.parse("{ a,  b , c }");
        assertTrue(s.equalsTo(stest));

        stest = new DSSet(new DSSet(), new DSSet(new DSValue<>("a")), new DSValue<>("a"));
        s = DSSet.parse("{ {}, { a }, a   } ");
        assertTrue(s.equalsTo(stest));

        stest = new DSSet(new DSPair<>("1","2"), new DSValue<>("1"), new DSValue<>("a"));
        s = DSSet.parse("{(1,2),1,a}");
        assertTrue(s.equalsTo(stest));
    }

    @Test
    public void testDSSetIntersect() {
        var q = DSSet.parse("{a,b,c}");
        var p = DSSet.parse("{b,c}");
        assertTrue(q.intersect(p).equalsTo(p));

        q = DSSet.parse("{{}, a, b}");
        p = DSSet.parse("{}");
        assertTrue(q.intersect(p).equalsTo(p));
        assertTrue(p.intersect(q).equalsTo(p));

        p = DSSet.parse("{{}}");
        assertTrue(p.intersect(q).equalsTo(p));
        assertTrue(q.intersect(p).equalsTo(p));

        p = DSSet.parse("{{ }}");
        assertTrue(p.intersect(q).equalsTo(p));
        assertTrue(q.intersect(p).equalsTo(p));
    }

    @Test
    public void testDSSJoin() {
        var p = DSSet.parse("{a,b,c}");
        var q = DSSet.parse("{d, e, f, a}");
        var r = DSSet.parse("{a,b,c,d,e,f}");
        assertTrue(p.join(q).equalsTo(r));

        p = DSSet.parse("{}");
        assertTrue(p.join(p).equalsTo(p));

        p = DSSet.parse("{a,a,a}");
        q = DSSet.parse("{a}");
        assertTrue(p.equalsTo(q));

        q = DSSet.parse("{a,b}");
        assertTrue(p.equalsTo(q));

        p = DSSet.parse("{a, (1,2), {1,2}}");
        q = DSSet.parse("{4,5}");
        r = DSSet.parse("{{4,5}, a, (1,2), {1,2}}");

    }

    @Test
    public void testDSSSubtract() {
        DSSet p = DSSet.parse("{a,b,c}");
        DSSet q = DSSet.parse("{a,b}");
        assertTrue(p.subtract(q).equalsTo(DSSet.parse("{c}")));
        assertTrue(q.subtract(p).equalsTo(DSSet.parse("{}")));

        p = DSSet.parse("{(1,2),{(1,2),a,b}");
        q = DSSet.parse("{{b,a,(1,2)}, (1,2)}");
        assertTrue(p.subtract(q).equalsTo(new DSSet()));
    }

    {
        assertTrue( true );
    }
}
