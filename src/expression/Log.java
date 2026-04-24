package expression;

public class Log extends AbstractBinaryOper {
    public MyExpression expr1;
    public MyExpression expr2;

    public Log(MyExpression expr1, MyExpression expr2) {
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
        return 500;
    }

    @Override
    public boolean isKomut() {
        return false;
    }

    @Override
    public int calcInt(int left, int right) {
        return (int)(Math.log(left) / Math.log(right));
    }

    @Override
    public long calcLong(long left, long right) {
        return (long)(Math.log(left) / Math.log(right));
    }

    @Override
    public String getPinOper() {
        return "//";
    }
}