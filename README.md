# Toy3 Compiler

**Toy3** è un linguaggio di programmazione didattico progettato per esplorare le fasi principali della costruzione di un compilatore. Questo progetto implementa un compilatore completo per Toy3 in **Java**, comprendente:

- ✅ Analizzatore lessicale (lexer) con **JFlex**
- ✅ Analizzatore sintattico (parser) con **JavaCup**
- ✅ Costruzione dell’**AST**
- ✅ Gestione dello **scoping**
- ✅ **Type Checking** semantico
- ✅ Generazione di **codice intermedio in linguaggio C**
- ✅ Architettura modulare basata su **Visitor Pattern**

---

## 📌 Caratteristiche principali del linguaggio Toy3

Toy3 è un linguaggio **imperativo**, **statisticamente tipizzato**, con le seguenti caratteristiche:

- Dichiarazioni di **variabili** e **funzioni**
- Tipi primitivi: `int`, `bool`, `double`, `string`, `char`
- Costanti: booleane (`true`, `false`) e letterali (`"stringhe"`, `'c'`, `42`, `3.14`)
- Controllo di flusso: `if`, `else`, `while`
- Input/output: `Vars <<`, `Exprs >>`, `Exprs !>>`
- Funzioni con **parametri per valore** o **per riferimento** (`ref`)
- **Assegnamenti multipli** (es. `a | b := x | y`)
- Espressioni **aritmetiche**, **logiche** e **relazionali**

---

## ⚙️ Architettura del compilatore

### 1. 🔍 Analizzatore lessicale – JFlex

- Riconoscimento dei token del linguaggio
- Gestione di commenti:
  - **Commenti di linea:** `//`
  - **Commenti multilinea:** `/* ... */`
- Rilevamento e segnalazione di errori:
  - `Stringa costante non completata`
  - `Commento non chiuso`

### 2. 🧩 Analizzatore sintattico – JavaCup

- Parsing **LALR(1)** della grammatica di Toy3
- Risoluzione dei conflitti con precedenze e associatività
- Costruzione automatica dell’**AST**

### 3. 🌳 AST e Visitor Pattern

- Struttura ad albero semantica del programma
- Implementazione dei visitor per:
  - **Scoping**
  - **Type checking**
  - **Generazione di codice intermedio C**

---

### 📐 Definizione formale di Toy3

<details>
<summary><strong>Espandi per visualizzare la grammatica BNF</strong></summary>

```bnf
Programma ::= PROGRAM Decls BEGIN VarDecls Statements END

Decls ::= VarDecl Decls 
        | DefDecl Decls
        | ε

VarDecls ::= VarDecls VarDecl
           | ε

VarDecl ::= VarsOptInit COLON TypeOrConstant SEMI

VarsOptInit ::= ID PIPE VarsOptInit
              | ID ASSIGNDECL Expr PIPE VarsOptInit
              | ID
              | ID ASSIGNDECL Expr

TypeOrConstant ::= Type 
                 | Constant

Type ::= INT  
       | BOOL  
       | DOUBLE  
       | STRING  
       | CHAR 

Constant ::= TRUE
           | FALSE
           | INT_CONST
           | DOUBLE_CONST
           | CHAR_CONST
           | STRING_CONST

DefDecl ::= DEF ID LPAR ParDecls RPAR OptType Body
          | DEF ID LPAR RPAR OptType Body

ParDecls ::= ParDecl SEMI ParDecls
           | ParDecl

ParDecl ::= PVars COLON Type

PVars ::= PVar COMMA PVars
        | PVar

PVar ::= ID 
       | REF ID

OptType ::= COLON Type
          | ε

Body ::= LBRAC VarDecls Statements RBRAC

Statements ::= Stat Statements
             | ε

Stat ::= Vars IN SEMI
       | Exprs OUT SEMI
       | Exprs OUTNL SEMI
       | Vars ASSIGN Exprs SEMI       // il controllo semantico sull’assegnamento è fatto nell’analisi semantica
       | FunCall SEMI
       | IF LPAR Expr RPAR THEN Body ELSE Body 
       | IF LPAR Expr RPAR THEN Body 
       | WHILE LPAR Expr RPAR DO Body 
       | RETURN Expr SEMI

Vars ::= ID PIPE Vars
       | ID

Exprs ::= Expr COMMA Exprs  
        | Expr

FunCall ::= ID LPAR Exprs RPAR 	
          | ID LPAR RPAR 

Expr ::= Expr ArithOp Expr
       | Expr BoolOp Expr
       | Expr RelOp Expr
       | LPAR Expr RPAR
       | MINUS Expr
       | NOT Expr
       | ID
       | FunCall
       | Constant

ArithOp ::= PLUS  
          | MINUS  
          | TIMES  
          | DIV 

BoolOp ::= AND 
         | OR  

RelOp ::= GT 
        | GE 
        | LT 
        | LE 
        | EQ 
        | NE
```
</details>

