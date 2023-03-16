; Written for https://schweigi.github.io/assembler-simulator/
; Paste into textbox and press "Run"
;   Optional: Turn on register addressing for "A" and "B".
;
; What does this do?
;   Prints 'T' if the given variable is a palindrome, 'F' otherwise

; C#: int a;
; C#: int b;
; C#: int c;
; C#: int d;

  JMP start

; Define variable named "foo"
;
;   C#: char[] foo = "able was I ere I saw elba";
foo:
  DB "able was I ere I saw elba"
  DB 0 ; Denotes the end of the string

; Define entrypoint
;
;   C#: void main() {
start:
  ; C# a = foo;
  MOV A, foo

  ; C#: b = findEnd();
  CALL findEnd ; Find end of string
  MOV B, A

  ; C#: a = foo;
  MOV A, foo

  ; C#: c = computePalindrome();
  CALL computePalindrome
  MOV A, C

  ; C#: printSuccessValue();
  CALL printSuccessValue

  ; C#: return;
  HLT
; }

; Define function "findEnd"
;
;   C#: int findEnd(int a) {
findEnd:
  ; param: A: start of variable
  ; return: A: end of variable

  PUSH B
  PUSH C
  MOV B, 0

.findEndLoop:
  MOV C, [A]
  INC A

  CMP C, B
  JNZ .findEndLoop

  DEC A
  POP C
  POP B
  RET
; }

; Define function "computePalindrome"
;
;  C#: int computePalindrome(int a, int b) {
computePalindrome:
  ; param A: start of variable
  ; param B: end of variable
  ; return C: zero if false

  PUSH A
  PUSH B
  DEC B

; C#: do {
.computePalindromeLoop:
  CMP A, B ; Check to see whether we have breached the midpoint
  JAE .computePalindromeYes

  MOV C, [A] ; Copy values
  MOV D, [B]
  INC A
  DEC B

  CMP C, D ; Compare values
  JZ .computePalindromeLoop
  ; } while (!(c == d));

  MOV C, 0
  JMP .computePalindromeEnd

.computePalindromeYes:
  MOV C, 1
  ; fall through

.computePalindromeEnd:
  POP B
  POP A
  RET
; }

; Define function "printSuccessValue"
;
;   C#: void printSuccessValue(int a) {
printSuccessValue:
  ; param A: success value (0 if false)

  PUSH A

  CMP A, 0 ; Determine whether A is zero
  JZ .printSuccessValueFailure

  ; C#: else {
  MOV [0xE8], 'T' ; If not zero, print 'T'
  JMP .printSuccessValueEnd
  ; }

; C#: if (!(a == 0)) {
.printSuccessValueFailure:
  MOV [0xE8], 'F' ; Otherwise, print 'F'
  ; Fall through
; }

.printSuccessValueEnd:
  POP A
  RET
; }
