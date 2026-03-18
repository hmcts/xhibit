package uk.gov.courtservice.xhibit.business.services.counselfacilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.counsel.CounselQuery;
import uk.gov.courtservice.xhibit.business.database.query.counsel.CounselSearchQuery;
import uk.gov.courtservice.xhibit.business.database.query.counsel.DefendantSearchQuery;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.SearchCounselFacilitiesCriteria;

/**
 * <p>
 * Title: CounselFacilitiesHelper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @author Laurent Bossard
 * @author Marie Holmberg
 * @version $Revision: 1.34 $
 */
public class CounselFacilitiesHelper {
    // logger
    private static final Logger log = CSServices.getLogger(CounselFacilitiesHelper.class);

    // controllers
    private HearingScheduleControllerLocal hearingScheduleController = (HearingScheduleControllerLocal) CSServices
            .getEJBServices().createLocalSession(HearingScheduleControllerLocalHome.class);

    // The counsel query that will execute the database call.
    private CounselQuery counselQuery = new CounselQuery();

    // The counsel search query that searches for counsels signed in.
    private CounselSearchQuery counselSearchQuery = new CounselSearchQuery();

    // The counsel search query that searches for defendants
    private DefendantSearchQuery defendantSearchQuery = new DefendantSearchQuery();

    /**
     * Default constructor
     */
    public CounselFacilitiesHelper() {
    }

    /**
     * Returns a collection of PartyOnCase value objects containing all the
     * information needed to display the Assign Representatives screen.
     * 
     * @param courtId
     *            specific courtHouse ID
     * @param scheduleDate
     *            the dat which you require the list of scheduled hearings
     * @param courtRoomId
     *            Integer
     * @return Collection of PartyOnCase VO
     * @throws CounselFacilitiesControllerException
     * 
     */
    public Collection getAssignRepresentatives(Integer courtId, Date scheduleDate, Integer courtRoomId)
            throws CounselFacilitiesControllerException {
        String methodName = "*** getAssignRepresentatives(" + courtId + ", " + scheduleDate + ", " + courtRoomId
                + ") -";
        log.debug(methodName + " called. ***");

        return counselQuery.getAssignedRepresentatives(courtId, scheduleDate, courtRoomId);
    }

    /**
     * This method is used to assign legal representatives to a case and role
     * type, for a specific hearing.
     * 
     * @param shLegRepBasicValues
     *            Collection of ShLegReps
     * @throws CounselFacilitiesControllerException
     * 
     */
    public void setAssignRepresentatives(Collection shLegRepBasicValues, String userDisplayName) throws CounselFacilitiesControllerException {
        String methodName = "setAssignRepresentatives(" + shLegRepBasicValues + ") -";
        log.debug(methodName + " called.");

        try {
            // There is no longer a need for addLegalReps to included the
            // scheduledHearing ID as its contained within the
            // value object passed in.
            /**
             * @todo Remove hardcoded value once this has been propogated
             *       throughtout the layers
             */
            hearingScheduleController.addLegalReps(new Integer(1), shLegRepBasicValues, userDisplayName);

        } catch (HearingScheduleException hse) {

            CSServices.getDefaultErrorHandler().handleError(hse, getClass());
            if (hse.getUserMessageAsMessage().getParameters().length == 0) {
                throw new CounselFacilitiesControllerException(hse.getUserMessageAsMessage().getKey(),
                        hse.getMessage(), hse);
            }

            throw new CounselFacilitiesControllerException(hse.getUserMessageAsMessage().getKey(), hse
                    .getUserMessageAsMessage().getParameters(), hse.getMessage(), hse);
        }
    }

