package expression.generic;

public class GenericConst<T> implements GenericExpression<T> {
    private T var;
    public GenericConst(T var) {
        this.var = var;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return var;
    }
}
