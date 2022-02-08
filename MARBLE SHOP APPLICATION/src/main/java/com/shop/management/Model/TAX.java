package com.shop.management.Model;

public class TAX {

    int taxID ;
    int sgst , cgst , igst;
    String gstName ,taxDescription;

    public TAX(int taxID, int sgst, int cgst, int igst, String gstName, String taxDescription) {
        this.taxID = taxID;
        this.sgst = sgst;
        this.cgst = cgst;
        this.igst = igst;
        this.gstName = gstName;
        this.taxDescription = taxDescription;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

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

    public String getGstName() {
        return gstName;
    }

    public void setGstName(String gstName) {
        this.gstName = gstName;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    @Override
    public String toString() {
        return this.getGstName();
    }
}
