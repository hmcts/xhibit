package uk.gov.courtservice.xhibit.business.ps.values;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_request.XhbPsrRequestBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_solicitor_firm.XhbRefSolicitorFirmBasicValue;

/**
 * <p>
 * Title: PSRRequestValueSet
 * </p>
 * <p>
 * Description: This holds a set of value objects used to describe a psr request
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley $Revision: 1.17 $
 */
public class PSRRequestValueSet implements Serializable {
    /**
     * The log4j logger
     */
    private static final Logger log = CSServices.getLogger(PSRRequestValueSet.class);

    /**
     * The request value object
     */
    private XhbPsrRequestBasicValue request;

    /**
     * The recipient value object
     */
    private PSRRecipientValueSet recipient;

    /**
     * The probation value object
     */
    private PSRProbationValueSet probation;

    /**
     * The solicitor value object
     */
    private XhbRefSolicitorFirmBasicValue solicitor;

    /**
     * The solicitor address value object
     */
    private XhbAddressBasicValue solicitorAddress;

    /**
     * The solicitor contacts Hashmap
     */
    private HashMap solicitorContacts;

    /**
     * The defendant value object
     */
    private XhbDefendantBasicValue defendant;

    /**
     * The defendant address object
     */
    private XhbAddressBasicValue defendantAddress;

    /**
     * The defendant contacts Hashmap
     */
    private HashMap defendantContacts;

    /**
     * The judge object
     */
    private XhbRefJudgeBasicValue judge;

    /**
     * The court room value
     */
    private XhbCourtRoomBasicValue courtRoom;

    /**
     * The case value
     */
    private XhbCaseBasicValue courtCase;

    /**
     * The list of offences
     */
    private XhbRefOffenceBasicValue[] offences;

    /**
     * The list of codefendants
     */
    private XhbDefendantBasicValue[] coDefendants;

    /**
     * The emil to send, not set in the constructor, set explicitly by action.
     */
    private byte[] email;
    
    private static final long serialVersionUID = 6989599930771376589L;

