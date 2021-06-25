/*
 * PostBbq.g4
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
Post-BBQ is the post query ANTLR-grammar for BloomBox queries to combine results
of named queries.
A query reference is defined as ${otherQueryName}
Query references can be combined using UNION, INTERSECT and MINUS.
Braces are supported.
Operators of the language are case-insensitive (e.g. "union" vs. "UNION"), query names
(references) are case-sensitive.
Query names and values can be optionally surrounded by double or single 
quotes, if they contain spaces or would otherwise conflict with the Post-BBQ language.
Should it be necessary to escape any character (e.g. double quote inside double quotes)
then the backslash is the escape character, which can itself be escaped by itself.
Escaping outside single or double quotes is not supported. 
Line breaks are treated as regular whitespace to support multi-line queries.
*/


grammar PostBbq;

fragment A          : ('A'|'a') ;
fragment U          : ('U'|'u') ;
fragment N          : ('N'|'n') ;
fragment I          : ('I'|'i') ;
fragment O          : ('O'|'o') ;
fragment E          : ('E'|'e') ;
fragment R          : ('R'|'r') ;
fragment T          : ('T'|'t') ;
fragment S          : ('S'|'s') ;
fragment C          : ('C'|'c') ;
fragment M          : ('M'|'m') ;
fragment X          : ('X'|'x') ;

fragment SQUOTE     : ('\'') ;

fragment DQUOTE     : ('"') ;

fragment ESCAPE     : ('\\') ;


ESCESC              : ESCAPE ESCAPE ;
ESCSQ               : ESCAPE SQUOTE ;
ESCDQ               : ESCAPE DQUOTE ;


UNION               : U N I O N ;
INTERSECT           : I N T E R S E C T ;
MINUS               : M I N U S ;
MINMAX              : M I N M A X ;


SIMPLE_WORD         : (~[ \t\r\n'"()${},;])+ ;


SQWORD              : SQUOTE (~['\\] | ' ' | ESCESC | ESCSQ)* SQUOTE {setText(getText().substring(1, getText().length()-1).replace("\\'","'").replace("\\\\","\\"));} ;

DQWORD              : DQUOTE (~["\\] | ' ' | ESCESC | ESCDQ)* DQUOTE {setText(getText().substring(1, getText().length()-1).replace("\\\"","\"").replace("\\\\","\\"));} ;


/*
  Parser rules
*/


refStart            : '$' '{' (' ' | '\t' | '\r' | '\n')* ;
refEnd              : (' ' | '\t' | '\r' | '\n')* '}' ;


source              : (SIMPLE_WORD | SQWORD | DQWORD) ;

reference           : refStart source refEnd ;

lowerBound          : SIMPLE_WORD ;

upperBound          : SIMPLE_WORD ;

minMaxExpression    : (' ' | '\t' | '\r' | '\n')* MINMAX (' ' | '\t' | '\r' | '\n')* '(' expression (',' (' ' | '\t' | '\r' | '\n')* expression)* (' ' | '\t' | '\r' | '\n')* ';' (' ' | '\t' | '\r' | '\n')* lowerBound (' ' | '\t' | '\r' | '\n')* ( ';' (' ' | '\t' | '\r' | '\n')* upperBound (' ' | '\t' | '\r' | '\n')*  )? ')' ;

unionExpression     : (' ' | '\t' | '\r' | '\n')* UNION (' ' | '\t' | '\r' | '\n')* (bracedExpression | reference | minMaxExpression) ;

intersectExpression : (' ' | '\t' | '\r' | '\n')* INTERSECT (' ' | '\t' | '\r' | '\n')* (bracedExpression | reference | minMaxExpression) ;

minusExpression     : (' ' | '\t' | '\r' | '\n')* MINUS (' ' | '\t' | '\r' | '\n')* (bracedExpression | reference | minMaxExpression) ;

bracedExpression    : (' ' | '\t' | '\r' | '\n')* '(' expression ')' (' ' | '\t' | '\r' | '\n')* ;

expression          : minMaxExpression | reference | (minMaxExpression | reference | bracedExpression) (unionExpression+ | intersectExpression+ | minusExpression+) ;


query               : expression (' ' | '\t' | '\r' | '\n')* EOF ;


