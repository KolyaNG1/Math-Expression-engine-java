package expression;

public class Pow extends AbstractBinaryOper {
    public MyExpression expr1;
    public MyExpression expr2;

    public Pow(MyExpression expr1, MyExpression expr2) {
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

    private static int pow(int a, int b) {
        int result = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                result *= a;
            }
            a *= a;
            b /= 2;
        }

        return result;
    }

    @Override
    public int calcInt(int left, int right) {
        return pow(left, right);
    }

    @Override
    public long calcLong(long left, long right) {
        return calcInt((int)left, (int)right);
    }

    @Override
    public String getPinOper() {
        return "**";
    }
}