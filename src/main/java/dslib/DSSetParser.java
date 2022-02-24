package dslib;

import java.util.Deque;
import java.util.LinkedList;

public class DSSetParser {

    public enum ElementType {ET_STRING, ET_CHAR, ET_INTEGER, ET_DOUBLE, ET_LONG,
        ET_DSSET, ET_EMPTY_DSSET, ET_DSPAIR, ET_DSNTUPLE, ET_INVALID, ET_UNKONWN}

    private static int countCommas(String s) {

        Deque<Character> bracket = new LinkedList<>();
        int commaCount = 0;
        for (int i=1; i<s.length()-1; i++) {
            if ( (s.charAt(i)=='(') || (s.charAt(i)=='{') || (s.charAt(i)=='[') ||
                    (s.charAt(i)=='"') || (s.charAt(i))=='\'') {
                bracket.push(s.charAt(i));
            }

            if (s.charAt(i)==')') {
                if (bracket.isEmpty() || bracket.pop()!='(')
                    throw new IllegalArgumentException("Invalid string representation");
            }

            if (s.charAt(i)=='}') {
                if (bracket.isEmpty() || bracket.pop()!='{')
                    throw new IllegalArgumentException("Invalid string representation");
            }

            if (s.charAt(i)==']') {
                if (bracket.isEmpty() || bracket.pop()!='[')
                    throw new IllegalArgumentException("Invalid string representation");
            }

            if (s.charAt(i)=='"') {
                if (bracket.isEmpty() || bracket.pop()!='"')
                    throw new IllegalArgumentException("Invalid string representation");
            }

            if (s.charAt(i)=='\'') {
                if (bracket.isEmpty() || bracket.pop()!='\'')
                    throw new IllegalArgumentException("Invalid string representation");
            }

            if (s.charAt(i)==',' && bracket.isEmpty()) commaCount++;
        }

        if (!bracket.isEmpty()) {
            throw new IllegalArgumentException("Invalid string representation");
        }

        return commaCount;
    }


