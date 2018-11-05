; Written for https://schweigi.github.io/assembler-simulator/
; Prints 'T' if the given variable is a palindrome, 'F' otherwise

  JMP start

var:
  DB "able was I ere I saw elba"
  DB 0

start:
  MOV A, var
  CALL findEnd ; Find end of string
  MOV B, A
  MOV A, var
  CALL computePalindrome
  MOV A, C
  CALL printSuccessValue
  HLT

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

computePalindrome:
  ; param A: start of variable
  ; param B: end of variable
  ; return C: zero if false

  PUSH A
  PUSH B
  DEC B

.computePalindromeLoop:
  CMP A, B ; Check to see whether we have breached the midpoint
  JAE .computePalindromeYes

  MOV C, [A] ; Copy values
  MOV D, [B]
  INC A
  DEC B

  CMP C, D ; Compare values
  JZ .computePalindromeLoop

  MOV C, 0
  JMP .computePalindromeEnd

.computePalindromeYes:
  MOV C, 1
  ; fall through

.computePalindromeEnd:
  POP B
  POP A
  RET

printSuccessValue:
  ; param A: success value (0 if false)

  PUSH A

  CMP A, 0 ; Determine whether A is zero
  JZ .printSuccessValueFailure

  MOV [0xE8], 'T' ; If not zero, print 'T'
  JMP .printSuccessValueEnd

.printSuccessValueFailure:
  MOV [0xE8], 'F' ; Otherwise, print 'F'
  ; Fall through

.printSuccessValueEnd:
  POP A
  RET
