package uk.gov.courtservice.xhibit.business.services.messaging.im;

import java.io.Serializable;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;

/**
 * <p>
 * Title: The Courtsite Information in form of a tree node.
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
 * @version $Id: CourtSiteNode.java,v 1.6 2006/06/05 12:29:13 bzjrnl Exp $
 */
public class CourtSiteNode extends XhbCourtSiteBasicValue implements Serializable {
    private static final Logger log = CSServices.getLogger(CourtSiteNode.class);

    CourtRoomNode[] courtRooms;

    /** The displayed name of the node seen on the tree model. */
    String name;

    public CourtSiteNode(Integer id) throws MessagingException {
        try {
            XhbCourtSite cs = XhbCourtSiteBeanHelper.findByPrimaryKey(id);
            this.name = cs.getDisplayName();

            XhbCourtRoomBasicValue[] a = cs.getXhbCourtRoomsData();
            this.courtRooms = new CourtRoomNode[a.length];
            this.setCourtSiteId(id);

            for (int i = 0; i < a.length; i++) {
                courtRooms[i] = new CourtRoomNode();
                courtRooms[i].name = a[i].getDisplayName();
            }
        } catch (ObjectNotFoundException ex) {
            log.error("Problem retrieving Court Rooms for id " + id + "  : " + ex);
            throw new MessagingException("MESSAGING_CSN001", "Could not retrieve Court Rooms");
        }
    }

    public CourtRoomNode[] getCourtRooms() {
        return courtRooms;
    }

    public String getName() {
        return name;
    }
}
