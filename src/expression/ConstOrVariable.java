package expression;

import java.util.Map;

public interface ConstOrVariable {
    public int evaluate(int x, int y, int z);
    public int evaluate(int v);
    public long evaluateL(Map<String, Long> map);
}
