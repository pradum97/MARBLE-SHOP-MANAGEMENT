package com.shop.management.Model;

public class ReturnMainModel {

    private int  returnMainId , saleMainId , sellerId ;
    private String oldInvoiceNum , newInvoiceNum , remark , returnDate;
    private  double totalRefundableAmount;
    private String customerName , customerPhone;

    public ReturnMainModel(int returnMainId, int saleMainId, int sellerId, String oldInvoiceNum, String newInvoiceNum,
                           String remark, String returnDate, double totalRefundableAmount, String customerName, String customerPhone) {
        this.returnMainId = returnMainId;
        this.saleMainId = saleMainId;
        this.sellerId = sellerId;
        this.oldInvoiceNum = oldInvoiceNum;
        this.newInvoiceNum = newInvoiceNum;
        this.remark = remark;
        this.returnDate = returnDate;
        this.totalRefundableAmount = totalRefundableAmount;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
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

    public int getReturnMainId() {
        return returnMainId;
    }

    public void setReturnMainId(int returnMainId) {
        this.returnMainId = returnMainId;
    }

    public int getSaleMainId() {
        return saleMainId;
    }

    public void setSaleMainId(int saleMainId) {
        this.saleMainId = saleMainId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getOldInvoiceNum() {
        return oldInvoiceNum;
    }

    public void setOldInvoiceNum(String oldInvoiceNum) {
        this.oldInvoiceNum = oldInvoiceNum;
    }

    public String getNewInvoiceNum() {
        return newInvoiceNum;
    }

    public void setNewInvoiceNum(String newInvoiceNum) {
        this.newInvoiceNum = newInvoiceNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalRefundableAmount() {
        return totalRefundableAmount;
    }

    public void setTotalRefundableAmount(double totalRefundableAmount) {
        this.totalRefundableAmount = totalRefundableAmount;
    }
}
