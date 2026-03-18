package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplay;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDd;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query.VIPDisplayCourtRoomQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query.VIPDisplayDocumentQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.DisplayNotFoundCheckedException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.PublicDisplayCheckedException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.RotationSetNotFoundCheckedException;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.CourtSitePDComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfiguration;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationCourtRoom;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationDisplayDocument;

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
 * @author Bob Boothby
 * @version 1.0
 * @ejb.bean name="PDConfigurationController" description="Public Display
 *           Configuration Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="PDConfigurationControllerHome"
 * @ejb.transaction type="Required"
 */
public class PDConfigurationControllerBean implements SessionBean {
    private static final Logger LOGGER = CSServices.getLogger(PDConfigurationControllerBean.class);

    protected SessionContext ctx;

    private PddaHelper pddaHelper;

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbActivate() throws EJBException {
        LOGGER.debug("ejbActivate()");
        pddaHelper = new PddaHelper();
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbRemove() throws EJBException {
        LOGGER.debug("ejbRemove()");
        pddaHelper.close();
        pddaHelper = null;
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbPassivate()throws EJBException {
        LOGGER.debug("ejbPassivate()");
        pddaHelper.close();
        pddaHelper = null;
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     */
    public void setSessionContext(SessionContext ctx)throws EJBException {
        LOGGER.debug("setSessionContext(SessionContext ctx)");
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        LOGGER.debug("ejbCreate()");
        pddaHelper = new PddaHelper();
    }

    /**
     * Gets all the courts that are to be rendered for Public Displays.
     * 
     * @return an array of IDs of courts to be included in the Public Display.
     * @ejb.interface-method view-type="remote"
     */
    public int[] getCourtsForPublicDisplay() {
        Collection courts = XhbCourtBeanHelper.findAll();
        int[] courtArray = new int[courts.size()];
        Iterator courtIterator = courts.iterator();
        for (int i = 0; i < courtArray.length; i++) {
            courtArray[i] = ((XhbCourt) courtIterator.next()).getCourtId().intValue();
        }
        return courtArray;
    }

    /**
     * Gets the full set of public display configuration data for a given court.
     * 
     * @param courtId
     *            The court for which to get the configuration information.
     * @return An array of <code>DisplayRotationSetData</code>, one for every
     *         display in the court.
     * @throws CourtNotFoundException
     *             When the court ID passed in is not valid.
     * @ejb.interface-method view-type="remote"
     */
    public DisplayRotationSetData[] getCourtConfiguration(int courtId)
            throws uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException {
        try {
            XhbCourt court = XhbCourtBeanHelper.findByPrimaryKey(new Integer(courtId));
            return DisplayRotationSetDataHelper.getDataForCourt(courtId, court);
        } catch (ObjectNotFoundException ex) {
            throw new uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException(
                    "Court not found with id: " + courtId, ex);
        }
    }

    /**
     * Gets the configuration data for all displays using the rotation set.
     * 
     * @param courtId
     *            The court that the rotation set belongs to.
     * @param rotationSetId
     *            The rotation set for which to get the data.
     * @return An array of <code>DisplayRotationSetData</code>, one for every
     *         display using the rotation set.
     * @ejb.interface-method view-type="remote"
     */
    public DisplayRotationSetData[] getUpdatedRotationSet(int courtId, int rotationSetId) {
        DisplayRotationSetData[] returnArray = null;
        try {
            XhbRotationSet rotationSet = XhbRotationSetBeanHelper.findByPrimaryKey(new Integer(rotationSetId));
            returnArray = DisplayRotationSetDataHelper.getDataForDisplayRotationSets(courtId, rotationSet);
        } catch (ObjectNotFoundException ex) {
            returnArray = new DisplayRotationSetData[0];
        }
        return returnArray;
    }

    /**
     * Gets the configuration data for a given display in a court.
     * 
     * @param courtId
     *            The court that the rotation set belongs to.
     * @param displayId
     *            The display for which to get the data
     * @return A <code>DisplayRotationSetData</code> array representing the
     *         configuration of the display. If no display exists of this ID
     *         then it returns a zero length array.
     * @ejb.interface-method view-type="remote"
     */
    public DisplayRotationSetData[] getUpdatedDisplay(int courtId, int displayId) {
        DisplayRotationSetData[] returnArray = null;
        try {
            XhbDisplay display = XhbDisplayBeanHelper.findByPrimaryKey(new Integer(displayId));
            XhbRotationSet rotationSet = display.getXhbRotationSet();
            DisplayRotationSetData displayRotationSetData = DisplayRotationSetDataHelper.getDisplayRotationSetData(
                    courtId, display, rotationSet);
            if (displayRotationSetData != null) {
                returnArray = new DisplayRotationSetData[] { displayRotationSetData };
            } else {
                returnArray = new DisplayRotationSetData[0];
            }
        } catch (ObjectNotFoundException ex) {
            returnArray = new DisplayRotationSetData[0];
        }
        return returnArray;
    }

    /**
     * Returns the Court Sites, Locations within the Site and Displays within
     * the Site.
     * 
     * @param courtId
     *            The court being maintained
     * @return Details of site, location and screen within a court
     * @ejb.interface-method view-type="remote"
     */
    public CourtSitePDComplexValue[] getDisplaysForCourt(Integer courtId) {
        return DisplayLocationDataHelper.getDisplaysForCourt(courtId);
    }

    /**
     * The Rotation sets, pages within each set and the screens the rotation
     * sets are assigned to are returned.
     * 
     * @param courtId
     *            The court the display is in.
     * @return Array of objects containing the rotation set, pages and screens
     *         rotation set is assigned to
     * @ejb.interface-method view-type="remote"
     */
    public RotationSetComplexValue[] getRotationSetsDetailForCourt(Integer courtId, Locale locale) {
        return DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale);
    }

    /**
     * Returns the list of RotationSets available in the court
     * 
     * @param courtId
     *            The court to which the rotation set belongs
     * @return The rotation sets within a court
     * @ejb.interface-method view-type="remote"
     */
    public XhbRotationSetBasicValue[] getRotationSetsForCourt(Integer courtId) {
        return XhbRotationSetBeanHelper.findByCourtIdValue(courtId);
    }

    /**
     * Returns the list of display documents
     * 
     * @return All Display Documents
     * @ejb.interface-method view-type="remote"
     */
    public XhbDisplayDocumentBasicValue[] getDisplayDocuments() {
        return XhbDisplayDocumentBeanHelper.findAllValue();
    }

    /**
     * Returns a rotation set with an array of the display documents that are
     * assigned in to the rotation set.
     * 
     * @param rotationSetId
     *            The rotation set being queried
     * @return RotationSetComplexValue
     * @ejb.interface-method view-type="remote"
     */
    public RotationSetComplexValue getRotationSet(Integer rotationSetId) throws RotationSetNotFoundCheckedException {
        RotationSetComplexValue returnValue = new RotationSetComplexValue();
        ArrayList results = new ArrayList();
        RotationSetDDComplexValue ddComplex;

        XhbRotationSet rotationSetLocal = null;
        try {
            rotationSetLocal = XhbRotationSetBeanHelper.findByPrimaryKey(rotationSetId);
        } catch (ObjectNotFoundException ex) {
            throw new RotationSetNotFoundCheckedException(rotationSetId, ex);
        }
        returnValue.setRotationSetBasicValue(rotationSetLocal.getData());

        Collection rotationSetDDCol = rotationSetLocal.getXhbRotationSetDds();
        Iterator rotationSetDDIter = rotationSetDDCol.iterator();
        while (rotationSetDDIter.hasNext()) {
            XhbRotationSetDd rotationSetDDLocal = (XhbRotationSetDd) rotationSetDDIter.next();
            ddComplex = new RotationSetDDComplexValue(rotationSetDDLocal.getData(), rotationSetDDLocal
                    .getXhbDisplayDocumentData());
            results.add(ddComplex);
        }
        returnValue.setRotationSetDDComplexValues((RotationSetDDComplexValue[]) results
                .toArray(new RotationSetDDComplexValue[results.size()]));
        return returnValue;
    }

    /**
     * Creates a new rotation set with associated documents
     * 
     * @param newRotationSet
     *            This object must contain a RotationSetBasic value with court
     *            Id populated and a list of RotationSetDDComplex values with
     *            RotationSetDDBasicValues and valid DisplayDocumentBasicValues
     * @ejb.interface-method view-type="remote"
     */
    public void createRotationSets(RotationSetComplexValue newRotationSet) {
        RotationSetMaintainHelper.createRotationSets(newRotationSet);
    }

    /**
     * Delete a rotation set with associated documents Note the rotation set
     * must not be assigned to any displays.
     * 
     * @param rotationSet
     *            The rotation set to be deleted
     * @ejb.interface-method view-type="remote"
     */
    public void deleteRotationSets(RotationSetComplexValue rotationSet) throws OptimisticLockException,
            PublicDisplayCheckedException {
        RotationSetMaintainHelper.deleteRotationSet(rotationSet);
    }

    /**
     * Updates the rotation set with display documents that have been selected
     * <p/> Note: sends a RotationSet changed JMS configuration message
     * 
     * @param rotationSet
     *            The rotation set being updated and an array of display
     *            documents with ordering and delay information
     * @throws PublicDisplayCheckedException
     *             Thrown if rotation set does not exist in the DB
     * @ejb.interface-method view-type="remote"
     */
    public void setDisplayDocumentsForRotationSet(RotationSetComplexValue rotationSet, String userDisplayName)
            throws PublicDisplayCheckedException {
        RotationSetMaintainHelper.setDisplayDocumentsForRotationSet(rotationSet, pddaHelper, userDisplayName);
    }

    /**
     * Returns the display, the rotations set assigned and an array of court
     * rooms assigned.
     * 
     * @param displayId
     *            the display (ie physical screen) being queried
     * @return An object containing details of the display, the rotation set and
     *         the courtrooms assigned
     * @ejb.interface-method view-type="remote"
     */
    public DisplayConfiguration getDisplayConfiguration(Integer displayId) {
        return DisplayConfigurationHelper.getDisplayConfiguration(displayId);
    }

    /**
     * Updates the display configuration with changes <p/> Note: sends a
     * DisplayConfigurationChanged JMS configuration message
     * 
     * @param displayConfiguration
     *            The updated display configuration to be stored
     * @ejb.interface-method view-type="remote"
     */
    public void updateDisplayConfiguration(DisplayConfiguration displayConfiguration, String userDisplayName)
            throws RotationSetNotFoundCheckedException, DisplayNotFoundCheckedException {
        DisplayConfigurationHelper.updateDisplayConfiguration(displayConfiguration, pddaHelper, userDisplayName);
    }

    /**
     * Requests that the rotation set and display documents within the rotation
     * set are all re-rendered from scratch. <p/> Note: sends a
     * RenderEntireDisplayRotationSet JMS configuration message
     * 
     * @param displayId
     *            the display that will have its pages and rotation set
     *            re-rendered
     * @ejb.interface-method view-type="remote"
     */
    public void initialiseDisplay(Integer courtId, Integer displayId, String userDisplayName) {
    	
    	try {
	    	CourtMaintainer courtMaintainer = new CourtMaintainer();
			String courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
	        CourtConfigurationChange ccc = new CourtDisplayConfigurationChange(courtId.intValue(), 
	        		courtName, displayId.intValue(), true);
	        ConfigurationChangeEvent ccEvent = new ConfigurationChangeEvent(ccc);
	        LOGGER.debug("Sending Display initialisation message.");
	        pddaHelper.sendMessage(ccEvent, userDisplayName);
	        LOGGER.debug("Display initialisation message sent.");
    	} catch (ObjectNotFoundException e) {
			LOGGER.error("Cannot find the court site name.");
			e.printStackTrace();
		}
    }

    /**
     * Requests that all rotation sets and display documents are all re-rendered
     * from scratch. <p/> Note: sends a RenderEntireCourt JMS configuration
     * message
     * 
     * @param courtId
     *            the court to be completely re-rendered.
     * @ejb.interface-method view-type="remote"
     */
    public void initialiseCourt(Integer courtId, String userDisplayName) {
    	try {
	    	CourtMaintainer courtMaintainer = new CourtMaintainer();
			String courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
	        CourtConfigurationChange ccc = new CourtConfigurationChange(courtId.intValue(), courtName, true);
	        ConfigurationChangeEvent ccEvent = new ConfigurationChangeEvent(ccc);
	        LOGGER.debug("Sending Court initialisation message.");
	        pddaHelper.sendMessage(ccEvent, userDisplayName);
	        LOGGER.debug("Court initialisation message sent.");
    	} catch (ObjectNotFoundException e) {
			LOGGER.error("Cannot find the court site name.");
			e.printStackTrace();
		}
    }

    /**
     * Requests all courtrooms for a court house
     * 
     * @param courtId
     *            the court house
     * @return array of XhbCourtRoomBasicValue
     * @ejb.interface-method view-type="remote"
     */
    public XhbCourtRoomBasicValue[] getCourtRoomsForCourt(Integer courtId) {
        ArrayList al = new ArrayList();
        try {
            Collection courtSites = XhbCourtBeanHelper.findByPrimaryKey(courtId).getXhbCourtSites();
            int numCourtSites = courtSites.size();
            Iterator iter = courtSites.iterator();
            while (iter.hasNext()) {
                XhbCourtSite site = (XhbCourtSite) iter.next();
                XhbCourtRoomBasicValue[] courtRooms;
                if (numCourtSites > 1) {
                    courtRooms = site.getMultiSiteXhbCourtRoomsData();
                } else {
                    courtRooms = site.getXhbCourtRoomsData();
                }
                for (int i = 0; i < courtRooms.length; i++) {
                    al.add(courtRooms[i]);
                }
            }
        } catch (ObjectNotFoundException ex) {
            throw new CourtNotFoundException(courtId, ex);
        }
        return (XhbCourtRoomBasicValue[]) al.toArray(new XhbCourtRoomBasicValue[al.size()]);
    }

    /**
     * Requests courtrooms assigned to the VIP screen for a court house If none
     * are found, falls back to returning all court rooms.
     * 
     * @param courtId
     *            the court house
     * @return array of XhbCourtRoomBasicValue
     * @ejb.interface-method view-type="remote"
     */
    public XhbCourtRoomBasicValue[] getVipCourtRoomsForCourt(Integer courtId) {
        boolean multiSite = false;
        try {
            Collection courtSites = XhbCourtBeanHelper.findByPrimaryKey(courtId).getXhbCourtSites();
            multiSite = (courtSites.size() > 1);
        } catch (ObjectNotFoundException ex) {
            throw new CourtNotFoundException(courtId, ex);
        }

        VipCourtRoomsQuery query = new VipCourtRoomsQuery(multiSite);
        XhbCourtRoomBasicValue[] results = query.getData(courtId);
        if (results.length > 0) {
            return results;
        } else {
            return getCourtRoomsForCourt(courtId);
        }
    }

    /**
     * Returns a composite value object containing display document, court room
     * and unassigned cases information for the court site.
     * 
     * @return VIPDisplayConfiguration
     * @ejb.interface-method view-type="remote"
     */
    public VIPDisplayConfiguration getVIPDisplayConfiguration(Integer courtSiteId) {
        VIPDisplayConfigurationCourtRoom[] courtRoomArray = null;
        VIPDisplayConfigurationDisplayDocument[] displayDocArray = null;

        // Retrieve display documents information for court site VIP
        VIPDisplayDocumentQuery vipDisplayDocumentQuery = new VIPDisplayDocumentQuery();
        Collection vipDisplayDocumentCol = vipDisplayDocumentQuery.getData(courtSiteId);
        if (vipDisplayDocumentCol != null) {
            displayDocArray = new VIPDisplayConfigurationDisplayDocument[vipDisplayDocumentCol.size()];
            vipDisplayDocumentCol.toArray(displayDocArray);
        }

        // Retrive assigned court room information for court site VIP
        VIPDisplayCourtRoomQuery vipDisplayCourtRoomQuery = new VIPDisplayCourtRoomQuery();
        Collection vipDisplayCourtRoomCol = vipDisplayCourtRoomQuery.getData(courtSiteId);
        if (vipDisplayCourtRoomCol != null) {
            courtRoomArray = new VIPDisplayConfigurationCourtRoom[vipDisplayCourtRoomCol.size()];
            vipDisplayCourtRoomCol.toArray(courtRoomArray);
        }

        // create a composite value of display documents,
        // court rooms and unassigned information for the Court site VIP.
        VIPDisplayConfiguration vipConfiguration = new VIPDisplayConfiguration(displayDocArray, courtRoomArray,
                vipDisplayCourtRoomQuery.isShowUnassignedCases());

        return vipConfiguration;
    }

    /**
     * Check the Public display activation status for this particular scheduled
     * hearing.
     * 
     * @param schedHearingId
     * @return boolean true if scheduled hearing is active
     * @ejb.interface-method view-type="remote"
     */
    public boolean isPublicDisplayActive(Integer schedHearingId)

    {
        return PublicDisplayActivationHelper.isPublicDisplayActive(schedHearingId);
    }

    /**
     * Sets the public display for this scheduling hearing to Activate.
     * 
     * @param schedHearingId
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void activatePublicDisplay(Integer schedHearingId, Date activationDate, String userDisplayName)

    {
        PublicDisplayActivationHelper
                .activatePublicDisplay(pddaHelper, schedHearingId, activationDate, true, userDisplayName);
    }

    /**
     * Sets the public display for this scheduling hearing to deActivate.
     * 
     * @param schedHearingId
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void deActivatePublicDisplay(Integer schedHearingId, Date deactivationDate, String userDisplayName)

    {
        PublicDisplayActivationHelper.activatePublicDisplay(pddaHelper, schedHearingId, deactivationDate,
                false, userDisplayName);
    }
}