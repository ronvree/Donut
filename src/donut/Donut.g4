lexer grammar Donut;

import DonutVocab;




fragment comment : COMMENT LBRACE commentText RBRACE;
fragment commentText : (~RBRACE)*;

varDecl : intDecl | boolDecl | charDecl | arrayDecl;

intDecl : INTTYPE variable ASSIGN number;
boolDecl : BOOLEANTYPE variable ASSIGN booleanValue;
charDecl : CHARTYPE variable ASSIGN APOSTROPHE CHAR APOSTOPHE;
arrayDecl : ARRAYTYPE TYPE variable ASSIGN ARRAYTYPE integer TYPE;

fragment variable : LETTER numberOrLetter* ;
fragment numberOrLetter : LETTER | DIGIT09 ;
integer : DIGIT19 DIGIT09*;

fragment booleanValue : TRUE | FALSE;

fragment TYPE : INTTYPE | BOOLEANTYPE | CHARTYPE ;