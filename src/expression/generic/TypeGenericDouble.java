package expression.generic;

import java.math.BigInteger;

public class TypeGenericDouble implements GenericOperations<Double> {
    public TypeGenericDouble() {
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }
    @Override
    public Double subsctract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }
    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double less(Double a, Double b) {
        return (Double.compare(a, b)) < 0 ? 1.0 : 0.0;
    }

    @Override
    public Double lessEq(Double a, Double b) {
        return (Double.compare(a, b)) <= 0 ? 1.0 : 0.0;
    }

    @Override
    public Double greater(Double a, Double b) {
        return (Double.compare(a, b)) > 0 ? 1.0 : 0.0;
    }

    @Override
    public Double greaterEq(Double a, Double b) {
        return (Double.compare(a, b)) >= 0 ? 1.0 : 0.0;
    }

    @Override
    public Double eq(Double a, Double b) {
        return a.equals(b) ? 1.0 : 0.0;
    }
    @Override
    public Double notEq(Double a, Double b) {
        return !a.equals(b) ? 1.0 : 0.0;
    }

    @Override
    public Double negete(Double a) {
        return -a;
    }

    @Override
    public Double parseToType(String str) {
        return Double.parseDouble(str);
    }

    @Override
    public Double toType(int x) {
        return Double.valueOf(x);
    }
}
