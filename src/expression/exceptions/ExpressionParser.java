package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) throws AbstrExcptParse {
        StringSource data = new StringSource(expression);
        Parser prs = new Parser(data);

        return prs.parse();
    }

    private static class Parser extends BaseParser {
        private char curTypeOfBrackets = 0;
        private final char[] openingParenthesis = {'(', '{', '['};
        private final char[] closingParenthesis = {')', '}', ']'};

        public Parser(CharSource source) {
            super(source);
        }

        private boolean isAllowedSymbol() {
            skipWhitespace();
            char curChar = getCurChar();

            boolean brckts = true;
            for (int i = 0; i < openingParenthesis.length; i++) {
                brckts = (curChar != openingParenthesis[i] && curChar != closingParenthesis[i]) && brckts;
            }

            boolean binOper = curChar != '*' && curChar != '/' && curChar != '+' && curChar != '-' && curChar != '<' && curChar != '>' && curChar != '=' && curChar != '!';

            return (binOper && brckts);
        }

        public MyExpression parse() throws AbstrExcptParse {
            MyExpression res = parseUtil();

            if (!eof()) {
                for (int i = 0; i < closingParenthesis.length; i++) {
                    if (closingParenthesis[i] == getCurChar() && curTypeOfBrackets == 0) {
                        throw new ExcptIncorrectBrackets("No opening parenthesis. Expected: " + openingParenthesis[i] + ".");
                    }
                }
                throw new ExcptIncorrectSymbol("Parsing finished, but not EOF. Check fictitious symbols; pos: " + (getCurPos() - 1));
            }

            return res;
        }

        public MyExpression parseUtil() throws AbstrExcptParse {
            skipWhitespace();
            MyExpression exprL = parseGreaterLess(-1);
            while (!eof()) {
                skipWhitespace();
                if (takeStr("==")) {
                    MyExpression exprR = parseGreaterLess(1);
                    exprL = new BinEq(exprL, exprR);
                } else if (takeStr("!=")) {
                    MyExpression exprR = parseGreaterLess(1);
                    exprL = new BinNotEq(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public MyExpression parseGreaterLess(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            MyExpression exprL = parseAddSub(flLeftOrRight);
            while (!eof()) {
                skipWhitespace();
                if (takeStr(">=")) {
                    MyExpression exprR = parseAddSub(1);
                    exprL = new BinGreaterEq(exprL, exprR);
                } else if (takeStr("<=")) {
                    MyExpression exprR = parseAddSub(1);
                    exprL = new BinLessEq(exprL, exprR);
                } else if (take('>')) {
                    MyExpression exprR = parseAddSub(1);
                    exprL = new BinGreater(exprL, exprR);
                } else if (take('<')) {
                    MyExpression exprR = parseAddSub(1);
                    exprL = new BinLess(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public MyExpression parseAddSub(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            MyExpression exprL = parseT(flLeftOrRight);
            while (!eof()) {
                skipWhitespace();

                if (take('+')) {
                    MyExpression exprR = parseT(1);
                    exprL = new CheckedAdd(exprL, exprR);
                } else if (take('-')) {
                    MyExpression exprR = parseT(1);
                    exprL = new CheckedSubtract(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public MyExpression parseT(int flLeftOrRight) throws AbstrExcptParse {
            MyExpression exprL = parseF(flLeftOrRight);

            if (isAllowedSymbol() && !between('A', 'Z') && !between('a', 'z') && !between('0', '9') && !eof()) {
                throw new ExcptIncorrectSymbol("Incorrect symbol: " + "'" + getCurChar() + "'" + " in pos: " + getCurPos() + ".");
            }
            while (!eof()) {
                skipWhitespace();

                if (take('*')) {
                    MyExpression exprR = parseF(1);
                    exprL = new CheckedMultiply(exprL, exprR);
                } else if (take('/')) {
                    MyExpression exprR = parseF(1);
                    exprL = new CheckedDivide(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        private MyExpression parseSquare(MyExpression exprDash) throws AbstrExcptParse {
            return exprDash;
        }

        private MyExpression parseBracket(char open, char close, int flSgn) throws AbstrExcptParse {
            if (take(open)) {
                curTypeOfBrackets = open;
                skipWhitespace();
                MyExpression exprDash = parseUtil();
                if (take(close)) {
                    if (flSgn == -1) {
                        return parseSquare(new CheckedNegate(exprDash));
                    }
                    curTypeOfBrackets = 0;
                    return parseSquare(exprDash);
                } else {
                    for (char parenthesis : closingParenthesis) {
                        if (parenthesis != open && take(parenthesis)) {
                            throw new ExcptIncorrectBrackets("Mismatched close parenthesis. Expected: " + close + ", but: " + parenthesis + ".");
                        }
                    }

                    throw new ExcptIncorrectBrackets("No close parenthesis. Expected: " + close + ".");
                }
            }

            return null;
        }

        private MyExpression parsePh(int flSgn) throws AbstrExcptParse {
            MyExpression exprBrackets;
            if ((exprBrackets = parseBracket('(', ')', flSgn)) != null) {
                return exprBrackets;
            } else if ((exprBrackets = parseBracket('[', ']', flSgn)) != null) {
                return exprBrackets;
            } else if ((exprBrackets = parseBracket('{', '}', flSgn)) != null) {
                return exprBrackets;
            }

            return null;
        }

        private void takeVal(final StringBuilder sb) throws AbstrExcptParse {
            char ch = getCurChar();
            if (take('z')) {
                sb.append('z');
            } else if (take('x')) {
                sb.append('x');
            } else if (take('y')) {
                sb.append('y');
            } else {
                throw new ExcptIncorrectVarConst("Incorrect symbol in var: " + ch);
            }
        }

        public MyExpression parseF(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            if (take('-')) {
                if (between('0', '9')) {
                    StringBuilder digt = new StringBuilder();
                    takeInteger(digt);
                    try {
                        return parseSquare(new Const(Integer.parseInt("-" + digt)));
                    } catch (NumberFormatException e) {
                        throw new ExcptIncorrectVarConst("Overflow number: " + digt);
                    }
                } else {
                    MyExpression exprDash;
                    if ((exprDash = parsePh(-1)) != null) {
                        return exprDash;
                    } else {
                        skipWhitespace();
                        return new CheckedNegate(parseF(flLeftOrRight));
                    }
                }
            } else if (between('0', '9')) {
                StringBuilder digt = new StringBuilder();
                takeInteger(digt);
                try {
                    return parseSquare(new Const(Integer.parseInt(digt.toString())));
                } catch (NumberFormatException e) {
                    throw new ExcptIncorrectVarConst("Overflow number: " + digt);
                }
            } else if (between('A', 'Z') || between('a', 'z')) {
                StringBuilder var = new StringBuilder();
                takeVal(var);
                return parseSquare(new Variable(var.toString()));
            } else {
                MyExpression exprDash;
                if ((exprDash = parsePh(1)) != null) {
                    return exprDash;
                }
            }

            if (isAllowedSymbol() && !between('A', 'Z') && !between('a', 'z') && !between('0', '9') && !eof()) {
                throw new ExcptIncorrectSymbol("Incorrect symbol: " + "'" + getCurChar() + "'" + " in pos: " + getCurPos() + ".");
            }

            if (flLeftOrRight == -1) {
                throw new ExcptIncorrectVarConst("No left argument: const or var; pos: " + getCurPos());
            } else {
                throw new ExcptIncorrectVarConst("No right argument: const or var; pos: " + getCurPos());
            }
        }
    }
}
