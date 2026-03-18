package uk.gov.courtservice.xhibit.client.order.gui.entry.d20offencecodes;

/**
 * This class can be instantiated by the AsynchronousLoader in order to trigger
 * the automatic instantiation of it as a loadable in the first instance. This
 * can be trivially done using reflection.
 * 
 * @author Neil Ellis
 */

public class D20OffenceCodesListFactory {
    private static D20OffenceCodeList instance = D20OffenceCodeListMidTier.getInstance();

    public static D20OffenceCodeList getD20OffenceCodesList() {
        return instance;
    }
}