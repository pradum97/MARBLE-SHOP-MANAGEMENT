package com.shop.management.Model;

public class Invoice {

    private int invoice_id , sale_main_id;
    private String invoiceDate , invoiceNumber , invoicePath , description;

    public Invoice(int invoice_id, int sale_main_id, String invoiceDate,
                   String invoiceNumber, String invoicePath, String description) {
        this.invoice_id = invoice_id;
        this.sale_main_id = sale_main_id;
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.invoicePath = invoicePath;
        this.description = description;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getSale_main_id() {
        return sale_main_id;
    }

    public void setSale_main_id(int sale_main_id) {
        this.sale_main_id = sale_main_id;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoicePath() {
        return invoicePath;
    }

    public void setInvoicePath(String invoicePath) {
        this.invoicePath = invoicePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
