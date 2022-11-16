import java.io.PrintWriter;

public class Assembler {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("You must provide 2 arguments!");
            System.out.println("Format: java Assembler [src] [tar]");
            return;
        }

        // get the file name of src and tar
        String asmFile = args[0];
        String hackFile = args[1];

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(hackFile);
        } catch (Exception e) {
            System.out.println(e);
        }


        // Two-Pass Assembler
        Parser parser;
        SymbolTable symbolTable = new SymbolTable();

        // First Pass
        parser = new Parser(asmFile);
        int pc = 0;     // record the ROM address
        while (parser.hasMoreCommands()) {
            parser.advance();

            CommandType cmdType = parser.commandType();
            if (cmdType == CommandType.NO_COMMAND) {
                // has no more command, just break loop
                break;
            }
            else if (cmdType == CommandType.L_COMMAND) {
                symbolTable.addEntry(parser.symbol(), pc);
            }
            else {
                // inc pc by 1 only when cmd is a C/A inst
                pc++;
            }
        }

        // Second-Pass
        parser = new Parser(asmFile);
        int n = 16;             // record the next available RAM address
        String code = null;     // binary code of instruction
        while (parser.hasMoreCommands()) {
            parser.advance();

            CommandType cmdType = parser.commandType();
            if (cmdType == CommandType.NO_COMMAND) {
                // has no more command, just break loop
                break;
            }
            else if (cmdType == CommandType.L_COMMAND) {
                // just skip L_COMMAND at second-pass
                continue;
            }
            else if (cmdType == CommandType.A_COMMAND) {
                String symbol = parser.symbol();
                int decimal;    // decimal address of symbol
                String binary;  // binary address of symbol

                // process symbol
                if (symbol.charAt(0) >= '0' && symbol.charAt(0) <= '9') {
                    // symbol is a number (since symbol/variable can't start with number)
                    decimal = Integer.valueOf(symbol);
                }
                else if (symbolTable.contains(symbol)) {
                    // symbol is not a number and is found in the table
                    decimal = symbolTable.getAddress(symbol);
                }
                else {
                    // symbol is not a number ad is not found in the table,
                    // it must be a new variable.
                    decimal = n;
                    symbolTable.addEntry(symbol, n++);
                }

                // convert to binary code
                binary = Integer.toBinaryString(decimal);
                code = "0".repeat(16 - binary.length()) + binary;
            }
            else if (cmdType == CommandType.C_COMMAND) {
                code = "111"
                        + Code.comp(parser.comp())
                        + Code.dest(parser.dest())
                        + Code.jump(parser.jump());
            }

            printWriter.println(code);
        }
        printWriter.close();
    }
}
