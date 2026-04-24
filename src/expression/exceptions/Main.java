package expression.exceptions;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String s = "(y * (y * (1 + z)))";
        s = "(y * (y * (1 + z)))";

        s = "10 20";

        try {
            ExpressionParser ep = new ExpressionParser();
            System.out.println(ep.parse(s).toMiniString());
            //System.out.println(ep.parse(s).evaluate(Integer.MAX_VALUE - 1, 0, 765036185));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

