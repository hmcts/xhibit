package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class CourtLogControllerModel implements Cloneable {
    private HearingHeaderValue hearingHeaderValue;

    private CourtLogHeaderTableModel courtLogHeaderTableModel;

    private CourtLogEventsTableModel courtLogEventsTableModel;

    private Object[] editableEvents;

    private CourtLogViewValue[] listOfEvents;

    private ApplicationCaseModel acm;

    private int selectedEventsTableRow;

    private int selectedHeadTableColumn;

    public CourtLogControllerModel() {
        super();
    }

    // Getters
    public CourtLogViewValue[] getListOfEvents() {
        return listOfEvents;
    }

    // public
    // uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.CourtLogHeaderValue
    // getCourtLogHeaderValue( ) { return courtLogHeaderValue; }
    public CourtLogHeaderTableModel getCourtLogHeaderTableModel() {
        return courtLogHeaderTableModel;
    }

    public CourtLogEventsTableModel getCourtLogEventsTableModel() {
        return courtLogEventsTableModel;
    }

    public Object[] getEditableEvents() {
        return editableEvents;
    }

    public ApplicationCaseModel getAcm() {
        return acm;
    }

    public int getSelectedEventsTableRow() {
        return selectedEventsTableRow;
    }

    public int getSelectedHeaderTableColumn() {
        return selectedHeadTableColumn;
    }

    public HearingHeaderValue getHearingHeaderValue() {
        return hearingHeaderValue;
    }

    // Setters
    public void setListOfEvents(CourtLogViewValue[] param) {
        this.listOfEvents = param;
    }

    // public void setCourtLogHeaderValue(
    // uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.CourtLogHeaderValue
    // param ) { this.courtLogHeaderValue = param; }
    public void setCourtLogHeaderTableModel(CourtLogHeaderTableModel param) {
        this.courtLogHeaderTableModel = param;
    }

    public void setCourtLogEventsTableModel(CourtLogEventsTableModel param) {
        this.courtLogEventsTableModel = param;
    }

    public void setEditableEvents(Object[] param) {
        this.editableEvents = param;
    }

    public void setAcm(ApplicationCaseModel param) {
        this.acm = param;
    }

    public void setSelectedEventsTableRow(int param) {
        this.selectedEventsTableRow = param;
    }

    public void setSelectedHeaderTableColumn(int param) {
        this.selectedHeadTableColumn = param;
    }

    public void setHearingHeaderValue(HearingHeaderValue param) {
        this.hearingHeaderValue = param;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("CourtLogControllerModel");
        XHIBITConstant.info("-----------------------");
        XHIBITConstant.info("Selected Column: " + getSelectedHeaderTableColumn());
        XHIBITConstant.info("Selected Event : " + getSelectedEventsTableRow());
        XHIBITConstant.info("Editable Events: ");

        for (int x = 0; x < getEditableEvents().length; x++) {
            String myString = (String) getEditableEvents()[x];
            XHIBITConstant.info("    " + myString);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        // setCourtLogHeaderValue( null );
        setListOfEvents(null);
        setCourtLogHeaderTableModel(null);
        setCourtLogEventsTableModel(null);
        setEditableEvents(null);
        setSelectedEventsTableRow(0);
        setSelectedHeaderTableColumn(0);
        setHearingHeaderValue(null);
    }
}