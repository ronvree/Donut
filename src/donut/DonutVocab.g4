lexer grammar DonutVocab;

DIGIT19 : [1-9];
DIGIT09 : '0' | DIGIT19;

LETTER: [a-zA-Z];
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

IF : 'eh?';
ELSE : 'nope';
WHILE : 'whileyoulittle';
FOR : 'for';
PRINT : 'I\'llshowyou';
COMMENT : 'Marge';

EOL : ';';
BEGINFILE : 'Alright brain... it\'s all up to you {';
ENDFILE : '} I hope I didn\'t brain my damage.';

WS : [ \n\r\t] -> skip;

CHAR : . ;