package dslib;

import java.util.*;

public class DSParser {

    private static final char[][] braces = {
            {'(', ')', 1},
            {'{', '}', 2},
            {'[', ']', 3},
            {'"', '"', 128},
            {'\'','\'', 129},
    };

    private static int openingBraceCode(char symbol) {
        for (char[] i : braces) {
            if (i[0]==symbol) {
                return i[2];
            }
        }
        return -1;
    }

    private static int closingBraceCode(char symbol) {
        for (char[] i : braces) {
            if (i[1]==symbol) {
                return i[2];
            }
        }
        return -1;
    }

    private static int countCommas(String s) {

        Deque<Integer> bracket = new LinkedList<>();
        int commaCount = 0;

        for (int i=0; i<s.length(); i++) {

            int code = openingBraceCode(s.charAt(i));
            if (code>=0) {
                if ((code & 128)==128 && !bracket.isEmpty() && bracket.peek()==code) {
                    //Not opening, but closing logical bracket that is " or ' and
                    // is the same for opening and closing.
                } else {
                    bracket.push(code);
                    continue;
                }
            }

            code = closingBraceCode(s.charAt(i));
            if (code>0) {
                if (bracket.pop()==code) {
                    continue;
                } else {
                    throw new IllegalArgumentException("Bracket mismatch");
                }
            }


            if (s.charAt(i)==',' && bracket.isEmpty()) commaCount++;
        }

        if (!bracket.isEmpty()) {
            throw new IllegalArgumentException("Invalid string representation");
        }

        return commaCount;
    }


    static List<String> split(String s) {
        s = s.strip();
        if (s.isBlank()) {
            return new ArrayList<>();
        }

        List<String> elements = new ArrayList<>();
        StringBuilder b = new StringBuilder();

        Deque<Integer> bracket = new LinkedList<>();
        for (int i=0; i<s.length(); i++) {

            int code = openingBraceCode(s.charAt(i));
            if (code>=0) {
                if ((code & 128)==128 && !bracket.isEmpty() && bracket.peek()==code) {
                    //Not opening, but closing logical bracket that is " or ' and
                    // is the same for opening and closing.
                } else {
                    bracket.push(code);
                    b.append(s.charAt(i));
                    continue;
                }
            }

            code = closingBraceCode(s.charAt(i));
            if (code>0) {
                if (bracket.pop()==code) {
                    b.append(s.charAt(i));
                    continue;
                } else {
                    throw new IllegalArgumentException("Bracket mismatch");
                }
            }

            if (s.charAt(i)==',' && bracket.isEmpty()) {
                elements.add(b.toString().strip());
                b = new StringBuilder();
                continue;
            }

            b.append(s.charAt(i));
        }

        elements.add(b.toString().strip());

        if (!bracket.isEmpty()) {
            throw new IllegalArgumentException("Invalid string representation");
        }

        return elements;
    }

    public static DSElement parse (String s) {
        s = s.strip();
        if (s.startsWith("{")) {
            if (s.endsWith("}")) {
                s = s.substring(1, s.length() - 1);
                List<String> strElements = split(s);
                List<DSElement> elements = strElements.stream()
                        .map(DSParser::parse)
                        .toList();
                if (elements.size() == 0) {
                    return new DSSet();
                } else {
                    return new DSSet(elements.toArray(new DSElement[1]));
                }
            } else {
                throw new IllegalArgumentException("Invalid representation of a set: " + s);
            }
        }


        if (s.startsWith("(")) {
            if (s.endsWith(")")) {
                s = s.substring(1, s.length() - 1);
                List<String> strElements = split(s);
                List<DSElement> elements = strElements.stream()
                        .map(DSParser::parse)
                        .toList();
                if (elements.size() < 2) {
                    throw new IllegalArgumentException("Invalid representation of a set: " + s);
                }

                if (elements.size() == 2) {
                    return new DSPair(elements.get(0), elements.get(1));
                } else {
                    return new DSNtuple(elements.get(0), elements.get(1),
                            elements.stream()
                                    .skip(2)
                                    .toList()
                                    .toArray(new DSElement[1]));
                }
            } else {
                throw new IllegalArgumentException("Invalid representation of a pair / ntuple: " + s);
            }
        }

        return new DSValue(s);
    }
}
