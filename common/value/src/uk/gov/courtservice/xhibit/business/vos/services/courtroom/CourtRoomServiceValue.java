package uk.gov.courtservice.xhibit.business.vos.services.courtroom;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;

/**
 * <p>
 * Title: CourtSiteBasicValue
 * </p>
 * <p>
 * Description: Service value that extends the basic courtroom value but also
 * contains nessecary court site information
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

public class CourtRoomServiceValue extends CourtRoomBasicValue {

    private String courtSiteShortName = null;

    private String courtSiteCourtCode = null;
    private static final long serialVersionUID = -2708116462271158924L;

    /**
     * Constructor to set all the basicvalues in this service value. A kind of
     * load construction.
     * 
     * @param basicValue
     *            CourtRoomBasicValue
     */
    public CourtRoomServiceValue(CourtRoomBasicValue basicValue) {
        super(basicValue.getId(), basicValue.getVersion());
        this.setCourtSiteId(basicValue.getCourtSiteId());
        this.setCourtRoomName(basicValue.getCourtRoomName());
        this.setCrestCourtRoomNo(basicValue.getCrestCourtRoomNo());
        this.setDescription(basicValue.getDescription());
        this.setLocation(basicValue.getLocation());
        this.setDisplayName(basicValue.getDisplayName());
        this.setObsInd(basicValue.getObsInd());
    }

    public CourtRoomServiceValue() {
    }

    public String getCourtSiteCourtCode() {
        return courtSiteCourtCode;
    }

    public String getCourtSiteShortName() {
        return courtSiteShortName;
    }

    public void setCourtSiteCourtCode(String courtSiteCourtCode) {
        this.courtSiteCourtCode = courtSiteCourtCode;
    }

    public void setCourtSiteShortName(String courtSiteShortName) {
        this.courtSiteShortName = courtSiteShortName;
    }

}