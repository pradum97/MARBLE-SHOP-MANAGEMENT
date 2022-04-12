package com.shop.management.Model;

public class InvoiceModel {

    private int sale_main_id , totalItems;
    private String customerName , customerPhone  ,billType , invoiceDate , invoiceNumber ;
    private double gstClaimedAMount  ;

    public InvoiceModel(int sale_main_id, int totalItems, String customerName, String customerPhone,
                        String billType, String invoiceDate, String invoiceNumber, double gstClaimedAMount) {
        this.sale_main_id = sale_main_id;
        this.totalItems = totalItems;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.billType = billType;
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.gstClaimedAMount = gstClaimedAMount;
    }

    public double getGstClaimedAMount() {
        return gstClaimedAMount;
    }

    public void setGstClaimedAMount(double gstClaimedAMount) {
        this.gstClaimedAMount = gstClaimedAMount;
    }

    public int getSale_main_id() {
        return sale_main_id;
    }

    public void setSale_main_id(int sale_main_id) {
        this.sale_main_id = sale_main_id;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
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
}
