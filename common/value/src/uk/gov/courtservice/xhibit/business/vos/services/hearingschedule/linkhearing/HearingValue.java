package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: HearingValue
 * </p>
 * <p>
 * Description: This will contain only a selection of all the hearing
 * attributes. It will be used to display hearings that can potentially be
 * linked.
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
 */

public class HearingValue implements Serializable {
	private static final long serialVersionUID = -4995651274635262748L;
    // hearing values
    private Integer hearingID;

    private Date startDate;

    private Date endDate;

    // refHearingType values
    private Integer refHearingTypeID;

    private String hearingTypeCode;

    private String hearingTypeDesc;

    /**
     * Default constructor
     */
    public HearingValue() {
    }

    /**
     * Constructor that set the hearing values but not the refHearingValues.
     * 
     * @param hearingID
     * @param startDate
     * @param endDate
     */
    public HearingValue(Integer hearingID, Date startDate, Date endDate, Integer refHearingTypeID) {
        this.hearingID = hearingID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.refHearingTypeID = refHearingTypeID;
    }

    // setters
    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRefHearingTypeID(Integer refHearingTypeID) {
        this.refHearingTypeID = refHearingTypeID;
    }

    public void setHearingTypeCode(String hearingTypeCode) {
        this.hearingTypeCode = hearingTypeCode;
    }

    public void setHearingTypeDesc(String hearingTypeDesc) {
        this.hearingTypeDesc = hearingTypeDesc;
    }

    // getters
    public Integer getHearingID() {
        return this.hearingID;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public Integer getRefHearingTypeID() {
        return this.refHearingTypeID;
    }

    public String getHearingTypeCode() {
        return this.hearingTypeCode;
    }

    public String getHearingTypeDesc() {
        return this.hearingTypeDesc;
    }
}