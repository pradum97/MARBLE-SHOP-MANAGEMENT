package com.shop.management.Model;

public class TAX {

    int taxID ;
    double sgst , cgst , igst;
    String taxDescription;

    public TAX(int taxID, double sgst, double cgst, double igst, String taxDescription) {
        this.taxID = taxID;
        this.sgst = sgst;
        this.cgst = cgst;
        this.igst = igst;
        this.taxDescription = taxDescription;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    @Override
    public String toString() {
        return this.getCgst()+this.getSgst()+this.getIgst()+" %";
    }
}
