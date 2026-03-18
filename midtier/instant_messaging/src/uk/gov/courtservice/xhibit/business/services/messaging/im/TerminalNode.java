package uk.gov.courtservice.xhibit.business.services.messaging.im;

import java.io.Serializable;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminal;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;

/**
 * <p>
 * Title: The Terminal Information in form of a tree node.
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
 * @version $Id: TerminalNode.java,v 1.5 2006/06/05 12:29:13 bzjrnl Exp $
 */
public class TerminalNode extends XhbTerminalBasicValue implements Serializable {
    private static final Logger log = CSServices.getLogger(TerminalNode.class);

    /**
     * The displayed name of the node seen on the tree model.
     */
    String nodeName;

    /**
     * The court under the identifying relationship between Court.
     */
    CourtNode court;

    private TerminalNode() {
    }

    public TerminalNode(Integer id) throws MessagingException {

        log.debug("TerminalNode ID = " + id.intValue());
        try {

            XhbTerminal t = XhbTerminalBeanHelper.findByPrimaryKey(id);
            log.debug("Terminal Name = " + t.getTerminalName());

            Integer courtSiteId = null;

            if (t.getCourtRoomId() != null) {

                XhbCourtRoom cr = XhbCourtRoomBeanHelper.findByPrimaryKey(t.getCourtRoomId());
                log.debug("CourtRoom Name = " + cr.getCourtRoomName());
                courtSiteId = cr.getCourtSiteId();

            } else if (t.getCourtSiteId() != null) {
                courtSiteId = t.getCourtSiteId();
            }

            XhbCourtSite cs = XhbCourtSiteBeanHelper.findByPrimaryKey(courtSiteId);

            court = new CourtNode(cs.getCourtId());

            log.debug("CourtNode Name:" + court.getName());

        } catch (ObjectNotFoundException ex) {
            log.error("Problem retrieving Court for id " + id + "  : " + ex);
            throw new MessagingException("MESSAGING_TN001", "Could not retrieve Court");
        }
    }

    public CourtNode getCourt() {
        return court;
    }
}
