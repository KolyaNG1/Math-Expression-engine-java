package expression.generic;

public class TypeGenericLong implements GenericOperations<Long> {
    private final boolean flSignd;

    public TypeGenericLong(boolean flSignd) {
        this.flSignd = flSignd;
    }

    @Override
    public Long add(Long left, Long right) {
        if ((flSignd) && ((left < 0 && right < Long.MIN_VALUE - left) || (left > 0 && right > Long.MAX_VALUE - left))) {
            return null;
        }


        return left + right;
    }
    @Override
    public Long subsctract(Long left, Long right) {
        if ((flSignd) && (left != Long.MIN_VALUE && right != Long.MIN_VALUE)) {
            if ((left > 0 && -right > Long.MAX_VALUE - left) || (left < 0 && -right < Long.MIN_VALUE - left)) {
                return null;
            }
        }

        if (flSignd) {
            if (left == Long.MIN_VALUE && right > 0) {
                return null;
            }

            if (right == Long.MIN_VALUE && left >= 0) {
                return null;
            }
        }


        return left - right;
    }

    @Override
    public Long divide(Long a, Long b) {
        if (b == 0) {
            return null;
        }

        if (a == 0) {
            return 0L;
        }

        if (a == 1 || b == 1) {
            return a / b;
        }

        if (flSignd) {
            if ((b == -1 && a == Long.MIN_VALUE)) {
                return null;
            }


            if (a != -1 && b != -1) {
                if (a > 0 && b > 0) {
                    if (b < 1 && a > Long.MAX_VALUE * b) {
                        return null;
                    }
                } else if (a < 0 && b < 0) {
                    if (b > -1 && a > Long.MAX_VALUE * b) {
                        return null;
                    }
                } else if (a > 0) {
                    if (b > -1 && a > Long.MIN_VALUE * b) {
                        return null;
                    }
                } else {
                    if (b < 1 && a > Long.MIN_VALUE * b) {
                        return null;
                    }
                }
            }
        }

        return a / b;
    }
    @Override
    public Long multiply(Long a, Long b) {
        if (a == 0 || b == 0) {
            return 0L;
        }

        if (a == 1 || b == 1) {
            return a * b;
        }

        if (flSignd) {
            if ((a == -1 && b == Long.MIN_VALUE) || (b == -1 && a == Long.MIN_VALUE)) {
                return null;
            }


            if (a != -1 && b != -1) {
                if (a > 0 && b > 0) {
                    if (a > Long.MAX_VALUE / b) {
                        return null;
                    }
                } else if (a < 0 && b < 0) {
                    if (a < Long.MAX_VALUE / b) {
                        return null;
                    }
                } else if (a > 0) {
                    if (a > Long.MIN_VALUE / b) {
                        return null;
                    }
                } else {
                    if (a < Long.MIN_VALUE / b) {
                        return null;
                    }
                }
            }
        }

        return a * b;
    }

    @Override
    public Long less(Long a, Long b) {
        return a < b ? Long.valueOf(1) : Long.valueOf(0);
    }

    @Override
    public Long lessEq(Long a, Long b) {
        return a <= b ? Long.valueOf(1) : Long.valueOf(0);
    }

    @Override
    public Long greater(Long a, Long b) {
        return a > b ? Long.valueOf(1) : Long.valueOf(0);
    }

    @Override
    public Long greaterEq(Long a, Long b) {
        return a >= b ? Long.valueOf(1) : Long.valueOf(0);
    }

    @Override
    public Long eq(Long a, Long b) {
        return a.equals(b) ? Long.valueOf(1) : Long.valueOf(0);
    }

    @Override
    public Long notEq(Long a, Long b) {
        return !a.equals(b) ? Long.valueOf(1) : Long.valueOf(0);
    }


    @Override
    public Long negete(Long a) {
        if (a == null || ((flSignd) && a == Long.MIN_VALUE)) {
            return null;
        }
        return -a;
    }

    @Override
    public Long parseToType(String str) {
        return Long.parseLong(str);
    }

    @Override
    public Long toType(int x) {
        return Long.valueOf(x);
    }
}
