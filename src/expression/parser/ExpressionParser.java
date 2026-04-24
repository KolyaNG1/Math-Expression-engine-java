package expression.parser;

import expression.*;
import expression.exceptions.AbstrExcptParse;

public class ExpressionParser implements TripleParser {

    @Override
    public TripleExpression parse(String expression) {
        return new Parser(new StringSource(expression)).parse();
    }

    public static Object parse(final CharSource source) {
        return new Parser(source).parse();
    }

    private static class Parser extends BaseParser {
        public Parser(CharSource source) {
            super(source);
        }

        /*
        E - parse()

        E -> T + E | T - E | T
        T -> F * T | F / T | F
        F -> N     | (E)
         */

        public MyExpression parse() {
            skipWhitespace();
            MyExpression exprL = parseT();
            while (!eof()) {
                skipWhitespace();

                if (take('+')) {
                    MyExpression exprR = parseT();
                    exprL = new Add(exprL, exprR);
                } else if (take('-')) {
                    MyExpression exprR = parseT();
                    exprL = new Subtract(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public MyExpression parseT() {
            MyExpression exprL = parseTF();
            while (!eof()) {
                skipWhitespace();

                if (take('*')) {
                    MyExpression exprR = parseTF();
                    exprL = new Multiply(exprL, exprR);
                } else if (take('/')) {
                    MyExpression exprR = parseTF();
                    exprL = new Divide(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }

        public MyExpression parseTF() {
            MyExpression exprL = parseF();
            while (!eof()) {
                skipWhitespace();

                if (takeStr("**")) {
                    MyExpression exprR = parseF();
                    exprL = new Pow(exprL, exprR);
                } else if (takeStr("//")) {
                    MyExpression exprR = parseF();
                    exprL = new Log(exprL, exprR);
                } else {
                    break;
                }
            }

            return exprL;
        }


        private MyExpression parseSquare(MyExpression exprDash) {
            skipWhitespace();
            if (take('²')) {
                return parseSquare(new UnarSquare(exprDash));
            } else {
                return exprDash;
            }
        }

        public MyExpression parseF() {
            skipWhitespace();
            if (take('-')) {
                if (between('0', '9')) {
                    StringBuilder digt = new StringBuilder();
                    takeInteger(digt);
                    return parseSquare(new Const(Long.parseLong("-" + digt.toString())));
                } else {
                    MyExpression exprDash = null;
                    if (take('(')) {
                        skipWhitespace();
                        exprDash = parse();
                        if (take(')')) {
                            return parseSquare(new UnarMin(exprDash));
                        }
                    } else {
                        skipWhitespace();
                        return new UnarMin(parseF());
                    }
                }
            } else if (between('0', '9')) {
                StringBuilder digt = new StringBuilder();
                takeInteger(digt);
                return parseSquare(new Const(Long.parseLong(digt.toString())));
            } else if (between('A', 'z')) {
                StringBuilder var = new StringBuilder();
                takeWord(var);
                return parseSquare(new Variable(var.toString()));
            } else if (take('(')) {
                skipWhitespace();
                MyExpression exprDash = parse();
                if (take(')')) {
                    return parseSquare(exprDash);
                }
            }

            throw error("Invalid word");
        }
    }
}
