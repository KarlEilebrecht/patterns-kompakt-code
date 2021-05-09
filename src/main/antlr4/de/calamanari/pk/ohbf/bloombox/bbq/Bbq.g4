/*
 * Bbq.g4
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
BBQ is the basic query ANTLR-grammar for BloomBox queries.
It allows basic comparison (infix-notation) as well as IN (val1, val2, ...) resp. NOT IN. 
Expressions can be combined using AND/OR.
Braces are supported.
Operators of the language are case-insensitive (e.g. "and" vs. "AND"), argument names
and values are case-sensitive.
Argument names and values can be optionally surrounded by double or single 
quotes, if they contain spaces or would otherwise conflict with the BBQ language.
Should it be necessary to escape any character (e.g. double quote inside double quotes)
then the backslash is the escape character, which can itself be escaped by itself.
Escaping outside single or double quotes is not supported. 
Line breaks are treated as regular whitespace to support multi-line queries.
*/

grammar Bbq;


fragment A          : ('A'|'a') ;
fragment N          : ('N'|'n') ;
fragment D          : ('D'|'d') ;
fragment O          : ('O'|'o') ;
fragment R          : ('R'|'r') ;
fragment I          : ('I'|'i') ;
fragment T          : ('T'|'t') ;

fragment SQUOTE     : ('\'') ;

fragment DQUOTE     : ('"') ;

fragment ESCAPE     : ('\\') ;


ESCESC              : ESCAPE ESCAPE ;
ESCSQ               : ESCAPE SQUOTE ;
ESCDQ               : ESCAPE DQUOTE ;

AND                 : A N D ;

OR                  : O R ;

NOT                 : N O T ;

IN                  : I N ;





SIMPLE_WORD         : (~[ \t\r\n'"()!=,])+ ;


SQWORD              : SQUOTE (~['\\] | ' ' | ESCESC | ESCSQ)* SQUOTE {setText(getText().substring(1, getText().length()-1).replace("\\'","'").replace("\\\\","\\"));};

DQWORD              : DQUOTE (~["\\] | ' ' | ESCESC | ESCDQ)* DQUOTE {setText(getText().substring(1, getText().length()-1).replace("\\\"","\"").replace("\\\\","\\"));};


/*
  Parser rules
*/


argName             : (SIMPLE_WORD | SQWORD | DQWORD);

argValue            : (SIMPLE_WORD | SQWORD | DQWORD) ;

inValue             : (' ' | '\t' | '\r' | '\n')* argValue (' ' | '\t' | '\r' | '\n')* ;
nextValue           : ',' inValue ;


cmpEquals           : argName (' ' | '\t' | '\r' | '\n')* '=' (' ' | '\t' | '\r' | '\n')* argValue ;

cmpNotEquals        : argName (' ' | '\t' | '\r' | '\n')* '!' '=' (' ' | '\t' | '\r' | '\n')* argValue ;

cmpIn               : argName (' ' | '\t' | '\r' | '\n')+ IN (' ' | '\t' | '\r' | '\n')* '(' inValue (nextValue)* ')' ;

cmpNotIn            : argName (' ' | '\t' | '\r' | '\n')+ NOT (' ' | '\t' | '\r' | '\n')+ IN (' ' | '\t' | '\r' | '\n')* '(' inValue (nextValue)* ')' ;

expressionDetails   : (' ' | '\t' | '\r' | '\n')* (cmpEquals | cmpNotEquals | cmpIn | cmpNotIn) ;

orExpression        : (' ' | '\t' | '\r' | '\n')* OR (' ' | '\t' | '\r' | '\n')* (fullBBQ | expressionDetails) ;

andExpression       : (' ' | '\t' | '\r' | '\n')* AND (' ' | '\t' | '\r' | '\n')* (fullBBQ | expressionDetails) ;

expression          : expressionDetails (andExpression | orExpression)* ;

bracedExpression    : (' ' | '\t' | '\r' | '\n')* '(' fullBBQ ')' (' ' | '\t' | '\r' | '\n')* ;

bbqDetails          : (expression | bracedExpression) ;

andBBQ              : (' ' | '\t' | '\r' | '\n')* AND bbqDetails ;

orBBQ               : (' ' | '\t' | '\r' | '\n')* OR bbqDetails ;

fullBBQ             : bbqDetails (andBBQ | orBBQ)* ;


query               : fullBBQ (' ' | '\t' | '\r' | '\n')+ EOF ;


