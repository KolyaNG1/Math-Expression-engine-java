package expression;

import java.util.*;

public class Variable implements MyExpression {
    private final String nameVal;

    public Variable(String nameV) {
        this.nameVal = nameV;
    }

    @Override
    public int getPreor() {
        return -1000;
    }

    @Override
    public boolean isKomut() {
        return true;
    }


    @Override
    public int evaluate(int v) {
        if (!nameVal.equals("x")) {
            throw new IllegalStateException("Error: Variable " + nameVal + " is prohibited.");
        }
        return v;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (nameVal.charAt(nameVal.length() - 1)) {
            case 'x' -> {
                return x;
            }
            case 'y' -> {
                return y;
            }
            case 'z' -> {
                return z;
            }
            default -> {
                throw new IllegalStateException("Error: Variable " + nameVal + " is prohibited.");
            }
        }
    }

    @Override
    public long evaluateL(Map<String, Long> map) {
        Long val;
        if ((val = map.get(nameVal)) != null) {
            return (Long)val;
        } else {
            throw new IllegalStateException("Error: The variable " + nameVal + " is not defined.");
        }
    }

    @Override
    public String toString() {
        return nameVal;
    }

    @Override
    public String toMiniString() {
        return nameVal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameVal);
    }

    @Override
    public boolean equals(Object expr) {
        if (!(expr instanceof Variable)) {
            return false;
        } else {
            return this.nameVal.equals(((Variable)expr).nameVal);
        }
    }
}
