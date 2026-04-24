package expression;

public interface MyExpression extends Expression, TripleExpression, LongMapExpression {
    int getPreor();
    boolean isKomut();
}
