package expression.exceptions;
import expression.*;

public class CheckedAdd extends AbstractBinaryOper {
    private MyExpression expr1;
    private MyExpression expr2;

    public CheckedAdd(MyExpression expr1, MyExpression expr2) {
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
        return 10;
    }

    @Override
    public boolean isKomut() {
        return true;
    }

    @Override
    public int calcInt(int left, int right) {
        if ((left < 0 && right < Integer.MIN_VALUE - left) || (left > 0 && right > Integer.MAX_VALUE - left)) {
            throw new ExcptOverflow(getPinOper());
        }
        return left + right;
    }

    @Override
    public long calcLong(long left, long right) {
        return left + right;
    }

    @Override
    public String getPinOper() {
        return "+";
    }
}
