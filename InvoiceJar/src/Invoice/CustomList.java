package Invoice;

import java.util.List;

public class CustomList {

    List<TaxDetails> taxDetails;

    public CustomList(){}

    public List<TaxDetails> getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(List<TaxDetails> taxDetails) {
        this.taxDetails = taxDetails;
    }
}
