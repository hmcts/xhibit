package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ExportAValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * ExportA enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 * 
 * <Change History/>
 * 
 * <P>
 * 18/02/03 - MH - Changed to use id from super class instead.
 */

public class ExportAValue extends CSAbstractValue {

	private static final long serialVersionUID = -5057536133900306299L;
	// private Integer exportAID;
    private Integer linkedHearingID;

    private String statusFlag;

    private String courtClerkName;

    private Integer hearingID;

    public ExportAValue() {
        super();
    }

    public ExportAValue(Integer version) {
        super(version);
    }

    public ExportAValue(Integer id, Integer version) {
        super(id, version);
    }

    public ExportAValue(Integer exportAID, Integer linkedHearingID, String statusFlag, String courtClerkName,
            Integer hearingID, Integer version) {
        this(exportAID, version);
        this.linkedHearingID = linkedHearingID;
        this.statusFlag = statusFlag;
        this.courtClerkName = courtClerkName;
        this.hearingID = hearingID;
    }

    // public Integer getId() {
    /**
     * @todo Implement this
     *       uk.gov.courtservice.framework.business.vos.CSValueObject method
     */
    // throw new java.lang.UnsupportedOperationException("Method getId() not
    // yet implemented.");
    // }
    // public Integer getVersion() {
    /**
     * @todo Implement this
     *       uk.gov.courtservice.framework.business.vos.CSValueObject method
     */
    // throw new java.lang.UnsupportedOperationException("Method
    // getVersion() not yet implemented.");
    // }
    public String getCourtClerkName() {
        return courtClerkName;
    }

    /*
     * use super getID() instead. public Integer getExportAID() { return
     * exportAID; }
     */
    public Integer getHearingID() {
        return hearingID;
    }

    public Integer getLinkedHearingID() {
        return linkedHearingID;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setCourtClerkName(String courtClerkName) {
        this.courtClerkName = courtClerkName;
    }

    /*
     * use super setID() instead. public void setExportAID(Integer exportAID) {
     * this.exportAID = exportAID; }
     */
    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

    public void setLinkedHearingID(Integer linkedHearingID) {
        this.linkedHearingID = linkedHearingID;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }
}