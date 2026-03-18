package uk.gov.courtservice.xhibit.business.ps.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ejb.SessionBean;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.dailylist.DailyListQueries;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_on_case_ref_sol_firm.XhbDefOnCaseRefSolFirm;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_on_case_ref_sol_firm.XhbDefOnCaseRefSolFirmBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_recipient.XhbPsrRecipient;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_recipient.XhbPsrRecipientBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_request.XhbPsrRequest;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_request.XhbPsrRequestBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_request.XhbPsrRequestBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudge;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudgeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_solicitor_firm.XhbRefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_sched_hearing_attendee.XhbSchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.xhb_sched_hearing_attendee.XhbSchedHearingAttendeeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_sched_hearing_defendant.XhbSchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_sitting.XhbSitting;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminal;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.exceptions.email.EmailException;
import uk.gov.courtservice.xhibit.business.ps.database.query.PSRQuery;
import uk.gov.courtservice.xhibit.business.ps.values.PSRProbationValueSet;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.business.services.email.PdfEmailHelper;
import uk.gov.courtservice.xhibit.business.vos.services.email.PdfEmailValue;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithJudge;

/**
 * <p>
 * Title: The PS Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Stateless Session Bean that provides all business
 * services for the thin client.
 * </p>
 * 
 * @ejb.bean name="PSController" description="PS Controller Bean"
 *           type="Stateless" view-type="remote" jndi-name="PSControllerHome"
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell
 * @version $Id: PSControllerBean.java,v 1.65 2014/06/20 17:36:33 atwells Exp $
 */
public class PSControllerBean extends CSSessionBean implements SessionBean {
    /** The psr record statuses */
    public static final String NEW_PSR_REQUEST_STATUS = "NEW";

    public static final String ISSUED_PSR_REQUEST_STATUS = "ISSUED";

    public static final String EDIT_PSR_REQUEST_STATUS = "EDIT";

    private static final String PSR_SUBJECT = "Pre Sentence Report Request";
    
    /**
     * The property file for this class
     */
    private static final String PSR_PROPERTIES = "psr.request";

    /**
     * Property representing a status of NEW
     */
    private static final String NEW_STATUS = "psr.request.trigger.new.status";

    /**
     * Load the properties for this class
     */
    private static final Properties _psrConfig = CSServices.getConfigServices().getProperties(PSR_PROPERTIES);

    /**
     * New Status indicator
     */
    private static final Integer _newStatus = new Integer(_psrConfig.getProperty(NEW_STATUS));
    

