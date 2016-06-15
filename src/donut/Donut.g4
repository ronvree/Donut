grammar Donut;

import DonutVocab;

program : BEGINFILE block ENDFILE;

block : LBRACE stat* RBRACE;

stat: ID ASSIGN expr EOL                        #assStat
    | LPAR expr RPAR IF block (ELSE block)?     #ifStat
    | WHILE LPAR expr RPAR block                #whileStat
    | varDecl                                   #declStat
    | type ID                                   #initStat
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

intDecl : INTTYPE ID (ASSIGN expr)? EOL;
boolDecl : BOOLEANTYPE ID (ASSIGN expr)? EOL ;
charDecl : CHARTYPE ID ASSIGN APOSTROPHE CHAR APOSTROPHE EOL;
arrayDecl : ARRAYTYPE type ID ASSIGN ARRAYTYPE NUM type EOL;

type : INTTYPE | BOOLEANTYPE | CHARTYPE ;
boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT | EQUALS | NOTEQUALS;
prfOperator : MINUS | NOT;






