package expression.generic;

public abstract class AbstrGenericBinaryOper<T> implements GenericBinaryOper<T> {
    protected abstract T calculate(T left, T right);
    //protected abstract T toType(int v);

    public AbstrGenericBinaryOper() {
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return calculate(this.getLeftExpr().evaluate(x, y, z), this.getRightExpr().evaluate(x, y, z));
    }
}
