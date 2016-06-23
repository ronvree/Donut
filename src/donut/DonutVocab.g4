lexer grammar DonutVocab;

LPAR : '(';
RPAR : ')';
LBRACKET  : '[';
RBRACKET  : ']';
LBRACE : '{';
RBRACE : '}';
APOSTROPHE : '\'';
DOT : '.';
COLON: ':';

PLUS : '+';
MINUS : '-';
DIV  : '/';
MULT : '*';
POW  : '^';
ASSIGN : '=';

NOT : 'not';
EQUALS : 'is';
NOTEQUALS: 'is not';
GE : '>=';
LE : '<=';
GT : '>';
LT : '<';

AND : 'and';
OR  : 'or';
XOR : 'xor';

INTTYPE : 'number';
FLOATTYPE   : 'dotnumber';
LONGTYPE : 'bignumber';
BOOLEANTYPE : 'reaction';
FALSE : 'D\'OH';
TRUE : 'WOOHOO';
ARRAYTYPE : 'bunchof';
CHARTYPE : 'symbol';

PARBEGIN: 'Duff';
GLOBAL: 'global';

ELSE : 'nope';
WHILE : 'whileyoulittle';
FOR : 'for';
PRINT : 'I\'llshowyou';
fragment COMMENT : 'Marge';

EOL : ';';
BEGINFILE : 'Alright brain... it\'s all up to you';
ENDFILE : 'I hope I didn\'t brain my damage.';


NUM : DIGIT+;
BOOLEANVALUE : FALSE | TRUE;
IF : 'eh?';
COMMENTLINE : COMMENT COLON .*? EOL  -> skip;

ID : LETTER (NUM | LETTER)*;

fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z];


WS : [ \n\r\t] -> skip;

CHARACTER : APOSTROPHE .? APOSTROPHE;