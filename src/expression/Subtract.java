package expression;

public class Subtract extends AbstractBinaryOper {
    public MyExpression expr1;
    public MyExpression expr2;

    public Subtract(MyExpression expr1, MyExpression expr2) {
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
