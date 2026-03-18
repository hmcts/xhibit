package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

/**
 * This class can be instantiated by the AsynchronousLoader in order to trigger
 * the automatic instantiation of it as a loadable in the first instance. This
 * can be trivially done using reflection.
 * 
 * @author Neil Ellis
 */

public class CourtListFactory {
    private static CourtList instance = CourtListMidTier.getInstance();

    public static CourtList createCourtList() {
        return instance;
    }
}
