package expression;

public interface BinaryExpr extends MyExpression {
    MyExpression getLeftExpr();
    MyExpression getRightExpr();
}
