grammar Donut;

import DonutVocab;

block : LBRACE stat* RBRACE;

stat: ID ASSIGN expr                            #assStat
    | IF LPAR expr RPAR block (ELSE block)?     #ifStat
    | WHILE expr block                          #whileStat
    ;

expr : numExpr
     | boolExpr
     | expr eqOperator expr
     | ID
     ;

numExpr : numExpr operator numExpr
        | LPAR numExpr RPAR
        | NUM
        ;

boolExpr: FALSE | TRUE
        | boolExpr boolOperator boolExpr
        | numExpr compOperator numExpr
        | LPAR boolExpr RPAR
        ;

varDecl : intDecl
        | boolDecl
        | charDecl
        | arrayDecl
        ;

intDecl : INTTYPE ID ASSIGN expr;
boolDecl : BOOLEANTYPE ID ASSIGN expr;
charDecl : CHARTYPE ID ASSIGN APOSTROPHE CHAR APOSTROPHE;
arrayDecl : ARRAYTYPE type ID ASSIGN ARRAYTYPE NUM type;

type : INTTYPE | BOOLEANTYPE | CHARTYPE ;
operator : PLUS | MINUS | MULT | DIV | POW;
boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT;
eqOperator : EQUALS | NOTEQUALS;







