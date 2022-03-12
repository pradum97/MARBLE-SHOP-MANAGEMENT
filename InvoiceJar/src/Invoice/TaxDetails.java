package Invoice;

public class TaxDetails {

    private int  sgst , cgst , igst ;
    private double taxableAmount , sgstAmount ,cgstAmount , igstAmount;
    private String hsn ;

    public TaxDetails(){}

    public int getSgst() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst = sgst;
    }

    public int getCgst() {
        return cgst;
    }

    public void setCgst(int cgst) {
        this.cgst = cgst;
    }

    public int getIgst() {
        return igst;
    }

    public void setIgst(int igst) {
        this.igst = igst;
    }

    public double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public double getIgstAmount() {
        return igstAmount;
    }

    public void setIgstAmount(double igstAmount) {
        this.igstAmount = igstAmount;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }
}
