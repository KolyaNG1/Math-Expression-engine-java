package expression;

import java.util.Map;

public class UnarSquare extends AbstractUnarOper {
    private MyExpression expr;

    public UnarSquare(MyExpression expr) {
        super();
        this.expr = expr;
    }

    @Override
    public boolean isKomut() {
        return true;
    }

    @Override
    public int calcInt(int v) {
        return v * v;
    }

    @Override
    public int getPreor() {
        return 101;
    }

    @Override
    public MyExpression getExpr() {
        return expr;
    }

    @Override
    public String getPinOper() {
        return "²";
    }

    @Override
    public int evaluate(int v) {
        return expr.evaluate(v) * expr.evaluate(v);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return expr.evaluate(x, y, z) * expr.evaluate(x, y, z);
    }

    @Override
    public long evaluateL(Map<String, Long> map) {
        return expr.evaluateL(map) * expr.evaluateL(map);
    }

    @Override
    public String toString() {
        return "(" + expr.toString() + ")²";
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