    /**
     * This will search for counsels (legal representatives) that have been
     * signed in.
     * 
     * @param criteria
     *            the user entered input set in a
     *            SearchCounselFacilitiesCriteria
     * @return Collection of PartyOnCase
     * @throws CounselFacilitiesControllerException
     * 
     */
    public Collection searchForCounsel(SearchCounselFacilitiesCriteria criteria)
            throws CounselFacilitiesControllerException {
        String methodName = "searchForCounsel(" + criteria + ") -";
        log.debug(methodName + " called.");

        Integer courtId = criteria.getCourtId();
        Date scheduleDate = criteria.getScheduleDate();
        String counselFirstName = criteria.getFirstName();
        String counselSurname = criteria.getSurname();
        Collection partyOnCase = counselSearchQuery.searchForCounsel(courtId, scheduleDate, counselFirstName,
                counselSurname);

        log.debug(methodName + " finished Returning collection of size " + partyOnCase.size());

        return partyOnCase;
    }

    /**
     * Method to search for a particular defendant in the counsel sign in. This
     * will return the defendant ant the legal representative assigned to
     * him/her.
     * 
     * @param criteria
     *            the user entered input set in a
     *            SearchCounselFacilitiesCriteria
     * @return Collection of PartyOnCaseValues.
     * @throws CounselFacilitiesControllerException
     * 
     */
    public Collection searchForDefendant(SearchCounselFacilitiesCriteria criteria)
            throws CounselFacilitiesControllerException {
        String methodName = "searchForDefendant(" + criteria + ") -";
        log.debug(methodName + " called.");

        Integer courtId = criteria.getCourtId();
        Date scheduleDate = criteria.getScheduleDate();
        String defFirstName = criteria.getFirstName();
        String defSurname = criteria.getSurname();
        Collection partyOnCase = defendantSearchQuery.searchForDefendants(courtId, scheduleDate, defFirstName,
                defSurname);

        log.debug(methodName + " finished. Returning collection of size " + partyOnCase.size());

        return partyOnCase;
    }

    /**
     * @param legalRepSignInValues
     *            collection of legalRepSignInValue
     * @throws CounselFacilitiesControllerException
     * 
     */
    public void removeSignedInLegalReps(Collection legalRepSignInValues) throws CounselFacilitiesControllerException {
        ShLegRepMaintainer maintainer = new ShLegRepMaintainer();

        Iterator it = legalRepSignInValues.iterator();
        while (it.hasNext()) {
            LegalRepSignInValue legRepSigninVal = (LegalRepSignInValue) it.next();
            Integer shid = legRepSigninVal.getShLegRepId();

            try {
                ShLegRep shLegRep = maintainer.findByPrimaryKey(shid);
                maintainer.delete(shid, shLegRep.getVersion());
            } catch (ObjectNotFoundException ex) {
                log
                        .info(
                                "Could not locate Sh Legal representative - unsure if this exception should be rethrown so logged for safety.",
                                ex);
            }
        }
    }

    public Collection getCourtRoomIds(Integer site) {
        Collection courtRooms = XhbCourtRoomBeanHelper2.findByCourtSiteId(site);
        Collection courtRoomIds = new ArrayList();
        for (Iterator i = courtRooms.iterator(); i.hasNext();) {
            XhbCourtRoom room = (XhbCourtRoom) i.next();
            Integer roomId = room.getCourtRoomId();
            log.debug("court Room id :: " + roomId);
            courtRoomIds.add(roomId);
        }
        log.debug("No of Room Ids to return :: " + courtRoomIds.size());
        return courtRoomIds;
    }

    public Collection getSiteInfo(Integer courtId) {
        Collection siteInfo = XhbCourtSiteBeanHelper2.findByCourtId(courtId);
        Collection rtnSiteDetails = new ArrayList();
        for (Iterator it = siteInfo.iterator(); it.hasNext();) {
            XhbCourtSite site = (XhbCourtSite) it.next();
            StringBuffer detail = new StringBuffer();
            detail.append(site.getCourtSiteId());
            detail.append("**");
            detail.append(site.getCourtSiteName());
            detail.append("**");
            detail.append(site.getCourtId());
            log.debug("getSiteInfo :: " + detail.toString());
            rtnSiteDetails.add(detail.toString());
        }
        return rtnSiteDetails;
    }
}