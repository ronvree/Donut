grammar Donut;

import DonutVocab;

program : BEGINFILE block ENDFILE;

block : LBRACE stat* RBRACE;
concurrentBlock: LBRACE stat* RBRACE;
lockBlock: LBRACE stat* RBRACE;

stat: ID ASSIGN expr EOL                        #assStat
    | LPAR expr RPAR IF block (ELSE block)?     #ifStat
    | WHILE LPAR expr RPAR block                #whileStat
    | GLOBAL? type ID (ASSIGN expr)? EOL        #declStat
    | PARBEGIN concurrentBlock                  #threadStat
    | LOCK LPAR ID RPAR lockBlock               #lockStat
    ;

expr: prfOperator expr          #prfExpr
    | expr POW expr             #powExpr
    | expr multop expr          #multExpr
    | expr plusop expr          #plusExpr
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

multop: MULT | DIV;
plusop: PLUS | MINUS;

type: INTTYPE | FLOATTYPE | LONGTYPE | BOOLEANTYPE | CHARTYPE | ARRAYTYPE type;

boolOperator : AND | OR | XOR;
compOperator : GE | LE | GT | LT | EQUALS | NOTEQUALS;
prfOperator : MINUS | NOT;






