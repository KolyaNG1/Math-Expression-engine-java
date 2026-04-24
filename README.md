## 1. Состав `src/`

| Пакет | Назначение |
|--------|------------|
| `base/` | Инфраструктура тестов: `Selector`, `TestCounter`, `Runner`, `Asserts`, случайные числа и вспомогательные типы. Точка входа для сценариев проверки — `MainChecker` / запуск `*Test.main`. |
| `expression/` | Ядро: интерфейсы выражений, листья `Const` / `Variable`, бинарные/унарные операции, абстракции `AbstractBinaryOper` / `AbstractUnarOper`, вспомогательные `ExpressionTest` и т.д. |
| `expression/parser/` | Парсер **базовой** грамматики (`ExpressionParser` → `MyExpression`), `BaseParser`, `CharSource` / `StringSource`, `ParserTester` и наборы тестов парсера. |
| `expression/exceptions/` | Парсер **checked** + узлы `Checked*`, сравнения `Bin*`, исключения парсинга/счисления, `ExceptionsTester` / `ExceptionsTest`. |
| `expression/generic/` | Обобщённые выражения `GenericExpression<T>`, `GenericOperations<T>`, реализации типов (`TypeGenericInteger`, `TypeGenericDouble`, ...), `GenericTabulator`, `GenericTester` / `GenericTest`. |
| `expression/common/` | Общий код для генерации тестов: `Node`, `Renderer`, `TestGenerator` и т.п. (привязан к `ParserTestSet`). |

---

## 2. Модель выражений (AST)

### Интерфейсы

- `Expression` — `int evaluate(int x)` (одна переменная, по смыслу `x`).
- `TripleExpression` — `int evaluate(int x, int y, int z)`.
- `LongMapExpression` — `long evaluateL(Map<String, Long> variables)` (произвольные имена переменных в карте).
- `ToMiniString` — `toMiniString()` (минимальные скобки относительно приоритетов).
- `MyExpression` — объединяет `Expression`, `TripleExpression`, `LongMapExpression` и даёт `getPreor()`, `isKomut()` для печати.

### Листья

- `Const` — хранит `long`, при `evaluate` приводит к `int` (для `int`-вычислений).
- `Variable` — имя строки. Для `evaluate(int x, y, z)` берётся **последний символ** имени: ожидается `x`, `y` или `z` (см. `switch` по `nameVal.charAt(nameVal.length() - 1)`). В режиме `evaluate(int v)` допускается только имя `"x"`.

### Узлы операций (не exceptions)

- Арифметика: `Add`, `Subtract`, `Multiply`, `Divide` — наследники `AbstractBinaryOper`, `int`/`long` через `calcInt` / `calcLong`.
- `Pow` — бинарный оператор с символом `**` в `toString` / приоритете 500, целочисленное возведение в степень (свой цикл, без `Math.pow` для int).
- `Log` — оператор `//` в лексике базового парсера, внутри: `(int)(Math.log(left) / Math.log(right))` (см. `Log.calcInt`).
- Унарные: `UnarMin` (унарный `-`), `UnarSquare` (квадрат, в т.ч. цепочка `²` в парсере).

Базовые бинарные/унарные абстракции реализуют `equals`/`hashCode`, полноскобочный `toString` и `toMiniString` с логикой по `getPreor()` (числовые уровни приоритета: например `+` — 10, `*` — 20, `*` и `/` в `Divide` — 40, `**`/`Pow` — 500 и т.д.).

### Ветка `expression.exceptions` (checked + сравнения)

- Арифметика с проверками: `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`, `CheckedDivide`, `CheckedNegate` — при переполнении/делении на 0/граничных случаях бросают `ExcptOverflow`, `ExcptDivByZero` (наследники `AbstrExcptCount`).
- Логика: `*`, `+`, ... — `RuntimeException` при ошибке счисления; парсинг — `AbstrExcptParse` и конкретные `ExcptIncorrectSymbol`, `ExcptIncorrectBrackets`, `ExcptIncorrectVarConst`, `ExcptODZ` и т.д.

- Сравнения (результат `0` / `1` как int): `BinEq`, `BinNotEq`, `BinLess`, `BinLessEq`, `BinGreater`, `BinGreaterEq` — тоже через `AbstractBinaryOper.calcInt`.

