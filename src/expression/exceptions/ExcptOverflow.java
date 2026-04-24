package expression.exceptions;

public class ExcptOverflow extends AbstrExcptCount {
    public ExcptOverflow(String pinOper) {
        super("Overflow: a " + pinOper + " b.");
    }
}
