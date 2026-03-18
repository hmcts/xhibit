package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * <p>
 * Title: HRJusticeValue
 * </p>
 * <p>
 * Description: Value object to store the justice information required on a
 * CREST form 'A'. This is a read-only object.
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
public class HRJusticeValue implements HRValueObject {

    private Integer shJusticeID;

    private String justiceName;

    private Integer hearingID;
    
    private static final long serialVersionUID = -6798385637156957580L;

    public HRJusticeValue(Integer shJID) {
        this.shJusticeID = shJID;
    }

    public HRJusticeValue() {
    }

    public Integer getShJusticeID() {
        return shJusticeID;
    }

    public void setShJusticeID(Integer shJusticeID) {
        this.shJusticeID = shJusticeID;
    }

    public String getJusticeName() {
        return justiceName;
    }

    public void setJusticeName(String justiceName) {
        this.justiceName = justiceName;
    }

    public Integer getHearingID() {
        return hearingID;
    }

    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

}
