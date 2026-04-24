package expression.parser;

import expression.MyExpression;

public class Main {
    public static void main(String[] args) {

        String s = "(y * (y * (1 + z)))";
        s = "(y * (y * (1 + z)))";
        s = "y²²";

        ExpressionParser ep = new ExpressionParser();
//        ExpressionParser ep = new ExpressionParser();
        //System.out.println(ep.parse(s).toString());
        MyExpression e = (MyExpression)ep.parse(s);
        System.out.println("mnStr: " + e.toMiniString());
        System.out.println(ep.parse(s).evaluate(1542288286, -357011580, 765036185));
        //x=1542288286, y=-357011580, z=765036185

    }
}
