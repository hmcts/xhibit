package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BailOrderBCaseStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BailOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BailSurety;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Contact;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Distance;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LiveSleep;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Other;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Passport;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PoliceReport;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Security;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: BailOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Bail Order
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Surtar Bachra/Bob Boothby/Neil Entwistle
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 01-09-2003 AW Daley validatePostLiveSleep method modified to validated line 1
 * and line 2 of the address
 */

public class BailOrderHelper {
    private static final Logger log = CSServices.getLogger(BailOrderHelper.class);

    private static final String SURETY_AMOUNT_MESSAGE = "ORDER_Bail_Surety_Amount_incorrect";

    private static final String PASSPORT_SURR_RET_MESSAGE = "ORDER_Bail_Passport_Surrendered_Retained_incorrect";

    private static final String SECURITY_AMOUNT_MESSAGE = "ORDER_Bail_Security_Amount_incorrect";

    private static final String LIVE_SLEEP_POSTCODE_MESSAGE = "ORDER_Live_Sleep_PostCode_format_incorrect";

    private static final String POLICE_REPORT_PERIOD_MESSAGE = "ORDER_Police_Report_Period_incorrect";

    private static final String POLICE_REPORT_STATION_MESSAGE = "ORDER_Police_Report_Station_incorrect";

    private static final String CONTACT_DETAILS_MESSAGE = "ORDER_Contact_Details_incorrect";

    private static final String DISTANCE_DISTANCE_MESSAGE = "ORDER_Distance_Distance_incorrect";

    private static final String DISTANCE_LOCATION_MESSAGE = "ORDER_Distance_Location_incorrect";

    private static final String OTHER_DETAILS_MESSAGE = "ORDER_Other_Details_incorrect";

    private static final String LIVE_SLEEP_ADDRESS_MESSAGE = "ORDER_Live_Sleep_Address_incorrect";

    private static final String PRE_CONDITIONS_MESSAGE = "ORDER_Pre_conditions_incorrect";

    private static final String POST_CONDITIONS_MESSAGE = "ORDER_Post_conditions_incorrect";
    
    private static final String DEFENDANT_DETAILS_MESSAGE = "ORDER_Defendant_details_incorrect";
    
    private static final String DEFENDANT_NAME_MESSAGE = "ORDER_Defendant_name_invalid";
    
    private static final String DEFENDANT_DOB_MESSAGE = "ORDER_Defendant_dob_invalid";
    
    private static final String DEFENDANT_POSTCODE_MESSAGE = "ORDER_Defendant_postcode_invalid";
    
    private static final String DEFENDANT_LINE1_MESSAGE = "ORDER_Defendant_line1_invalid";
    
    private static final String DEFENDANT_LINE2_MESSAGE = "ORDER_Defendant_line2_invalid";

    private static final String POSTCODE_REG_EXP = // "(GIR
    // 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))
    // [0-9][A-Z]{2})";
    "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
            + "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";