<details>
<summary><strong>Espandi per visualizzare la Specifica Lessicale (JFlex)</strong></summary>

```
  Commenti:
  // commento di linea
  /* commento di blocco */

Parole chiave:
  PROGRAM     → program
  BEGIN       → begin
  END         → end
  DEF         → def
  INT         → int
  BOOL        → bool
  DOUBLE      → double
  STRING      → string
  CHAR        → char
  TRUE        → true
  FALSE       → false
  RETURN      → return
  IF          → if
  THEN        → then
  ELSE        → else
  WHILE       → while
  DO          → do
  NOT         → not
  AND         → and
  OR          → or
  REF         → ref
  IN          → <<
  OUT         → >>
  OUTNL       → !>>

Simboli:
  SEMI        → ;
  COLON       → :
  COMMA       → ,
  LPAR        → (
  RPAR        → )
  LBRAC       → {
  RBRAC       → }
  ASSIGN      → :=
  ASSIGNDECL  → =
  PIPE        → |

Operatori aritmetici:
  PLUS        → +
  MINUS       → -
  TIMES       → *
  DIV         → /

Operatori relazionali:
  GT          → >
  GE          → >=
  LT          → <
  LE          → <=
  EQ          → ==
  NE          → <>

Token:
  ID              → jletter (jletter | jdigit)*
  INT_CONST       → pattern per interi
  DOUBLE_CONST    → pattern per double
  STRING_CONST    → pattern per stringhe
  CHAR_CONST      → pattern per caratteri

Precedenze e associatività
Dalla più alta alla più bassa:

() 
* /
+ -               (associatività sinistra se binaria, destra se unaria)
== <> < <= > >=
not               (associatività destra)
and
or
```
</details>

### ✅ Esempio di codice Toy3

```toy3
program
def somma(x: int; y: int): int {
  return x + y;
}

begin
  a = 5 | b = 10 : int;
  c := somma(a | b);
  c >>;
end
```

### 📄 Codice intermedio generato (linguaggio C)

```c
int somma(int x, int y) {
  return x + y;
}

int main() {
  int a = 5, b = 10, c;
  c = somma(a, b);
  printf("%d", c);
  return 0;
}
```

---

## 🎯 Obiettivi

Toy3 Compiler è stato realizzato come parte di un corso universitario avanzato sui compilatori. L’obiettivo è fornire un’implementazione completa e modulare delle principali fasi di un compilatore reale:

- Riconoscimento e gestione dei token
- Parsing di strutture complesse e ambigue
- Analisi semantica rigorosa
- Generazione di codice intermedio C

---

## 🛠️ Tecnologie utilizzate

- **Java 17**
- **JFlex 1.9+**
- **JavaCup 11k**
- **Design Pattern:** Visitor

---

## 📚 Approfondimenti

Toy3 è progettato per essere facilmente estensibile: è possibile introdurre nuovi tipi, costrutti e regole semantiche intervenendo direttamente sull’AST e sui visitor.
