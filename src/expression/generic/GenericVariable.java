package expression.generic;

public class GenericVariable<T> implements GenericExpression<T> {
    private final String nameVal;

    public GenericVariable(String nameV) {
        this.nameVal = nameV;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (nameVal.charAt(nameVal.length() - 1)) {
            case 'x' -> {
                return x;
            }
            case 'y' -> {
                return y;
            }
            case 'z' -> {
                return z;
            }
            default -> {
                throw new IllegalStateException("Error: Variable " + nameVal + " is prohibited.");
            }
        }
    }
}
