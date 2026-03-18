package uk.gov.courtservice.xhibit.business.vos.services.systemadmin;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: Import/Export Status Busines Value Object
 * </p>
 * <p>
 * Description: Business Value object for import and export notification
 * statuses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class ImportExportStatusValue extends CSAbstractValue {

    // varibales to hold collections of basic value objects
    private ArrayList _caseStatuses = new ArrayList();

    private ArrayList _courtStatuses = new ArrayList();

    // variables to hold the case type and number
    private String _caseType;

    private String _caseNumber;
    
    private static final long serialVersionUID =-2284002577997699717L;

    // empty constructor
    public ImportExportStatusValue() {
    }

    /**
     * Returns a collection of import and export statuses for a particular case
     * 
     * @return - A collection of ImportExportCaseCourtDetailsValue
     */
    public ArrayList getCaseStatuses() {
        return _caseStatuses;
    }

    /**
     * Returns a collection of import and export statuses for a particular court
     * 
     * @return - A collection of ImportExportCaseCourtDetailsValue
     */
    public ArrayList getCourtStatuses() {
        return _courtStatuses;
    }

    public String getcaseType() {
        return _caseType;
    }

    public String getcaseNumber() {
        return _caseNumber;
    }

    // setters
    public void setCaseStatuses(ArrayList caseStatuses) {
        _caseStatuses = caseStatuses;
    }

    public void setCourtStatuses(ArrayList courtStatuses) {
        _courtStatuses = courtStatuses;
    }

    public void setCaseType(String caseType) {
        _caseType = caseType;
    }

    public void setCaseNumber(String caseNumber) {
        _caseNumber = caseNumber;
    }

}