    static ElementType detect(String s) {
        s = s.strip();

        if (s.startsWith("{") && (s.endsWith("}"))) {
          s = s.substring(1, s.length()-1);
          if (s.isBlank())
              return ElementType.ET_EMPTY_DSSET;
          else
              return ElementType.ET_DSSET;
        }

        if (s.startsWith("(") && s.endsWith(")")) {
            s = s.substring(1, s.length()-1);
            int commaCount = countCommas(s);

            try {
                switch (commaCount) {
                    case 0:
                        return ElementType.ET_INVALID;
                    case 1:
                        return ElementType.ET_DSPAIR;
                    default:
                        return ElementType.ET_DSNTUPLE;
                }
            } catch (IllegalArgumentException e) {
                return ElementType.ET_INVALID;
            }
        }

        if (s.startsWith("\"") && s.endsWith("\"")) {
            return ElementType.ET_STRING;
        }

        if (s.startsWith("'") && s.endsWith("'")) {
            return ElementType.ET_CHAR;
        }

        boolean error = false;
        try {
            Integer.valueOf(s);
        } catch (NumberFormatException e) {
            error = true;
        }

        if (!error) {
            return ElementType.ET_INTEGER;
        }

        error = false;
        try {
            Long.valueOf(s);
        } catch (NumberFormatException e) {
            error = true;
        }

        if (!error) {
            return ElementType.ET_LONG;
        }

        error = false;
        try {
            Double.valueOf(s);
        } catch (NumberFormatException e) {
            error = true;
        }

        if (!error) {
            return ElementType.ET_DOUBLE;
        }

        return ElementType.ET_UNKONWN;
    }


//    public static DSSet parse(String s) {
//        List<String> elements = new ArrayList<>();
//
//        s = s.strip();
//        if (!isSetRepresentation(s)) {
//            throw new IllegalArgumentException("Invalid string representation of a set: " + s);
//        }
//
//        s = new StringBuilder(s).deleteCharAt(s.length() - 1).deleteCharAt(0).toString().strip();
//
//        if (s.isBlank()) {
//            return new DSSet();
//        }
//
//        Deque<Character> bracket = new LinkedList<>();
//        StringBuilder b = new StringBuilder();
//
//        for (int i = 0; i < s.length(); i++) {
//            if ((s.charAt(i) == '(') || (s.charAt(i) == '{') || (s.charAt(i) == '[') ||
//                    (s.charAt(i) == '"') || (s.charAt(i)=='\'')) {
//                bracket.push(s.charAt(i));
//            }
//
//            if (s.charAt(i) == ')') {
//                if (bracket.isEmpty() || bracket.pop() != '(') {
//                    throw new IllegalArgumentException("Ivalid string representation of a set: " + s);
//                }
//            }
//
//            if (s.charAt(i) == '}') {
//                if (bracket.isEmpty() || bracket.pop() != '{') {
//                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
//                }
//            }
//
//            if (s.charAt(i) == ']') {
//                if (bracket.isEmpty() || bracket.pop() != '[') {
//                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
//                }
//            }
//
//            if (s.charAt(i) == '"') {
//                if (bracket.isEmpty() || bracket.pop() != '"') {
//                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
//                }
//            }
//
//            if (s.charAt(i) == '\'') {
//                if (bracket.isEmpty() || bracket.pop() != '\'') {
//                    throw new IllegalArgumentException("Invalid string representation of a set: " + s);
//                }
//            }
//
//            if (s.charAt(i) == ',' && bracket.isEmpty()) {
//                elements.add(b.toString().strip());
//                b = new StringBuilder();
//                continue;
//            }
//
//            b.append(s.charAt(i));
//        }
//
//        elements.add(b.toString().strip());
//
//        //Mapping logic
//
//        List<DSElement> transformed = elements.stream()
//                .map(a -> {
//                    if (isSetRepresentation(a)) {
//                        return parse(a);
//                    }
//                    if (isPairRepresentation(a)) {
//                        return DSPair.<String, String>parse(a, (q) -> q, (p) -> p);
//                    }
//                    return new DSValue<String>(a);
//                }).toList();
//
//
//        return new DSSet(transformed.toArray(new DSElement[1]));
//    }
//

//    public static parsePair(String s) {
//
//        s = s.strip();
//        if (!(s.startsWith("(") && (s.endsWith(")")))) {
//            throw new IllegalArgumentException("Invalid string representation of a pair: "+s);
//        }
//
//        Deque<Character> bracket = new LinkedList<>();
//        StringBuilder b = new StringBuilder();
//        int commaCount = 0;
//
//        for (int i=1; i<s.length()-1; i++) {
//            if ((s.charAt(i) == '(') || (s.charAt(i) == '{') || (s.charAt(i) == '[') || (s.charAt(i) == '"')) {
//                bracket.push(s.charAt(i));
//            }
//
//            if (s.charAt(i) == ')') {
//                if (bracket.isEmpty() || bracket.pop() != '(') {
//                    throw new IllegalArgumentException("Ivalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == '}') {
//                if (bracket.isEmpty() || bracket.pop() != '{') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == ']') {
//                if (bracket.isEmpty() || bracket.pop() != '[') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == '"') {
//                if (bracket.isEmpty() || bracket.pop() != '"') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == ',' && bracket.isEmpty()) {
//                if (commaCount == 0) {
//                    first = convertT.apply(b.toString().strip());
//                    b = new StringBuilder();
//                }
//                if (commaCount == 1) {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                commaCount++;
//                continue;
//            }
//
//            b.append(s.charAt(i));
//        }
//
//        if (commaCount==1) {
//            second = convertU.apply(b.toString().strip());
//        } else {
//            throw new IllegalArgumentException("Invalid string representation of a pair");
//        }
//
//        return new DSPair<>(first, second);
//
//    }
//
//
//    public static <T extends Comparable<T>, U extends Comparable<U>> DSPair<T,U>  parsePairWithType(String s,
//                                                                                        Function<String, T> convertT,
//                                                                                        Function<String, U> convertU) {
//        T first = null;
//        U second = null;
//
//        s = s.strip();
//        if (!(s.startsWith("(") && (s.endsWith(")")))) {
//            throw new IllegalArgumentException("Invalid string representation of a pair: "+s);
//        }
//
//        Deque<Character> bracket = new LinkedList<>();
//        StringBuilder b = new StringBuilder();
//        int commaCount = 0;
//
//        for (int i=1; i<s.length()-1; i++) {
//            if ((s.charAt(i) == '(') || (s.charAt(i) == '{') || (s.charAt(i) == '[') || (s.charAt(i) == '"')) {
//                bracket.push(s.charAt(i));
//            }
//
//            if (s.charAt(i) == ')') {
//                if (bracket.isEmpty() || bracket.pop() != '(') {
//                    throw new IllegalArgumentException("Ivalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == '}') {
//                if (bracket.isEmpty() || bracket.pop() != '{') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == ']') {
//                if (bracket.isEmpty() || bracket.pop() != '[') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == '"') {
//                if (bracket.isEmpty() || bracket.pop() != '"') {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                continue;
//            }
//
//            if (s.charAt(i) == ',' && bracket.isEmpty()) {
//                if (commaCount == 0) {
//                    first = convertT.apply(b.toString().strip());
//                    b = new StringBuilder();
//                }
//                if (commaCount == 1) {
//                    throw new IllegalArgumentException("Invalid string representation of a pair: " + s);
//                }
//                commaCount++;
//                continue;
//            }
//
//            b.append(s.charAt(i));
//        }
//
//        if (commaCount==1) {
//            second = convertU.apply(b.toString().strip());
//        } else {
//            throw new IllegalArgumentException("Invalid string representation of a pair");
//        }
//
//        return new DSPair<>(first, second);
//
//    }

}
