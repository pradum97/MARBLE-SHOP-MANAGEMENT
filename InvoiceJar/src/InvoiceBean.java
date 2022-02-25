public class InvoiceBean {

    // product Details
    private int srNo;
    private String productName, productSize, quantity, discountName;
    private long hsn;
    private int sgst, cgst, igst;
    private double mrp, taxAmount, discountAmount, netAmount;

    public InvoiceBean(int srNo, String productName, String productSize,
                       String quantity, String discountName, long hsn, int sgst, int cgst, int igst,
                       double mrp, double taxAmount, double discountAmount, double netAmount) {
        this.srNo = srNo;
        this.productName = productName;
        this.productSize = productSize;
        this.quantity = quantity;
        this.discountName = discountName;
        this.hsn = hsn;
        this.sgst = sgst;
        this.cgst = cgst;
        this.igst = igst;
        this.mrp = mrp;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.netAmount = netAmount;
    }

    // customer details
    private int customerId ;
    private long invoiceNumber;
    private String customerName, customerPhone, customerAddress;

    public InvoiceBean(long invoiceNumber ,int customerId, String customerName, String customerPhone, String customerAddress) {
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
    }

    // shop details
    private String shopName;
    private String shopPhone1;
    private String shopPhone2;
    private String shopGstNum;
    private String shopEmail;
    private String shopAddress;
    private String shopOwnerName;

    public InvoiceBean(String shopName, String shopPhone1, String shopPhone2,
                       String shopGstNum, String shopEmail, String shopAddress , String shopOwnerName) {
        this.shopName = shopName;
        this.shopPhone1 = shopPhone1;
        this.shopPhone2 = shopPhone2;
        this.shopGstNum = shopGstNum;
        this.shopEmail = shopEmail;
        this.shopAddress = shopAddress;
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone1() {
        return shopPhone1;
    }

    public void setShopPhone1(String shopPhone1) {
        this.shopPhone1 = shopPhone1;
    }

    public String getShopPhone2() {
        return shopPhone2;
    }

    public void setShopPhone2(String shopPhone2) {
        this.shopPhone2 = shopPhone2;
    }

    public String getShopGstNum() {
        return shopGstNum;
    }

    public void setShopGstNum(String shopGstNum) {
        this.shopGstNum = shopGstNum;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public long getHsn() {
        return hsn;
    }

    public void setHsn(long hsn) {
        this.hsn = hsn;
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

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }
}