---

## 3. Парсеры: три реализации, разная грамматика

Общие примитивы: `BaseParser` (текущий символ, `take`, `skipWhitespace`, `takeInteger`, `takeWord` / в exceptions свой разбор имён переменных), `CharSource` с позицией и `error(...)`.

### 3.1. `expression.parser.ExpressionParser` (`TripleParser` без `throws`)

- Рекурсия: сначала уровень `+`/`-` (`parse` → `parseT` → …), ниже `*`/`/`, затем `**` (`Pow`) и `//` (`Log`), затем `parseF` (унарный `-`, константы, идентификатор, скобки).
- Константы: разбор **long**-литералов в `parseF` (`Long.parseLong`), затем `Const`.
- Дополнительно: `²` после атома — `UnarSquare` (и рекурсивно).
- `TripleParser` в этом пакете: `TripleExpression parse(String)`.

**Важно:** в этой ветке **нет** сравнений `==`, `>=` и т.д. Есть `**`, `//`, `²` и «широкий» разбор «слова» для переменной (не только одна буква).

### 3.2. `expression.exceptions.ExpressionParser` (`TripleParser` с `throws Exception`)

- Уровни: сначала `==` / `!=`, потом `>=`, `<=`, `>`, `<`, потом `+`/`-` с `Checked*`, потом `*`/`/` с `Checked*`, атом `parseF`.
- Скобки: `( )`, `[ ]`, `{ }`; при `(-` внутри фиксированной пары скобок — трактуется как `CheckedNegate` поддерева.
- Константы: `Integer.parseInt` в `int`, overflow литерала — `ExcptIncorrectVarConst`.
- Переменные: `takeVal` разрешает **только** `x`, `y`, `z` (по одному символу), не произвольные идентификаторы.
- После успешного разбора — проверка, что весь вход consumed; иначе `ExcptIncorrectSymbol` / ошибки скобок.

`TripleParser` в `expression.exceptions` объявлен с `parse(...) throws Exception`.

### 3.3. `expression.generic.ExpressionParser<T>`

- Грамматика по уровням **аналогична** exceptions (сравнения, checked-подобная структура, скобки, `takeVal` только `x`/`y`/`z`).
- Вместо `MyExpression` строится `GenericExpression<T>`; операции делегируют в переданный `GenericOperations<T>` (сложение, сравнения как 0/1 в типе `T` и т.д.).

---

## 4. Обобщённые вычисления и табуляция

- `GenericOperations<T>` — набор `add`, `subsctract`, `divide`, `multiply`, сравнения, `negete`, `parseToType`, `toType(int)`.
- Реализации: `TypeGenericInteger` (флаг `flSignd` — checked vs переполнение в виде `null` и т.д.), `TypeGenericDouble`, `TypeGenericBigInteger`, `TypeGenericLong`.
- `GenericExpression<T>.evaluate(T x, T y, T z)` — вычисление по дереву.
- `GenericTabulator` реализует `Tabulator`: метод `tabulate(mode, expression, x1, x2, y1, y2, z1, z2)`:
  - `mode`: `"i"` (checked int), `"u"`, `"d"`, `"bi"`, `"l"`;
  - парсит выражение один раз, заполняет трёхмерный массив `Object[][][]` значениями `evaluate` на сетке (индексы завязаны на диапазоны в коде `getAnsv`).

---

## 5. Тестовый раннер (для ориентира)

- `expression.ExpressionTest`, `expression.TripleExpression` (свой `main` через `SELECTOR`), `expression.generic.GenericTest`, `expression.exceptions.ExceptionsTest` — запускают сценарии `easy`/`hard` и варианты вроде `Base`, `3233`, …
- `ParserTester` / `GenericTester` / `ExceptionsTester` подключают генераторы выражений из `expression.common` и `ParserTestSet`.

Для **описания проекта** достаточно знать, что проверка автоматизирована этим фреймворком; детали сценариев — в классах `*Test` / `*Tester`.

---

## 6. Сборка и запуск (без Maven/Gradle)

Скомпилировать все `*.java` под один `out/`:

**PowerShell (из корня репозитория с `src/`):**

```powershell
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -encoding UTF-8 -cp src -d out @files
```

**Пример запуска** уже существующих классов:

```text
java -cp out expression.Main 5
java -cp out expression.TripleExpression
java -cp out expression.exceptions.ExceptionsTest
java -cp out expression.generic.GenericTest
```

(аргументы `easy` / `hard` и варианты — см. `Selector` в `base`.)

---

## 7. Пример: как это работает (руками и через парсер)

### 7.1. Дерево в коде, вычисление по `x, y, z`

Выражение \((x + 2) \cdot y\) в терминах AST:

```java
import expression.*;

public class ExampleManual {
    public static void main(String[] args) {
        MyExpression e = new Multiply(
                new Add(new Variable("x"), new Const(2)),
                new Variable("y")
        );
        System.out.println(e.toString());     // полные скобки, вид как в тестах
        System.out.println(e.toMiniString()); // минимизация по приоритетам
        // x=1, y=3, z=0
        System.out.println(e.evaluate(1, 3, 0));  // (1+2)*3 = 9
    }
}
```

Сохранить как `src/ExampleManual.java` (пакет по умолчанию), пересобрать `javac` с этим файлом, запустить: `java -cp out ExampleManual`.

### 7.2. Строка → парсер exceptions → `evaluate`

Парсер ветки `exceptions` понимает сравнения и checked-арифметику; переменные в строке — только `x`, `y`, `z`.

```java
import expression.TripleExpression;
import expression.exceptions.ExpressionParser;

public class ExampleParse {
    public static void main(String[] args) throws Exception {
        ExpressionParser p = new ExpressionParser();
        TripleExpression e = p.parse("x * y + 1");
        System.out.println(e.toMiniString());
        // x=2, y=3, z=0  ->  2*3+1 = 7
        System.out.println(e.evaluate(2, 3, 0));
    }
}
```

`src/ExampleParse.java`, затем снова `javac` и `java -cp out ExampleParse`.

### 7.3. Базовый парсер: `**`, `//`, `²`

`expression.parser.ExpressionParser` — другая грамматика. Уровни в коде: внешний `parse()` — `+` / `-` между **термами** `parseT()`; внутри `parseT()` — `*` и `/`; внутри `parseTF()` — `**` (степень) и `//` (лог-формула из `Log`); в `parseF()` — унарный минус, константа, буква, скобки, цепочка `²` (`UnarSquare`).

Пример: строка `2**3+1` разбирается как `(2**3) + 1` → `8 + 1` → `9` при `evaluate` (аргументы `x,y,z` здесь не задействованы, но интерфейс требует три числа — можно передать `0,0,0`).

```java
import expression.TripleExpression;
import expression.parser.ExpressionParser;

public class ExampleParserExt {
    public static void main(String[] args) {
        ExpressionParser p = new ExpressionParser();
        TripleExpression e = p.parse("2**3+1");
        System.out.println(e.evaluate(0, 0, 0)); // 9
    }
}
```

Внутри `**` **без** пробела между `*` (иначе `takeStr("**")` не сработает). Рядом в репозитории тот же сценарий `parse` + `toMiniString` + `evaluate` смотри в `expression.parser.Main`.

---

## 8. Краткий список возможностей (что умеет код)

- Представлять выражение как **дерево** и вычислять в **одном** аргументе `x`, в **трёх** `x,y,z` или через **карту** имя → значение (`LongMapExpression` + `Variable` / тесты).
- **Печать** с полными скобками и **укороченная** с расстановкой скобок по приоритетам.
- **Три варианта парсера** с разной грамматикой: расширенный базовый (`**`, `//`, `²`, long-константы) и две «курсовых» ветки с сравнениями и `Checked*` (exceptions и generic).
- **Checked-целочисленная** арифметика с явными исключениями вместо молчаливого wrap-around.
- **Generic**-режим: одно и то же строковое выражение, разные типы и политика ошибок (`GenericTabulator` + режимы `i`/`u`/`d`/`bi`/`l`).
- **Встроенные** стресс-тесты с генерацией (пакеты `base`, `expression.common`, `*Tester` / `*Test`).
