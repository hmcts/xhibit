package uk.gov.courtservice.xhibit.business.services.witness;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtHome;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingHome;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeHome;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbCaseHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbCaseValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSession;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCaseStatusUnassignedCasesQuery;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.AllCourtStatusQuery;
import uk.gov.courtservice.xhibit.business.services.userterminal.TerminalNotFoundException;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerLocal;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.viewschedule.ViewScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.viewschedule.ViewScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoDirectionsForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TerminalFindException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessCreationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessDetailImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers.CaseDurationHelper;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers.TrialSessionHelper;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers.WitnessHelper;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;

/**
 * <p>
 * Title: The Witness Stateless Session EJB.
 * </p>
 * <p>
 * Description: <p/>
 * <p>
 * See: <b><a href="http://gbspsiad002:8888/Xhibit/546">Witness Swiki Page</a></b>
 * </p>
 * <br/> <p/> This is the Stateless Session Bean that provides core business
 * services required for the Witness Facilities part of XHIBIT .
 * </p>
 * 
 * @author Neil Ellis
 * @ejb.bean name="WitnessController" description="Witness Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="WitnessControllerHome"
 * @ejb.transaction type="Required"
 *                        <p>
 *                        Copyright: Copyright (c) 2003
 *                        </p>
 *                        <p>
 *                        Company: EDS
 *                        </p>
 */
public class WitnessControllerBean extends CSSessionBean implements SessionBean {
    protected static final float NO_DURATION_FOR_CASE_DUMMY_VALUE = 0.0f;

    // MH
    private ViewScheduleControllerLocal viewScheduleController;

    private HearingScheduleControllerLocal hearingScheduleController;

    private UserTerminalControllerLocal userTerminalController;

    private WitnessHelper witnessDetailHelper = new WitnessHelper();

    // home intefaces to entities
    private DefendantOnCaseHome defendantOnCaseHome;

    private RefHearingTypeHome refHearingTypeHome;

    private CourtHome courtHome;

    private CaseHome caseHome;

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();

