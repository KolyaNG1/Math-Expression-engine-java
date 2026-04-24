package expression.exceptions;
import expression.*;

public class CheckedSubtract extends AbstractBinaryOper {
    public MyExpression expr1;
    public MyExpression expr2;

    public CheckedSubtract(MyExpression expr1, MyExpression expr2) {
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
        return 20;
    }

    @Override
    public boolean isKomut() {
        return false;
    }

    @Override
    protected int calcInt(int left, int right) {
        if (left != Integer.MIN_VALUE && right != Integer.MIN_VALUE) {
            if ((left > 0 && -right > Integer.MAX_VALUE - left) || (left < 0 && -right < Integer.MIN_VALUE - left)) {
                throw new ExcptOverflow(getPinOper());
            }
        }

        if (left == Integer.MIN_VALUE && right > 0) {
            throw new ExcptOverflow(getPinOper());
        }
        if (right == Integer.MIN_VALUE && left >= 0) {
            throw new ExcptOverflow(getPinOper());
        }


        return left - right;
    }

    @Override
    public long calcLong(long left, long right) {
        return left - right;
    }

    @Override
    public String getPinOper() {
        return "-";
    }
}
