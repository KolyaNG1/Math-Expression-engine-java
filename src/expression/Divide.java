package expression;

public class Divide extends AbstractBinaryOper {
    private MyExpression expr1;
    private MyExpression expr2;

    public Divide(MyExpression expr1, MyExpression expr2) {
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
    public int calcInt(int left, int right) {
        return left / right;
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
