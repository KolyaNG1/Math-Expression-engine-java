package expression.generic;
import expression.*;

public class GenericAdd<T> extends AbstrGenericBinaryOper<T> {
    private final GenericExpression<T> expr1;
    private final GenericExpression<T> expr2;
    private final GenericOperations<T> genericOperations;

    public GenericAdd(GenericExpression<T> expr1, GenericExpression<T> expr2, GenericOperations<T> genericOperations) {
        super();
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.genericOperations = genericOperations;
    }

    @Override
    public GenericExpression<T> getRightExpr() {
        return expr2;
    }

    @Override
    public GenericExpression<T> getLeftExpr() {
        return expr1;
    }


    @Override
    public T calculate(T left, T right) {
        if (left == null || right == null) {
            return null;
        }
        return genericOperations.add(left, right);
    }
}
