package uk.gov.courtservice.xhibit.business.vos.services.systemadmin;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: Import Export Case / Court Details
 * </p>
 * <p>
 * Description: Case / Court Details for Import Export Statuses
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

public class ImportExportCaseCourtDetailsValue extends CSAbstractValue {

    // variables to hold the data
    private ImportExportStatusVO importExportStatusVO;

    private String _defFirstname;

    private String _defSurname;

    private String _defMiddlename;

    private String _defInitials;
    
    private static final long serialVersionUID =4340762980782048187L;

    public ImportExportCaseCourtDetailsValue(ImportExportStatusVO impExpVO) {
        importExportStatusVO = impExpVO;
    }

    public ImportExportStatusVO getImportExportStatusValueObject() {
        return importExportStatusVO;
    }

    public String getDefFirstname() {
        return _defFirstname;
    }

    public String getDefInitials() {
        return _defInitials;
    }

    public String getDefMiddlename() {
        return _defMiddlename;
    }

    public String getDefSurname() {
        return _defSurname;
    }

    // public void set_importExportBasicValue(ImportExportBasicValue
    // _importExportBasicValue)
    // {
    // _importExportBasicValue = _importExportBasicValue;
    // }
    public void setDefFirstname(String defFirstname) {
        _defFirstname = defFirstname;
    }

    public void setDefInitials(String defInitials) {
        _defInitials = defInitials;
    }

    public void setDefMiddlename(String defMiddlename) {
        _defMiddlename = defMiddlename;
    }

    public void setDefSurname(String defSurname) {
        _defSurname = defSurname;
    }

}