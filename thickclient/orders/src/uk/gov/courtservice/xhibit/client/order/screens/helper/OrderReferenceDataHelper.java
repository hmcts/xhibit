package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: xztnfq Date: 05-Mar-2003 Time: 13:48:18 To
 * change this template use Options | File Templates.
 */
public class OrderReferenceDataHelper {
    private ArrayList signatories;

    public OrderReferenceDataHelper() {
        super();
    }

    /**
     * 
     * @return
     */
    public ArrayList getSignatoryList() {
        if (signatories == null) {
            loadSignatories();
        }
        return signatories;
    }

    /**
     * 
     */
    private void loadSignatories() {
        String[] names = new String[] { " ", "Clerk A", "Clerk B", "Clerk C", "Judge Wilberforce P Cuthbertson" };
        signatories = new ArrayList();
        for (int i = 0; i < names.length; i++) {
            signatories.add(names[i]);
        }
    }

    public void getSignatoryDetails(int index) {
        signatories.get(index);
    }

}
