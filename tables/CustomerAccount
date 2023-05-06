package tables;

import java.util.Arrays;

public class CustomerAccount {
    private long CA_ID;
    private long CA_B_ID;
    private long CA_C_ID;
    private String CA_NAME;
    private long CA_TAX_ST;
    private float CA_BAL;

    public CustomerAccount() {}
    public CustomerAccount(String line) {
        String[] row = line.split("\\|");
        this.CA_ID = Long.parseLong(row[0]);
        this.CA_B_ID = Long.parseLong(row[1]);
        this.CA_C_ID = Long.parseLong(row[2]);
        this.CA_NAME = row[3];
        this.CA_TAX_ST = Long.parseLong(row[4]);
        this.CA_BAL = Float.parseFloat(row[5]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(CA_ID).append("|");
        sb.append(CA_B_ID).append("|");
        sb.append(CA_C_ID).append("|");
        sb.append(CA_NAME).append("|");
        sb.append(CA_TAX_ST).append("|");
        sb.append(CA_BAL);

        return sb.toString();
    }
}
