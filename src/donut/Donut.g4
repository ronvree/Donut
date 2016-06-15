grammar Donut;

import DonutVocab;


expr : numExpr | boolExpr | expr eqOperator expr;

numExpr : numExpr operator numExpr
        | LPAR numExpr RPAR
        | NUM;

boolExpr: FALSE | TRUE
        | boolExpr boolOperator boolExpr
        | numExpr compOperator numExpr
        | LPAR boolExpr RPAR;


varDecl : intDecl | boolDecl | charDecl | arrayDecl ;

intDecl : INTTYPE ID ASSIGN expr;
boolDecl : BOOLEANTYPE ID ASSIGN expr;
charDecl : CHARTYPE ID ASSIGN APOSTROPHE CHAR APOSTROPHE;
arrayDecl : ARRAYTYPE type ID ASSIGN ARRAYTYPE NUM type;

type : INTTYPE | BOOLEANTYPE | CHARTYPE ;
operator : PLUS | MINUS | MULT | DIV | POW;
boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT;
eqOperator : EQUALS | NOTEQUALS;







