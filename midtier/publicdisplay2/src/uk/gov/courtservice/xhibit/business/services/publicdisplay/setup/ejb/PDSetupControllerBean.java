package uk.gov.courtservice.xhibit.business.services.publicdisplay.setup.ejb;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplay;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_location.XhbDisplayLocation;
import uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown.CourtDrillDown;
import uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown.CourtSiteDrillDown;
import uk.gov.courtservice.xhibit.common.publicdisplay.setup.drilldown.DisplayLocationDrillDown;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;

/**
 * <p>
 * Title:
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
 * @author Neil Ellis
 * @version $Revision: 1.8 $
 * @ejb.bean name="PDSetupController" description="Public Display Setup
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="PDSetupControllerHome"
 * @ejb.transaction type="Required"
 */
public class PDSetupControllerBean implements SessionBean {
    private static final Logger log = CSServices.getLogger(PDSetupControllerBean.class);

    protected SessionContext ctx;

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbActivate() {
        log.debug("ejbActivate()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbRemove() {
        log.debug("ejbRemove()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbPassivate() {
        log.debug("ejbPassivate()");
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     */
    public void setSessionContext(SessionContext ctx) {
        log.debug("setSessionContext(SessionContext ctx)");
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        log.debug("ejbCreate()");

    }

    /**
     * TODO: Doucment and clean this up- Neil Ellis
     * 
     * @post return != null
     * @ejb.interface-method view-type="remote"
     */
    public CourtDrillDown getDrillDownForCourt(Integer courtId)
            throws uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException {
        XhbCourt court = null;
        try {
            court = XhbCourtBeanHelper.findByPrimaryKey(courtId);
        } catch (ObjectNotFoundException e) {
            throw new uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException(courtId, e);
        }
        Collection xhbCourtSites = court.getXhbCourtSites();
        CourtDrillDown courtDrillDown = new CourtDrillDown(court.getDisplayName());
        for (Iterator iterator = xhbCourtSites.iterator(); iterator.hasNext();) {
            XhbCourtSite xhbCourtSite = (XhbCourtSite) iterator.next();
            Collection xhbDisplayLocations = xhbCourtSite.getXhbDisplayLocations();
            CourtSiteDrillDown courtSiteDrillDown = new CourtSiteDrillDown(xhbCourtSite.getDisplayName());
            for (Iterator iterator1 = xhbDisplayLocations.iterator(); iterator1.hasNext();) {
                XhbDisplayLocation xhbDisplayLocation = (XhbDisplayLocation) iterator1.next();
                Collection xhbDisplays = xhbDisplayLocation.getXhbDisplays();
                DisplayLocationDrillDown displayLocationDrillDown = new DisplayLocationDrillDown(xhbDisplayLocation
                        .getDescriptionCode());
                for (Iterator iterator2 = xhbDisplays.iterator(); iterator2.hasNext();) {
                    XhbDisplay xhbDisplay = (XhbDisplay) iterator2.next();
                    DisplayURI displayURI = new DisplayURI(court.getShortName().toLowerCase(), xhbCourtSite
                            .getCourtSiteCode().toLowerCase(), xhbDisplayLocation.getDescriptionCode().toLowerCase(),
                            xhbDisplay.getDescriptionCode().toLowerCase());
                    displayLocationDrillDown.addDisplay(displayURI);
                    if (log.isDebugEnabled()) {
                        log.debug("Drilldown added for URI '" + displayURI);
                    }
                }
                courtSiteDrillDown.addCourtRoom(displayLocationDrillDown);

            }
            courtDrillDown.addCourtSite(courtSiteDrillDown);
        }
        return courtDrillDown;

    }

    /**
     * todo: document this - Neil Ellis
     * 
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public XhbCourtBasicValue[] getAllCourts() {
        XhbCourtBasicValue[] allValue = XhbCourtBeanHelper.findAllValue();
        return allValue;
    }

}