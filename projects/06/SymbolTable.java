import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Integer> symbolTable;

    /**
     * Creates a new empty symbol table.
     * But I let it contain predefined symbols.
     */
    public SymbolTable() {
        symbolTable = new HashMap<>();

        // add predefined symbols
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        for (int i = 0; i < 16; i++) {
            symbolTable.put("R"+i, i);
        }
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }

    /**
     * Adds the pair (symbol, address) to the table.
     */
    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    /**
     * Does the symbol table contain the given symbol?
     */
    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    /**
     * @return the address associated with the symbol.
     */
    public int getAddress(String symbol) {
        return symbolTable.get(symbol);
    }
}
