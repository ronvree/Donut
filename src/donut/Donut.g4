grammar Donut;

import DonutVocab;

program : BEGINFILE block ENDFILE;

block : LBRACE stat* RBRACE;

stat: ID ASSIGN expr EOL                        #assStat
    | LPAR expr RPAR IF block (ELSE block)?     #ifStat
    | WHILE LPAR expr RPAR block                #whileStat
    | type ID EOL                                #initStat
    | varDecl EOL                               #declStat
    ;

expr: prfOperator expr        #prfExpr
    | <assoc=right> expr MULT expr  #multExpr
    | <assoc=right> expr PLUS expr  #plusExpr
    | <assoc=right> expr MINUS expr  #plusExpr
    | <assoc=right> expr compOperator expr  #compExpr
    | <assoc=right> expr boolOperator expr  #boolExpr
    | LPAR expr RPAR    #parExpr
    | ID                #idExpr
    | NUM               #numExpr
    | TRUE              #trueExpr
    | FALSE             #falseExpr
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
prfOperator : MINUS | ;






