package com.shop.management.Model;

public class SupplierModel {

    private int supplierId;
    private String supplierName , supplierPhone , supplierEmail , supplierGstNum  ,supplierAddress  , supplierState , addedSate;

    public SupplierModel(int supplierId, String supplierName, String supplierPhone, String supplierEmail, String supplierGstNum,
                         String supplierAddress, String supplierState, String addedSate) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
        this.supplierEmail = supplierEmail;
        this.supplierGstNum = supplierGstNum;
        this.supplierAddress = supplierAddress;
        this.supplierState = supplierState;
        this.addedSate = addedSate;
    }

    @Override
    public String toString() {
        return this.getSupplierName();
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierGstNum() {
        return supplierGstNum;
    }

    public void setSupplierGstNum(String supplierGstNum) {
        this.supplierGstNum = supplierGstNum;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState;
    }

    public String getAddedSate() {
        return addedSate;
    }

    public void setAddedSate(String addedSate) {
        this.addedSate = addedSate;
    }
}
