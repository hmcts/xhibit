package uk.gov.courtservice.xhibit.business.services.messaging.im;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

/**
 * <p>
 * Title: The Courtroom Information in form of a tree node.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: CourtRoomNode.java,v 1.5 2006/06/05 12:29:12 bzjrnl Exp $
 */
public class CourtRoomNode extends XhbCourtRoomBasicValue implements Serializable {
    /**
     * The displayed name of the node seen on the tree model.
     */
    String name;

    public CourtRoomNode() {
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
