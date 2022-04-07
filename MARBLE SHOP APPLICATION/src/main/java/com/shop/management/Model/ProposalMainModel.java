package com.shop.management.Model;

public class ProposalMainModel {
    private int proposalMainId;
    private String customerName , customerPhone , customerAddress , invoiceNumber , proposeDate;

    public ProposalMainModel(int proposalMainId, String customerName, String customerPhone,
                             String customerAddress, String invoiceNumber, String proposeDate) {
        this.proposalMainId = proposalMainId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.invoiceNumber = invoiceNumber;
        this.proposeDate = proposeDate;
    }

    public int getProposalMainId() {
        return proposalMainId;
    }

    public void setProposalMainId(int proposalMainId) {
        this.proposalMainId = proposalMainId;
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

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getProposeDate() {
        return proposeDate;
    }

    public void setProposeDate(String proposeDate) {
        this.proposeDate = proposeDate;
    }
}
