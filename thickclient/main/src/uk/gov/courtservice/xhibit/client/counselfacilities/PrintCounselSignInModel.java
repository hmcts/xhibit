package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: PrintCounselSignInModel
 * </p>
 * <p>
 * Description: The model for printing the counsel sign in List.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class PrintCounselSignInModel implements Cloneable {
    public PrintCounselSignInModel() {
        // empty
    }

    // Fields
    private Integer selectedCourtRoom;

    private String selectedOption;

    private XhibitApplicationController xac;

    private boolean courtRoomKnown;

    // Getters
    public Integer getSelectedCourtRoom() {
        return selectedCourtRoom;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public boolean isCourtRoomKnown() {
        return courtRoomKnown;
    }

    // Setters
    public void setSelectedCourtRoom(Integer courtRoom) {
        selectedCourtRoom = courtRoom;
    }

    public void setSelectedOption(String option) {
        selectedOption = option;
    }

    public void setXac(XhibitApplicationController controller) {
        xac = controller;
    }

    public void setCourtRoomKnown(boolean param) {
        courtRoomKnown = param;
    }

    public void printModel() {
        XHIBITConstant.info("PrintCounselSignInModel");
        XHIBITConstant.info("-----------------------");
        XHIBITConstant.info("Selected Option    : " + getSelectedOption());
        XHIBITConstant.info("Selected Court Room: " + getSelectedCourtRoom());
        XHIBITConstant.info("XAC                : " + getXac());
        XHIBITConstant.info("Court Room Known?  : " + isCourtRoomKnown());
    }

    public void clearmodel() {
        setSelectedCourtRoom(null);
        setSelectedOption(null);
        setXac(null);
        setCourtRoomKnown(false);
    }
}
