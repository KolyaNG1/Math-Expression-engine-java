package expression.generic;

import expression.Expression;

public interface GenericExpression<T> {
    T evaluate(T x, T y, T z);
    //T toType(int v);
}
