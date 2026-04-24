package expression.generic;

import expression.exceptions.AbstrExcptParse;
import expression.exceptions.ExcptIncorrectBrackets;
import expression.exceptions.ExcptIncorrectSymbol;
import expression.exceptions.ExcptIncorrectVarConst;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

public class ExpressionParser<T> {
    public final GenericOperations<T> genericOperations;

    public ExpressionParser(GenericOperations<T> genericOperations) {
        this.genericOperations = genericOperations;
    }

    public GenericExpression<T> parse(String expression) throws AbstrExcptParse {
        StringSource data = new StringSource(expression);
        Parser<T> prs = new Parser<>(data, genericOperations);

        return prs.parse();
    }

    private static class Parser<T> extends BaseParser {
        private final GenericOperations<T> genericOperations;
        private char curTypeOfBrackets = 0;
        private final char[] openingParenthesis = {'(', '{', '['};
        private final char[] closingParenthesis = {')', '}', ']'};

        public Parser(CharSource source, GenericOperations<T> genericOperations) {
            super(source);
            this.genericOperations = genericOperations;
        }

        private boolean isAllowedSymbol() {
            skipWhitespace();
            char curChar = getCurChar();

            boolean brckts = true;
            for (int i = 0; i < openingParenthesis.length; i++) {
                brckts = (curChar != openingParenthesis[i] && curChar != closingParenthesis[i]) && brckts;
            }

            boolean binOper = curChar != '*' && curChar != '/'  && curChar != '+' && curChar != '-' && curChar != '<' && curChar != '>' && curChar != '=' && curChar != '!';

            return (binOper && brckts);
        }

        public GenericExpression<T> parse() throws AbstrExcptParse {
            GenericExpression<T> res = parseUtil();

            if (!eof()) {
                for (int i = 0; i < closingParenthesis.length; i++) {
                    if (closingParenthesis[i] == getCurChar() && curTypeOfBrackets == 0) {
                        throw new ExcptIncorrectBrackets("No opening parenthesis. Expected: " + openingParenthesis[i] + ".");
                    }
                }
                throw new ExcptIncorrectSymbol("Parsing finished, but not EOF. Check fictitious symbols; pos: "  + (getCurPos() - 1));
            }

            return res;
        }

        public GenericExpression<T> parseUtil() throws AbstrExcptParse {
            skipWhitespace();
            GenericExpression<T> exprL = parseGreaterLess(-1);
            while (!eof()) {
                skipWhitespace();
                 if (takeStr("==")) {
                     GenericExpression<T> exprR = parseGreaterLess(1);
                    exprL = new GenericEq<>(exprL, exprR, genericOperations);
                } else if (takeStr("!=")) {
                     GenericExpression<T> exprR = parseGreaterLess(1);
                    exprL = new GenericNotEq<>(exprL, exprR, genericOperations);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public GenericExpression<T> parseGreaterLess(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            GenericExpression<T> exprL = parseAddSub(flLeftOrRight);
            while (!eof()) {
                skipWhitespace();
                if (takeStr(">=")) {
                    GenericExpression<T> exprR = parseAddSub(1);
                    exprL = new GenericGreaterEq<>(exprL, exprR, genericOperations);
                } else if (takeStr("<=")) {
                    GenericExpression<T> exprR = parseAddSub(1);
                    exprL = new GenericLessEq<>(exprL, exprR, genericOperations);
                } else if (take('>')) {
                    GenericExpression<T> exprR = parseAddSub(1);
                    exprL = new GenericGreater<>(exprL, exprR, genericOperations);
                } else if (take('<')) {
                    GenericExpression<T> exprR = parseAddSub(1);
                    exprL = new GenericLess<>(exprL, exprR, genericOperations);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public GenericExpression<T> parseAddSub(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            GenericExpression<T> exprL = parseT(flLeftOrRight);
            while (!eof()) {
                skipWhitespace();

                if (take('+')) {
                    GenericExpression<T> exprR = parseT(1);
                    exprL = new GenericAdd<>(exprL, exprR, genericOperations);
                } else if (take('-')) {
                    GenericExpression<T> exprR = parseT(1);
                    exprL = new GenericSubtract<>(exprL, exprR, genericOperations);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public GenericExpression<T> parseT(int flLeftOrRight) throws AbstrExcptParse {
            GenericExpression<T> exprL = parseF(flLeftOrRight);

            if (isAllowedSymbol() && !between('A', 'Z') && !between('a', 'z') && !between('0', '9') && !eof()) {
                throw new ExcptIncorrectSymbol("Incorrect symbol: " + "'" + getCurChar() + "'" + " in pos: " + getCurPos() + ".");
            }
            while (!eof()) {
                skipWhitespace();

                if (take('*')) {
                    GenericExpression<T> exprR = parseF(1);
                    exprL = new GenericMultiply<>(exprL, exprR, genericOperations);
                } else if (take('/')) {
                    GenericExpression<T> exprR = parseF(1);
                    exprL = new GenericDivide<>(exprL, exprR, genericOperations);
                } else {
                    break;
                }
            }

            return exprL;
        }

        private GenericExpression<T> parseSquare(GenericExpression<T> exprDash) throws AbstrExcptParse {
            return exprDash;
        }

        private GenericExpression<T> parseBracket(char open, char close, int flSgn) throws AbstrExcptParse {
            if (take(open)) {
                curTypeOfBrackets = open;
                skipWhitespace();
                GenericExpression<T> exprDash = parseUtil();
                if (take(close)) {
                    if (flSgn == -1) {
                        return parseSquare(new GenericNegate<>(exprDash, genericOperations));
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

        private GenericExpression<T> parsePh(int flSgn) throws AbstrExcptParse {
            GenericExpression<T> exprBrackets;
            if ((exprBrackets = parseBracket('(', ')', flSgn)) != null) {
                return exprBrackets;
            } else if ((exprBrackets = parseBracket( '[', ']', flSgn)) != null) {
                return exprBrackets;
            } else if ((exprBrackets = parseBracket( '{', '}', flSgn)) != null) {
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

        public GenericExpression<T> parseF(int flLeftOrRight) throws AbstrExcptParse {
            skipWhitespace();
            if (take('-')) {
                if (between('0', '9')) {
                    StringBuilder digt = new StringBuilder();
                    takeInteger(digt);
                    try {
                        return parseSquare(new GenericConst<>(genericOperations.parseToType("-" + digt)));
                    } catch (NumberFormatException e) {
                        throw new ExcptIncorrectVarConst("Overflow number: " + digt);
                    }
                } else {
                    GenericExpression<T> exprDash;
                    if ((exprDash = parsePh(-1)) != null) {
                        return exprDash;
                    } else {
                        skipWhitespace();
                        return new GenericNegate<>(parseF(flLeftOrRight), genericOperations);
                    }
                }
            } else if (between('0', '9')) {
                StringBuilder digt = new StringBuilder();
                takeInteger(digt);
                try {
                    return parseSquare(new GenericConst<>(genericOperations.parseToType(digt.toString())));
                } catch (NumberFormatException e) {
                    throw new ExcptIncorrectVarConst("Overflow number: " + digt);
                }
            } else if (between('A', 'Z') || between('a', 'z')) {
                StringBuilder var = new StringBuilder();
                takeVal(var);
                return parseSquare(new GenericVariable<>(var.toString()));
            } else {
                GenericExpression<T> exprDash;
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
