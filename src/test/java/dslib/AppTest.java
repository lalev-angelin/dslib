package dslib;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    /**
     * Test DSValue equals and copy methods
     */
    @Test
    public void testDSValueCopyAndEquals() {
        DSValue i = new DSValue("5");
        DSValue j = i.copy();

        assertEquals(i, j);

        DSValue k = new DSValue("6");
        assertNotEquals(i, k);

        DSValue s = new DSValue("test");
        DSValue t = s.copy();
        assertEquals(s, t);
    }

    /**
     * Tests equals and copy methods of DSSet
     */
    @Test
    public void testDSSetEquals() {
        DSSet s = new DSSet(new DSValue("alpha"), new DSValue("5"), new DSPair("beta","6"));
        DSSet p = s.copy();
        assertEquals(s, p);
        assertEquals(p, s);

        DSSet q = new DSSet(new DSSet());
        assertEquals(q, q);

        DSSet a = new DSSet();
        DSSet b = new DSSet();
        assertEquals(a, b);
        assertEquals(b, a);


        DSSet d = new DSSet("a","b", "a");
        DSSet e = new DSSet("a", "b");
        assertEquals(d, e);
        assertEquals(e, d);

        DSSet f = new DSSet("a");
        DSSet g = new DSSet();
        assertNotEquals(f, g);

        f = new DSSet(new DSSet());
        g = new DSSet();
        assertNotEquals(f, g);

    }

    /**
     * Test pair inversion
     */
    @Test
    public void testDSPairInverse() {
        DSPair a = new DSPair("1","2");
        DSPair b = new DSPair("2","1");
        assertEquals(a, b.invert());
        assertEquals(a.invert(), b);
    }

    @Test
    public void testDSParserSplit() {
        assertEquals(3, DSParser.split("1,2,3").size());
        assertEquals(4, DSParser.split("1,,2,3").size());
        assertEquals(3, DSParser.split("{1,2},2,3").size());
        assertEquals(1, DSParser.split("(1,2,2,3)").size());
        assertEquals(3, DSParser.split("1,\"2,2\",3").size());
        assertEquals(4, DSParser.split("1,'2',2,3").size());
        assertEquals(0, DSParser.split("").size());

        List<String> parts = DSParser.split("1,2,3");
        assertEquals("1", parts.get(0));
        assertEquals("2", parts.get(1));
        assertEquals("3", parts.get(2));

        parts = DSParser.split("1,\"2,2\",3");
        assertEquals("1", parts.get(0));
        assertEquals("\"2,2\"", parts.get(1));
        assertEquals("3", parts.get(2));

        parts = DSParser.split("{1,2},2,3");
        assertEquals("{1,2}", parts.get(0));
        assertEquals("2", parts.get(1));
        assertEquals("3", parts.get(2));
    }

    @Test
    public void testDSParse() {
        DSElement a = DSParser.parse("(1,2)");
        assertTrue(a instanceof DSPair);
        assertEquals("1", ((DSPair)a).getFirst().toString());
        assertEquals("2", ((DSPair)a).getSecond().toString());

        DSSet b = (DSSet) DSParser.parse("{1,2,3}");
        assertEquals("{1,2,3}", b.toString());
        assertEquals(new DSSet(new DSValue("1"),
                        new DSValue("2"),
                        new DSValue("3")), b);

        DSSet c = (DSSet) DSParser.parse("{ {a,b} , (1,2), 1}");
        assertEquals(c.getElement(0), new DSSet(new DSValue("a"),
                new DSValue("b")));
        assertEquals(c.getElement(1), new DSPair(new DSValue("1"),
                new DSValue("2")));
        assertEquals(c.getElement(2), new DSValue("1"));
    }


    @Test
    public void testDSSetIntersect() {
        DSSet q = (DSSet) DSParser.parse("{a,b,c}");
        DSSet p = (DSSet) DSParser.parse("{b,c}");
        assertEquals(q.intersect(p), p);
        assertEquals(p.intersect(q), q);

        q = (DSSet) DSParser.parse("{{}, a, b}");
        p = (DSSet) DSParser.parse("{}");
        assertEquals(q.intersect(p), p);
        assertEquals(p.intersect(q), p);

        p = (DSSet) DSParser.parse("{{}}");
        assertEquals(q.intersect(p), p);
        assertEquals(p.intersect(q), q);

        p = (DSSet) DSParser.parse("{{ }}");
        assertEquals(q.intersect(p), p);
        assertEquals(p.intersect(q), q);
    }

    @Test
    public void testDSSUnion() {
        DSSet p = (DSSet) DSParser.parse("{a,b,c}");
        DSSet q = (DSSet) DSParser.parse("{d, e, f, a}");
        DSSet r = (DSSet) DSParser.parse("{a,b,c,d,e,f}");
        assertEquals(p.union(q), r);
        assertEquals(q.union(p), r);

        p = (DSSet) DSParser.parse("{}");
        assertEquals(p.union(p), p);

        p = (DSSet) DSParser.parse("{a,a,a}");
        q = (DSSet) DSParser.parse("{a}");
        assertEquals(p.union(q), q);
        assertEquals(q.union(p), p);

        p = (DSSet) DSParser.parse("{{1,2},2,{2,4},1}");
        q = (DSSet) DSParser.parse("{{},3,{2,4}}");
        r = (DSSet) DSParser.parse("{{1,2},2,{2,4},1,{},3}");
        DSSet s = p.union(q);
        assertEquals(p.union(q), q.union(p));
        assertEquals(p.union(q), r);

        p = (DSSet) DSParser.parse("{{},1}");
        q = (DSSet) DSParser.parse("{2,3}");
        assertEquals(p.union(q), (DSSet) DSParser.parse("{{},1,2,3}"));

    }

    @Test
    public void testDSSSubtract() {
        DSSet p = (DSSet) DSParser.parse("{a,b,c}");
        DSSet q = (DSSet) DSParser.parse("{a,b}");
        assertEquals(p.subtract(q), DSParser.parse("{c}"));
        assertEquals(q.subtract(p), DSParser.parse("{}"));

        p = (DSSet) DSParser.parse("{(1,2),{(1,2),a,b}}");
        q = (DSSet) DSParser.parse("{{b,a,(1,2)}, (1,2)}");
        assertEquals(p.subtract(q), new DSSet());
    }

    @Test
    public void testDSSSymmetricDifference() {
        DSSet p = (DSSet) DSParser.parse("{a,b,c}");
        DSSet q = (DSSet) DSParser.parse("{a,b}");
        assertEquals(p.symmetricDifference(q), DSParser.parse("{c}"));

        p = (DSSet) DSParser.parse("{a,{b,1},c}");
        q = (DSSet) DSParser.parse("{a,{b,2}}");
        assertEquals(p.symmetricDifference(q), DSParser.parse("{{b,1}, {b,2}, c}"));
    }

    @Test
    public void testDSSPowerSet() {
        DSSet p = (DSSet) DSParser.parse("{a,b,c}");
        DSSet q = (DSSet) DSParser.parse("{{}, {a}, {b}, {c}, {a,b}, {a,c}, {b,c}, {a,b,c}}");
        assertEquals(p.powerSet(), q);

        p = (DSSet) DSParser.parse("{}");
        q = (DSSet) DSParser.parse("{{}}");
        assertEquals(p.powerSet(), q);


    }

    @Test
    public void testDSRelationConstructor() {
        DSRelation r = new DSRelation(new DSPair("1","1"),
                new DSPair("2","1"),
                new DSPair("3","4"),
                new DSPair("5","6"));
        assertEquals(r, r);
        DSRelation s = new DSRelation("(1,1)", "(2,1)", "(3,4)", "(5,6)");
        assertEquals(r, s);

        DSRelation q = new DSRelation("(1,1)", "(2,1)", "(3,4)");
        assertNotEquals(s, q);
    }

    @Test
    public void testDSRelationAsSet() {
        DSSet d = (DSSet) DSParser.parse("{(1,2), (3,4)}");
        DSRelation e = new DSRelation("(1,2)", "(3,4)");
        DSSet f = e.asSet();
        assertEquals(d, f);
    }

    @Test
    public void testDSRelationInvert() {
        DSRelation r = new DSRelation(new DSPair("1","1"),
                new DSPair("2","1"),
                new DSPair("3","4"),
                new DSPair("5","6"));
        DSRelation s = new DSRelation(new DSPair("1","1"),
                new DSPair("1","2"),
                new DSPair("4","3"),
                new DSPair("6","5"));
        DSRelation p = r.inverse();
        assertEquals(p, s);
        assertEquals(r.inverse().inverse(), r);
        assertEquals(s.inverse().inverse(), s);
    }

    @Test
    public void testDSRelationCompose() {
        DSRelation a = new DSRelation("(a,1)","(b,2)", "(c,3)");
        DSRelation b = new DSRelation("(1,a)", "(2,b)", "(3,c)");
        DSRelation e = new DSRelation("(1,1)", "(2,2)", "(3,3)");
        DSRelation f = new DSRelation("(a,a)", "(b,b)", "(c,c)");
        DSRelation g = a.compose(b);
        assertEquals(g,e);
        DSRelation h = b.compose(a);
        assertEquals(h, f);

//        DSRelation ainv = a.inverse();
//        DSRelation aident = DSRelation.identityRelationOf(a);
//        assertEquals(ainv.compose(a), aident);
//        assertEquals(a.compose(ainv), aident);
    }

    {
        assertTrue( true );
    }
}
