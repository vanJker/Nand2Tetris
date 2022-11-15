import java.io.File;
import java.util.Scanner;

public class Parser {
    private String currentCommand;
    private Scanner filScanner;

    /**
     * Opens the input file/stream and gets ready to parser it.
     */
    public Parser(String fileName) {
        currentCommand = null;
        try {
            filScanner = new Scanner(new File(fileName));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Are there more commands in the input?
     */
    public Boolean hasMoreCommands() {
        return filScanner.hasNextLine();
    }

    /**
     * Reads the next command from the input and makes it the current command.
     * Should be called only if hasMoreCommands() is true.
     * Initially there is no current command.
     */
    public void advance() {
        // since the next line might not be a valid command,
        // we must read rest line until read a valid command
        // or reach the end of file. Thus, though hasMoreCommand()
        // return true, might not exist valid command at rest of
        // file. I add NO_COMMAND to represent this situation.
        for (; ; ) {
            if (hasMoreCommands()) {
                currentCommand = filScanner.nextLine();
            }
            else {
                currentCommand = null;
                return;
            }

            // remove all space and comment
            currentCommand = clearCommand(currentCommand);

            // valid command
            if (currentCommand.length() > 0) {
                return;
            }
        }
    }

    /**
     * @return the type of the current command.
     */
    public CommandType commandType() {
        if (currentCommand == null) {
            return CommandType.NO_COMMAND;
        }
        else if (currentCommand.charAt(0) == '@') {
            return CommandType.A_COMMAND;
        }
        else if (currentCommand.charAt(0) == '(') {
            return CommandType.L_COMMAND;
        }
        else {
            return CommandType.C_COMMAND;
        }
    }

    /**
     * @return the symbol or decimal Xxx of the current command @Xxx or (Xxx).
     * Should be called only when commandType() is A_COMMAND or L_COMMAND.
     */
    public String symbol() {
        CommandType type = commandType();
        String symbol = null;

        if (type == CommandType.A_COMMAND) {
            symbol = currentCommand.substring(1);
        }
        else if (type == CommandType.L_COMMAND) {
            symbol = currentCommand.substring(1, currentCommand.length() - 1);
        }
        return symbol;
    }

    /**
     * @return the dest mnemonic in the current C-command (8 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    public String dest() {
        String dest = null;
        int index = currentCommand.indexOf('=');

        // if '=' is at 0-index or not exist, return null
        if (index > 0) {
            dest = currentCommand.substring(0, index);
        }
        return dest;
    }

    /**
     * @return the comp mnemonic in the current C-command (28 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    public String comp() {
        String comp = null;
        // indices of start(in) and end(ex) of comp part
        int start, end, index;

        // get the range of comp part
        index = currentCommand.indexOf('=');
        start = (index != -1) ? (index + 1) : 0;
        index = currentCommand.indexOf(';');
        end = (index != -1) ? index : currentCommand.length();

        comp = currentCommand.substring(start, end);
        return comp;
    }

    /**
     * @return the jump mnemonic in the current C-command (8 possibilities).
     * Should be called only when commandType() is C_COMMAND.
     */
    public String jump() {
        String jump = null;
        int index = currentCommand.indexOf(';');

        // if not jump part, just return null
        if (index != -1) {
            jump = currentCommand.substring(index + 1);
        }
        return jump;
    }

    /**
     * @return the cleared command
     * Removes all white space and comments.
     */
    private static String clearCommand(String cmd) {
        // remove all space in the command
        String result = cmd.replaceAll(" ", "");

        // remove comment in the command
        int index = result.indexOf("//");
        if (index == -1) {
            result = result.substring(0);
        }
        else {
            result = result.substring(0, index);
        }

        return result;
    }
}