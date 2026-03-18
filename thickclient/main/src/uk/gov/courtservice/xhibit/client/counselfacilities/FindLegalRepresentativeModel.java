package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: FindLegalRepresentativeModel
 * </p>
 * <p>
 * Description: The model for Finding Legal Representatives
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

public class FindLegalRepresentativeModel implements Cloneable {
    private String repTypeRadio;

    private FindLegalRepresentativeTableRowModel flrTableRowModel;

    private FindLegalRepresentativeTableRowModel flrInstructedAdvocateRowModel;

    private XhibitApplicationController xac;

    private boolean disableInPerson;
    
    private boolean disableNonAttendance;
    
    private String barristerType;

    public FindLegalRepresentativeModel() {
        // empty
    }

    // Getters
    public String getRepTypeRadio() {
        return CounselFacilitiesHelper.tidyUp(repTypeRadio);
    }

    public String getBarristerType() {
        return barristerType;
    }
    
    public FindLegalRepresentativeTableRowModel getFindLegalRepresentativeTableRowModel() {
        return flrTableRowModel;
    }

    public FindLegalRepresentativeTableRowModel getInstructedAdvocateTableRowModel() {
        return flrInstructedAdvocateRowModel;
    }
    
    public XhibitApplicationController getXac() {
        return xac;
    }

    public boolean isDisableInPerson() {
        return disableInPerson;
    }
    
    public boolean isDisableNonAttendance() {
        return disableNonAttendance;
    }

    // Setters
    public void setRepTypeRadio(String param) {
        repTypeRadio = param;
    }

    public void setBarristerType(String param) {
        barristerType = param;
    }
    
    public void setFindLegalRepresentativeTableRowModel(FindLegalRepresentativeTableRowModel param) {
        flrTableRowModel = param;
    }

    public void setInstructedAdvocateTableRowModel(FindLegalRepresentativeTableRowModel param) {
        flrInstructedAdvocateRowModel = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setDisableInPerson(boolean param) {
        disableInPerson = param;
    }
    
    public void setDisableNonAttendance(boolean param) {
        disableNonAttendance = param;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("FindLegalRepresentativeModel");
        XHIBITConstant.info("----------------------------");
        XHIBITConstant.info("RepTypeRadio   : " + getRepTypeRadio());
        XHIBITConstant.info("XAC            : " + getXac());
        XHIBITConstant.info("DisableInPerson: " + isDisableInPerson());
        XHIBITConstant.info("DisableNonAttendance: " + isDisableNonAttendance());

        FindLegalRepresentativeTableRowModel item = getFindLegalRepresentativeTableRowModel();
        if (item != null) {
            item.printModel();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setBarristerType(null);
        setRepTypeRadio(null);
        setFindLegalRepresentativeTableRowModel(null);
        setInstructedAdvocateTableRowModel(null);
        setXac(null);
        setDisableInPerson(false);
    }
    
    public boolean isInPersonSelected() {
        return (repTypeRadio != null && repTypeRadio.equals(CounselFacilitiesHelper.INPRADIO));
    }
    
    public boolean isNonAttendanceSelected() {
        return (repTypeRadio != null && repTypeRadio.equals(CounselFacilitiesHelper.NONATTRADIO));
    }
    
    public boolean isBarristerSelected() {
        return (repTypeRadio != null && repTypeRadio.equals(CounselFacilitiesHelper.BARRADIO));
    }
    
    public boolean isSolicitorSelected() {
        return (repTypeRadio != null && repTypeRadio.equals(CounselFacilitiesHelper.SOLRADIO));
    }
}
