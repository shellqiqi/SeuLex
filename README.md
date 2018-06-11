# SeuLex

A Lexical-Analyzer Generator written in Java. The target language is C++.

Its core tech are reverse polish notation.

## Build

Use maven to package

## Usage

We need you to write a lex file.
Here is an [example lex file](resource/example2.lex) of ANSI C.

Running JAR need two arguments.

```sh
InputFile OutputFile
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
