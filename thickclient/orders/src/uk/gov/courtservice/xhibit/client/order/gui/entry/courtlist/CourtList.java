package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

/**
 * Created by IntelliJ IDEA. User: qzd3k3 Date: 11-Mar-2003 Time: 09:35:25 To
 * change this template use Options | File Templates.
 */
public interface CourtList {
    String[] getShortNames();

    String[] getOrderTypes();

    String[] getMagistrateNames();

    String[] getYouthValues();

    String[] getCourtValues();

    boolean isCourtExists(String name);

    public Object getCourt(String key); // SCR 52926 & 52933
}
