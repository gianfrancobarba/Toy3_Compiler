package toy3;
import java_cup.runtime.*;

%%

/* Opzioni e dichiarazioni per JFlex */
%class Lexer
%unicode
%cupsym sym
%cup
%public
%line
%column

%state COMMENT_LINE, COMMENT_BLOCK, STRING, CHAR

/* Espressioni regolari */

// Terminatori di riga
LineTerminator = \r|\n|\r\n // a capo
Whitespace = {LineTerminator} | [ \t\f] // spazi, tabulazioni, a capo

// Numeri
INT_CONST    = \d+ // interi positivi o negativi
DOUBLE_CONST = (\d.\d+|\d+.\d)([eE][+-]?\d+)? // numeri in virgola mobile con o senza esponente (es. 1.0, 1.0e-1)

// Escape
ESCAPE_SEQUENCE = \\[bfnrt'\\] // Escape validi

// Stringhe
STRING_START = \" // Riconosce solo l'inizio stringa
CHAR_START   = \' // Riconosce solo l'inizio carattere
STRING_CONST = \"([^\\\"]|\\.)*\"? // stringhe costanti es. "ciao" o "ciao\""

// Caratteri
CHAR_CONST   = '([^\\']|\\.)' // caratteri costanti es. 'a' o '\n'

// Identificatori
ID = [:jletter:][:jletterdigit:]* // lettera seguita da lettere o numeri

%{
        StringBuffer string = new StringBuffer();
        private Symbol symbol(int type) {
            return new Symbol(type, yyline, yycolumn);
        }
        private Symbol symbol(int type, Object value) {
            return new Symbol(type, yyline, yycolumn, value);
        }

        private Symbol installID(String lessema){
                Symbol token;

                token=symbol(sym.ID,lessema);
                return token;
            }
%}

%%

<YYINITIAL> {

/* Token riconosciuti */

    "program"          { return symbol(sym.PROGRAM); }
    "begin"            { return symbol(sym.BEGIN); }
    "end"              { return symbol(sym.END); }
    "int"              { return symbol(sym.INT); }
    "bool"             { return symbol(sym.BOOL); }
    "double"           { return symbol(sym.DOUBLE); }
    "string"           { return symbol(sym.STRING); }
    "char"             { return symbol(sym.CHAR); }
    "true"             { return symbol(sym.TRUE, true); }
    "false"            { return symbol(sym.FALSE, false); }
    "def"              { return symbol(sym.DEF); }
    "if"               { return symbol(sym.IF); }
    "then"             { return symbol(sym.THEN); }
    "else"             { return symbol(sym.ELSE); }
    "while"            { return symbol(sym.WHILE); }
    "do"               { return symbol(sym.DO); }
    "return"           { return symbol(sym.RETURN); }
    "not"              { return symbol(sym.NOT); }
    "and"              { return symbol(sym.AND); }
    "or"               { return symbol(sym.OR); }
    "ref"              { return symbol(sym.REF); }

    /* Simboli */

    ";"                { return symbol(sym.SEMI); }
    ":"                { return symbol(sym.COLON); }
    ","                { return symbol(sym.COMMA); }
    "("                { return symbol(sym.LPAR); }
    ")"                { return symbol(sym.RPAR); }
    "{"                { return symbol(sym.LBRAC); }
    "}"                { return symbol(sym.RBRAC); }
    "<<"               { return symbol(sym.IN); }
    ">>"               { return symbol(sym.OUT); }
    "!>>"              { return symbol(sym.OUTNL); }
    "+"                { return symbol(sym.PLUS);}
    "-"                { return symbol(sym.MINUS); }
    "*"                { return symbol(sym.TIMES); }
    "/"                { return symbol(sym.DIV); }
    ">"                { return symbol(sym.GT); }
    ">="               { return symbol(sym.GE); }
    "<"                { return symbol(sym.LT); }
    "<="               { return symbol(sym.LE); }
    "=="               { return symbol(sym.EQ); }
    "<>"               { return symbol(sym.NE); }
    ":="               { return symbol(sym.ASSIGN); }
    "="                { return symbol(sym.ASSIGNDECL); }
    "|"                { return symbol(sym.PIPE); }

    // Commenti
    "//"              { yybegin(COMMENT_LINE); }
    "/*"              { yybegin(COMMENT_BLOCK); }

    {Whitespace}   { /* ignore */ }
    {INT_CONST}    { return symbol(sym.INT_CONST, Integer.parseInt(yytext())); }
    {DOUBLE_CONST} { try {
                        // Parsing del valore
                        double value = Double.parseDouble(yytext());

                        // Controllo per overflow
                        if (!Double.isFinite(value)) {
                            throw new IllegalArgumentException("Numero double fuori dai limiti consentiti: " + yytext());
                        }

                        // Restituisce il valore corretto
                        return symbol(sym.DOUBLE_CONST, value);

                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Formato di double non valido: " + yytext());
                    }}
    {STRING_START} { string.setLength(0);      // Pulisce il buffer per una nuova stringa
                     yybegin(STRING); }
    {CHAR_START}   { string.setLength(0);      // Pulisce il buffer per un nuovo carattere
                        yybegin(CHAR); }
    {ID}           { return installID(yytext()); }
}

