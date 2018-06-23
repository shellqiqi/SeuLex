# SeuLex

A Lexical-Analyzer Generator written in Java. The target language is C++.

Its core tech is reverse polish notation.

## Build

Use maven to package.

## Usage

We need you to write a lex file.
Here is an [example lex file](resource/example2.lex) of ANSI C.

Running JAR need two arguments.

```sh
path/to/intputFile path/to/outputFile
```

The input file is your lex file.
The output file is the C++ lexer code we generated.
And you will get a head file `yy.tab.h`.

## What functions we provide

```cpp
void count();
int yywrap();
void comment();
int check_type();
int yylex();
```

Feel free to modify them.

## Example

The example using the [example lex file](resource/example2.lex) of ANSI C. Run JAR with it.

```sh
$ java -jar SeuLex-1.0.jar path/to/example2.lex path/to/lex.cpp
```

Then we will get 2 files like `lex.cpp` and `yy.tab.h`. Compile them in a c++ compiler. Assume the executable file we get is `lex.exe`, execute it with one argument which is the file you want to perform lexical analysis. In addition, you can take no arguments to execute `lex.exe` which allows you to type your code in the console directly.

Input code segment

```cpp
int main() {return 0;}
```

Output in console

```
Match word: int ID: 142
Match word:     ID: 0
Match word: main        ID: 10000
Match word: (   ID: 40
Match word: )   ID: 41
Match word:     ID: 0
Match word: {   ID: 123
Match word: return      ID: 181
Match word:     ID: 0
Match word: 0   ID: 128
Match word: ;   ID: 59
Match word: }   ID: 125
```
