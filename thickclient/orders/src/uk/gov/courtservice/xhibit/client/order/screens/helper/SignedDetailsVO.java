package uk.gov.courtservice.xhibit.client.order.screens.helper;

/**
 * <p>Title: SignedDetailsVO</p>
 * <p>Description: Holds deatils when an order is signed in the dialog</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Neil Entwistle
 * @version 1.0
 */

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class SignedDetailsVO {

    /**
     * Date the order was signed as a String
     */
    private String signedDate = null;

    private String signedSurname = null;

    private String signedForename = null;

    private String signedInitial = null;

    private String signedTitle = null;

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
    public String getSignedForename() {
        return signedForename;
    }

    /**
     * Return the initial of the signatory
     * 
     * @return The initial
     */
    public String getSignedInitial() {
        return signedInitial;
    }

    /**
     * Return the title of the signatory
     * 
     * @return The title
     */
    public String getSignedTitle() {
        return signedTitle;
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
     * Returns the date the order was signed
     * 
     * @return String
     */
    public String getSignedDate() {
        return signedDate;
    }

    /**
     * Return the surname of the signatory
     * 
     * @return The surname
     */
    public String getSignedSurname() {
        return signedSurname;
    }

    /**
     * Sets the forename of the signatory
     * 
     * @param signedForename
     *            The forename
     */
    public void setSignedForename(String signedForename) {
        // Hack fix.
        if (signedForename == null || signedForename.length() == 0) {
            this.signedForename = " ";
        } else {
            this.signedForename = signedForename;
        }
    }

    /**
     * Sets the initial of the signatory
     * 
     * @param signedInitial
     *            The initial
     */
    public void setSignedInitial(String signedInitial) {
        // Hack fix.
        if (signedInitial == null || signedInitial.length() == 0) {
            this.signedInitial = " ";
        } else {
            this.signedInitial = signedInitial;
        }
    }

    /**
     * Sets the title of the signatory
     * 
     * @param signedTitle
     *            The title
     */
    public void setSignedTitle(String signedTitle) {
        // Hack fix.
        if (signedTitle == null || signedTitle.length() == 0) {
            this.signedTitle = " ";
        } else {
            this.signedTitle = signedTitle;
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
     * Sets the date the order was signed
     * 
     * @param date
     *            The date
     */
    public void setSignedDate(String date) {
        signedDate = date;
    }

    /**
     * Sets the surname of the signatory
     * 
     * @param signedSurname
     *            The surname
     */
    public void setSignedSurname(String signedSurname) {
        // Hack fix.
        if (signedSurname == null || signedSurname.length() == 0) {
            this.signedSurname = " ";
        } else {
            this.signedSurname = signedSurname;
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
     * Reset the signed details.
     */
    public void reset() {
        signedDate = null;
        signedSurname = null;
        signedForename = null;
        signedInitial = null;
        signedTitle = null;
    }

}