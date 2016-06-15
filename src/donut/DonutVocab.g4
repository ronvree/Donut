lexer grammar DonutVocab;


NUM : DIGIT+;
ID : LETTER (NUM | LETTER)*;

fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z];

LPAR : '(';
RPAR : ')';
LBRACKET  : '[';
RBRACKET  : ']';
LBRACE : '{';
RBRACE : '}';
APOSTROPHE : '\'';
DOT : '.';

PLUS : '+';
MINUS : '-';
DIV  : '/';
MULT : '*';
POW  : '^';
ASSIGN : '=';

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
TRUE : 'WOOHOO';
FALSE : 'D\'OH';
ARRAYTYPE : 'bunchof';
CHARTYPE : 'symbol';

fragment IF : 'eh?';
fragment ELSE : 'nope';
fragment WHILE : 'whileyoulittle';
fragment FOR : 'for';
fragment PRINT : 'I\'llshowyou';
fragment COMMENT : 'Marge';

fragment EOL : ';';
fragment BEGINFILE : 'Alright brain... it\'s all up to you {';
fragment ENDFILE : '} I hope I didn\'t brain my damage.';

WS : [ \n\r\t] -> skip;

COMMENTLINE : COMMENT LBRACE ~'}'* RBRACE  -> skip;
