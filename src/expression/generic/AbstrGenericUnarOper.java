package expression.generic;

public abstract class AbstrGenericUnarOper<T> implements GenericUnarOper<T> {
    protected abstract T calculate(T v);

    public AbstrGenericUnarOper() {
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calculate(this.getExpr().evaluate(x, y, z));
    }
}
