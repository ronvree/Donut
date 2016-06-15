grammar Donut;

import DonutVocab;


varDecl : intDecl | boolDecl | charDecl | arrayDecl ;

intDecl : INTTYPE variable ASSIGN integer;
boolDecl : BOOLEANTYPE variable ASSIGN booleanValue;
charDecl : CHARTYPE variable ASSIGN APOSTROPHE CHAR APOSTROPHE;
arrayDecl : ARRAYTYPE type variable ASSIGN ARRAYTYPE integer type;


integer : DIGIT19 DIGIT09*;

variable : LETTER (LETTER | DIGIT09)* ;
booleanValue : TRUE | FALSE;

type : INTTYPE | BOOLEANTYPE | CHARTYPE ;