package uk.gov.courtservice.xhibit.client.util.security;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: RoamingTerminalValue.java,v 1.3 2006/06/05 12:30:43 bzjrnl Exp $
 */

public class RoamingTerminalValue {

    private Integer courtId;

    private Integer courtSiteId;

    private Integer courtRoomId;

    private String courtName;

    private String courtDisplayName;

    private String courtSiteDisplayName;

    private String courtRoomDisplayName;

    public RoamingTerminalValue() {
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setCourtSiteId(Integer courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    public Integer getCourtSiteId() {
        return courtSiteId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setTerminalLocation(String courtDisplayName, String courtSiteDisplayName, String courtRoomDisplayName) {
        this.courtDisplayName = courtDisplayName;
        this.courtSiteDisplayName = courtSiteDisplayName;
        this.courtRoomDisplayName = courtRoomDisplayName;
    }

    public String getTerminalLocation() {
        if (courtDisplayName == null || courtSiteDisplayName == null || courtRoomDisplayName == null) {
            throw new CSUnrecoverableException(new Message("roaming.terminallocation.invalid"),
                    "The roaming value is not correctly set up. courtDisplayName=\"" + courtDisplayName
                            + "\" courtSiteDisplayName=\"" + courtSiteDisplayName + "\" courtRoomDisplayName=\""
                            + courtRoomDisplayName + "\"");
        }
        return "/" + courtDisplayName.toLowerCase().replace(' ', '_') + "/"
                + courtSiteDisplayName.toLowerCase().replace(' ', '_') + "/"
                + courtRoomDisplayName.toLowerCase().replace(' ', '_');
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCourtName() {
        return courtName;
    }
}