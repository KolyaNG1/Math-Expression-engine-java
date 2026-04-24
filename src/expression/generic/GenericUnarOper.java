package expression.generic;

public interface GenericUnarOper<T> extends GenericExpression<T> {
    GenericExpression<T> getExpr();
}
