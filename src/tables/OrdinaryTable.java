package tables;

import java.util.Arrays;

public class OrdinaryTable {
    private long CA_ID;
    private String CA_NAME;

    public OrdinaryTable() {}
    public OrdinaryTable(String line) {
        String[] row = line.split("\\|");
        this.CA_ID = Long.parseLong(row[0]);
        this.CA_NAME = row[1];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(CA_ID).append("|");
        sb.append(CA_NAME).append("|");

        return sb.toString();
    }
}
