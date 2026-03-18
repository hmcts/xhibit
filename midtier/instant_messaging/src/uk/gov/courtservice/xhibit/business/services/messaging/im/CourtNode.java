package uk.gov.courtservice.xhibit.business.services.messaging.im;

import java.io.Serializable;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;

/**
 * <p>
 * Title: The Court Information in form of a tree node.
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
 * @version $Id: CourtNode.java,v 1.5 2006/06/05 12:29:12 bzjrnl Exp $
 */
public class CourtNode extends XhbCourtBasicValue implements Serializable {
    private static final Logger log = CSServices.getLogger(CourtNode.class);

    /**
     * Associated Courtsites under the Identifying Relationship with Terminal
     */
    CourtSiteNode[] courtSites;

    /** The displayed name of the node seen on the tree model. */
    String name;

    public CourtNode(Integer id) throws MessagingException {
        try {
            XhbCourt c = XhbCourtBeanHelper.findByPrimaryKey(id);
            this.name = c.getDisplayName();

            XhbCourtSiteBasicValue[] a = c.getXhbCourtSitesData();
            this.courtSites = new CourtSiteNode[a.length];
            for (int i = 0; i < a.length; i++) {
                courtSites[i] = new CourtSiteNode(a[i].getCourtSiteId());
                courtSites[i].name = a[i].getDisplayName();
            }
        } catch (ObjectNotFoundException ex) {
            log.error("Problem retrieving Court Sites for id " + id + "  : " + ex);
            throw new MessagingException("MESSAGING_CN001", "Could not retrieve Court Sites");
        }
    }

    public String getName() {
        return name;
    }

    public CourtSiteNode[] getCourtSites() {
        return courtSites;
    }
}
