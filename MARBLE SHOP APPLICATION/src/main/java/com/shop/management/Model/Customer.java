package com.shop.management.Model;

public class Customer {

    private int customerId;
    private String name;
    private long phone;
    private String address;
    private double duesAmount;
    private String notes , description;

    public Customer(int customerId, String name, long phone,
                    String address, double duesAmount,
                    String notes, String description) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.duesAmount = duesAmount;
        this.notes = notes;
        this.description = description;
    }

    public Customer(int customerId) {
        this.customerId = customerId;

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDuesAmount() {
        return duesAmount;
    }

    public void setDuesAmount(double duesAmount) {
        this.duesAmount = duesAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
