package expression.generic;
import expression.*;

import java.util.*;

public class GenericNegate<T> extends AbstrGenericUnarOper<T> {
    private GenericExpression<T> expr;
    private final GenericOperations<T> genericOperations;

    public GenericNegate(GenericExpression<T> expr, GenericOperations<T> genericOperations) {
        super();
        this.expr = expr;
        this.genericOperations = genericOperations;
    }


    @Override
    public GenericExpression<T> getExpr() {
        return expr;
    }

    @Override
    public T calculate(T v) {
        if (v == null) {
            return null;
        }
        return genericOperations.negete(v);
    }
}