// stato dei commenti dove dai errore se il commento non viene chiuso prima di incontrare eof
<COMMENT_BLOCK> {
    "*/"         { yybegin(YYINITIAL); }
    [^*]+        { /* ignore */ }
    \* [^/]      { /* ignore */ }
    <<EOF>>      { throw new IllegalArgumentException("Commento non chiuso"); }
}

// stato dei commenti dove dai errore se il commento non viene chiuso prima di incontrare eof
<COMMENT_LINE> {
    {LineTerminator}     { yybegin(YYINITIAL); }
    [^\r\n]+             { /* ignore */ }
}

//stato delle stringhe dove dai errore se la stringa non viene chiusa prima di incontrare eof
<STRING> {

    \" { // Chiusura corretta
        yybegin(YYINITIAL);
        return symbol(toy3.sym.STRING_CONST, string.toString());
    }

    [^\n\r\"\\]+ { string.append(yytext()); } // Aggiunge testo normale al buffer

    {ESCAPE_SEQUENCE} { // Caratteri di escape validi
        switch(yytext().charAt(1)) {
            case 'b': string.append('\b'); break;
            case 'f': string.append('\f'); break;
            case 'n': string.append('\n'); break;
            case 'r': string.append('\r'); break;
            case 't': string.append('\t'); break;
            case '\\': string.append('\\'); break;
            case '\"': string.append('\"'); break;
            case '\'': string.append('\''); break;
            default: throw new IllegalArgumentException("Carattere di escape non valido: " + yytext());
        }
    }

    \\[^\n\r] { // Escape non valido
        throw new IllegalArgumentException("Carattere di escape non valido: " + yytext());
    }

    {LineTerminator} { // ERRORE su newline non escapato
        throw new IllegalArgumentException("Stringa non chiusa su newline");
    }

    <<EOF>> { // ERRORE su EOF
        throw new IllegalArgumentException("Stringa non chiusa");
    }
}

<CHAR> {

    {ESCAPE_SEQUENCE} { // Escape validi
        switch(yytext().charAt(1)) {
            case 'b': string.append('\b'); break;
            case 'f': string.append('\f'); break;
            case 'n': string.append('\n'); break;
            case 'r': string.append('\r'); break;
            case 't': string.append('\t'); break;
            case '\\': string.append('\\'); break;
            case '\'': string.append('\''); break;
        }
    }

    [^\\'] { // Singolo carattere normale
        if (string.length() > 0) { // Controlla se è già stato letto un carattere
            throw new IllegalArgumentException("Troppi caratteri nella costante: " + yytext());
        }
        string.append(yytext()); // Aggiunge il carattere
    }

    \' { // Chiusura della costante carattere
        if (string.length() != 1) { // Controlla che ci sia un solo carattere
            throw new IllegalArgumentException("Costante carattere non valida: '" + string + "'");
        }
        yybegin(YYINITIAL); // Torna allo stato iniziale
        return symbol(sym.CHAR_CONST, string.charAt(0)); // Restituisce il carattere
    }

    {LineTerminator} { // Errore su newline
        throw new IllegalArgumentException("Costante carattere non chiusa su newline");
    }

    <<EOF>> { // Errore su EOF
        throw new IllegalArgumentException("Costante carattere non chiusa");
    }

}

<<EOF>> { return symbol(sym.EOF); }
[^] { throw new IllegalArgumentException("Carattere non riconosciuto: " + yytext()); }