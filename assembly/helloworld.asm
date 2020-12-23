section .data
  welcome_message DB "Enter 'Hello world!'", 0x0A
  welcome_message_length EQU $-welcome_message
  expected_input DB "Hello world!", 0x0A
  expected_input_length EQU $-expected_input
  good DB "Good!", 0x0A
  good_length EQU $-good
  bad DB "Bad!", 0x0A
  bad_length EQU $-bad

section .bss
  user_input RESB 0x0C

section .text
  global _start

_start:
  MOV EAX, 0x04
  MOV EBX, 0x01
  MOV ECX, welcome_message
  MOV EDX, welcome_message_length
  INT 0x80

  MOV EAX, 0x03
  MOV EBX, 0x02
  MOV ECX, user_input
  MOV EDX, expected_input_length
  INT 0x80
 
  MOV EBX, expected_input_length
  CMP EAX, EBX
  JNE print_bad

  LEA ESI, [expected_input]
  LEA EDI, [ECX]
  MOV ECX, expected_input_length
  REP CMPSB
  JNE print_bad
  JE print_good

end:
  MOV EAX, 0x01
  MOV EBX, 0x00
  INT 0x80

print_bad:
  MOV EAX, 0x04
  MOV EBX, 0x01
  MOV ECX, bad
  MOV EDX, bad_length
  INT 0x80
  JMP end

print_good:
  MOV EAX, 0x04
  MOV EBX, 0x01
  MOV ECX, good
  MOV EDX, good_length
  INT 0x80
  JMP end