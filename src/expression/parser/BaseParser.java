package expression.parser;

public class BaseParser {
    private static final char END = '\0';
    public final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char getCurChar() {
        return ch;
    }

    protected int getCurPos() {
        return source.getPos();
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch) && take(ch));
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean takeStr(final String str) {
        int tmp = source.getPos();
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(0) == '=' || str.charAt(0) == '!') {
                skipWhitespace();
            }
            if (test(str.charAt(i))) {
                take();
            } else {
                source.setPos(tmp - 1);
                take();
                return false;
            }

            i++;
        }
        return true;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected void takeChar(final StringBuilder sb) {
        while (between('A', 'Z') || between('a', 'z')) {
            sb.append(take());
        }
    }

    protected void takeWord(final StringBuilder sb) {
        if (take('z')) {
            sb.append('z');
        } else if (between('A', 'z')) {
            takeChar(sb);
        } else {
            throw error("Invalid number");
        }
    }

    protected void takeDigits(final StringBuilder sb) {
        while (between('0', '9')) {
            sb.append(take());
        }
    }

    protected void takeInteger(final StringBuilder sb) {
        if (take('-')) {
            sb.append('-');
        }
        if (take('0')) {
            sb.append('0');
        } else if (between('1', '9')) {
            takeDigits(sb);
        } else {
            throw error("Invalid number");
        }
    }
}