        hearingScheduleController = (HearingScheduleControllerLocal) CSServices.getEJBServices().createLocalSession(
                HearingScheduleControllerLocalHome.class);
        userTerminalController = (UserTerminalControllerLocal) CSServices.getEJBServices().createLocalSession(
                UserTerminalControllerLocalHome.class);
        defendantOnCaseHome = (DefendantOnCaseHome) CSServices.getServiceLocator().getLocalHome(
                DefendantOnCaseHome.class);
        refHearingTypeHome = (RefHearingTypeHome) CSServices.getServiceLocator().getLocalHome(RefHearingTypeHome.class);
        courtHome = (CourtHome) CSServices.getServiceLocator().getLocalHome(CourtHome.class);
        caseHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);

        // MH
        viewScheduleController = (ViewScheduleControllerLocal) CSServices.getEJBServices().createLocalSession(
                ViewScheduleControllerLocalHome.class);

        log.debug("Exiting ejbCreate");
    }

    /**
     * @param caseId
     * @return the names of judges for a case: get the last PAD judge
     * @ejb.interface-method view-type="remote"
     */
    public String getJudgeForCase(final Integer caseId, String userDisplayName) throws NoJudgeForCaseException {
        try {
            Context ctx = new InitialContext();
            HearingHome home = (HearingHome) ctx.lookup("HearingHome");
            Collection hearings = home.findByCaseId(caseId);
            Iterator hit = hearings.iterator();
            Timestamp lastPADTime = null;
            Integer lastPADShid = null;

            while (hit.hasNext()) {
                Hearing hearing = (Hearing) hit.next();
                Integer refHearingTypeId = hearing.getRefHearingTypeId();
                RefHearingType refHearingType = refHearingTypeHome.findByPrimaryKey(refHearingTypeId);
                String code = refHearingType.getHearingTypeCode();
                Collection schedHearingCol = hearing.getScheduledHearings();
                Iterator sit = schedHearingCol.iterator();
                Timestamp latestTime = null;
                Integer latestShid = null;

                // get the latest hearing time for this type of hearing
                while (sit.hasNext()) {
                    ScheduledHearing schedHearing = (ScheduledHearing) sit.next();
                    Sitting sitting = schedHearing.getSitting();
                    Timestamp ts = sitting.getSittingTime();

                    // ts can be null from the sitting table
                    if (ts != null) {
                        if (latestTime == null || ts.after(latestTime)) {
                            latestTime = ts;
                            latestShid = schedHearing.getScheduledHearingId();
                        }
                    }
                    // ensure latesShid alway has a value
                    else if (latestShid == null) {
                        latestShid = schedHearing.getScheduledHearingId();
                    }
                }

                // put a default time in incase no pads are found
                if (lastPADTime == null) {
                    lastPADTime = latestTime;
                    lastPADShid = latestShid;
                }

                // get the last PAD hearing
                if (code.equalsIgnoreCase("PAD") && latestTime.after(lastPADTime)) {
                    lastPADTime = latestTime;
                    lastPADShid = latestShid;
                }
            }

            Iterator judgeNames = getJudgeNames(lastPADShid, userDisplayName).iterator();
            if (judgeNames.hasNext()) {
                StringBuffer buffer = new StringBuffer((String) judgeNames.next());
                while (judgeNames.hasNext()) {
                    buffer.append(" ");
                    buffer.append((String) judgeNames.next());
                }

                return buffer.toString();
            }
        } catch (NamingException e) {
            throw new NoJudgeForCaseException("WITNESS_XXX", "Could not find judge for case:" + caseId, e);
        } catch (FinderException e) {
            throw new NoJudgeForCaseException("WITNESS_XXX", "Could not find judge for case:" + caseId, e);
        }

        throw new NoJudgeForCaseException("WITNESS_XXX", "Could not find judge for case:" + caseId);
    }

    /**
     * Get the names of judges for a scheduled hearing
     * 
     * @param scheduledHearingId
     *            the scheduled hearing to get the judges for
     * @reuturn a collection of the names of judges for a hearing
     */
    private Collection getJudgeNames(Integer scheduledHearingId, String userDisplayName) {
        try {
            List judgeNameList = new ArrayList();

            Iterator attendees = hearingScheduleController.getAttendees(scheduledHearingId, userDisplayName).iterator();
            while (attendees.hasNext()) {
                PersonValue attendee = (PersonValue) attendees.next();

                if (attendee.getPersonType().equals(PersonValue.JUDGE)) {
                    judgeNameList.add(attendee.getFullName());
                }
            }

            return judgeNameList;
        } catch (HearingScheduleException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Retrieves a Witness Detail object keyed by the witness primary key.
     * 
     * @param witnessId
     *            the primary key for the witness table.
     * @return a WitnessDetail.
     * @throws WitnessNotFoundException
     *             if there is no such witness for this id.
     * @ejb.interface-method view-type="remote"
     */
    public WitnessDetail getWitnessDetail(final Integer witnessId) throws WitnessNotFoundException {
        return WitnessHelper.getWitnessDetail(witnessId);
    }

    /**
     * Updates the witness details on the database from the value object
     * supplied.
     * 
     * @param detail
     * @throws WitnessNotFoundException
     * @ejb.interface-method view-type="remote"
     */
    public WitnessDetail updateWitnessDetail(final WitnessDetail detail) throws WitnessNotFoundException {
        return WitnessHelper.updateWitnessDetail(detail);
    }

    /**
     * Updates the witness details on the database from the value object
     * supplied.
     * 
     * @param session
     * @throws WitnessNotFoundException
     * @ejb.interface-method view-type="remote"
     */
    public WitnessDetail updateWitnessSession(final WitnessSession session) throws WitnessModificationException {
        return witnessDetailHelper.updateWitnessSession(session);
    }

    /**
     * Retrieves the case details for the specified case. The case details are a
     * subset of the data supplied on the XHB_CASE table.
     * 
     * @param caseId
     *            the primary key of the XHB_CASE table.
     * @param caseId
     * @param sessionId
     * @param witnessFullname
     * @param witnessType
     * @param age
     * @param expectedArrivalTime
     * @param notes
     * @return
     * @throws CaseNotFoundException
     * @throws WitnessCreationException
     * @throws
     *             uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @throws SkeletonSessionNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public WitnessSession createWitnessSession(final Integer caseId, final Integer sessionId,
            final String witnessFullname, final String witnessType, final String witnessStatus, final int age,
            final Time expectedArrivalTime, final String notes) throws WitnessCreationException, CaseNotFoundException,
            SkeletonSessionNotFoundException {

        XhbSkeletonSession skeleton = null;
        XhbCase theCase = null;
        try {
            skeleton = XhbSkeletonSessionHelper.getLocalHome().findByPrimaryKey(sessionId);
        } catch (FinderException e) {
            log.error(e);
            throw new SkeletonSessionNotFoundException("WITNESS_XXX", "No session matching " + sessionId + " found.", e);
        }
        try {
            theCase = XhbCaseHelper.getLocalHome().findByPrimaryKey(caseId);
        } catch (FinderException e) {
            log.error(e);
            throw new CaseNotFoundException("WITNESS_XXX", "Could not find case: " + caseId, e);
        }
        XhbWitness witness = null;
        try {
            witness = XhbWitnessHelper.getLocalHome().create(witnessFullname, witnessType, expectedArrivalTime, null,
                    null, null, null, new Integer(0), skeleton, theCase);
            witness.setStatus(witnessStatus);
            witness.setAge((short) age);
            witness.setNotes(notes);
        } catch (CreateException e) {
            log.error(e);
            throw new WitnessCreationException("WITNESS_XXX", "Could not create witness.", e);
        }
        // todo: sort out this nasty little hack. This is to get round
        // optimistic locking problems.
        XhbWitnessValue xhbWitnessValue = witness.getData();
        // todo: this is a nasty hack which gets around the fact that the
        // transaction is not yet commited
        // todo: so the before insert trigger has not been executed which
        // updates the version.
        xhbWitnessValue.setVersion(new Integer(xhbWitnessValue.getVersion().intValue() + 1));
        return new WitnessSessionImpl(xhbWitnessValue);
    }

    /**
     * @param detail
     * @throws WitnessNotFoundException
     * @ejb.interface-method view-type="remote"
     */
    public void removeWitnessDetail(final WitnessDetail detail) throws WitnessNotFoundException {
        try {
            XhbWitnessHelper.remove(((WitnessDetailImpl) detail).getWitnessValue());
        } catch (javax.ejb.RemoveException e) {
            log.error(e);
            throw new CSUnrecoverableException("Could not remove witness:" + detail.getName(), e);
        } catch (FinderException e) {
            log.error(e);
            throw new WitnessNotFoundException("", "", e);
        }
    }

    /**
     * Retrieves the case details for the specified case. The case details are a
     * subset of the data supplied on the XHB_CASE table.
     * 
     * @param caseId
     *            the primary key of the XHB_CASE table.
     * @return an object which complies with the CaseDetail interface.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetails(final Integer caseId) throws CaseNotFoundException {
        try {
            final String[] defStrings = getDefendantsAsNames(caseId);
            return CaseDetailImpl.createCaseDetailValue((XhbCaseHelper.findByPrimaryKey(caseId)), defStrings);
        } catch (FinderException e) {
            log.error(e);
            throw new CaseNotFoundException("WITNESS_XXX", "Could not find case: " + caseId, e);
        }
    }

    /**
     * Retrieves the case details for the specified case. The case details are a
     * subset of the data supplied on the XHB_CASE table. <p/> Theorhetically
     * there could be multiple cases for a given case number but for the moment
     * we're only returning the first.
     * 
     * @param caseNumber
     *            the case number.
     * @return an object which complies with the CaseDetail interface.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetailsByCaseNumber(final Integer caseNumber) throws CaseNotFoundException {
        try {
            XhbCaseValue[] cv = XhbCaseHelper.findByCaseNumber(caseNumber.intValue());
            if (cv.length > 0) {
                final String[] defStrings = getDefendantsAsNames(cv[0].getCaseId());
                return CaseDetailImpl.createCaseDetailValue(cv[0], defStrings);
            }

            return null;
        } catch (FinderException e) {
            log.error(e);
            throw new CaseNotFoundException("WITNESS_XXX", "Could not find case winth number: " + caseNumber, e);
        }
    }

    /**
     * Retrieves the case details for the specified case and court. The case
     * details are a subset of the data supplied on the XHB_CASE table. <p/>
     * 
     * @param caseNumber
     *            the case number.
     * @param courtId
     *            the court id.
     * @return an object which complies with the CaseDetail interface.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetailsByCaseNumberAndCourtId(final Integer caseNumber, final Integer courtId)
            throws CaseNotFoundException {
        try {
            XhbCaseValue[] cv = XhbCaseHelper.findByCaseAndCourt(caseNumber, courtId);
            if (cv.length > 0) {
                final String[] defStrings = getDefendantsAsNames(cv[0].getCaseId());
                return CaseDetailImpl.createCaseDetailValue(cv[0], defStrings);
            }

            return null;
        } catch (FinderException e) {
            log.error(e);
            throw new CaseNotFoundException("WITNESS_XXX", "Could not find case winth number: " + caseNumber, e);
        }
    }

    /**
     * Retrieves the case details for the specified case, type and court. The
     * case details are a subset of the data supplied on the XHB_CASE table.
     * <p/>
     * 
     * @param caseNumber
     *            the case number.
     * @param caseType
     *            the case type.
     * @param courtId
     *            the court id.
     * @return an object which complies with the CaseDetail interface.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetailsByCaseNumberAndTypeAndCourtId(final Integer caseNumber, final String caseType,
            final Integer courtId) throws CaseNotFoundException {
        try {
            XhbCaseValue[] cv = XhbCaseHelper.findByCaseAndTypeAndCourt(caseNumber, caseType, courtId);
            if (cv.length > 0) {
                final String[] defStrings = getDefendantsAsNames(cv[0].getCaseId());
                return CaseDetailImpl.createCaseDetailValue(cv[0], defStrings);
            }

            return null;
        } catch (FinderException e) {
            log.error(e);
            throw new CaseNotFoundException("WITNESS_XXX", "Could not find case with number: " + caseNumber, e);
        }
    }

    /**
     * @param caseRef
     * @param courtId
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetailsByCaseRefAndCourtId(final String caseRef, final Integer courtId)
            throws CaseNotFoundException {
        final String type = caseRef.substring(0, 1);
        final String caseStr = caseRef.substring(1);
        final Integer caseNumber = new Integer(caseStr);
        return getCaseDetailsByCaseNumberAndTypeAndCourtId(caseNumber, type, courtId);
    }

    /**
     * @param caseRef
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CaseDetail getCaseDetailsByCaseRef(final String caseRef) throws CaseNotFoundException {
        // final String type=caseRef.substring(0,1);
        final String caseStr = caseRef.substring(1);
        final Integer caseNumber = new Integer(caseStr);
        return getCaseDetailsByCaseNumber(caseNumber);
    }

    /**
     * @param caseDetail
     * @throws CaseModificationException
     * @ejb.interface-method view-type="remote"
     */
    public void updateCaseDetail(final CaseDetail caseDetail) throws CaseModificationException {
        try {
            XhbCaseHelper.update(((CaseDetailImpl) caseDetail).getCase());
        } catch (FinderException e) {
            log.error(e);
            throw new CaseModificationException("WITNESS_XXX", "Could not update case: " + caseDetail.getCaseNumber(),
                    e);
        }
    }

    /**
     * Returns a DailyList object from which you can retrive an Array of
     * hearing, defendant or court centric entries.
     * 
     * @param courtId
     *            The courtId used to obtain the daily list. This value can be
     *            obtained through getCourtIdByTerminalName().
     * @param date
     *            The date to obtain the list for. Mostly this will be today.
     * @return a DailyList which holds the application logic for obtaining the
     *         hearings, defendants, courts etc.
     * @ejb.interface-method
     */
    public DailyListWithWitness[] getDailyList(final Integer courtId, final Date date) {
        return viewScheduleController.getDailyListWithWitness(courtId, date);
    }

    /**
     * @ejb.interface-method
     */
    public DailyList[] getDailyListByDefendant(final Integer courtId, final Date date) {
        return viewScheduleController.getDailyListByDefendant(courtId, date);
    }

    /**
     * Returns a collection of AllCourtStatus value which holds information for
     * each court room
     * 
     * @param courtId -
     *            used to determine court site/room status
     * @param date -
     *            today's date (used to get court log information)
     * @return - collection of information for the court site - case,
     *         defendants, courtlog etc. a DailyList which holds the application
     *         logic for obtaining the
     * @ejb.interface-method
     */
    public Collection getAllCourtStatus(final int courtId, final Date date) {
        final int[] rooms = getCourtRoomIds(courtId);
        return new AllCourtStatusQuery().getData(date, courtId, rooms);
    }

    /**
     * Returns a collection of AllCaseStatus value which holds information for
     * each court room
     * 
     * @param courtId -
     *            used to determine court site/room status
     * @param date -
     *            today's date (used to get court log information)
     * @return - collection of information for the court site - case,
     *         defendants, courtlog etc. a DailyList which holds the application
     *         logic for obtaining the
     * @ejb.interface-method
     */
    public Collection getAllCaseStatus(final int courtId, final Date date) {
        final int[] rooms = getCourtRoomIds(courtId);
        return new AllCaseStatusUnassignedCasesQuery().getData(date, courtId, rooms);
    }

    /**
     * Private method to return all the court room id's for the given court id
     */
    private int[] getCourtRoomIds(final int courtId) {
        ArrayList rooms = new ArrayList();
        try {
            // get the court entity for the given court id
            Court court = courtHome.findByPrimaryKey(new Integer(courtId));
            // get the court sites associated with the court
            Collection courtSitesForCourt = court.getCourtSites();

            for (Iterator sites = courtSitesForCourt.iterator(); sites.hasNext();) {
                CourtSite courtSite = (CourtSite) sites.next();

                // get the court rooms for the court site
                Collection courtRoomsForSite = courtSite.getCourtRooms();
                for (Iterator CourtRooms = courtRoomsForSite.iterator(); CourtRooms.hasNext();) {
                    CourtRoom courtRoom = (CourtRoom) CourtRooms.next();
                    // add the court room id to array
                    rooms.add(courtRoom.getCourtRoomId());
                }
            }
            // loop arraylist and return roomid's
            int[] roomids = new int[rooms.size()];
            for (int i = 0; i < rooms.size(); i++) {
                Integer room = (Integer) rooms.get(i);
                roomids[i] = room.intValue();
            }
            return roomids;

        } catch (FinderException e) {
            // do nothing
        }
        return null;
    }

    /**
     * This is a convenience method to convert a terminal name to a court id.
     * 
     * @ejb.interface-method
     * @see UserSessionController
     */
    public Integer getCourtIdByTerminalName(final String terminalName) throws TerminalFindException {
        try {
            SessionPropertiesMap terminal = userTerminalController.getXHIBITTerminalLocation(terminalName);
            return (Integer) terminal.get(UserTerminalProperties.COURT_ID);
        } catch (TerminalNotFoundException ex) {
            throw new TerminalFindException();
        }
    }

    /**
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public float getEstimatedCaseDuration(final Integer caseId) throws NoDirectionsForCaseException {
        try {
            final XhbDirectionsForCase directionsForCase = XhbDirectionsForCaseBeanHelper2.findByCaseID(caseId);
            Float durationObject = directionsForCase.getTrialTimeEstimate();
            if (durationObject == null) {
                return NO_DURATION_FOR_CASE_DUMMY_VALUE;
            }
            final float duration = durationObject.floatValue();
            final Integer unit = directionsForCase.getTrialTimeUnit();
            if (unit == null) {
                return NO_DURATION_FOR_CASE_DUMMY_VALUE;
            }
            return CaseDurationHelper.getDaysFromDurationAndUnit(duration, unit);
        }
        // ===================================================================
        catch (Exception e) {
            throw new NoDirectionsForCaseException(caseId);
        }
    }

    /**
     * @param durationInDays
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException
     *            if durationInDays rounded up to the nearest half day is less
     *            than the last witness day number + session.
     * @ejb.interface-method view-type="remote"
     */
    public void setEstimatedCaseDuration(final Integer caseId, final float durationInDays, final boolean force)
            throws DurationLessThanMinimumException, NoDirectionsForCaseException {
        if (!force && isDurationBeforeEndOfCase(caseId, durationInDays)) {
            throw new DurationLessThanMinimumException("WITNESS_XXX", "Duration was:" + durationInDays + " for case: "
                    + caseId);
        }
        final XhbDirectionsForCase directionsForCase = XhbDirectionsForCaseBeanHelper2.findByCaseID(caseId);
        directionsForCase.setTrialTimeEstimate(new Float(durationInDays));
        directionsForCase.setTrialTimeUnit(CaseDurationHelper.TRIAL_DAY_UNIT);
        TrialSessionHelper.markSessionsObsolete(caseId, new Integer(new Float(durationInDays).intValue()));
    }

    private boolean isDurationBeforeEndOfCase(final Integer caseId, final float durationInDays) {
        final TrialSession lastTrialSession = TrialSessionHelper.getLastTrialSessionWithWitnesses(caseId);
        if (null == lastTrialSession) {
            return false;
        }

        float lastDayOfCase = lastTrialSession.getDayAndSessionAsDurationInDays();
        log.debug("durationInDays: " + durationInDays + " lastDayOfCase: " + lastDayOfCase);
        return durationInDays < lastDayOfCase;
    }

    private String[] getDefendantsAsNames(final Integer caseId) throws FinderException {
        final Collection defendantBeans = defendantOnCaseHome.findByCaseId(caseId);
        final String[] defStrings = new String[defendantBeans.size()];
        int count = 0;
        for (Iterator iterator = defendantBeans.iterator(); iterator.hasNext();) {
            final DefendantOnCase defendantBean = (DefendantOnCase) iterator.next();
            final Defendant defendant = defendantBean.getDefendant();
            defStrings[count++] = defendant.getFirstName()
                    + (defendant.getMiddleName() != null ? (" " + defendant.getMiddleName()) : "") + " "
                    + defendant.getSurname();

        }
        return defStrings;
    }

    /**
     * Method to return a string containing the court room no and name for a
     * given court id
     * 
     * @param courtId
     * @ejb.interface-method view-type="remote"
     */
    public ArrayList getCourtRoomsByCourtId(Integer courtId) {

        // @todo handling multiple court sites

        ArrayList rooms = new ArrayList();
        try {
            // get the court entity for the given court id
            Court court = courtHome.findByPrimaryKey(courtId);
            // get the court sites associated with the court
            Collection courtSitesForCourt = court.getCourtSites();

            for (Iterator sites = courtSitesForCourt.iterator(); sites.hasNext();) {
                CourtSite courtSite = (CourtSite) sites.next();

                // get the court rooms for the court site
                Collection courtRoomsForSite = courtSite.getCourtRooms();
                for (Iterator CourtRooms = courtRoomsForSite.iterator(); CourtRooms.hasNext();) {
                    CourtRoom courtRoom = (CourtRoom) CourtRooms.next();
                    // add Room no to array ready for sorting
                    rooms.add(courtRoom);
                }
            }
        } catch (FinderException e) {
            // @todo exception to throw when court not found
        }

        // Sort the array list in ascending crestCourtRoomNo order
        String[] sortCriteria = { "crestCourtRoomNo" };
        Sorter.sort(rooms, sortCriteria, Sorter.ASCENDING);

        ArrayList rtnRooms = new ArrayList(rooms.size());
        for (int i = 0; i < rooms.size(); i++) {
            CourtRoom r = (CourtRoom) rooms.get(i);
            // set up new arraylist with courtroom no and name details
            rtnRooms.add(r.getCrestCourtRoomNo().toString() + "**" + r.getCourtRoomName());
        }
        return rtnRooms;
    }

    /**
     * Method to check if the case is at the court specified.
     * 
     * @param caseid
     * @param courtId
     * @ejb.interface-method view-type="remote"
     */
    public boolean isCurrentCourtCase(Integer caseid, Integer courtId) {
        boolean currentCourtCase = false;
        try {
            Case casedetails = caseHome.findByPrimaryKey(caseid);
            if (casedetails.getCourtId().equals(courtId)) {
                currentCourtCase = true;
            }

        } catch (FinderException e) {
            // do nothing
        }
        log.debug("Is Current Court Case :: " + currentCourtCase);
        return currentCourtCase;
    }

    /**
     * Method to return a Skeleton session for a given session id Populated with
     * session day details
     * 
     * @param sessionId
     *            id to search for
     * @ejb.interface-method view-type="remote"
     */
    public ArrayList getSkeletonSession(Integer sessionId) {
        XhbSkeletonSessionValue ss = null;
        XhbSkeletonDayValue sd = null;
        try {
            ss = XhbSkeletonSessionHelper.findByPrimaryKey(sessionId);
            sd = XhbSkeletonDayHelper.findByPrimaryKey(ss.getXhbSkeletonDay().getSkeletonDayId());
        } catch (FinderException e) {
            // do nothing
        }
        ss.setXhbSkeletonDay(sd);
        ArrayList ssArray = new ArrayList();
        ssArray.add(ss);
        return ssArray;
    }
}
