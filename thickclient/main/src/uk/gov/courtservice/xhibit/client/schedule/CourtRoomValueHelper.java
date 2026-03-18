package uk.gov.courtservice.xhibit.client.schedule;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CourtRoomValueHelper {
    private XhbCourtRoomBasicValue model;

    public CourtRoomValueHelper(XhbCourtRoomBasicValue obj) {
        setModel(obj);
    }

    public String toString() {
        return getModel().getDisplayNameNoSite();
    }

    public void setModel(XhbCourtRoomBasicValue model) {
        this.model = model;
    }

    public XhbCourtRoomBasicValue getModel() {
        return model;
    }
}