package expression;


public class Main {
    public static void main(String[] args) {
        MyExpression expr = new Add(
                new Subtract(
                        new Multiply(new Variable("x"), new Variable("x")),
                        new Multiply(new Const(2), new Variable("x"))
                ),
                new Const(1)
        );

        expr = new Const(1);

        System.out.println(expr.equals(new Const(1)));

        System.out.println(
                new Multiply(new Variable("x"), new Const(2))
                .equals(new Multiply(new Variable("x"), new Const(2))));

        System.out.println(expr.evaluate(Integer.parseInt(args[0])));
    }

    private void test() {
        MyExpression x = new Variable("x");
        MyExpression y = new Variable("y");
        MyExpression e1 = new Add(x, y);
        MyExpression e2 = new Divide(x, y);
    }
}
