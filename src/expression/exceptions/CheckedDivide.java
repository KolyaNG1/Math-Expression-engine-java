package expression.exceptions;
import expression.*;

public class CheckedDivide extends AbstractBinaryOper {
    private MyExpression expr1;
    private MyExpression expr2;

    public CheckedDivide(MyExpression expr1, MyExpression expr2) {
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
        return 40;
    }
    @Override
    public boolean isKomut() {
        return false;
    }

    @Override
    public int calcInt(int a, int b) {
        if (b == 0) {
            throw new ExcptDivByZero("Error: divide by 0.");
        }

        if ((b == -1 && a == Integer.MIN_VALUE)) {
            throw new ExcptOverflow(getPinOper());
        }

        if (a > 0 && b > 0) {
            if (b < 1 && a > Integer.MAX_VALUE * b) {
                throw new ExcptOverflow(getPinOper());
            }
        } else if (a < 0 && b < 0) {
            if (b > -1 && a > Integer.MAX_VALUE * b) {
                throw new ExcptOverflow(getPinOper());
            }
        } else if (a > 0) {
            if (b > -1 && a > Integer.MIN_VALUE * b) {
                throw new ExcptOverflow(getPinOper());
            }
        } else {
            if (b < 1 && a > Integer.MIN_VALUE * b && a != 0) {
                throw new ExcptOverflow(getPinOper());
            }
        }

        return a / b;
    }

    @Override
    public long calcLong(long left, long right) {
        return left / right;
    }

    @Override
    public String getPinOper() {
        return "/";
    }
}
