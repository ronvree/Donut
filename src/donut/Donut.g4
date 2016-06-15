grammar Donut;

import DonutVocab;


expr : numExpr | boolExpr | <assoc=right> expr eqOperator expr;

numExpr : integer
        | <assoc=right> numExpr operator numExpr
        | LPAR numExpr RPAR;

boolExpr : <assoc=right> boolExpr boolOperator boolExpr
        | numExpr compOperator numExpr
        | booleanValue
        | LPAR boolExpr RPAR;



varDecl : intDecl | boolDecl | charDecl | arrayDecl ;

intDecl : INTTYPE ID ASSIGN expr;
boolDecl : BOOLEANTYPE ID ASSIGN expr;
charDecl : CHARTYPE ID ASSIGN APOSTROPHE CHAR APOSTROPHE;
arrayDecl : ARRAYTYPE type ID ASSIGN ARRAYTYPE integer type;


integer : DIGIT19 DIGIT09*;

booleanValue : TRUE | FALSE;

type : INTTYPE | BOOLEANTYPE | CHARTYPE ;
operator : PLUS | MINUS | MULT | DIV | POW;
boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT;
eqOperator : EQUALS | NOTEQUALS;