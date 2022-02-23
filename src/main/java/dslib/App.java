package dslib;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DSSet s = new DSSet(new DSValue<>("alpha"), new DSValue<>(5), new DSPair<>("beta",6));
        DSSet p = (DSSet) s.makeCopy();
        DSSet q = new DSSet(new DSSet());
        System.out.println(s.equalsTo(p));
        System.out.println(q.equalsTo(q));
        System.out.println(q.equalsTo(s));
    }
}
