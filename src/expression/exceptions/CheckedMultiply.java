package expression.exceptions;
import expression.*;

public class CheckedMultiply extends AbstractBinaryOper {
    public MyExpression expr1;
    public MyExpression expr2;

    public CheckedMultiply(MyExpression expr1, MyExpression expr2) {
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
        return 30;
    }

    @Override
    public boolean isKomut() {
        return true;
    }

    @Override
    public int calcInt(int a, int b) {
        if ((a == -1 && b == Integer.MIN_VALUE) || (b == -1 && a == Integer.MIN_VALUE)) {
            throw new ExcptOverflow(getPinOper());
        }

        if (a != -1 && b != -1) {
            if (a > 0 && b > 0) {
                if (a > Integer.MAX_VALUE / b) {
                    throw new ExcptOverflow(getPinOper());
                }
            } else if (a < 0 && b < 0) {
                if (a < Integer.MAX_VALUE / b) {
                    throw new ExcptOverflow(getPinOper());
                }
            } else if (a > 0) {
                if (b != 0 && a > Integer.MIN_VALUE / b) {
                    throw new ExcptOverflow(getPinOper());
                }
            } else {
                if (b != 0 && a != 0 && a < Integer.MIN_VALUE / b) {
                    throw new ExcptOverflow(getPinOper());
                }
            }
        }

        return a * b;
    }

    @Override
    public long calcLong(long left, long right) {
        return left * right;
    }

    @Override
    public String getPinOper() {
        return "*";
    }
}
