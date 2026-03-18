package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: PSRRequestDetailBean</p>
 * <p>Description: This holds summary information for a given PSR Request</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 *
 * @author  Edward Cawley (2003)
 * @version $Id: PSRRequestDetailBean.java,v 1.22 2006/06/05 12:32:27 bzjrnl Exp $
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractBean;
import uk.gov.courtservice.xhibit.web.framework.bean.DateField;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;

public class PSRRequestDetailBean extends AbstractBean {
    /**
     * The probation details
     */
    private ProbationServiceDetailsBean probationDetails;

    /**
     * The recipient details
     */
    private PSRRecipientDetailsBean recipientDetails;

    /**
     * The judge's title
     */
    private StringField probationAddress; // unlike recipient where we

    // just use the first line,
    // probation needs two versions.

    /**
     * The judge's title
     */
    private StringField judgeTitle;

    /**
     * The creating date
     */
    private Date creationDate;

    /**
     * The court name
     */
    private StringField courtName;

    /**
     * The hearing date
     */
    private DateField hearingDate;

    /**
     * The defendant surname
     */
    private StringField defendantSurname;

    /**
     * The defendantForenames
     */
    private StringField defendantForenames;

    /**
     * The defendants dob
     */
    private Date defendantDOB;

    /**
     * The defendants age
     */
    private int defendantAge;

    /**
     * The defendants address
     */
    private AddressBean defendantAddress;

    /**
     * The defendant's telephone number
     */
    private StringField defendantTelephone;

    /**
     * The defendant's remand location
     */
    private StringField defendantLocation;

    /**
     * The defendant's solicitor's name
     */
    private StringField solicitorName;

    /**
     * The defendant's solicitor's telephone number
     */
    private StringField solicitorTelephone;

    /**
     * The defendant's probation contact
     */
    private StringField probationContact;

    /**
     * The antecendents
     */
    private StringField antecedents;

    /**
     * The office for CPS information
     */
    private StringField cpsOffice;

    /**
     * The offences on this case
     */
    private StringField offences;

    /**
     * The Codefendants on this case
     */
    private StringField coDefendants;

    /**
     * The circumstances of the offences
     */
    private StringField circumstances;

    /**
     * comments by the court
     */
    private StringField comments;

    /**
     * available for interview
     */
    private StringField available;

    /**
     * The date the request must be returned by
     */
    private DateField noLaterThan;

    /**
     * The curret date (by the signiture)
     */
    private Date currentDate;

    /**
     * User name, officer
     */
    private StringField userName;

    /**
     * The case number
     */
    private StringField caseNumber;

    /**
     * The court number
     */
    private StringField courtNumber;

    /**
     * The Status of the request
     */
    private StringField status;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    private StringField hearingDateString;

    private StringField nolaterthanField;

