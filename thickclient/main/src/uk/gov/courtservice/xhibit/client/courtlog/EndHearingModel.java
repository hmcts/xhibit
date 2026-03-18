package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class EndHearingModel extends FreeTextModel {
    private boolean endLinkedCases;

    /**
     * Public constructor
     */
    public EndHearingModel() {
        super();
    }

    /**
     * Indicates whether or not a case is linked
     * 
     * @return - true if the case is linked, otherwise return false
     */
    public boolean isEndLinkedCases() {
        return endLinkedCases;
    }

    /**
     * Sets the indicator that determines whether or not the case is linked
     * 
     * @param endLinkedCases -
     *            true if they are linked, otherwise false
     */
    public void setEndLinkedCases(boolean endLinkedCases) {
        this.endLinkedCases = endLinkedCases;
    }

    /**
     * Prints details from the model
     */
    public void printModel() {
        super.printModel();

        XHIBITConstant.info("EndHearingModel");
        XHIBITConstant.info("---------------");
        XHIBITConstant.info("endLinkedCases?: " + isEndLinkedCases());
    }

    /**
     * Clears the model initialising any attributes
     */
    public void clearmodel() {
        super.clearmodel();

        setEndLinkedCases(false);
    }
}
