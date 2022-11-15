import java.io.PrintWriter;

public class Assembler {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("You must prvide 2 arguments!");
            return;
        }

        String asmFile = args[0];
        String hackFile = args[1];
        String code = null;

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(hackFile);
        } catch (Exception e) {
            System.out.println(e);
        }

        // create a parser with given file name
        Parser parser = new Parser(asmFile);
        while (parser.hasMoreCommands()) {
            parser.advance();

            CommandType cmdType = parser.commandType();
            if (cmdType == CommandType.NO_COMMAND) {
                // has no more command, just return
                break;
            }
            else if (cmdType == CommandType.A_COMMAND) {
                int decimal = Integer.valueOf(parser.symbol());
                String symbol = Integer.toBinaryString(decimal);
                code = "0"
                        + new String("0").repeat(15 - symbol.length())
                        + symbol;
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
