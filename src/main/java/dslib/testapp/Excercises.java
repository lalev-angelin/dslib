package dslib.testapp;

import dslib.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class Excercises {
    public static void setPractice1 (Consumer<String> printer, Consumer<String> messenger) {
        DSSet set1;
        DSSet set2;

        Random r = new Random(System.currentTimeMillis());
        int j = r.nextInt(3);
        switch (j) {

            case 0:
                set1 = DSGenerator.generateSet(3, 5,
                        new DSElement[]{
                                new DSValue("a"),
                                new DSValue("b"),
                                new DSValue("c"),
                                new DSValue("d"),
                                new DSValue("e"),
                                new DSValue("f")
                        });

                set2 = DSGenerator.generateSet(3, 5,
                        new DSElement[]{
                                new DSValue("a"),
                                new DSValue("b"),
                                new DSValue("c"),
                                new DSValue("d"),
                                new DSValue("e"),
                                new DSValue("f")
                        });
                break;

            case 1:
                set1 = DSGenerator.generateSet(4, 6,
                        new DSElement[]{
                                new DSValue("1"),
                                new DSValue("2"),
                                new DSValue("3"),
                                new DSValue("4"),
                                new DSValue("5"),
                                new DSValue("6")
                        });

                set2 = DSGenerator.generateSet(2, 5,
                        new DSElement[]{
                                new DSValue("1"),
                                new DSValue("2"),
                                new DSValue("3"),
                                new DSValue("4"),
                                new DSValue("5"),
                                new DSValue("6")
                        });
                break;

            default:
                set1 = DSGenerator.generateSet(4, 6,
                        new DSElement[]{
                                new DSValue("1"),
                                new DSValue("2"),
                                new DSValue("3"),
                                new DSValue("4"),
                                DSParser.parse("{1,2}"),
                                DSParser.parse("{2,4}"),
                                DSParser.parse("{}")
                        });

                set2 = DSGenerator.generateSet(2, 5,
                        new DSElement[] {
                                new DSValue("1"),
                                new DSValue("2"),
                                new DSValue("3"),
                                new DSValue("4"),
                                DSParser.parse("{1,2}"),
                                DSParser.parse("{2,4}"),
                                DSParser.parse("{}")
                        });
                break;
        }

        printer.accept("Намерете обединението, сечението, разликата и симетричната разлика на множествата:");
        printer.accept(set1.toString());
        printer.accept(set2.toString());
        messenger.accept("Получи решението сега!");

        printer.accept(set1 + " \u222a " + set2 + " = " +
                set1.union(set2));

        printer.accept(set1 + " \u2229 " + set2 + " = " +
                set1.intersect(set2));

        printer.accept(set1 + " - " + set2 + " = " +
                set1.subtract(set2));

        printer.accept(set1 + " \u2295 "+ set2 +" = "+
                set1.symmetricDifference(set2));

        printer.accept("2^" + set1 + " = " +
                set1.powerSet());

        printer.accept("2^" + set2 + " = " +
                set2.powerSet());
    }


    public static void setPractice2 (Consumer<String> printer, Consumer<String> messenger) {
        DSSet set1;
        DSSet set2;

        set1 = DSGenerator.generateSet(4, 4,
                new DSElement[]{
                        new DSValue("a"),
                        new DSValue("b"),
                        new DSValue("c"),
                        new DSValue("d"),
                        new DSValue("e"),
                        new DSValue("f")
                });

        set2 = DSGenerator.generateSet(3, 3,
                new DSElement[]{
                        new DSValue("a"),
                        new DSValue("b"),
                        new DSValue("c"),
                        new DSValue("d"),
                        new DSValue("e"),
                        new DSValue("f")
                });

        printer.accept("Намерете степенното множество на множествата: "
                + set1 + set2);
        messenger.accept("Получи решението сега!");

        printer.accept("2^" + set1 + " = " +
                set1.powerSet());

        printer.accept("2^" + set2 + " = " +
                set2.powerSet());
    }

    public static void setPractice3 (Consumer<String> printer, Consumer<String> messenger) {
        Random r = new Random(System.currentTimeMillis());

        DSSet set1;
        DSSet set2;

        int j = r.nextInt(2);
        if (j == 0) {
            set1 = DSGenerator.generateSet(6, 9,
                    new DSElement[]{
                            new DSValue("a"),
                            new DSValue("b"),
                            new DSValue("c"),
                            new DSValue("d"),
                            new DSValue("e"),
                            new DSValue("f"),
                            new DSValue("g"),
                            new DSValue("h"),
                            new DSValue("i"),
                            new DSValue("j")
                    });

            List<DSElement> elements = new ArrayList<>();
            for (int i = 0; i < set1.getElementCount(); i++) {
                if (r.nextInt(4) < 2) {
                    elements.add(set1.getElement(i));
                }
            }

            set2 = new DSSet(elements);

        } else {
            set1 = DSGenerator.generateSet(6, 9,
                    new DSElement[]{
                            new DSValue("1"),
                            new DSValue("2"),
                            new DSValue("3"),
                            new DSValue("4"),
                            DSParser.parse("{1,2}"),
                            DSParser.parse("{2,4}"),
                            DSParser.parse("{1,3}"),
                            DSParser.parse("{1,2,3}"),
                            DSParser.parse("{1,4}"),
                            DSParser.parse("{}")
                    });

            List<DSElement> elements = new ArrayList<>();
            for (int i = 0; i < set1.getElementCount(); i++) {
                if (r.nextInt(4) < 2) {
                    elements.add(set1.getElement(i));
                }
            }

            set2 = new DSSet(elements);
        }

        printer.accept("Намерете допълнението на множеството " + set2 + " в " + set1);
        messenger.accept("Получи решението сега!");

        System.out.println(set2.complement(set1));
    }

}