    /**
     * Construct a recipient value object from a value objects
     * 
     * @param newRequest
     *            the request value
     * @param newRecipient
     *            the recipient value
     * @param newProbation
     *            the probation value
     * @param newSolicitor
     *            the solicitor value
     * @param newSolicitorAddress
     *            the solicitor address value
     * @param newSolicitorContacts
     *            the solicitor contacts value
     * @param newDefendant
     *            the defendant value
     * @param newDefendantAddress
     *            the defendant address value
     * @param newJudge
     *            the defendant address value
     * @param newCourtRoom
     *            the court room value
     * @param newCase
     *            the case value
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     */
    public PSRRequestValueSet(XhbPsrRequestBasicValue newRequest, PSRRecipientValueSet newRecipient,
            PSRProbationValueSet newProbation, XhbRefSolicitorFirmBasicValue newSolicitor,
            XhbAddressBasicValue newSolicitorAddress, HashMap newSolicitorContacts,
            XhbDefendantBasicValue newDefendant, XhbAddressBasicValue newDefendantAddress,
            HashMap newDefendantContacts, XhbRefJudgeBasicValue newJudge, XhbCourtRoomBasicValue newCourtRoom,
            XhbCaseBasicValue newCourtCase, XhbRefOffenceBasicValue[] newOffences,
            XhbDefendantBasicValue[] newCoDefendants) throws IllegalArgumentException {
        setRequest(newRequest);
        setRecipient(newRecipient);
        setProbation(newProbation);
        setSolicitor(newSolicitor);
        setSolicitorAddress(newSolicitorAddress);
        setSolicitorContacts(newSolicitorContacts);
        setDefendant(newDefendant);
        setDefendantAddress(newDefendantAddress);
        setDefendantContacts(newDefendantContacts);
        setJudge(newJudge);
        setCourtRoom(newCourtRoom);
        setCourtCase(newCourtCase);
        setOffences(newOffences);
        setCoDefendants(newCoDefendants);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the request value pobject
     */
    public XhbPsrRequestBasicValue getRequest() {
        return request;
    }

    /**
     * Standard java bean setter
     * 
     * @param newRequest
     *            the request value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setRequest(XhbPsrRequestBasicValue newRequest) throws IllegalArgumentException {
        if (newRequest == null) {
            throw new IllegalArgumentException("newRequest");
        }
        request = newRequest;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the recipient value object
     */
    public PSRRecipientValueSet getRecipient() {
        return recipient;
    }

    /**
     * Standard java bean setter
     * 
     * @param newRequest
     *            the request value object
     */
    public void setRecipient(PSRRecipientValueSet newRecipient) throws IllegalArgumentException {
        recipient = newRecipient;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the probation value object
     */
    public PSRProbationValueSet getProbation() {
        return probation;
    }

    /**
     * Standard java bean setter
     * 
     * @param newProbation
     *            the probation value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setProbation(PSRProbationValueSet newProbation) throws IllegalArgumentException {
        if (newProbation == null) {
            throw new IllegalArgumentException("newProbation");
        }
        probation = newProbation;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the solicitor value object
     */
    public XhbRefSolicitorFirmBasicValue getSolicitor() {
        return solicitor;
    }

    /**
     * Standard java bean setter
     * 
     * @param newSolicitor
     *            the solicitor value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setSolicitor(XhbRefSolicitorFirmBasicValue newSolicitor) {
        solicitor = newSolicitor;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the solicitor address value object
     */
    public XhbAddressBasicValue getSolicitorAddress() {
        return solicitorAddress;
    }

    /**
     * Standard java bean setter
     * 
     * @param newSolicitorAddress
     *            the solicitor address value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setSolicitorAddress(XhbAddressBasicValue newSolicitorAddress) {
        solicitorAddress = newSolicitorAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the solicitor contacts hash map
     */
    public HashMap getSolicitorContacts() {
        return solicitorContacts;
    }

    /**
     * Standard java bean setter
     * 
     * @param newSolicitorContacts
     *            the solicitor address value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setSolicitorContacts(HashMap newSolicitorContacts) {
        solicitorContacts = newSolicitorContacts;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant value object
     */
    public XhbDefendantBasicValue getDefendant() {
        return defendant;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendant
     *            the defendant value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendant(XhbDefendantBasicValue newDefendant) {
        defendant = newDefendant;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant address value object
     */
    public XhbAddressBasicValue getDefendantAddress() {
        return defendantAddress;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantAddress
     *            the defendant address value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantAddress(XhbAddressBasicValue newDefendantAddress) {
        defendantAddress = newDefendantAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant contacts hashmap
     */
    public HashMap getDefendantContacts() {
        return defendantContacts;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantContacts
     *            the defendant contacts hashmap
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantContacts(HashMap newDefendantContacts) {
        defendantContacts = newDefendantContacts;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the judge value object
     */
    public XhbRefJudgeBasicValue getJudge() {
        return judge;
    }

    /**
     * Standard java bean setter
     * 
     * @param newJudge
     *            the judge value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setJudge(XhbRefJudgeBasicValue newJudge) {
        judge = newJudge;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the courtroom value object
     */
    public XhbCourtRoomBasicValue getCourtRoom() {
        return courtRoom;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCourtRoom
     *            the courtRoom value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourtRoom(XhbCourtRoomBasicValue newCourtRoom) {
        courtRoom = newCourtRoom;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the case value object
     */
    public XhbCaseBasicValue getCourtCase() {
        return courtCase;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCase
     *            the case value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourtCase(XhbCaseBasicValue newCourtCase) {
        courtCase = newCourtCase;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the list of offences
     */
    public XhbRefOffenceBasicValue[] getOffences() {
        return offences;
    }

    /**
     * Standard java bean setter
     * 
     * @param newOffences
     *            the list of offences
     */
    public void setOffences(XhbRefOffenceBasicValue[] newOffences) {
        offences = newOffences;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the list of codefendants
     */
    public XhbDefendantBasicValue[] getCoDefendants() {
        return coDefendants;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCoDefendants
     *            the list of codefendants
     */
    public void setCoDefendants(XhbDefendantBasicValue[] newCoDefendants) {
        coDefendants = newCoDefendants;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the email to send
     */
    public byte[] getEmail() {
        return email;
    }

    /**
     * Standard java bean setter
     * 
     * @param newEmail
     *            the email body
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setEmail(byte[] newEmail) {
        email = newEmail;
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the request id
     */
    public Integer getRequestId() {
        return request.getPrimaryKey();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the arrive no later than date
     */
    public Date getNoLaterThan() {
        return request.getArriveNoLaterThanDate();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the hearing date
     */
    public Date getHearingDate() {
        return request.getHearingDate();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant remand location
     */
    public String getDefendantRemandLocation() {
        return request.getDefendantRemandLocation();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the solicitors telephone number
     */
    public String getSolicitorTelephone() {
        if (request.getSolicitorsTelephone() != null) {
            return request.getSolicitorsTelephone();
        }

        return getContactFromHashMap("Phone", solicitorContacts);
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation contact
     */
    public String getProbationConatct() {
        return request.getProbationContact();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the antecedants
     */
    public String getAntecedents() {
        return request.getAntecendants();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the cps office
     */
    public String getCPSOffice() {
        return request.getCpsOfficeForAntecendants();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return circumstances for offences
     */
    public String getCircumstancesForOffences() {
        return request.getCircumstancesForOffences();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return comments by court
     */
    public String getCommentsByCourt() {
        return request.getCommentsByCourt();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return available for interview
     */
    public String getAvailableForInterview() {
        return request.getAvailableForInterview();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office name
     */
    public String getProbationOfficeName() {
        if (request.getProbationOfficeName() != null) {
            return request.getProbationOfficeName();
        }

        return probation.getCourt().getProbationOfficeName();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address as a string
     */
    public String getProbationOfficeAddress() {
        if (request.getProbationOfficeAddress() != null) {
            return request.getProbationOfficeAddress();
        }

        return getAddressAsString(probation.getAddress());
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address line 1
     */
    public String getProbationOfficeAddressLine1() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 1);
        }

        return probation.getAddress().getAddress1();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address line 2
     */
    public String getProbationOfficeAddressLine2() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 2);
        }

        return probation.getAddress().getAddress2();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address line 3
     */
    public String getProbationOfficeAddressLine3() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 3);
        }

        return probation.getAddress().getAddress3();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address line 4
     */
    public String getProbationOfficeAddressLine4() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 4);
        }

        return probation.getAddress().getAddress4();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address town
     */
    public String getProbationOfficeAddressTown() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 5);
        }

        return probation.getAddress().getTown();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address county
     */
    public String getProbationOfficeAddressCounty() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 6);
        }

        return probation.getAddress().getCounty();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office address county
     */
    public String getProbationOfficeAddressPostcode() {
        if (request.getProbationOfficeAddress() != null) {
            return getAddressLine(request.getProbationOfficeAddress(), 7);
        }

        return probation.getAddress().getPostcode();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office telephone
     */
    public String getProbationOfficeTelephone() {
        if (request.getProbationOfficeTelephone() != null) {
            return request.getProbationOfficeTelephone();
        }

        return getContactFromHashMap("PS_Phone", probation.getContacts());
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office fax
     */
    public String getProbationOfficeFax() {
        if (request.getProbationOfficeFax() != null) {
            return request.getProbationOfficeFax();
        }

        return getContactFromHashMap("PS_Fax", probation.getContacts());
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the probation office email
     */
    public String getProbationOfficeEmail() {
        if (request.getProbationOfficeEmail() != null) {
            return request.getProbationOfficeEmail();
        }

        return getContactFromHashMap("PS_Email", probation.getContacts());
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the recipient name
     */
    public String getRecipientName() {
        if (request.getRecipientName() != null) {
            return request.getRecipientName();
        }

        if (recipient != null && recipient.getRecipient() != null) {
            return recipient.getRecipient().getRecipientName();
        }

        return "";
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the recipient address as a string
     */
    public String getRecipientAddress() {
        if (request.getRecipientAddress() != null) {
            return request.getRecipientAddress();
        }

        if (recipient != null) {
            return getAddressAsString(recipient.getAddress());
        }

        return ""; // may well be null so don't break . . .
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the recipient telephone
     */
    public String getRecipientTelephone() {
        if (request.getRecipientTelephone() != null) {
            return request.getRecipientTelephone();
        }

        if (recipient != null) {
            return getContactFromHashMap("Phone", recipient.getContacts());
        }

        return null;
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the recipient fax
     */
    public String getRecipientFax() {
        if (request.getRecipientFax() != null) {
            return request.getRecipientFax();
        }

        if (recipient != null) {
            return getContactFromHashMap("Fax", recipient.getContacts());
        }

        return null;
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the recipient email
     */
    public String getRecipientEmail() {
        if (request.getRecipientEmail() != null) {
            return request.getRecipientEmail();
        }

        if (recipient != null) {
            return getContactFromHashMap("Email", recipient.getContacts());
        }

        return null;
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant age
     */
    public Integer getDefendantAge() {
        if (request.getDefendantAge() != null) {
            return request.getDefendantAge();
        }

        if (defendant != null) {
            Date defDOB = this.getDefendantDOB();

            // defendant DOB can be null
            if (defDOB != null) {
                Calendar dob = Calendar.getInstance();
                dob.setTime(defDOB);

                Calendar today = Calendar.getInstance();

                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                dob.set(Calendar.YEAR, today.get(Calendar.YEAR));
                if (dob.after(today)) {
                    age--;
                }
                return new Integer(age);
            }

            log.debug("Cannot calculate age, defendant DOB  is null");
            return new Integer(0);
        }

        log.debug("Cannot calculate age, defendant null");
        return new Integer(0);
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant address as a string
     */
    public String getDefendantAddressString() {
        if (request.getDefendantAddress() != null) {
            return request.getDefendantAddress();
        }

        return getAddressAsString(defendantAddress);
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant surname
     */
    public String getDefendantSurname() {
        if (request.getDefendantSurname() != null) {
            return request.getDefendantSurname();
        }

        if (defendant != null) {
            return defendant.getSurname();
        }

        return "";
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant forenames
     */
    public String getDefendantForenames() {
        if (request.getDefendantForenames() != null) {
            log.debug("PSRRequest defs not null: " + request.getDefendantForenames());
            return request.getDefendantForenames();
        }

        if (defendant != null) {
            log
                    .debug("PSRRequest defs null so get defs: "
                            + (defendant.getFirstName() + (defendant.getMiddleName() != null ? (" " + defendant
                                    .getMiddleName()) : "")));
            return defendant.getFirstName()
                    + (defendant.getMiddleName() != null ? (" " + defendant.getMiddleName()) : "");
        }

        return "";
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant DOB
     */
    public Date getDefendantDOB() {
        if (request.getDefendantDateOfBirth() != null) {
            return request.getDefendantDateOfBirth();
        }

        if (defendant != null) {
            return defendant.getDateOfBirth();
        }

        return null;
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant telephone number
     */
    public String getDefendantTelephone() {
        return getContactFromHashMap("Phone", defendantContacts);
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the defendant remand location
     */
    public String getDefendantLocation() {
        return request.getDefendantRemandLocation();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the judge title
     */
    public String getJudgeTitle() {
        if (request.getJudgeTitle() != null) {
            return request.getJudgeTitle();
        }

        if (judge != null) {
            return judge.getFullListTitle1();
        }

        return "";
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the court name
     */
    public String getCourtName() {
        if (request.getCourtName() != null) {
            return request.getCourtName();
        }

        return probation.getCourt().getCourtName();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the solicitor firm name
     */
    public String getSolicitorName() {
        if (request.getSolicitorFirmName() != null) {
            return request.getSolicitorFirmName();
        }

        if (solicitor != null) {
            return solicitor.getSolicitorFirmName();
        }

        return "";
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the court room name
     */
    public String getCourtRoomName() {
        return courtRoom.getCourtRoomName();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the case number
     */
    public String getCaseNumber() {
        return courtCase.getCaseType() + courtCase.getCaseNumber().toString();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the status
     */
    public String getStatus() {
        return request.getPsrStatus();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the list of offences as a string
     */
    public String getOffencesString() {
        StringBuffer offencesString = new StringBuffer();

        for (int i = 0; i < offences.length; i++) {
            String offencedesc = offences[i].getOffenceDesc();
            offencesString.append(' ');
            offencesString.append(offencedesc);
            if (!(offencedesc.endsWith("."))) {
                offencesString.append('.');
            }

        }
        return offencesString.toString();
    }

    /**
     * get function which automatically switches according to what data is
     * available
     * 
     * @return the list of codefendants as a string
     */
    public String getCoDefendantsString() {
        StringBuffer defendantsString = new StringBuffer();
        if (coDefendants != null) {
            for (int i = 0; i < coDefendants.length; i++) {
                defendantsString.append(' ');
                defendantsString.append(coDefendants[i].getFirstName());
                defendantsString.append(' ');
                defendantsString.append(coDefendants[i].getSurname());
                if (i + 1 < coDefendants.length) {
                    defendantsString.append(", ");
                }
            }
        }
        return defendantsString.toString();
    }

    /**
     * returns a value from the contact hashmap
     * 
     * @param key
     *            the value to look up
     * @param contacts
     *            the hashMap to look in
     * @return the value found, an empty string if no value was found
     */
    public static String getContactFromHashMap(String key, HashMap contacts) {
        if (contacts != null && contacts.get(key) != null) {
            XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) contacts.get(key);
            return contact.getContactValue();
        }

        return "";
    }

    /**
     * returns an addresss line from an address string
     * 
     * @param address
     *            the address string to look in
     * @param line
     *            the address line we want
     * @return the string representing the line
     */
    public static String getAddressLine(String address, int line) {
        if (address != null) {
            StringTokenizer st = new StringTokenizer(address, ",");
            for (int i = 1; i < line; i++) {
                st.nextToken();
                if (!st.hasMoreTokens()) {
                    return "";
                }
            }
            if (st.hasMoreTokens()) {
                return st.nextToken();
            }
        }
        return "";
    }

    /**
     * returns an addresss as a single string
     * 
     * @param address
     *            the address value to look in
     * @return the string created from the address
     */
    public static String getAddressAsString(XhbAddressBasicValue address) {
        if (address != null) {
            return (address.getAddress1() != null && address.getAddress1().length() > 0 ? address.getAddress1() + ", "
                    : "")
                    + (address.getAddress2() != null && address.getAddress2().length() > 0 ? address.getAddress2()
                            + ", " : "")
                    + (address.getAddress3() != null && address.getAddress3().length() > 0 ? address.getAddress3()
                            + ", " : "")
                    + (address.getAddress4() != null && address.getAddress4().length() > 0 ? address.getAddress4()
                            + ", " : "")
                    + (address.getTown() != null && address.getTown().length() > 0 ? address.getTown() + ", " : "")
                    + (address.getCounty() != null && address.getCounty().length() > 0 ? address.getCounty() + ", "
                            : "")
                    + (address.getPostcode() != null && address.getPostcode().length() > 0 ? address.getPostcode() : "");
        }

        return "";
    }
}
