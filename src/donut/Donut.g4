grammar Donut;

import DonutVocab;

program : BEGINFILE block ENDFILE;

block : LBRACE stat* RBRACE;

stat: ID ASSIGN expr EOL                        #assStat
    | LPAR expr RPAR IF block (ELSE block)?     #ifStat
    | WHILE LPAR expr RPAR block                #whileStat
    | varDecl EOL                               #declStat
    | type ID EOL                               #initStat
    ;

expr: prfOperator expr          #prfExpr
    | expr MULT expr            #multExpr
    | expr PLUS expr            #plusExpr
    | expr MINUS expr           #minusExpr
    | expr DIV expr             #divExpr
    | expr POW expr             #powExpr
    | expr compOperator expr    #compExpr
    | expr boolOperator expr    #boolExpr
    | LPAR expr RPAR            #parExpr
    | ID                        #idExpr
    | NUM                       #numExpr
    | TRUE                      #trueExpr
    | FALSE                     #falseExpr
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
boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT | EQUALS | NOTEQUALS;
prfOperator : MINUS | NOT;






