lexer grammar DonutVocab;

DIGIT : [0-9];
LETTER: [a-zA-Z];
LPAR : '(';
RPAR : ')';
LBRACKET  : '[';
RBRACKET  : ']';
LBRACE : '{';
RBRACE : '}';

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

INTEGER : 'number';
FLOAT   : 'dotnumber';
BOOLEAN : 'reaction';
TRUE : 'WOOHOO';
FALSE : 'D\'OH';
LONG : 'bignumber';
ARRAY : 'bunchof';
CHAR : 'symbol';

IF : 'if';
ELSE : 'nope';
ELIF : 'elif';
WHILE : 'while';
FOR : 'for';
PRINT : 'I\'llshowyou';
COMMENT : 'Marge';
COLON : ':';

EOL : ';';
BEGINFILE : 'Alright brain... it\'s all up to you {';
ENDFILE : '} I hope I didn\'t brain my damage.';

WS : [ \n\r\t] -> skip;