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
    | expr MULT expr            #multExpr
    | expr DIV expr             #divExpr
    | expr PLUS expr            #plusExpr
    | expr MINUS expr           #minusExpr
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