    /**
     * Construct request details
     * 
     * @param newId
     *            the id for the bean
     * @param newProbationDetails
     *            the probation details
     * @param newRecipientDetails
     *            the details of the recipient
     * @param newProbationAddress
     *            the single string probation address
     * @param newJudgeTitle
     *            the Judge's title
     * @param newCreationDate
     *            creation date for the the psr request
     * @param newCourtName
     *            the court name
     * @param newHearingDate
     *            the date of the hearing
     * @param newDefendantSurname
     *            the defendants surname
     * @param newDefendantForenames
     *            the defendants forenames
     * @param newDefendantDOB
     *            the defendants date of birth
     * @param newDefendantAge
     *            the defendants age
     * @param newDefendantAddress
     *            the defendants address
     * @param newDefendantTelephone
     *            the defendants telephone number
     * @param newDefendantLocation
     *            the defendants remand location
     * @param newSolicitorName
     *            the solicitors name
     * @param newSolicitorTelephone
     *            the solicitors telephone number
     * @param newProbationContact
     *            the probation contact
     * @param newAntecedents
     *            the antecedents
     * @param newCpsOffice
     *            the office for cps
     * @param newOffences
     *            the offences
     * @param newCoDefendants
     *            the Co-defendants
     * @param newCircumstances
     *            the circumstances
     * @param newComments
     *            the comments
     * @param newAvailable
     *            the availability for an interview
     * @param newCurrentDate
     *            the date next to the signature
     * @param newUserName
     *            the username (officers name)
     * @param newCaseNumber
     *            case number
     * @param newCourtNumber
     *            the number of the court
     * @param newStatus
     *            the status of the request
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     */
    public PSRRequestDetailBean(long newId, PSRRecipientDetailsBean newRecipientDetails,
            ProbationServiceDetailsBean newProbationDetails, String newProbationAddress, String newJudgeTitle,
            Date newCreationDate, String newCourtName, Date newHearingDate, String newDefendantSurname,
            String newDefendantForenames, Date newDefendantDOB, int newDefendantAge, AddressBean newDefendantAddress,
            String newDefendantTelephone, String newDefendantLocation, String newSolicitorName,
            String newSolicitorTelephone, String newProbationContact, String newAntecedents, String newCpsOffice,
            String newOffences, String newCoDefendants, String newCircumstances, String newComments,
            String newAvailable, Date newNoLaterThan, Date newCurrentDate, String newUserName, String newCaseNumber,
            String newCourtNumber, String newStatus) throws IllegalArgumentException {
        super(newId);
        setRecipientDetails(newRecipientDetails);
        setProbationDetails(newProbationDetails);
        setJudgeTitle(new StringField(newJudgeTitle, 100, true));
        setProbationAddress(new StringField(newProbationAddress, 255, true));
        setCreationDate(newCreationDate);
        setCourtName(new StringField(newCourtName, 60, true));
        setHearingDate(new DateField(newHearingDate));
        setDefendantSurname(new StringField(newDefendantSurname, 30, true));
        setDefendantForenames(new StringField(newDefendantForenames, 50, true));
        setDefendantDOB(newDefendantDOB);
        setDefendantAge(newDefendantAge);
        setDefendantAddress(newDefendantAddress);
        setDefendantTelephone(new StringField(newDefendantTelephone, 30, true));
        setDefendantLocation(new StringField(newDefendantLocation, 100, true));
        setSolicitorName(new StringField(newSolicitorName, 35, true));
        setSolicitorTelephone(new StringField(newSolicitorTelephone, 30, true));
        setProbationContact(new StringField(newProbationContact, 30, true));
        setAntecedents(new StringField(newAntecedents, 255, true));
        setCpsOffice(new StringField(newCpsOffice, 30, true));
        setOffences(new StringField(newOffences, 255, true));
        setCoDefendants(new StringField(newCoDefendants, 255, true));
        setCircumstances(new StringField(newCircumstances, 255, true));
        setComments(new StringField(newComments, 255, true));
        setAvailable(new StringField(newAvailable, 30, true));
        setNoLaterThan(new DateField(newNoLaterThan));
        setCurrentDate(newCurrentDate);
        setUserName(new StringField(newUserName, 30, true)); // guessing 30
        setCaseNumber(new StringField(newCaseNumber, 10, true));
        setCourtNumber(new StringField(newCourtNumber, 10, true));
        setStatus(new StringField(newStatus, 6, false));
    }

    /**
     * Used to find out if the data in the bean is valid, used for html
     * validation
     * 
     * @return a boolean showing the status of the bean
     */
    public boolean isValid() {
        return !hasError();
    }

    /**
     * Return true if this bean contains an error
     * 
     * @return a boolean showing the status of the bean
     */
    public boolean hasError() {
        return hearingDate.hasError() || probationContact.hasError() || defendantLocation.hasError()
                || antecedents.hasError() || cpsOffice.hasError() || circumstances.hasError() || comments.hasError()
                || available.hasError() || noLaterThan.hasError();
    }

