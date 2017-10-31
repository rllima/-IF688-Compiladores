grammar imp;

goal: mainClass ( classDeclaration )* EOF;

mainClass : 'class' identifier '{' 'public' 'static' 'void' 'main' '(' 'String' '[' ']' identifier ')' '{' statement '}' '}';

classDeclaration: classDeclSimple | classDeclExtends;

classDeclSimple : 'class' identifier '{' ( varDeclaration )* ( methodDeclaration )* '}';
classDeclExtends : 'class' identifier 'extends' identifier '{' ( varDeclaration )* ( methodDeclaration )* '}';

varDeclaration: type identifier ';';

methodDeclaration: 'public' type identifier '(' ( type identifier ( ',' type identifier )* )? ')' '{' ( varDeclaration )* ( statement )* 'return' expression ';' '}';

type: 'int' '[' ']'
	| 'boolean'
	| 'int'
	| identifier;
	
statement: '{' ( statement )* '}'
	| 'if' '(' expression ')' statement 'else' statement
	| 'while' '(' expression ')' statement
	| 'System.out.println' '(' expression ')' ';'
	| identifier '=' expression ';'
	| identifier '[' expression ']' '=' expression ';';
	
expression: expression ( '&&' | '<' | '+' | '-' | '*' ) expression
	| expression '[' expression ']'
	| expression '.' 'length'
	| expression '.' identifier '(' ( expression ( ',' expression )* )? ')'
	| INTEGER_LITERAL
	| 'true'
	| 'false'
	| identifier
	| 'this'
	| 'new' 'int' '[' expression ']'
	| 'new' identifier '(' ')'
	| '!' expression
	| '(' expression ')';
	
	
identifier: IDENTIFIER;


COMMENT:   '/*' .*? '*/' -> skip;
LINE_COMMENT:   '//' ~[\r\n]* -> skip;

INTEGER_LITERAL: [1-9][0-9]* | '0';
IDENTIFIER: [_a-zA-Z][a-zA-Z0-9_]*;
WS:         [ \t\r\n]+ -> skip;