    /**
     * Utility methodValidation
     * 
     * @param bos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateBailOrder(BailOrderStructure bos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Bail Order: Logical Validation");
        validateDefendantDetailsSelected(bos, helper);
        validateBeforeReleaseConditions(bos, helper);
        validateAfterReleaseConditions(bos, helper);
    }
    
    
    /**
     * Validate the Pre Release Conditions
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validateBeforeReleaseConditions(BailOrderStructure bos, ValidationHelper helper) {
        // check to ensure that the PostConditions has been selected
        if (bos.getBailPreConditions().hasSelected() && bos.getBailPreConditions().getSelected()) {
            log.debug("*******************Pre Conditions Selected");
            // Check at least one option selected - PR58476
            validatePreConditionsSelected(bos, helper);
            validatePreBailSurety(bos, helper);
            validatePrePassport(bos, helper);
            validatePreSecurity(bos, helper);
        }
    }
    

    /**
     * Validate that at least one of the preconditions is selected
     * 
     * @param bos
     * @param helper
     */
    private static void validatePreConditionsSelected(BailOrderStructure bos, ValidationHelper helper) {
        log.debug("*****************validatePreConditionsSelected");
        if (!bos.getBailPreConditions().getBailSurety().getSelected()
                && bos.getBailPreConditions().getBailSurety().hasSelected()
                && !bos.getBailPreConditions().getPassport().getSelected()
                && bos.getBailPreConditions().getPassport().hasSelected()
                && !bos.getBailPreConditions().getSecurity().getSelected()
                && bos.getBailPreConditions().getSecurity().hasSelected()) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(PRE_CONDITIONS_MESSAGE, null);
        }
    }
    
    
    /**
     * Validate that at least one of the preconditions is selected
     * 
     * @param bos
     * @param helper
     */
    private static void validateDefendantDetailsSelected(BailOrderStructure bos, ValidationHelper helper) {
        log.debug("*****************validatePreConditionsSelected");
        
        // Check name/address/postcode for B cases only
        if ((bos.getOrderHeader().getCaseNumber() != null) && (bos.getOrderHeader().getCaseNumber().startsWith("B"))) { 
            if (bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress() != null) {
                if (bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getLine()[0] != null &&
                        bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getLine()[0].trim().length() > 0) {
                    log.debug("validateDefendantDetailsSelected::Bail Order: address line 1 is ok");
                } else {
                    helper.addLogicalProblem(DEFENDANT_LINE1_MESSAGE, null);
                    log.debug("validateDefendantDetailsSelected::Bail Order error: address line 1 is empty");
                }
                if (bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getLine()[1] != null &&
                        bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getLine()[1].trim().length() > 0) {
                    log.debug("validateDefendantDetailsSelected::Bail Order: address line 2 is ok");
                } else {
                    helper.addLogicalProblem(DEFENDANT_LINE2_MESSAGE, null);
                    log.debug("validateDefendantDetailsSelected::Bail Order error: address line 2 is empty");
                }
                if (bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getPostCode() != null &&
                        !bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getPostCode().equals("AA1 1AA") &&
                        bos.getOrderHeader().getDefendant().getPersonalDetails().getAddress().getPostCode().trim().length() > 0) {
                    log.debug("validateDefendantDetailsSelected::Bail Order: postcode is ok");
                } else {
                    helper.addLogicalProblem(DEFENDANT_POSTCODE_MESSAGE, null);
                    log.debug("validateDefendantDetailsSelected::Bail Order error: postcode is empty");
                }
            } else {
                log.debug("validateDefendantDetailsSelected::Bail Order error: defendant address is null");
            }
            
            // Check date of birth
            if (bos.getOrderHeader().getDefendant().getPersonalDetails().getDateOfBirth() != null &&
                    bos.getOrderHeader().getDefendant().getPersonalDetails().getDateOfBirth().getBirthDate() != null) {
                log.debug("validateDefendantDetailsSelected::Bail Order: defendant date of birth is ok");
            } else {
                helper.addLogicalProblem(DEFENDANT_DOB_MESSAGE, null);
                log.debug("validateDefendantDetailsSelected::Bail Order error: defendant date of birth is empty");
            }
            
            // Check name
            if (bos.getOrderHeader().getDefendant().getPersonalDetails().getName() != null) {
             
                if (bos.getOrderHeader().getDefendant().getPersonalDetails().getName().getCitizenNameSurname() != null) {
                    log.debug("validateDefendantDetailsSelected::Bail Order: defendant name is ok");
                } else {
                    helper.addLogicalProblem(DEFENDANT_NAME_MESSAGE, null);
                    log.debug("validateDefendantDetailsSelected::Bail Order error: defendant name is empty");
                }
            } else {
                log.debug("validateDefendantDetailsSelected::Bail Order error: defendant name is null");
            }
        }
        
    }

    /**
     * Validate the Pre Condition - Surety
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePreBailSurety(BailOrderStructure bos, ValidationHelper helper) {
        BailSurety bailSurety = bos.getBailPreConditions().getBailSurety();
        // check to ensure that an amount has been entered if the Surety
        // option has been selected
        if (bailSurety.hasSelected() && bailSurety.getSelected()) {
            log.debug("*****************Bail Surety Selected");
            // check to ensure that the amount is > £0.00
            if (bailSurety.getMonetaryValue().getAmount().doubleValue() <= 0.00) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(SURETY_AMOUNT_MESSAGE, null);
            }
        }
    }
    

    /**
     * Validate the Pre Condition - Passport
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePrePassport(BailOrderStructure bos, ValidationHelper helper) {
        Passport passport = bos.getBailPreConditions().getPassport();
        // check to ensure that a passport sub-option is selected if the main
        // Bail Passport option has been selected
        if (passport.hasSelected() && passport.getSelected()) {
            log.debug("*****************Passport Conditions Selected");
            // check to ensure that a passport sub-option has been selected
            if (!passport.getSurrendered() // Surrendered NOT selected
                    && // AND
                    !passport.getRetained()) // Retained NOT selected
            {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(PASSPORT_SURR_RET_MESSAGE, null);
            }
        }
    }
    

    /**
     * Validate the Pre Condition - Security
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePreSecurity(BailOrderStructure bos, ValidationHelper helper) {
        Security bailSecurity = bos.getBailPreConditions().getSecurity();
        // check to ensure that an amount has been entered if the Security
        // option has been selected
        if (bailSecurity.hasSelected() && bailSecurity.getSelected()) {
            log.debug("*****************Bail Security Selected");
            // check to ensure that the amount is > £0.00
            if (bailSecurity.getMonetaryValue().getAmount().doubleValue() <= 0.00) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(SECURITY_AMOUNT_MESSAGE, null);
            }
        }
    }
    

    /**
     * Validate all the After Release Conditions
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validateAfterReleaseConditions(BailOrderStructure bos, ValidationHelper helper) {
        // check to ensure that the PostConditions has been selected
        if (bos.getBailPostConditions().hasSelected() && bos.getBailPostConditions().getSelected()) {
            log.debug("*******************Post Conditions Selected");
            validatePostConditionsSelected(bos, helper);
            validatePostLiveSleep(bos, helper);
            validatePostPoliceReport(bos, helper);
            validatePostContact(bos, helper);
            validatePostDistance(bos, helper);
            validatePostOther(bos, helper);
        }
    }
    

    /**
     * Validate that at least one of the postconditions is selected
     * 
     * @param bos
     * @param helper
     */
    private static void validatePostConditionsSelected(BailOrderStructure bos, ValidationHelper helper) {
        log.debug("*****************validatePostConditionsSelected");
        if (!bos.getBailPostConditions().getLiveSleep().getSelected()
                && bos.getBailPostConditions().getLiveSleep().hasSelected()
                && !bos.getBailPostConditions().getNotifyPolice().getSelected()
                && bos.getBailPostConditions().getNotifyPolice().hasSelected()
                && !bos.getBailPostConditions().getCurfew().getSelected()
                && bos.getBailPostConditions().getCurfew().hasSelected()
                && !bos.getBailPostConditions().getPoliceReport().getSelected()
                && bos.getBailPostConditions().getPoliceReport().hasSelected()
                && !bos.getBailPostConditions().getAvailable().getSelected()
                && bos.getBailPostConditions().getAvailable().hasSelected()
                && !bos.getBailPostConditions().getContact().getSelected()
                && bos.getBailPostConditions().getContact().hasSelected()
                && !bos.getBailPostConditions().getDistance().getSelected()
                && bos.getBailPostConditions().getDistance().hasSelected()
                && !bos.getBailPostConditions().getTravelWarrants().getSelected()
                && bos.getBailPostConditions().getTravelWarrants().hasSelected() 
                && !bos.getBailPostConditions().getOther().getSelected()
                && bos.getBailPostConditions().getOther().hasSelected()) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(POST_CONDITIONS_MESSAGE, null);
        }
    }
    

    /**
     * Validate the Post Condition - Live/Sleep
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePostLiveSleep(BailOrderStructure bos, ValidationHelper helper) {
        LiveSleep liveSleep = bos.getBailPostConditions().getLiveSleep();

        // Get Default LiveSleep from empty schema
        
        LiveSleep defaultLiveSleep = null;
        
        if (helper.getDefaultOrder().getBailOrder() != null) {
            defaultLiveSleep = helper.getDefaultOrder().getBailOrder().getBailPostConditions().getLiveSleep();
        } else {
            defaultLiveSleep = helper.getDefaultOrder().getBailOrderBCase().getBailPostConditions().getLiveSleep();
        }

        // check to ensure that the Live/Sleep condition has been selected
        if (liveSleep.hasSelected() && liveSleep.getSelected())

            log.debug("*****************Live Sleep Selected");
        // check to ensure that the AtFollowing is True
        if (liveSleep.getAtFollowingAddress()) {
            String postCode = liveSleep.getAddress().getPostCode();

            log.debug("*********************Post Code : " + postCode);

            if (postCode != null && postCode.length() > 0) {
                RE regexp = null;
                try {
                    regexp = new RE(POSTCODE_REG_EXP);

                    boolean matched = regexp.match(postCode);
                    log.debug("*******************MATCHED : " + matched);
                    if (!matched) {
                        // invalid postcode format
                        log.debug("*************** PROBLEM ADDED");
                        helper.addLogicalProblem(LIVE_SLEEP_POSTCODE_MESSAGE, null);
                    }
                } catch (RESyntaxException e) {
                    e.printStackTrace();
                    log.error(e);
                }
            }

            try {
                // Validate address line 1 and address line 2
                Address address = liveSleep.getAddress();
                Address defaultAddress = defaultLiveSleep.getAddress();

                if (!isAddressValid(address, defaultAddress)) {
                    helper.addLogicalProblem(LIVE_SLEEP_ADDRESS_MESSAGE, null);
                }
            }

            catch (java.lang.IndexOutOfBoundsException e) {
                e.printStackTrace();
                log.error(e);
            }
        }
    }
    

    /**
     * Validate the Post Condition - Police Report
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePostPoliceReport(BailOrderStructure bos, ValidationHelper helper) {
        PoliceReport policeReport = bos.getBailPostConditions().getPoliceReport();
        // check to ensure that the Police Report condition has been selected
        if (policeReport.hasSelected() && policeReport.getSelected()) {
            log.debug("*****************Police Report Selected");
            // check to ensure that the Police Report station and period
            // have been set
            if (isEmptyString(policeReport.getStation())) {
                // No police station entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(POLICE_REPORT_STATION_MESSAGE, null);
            }
            // Period
            if (isEmptyString(policeReport.getPeriod())) {
                // No period entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(POLICE_REPORT_PERIOD_MESSAGE, null);
            }

        }
    }
    

    /**
     * Validate the Post Condition - Contact
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePostContact(BailOrderStructure bos, ValidationHelper helper) {
        Contact contact = bos.getBailPostConditions().getContact();
        // check to ensure that the Contact condition has been selected
        if (contact.hasSelected() && contact.getSelected()) {
            log.debug("*****************Contact Selected");
            // check to ensure that the Contact details have been set
            if (isEmptyString(contact.getContent())) {
                // No contact details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(CONTACT_DETAILS_MESSAGE, null);
            }
        }
    }
    

    /**
     * Validate the Post Condition - Distance
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePostDistance(BailOrderStructure bos, ValidationHelper helper) {
        Distance distance = bos.getBailPostConditions().getDistance();
        // check to ensure that the Distance condition has been selected
        if (distance.hasSelected() && distance.getSelected()) {
            log.debug("*****************Distance Selected");
            // check to ensure that the Distance details have been set
            if (isEmptyString(distance.getTheDistance())) {
                // No distance details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DISTANCE_DISTANCE_MESSAGE, null);
            }
            // check to ensure that the Distance location details have been
            // set
            if (isEmptyString(distance.getLocation())) {
                // No distance location entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DISTANCE_LOCATION_MESSAGE, null);
            }
        }
    }
    

    /**
     * Validate the Post Condition - Other
     * 
     * @param bos
     *            the bail order
     * @param helper
     *            the helper
     */
    private static void validatePostOther(BailOrderStructure bos, ValidationHelper helper) {
        Other other = bos.getBailPostConditions().getOther();
        // check to ensure that the Other condition has been selected
        if (other.hasSelected() && other.getSelected()) {
            log.debug("*****************Other Selected");
            // check to ensure that the Other details have been set
            if (isEmptyString(other.getContent())) {
                // No other details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(OTHER_DETAILS_MESSAGE, null);
            }
        }
    }
    

    // Utility Methods

    /**
     * Checks if a passed string is null, empty or a single space
     * 
     * @param value
     *            the string
     * @return true if string is null, empty or a single space
     */
    private static boolean isEmptyString(String value) {
        boolean result = false;
        if (value == null // if string is null
                || // OR
                value.equals("") // empty string
                || // OR
                value.equals(" ")) // single space
        {
            result = true;
        }
        return result;
    }

    /**
     * Validates the address. The first two lines of the address must be
     * specified
     * 
     * @param address
     * @return true if valid.
     */
    private static boolean isAddressValid(Address address, Address defaultAddress)
            throws java.lang.IndexOutOfBoundsException {
        // Validate that the first two lines of the address
        // are set.
        String addressLine1 = address.getLine(0);

        if (addressLine1.equals("") || addressLine1.equals(defaultAddress.getLine(0)))
            return false;

        String addressLine2 = address.getLine(1);

        if (addressLine2.equals("") || addressLine2.equals(defaultAddress.getLine(1)))
            return false;

        return true;
    }

}
