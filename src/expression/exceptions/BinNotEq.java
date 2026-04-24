package expression.exceptions;

import expression.AbstractBinaryOper;
import expression.MyExpression;

public class BinNotEq extends AbstractBinaryOper {
    private MyExpression expr1;
    private MyExpression expr2;

    public BinNotEq(MyExpression expr1, MyExpression expr2) {
        super();
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public MyExpression getRightExpr() {
        return expr2;
    }

    @Override
    public MyExpression getLeftExpr() {
        return expr1;
    }

    @Override
    public int getPreor() {
        return 3;
    }

    @Override
    public boolean isKomut() {
        return true;
    }

    @Override
    public int calcInt(int left, int right) {
        return left != right ? 1 : 0;
    }

    @Override
    public long calcLong(long left, long right) {
        return left != right ? 1 : 0;
    }

    @Override
    public String getPinOper() {
        return "!=";
    }
}
