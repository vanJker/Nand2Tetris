// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
(LOOP)
    @256
    D=A         // D = 256
    @R0
    M=D         // R0 = 256
    @SCREEN
    D=A
    @R2
    M=D         // R2 = SCREEN
    @KBD
    D=M         // D = KBD
    @CLEAR
    D;JEQ       // If KBD == 0, goto CLEAR
(BLACK)
    @R0
    MD=M-1      // D = --R0
    @LOOP
    D;JLT       // If R0 < 0, goto LOOP
    @32
    D=A         // D = 32
    @R1
    M=D         // R1 = 32
(BLOOP)
    @R1
    MD=M-1      // D = --R1
    @BLACK
    D;JLT       // If R1 < 0, goto BLACK
    @R2
    D=M
    M=M+1       // advance address of screen
    A=D         // update A's content
    M=-1        // -1 for 16-bit 1
    @BLOOP
    0;JMP
(CLEAR)
    @R0
    MD=M-1      // D = --R0
    @LOOP
    D;JLT       // If R0 < 0, goto LOOP
    @32
    D=A         // D = 32
    @R1
    M=D         // R1 = 32
(CLOOP)
    @R1
    MD=M-1      // D = --R1
    @CLEAR
    D;JLT       // If R1 < 0, goto CLEAR
    @R2
    D=M
    M=M+1       // advance address of screen
    A=D         // update A's content
    M=0         // 0 for 16-bit 0
    @CLOOP
    0;JMP