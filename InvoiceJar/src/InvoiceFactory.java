import java.util.Collection;
import java.util.Vector;

public class InvoiceFactory {

    public static Collection<InvoiceBean> getProduct() {

        InvoiceBean invoiceBean1 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);
        InvoiceBean invoiceBean2 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);
        InvoiceBean invoiceBean3 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);
        InvoiceBean invoiceBean4 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);
        InvoiceBean invoiceBean5 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);
        InvoiceBean invoiceBean6 = new InvoiceBean(1,  "Marble", "200x200", "20-PKS", "SPECIAL", 3200,
                9, 9, 0, 200, 80, 30, 1000);

        Vector<InvoiceBean> beanCollection = new Vector<>();
        beanCollection.add(invoiceBean1);
        beanCollection.add(invoiceBean2);
        beanCollection.add(invoiceBean3);
        beanCollection.add(invoiceBean4);
        beanCollection.add(invoiceBean5);
        beanCollection.add(invoiceBean6);

        return beanCollection;
    }

    public static Collection<InvoiceBean> getShop() {

        InvoiceBean shop = new InvoiceBean("SUMA MARBLE AND TILES","9709759387" , "9608461591" , "778FHFSHF7FSD","suma@gmail.com","छोटी बलिया , N.H. 31 , पानी टंकी से पूरब ( बेगूसराय ) - 851211 Bihar","Raj Kumar");


        Vector<InvoiceBean> beanCollection = new Vector<>();
        beanCollection.add(shop);

        return beanCollection;
    }


    public static Collection<InvoiceBean> getCustomer() {

        InvoiceBean cus = new InvoiceBean(25220221 ,12345, "Pradum Kumar", "9608461591",
                "Lakhisarai Bihar , 811106");


        Vector<InvoiceBean> beanCollection = new Vector<>();
        beanCollection.add(cus);

        return beanCollection;
    }


}
