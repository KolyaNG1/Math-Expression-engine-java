package expression.generic;

import expression.MyExpression;

public interface GenericBinaryOper<T> extends GenericExpression<T> {
    GenericExpression<T> getLeftExpr();
    GenericExpression<T> getRightExpr();
}
