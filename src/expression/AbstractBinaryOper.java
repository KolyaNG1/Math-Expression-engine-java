package expression;

import java.util.*;

public abstract class AbstractBinaryOper implements BinaryExpr {
    protected abstract int calcInt(int left, int right);
    protected abstract long calcLong(long left, long right);
    protected abstract String getPinOper();

    public AbstractBinaryOper() {
    }

    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();

        boolean flDashL = true;
        boolean flDashR = true;

        int preorExprL = this.getLeftExpr().getPreor();
        int preorExprR = this.getRightExpr().getPreor();
        int preorThis = this.getPreor();

        if (preorExprL > 20) {
            flDashL = false;
        }

        switch (preorThis) {
            case 500 -> {
                if (preorExprL < 100) {
                    flDashL = true;
                }

                if (preorExprR >= 100 && preorExprR < 500) {
                    flDashR = false;
                }
            }
            case 40 -> {
                if (preorExprR >= 100) {
                    flDashR = false;
                }

            }
            case 30 -> {
                if (preorExprR == 30 || preorExprR == -1000) {
                    flDashR = false;
                }

                if (preorExprL == -1000) {
                    flDashL = false;
                }
                if (preorExprR >= 100) {
                    flDashR = false;
                }

            }
            case 20 -> {
                if (preorExprL >= 10) {
                    flDashL = false;
                }

                if (preorExprR > 20) {
                    flDashR = false;
                }
            }
            case 10 -> {
                if (preorExprL >= 10 || preorExprL == -1000) {
                    flDashL = false;
                }

                flDashR = preorExprR < 10;
            }
            case 5 -> {
                flDashR = false;

                flDashL = preorExprL == 3;

                if (preorExprR == 3) {
                    flDashR = true;
                }
            }
            case 3 -> {
                flDashR = false;
                flDashL = false;
            }
        }

        if (preorExprL < 0) {
            flDashL = false;
        }

        if (preorExprR < 0) {
            flDashR = false;
        }

        if ((preorExprR == 5 || preorExprR == 3) && preorThis == preorExprR) {
            flDashR = true;
        }

        if (preorThis < 500 && preorExprR == 500) {
            flDashR = false;
        }
        
        String dashBg = "";
        String dashEnd = "";

        if (preorThis == 100) {
            res.append("-");
            if (preorExprR == -1000 || (preorExprR >= 100 && preorExprR < 500)) {
                res.append(" ");
                res.append(getRightExpr().toMiniString());
            } else {
                res.append("(");
                res.append(getRightExpr().toMiniString());
                res.append(")");
            }

            return res.toString();

        } else if (preorThis == 101) {
            if (preorExprR != -1000 && preorExprR != 101) {
                res.append("(");
                res.append(getRightExpr().toMiniString());
                res.append(")");
            } else {
                res.append(getRightExpr().toMiniString());
            }

            return res+ this.getPinOper();
        } else {
            if (flDashL) {
                dashBg = "(";
                dashEnd = ")";
            }

            res.append(dashBg);
            res.append(this.getLeftExpr().toMiniString());
            res.append(dashEnd);

            res.append(" ");
            res.append(this.getPinOper());
            res.append(" ");

            dashBg = "";
            dashEnd = "";
            if (flDashR) {
                dashBg = "(";
                dashEnd = ")";
            }
            res.append(dashBg);
            res.append(this.getRightExpr().toMiniString());
            res.append(dashEnd);

            return res.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("(");
        res.append(this.getLeftExpr().toString());
        res.append(" ");
        res.append(this.getPinOper());
        res.append(" ");
        res.append(this.getRightExpr().toString());
        res.append(")");

        return res.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLeftExpr().hashCode(), this.getRightExpr().hashCode(), this.getPinOper());
    }

    @Override
    public boolean equals(Object expr) {
        if (expr instanceof AbstractBinaryOper) {
            AbstractBinaryOper eqExpr = (AbstractBinaryOper)expr;
            return this.getPinOper().equals(eqExpr.getPinOper()) && this.getLeftExpr().equals(eqExpr.getLeftExpr()) && this.getRightExpr().equals(eqExpr.getRightExpr());
        } else {
            return false;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calcInt(this.getLeftExpr().evaluate(x, y, z), this.getRightExpr().evaluate(x, y, z));
    }

    @Override
    public int evaluate(int v) {
        return calcInt(this.getLeftExpr().evaluate(v), this.getRightExpr().evaluate(v));
    }

    @Override
    public long evaluateL(Map<String, Long> map) {
        return calcLong(this.getLeftExpr().evaluateL(map), this.getRightExpr().evaluateL(map));
    }
}