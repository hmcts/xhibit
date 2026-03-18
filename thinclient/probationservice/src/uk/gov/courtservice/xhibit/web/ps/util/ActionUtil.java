package uk.gov.courtservice.xhibit.web.ps.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_recipient.XhbPsrRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_request.XhbPsrRequestBasicValue;
import uk.gov.courtservice.xhibit.business.ps.values.PSRProbationValueSet;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.PSRSummaryValue;
import uk.gov.courtservice.xhibit.web.ps.bean.AddressBean;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRecipientDetailsBean;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRecipientSummaryBean;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestSummaryBean;
import uk.gov.courtservice.xhibit.web.ps.bean.ProbationServiceDetailsBean;

/**
 * <p>
 * Title: ActionUtil
 * </p>
 * <p>
 * Description: This class provides utilitys for moving data to the web beans
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.23 $
 */
public abstract class ActionUtil {
    private static final Logger log = CSServices.getLogger(ActionUtil.class);

    /**
     * <p>
     * This method returns a set of recipient summary beans given a set of
     * values
     * </p>
     * 
     * @param recipientValues ,
     *            the lis of recipient values from which to build the bean
     * @return a list of summary beans
     */
    public static List getRecipientSummaries(List recipientValues) {
        List toReturn = new ArrayList();
        Iterator i = recipientValues.iterator();
        long count = 0;
        while (i.hasNext()) {
            PSRRecipientValueSet valueSet = (PSRRecipientValueSet) i.next();
            toReturn.add(new PSRRecipientSummaryBean(count++, valueSet.getRecipient().getRecipientName(),
                    getAddressAsString(valueSet.getAddress()), getContactFromHashMap("Phone", valueSet.getContacts()),
                    getContactFromHashMap("Fax", valueSet.getContacts()), getContactFromHashMap("Email", valueSet
                            .getContacts())));
        }
        return toReturn;
    }

    /**
     * <p>
     * This method returns a set of request summary beans given a set of values
     * </p>
     * 
     * @param requestValues ,
     *            the list of request values from which to build the bean
     * @return a list of summary beans
     */
    public static List getRequestSummaries(List requestValues) {
        List toReturn = new ArrayList();
        Iterator i = requestValues.iterator();
        long count = 0;
        while (i.hasNext()) {
            PSRSummaryValue valueSet = (PSRSummaryValue) i.next();
            toReturn.add(new PSRRequestSummaryBean(count++, valueSet.getDefendant(), valueSet.getCaseNumber(), valueSet
                    .getPsrCourtRoom(), valueSet.getPsrStatus()));
        }
        return toReturn;
    }

