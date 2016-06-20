grammar Donut;

import DonutVocab;

program : BEGINFILE block ENDFILE;

block : LBRACE stat* RBRACE;

stat: ID ASSIGN expr EOL                        #assStat
    | LPAR expr RPAR IF block (ELSE block)?     #ifStat
    | WHILE LPAR expr RPAR block                #whileStat
    | type ID (ASSIGN expr)? EOL                #declStat
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
    | (ARRAYTYPE NUM)+ type     #arrayExpr
    | CHARACTER                 #charExpr
    ;

type: INTTYPE | FLOATTYPE | LONGTYPE | BOOLEANTYPE | CHARTYPE | ARRAYTYPE type;

boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT | EQUALS | NOTEQUALS;
prfOperator : MINUS | NOT;






