package uk.gov.courtservice.xhibit.client.order.screens.helper;

/**
 * <p>Title: SentDetailsVO</p>
 * <p>Description: Holds deatils when an order is sent in the dialog</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Neil Entwistle
 * @version 1.0
 */

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class SentDetailsVO {

    /**
     * Date the order was sent as a String
     */
    private String sentDate = null;

    private String sentSurname = null;

    private String sentForename = null;

    private String sentInitial = null;

    private String sentTitle = null;

    private XhibitApplicationController controller;

    /**
     * Indicator to show that the dialog was cancelled by the user
     */
    private boolean cancelledByUser = false;

    /**
     * Return the forename of the signatory
     * 
     * @return The forename
     */
    public String getSentForename() {
        return sentForename;
    }

    /**
     * Return the initial of the signatory
     * 
     * @return The initial
     */
    public String getSentInitial() {
        return sentInitial;
    }

    /**
     * Return the title of the signatory
     * 
     * @return The title
     */
    public String getSentTitle() {
        return sentTitle;
    }

    /**
     * Return the Xhibit Controller
     * 
     * @return The controller
     */
    public XhibitApplicationController getController() {
        return controller;
    }

    /**
     * Return the indicator to show that the dialog was cancelled
     * 
     * @return true if dialog was cancelled
     */
    public boolean getCancelledBy() {
        return cancelledByUser;
    }

    /**
     * Returns the date the order was sent
     * 
     * @return String
     */
    public String getSentDate() {
        return sentDate;
    }

    /**
     * Return the surname of the signatory
     * 
     * @return The surname
     */
    public String getSentSurname() {
        return sentSurname;
    }

    /**
     * Sets the forename of the signatory
     * 
     * @param sentForename
     *            The forename
     */
    public void setSentForename(String sentForename) {
        // Hack fix.
        if (sentForename == null || sentForename.length() == 0) {
            this.sentForename = " ";
        } else {
            this.sentForename = sentForename;
        }
    }

    /**
     * Sets the initial of the signatory
     * 
     * @param sentInitial
     *            The initial
     */
    public void setSentInitial(String sentInitial) {
        // Hack fix.
        if (sentInitial == null || sentInitial.length() == 0) {
            this.sentInitial = " ";
        } else {
            this.sentInitial = sentInitial;
        }
    }

    /**
     * Sets the title of the signatory
     * 
     * @param sentTitle
     *            The title
     */
    public void setSentTitle(String sentTitle) {
        // Hack fix.
        if (sentTitle == null || sentTitle.length() == 0) {
            this.sentTitle = " ";
        } else {
            this.sentTitle = sentTitle;
        }
    }

    /**
     * Sets the XhibitApplicationController
     * 
     * @param controller
     *            The controller
     */
    public void setController(XhibitApplicationController controller) {
        this.controller = controller;
    }

    /**
     * Sets the date the order was sent
     * 
     * @param date
     *            The date
     */
    public void setSentDate(String date) {
        sentDate = date;
    }

    /**
     * Sets the surname of the signatory
     * 
     * @param sentSurname
     *            The surname
     */
    public void setSentSurname(String sentSurname) {
        // Hack fix.
        if (sentSurname == null || sentSurname.length() == 0) {
            this.sentSurname = " ";
        } else {
            this.sentSurname = sentSurname;
        }
    }

    /**
     * Sets the indicator to show if the dialog was cancelled
     * 
     * @param can
     *            true if dialog was cancelled
     */
    public void setCancelledBy(boolean can) {
        cancelledByUser = can;
    }

    /**
     * Reset the sent details.
     */
    public void reset() {
        sentDate = null;
        sentSurname = null;
        sentForename = null;
        sentInitial = null;
        sentTitle = null;
    }

}