    /**
     * returns a value from the contact hashmap this function is also in some
     * values, it probably should be removed from here at some point
     * 
     * @param key
     *            the value to look up
     * @param contacts
     *            the hashMap to look in
     * @return the value found, an empty string if no value was found
     */
    public static String getContactFromHashMap(String key, HashMap contacts) {
        if (contacts.get(key) != null) {
            XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) contacts.get(key);
            return contact.getContactValue();
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
    private static String getAddressAsString(XhbAddressBasicValue address) {
        if (address != null) {
            return (address.getAddress1() != null && address.getAddress1().length() > 0 ? address.getAddress1() + ", "
                    : "")
                    + (address.getAddress2() != null && address.getAddress2().length() > 0 ? address.getAddress2()
                            + ", " : "")
                    + (address.getAddress3() != null && address.getAddress3().length() > 0 ? address.getAddress3()
                            + ", " : "")
                    + (address.getAddress4() != null && address.getAddress4().length() > 0 ? address.getAddress4()
                            + ", " : "")
                    + (address.getPostcode() != null && address.getPostcode().length() > 0 ? address.getPostcode() : "");
        }

        return "";
    }

    /**
     * <p>
     * This method returns a recipient detail bean from a set of values
     * </p>
     * 
     * @param recipientValue ,
     *            recipient values from which to build the bean
     * @return the populates recipient bean
     */
    public static PSRRecipientDetailsBean getRecipientDetails(PSRRecipientValueSet recipientValue) {
        AddressBean addressBean = new AddressBean(recipientValue.getAddress().getAddress1(), recipientValue
                .getAddress().getAddress2(), recipientValue.getAddress().getAddress3(), recipientValue.getAddress()
                .getAddress4(), recipientValue.getAddress().getTown(), recipientValue.getAddress().getCounty(),
                recipientValue.getAddress().getPostcode(), recipientValue.getAddress().getCountry());
        return new PSRRecipientDetailsBean(0, recipientValue.getRecipient().getRecipientName(), addressBean,
                getContactFromHashMap("Phone", recipientValue.getContacts()), getContactFromHashMap("Fax",
                        recipientValue.getContacts()), getContactFromHashMap("Email", recipientValue.getContacts()),
                recipientValue.getRecipient().getRecipientMethodOfContact());
    }

    /**
     * <p>
     * This method returns a request detail bean from a set of values
     * </p>
     * 
     * @param requestValue ,
     *            request values from which to build the bean
     * @return the populated request bean
     */
    public static PSRRequestDetailBean getRequestDetails(PSRRequestValueSet requestValue) {
        AddressBean recipientAddress = new AddressBean(requestValue.getRecipientAddress(), "", "", "", "", "", "", "");
        AddressBean defendantAddress = new AddressBean(requestValue.getDefendantAddressString(), "", "", "", "", "",
                "", "");
        PSRRecipientDetailsBean psrRecipientDetailsBean = new PSRRecipientDetailsBean(1, requestValue
                .getRecipientName(), recipientAddress, requestValue.getRecipientTelephone(), requestValue
                .getRecipientFax(), requestValue.getRecipientEmail(), "");

        AddressBean probationAddress = new AddressBean(requestValue.getProbationOfficeAddressLine1(), requestValue
                .getProbationOfficeAddressLine2(), requestValue.getProbationOfficeAddressLine3(), requestValue
                .getProbationOfficeAddressLine4(), requestValue.getProbationOfficeAddressTown(), requestValue
                .getProbationOfficeAddressCounty(), requestValue.getProbationOfficeAddressPostcode(), "");
        ProbationServiceDetailsBean probationDetailsBean = new ProbationServiceDetailsBean(0, requestValue
                .getProbationOfficeName(), probationAddress, requestValue.getProbationOfficeTelephone(), requestValue
                .getProbationOfficeFax(), requestValue.getProbationOfficeEmail());

        return new PSRRequestDetailBean(requestValue.getRequest().getPrimaryKey().longValue(), psrRecipientDetailsBean,
                probationDetailsBean, requestValue.getProbationOfficeAddress(), requestValue.getJudgeTitle(),
                requestValue.getRequest().getCreationDate(), requestValue.getCourtName(),
                requestValue.getHearingDate(), requestValue.getDefendantSurname(),
                requestValue.getDefendantForenames(), requestValue.getDefendantDOB(), requestValue.getDefendantAge()
                        .intValue(), defendantAddress, requestValue.getDefendantTelephone(), requestValue
                        .getDefendantLocation(), requestValue.getSolicitorName(), requestValue.getSolicitorTelephone(),
                requestValue.getProbationConatct(), requestValue.getAntecedents(), requestValue.getCPSOffice(),
                requestValue.getOffencesString(), requestValue.getCoDefendantsString(), requestValue
                        .getCircumstancesForOffences(), requestValue.getCommentsByCourt(), requestValue
                        .getAvailableForInterview(), requestValue.getNoLaterThan(), new Date(), "username" // set
                                                                                                            // later
                , requestValue.getCaseNumber(), requestValue.getCourtRoomName(), requestValue.getStatus());
    }

    /**
     * <p>
     * This method modifies a request value according to a request bean
     * </p>
     * 
     * @param requestValue ,
     *            the request value to modify
     * @param requestBean ,
     *            the new data
     */
    public static void modifyRequestValue(XhbPsrRequestBasicValue requestValue, PSRRequestDetailBean requestBean) {
        requestValue.setDefendantRemandLocation(requestBean.getDefendantLocation().getValue());
        requestValue.setProbationContact(requestBean.getProbationContact().getValue());
        requestValue.setAntecendants(requestBean.getAntecedents().getValue());
        requestValue.setCpsOfficeForAntecendants(requestBean.getCpsOffice().getValue());
        requestValue.setCircumstancesForOffences(requestBean.getCircumstances().getValue());
        requestValue.setCommentsByCourt(requestBean.getComments().getValue());
        requestValue.setAvailableForInterview(requestBean.getAvailable().getValue());
        if (requestBean.getHearingDate().getTimestamp() != null) {
            requestValue.setHearingDate(requestBean.getHearingDate().getTimestamp());
        } else {
            requestValue.setHearingDate(null);
        }

        if (requestBean.getNoLaterThan().getTimestamp() != null) {
            requestValue.setArriveNoLaterThanDate(requestBean.getNoLaterThan().getTimestamp());
        } else {
            requestValue.setArriveNoLaterThanDate(null);
        }
    }

    /**
     * <p>
     * This method returns a probation detail bean from a set of values
     * </p>
     * 
     * @param probationValue ,
     *            probation values from which to build the bean
     * @return the populated probation bean
     */
    public static ProbationServiceDetailsBean getProbationDetails(PSRProbationValueSet probationValue) {
        AddressBean addressBean = new AddressBean(probationValue.getAddress().getAddress1(), probationValue
                .getAddress().getAddress2(), probationValue.getAddress().getAddress3(), probationValue.getAddress()
                .getAddress4(), probationValue.getAddress().getTown(), probationValue.getAddress().getCounty(),
                probationValue.getAddress().getPostcode(), probationValue.getAddress().getCountry());
        return new ProbationServiceDetailsBean(0, probationValue.getCourt().getProbationOfficeName(), addressBean,
                getContactFromHashMap("PS_Phone", probationValue.getContacts()), getContactFromHashMap("PS_Fax",
                        probationValue.getContacts()), getContactFromHashMap("PS_Email", probationValue.getContacts()));
    }

    /**
     * <p>
     * This method populates or alters a psr recipient value set according to a
     * bean
     * </p>
     * 
     * @param recipientValue ,
     *            recipient values to be added, if null a new value set will be
     *            created
     * @return the modified value set
     */
    public static PSRRecipientValueSet getModifiedRecipientValue(PSRRecipientValueSet recipientValue,
            PSRRecipientDetailsBean recipientBean) {
        if (recipientValue == null) {
            XhbPsrRecipientBasicValue recipient = new XhbPsrRecipientBasicValue();
            XhbAddressBasicValue address = new XhbAddressBasicValue();
            recipientValue = new PSRRecipientValueSet(recipient, address, new HashMap());
        }
        recipientValue.getRecipient().setRecipientName(recipientBean.getOfficeName().getValue());
        recipientValue.getAddress().setAddress1(recipientBean.getAddress().getLine1().getValue());
        recipientValue.getAddress().setAddress2(nullIfEmpty(recipientBean.getAddress().getLine2().getValue()));
        recipientValue.getAddress().setAddress3(nullIfEmpty(recipientBean.getAddress().getLine3().getValue()));
        recipientValue.getAddress().setAddress4(nullIfEmpty(recipientBean.getAddress().getLine4().getValue()));
        recipientValue.getAddress().setTown(nullIfEmpty(recipientBean.getAddress().getTown().getValue()));
        recipientValue.getAddress().setCounty(nullIfEmpty(recipientBean.getAddress().getCounty().getValue()));
        recipientValue.getAddress().setPostcode(recipientBean.getAddress().getPostcode().getValue());
        recipientValue.getAddress().setCountry(nullIfEmpty(recipientBean.getAddress().getCountry().getValue()));
        recipientValue.getRecipient().setRecipientMethodOfContact(recipientBean.getMethod());
        setContactInHashMap(recipientValue.getContacts(), "Phone", recipientBean.getTelephone().getValue());
        setContactInHashMap(recipientValue.getContacts(), "Fax", recipientBean.getFax().getValue());
        setContactInHashMap(recipientValue.getContacts(), "Email", recipientBean.getEmail().getValue());
        return recipientValue;
    }

    /**
     * <p>
     * This method populates or alters a psr probation value set according to a
     * bean
     * </p>
     * 
     * @param probationValue ,
     *            probation values to be added, if null a new value set will be
     *            created
     * @return the modified value set
     */
    public static PSRProbationValueSet getModifiedProbationValue(PSRProbationValueSet probationValue,
            ProbationServiceDetailsBean probationBean) {
        if (probationValue == null) {
            XhbCourtBasicValue court = new XhbCourtBasicValue();
            XhbAddressBasicValue address = new XhbAddressBasicValue();
            probationValue = new PSRProbationValueSet(court, address, new HashMap());
        }
        probationValue.getCourt().setProbationOfficeName(probationBean.getOfficeName().getValue());
        probationValue.getAddress().setAddress1(probationBean.getAddress().getLine1().getValue());
        probationValue.getAddress().setAddress2(nullIfEmpty(probationBean.getAddress().getLine2().getValue()));
        probationValue.getAddress().setAddress3(nullIfEmpty(probationBean.getAddress().getLine3().getValue()));
        probationValue.getAddress().setAddress4(nullIfEmpty(probationBean.getAddress().getLine4().getValue()));
        probationValue.getAddress().setTown(nullIfEmpty(probationBean.getAddress().getTown().getValue()));
        probationValue.getAddress().setCounty(nullIfEmpty(probationBean.getAddress().getCounty().getValue()));
        probationValue.getAddress().setPostcode(probationBean.getAddress().getPostcode().getValue());
        probationValue.getAddress().setCountry(nullIfEmpty(probationBean.getAddress().getCountry().getValue()));
        setContactInHashMap(probationValue.getContacts(), "PS_Phone", probationBean.getTelephone().getValue());
        setContactInHashMap(probationValue.getContacts(), "PS_Fax", probationBean.getFax().getValue());
        setContactInHashMap(probationValue.getContacts(), "PS_Email", probationBean.getEmail().getValue());
        return probationValue;
    }

    /**
     * Returns null if the string is empty, useful to stop empty strings in the
     * database where the bean allows a blank string.
     * 
     * @param s
     *            the string to evaluate
     * @return null if the string was empty
     */
    public static String nullIfEmpty(String s) {
        return ((s != null) && (s.length() > 0)) ? s : null;
    }

    /**
     * Sets a value in the contact hashmap this function is also in some values,
     * it probably should be removed from here at some point
     * 
     * @param contacts
     *            the hashMap to look in
     * @param valueName
     *            the name of the value we're trying to set
     * @param value
     *            the value we're trying to set
     */
    public static void setContactInHashMap(HashMap contacts, String valueName, String value) {
        if (contacts.get(valueName) != null) {
            XhbContactDetailBasicValue contact = (XhbContactDetailBasicValue) contacts.get(valueName);
            contact.setContactValue(value);
        } else {
            XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();
            contact.setContactType(valueName);
            contact.setContactValue(value);
            contacts.put(valueName, contact);
        }
    }

    /**
     * 
     * @return
     */
    public static String getUserName() {
        String userName = getUserDelegate().getUser("");
        log.debug("***********getUserName ::::" + userName);
        return userName;
    }

    /**
     * 
     * @return
     */
    public static UserTerminalControllerBeanBusinessDelegate getUserDelegate() {
        return UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }

}
