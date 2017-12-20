This is the parser that statically parses Java bytecode to detect the existence of unnecessary initialization.


First, compile the source code.

	javac MainEntry.java MammalInt.java


Second, disassemble the .classfiles to bytecode

	javap -c MainEntry.class > MainEntry.log

	javap -c MammalInt.class > MammalInt.log


Third, write the input file (ex. input.txt) that specifies

	1)Filenames that the parser will read from.

	2)The initialization functions of relevant classes.


Fourth, run the parser

	java Parse input.txt