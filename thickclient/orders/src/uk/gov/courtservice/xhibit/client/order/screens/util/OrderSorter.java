package uk.gov.courtservice.xhibit.client.order.screens.util;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;

/**
 * <p>
 * Title: WitnessSessionSorter
 * </p>
 * <p>
 * Description: Utility class for sorting the XhbOrderValue opbjects returned
 * from the middle tier. Currently implemented to sort as follows: Order Type,
 * Status (DESC) - Signed is at the top, Description
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderSorter {

    private static final Logger log = CSServices.getLogger(OrderSorter.class);

    /**
     * Sorts a XhbOrderValue[] to display in the Order List screen.
     * 
     * @param Array
     *            of XhbOrderValue objects
     * @return The sorted array
     */
    public static XhbOrderValue[] sort(XhbOrderValue[] orders) {
        // set up comparator
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof XhbOrderValue) || !(o2 instanceof XhbOrderValue)) {
                    return 0;
                }
                return compare((XhbOrderValue) o1, (XhbOrderValue) o2);
            }

            /**
             * Compare method to compare two XhbOrderValue objects to display in
             * the Order List table. The sort is on Order Type (ASC), Status
             * (DESC) (so that SIGNED appears at the top for each type and
             * Description (ASC)
             * 
             * @param n1
             *            The first XhbOrderValue to compare
             * @param n2
             *            The second XhbOrderValue to compare
             * @return O if same, -1 if n1 < n2, 1 if n1 > n2
             */
            public int compare(XhbOrderValue n1, XhbOrderValue n2) {
                // Compare the Order Types
                int orderCodeResult = n1.getXhbOrderTemplate().getXhbOrderType().getCode().compareTo(
                        n2.getXhbOrderTemplate().getXhbOrderType().getCode());
                // Compare the Order Status
                // Sort the statuses in descending order so that SIGNED appears
                // at the
                // top for each type. The - (minus sign) denotes a descending
                // sort
                int statusResult = -n1.getXhbOrderStatus().getCode().compareTo(n2.getXhbOrderStatus().getCode());
                // Compare the descriptions
                // If the description is null, default to an empty string
                String s1 = n1.getDescription() == null ? "" : n1.getDescription();
                String s2 = n2.getDescription() == null ? "" : n2.getDescription();
                int descResult = s1.compareTo(s2);
                // If the two XhbOrderValues have the different order types
                // just compare these. If the order types are the same, compare
                // the status, if the statuses are the same compare
                // descriptions.
                return (orderCodeResult == 0) ? ((statusResult == 0) ? descResult : statusResult) : orderCodeResult;
            }
        };

        // sort the array
        Arrays.sort(orders, comp);
        return orders;
    }
}