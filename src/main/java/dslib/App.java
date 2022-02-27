package dslib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

public class App {

        public static void printMenu() {
        System.out.println();
        System.out.println("Меню");
        System.out.println("====================================");
        System.out.println("1. Задачи по действия с множества");
        System.out.println("2. Задачи за релации");
        System.out.println("9. Изход");
        System.out.print("Изберете [129]: ");
    }

    public static void setPractice1 (Scanner s) {
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

        System.out.println("Намерете обединението, сечението, разликата и симетричната разлика на множествата:");
        System.out.println(set1);
        System.out.println(set2);
        System.out.print("Натиснете Enter за получаване на решенията: ");
        s.nextLine();

        System.out.print(set1);
        System.out.print(" \u222a ");
        System.out.print(set2);
        System.out.print(" = ");
        System.out.println(set1.union(set2));

        System.out.print(set1);
        System.out.print(" \u2229 ");
        System.out.print(set2);
        System.out.print(" = ");
        System.out.println(set1.intersect(set2));

        System.out.print(set1);
        System.out.print(" - ");
        System.out.print(set2);
        System.out.print(" = ");
        System.out.println(set1.subtract(set2));

        System.out.print(set1);
        System.out.print(" \u2a01 ");
        System.out.print(set2);
        System.out.print(" = ");
        System.out.println(set1.symmetricDifference(set2));

        System.out.print("2^");
        System.out.print(set1);
        System.out.print(" = ");
        System.out.println(set1.powerSet());

        System.out.print("2^");
        System.out.print(set2);
        System.out.print(" = ");
        System.out.println(set2.powerSet());
    }

    public static void setPractice2 (Scanner s) {
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

        System.out.println("Намерете допълнението на множеството " + set2 + " в " + set1);
        System.out.print("Натиснете Enter за да видите решенията: ");
        s.nextLine();

        System.out.println(set2.complement(set1));
    }

    public static void main( String[] args ) {
        Scanner s = new Scanner(System.in);

         String choice;
        while (true) {
            try {
                printMenu();
                choice = s.nextLine();
            } catch (Exception e) {
                continue;
            }

            if (choice.strip().equals("1")) {
                setPractice1(s);
                System.out.println();
                setPractice2(s);
            }

            if (choice.strip().equals("9")) {
                System.exit(0);
            }

        }
    }
}
