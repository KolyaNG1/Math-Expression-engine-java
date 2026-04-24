package expression.generic;

import java.math.BigInteger;

public class TypeGenericBigInteger implements GenericOperations<BigInteger> {
    public TypeGenericBigInteger() {
    }

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subsctract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        try {
            return a.divide(b);
        } catch (ArithmeticException e) {
            return null;
        }

    }
    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger less(BigInteger a, BigInteger b) {
        return a.compareTo(b) < 0 ? BigInteger.ONE : BigInteger.ZERO;
    }


    @Override
    public BigInteger lessEq(BigInteger a, BigInteger b) {
        return a.compareTo(b) <= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigInteger greater(BigInteger a, BigInteger b) {
        return a.compareTo(b) > 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigInteger greaterEq(BigInteger a, BigInteger b) {
        return a.compareTo(b) >= 0 ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigInteger eq(BigInteger a, BigInteger b) {
        return a.equals(b) ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigInteger notEq(BigInteger a, BigInteger b) {
        return !a.equals(b) ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigInteger negete(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger parseToType(String str) {
        return new BigInteger(str);
    }

    @Override
    public BigInteger toType(int x) {
        return BigInteger.valueOf(x);
    }
}