    /**
     * Insert a psrrequest for the defendant on request, if defendantOnCaseId is
     * null attemp to guess from case and court id (use first guilty defendant
     * on case)
     * 
     * @param caseId
     *            the id of the case
     * @param defendantOnCaseId
     *            defendant on case id (can be null)
     * @param courtRoomName
     *            the court room name
     * @param schHearingID
     *            the scheduled hearing id
     * @param longAdjournDate
     *            the long adjournment date
     * @ejb.interface-method view-type="remote"
     */
    public void createPSRRequest(Integer caseId, Integer defendantOnCaseId, String courtRoomName,
            Integer schHearingID, Date longAdjournDate) throws CSUnrecoverableException {
        if (log.isDebugEnabled()) {
            log.debug("PSControllerBean createPSRRequest start...");
        }

        if (defendantOnCaseId != null) {
            XhbPsrRequestBasicValue requestValue = new XhbPsrRequestBasicValue();
            requestValue.setDefendantOnCaseId(defendantOnCaseId);
            requestValue.setPsrStatus(NEW_PSR_REQUEST_STATUS);
            requestValue.setPsrCourtRoom(courtRoomName);
            requestValue.setJudgeTitle(getJudgeForSchHearing(schHearingID));
            requestValue.setPsrRequestTrigger(_newStatus);
            requestValue.setLongAdjournmentDate(longAdjournDate);
            XhbPsrRequestBeanHelper2.create(requestValue);
            return;
        }

        final XhbCase caze = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        final Iterator docIt = caze.getXhbDefendantOnCases().iterator();

        while (docIt.hasNext()) {
            final XhbDefendantOnCase xdoc = (XhbDefendantOnCase) docIt.next();
            final Iterator dooIt = xdoc.getXhbDefendantOnOffences().iterator();

            while (dooIt.hasNext()) {
                final XhbDefendantOnOffence xdoo = (XhbDefendantOnOffence) dooIt.next();
                final Iterator vIt = xdoo.getXhbVerdicts().iterator();

                while (vIt.hasNext()) {
                    final XhbVerdict verdict = (XhbVerdict) vIt.next();
                    final XhbRefSystemCode refSystemCode = verdict.getXhbRefSystemCode();

                    if ((refSystemCode != null) && refSystemCode.getCode().startsWith("G")) {
                        XhbPsrRequestBasicValue requestValue = new XhbPsrRequestBasicValue();
                        requestValue.setDefendantOnCaseId(xdoc.getDefendantOnCaseId());
                        requestValue.setPsrStatus(NEW_PSR_REQUEST_STATUS);
                        requestValue.setPsrCourtRoom(courtRoomName);// set
                        // the
                        // solicitor
                        // data
                        requestValue.setJudgeTitle(getJudgeForSchHearing(schHearingID));
                        XhbPsrRequestBeanHelper2.create(requestValue);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Issues a psr request, copying data from the value object into the
     * psrrequest table . . .
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void issuePSRRequest(PSRRequestValueSet requestValues) throws CSUnrecoverableException {
        try {
            XhbPsrRequestBasicValue requestValue = requestValues.getRequest();
            requestValue.setProbationOfficeName(requestValues.getProbationOfficeName());
            requestValue.setProbationOfficeAddress(requestValues.getProbationOfficeAddress());
            requestValue.setProbationOfficeTelephone(requestValues.getProbationOfficeTelephone());
            requestValue.setProbationOfficeFax(requestValues.getProbationOfficeFax());
            requestValue.setProbationOfficeEmail(requestValues.getProbationOfficeEmail());
            requestValue.setRecipientName(requestValues.getRecipientName());
            requestValue.setRecipientAddress(requestValues.getRecipientAddress());
            requestValue.setRecipientTelephone(requestValues.getRecipientTelephone());
            requestValue.setRecipientFax(requestValues.getRecipientFax());
            requestValue.setRecipientEmail(requestValues.getRecipientEmail());
            requestValue.setDefendantAge(requestValues.getDefendantAge());
            requestValue.setDefendantAddress(requestValues.getDefendantAddressString());
            requestValue.setDefendantSurname(requestValues.getDefendantSurname());
            requestValue.setDefendantForenames(requestValues.getDefendantForenames());
            if (requestValues.getDefendantDOB() != null) {
                requestValue.setDefendantDateOfBirth(new Timestamp(requestValues.getDefendantDOB().getTime()));
            }
            requestValue.setJudgeTitle(requestValues.getJudgeTitle());
            requestValue.setCourtName(requestValues.getCourtName());
            requestValue.setSolicitorFirmName(requestValues.getSolicitorName());
            requestValue.setSolicitorsTelephone(requestValues.getSolicitorTelephone());

            String method = requestValues.getRecipient().getRecipient().getRecipientMethodOfContact();
            String recipientName = requestValues.getRecipientName();
            if (requestValues.getProbation().getContacts().get("PS_Email") != null) {
                String pEmail = ((XhbContactDetailBasicValue) requestValues.getProbation().getContacts()
                        .get("PS_Email")).getContactValue();
                if (requestValues.getRecipient().getContacts().get("Email") != null && method.equalsIgnoreCase("email")) {
                    String recipient = ((XhbContactDetailBasicValue) requestValues.getRecipient().getContacts().get(
                            "Email")).getContactValue();
                    if (pEmail.length() > 0 && recipient.length() > 0) {
                        InternetAddress sender = new InternetAddress(pEmail);
                        if (requestValues.getEmail() != null) {
                            Integer courtId = requestValues.getProbation().getCourt().getPrimaryKey();
                            StringBuffer buf = new StringBuffer();
                            buf.append(requestValues.getProbation().getCourt().getCourtName());
                            buf.append(" ");
                            buf.append(PSR_SUBJECT);
                            PdfEmailValue ev = new PdfEmailValue(recipient, recipientName, buf.toString(), sender,
                                    "Please find attached a PSR request.", requestValues.getEmail(), false, courtId);
                            PdfEmailHelper.sendEmail(ev);
                        }
                    }
                } else if (requestValues.getRecipient().getContacts().get("Fax") != null
                        && method.equalsIgnoreCase("fax")) {
                    String recipient = ((XhbContactDetailBasicValue) requestValues.getRecipient().getContacts().get(
                            "Fax")).getContactValue();
                    if (pEmail.length() > 0 && recipient.length() > 0) {
                        InternetAddress sender = new InternetAddress(pEmail);
                        if (requestValues.getEmail() != null) {
                            Integer courtId = requestValues.getProbation().getCourt().getPrimaryKey();
                            PdfEmailValue ev = new PdfEmailValue(recipient, recipientName,
                                    "Pre Sentence Report Request", sender, "Please find attached a PSR request.",
                                    requestValues.getEmail(), false, courtId);
                            PdfEmailHelper.sendEmail(ev);
                        }
                    }
                }
            }
            requestValue.setPsrStatus(ISSUED_PSR_REQUEST_STATUS);
            requestValue.setPsrRecipientId(null); // zap the ids
            XhbPsrRequestBeanHelper2.update(requestValue);
        } catch (AddressException e) {
            log.fatal("AddressException", e);
            throw new CSUnrecoverableException(e);
        } catch (EmailException e) {
            log.fatal("EmailException", e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Returns a request value set for a given request value
     */
    private PSRRequestValueSet findRequestValues(XhbPsrRequestBasicValue request, String terminal)
            throws CSUnrecoverableException {
        final PSRRecipientValueSet recipient = getRecipient(request.getPsrRecipientId());

        final XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(request.getDefendantOnCaseId());
        final XhbDefendant defendant = doc.getXhbDefendant();
        final XhbAddress defendantAddress = defendant.getXhbAddress();
        final HashMap defendantContacts = getContacts(defendantAddress);

        final XhbCase caze = doc.getXhbCase();

        final XhbRefOffenceBasicValue[] refOffences = getRefOffences(doc);
        final XhbDefendantBasicValue[] otherDefendants = getOtherDefendants(caze, defendant.getDefendantId());

        XhbRefSolicitorFirm solicitorFirm = null;
        XhbAddress solicitorFirmAddress = null;
        HashMap solicitorContacts = null;
        XhbRefJudgeBasicValue judge = new XhbRefJudgeBasicValue();
        XhbCourtRoom courtRoom = null;

        final XhbScheduledHearing latestSh = getLatestScheduledHearing(doc);

        if (latestSh != null) {
            final Iterator rsfIt = XhbDefOnCaseRefSolFirmBeanHelper2.findByDefOnCaseId(doc.getDefendantOnCaseId())
                    .iterator();

            // Shouldn't be multiple records, as CREST should prevent it,
            // but we
            // only get the first one based on REP_ST_DATE ...
            if (rsfIt.hasNext()) {
                solicitorFirm = ((XhbDefOnCaseRefSolFirm) rsfIt.next()).getXhbRefSolicitorFirm();
                solicitorFirmAddress = solicitorFirm.getXhbAddress();
                solicitorContacts = getContacts(solicitorFirmAddress);
            }

            XhbSitting sitting = latestSh.getXhbSitting();
            courtRoom = sitting.getXhbCourtRoom();
            // set the judge value from the PSR_REQUEST table
            judge.setFullListTitle1(request.getJudgeTitle());
        }

        return new PSRRequestValueSet(request, recipient, findProbation(terminal),
                ((solicitorFirm != null) ? solicitorFirm.getData() : null),
                ((solicitorFirmAddress != null) ? solicitorFirmAddress.getData() : null), solicitorContacts, defendant
                        .getData(), defendantAddress.getData(), defendantContacts, judge,
                ((courtRoom != null) ? courtRoom.getData() : null), caze.getData(), refOffences, otherDefendants);
    }

    /**
     * Return Judge for given Scheduled Hearing ID
     * 
     */
    private String getJudgeForSchHearing(Integer scheduledHearingID) {
        String judgeName = new String("");
        Collection attendees = XhbSchedHearingAttendeeBeanHelper2.findByAttendeeTypeAndSchedHearingID("J",
                scheduledHearingID);
        Iterator i = attendees.iterator();
        if (attendees.size() > 0) {
            XhbSchedHearingAttendee latestAttendee = null;
            // loop through all attendee's to pick up the most recent entry
            while (i.hasNext()) {
                XhbSchedHearingAttendee att = (XhbSchedHearingAttendee) i.next();
                if (latestAttendee == null) {
                    latestAttendee = att;
                } else {
                    // if this attendee has a greater primary key then this
                    // is the latest judge
                    if (att.getShAttendeeId().intValue() > latestAttendee.getShAttendeeId().intValue()) {
                        latestAttendee = att;
                    }
                }
            }
            // for the latest attendee get the judge details
            Integer refJudgeId = latestAttendee.getRefJudgeId();
            XhbRefJudge rj = XhbRefJudgeBeanHelper2.findByPrimaryKey(refJudgeId);
            if (rj != null) {
                String fullTitle = rj.getFullListTitle1();
                String judgeSurname = rj.getSurname();
                // use if fullTitle
                if (fullTitle != null) {
                    judgeName = fullTitle;
                }
                // otherwise use surname
                else {
                    judgeName = judgeSurname;
                }
            }
        }
        // return the judge name
        return judgeName;
    }

    /**
     * Returns a request value set for a given primary key
     * 
     * @ejb.interface-method view-type="remote"
     */
    public PSRRequestValueSet findRequestByPrimaryKey(Integer pk, String terminal) throws CSUnrecoverableException {
        XhbPsrRequestBasicValue request = XhbPsrRequestBeanHelper2.findByPrimaryKeyValue(pk);
        return findRequestValues(request, terminal);
    }

    /**
     * Returns a list of unissued request value sets
     * 
     * @ejb.interface-method view-type="remote"
     */
    public List findAllUnissuedRequests(String terminal) throws CSUnrecoverableException {
        Integer courtId = getCourtIdForTerminal(terminal);
        return PSRQuery.getUnissuedPsrRequests(courtId);
    }

    /**
     * Returns a list of issued request value sets
     * 
     * @ejb.interface-method view-type="remote"
     */
    public List findAllIssuedRequests(String terminal) throws CSUnrecoverableException {
        Integer courtId = getCourtIdForTerminal(terminal);
        return PSRQuery.getIssuedPsrRequests(courtId);
    }

    /**
     * Returns a probation value set for a given primary key
     * 
     * @ejb.interface-method view-type="remote"
     */
    public PSRProbationValueSet findProbation(String terminal) throws CSUnrecoverableException {
        XhbCourt court = getCourtForTerminal(terminal);
        XhbAddress address = court.getXhbAddress();
        HashMap contacts = getContacts(address);

        return new PSRProbationValueSet(court.getData(), address.getData(), contacts);
    }

    /**
     * Updates a probation value set
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void updateProbationValues(PSRProbationValueSet probationValue) {
        XhbCourtBeanHelper2.update(probationValue.getCourt());
        XhbAddressBeanHelper2.update(probationValue.getAddress());

        HashMap contacts = probationValue.getContacts();
        Iterator i = contacts.values().iterator();

        while (i.hasNext()) {
            XhbContactDetailBasicValue contactDetail = (XhbContactDetailBasicValue) i.next();
            if (contactDetail.getPrimaryKey() == null && contactDetail.getContactValue().length() > 0) // added
            // a new
            // contact
            {
                contactDetail.setAddressId(probationValue.getAddress().getPrimaryKey());
                XhbContactDetailBeanHelper2.create(contactDetail);
            } else if (contactDetail.getContactValue().length() == 0) {
                if (contactDetail.getPrimaryKey() != null) // cleared
                // existing
                // contact
                // details . . .
                {
                    XhbContactDetailBeanHelper2.remove(contactDetail);
                }
            } else { // normal update . . .
                XhbContactDetailBeanHelper2.update(contactDetail);
            }
        }
    }

    /**
     * Update request value set
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void updateRequestValues(PSRRequestValueSet requestValue) throws CSUnrecoverableException {
        requestValue.getRequest().setPsrStatus(EDIT_PSR_REQUEST_STATUS);
        XhbPsrRequestBeanHelper2.update(requestValue.getRequest());
    }

    /**
     * Returns a list of recipient
     * 
     * @ejb.interface-method view-type="remote"
     */
    public List getAllRecipients() throws CSUnrecoverableException {
        List toReturn = new ArrayList();
        Iterator it = XhbPsrRecipientBeanHelper2.findAll().iterator();

        while (it.hasNext()) {
            XhbPsrRecipient recipient = (XhbPsrRecipient) it.next();
            XhbAddress address = recipient.getXhbAddress();
            HashMap contacts = getContacts(address);

            toReturn.add(new PSRRecipientValueSet(recipient.getData(), address.getData(), contacts));
        }

        return toReturn;
    }

    /**
     * Returns a recipient value set for a given primary key
     * 
     * @ejb.interface-method view-type="remote"
     */
    public PSRRecipientValueSet findRecipientByPrimaryKey(java.lang.Integer pk) throws CSUnrecoverableException {
        XhbPsrRecipient recipient = XhbPsrRecipientBeanHelper2.findByPrimaryKey(pk);
        XhbAddress address = recipient.getXhbAddress();
        HashMap contacts = getContacts(address);

        return new PSRRecipientValueSet(recipient.getData(), address.getData(), contacts);
    }

    /**
     * Updates a recipient value set
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void updateRecipientValues(PSRRecipientValueSet recipientValue) throws CSUnrecoverableException {
        XhbPsrRecipientBeanHelper2.update(recipientValue.getRecipient());
        XhbAddressBeanHelper2.update(recipientValue.getAddress());

        HashMap contacts = recipientValue.getContacts();
        Iterator i = contacts.values().iterator();
        while (i.hasNext()) {
            XhbContactDetailBasicValue contactDetail = (XhbContactDetailBasicValue) i.next();
            if (contactDetail.getPrimaryKey() == null && contactDetail.getContactValue().length() > 0) // added
            // a new
            // contact
            {
                contactDetail.setAddressId(recipientValue.getAddress().getPrimaryKey());
                XhbContactDetailBeanHelper2.create(contactDetail);
            } else if (contactDetail.getContactValue().length() == 0) {
                if (contactDetail.getPrimaryKey() != null) // cleared
                // existing
                // contact
                // details . . .
                {
                    XhbContactDetailBeanHelper2.remove(contactDetail);
                }
            } else { // normal update . . .
                XhbContactDetailBeanHelper2.update(contactDetail);
            }
        }
    }

    /**
     * Inserts a recipient value set
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void insertRecipientValues(PSRRecipientValueSet recipientValue) {
        XhbAddressBasicValue address = XhbAddressBeanHelper2.create(recipientValue.getAddress());

        recipientValue.getRecipient().setRecipientAddressId(address.getPrimaryKey());
        XhbPsrRecipientBeanHelper2.create(recipientValue.getRecipient());
        HashMap contacts = recipientValue.getContacts();
        Iterator i = contacts.values().iterator();
        while (i.hasNext()) {
            XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) i.next();
            if (contact.getContactValue().length() > 0) // don't enter the blank
            // fields
            {
                contact.setAddressId(address.getPrimaryKey());
                XhbContactDetailBeanHelper2.create(contact);
            }
        }
    }

    /**
     * Deletes a recipient
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void removeRecipientValues(PSRRecipientValueSet recipientValue) {
        Iterator it = XhbPsrRequestBeanHelper2.findByPsrRecipientId(recipientValue.getRecipient().getPrimaryKey())
                .iterator();
        while (it.hasNext()) {
            XhbPsrRequest request = (XhbPsrRequest) it.next();

            log.debug("Request primary key : " + request.getPrimaryKey());
            request.setXhbPsrRecipient(null);
        }

        XhbPsrRecipientBeanHelper2.remove(recipientValue.getRecipient());
    }

    /**
     * @ejb.interface-method
     */
    public DailyListWithJudge[] getDailyListWithJudge(final Integer courtId, final Date date) {
        return DailyListQueries.getDailyListWithJudge(courtId, date);
    }

    /**
     * @ejb.interface-method
     */
    public DailyList[] getDailyListByDefendant(final Integer courtId, final Date date) {
        return DailyListQueries.getDailyListByDefendant(courtId, date);
    }

    /**
     * @param pk
     *            the primary key of the address
     * @return a hashmap of the contacts
     */
    private HashMap getContacts(XhbAddress address) {
        HashMap contacts = new HashMap();

        if (address != null) {
            XhbContactDetailBasicValue[] contactArray = address.getXhbContactDetailsData();

            for (int j = 0; j < contactArray.length; j++) {
                contacts.put(contactArray[j].getContactType(), contactArray[j]);
            }
        }

        return contacts;
    }

    private XhbRefOffenceBasicValue[] getRefOffences(XhbDefendantOnCase xdoc) {
        final Collection defendantOnOffences = xdoc.getXhbDefendantOnOffences();
        final Iterator dooIt = defendantOnOffences.iterator();

        // the results...
        final List refOffences = new ArrayList(defendantOnOffences.size());

        while (dooIt.hasNext()) {
            XhbDefendantOnOffence xdoo = (XhbDefendantOnOffence) dooIt.next();
            refOffences.add(xdoo.getXhbOffence().getXhbRefOffenceData());
        }

        return (XhbRefOffenceBasicValue[]) refOffences.toArray(new XhbRefOffenceBasicValue[refOffences.size()]);
    }

    /**
     * @param doc
     * @return
     */
    private XhbScheduledHearing getLatestScheduledHearing(final XhbDefendantOnCase doc) {
        XhbScheduledHearing latestSh = null;
        final Iterator shdIt = doc.getXhbSchedHearingDefendants().iterator();
        while (shdIt.hasNext()) {
            XhbSchedHearingDefendant schedDef = (XhbSchedHearingDefendant) shdIt.next();
            XhbScheduledHearing sh = schedDef.getXhbScheduledHearing();

            if (latestSh == null) {
                latestSh = sh;
            } else {
                // Get date from list as date originally used was never
                // populated
                // date of hearing can be null.
                Date shts = sh.getXhbSitting().getXhbHearingList().getStartDate();

                if (shts != null && shts.after(latestSh.getXhbSitting().getXhbHearingList().getStartDate())) {
                    latestSh = sh;
                }
            }
        }
        return latestSh;
    }

    /**
     * @param caze
     * @param defendantId
     */
    private XhbDefendantBasicValue[] getOtherDefendants(final XhbCase caze, final Integer defendantId) {
        final List otherDefendants = new ArrayList();
        final Iterator odIt = caze.getXhbDefendantOnCases().iterator();

        while (odIt.hasNext()) {
            XhbDefendantOnCase xdoc = (XhbDefendantOnCase) odIt.next();
            if (!xdoc.getDefendantId().equals(defendantId)) {
                otherDefendants.add(xdoc.getXhbDefendantData());
            }
        }

        return (XhbDefendantBasicValue[]) otherDefendants.toArray(new XhbDefendantBasicValue[otherDefendants.size()]);
    }

    /**
     * @param request
     * @return
     */
    private PSRRecipientValueSet getRecipient(Integer recipientId) {
        PSRRecipientValueSet recipient = null;
        if (recipientId != null) {
            recipient = findRecipientByPrimaryKey(recipientId);
        }
        return recipient;
    }

    private XhbCourt getCourtForTerminal(String terminalName) {
        return getCourtSiteForTerminal(terminalName).getXhbCourt();
    }

    private Integer getCourtIdForTerminal(String terminalName) {
        final Integer courtId = getCourtSiteForTerminal(terminalName).getCourtId();
        return courtId;
    }

    private XhbCourtSite getCourtSiteForTerminal(String terminalName) {
        XhbTerminal terminal = XhbTerminalBeanHelper2.findByUniqueTerminalName(terminalName);
        XhbCourtSite courtSite = terminal.getXhbCourtSite();

        if (courtSite == null) {
            courtSite = terminal.getXhbCourtRoom().getXhbCourtSite();
        }

        return courtSite;
    }
}
