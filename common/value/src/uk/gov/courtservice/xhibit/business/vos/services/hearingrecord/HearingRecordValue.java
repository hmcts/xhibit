package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

//import java.io.Serializable;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:HearingRecordValue
 * </p>
 * <p>
 * Description: This is the top level value for view hearing record. This value
 * object will hold all value objects so that a hearing record (CREST form 'A')
 * can be viewed. This will hold both editable and non-editable value objects.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Anthony Martin / Marie Holmberg
 * @version 1.0
 */
public class HearingRecordValue extends CSAbstractValue {

    private HearingRecordDisplayValue hearingRecordDisplayValue;

    private HearingRecordUpdateValue hearingRecordUpdateValue;

    private String exportStatus;

    private String courtClerkExporter;

    private Integer exportID;
    
    private static final long serialVersionUID = 2058983335506639187L;

    /**
     * Default constructor, nothing will be set.
     */
    public HearingRecordValue() {
    }

    public HearingRecordDisplayValue getHearingRecordDisplayValue() {
        return hearingRecordDisplayValue;
    }

    public void setHearingRecordDisplayValue(HearingRecordDisplayValue displayValue) {
        hearingRecordDisplayValue = displayValue;
    }

    public HearingRecordUpdateValue getHearingRecordUpdateValue() {
        return hearingRecordUpdateValue;
    }

    public void setHearingRecordUpdateValue(HearingRecordUpdateValue updateValue) {
        hearingRecordUpdateValue = updateValue;
    }

    public String getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(String exportStatus) {
        this.exportStatus = exportStatus;
    }

    public String getCourtClerkExporter() {
        return courtClerkExporter;
    }

    public Integer getExportID() {
        return exportID;
    }

    public void setCourtClerkExporter(String courtClerkExporter) {
        this.courtClerkExporter = courtClerkExporter;
    }

    public void setExportID(Integer exportID) {
        this.exportID = exportID;
    }
}
