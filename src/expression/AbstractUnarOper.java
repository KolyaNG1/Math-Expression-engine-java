package expression;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractUnarOper implements UnarExpr {
    protected abstract int calcInt(int v);
//    protected abstract long calcLong(long left, long right);
    protected abstract String getPinOper();

    public AbstractUnarOper() {
    }


    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();

        int preorExprR = this.getExpr().getPreor();
        int preorThis = this.getPreor();

        switch (preorThis) {
            case 100 -> {
                res.append("-");
                if (preorExprR == -1000 || (preorExprR >= 100 && preorExprR < 500)) {
                    res.append(" ");
                    res.append(getExpr().toMiniString());
                } else {
                    res.append("(");
                    res.append(getExpr().toMiniString());
                    res.append(")");
                }

                return res.toString();
            }
            case 101 -> {
                if (preorExprR != -1000 && preorExprR != 101) {
                    res.append("(");
                    res.append(getExpr().toMiniString());
                    res.append(")");
                } else {
                    res.append(getExpr().toMiniString());
                }

                return res.toString() + this.getPinOper();
            }
            default -> {
                return res.toString();
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getExpr().hashCode(), this.getPinOper());
    }

    @Override
    public boolean equals(Object expr) {
        if (expr instanceof AbstractUnarOper) {
            AbstractUnarOper eqExpr = (AbstractUnarOper)expr;
            return this.getPinOper().equals(eqExpr.getPinOper()) && this.getExpr().equals(eqExpr.getExpr());
        } else {
            return false;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calcInt(this.getExpr().evaluate(x, y, z));
    }

    @Override
    public int evaluate(int v) {
        return calcInt(this.getExpr().evaluate(v));
    }
}