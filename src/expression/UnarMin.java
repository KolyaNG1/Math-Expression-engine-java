package expression;

import java.util.*;

public class UnarMin extends AbstractUnarOper {
    private MyExpression expr;

    public UnarMin(MyExpression expr) {
        super();
        this.expr = expr;
    }

    @Override
    public boolean isKomut() {
        return true;
    }

    @Override
    public MyExpression getExpr() {
        return expr;
    }


    @Override
    public int calcInt(int v) {
        return -v;
    }

    @Override
    public int getPreor() {
        return 100;
    }

    @Override
    public String getPinOper() {
        return "-";
    }

    @Override
    public int evaluate(int v) {
        return -expr.evaluate(v);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expr.evaluate(x, y, z);
    }

    @Override
    public long evaluateL(Map<String, Long> map) {
        return -expr.evaluateL(map);
    }

    @Override
    public String toString() {
        return "-" + "(" + expr.toString() + ")";
    }

    @Override
    public int hashCode() {
        return expr.hashCode();
    }

    @Override
    public boolean equals(Object expr) {
        return this.expr.equals(expr);
    }
}