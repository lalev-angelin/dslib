package dslib.testapp;

import dslib.*;

import java.io.UnsupportedEncodingException;
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
        System.out.println("1. Задачи по действия с множества 1");
        System.out.println("2. Задачи по действия с множества 2");
        System.out.println("3. Задачи по действия с множества 3");
        System.out.println("4. Задачи за релации");
        System.out.println("9. Изход");
        System.out.print("Изберете [12349]: ");
    }




    public static void relationPractice1(Scanner s) {
        Random r = new Random(System.currentTimeMillis());
        int numPairs = 2 + r.nextInt(3);
        List<DSPair> pairs = new ArrayList<>();
        for (int i=0; i<numPairs; i++) {
            pairs.add(new DSPair(String.valueOf(r.nextInt(5)), String.valueOf(r.nextInt(5))));
        }

        List<DSPair> members = new ArrayList<>();
        for (int i=0; i<pairs.size(); i++) {
            if (r.nextBoolean()) {
                members.add(pairs.get(i));
            }
        }
        DSRelation r1 = new DSRelation(members);
        members.clear();

        for (int i=0; i<pairs.size(); i++) {
            if (r.nextBoolean()) {
                members.add(pairs.get(i));
            }
        }
        DSRelation r2 = new DSRelation(members);

        System.out.println("Намерете "+r1+" \u25ef "+r2);

        System.out.println("Натиснете Enter за отговор: ");
        s.nextLine();

        System.out.println(r1+" \u25ef "+r2+" = "+r1.compose(r2));
    }


    public static void main( String[] args ) throws UnsupportedEncodingException {
        Application application = new Application();
        application.run();

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
                Excercises.setPractice1(
                        System.out::println,
                        (String z)-> {
                            System.out.println(z);
                            s.nextLine();
                        });
                System.out.println();
                Excercises.setPractice3(System.out::println, (String z)-> {
                    System.out.println(z);
                    s.nextLine();
                });
                continue;
            }

            if (choice.strip().equals("2")) {
                relationPractice1(s);
                continue;
            }

            if (choice.strip().equals("9")) {
                System.exit(0);
            }

        }
    }
}
