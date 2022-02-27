package dslib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DSRelation implements DSElement {
    private final List<DSPair> members;

    public DSRelation(DSPair member1, DSPair... more) {
        members = new ArrayList<>();
        members.add(member1);
        Arrays.stream(more)
                .forEach(m->members.add(m.copy()));
    }

    public DSRelation(String member1, String... more) {
        List<String> strMembers = new ArrayList<>();
        strMembers.add(member1);
        strMembers.addAll(Arrays.asList(more));

        members = new ArrayList<>();
        members.addAll(
                strMembers.stream()
                        .map(str-> {
                            DSElement dse = DSParser.parse(str);
                            if (!(dse instanceof DSPair)) {
                                throw new IllegalArgumentException("Malformed pair: " + dse);
                            }
                            return (DSPair) dse;
                        })
                        .toList());

    }

    @Override
    public DSElement copy() {
        return new DSRelation(members.get(0),
                members.stream()
                        .skip(1)
                        .toList().toArray(new DSPair[1])
        );
    }

    @Override
    public DSType getType() {
        return DSType.DS_RELATION;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;

        if (other instanceof DSRelation) {
            DSRelation test = (DSRelation) other;

            for (DSPair m : members) {
                boolean found = false;
                for (DSPair t : test.members) {
                    if (m.equals(t)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public DSSet asSet() {
        return new DSSet((DSElement) members);
    }

    @Override
    public String toString() {
        return asSet().toString();
    }

    public static DSRelation identityRelation(int dims) {
        List<DSPair> members = new ArrayList<>();

        if (dims<1) {
            throw new IllegalArgumentException("Invalid number of dimensions: "+dims);
        }

        for (int i=0; i<dims; i++) {
            members.add(new DSPair(String.valueOf(i), String.valueOf(i)));
        }

        if (dims==1) {
            return new DSRelation(members.get(0));
        } else {
            return new DSRelation(members.get(0),
                    members.stream()
                            .skip(1)
                            .toList()
                            .toArray(new DSPair[1]));
        }
    }

    public static DSRelation identityRelationOf(DSRelation other) {
        return identityRelation(other.getDimensions());
    }

    public int getDimensions() {
        return members.size();
    }

    public DSRelation inverse() {

        List<DSPair> inverseMembers = members.stream()
                .map(DSPair::invert)
                .toList();

        if (members.size()==1) {
            return new DSRelation(members.get(0));
        } else {
            return new DSRelation(members.get(0),
                    members.stream()
                            .skip(1)
                            .toList()
                            .toArray(new DSPair[1]));
        }
    }
}
