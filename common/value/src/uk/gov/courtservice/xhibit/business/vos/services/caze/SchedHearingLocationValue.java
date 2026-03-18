package uk.gov.courtservice.xhibit.business.vos.services.caze;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @Jide Fakoya
 * @version 1.0
 */

public class SchedHearingLocationValue extends CSAbstractValue {
    private Integer courtSiteID;

    private Integer courtRoomID;

    private Integer shedHearingID;
    private static final long serialVersionUID = 5801203133287702757L;

    /**
     * 
     * @param courtSiteID
     */
    public Integer getCourtSiteID() {
        return courtSiteID;
    }

    public void setCourtSiteID(Integer courtSiteID) {
        this.courtSiteID = courtSiteID;
    }

    public void setCourtRoomID(Integer courtRoomID) {
        this.courtRoomID = courtRoomID;
    }

    /**
     * 
     * @param courtRoomID
     */

    public Integer getCourtRoomID() {
        return courtRoomID;
    }

    public void setShedHearingID(Integer shedHearingID) {
        this.shedHearingID = shedHearingID;
    }

    /**
     * 
     * @param shedHearingID
     */
    public Integer getShedHearingID() {
        return shedHearingID;
    }

}