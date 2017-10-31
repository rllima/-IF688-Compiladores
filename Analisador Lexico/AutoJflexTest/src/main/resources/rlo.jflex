%%

/* Não altere as configurações a seguir */

%line
%column
%unicode
//%debug
%public
%standalone
%class Minijava
%eofclose

/* Insira as regras léxicas abaixo */

letter               = [A-Za-z]
digit                = [0-9]
integer              = [1-9]{digit}*|0
alphanumeric         = {letter}|{digit}
underline            = [_]
identifier           = ({letter}|{underline})({alphanumeric})*
LineTerminator       = \r|\n|\r\n
InputCharacter       = [^\r\n]
whiteSpace           = {LineTerminator} | [ \t\f]
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+"/"
CommentContent       = ( [^*] | \*+ [^/*] )*
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

%%

boolean               { System.out.println("boolean"); }
class                 { System.out.println("class"); }
public                { System.out.println("public"); }
extends               { System.out.println("extends"); }
static                { System.out.println("static"); }
void                  { System.out.println("static"); }
main                  { System.out.println("main"); }
String                { System.out.println("String"); }
int                   { System.out.println("int"); }
while                 { System.out.println("while"); }
if                    { System.out.println("if"); }
else                  { System.out.println("else"); }
return                { System.out.println("return"); }
length                { System.out.println("length"); }
true                  { System.out.println("true"); }
false                 { System.out.println("false"); }
this                  { System.out.println("this"); }  
new                   { System.out.println("new"); }
System.out.println    { System.out.println("System.out.println"); }
"&&"                  { System.out.println("Token &&"); }
"<"                   { System.out.println("Token <"); }
">"                   { System.out.println("Token >"); }
"=="                  { System.out.println("Token =="); }
"!="                  { System.out.println("Token !="); }
"+"                   { System.out.println("Token +"); }
"-"                   { System.out.println("Token -"); }
"*"                   { System.out.println("Token *"); }
"!"                   { System.out.println("Token !"); }
";"                   { System.out.println("Token ;"); }
"."                   { System.out.println("Token ."); }
","                   { System.out.println("Token ,"); }
"="                   { System.out.println("Token ="); }
"("                   { System.out.println("Token ("); }
")"                   { System.out.println("Token )"); }
"{"                   { System.out.println("Token {"); }
"}"                   { System.out.println("Token }"); }
"["                   { System.out.println("Token ["); }
"]"                   { System.out.println("Token ]"); }
{identifier}          { System.out.println("Token id"+ yytext()); }
{integer}             { System.out.println("Token integer"); }
{whiteSpace}         {}
{Comment}            {}
  
/* Insira as regras léxicas no espaço acima */     
     
. { throw new RuntimeException("Caractere ilegal! '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }