package expression.generic;

public interface GenericOperations<T> {
    T add(T a, T b);
    T subsctract(T a, T b);
    T divide(T a, T b);
    T multiply(T a, T b);

    T less(T a, T b);
    T lessEq(T a, T b);

    T greater(T a, T b);
    T greaterEq(T a, T b);

    T eq(T a, T b);
    T notEq(T a, T b);

    T negete(T a);

    T parseToType(String str);

    T toType(int v);
}
