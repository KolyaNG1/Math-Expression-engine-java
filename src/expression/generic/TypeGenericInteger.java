package expression.generic;

public class TypeGenericInteger implements GenericOperations<Integer> {
    private final boolean flSignd;

    public TypeGenericInteger(boolean flSignd) {
        this.flSignd = flSignd;
    }

    @Override
    public Integer add(Integer left, Integer right) {
        if ((flSignd) && ((left < 0 && right < Integer.MIN_VALUE - left) || (left > 0 && right > Integer.MAX_VALUE - left))) {
            return null;
        }


        return left + right;
    }
    @Override
    public Integer subsctract(Integer left, Integer right) {
        if ((flSignd) && (left != Integer.MIN_VALUE && right != Integer.MIN_VALUE)) {
            if ((left > 0 && -right > Integer.MAX_VALUE - left) || (left < 0 && -right < Integer.MIN_VALUE - left)) {
                return null;
            }
        }

        if (flSignd) {
            if (left == Integer.MIN_VALUE && right > 0) {
                return null;
            }

            if (right == Integer.MIN_VALUE && left >= 0) {
                return null;
            }
        }


        return left - right;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            return null;
        }

        if (a == 0) {
            return 0;
        }

        if (a == 1 || b == 1) {
            return a / b;
        }

        if (flSignd) {
            if ((b == -1 && a == Integer.MIN_VALUE)) {
                return null;
            }


            if (a != -1 && b != -1) {
                if (a > 0 && b > 0) {
                    if (b < 1 && a > Integer.MAX_VALUE * b) {
                        return null;
                    }
                } else if (a < 0 && b < 0) {
                    if (b > -1 && a > Integer.MAX_VALUE * b) {
                        return null;
                    }
                } else if (a > 0) {
                    if (b > -1 && a > Integer.MIN_VALUE * b) {
                        return null;
                    }
                } else {
                    if (b < 1 && a > Integer.MIN_VALUE * b) {
                        return null;
                    }
                }
            }
        }

        return a / b;
    }
    @Override
    public Integer multiply(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        if (a == 1 || b == 1) {
            return a * b;
        }

        if (flSignd) {
            if ((a == -1 && b == Integer.MIN_VALUE) || (b == -1 && a == Integer.MIN_VALUE)) {
                return null;
            }


            if (a != -1 && b != -1) {
                if (a > 0 && b > 0) {
                    if (a > Integer.MAX_VALUE / b) {
                        return null;
                    }
                } else if (a < 0 && b < 0) {
                    if (a < Integer.MAX_VALUE / b) {
                        return null;
                    }
                } else if (a > 0) {
                    if (a > Integer.MIN_VALUE / b) {
                        return null;
                    }
                } else {
                    if (a < Integer.MIN_VALUE / b) {
                        return null;
                    }
                }
            }
        }

        return a * b;
    }

    @Override
    public Integer less(Integer a, Integer b) {
        return a < b ? 1 : 0;
    }

    @Override
    public Integer lessEq(Integer a, Integer b) {
        return a <= b ? 1 : 0;
    }

    @Override
    public Integer greater(Integer a, Integer b) {
        return a > b ? 1 : 0;
    }

    @Override
    public Integer greaterEq(Integer a, Integer b) {
        return a >= b ? 1 : 0;
    }

    @Override
    public Integer eq(Integer a, Integer b) {
        return a.equals(b) ? 1 : 0;
    }
    @Override
    public Integer notEq(Integer a, Integer b) {
        return !a.equals(b) ? 1 : 0;
    }

    @Override
    public Integer negete(Integer a) {
        if (a == null || ((flSignd) && a == Integer.MIN_VALUE)) {
            return null;
        }
        return -a;
    }

    @Override
    public Integer parseToType(String str) {
        return Integer.parseInt(str);
    }

    @Override
    public Integer toType(int x) {
        return Integer.valueOf(x);
    }
}