    public String[] getErrorMessageKeys() {
        List keyList = new ArrayList();
        if (hearingDate.hasError()) {
            keyList.add("invalidhearingdate");
        }
        if (probationContact.hasError()) {
            keyList.add("invalidprobationcontact");
        }
        if (defendantLocation.hasError()) {
            keyList.add("invaliddefendantlocation");
        }
        if (antecedents.hasError()) {
            keyList.add("invalidantecedents");
        }
        if (cpsOffice.hasError()) {
            keyList.add("invalidcpsoffice");
        }
        if (circumstances.hasError()) {
            keyList.add("invalidcircumstances");
        }
        if (comments.hasError()) {
            keyList.add("invalidcomments");
        }
        if (available.hasError()) {
            keyList.add("invalidavailable");
        }
        if (noLaterThan.hasError()) {
            keyList.add("invalidnolaterthan");
        }
        return (String[]) keyList.toArray(new String[keyList.size()]);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the probation details
     */
    public ProbationServiceDetailsBean getProbationDetails() {
        return probationDetails;
    }

    /**
     * Standard java bean setter
     * 
     * @param newProbationDetails
     *            the probation details
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setProbationDetails(ProbationServiceDetailsBean newProbationDetails) throws IllegalArgumentException {
        if (newProbationDetails == null) {
            throw new IllegalArgumentException("newProbationDetails");
        }
        probationDetails = newProbationDetails;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the name of the defendant
     */
    public PSRRecipientDetailsBean getrecipientDetails() {
        return recipientDetails;
    }

    /**
     * Standard java bean setter
     * 
     * @param newRecipientDetails
     *            the details for the recipient
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setRecipientDetails(PSRRecipientDetailsBean newRecipientDetails) throws IllegalArgumentException {
        if (newRecipientDetails == null) {
            throw new IllegalArgumentException("newRecipientDetails");
        }
        recipientDetails = newRecipientDetails;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the judge's title
     */
    public StringField getJudgeTitle() {
        return judgeTitle;
    }

    /**
     * Standard java bean setter
     * 
     * @param newJudgeTitle
     *            the judge's title
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setJudgeTitle(StringField newJudgeTitle) throws IllegalArgumentException {
        if (newJudgeTitle == null) {
            throw new IllegalArgumentException("newJudgeTitle");
        }
        judgeTitle = newJudgeTitle;
    }

    /**
     * Standard java bean setter
     * 
     * @param newProbationAddress
     *            the long probationAddress
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setProbationAddress(StringField newProbationAddress) throws IllegalArgumentException {
        if (newProbationAddress == null) {
            throw new IllegalArgumentException("newProbationAddress");
        }
        probationAddress = newProbationAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the single string probation address
     */
    public StringField getProbationAddress() {
        return probationAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCreationDate
     *            the creation date
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCreationDate(Date newCreationDate) throws IllegalArgumentException {
        if (newCreationDate == null) {
            throw new IllegalArgumentException("newCreationDate");
        }
        creationDate = newCreationDate;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the name of the court
     */
    public StringField getCourtName() {
        return courtName;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCourtName
     *            the name of the court
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourtName(StringField newCourtName) throws IllegalArgumentException {
        if (newCourtName == null) {
            throw new IllegalArgumentException("newCourtName");
        }
        courtName = newCourtName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the hearing date
     */
    public DateField getHearingDate() {
        return hearingDate;
    }

    /**
     * Set the hearing date as java.util.date and populate the StringField
     * hearingDateString
     * 
     * @param newHearingDate
     *            the hearing date
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setHearingDate(DateField newHearingDate) throws IllegalArgumentException {
        hearingDate = newHearingDate;
    }

    /**
     * 
     * @return hearing date in string field
     */
    public StringField getHearingDateString() {
        return hearingDateString;
    }

    /**
     * Get the nolaterthan StringField
     * 
     * @return StringField
     */

    public StringField getNolaterthanField() {
        return nolaterthanField;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the Surname of the defendant
     */
    public StringField getDefendantSurname() {
        return defendantSurname;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantSurname
     *            the surname of the defendant
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantSurname(StringField newDefendantSurname) throws IllegalArgumentException {
        if (newDefendantSurname == null) {
            throw new IllegalArgumentException("newDefendantSurname");
        }
        defendantSurname = newDefendantSurname;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the forenames of the defendant
     */
    public StringField getDefendantForenames() {
        return defendantForenames;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantForenames
     *            the forenames of the defendant
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantForenames(StringField newDefendantForenames) throws IllegalArgumentException {
        if (newDefendantForenames == null) {
            throw new IllegalArgumentException("newDefendantForenames");
        }
        defendantForenames = newDefendantForenames;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant's DOB
     */
    public Date getDefendantDOB() {
        return defendantDOB;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantDOB
     *            the defendants DOB
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantDOB(Date newDefendantDOB) throws IllegalArgumentException {
        /*
         * if (newDefendantDOB == null) { throw new
         * IllegalArgumentException("newDefendantDOB"); }
         */
        if (newDefendantDOB != null) {
            defendantDOB = newDefendantDOB;
        }

    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant's age
     */
    public int getDefendantAge() {
        return defendantAge;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantAge
     *            the defendants Age
     */
    public void setDefendantAge(int newDefendantAge) {
        defendantAge = newDefendantAge;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the defendant's Address
     */
    public AddressBean getDefendantAddress() {
        return defendantAddress;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantAddress
     *            the defendants Address
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantAddress(AddressBean newDefendantAddress) throws IllegalArgumentException {
        if (newDefendantAddress == null) {
            throw new IllegalArgumentException("newDefendantAddres");
        }
        defendantAddress = newDefendantAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the Telephone number of the defendant
     */
    public StringField getDefendantTelephone() {
        return defendantTelephone;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantTelephone
     *            the telephone number of the defendant
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantTelephone(StringField newDefendantTelephone) throws IllegalArgumentException {
        if (newDefendantTelephone == null) {
            throw new IllegalArgumentException("newDefendantTelephone");
        }
        defendantTelephone = newDefendantTelephone;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the remand location of the defendant
     */
    public StringField getDefendantLocation() {
        return defendantLocation;
    }

    /**
     * Standard java bean setter
     * 
     * @param newDefendantLocation
     *            the dafendant remand location
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setDefendantLocation(StringField newDefendantLocation) throws IllegalArgumentException {
        if (newDefendantLocation == null) {
            throw new IllegalArgumentException("newDefendantLocation");
        }
        defendantLocation = newDefendantLocation;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the name of the defendant's solicitor
     */
    public StringField getSolicitorName() {
        return solicitorName;
    }

    /**
     * Standard java bean setter
     * 
     * @param newSolicitorName
     *            the defendant's solicitor name
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setSolicitorName(StringField newSolicitorName) throws IllegalArgumentException {
        if (newSolicitorName == null) {
            throw new IllegalArgumentException("newSolicitorName");
        }
        solicitorName = newSolicitorName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the telephone number of the defendant's solicitor
     */
    public StringField getSolicitorTelephone() {
        return solicitorTelephone;
    }

    /**
     * Standard java bean setter
     * 
     * @param newSolicitorTelephone
     *            the defendant's solicitor's telephone number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setSolicitorTelephone(StringField newSolicitorTelephone) throws IllegalArgumentException {
        if (newSolicitorTelephone == null) {
            throw new IllegalArgumentException("newSolicitorTelephone");
        }
        solicitorTelephone = newSolicitorTelephone;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the probation contact for the defendant
     */
    public StringField getProbationContact() {
        return probationContact;
    }

    /**
     * Standard java bean setter
     * 
     * @param newProbationContact
     *            the defendant's probation contact
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setProbationContact(StringField newProbationContact) throws IllegalArgumentException {
        if (newProbationContact == null) {
            throw new IllegalArgumentException("newProbationContact");
        }
        probationContact = newProbationContact;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the antecedents
     */
    public StringField getAntecedents() {
        return antecedents;
    }

    /**
     * Standard java bean setter
     * 
     * @param newAntecedents
     *            the antecedents for this psr
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setAntecedents(StringField newAntecedents) throws IllegalArgumentException {
        if (newAntecedents == null) {
            throw new IllegalArgumentException("newAntecendets");
        }
        antecedents = newAntecedents;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the cps office
     */
    public StringField getCpsOffice() {
        return cpsOffice;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCpsOffice
     *            the office for CPS inforamtion
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCpsOffice(StringField newCpsOffice) throws IllegalArgumentException {
        if (newCpsOffice == null) {
            throw new IllegalArgumentException("newCpsOffice");
        }
        cpsOffice = newCpsOffice;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the offences
     */
    public StringField getOffences() {
        return offences;
    }

    /**
     * Standard java bean setter
     * 
     * @param newOffences
     *            the offences for this case
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setOffences(StringField newOffences) throws IllegalArgumentException {
        if (newOffences == null) {
            throw new IllegalArgumentException("newOffences");
        }
        offences = newOffences;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the the codefendants
     */
    public StringField getCoDefendants() {
        return coDefendants;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCoDefendants
     *            the codefendants for this case
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCoDefendants(StringField newCoDefendants) throws IllegalArgumentException {
        if (newCoDefendants == null) {
            throw new IllegalArgumentException("newCoDefendants");
        }
        coDefendants = newCoDefendants;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the the circumstances of the offences
     */
    public StringField getCircumstances() {
        return circumstances;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCircumstances
     *            the circumstances of the offences
     * @throws IllegalArgumentException
     *             if the value is null
     */

    public void setCircumstances(StringField newCircumstances) throws IllegalArgumentException {
        if (newCircumstances == null) {
            throw new IllegalArgumentException("newCircumstances");
        }
        circumstances = newCircumstances;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the the comments by the court
     */
    public StringField getComments() {
        return comments;
    }

    /**
     * Standard java bean setter
     * 
     * @param newComments
     *            comments by the court
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setComments(StringField newComments) throws IllegalArgumentException {
        if (newComments == null) {
            throw new IllegalArgumentException("newComments");
        }
        comments = newComments;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the availability for interview
     */
    public StringField getAvailable() {
        return available;
    }

    /**
     * Standard java bean setter
     * 
     * @param newAvailable
     *            availability for interview
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setAvailable(StringField newAvailable) throws IllegalArgumentException {
        if (newAvailable == null) {
            throw new IllegalArgumentException("newAvailable");
        }
        available = newAvailable;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the date by which copies of the PSR must be sent
     */
    public DateField getNoLaterThan() {
        return noLaterThan;
    }

    /**
     * 
     * Sets noLaterThan java.util.Date and corresponding StringField
     * noLaterThanField
     * 
     * @param newNoLaterThan
     *            the date by which copies of the PSR must be sent
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setNoLaterThan(DateField newNoLaterThan) {
        noLaterThan = newNoLaterThan;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the date used next to the signature
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCurrentDate
     *            the date used next to the signature
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCurrentDate(Date newCurrentDate) throws IllegalArgumentException {
        if (newCurrentDate == null) {
            throw new IllegalArgumentException("newCurrentDate");
        }
        currentDate = newCurrentDate;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the username used for the officer name
     */
    public StringField getUserName() {
        return userName;
    }

    /**
     * Standard java bean setter
     * 
     * @param newUserName
     *            the case number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setUserName(StringField newUserName) throws IllegalArgumentException {
        if (newUserName == null) {
            throw new IllegalArgumentException("newUserName");
        }
        userName = newUserName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the case number
     */
    public StringField getCaseNumber() {
        return caseNumber;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCaseNumber
     *            the case number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCaseNumber(StringField newCaseNumber) throws IllegalArgumentException {
        if (newCaseNumber == null) {
            throw new IllegalArgumentException("newCaseNumber");
        }
        caseNumber = newCaseNumber;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the court number
     */
    public StringField getCourtNumber() {
        return courtNumber;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCourtNumber
     *            the court number
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourtNumber(StringField newCourtNumber) throws IllegalArgumentException {
        if (newCourtNumber == null) {
            throw new IllegalArgumentException("newCourtNumber");
        }
        courtNumber = newCourtNumber;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the status of the request
     */
    public StringField getStatus() {
        return status;
    }

    /**
     * Standard java bean setter
     * 
     * @param newStatus
     *            the status
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setStatus(StringField newStatus) throws IllegalArgumentException {
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus");
        }
        status = newStatus;
    }

}
