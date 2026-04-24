package expression;

import java.util.Map;
import java.util.Objects;

public class Const implements MyExpression {
    private final long longVal;

    public Const(int v) {
        this.longVal = (long)v;
    }

    public Const(long v) {
        this.longVal = v;
    }
    @Override
    public boolean isKomut() {
        return true;
    }
//    @Override
//    public MyExpression getRightExpr() {
//        return this;
//    }
//
//    @Override
//    public MyExpression getLeftExpr() {
//        return this;
//    }

    @Override
    public int getPreor() {
        return -1000;
    }

    @Override
    public int evaluate(int v) {
        return (int)longVal;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (int)longVal;
    }

    @Override
    public long evaluateL(Map<String, Long> map) {
        return longVal;
    }

    @Override
    public String toString() {
        return Long.toString(longVal);
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash((int)longVal);
    }

    @Override
    public boolean equals(Object expr) {
        if (expr instanceof Const) {
            return this.longVal == ((Const)expr).longVal;
        } else {
            return false;
        }
    }
}
