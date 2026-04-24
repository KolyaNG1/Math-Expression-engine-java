package expression.generic;

import expression.exceptions.AbstrExcptParse;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(
            String mode, String expression,
            int x1, int x2, int y1, int y2, int z1, int z2
    ) throws Exception {
        Object[][][] res = new Object[Math.abs(x2 - x1 + 1)][Math.abs(y2 - y1 + 1)][Math.abs(z2 - z1 + 1)];

        switch (mode) {
            case "i" -> {
                return getAnsv(new TypeGenericInteger(true), expression, x1, x2, y1, y2, z1, z2);
            }
            case "d" -> {
                return getAnsv(new TypeGenericDouble(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "bi" -> {
                return getAnsv(new TypeGenericBigInteger(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "u" -> {
                return getAnsv(new TypeGenericInteger(false), expression, x1, x2, y1, y2, z1, z2);
            }
            case "l" -> {
                return getAnsv(new TypeGenericLong(false), expression, x1, x2, y1, y2, z1, z2);
            }
            default -> {
                return null;
            }
        }
    }

    private <T> Object[][][] getAnsv(GenericOperations<T> genericOperations, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws AbstrExcptParse {
        ExpressionParser<T> parser = new ExpressionParser<>(genericOperations);

        Object[][][] res = new Object[Math.abs(x2 - x1 + 1)][Math.abs(y2 - y1 + 1)][Math.abs(z2 - z1 + 1)];

        GenericExpression<T> expr = parser.parse(expression);

        for (int x = 0; x1 + x <= x2; x++) {
            for (int z = 0; z1 + z <= z2; z++) {
                for (int y = 0; y1 + y <= y2; y++) {
                    res[x][y][z] = expr.evaluate(genericOperations.toType(x1 + x),
                            genericOperations.toType(y1 + y),
                            genericOperations.toType(z1 + z));
                }
            }
        }

        return res;
    }
}
