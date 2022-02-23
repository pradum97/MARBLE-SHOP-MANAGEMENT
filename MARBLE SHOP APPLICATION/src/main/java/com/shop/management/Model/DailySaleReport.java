package com.shop.management.Model;

public class DailySaleReport {

    private int sales_id;
    private String product_name, product_type, product_size , quantity;

    private double purchase_price, product_mrp, sell_price,
            discount_amount, net_amount;
  private   String tax_amount;

    private int product_tax;
    private String billType;

    public DailySaleReport(int sales_id, String product_name,
                           String product_type, String product_size, String quantity,
                           double purchase_price, double product_mrp, double sell_price, double discount_amount, double net_amount, String tax_amount, int product_tax, String billType) {
        this.sales_id = sales_id;
        this.product_name = product_name;
        this.product_type = product_type;
        this.product_size = product_size;
        this.quantity = quantity;
        this.purchase_price = purchase_price;
        this.product_mrp = product_mrp;
        this.sell_price = sell_price;
        this.discount_amount = discount_amount;
        this.net_amount = net_amount;
        this.tax_amount = tax_amount;
        this.product_tax = product_tax;
        this.billType = billType;
    }

    @Override
    public String toString() {
        return this.getProduct_name();
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }


    public int getSales_id() {
        return sales_id;
    }

    public void setSales_id(int sales_id) {
        this.sales_id = sales_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(double product_mrp) {
        this.product_mrp = product_mrp;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(double net_amount) {
        this.net_amount = net_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public int getProduct_tax() {
        return product_tax;
    }

    public void setProduct_tax(int product_tax) {
        this.product_tax = product_tax;
    }
}
