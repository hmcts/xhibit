package uk.gov.courtservice.xhibit.business.audittrail;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;

/**
 * <p>
 * Title: EventDataHelper
 * </p>
 * <p>
 * Description: Helper class for generating formatted strings to be included in AuditTrailEvents
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class EventDataHelper {
    public static final String PROPERTY_FILE = "audittrail";
    
    //Common
    public static final String DELETED = "deleted";
    public static final String DEF_ON_CASE_ID = "defOnCaseID";
    public static final String DEF_ON_OFFENCE_ID = "defOnOffenceID";
    public static final String DEF_CHARGE_ID = "defChargeId";
    public static final String DEFENDANT = "defendant";
    
    //Login and logoff
    public static final String SESSION_ID = "sessionId";
    
    //CourtLog
    public static final String ENTRY_ID = "entryId";
    public static final String EVENT_TYPE = "eventType";
    public static final String XML = "xml";
    
    //Defendant
    public static final String FIRST_NAME = "firstName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String SURNAME = "surname";
    public static final String INITIALS = "initials";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String ADDRESS3 = "address3";
    public static final String ADDRESS4 = "address4";
    public static final String TOWN = "town";
    public static final String POST_CODE = "postCode";
    public static final String NATIONALITY = "nationality";
    public static final String DOB = "dob";
    public static final String SEX = "sex";
    public static final String JUVENILE = "juvenile";
    public static final String MASKED = "masked";
    public static final String MASKED_NAME = "maskedName";
    public static final String LAST_CONVICTION_DATE = "lastConvictionDate";
    public static final String ASN = "asn";
    public static final String URN = "ptiUrn";
    
    //Disposal
    public static final String DISPOSAL_TYPE = "disposalType";
    public static final String DISPOSAL_LINE_NO = "disposalLineNo";
    public static final String DISPOSAL_LINE_DATA = "disposalLineData";
    public static final String DISPOSAL_LINES_START = "disposalLinesStart";
    public static final String DISPOSAL_LINES_END = "disposalLinesEnd";
    
    //Plea
    public static final String PLEA_CODE = "pleaCode";
    public static final String PLEA = "plea";
    public static final String PLEA_ADDITIONAL_INFO = "additionalInfo";
    public static final String ARRAIGNMENT_DATE = "arraignmentDate";
    
    //Verdict
    public static final String VERDICT = "verdict";
    public static final String JURORS_ASSENTING = "jurorsAssenting";
    public static final String JURORS_DISSENTING = "jurorsDissenting";
    public static final String VERDICT_CODE = "verdictCode";
    public static final String VERDICT_DATE = "verdictDate";
    public static final String OTHER_VERDICT_TEXT = "otherVerdictText";
    
    //RoleMapping
    public static final String ROLE_REMOVED = "removed";
    public static final String ROLE_ADDED = "added";
    public static final String ROLE = "role";
    public static final String GROUP = "group";    
    
    //System Errors
    public static final String ERROR = "error";
    public static final String ERROR_CS_RF = "errorCsRf";
    
    public static final String SEP = ", ";
    public static final String EQUALS = " = ";
    
    /**
     * Returns a formatted String in the format "key = value". Includes a seperator if the firstValue
     * argument is not true.
     * 
     * @param key
     * @param value
     * @param firstValue
     * @return
     */
    public static String getString(String key, String value, boolean firstValue){        
        StringBuffer s = new StringBuffer();
        
        if(!firstValue){
            s.append(SEP);
        }
        String name = getValue(key);
        s.append(name);
        s.append(EQUALS);
        s.append(value);
        
        return s.toString();
    }
    
    /**
     * Looks up the given key from the audittrail.properties file
     * @param key
     * @return
     */
    public static String getValue(String key){
        return CSServices.getConfigServices().getProperties("audittrail").
            getProperty(key);
    }
    
    /**
     * Returns a string to prefix the event with if the object ahs been deleted
     * @return
     */
    public static String getDeleted(){
        return getValue(DELETED) + " ";
    }
    
    /**
     * Given a DefendantOnCaseId, this method returns the defendant name
     * @param defOnCaseId
     * @return
     */
    public static String getDeftDetailsFromDocId(Integer defOnCaseId){
        XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId);
        XhbDefendant def = doc.getXhbDefendant();
        return getDeftName(def);
    }
    
    /**
     * Given a defendantOnOffenceId, this method returns the defendant name
     * @param defOnOffenceId
     * @return
     */
    public static String getDeftDetailsFromDofId(Integer defOnOffenceId){
        XhbDefendantOnOffence dof = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defOnOffenceId);
        XhbDefendant def = dof.getXhbDefendantOnCase().getXhbDefendant();
        return getDeftName(def);
    }
    
    /**
     * Given a defendant object, this method returns teh defendant name in the format "Surname, Firstname Msiddlename"
     * @param defendant
     * @return
     */
    private static String getDeftName(XhbDefendant defendant){
        StringBuffer s = new StringBuffer();
        s.append(defendant.getSurname());
        s.append(", ");
        s.append(defendant.getFirstName());
        s.append(", ");
        s.append(defendant.getMiddleName());
        return s.toString();
    }
}
