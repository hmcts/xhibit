package uk.gov.courtservice.xhibit.business.services.charge;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: ChargeEventHelper
 * </p>
 * <p>
 * Description: Creates court log events in the Charges area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ChargeEventHelper.java,v 1.44 2010/05/14 17:19:33 warsoph Exp $
 * 
 * @todo In future CJSE event parameters will need to be added to the court log
 *       events created in this class. However these events are not being sent
 *       to the CJSE for the October 03 release so the events will not be
 *       populated here.
 * 
 */
public class ChargeEventHelper implements CourtLogEvents, UncodedOffenceInterface {
    private static final Logger log = CSServices.getLogger(ChargeEventHelper.class);

    private final ResourceBundle messages = CSServices.getConfigServices().getBundle("CourtLogFreeText");

    public ChargeEventHelper() {
        super();
    }

    /**
     * Creates the relevant court log entry for the type of 'stay' event.
     * Additional parameters for CJSE are added for future use but will not be
     * used currently as the events do no go to CJSE for the Octobe 03 release.
     * 
     * @param defOnOffenceBasicValue
     *            Details of the defendant on offence
     * @param logEntry
     *            The court log entry to create
     * @throws CourtLogBusinessException
     */
    public void logStayEvent(XhbDefendantOnOffenceBasicValue defOnOffenceBasicValue, CourtLogCRUDValue logEntry)
            throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("logStayEvent() with defOnOffenceBasicValue: " + defOnOffenceBasicValue + " logEntry: "
                    + logEntry);
        }

        // get details for the court log event property map

        // find the defendant
        XhbDefendantOnOffence doo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defOnOffenceBasicValue
                .getDefendantOnOffenceId());
        XhbDefendant defendant = doo.getXhbDefendantOnCase().getXhbDefendant();
        String defendantName = getDefendantName(defendant);

        // find the offence \ charge \ case details
        Integer offenceId = defOnOffenceBasicValue.getOffenceId();
        XhbOffence offence = XhbOffenceBeanHelper2.findByPrimaryKey(offenceId);

        Integer countNum = offence.getCrestOffenceSeqNo();
        Integer indictmentNum = offence.getXhbCharge().getCrestChargeSeqNo();
        XhbCase caze = offence.getXhbCharge().getXhbCase();
        String caseNumber = caze.getCaseType() + caze.getCaseNumber();

        // create the property map for the court log event
        HashMap<String, Comparable> stayOptions = new HashMap<String, Comparable>();

        // check which Stay action we are performing
        if (logEntry.getEventType().equals(new Integer(40204))) {
            // defendant on count
            stayOptions.put("E40204_Stay_Defendant_Name", defendantName);
            stayOptions.put("E40204_Stay_Count_Number", countNum);
            stayOptions.put("E40204_Stay_Indictment_Number", indictmentNum);
            stayOptions.put("E40204_Stay_Case_Number", caseNumber);
            logEntry.setProperty("E40204_Stay_Options", stayOptions);
        } else if (logEntry.getEventType().equals(new Integer(40205))) {
            // defendant on indictment
            stayOptions.put("E40205_Stay_Defendant_Name", defendantName);
            stayOptions.put("E40205_Stay_Indictment_Number", indictmentNum);
            stayOptions.put("E40205_Stay_Case_Number", caseNumber);
            logEntry.setProperty("E40205_Stay_Options", stayOptions);
        } else if (logEntry.getEventType().equals(new Integer(40207))) {
            // stay count
            stayOptions.put("E40207_Stay_Count_Number", countNum);
            stayOptions.put("E40207_Stay_Indictment_Number", indictmentNum);
            stayOptions.put("E40207_Stay_Case_Number", caseNumber);
            logEntry.setProperty("E40207_Stay_Options", stayOptions);
        } else if (logEntry.getEventType().equals(new Integer(40206))) {
            // stay indictement
            stayOptions.put("E40206_Stay_Indictment_Number", indictmentNum);
            stayOptions.put("E40206_Stay_Case_Number", caseNumber);
            logEntry.setProperty("E40206_Stay_Options", stayOptions);
        }

        // create the entry
        CourtLogWorkFlow.newEntry(logEntry, false);

        log.debug("logStayEvent() exited");
    }

    /**
     * Court Log Event for Exporting Indictments.
     * 
     * @param caseID
     * @param scheduledHearingID
     * @throws CourtLogBusinessException
     */
    public void logExportIndictments(Integer caseID, Integer scheduledHearingID) throws CourtLogBusinessException {
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();
        logEntry.setEventType(EXPORT_IND);
        logEntry.setCaseId(caseID);
        logEntry.setScheduledHearingId(scheduledHearingID);
        logEntry.setEntryDate(Calendar.getInstance().getTime());

        CourtLogWorkFlow.newEntry(logEntry, false);
    }

    /**
     * Create the court log event for adding a count or offence
     * 
     * @param offenceValue
     *            The offence being added
     * @throws CourtLogBusinessException
     */
    public void addOffenceCourtLog(OffenceValue offenceValue) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("addOffenceCourtLog() start with " + "OffenceValue : " + offenceValue);
        }

        // check the charge type
        XhbCharge chargeBean = getCharge(offenceValue.getChargeID());
        String chargeType = chargeBean.getChargeType();

        String offenceNoDesc = null;
        // for an indictment we need the offence sequence number
        if (offenceValue.getOffenceID() != null && chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            offenceNoDesc = findCrestOffenceSequenceNumber(offenceValue.getOffenceID());
        } else // otherwise we need the description
        {
            offenceNoDesc = offenceValue.getOffenceDescription();
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(offenceValue.isInCourt());
        logEntry.setCaseId(offenceValue.getCaseID());

        // set the event type and freetext
        setOffenceEventTypeAndFreetext(chargeBean.getCrestChargeSeqNo(), chargeType, offenceNoDesc, logEntry);

        if (offenceValue.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("addOffence() - CourtLogDate has not been set on the OffenceValue.");
        }
        logEntry.setEntryDate(offenceValue.getCourtLogDate().getTime());

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("addOffenceCourtLog() end");
    }

    /**
     * Create the court log event for adding a count to a joinder
     * 
     * @param offenceValue
     *            The offence being added
     * @throws CourtLogBusinessException
     */
    public void addJoinderOffenceCourtLog(OffenceValue offenceValue) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("addOffenceCourtLog() start with " + "OffenceValue : " + offenceValue);
        }

        // check the charge type
        XhbCharge chargeBean = getCharge(offenceValue.getChargeID());
        String chargeType = chargeBean.getChargeType();

        String offenceNoDesc = null;
        // for an indictment we need the offence sequence number
        if (offenceValue.getOffenceID() != null && chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            offenceNoDesc = findCrestOffenceSequenceNumber(offenceValue.getOffenceID());
        } else // otherwise we need the description
        {
            offenceNoDesc = offenceValue.getOffenceDescription();
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(offenceValue.isInCourt());
        logEntry.setCaseId(offenceValue.getCaseID());

        // set the event type and freetext
        //setOffenceEventTypeAndFreetext(chargeBean.getCrestChargeSeqNo(), chargeType, offenceNoDesc, logEntry);
        logEntry.setEventType(CourtLogEvents.ADD_COUNT_TO_JOINDER);
        logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addCountToJoinder"),
                new Object[] {offenceNoDesc, chargeBean.getCrestChargeSeqNo() }));

        if (offenceValue.getCourtLogDate() == null) {
            throw new CSUnrecoverableException(
                    "addJoinderOffence() - CourtLogDate has not been set on the OffenceValue.");
        }
        logEntry.setEntryDate(offenceValue.getCourtLogDate().getTime());

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("addOffenceCourtLog() end");
    }

    private XhbCharge getCharge(Integer chargeId) {
        return XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
    }

    /**
     * Create a court log event for Add Defendant to Count or Add Count to
     * Defendant
     * 
     * @param linkVal
     *            The count and defendant being linked
     * @param doof
     *            The defendant on offence value
     * @throws CourtLogBusinessException
     */
    public void linkCountDefCourtLog(LinkCountDefValue linkVal, DefendantOnOffenceValue doof)
            throws CourtLogBusinessException {
        final String methodName = "linkCountDefCourtLog():: ";
        if (log.isDebugEnabled()) {
            log.debug(methodName + " start with LinkCountDefValue : " + linkVal + " DefendantOnOffenceValue : " + doof);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(linkVal.isInCourt());
        logEntry.setCaseId(linkVal.getCaseID());
        logEntry.setEventType(CourtLogEvents.ADD_COUNT_TO_DEFENDANT);

        // set the date
        if (linkVal.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("linkCountsAndDefendants() - CourtLogDate has not been "
                    + "set on the LinkCountDefValue.");
        }
        logEntry.setEntryDate(linkVal.getCourtLogDate().getTime());

        // set the freetext
        XhbOffence offence = XhbOffenceBeanHelper2.findByPrimaryKey(doof.getOffenceId());

        // String msgString =
        // linkVal.isAddDefendantToCount()?"linkDefCount":"linkCountDef";

        String freeText = "";
        if (linkVal.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
            log.debug(methodName + " indictment found");
            freeText = MessageFormat.format(messages.getString("linkCountDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), offence.getXhbCharge().getCrestChargeSeqNo(),
                    getDefendantName(doof.getDefendantId()) });
        } else if (linkVal.getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
            log.debug(methodName + " breach found");
            freeText = MessageFormat.format(messages.getString("linkBreachDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), offence.getXhbCharge().getCrestChargeSeqNo(),
                    getDefendantName(doof.getDefendantId()) });
        } else if (linkVal.getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            log.debug(methodName + " failure to appear found");
            freeText = MessageFormat.format(messages.getString("linkFail2AppearDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), offence.getXhbCharge().getCrestChargeSeqNo(),
                    getDefendantName(doof.getDefendantId()) });
        } else if (linkVal.getChargeType().equals(ChargeTypes.SECTION_41.getChargeType())) {
            log.debug(methodName + " S41 found");
            freeText = MessageFormat.format(messages.getString("linkS41OffenceDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), getDefendantName(doof.getDefendantId()) });
        } else if (linkVal.getChargeType().equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            log.debug(methodName + " C4S found");
            freeText = MessageFormat.format(messages.getString("linkC4SOffenceDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), getDefendantName(doof.getDefendantId()) });
        } else if (linkVal.getChargeType().equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
        	log.debug(methodName + " Appeal found");
        	freeText = MessageFormat.format(messages.getString("linkAppealOffenceDef"), new Object[] {
                    offence.getCrestOffenceSeqNo(), getDefendantName(doof.getDefendantId()) });
        }
        logEntry.setEntryFreeText(freeText);
        // set property map (currently used to indicate if it is add defendant
        // to count or add count to defendant
        setPropertyMap(logEntry, linkVal);

        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("linkCountDefCourtLog() end");
    }

    /**
     * Create a court log event for adding an Indictment or Breach to the case
     * 
     * @param chargeValue
     *            The charge being added
     * @return the CourtLogCRUDValue used to create the event
     * @throws CourtLogBusinessException
     * @throws BisRefControllerException
     */
    public CourtLogCRUDValue addChargeCourtLog(ChargeValue chargeValue) throws CourtLogBusinessException,
            BisRefControllerException {
        if (log.isDebugEnabled()) {
            log.debug("addChargeCourtLog() start with " + "ChargeValue : " + chargeValue);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // Only log if this is an Indictment or a Breach, otherwise just the
        // offences will be logged
        Integer eventTypeCode = null;
        String freeText = null;
        Collection offenceValues = chargeValue.getOffenceValues();

        if (chargeValue.getChargeTypeDescription().equals(ChargeTypes.INDICTMENT.getTypeDescription())) {
            eventTypeCode = CourtLogEvents.ADD_INDICTMENT;
            freeText = MessageFormat.format(messages.getString("addIndictment"), new Object[] {
                    findCrestChargeSequenceNumber(chargeValue.getChargeID()), getCaseNumber(chargeValue.getCaseID()) });
        } else if (chargeValue.getChargeTypeDescription().equals(ChargeTypes.BREACH.getTypeDescription())) {
            eventTypeCode = CourtLogEvents.ADD_BREACH;
            if (offenceValues != null && !offenceValues.isEmpty()) {
                freeText = MessageFormat.format(messages.getString("addBreachWithOffences"), new Object[] {
                        getBreachDescription(chargeValue.getBreachValue().getRefSystemCodeID()),
                        getBreachOffences(offenceValues), getCaseNumber(chargeValue.getCaseID()) });
            } else {
                freeText = MessageFormat.format(messages.getString("addBreach"), new Object[] {
                        getBreachDescription(chargeValue.getBreachValue().getRefSystemCodeID()),
                        getCaseNumber(chargeValue.getCaseID()) });
            }
        }

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(chargeValue.isInCourt());
        logEntry.setCaseId(chargeValue.getCaseID());
        if (chargeValue.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("addChargeCourtLog() - CourtLogDate has not been set on "
                    + "the ChargeValue.");
        }
        logEntry.setEntryDate(chargeValue.getCourtLogDate().getTime());

        if (eventTypeCode != null) {
            logEntry.setEventType(eventTypeCode);
            logEntry.setEntryFreeText(freeText);

            
            CourtLogWorkFlow.newEntry(logEntry, false);
        }
        log.debug("addChargeCourtLog() end, returning : " + logEntry);
        return logEntry;
    }

    /**
     * Create a court log event for a count or offence created at the same time
     * as adding the charge
     * 
     * @param chargeType
     *            The type of charge
     * @param logEntry
     *            The court log entry to create, part populated during the
     *            charge creation
     * @param offenceVal
     *            The offence to be added
     * @throws CourtLogBusinessException
     */
    public void logRelatedOffence(String chargeType, CourtLogCRUDValue logEntry, OffenceValue offenceVal)
            throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("logRelatedOffence() start with chargeType : " + chargeType + " CourtLogCRUDValue : " + logEntry
                    + " OffenceValue : " + offenceVal);
        }

        // create the log entry for this event
        setOffenceEventTypeAndFreetext(null, chargeType, offenceVal.getOffenceDescription(), logEntry);

        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("logRelatedOffence() end");
    }

    public void logRelatedCount(String chargeType, CourtLogCRUDValue logEntry, Integer chargeId, Integer offenceId)
            throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("logRelatedCount() start with offenceId : " + offenceId + " CourtLogCRUDValue : " + logEntry);
        }

        // for an indictment we need the offence sequence number
        String offenceNoDesc = findCrestOffenceSequenceNumber(offenceId);

        // create the log entry for this event
        setOffenceEventTypeAndFreetext(findCrestChargeSequenceNumber(chargeId), chargeType, offenceNoDesc, logEntry);

        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("logRelatedOffence() end");
    }

    /**
     * Creates a court log entry for updating a count\offence
     * 
     * @param offenceValue
     *            The offence to be updated
     * @throws CourtLogBusinessException
     */
    public void updateOffenceLogEntry(OffenceValue offenceValue) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("updateOffenceLogEntry() start with offenceValue : " + offenceValue);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // check the charge type
        XhbCharge chargeBean = getCharge(offenceValue.getChargeID());
        String chargeType = chargeBean.getChargeType();
        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.AMEND_COUNT);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateCount"), new Object[] {
                    offenceValue.getCrestOffenceSeqNo(), chargeBean.getCrestChargeSeqNo() }));
        } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.AMEND_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateCommittalOffence"),
                    new Object[] { offenceValue.getOffenceDescription() }));
        } else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.AMEND_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateSummaryOffence"),
                    new Object[] { offenceValue.getOffenceDescription() }));
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.AMEND_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateFail2AppearOffence"),
                    new Object[] { offenceValue.getOffenceDescription() }));
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
        	logEntry.setEventType(CourtLogEvents.AMEND_OFFENCE);
        	logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateAppealOffence"),
                    new Object[] { offenceValue.getOffenceDescription() }));
        } else {
            logEntry.setEventType(CourtLogEvents.AMEND_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateBreachOffence"),
                    new Object[] { offenceValue.getOffenceDescription() }));
        }
        // set the value which notifies if the case is active in court
        logEntry.setInCourt(offenceValue.isInCourt());
        logEntry.setCaseId(offenceValue.getCaseID());
        if (offenceValue.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("updateOffenceInternal() - CourtLogDate has not been "
                    + "set on the OffenceValue.");
        }
        logEntry.setEntryDate(offenceValue.getCourtLogDate().getTime());

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("updateOffenceLogEntry() end");
    }

    /**
     * Creates a court log event for deleteing a count or offence
     * 
     * @param delOffenceVal
     *            Describes the count\offence being deleted
     * @throws CourtLogBusinessException
     * @throws BisRefControllerException
     */
    public void deleteOffenceCourtLog(DelOffenceValue delOffenceVal) throws CourtLogBusinessException,
            BisRefControllerException {
        if (log.isDebugEnabled()) {
            log.debug("deleteOffenceCourtLog() start with delOffenceVal : " + delOffenceVal);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // check the charge type
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(delOffenceVal.getChargeID());
        String chargeType = chargeBean.getChargeType();
        XhbOffence offence = XhbOffenceBeanHelper2.findByPrimaryKey(delOffenceVal.getOffenceID());
        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_COUNT);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteCount"), new Object[] {
                    offence.getCrestOffenceSeqNo(), chargeBean.getCrestChargeSeqNo() }));

        } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteCommittalOffence"),
                    new Object[] { getOffenceDesc(offence) }));
        } else if (chargeType.equals(ChargeTypes.BREACH.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteBreach"),
                    new Object[] { getOffenceDesc(offence) }));
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteFailToAppear"),
                    new Object[] { getOffenceDesc(offence) }));
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
        	logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
        	logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteAppealOffence"),
                    new Object[] { getOffenceDesc(offence) }));
    	} else {
            logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteSummaryOffence"),
                    new Object[] { getOffenceDesc(offence) }));
        }
        // set the value which notifies if the case is active in court
        logEntry.setInCourt(delOffenceVal.isInCourt());
        logEntry.setCaseId(delOffenceVal.getCaseID());
        if (delOffenceVal.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("deleteOffenceInternal() - CourtLogDate has not been set "
                    + "on the DelOffenceValue.");
        }
        logEntry.setEntryDate(delOffenceVal.getCourtLogDate().getTime());

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("deleteOffenceCourtLog() end");
    }

    /**
     * Adds a court log event for deleting and Indictment or a Breach
     * 
     * @param delChargeVal
     *            The charge being deleted
     * @param chargeType
     *            The type of the charge we are deleting
     * @throws CourtLogBusinessException
     * @throws BisRefControllerException
     */
    public void deleteChargeCourtLog(DelChargeValue delChargeVal, String chargeType) throws CourtLogBusinessException,
            BisRefControllerException {
        if (log.isDebugEnabled()) {
            log.debug("deleteChargeCourtLog() start with delChargeVal : " + delChargeVal + " and chargeType : "
                    + chargeType);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(delChargeVal.isInCourt());
        logEntry.setCaseId(delChargeVal.getCaseID());

        if (delChargeVal.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("doDeleteCharge() - CourtLogDate has not been set "
                    + "on the DelChargeValue.");
        }
        logEntry.setEntryDate(delChargeVal.getCourtLogDate().getTime());

        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_INDICTMENT);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteIndictment"),
                    new Object[] { delChargeVal.getCrestChargeSeqNo() }));
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.DELETE_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteFail2Appear"),
                    new Object[] { getFail2AppearOffenceDesc(delChargeVal.getChargeID()) }));
        } else
        // must be a breach as we can only delete indictment, Fail2Appear and
        // breach
        // charges
        {
            logEntry.setEventType(CourtLogEvents.DELETE_BREACH);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("deleteBreach"),
                    new Object[] { getBreachDescription(getCharge(delChargeVal.getChargeID()).getRefSystemCodeId()) }));
        }

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
    }

    private String getFail2AppearOffenceDesc(Integer chargeId) throws BisRefControllerException {
        String returnString = "";

        XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
        Collection offences = charge.getXhbOffences();
        if (offences != null && offences.size() == 1) {
            Iterator it = offences.iterator();
            returnString = getOffenceDesc((XhbOffence) it.next());
        }

        return returnString;
    }

    /**
     * Creates the court log entry for updating a breach
     * 
     * @param breachValue
     *            The breach being updated
     * @throws CourtLogBusinessException
     */
    public void updateBreachCourtLog(BreachValue breachValue) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("updateBreachCourtLog() start with breachValue : " + breachValue);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        // set the value which notifies if the case is active in court
        logEntry.setInCourt(breachValue.isInCourt());
        logEntry.setCaseId(breachValue.getCaseID());
        logEntry.setEventType(CourtLogEvents.AMEND_BREACH);

        if (breachValue.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("updateBreach() - CourtLogDate has not been set on the BreachValue.");
        }
        logEntry.setEntryDate(breachValue.getCourtLogDate().getTime());
        logEntry.setEntryFreeText(MessageFormat.format(messages.getString("updateBreach"), new Object[] { getCharge(
                breachValue.getChargeID()).getCrestChargeSeqNo() }));

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("updateBreachCourtLog() end");
    }

    /**
     * Creates the court log entry for signing and indictment
     * 
     * @param signIndVal
     *            Details of the indictment to be signed
     * @param totalDays
     *            Number of days between the prosecution papers served date and
     *            the date of the event
     * @throws CourtLogBusinessException
     */
    public void signIndictmentCourtLog(SignIndValue signIndVal, int totalDays) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("signIndictmentCourtLog() start with signIndVal : " + signIndVal + " totalDays : " + totalDays);
        }

        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();
        // set the value which notifies if the case is active in court
        logEntry.setInCourt(signIndVal.isInCourt());
        logEntry.setCaseId(signIndVal.getCaseID());
        logEntry.setEventType(CourtLogEvents.SIGN_INDICTMENT);

        if (signIndVal.getCourtLogDate() == null) {
            throw new CSUnrecoverableException("signIndictmentCourtLog() - CourtLogDate has not been set "
                    + "on the SignIndValue.");
        }
        logEntry.setEntryDate(signIndVal.getCourtLogDate().getTime());
        XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(signIndVal.getChargeID());
        SimpleDateFormat defaultDateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String dateString = defaultDateformat.format(signIndVal.getCourtLogDate().getTime());

        if (totalDays <= SignIndValue.SIGN_IND_DAYS) {
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("signIndictment"), new Object[] {
                    charge.getCrestChargeSeqNo(), dateString }));
        } else if (totalDays <= SignIndValue.SIGN_IND_LATE_DAYS) {
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("signIndOutOfTime"), new Object[] {
                    signIndVal.getJudgeName(), charge.getCrestChargeSeqNo(), dateString }));
        } else {
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("signIndLate"), new Object[] {
                    signIndVal.getJudgeName(), charge.getCrestChargeSeqNo(), dateString }));
        }

        // create the entry
        
        CourtLogWorkFlow.newEntry(logEntry, false);
    }

    // ---------------------------- Private Methods
    // -----------------------------
    private void setOffenceEventTypeAndFreetext(Integer chargeSeqNo, String chargeType, String offenceNoDesc,
            CourtLogCRUDValue logEntry) {
        if (log.isDebugEnabled()) {
            log.debug("setOffenceEventTypeAndFreetext(" + chargeSeqNo + ", " + chargeType + ", " + offenceNoDesc
                    + "[CourtLogCRUDValue])");
        }

        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.ADD_COUNT);
            if (chargeSeqNo != null) {
                logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addCount"), new Object[] {
                        offenceNoDesc, chargeSeqNo }));
            } else {
                logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addCountNewIndictment"),
                        new Object[] { offenceNoDesc, chargeSeqNo }));
            }

        } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.ADD_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addCommittalOffence"),
                    new Object[] { offenceNoDesc }));
        } else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.ADD_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addSummaryOffence"),
                    new Object[] { offenceNoDesc }));
        } else if (chargeType.equals(ChargeTypes.BREACH.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.ADD_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addBreachOffence"),
                    new Object[] { offenceNoDesc }));
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            logEntry.setEventType(CourtLogEvents.ADD_OFFENCE);
            logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addFail2AppearOffence"),
                    new Object[] { offenceNoDesc }));
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
        	logEntry.setEventType(CourtLogEvents.ADD_OFFENCE);
        	logEntry.setEntryFreeText(MessageFormat.format(messages.getString("addAppealOffence"), 
        			new Object[] { offenceNoDesc }));
        }
    }

    private String getDefendantName(Integer defendantId) {
        XhbDefendant defendant = XhbDefendantBeanHelper2.findByPrimaryKey(defendantId);
        return getDefendantName(defendant);
    }

    private String getDefendantName(XhbDefendant def) {
        String firstName = def.getFirstName();
        String middleName = def.getMiddleName();
        String surname = def.getSurname();

        StringBuffer defName = new StringBuffer();

        if (firstName != null && !firstName.equals(""))
            defName.append(firstName + " ");
        if (middleName != null && !middleName.equals(""))
            defName.append(middleName + " ");
        if (surname != null && !surname.equals(""))
            defName.append(surname);

        log.debug("def name : " + defName);

        return defName.toString();
    }

    private String getBreachDescription(Integer refSystemCode) throws BisRefControllerException {
        // get the controller
        BisRefControllerLocal bisRefController = (BisRefControllerLocal) CSServices.getEJBServices()
                .createLocalSession(BisRefControllerLocalHome.class);
        // set the criteria to search by
        RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
        criteria.setPrimaryKey(refSystemCode);

        Collection codes = bisRefController.findSystemCodes(criteria);

        // should be only one code as we've searched by pk
        if (codes.size() != 1) {
            throw new CSUnrecoverableException("Found " + codes.size() + " system codes for id " + refSystemCode
                    + " expected 1");
        }

        return ((RefSystemCodeBasicValue) (codes.iterator().next())).getDecode();
    }

    private String getBreachOffences(Collection offenceValues) {
        StringBuffer breachOffences = new StringBuffer();
        Iterator offencesIt = offenceValues.iterator();
        while (offencesIt.hasNext()) {
            OffenceValue offenceVal = (OffenceValue) offencesIt.next();
            breachOffences.append(offenceVal.getOffenceDescription() + ", ");
        }

        breachOffences.delete(breachOffences.length() - 2, breachOffences.length());
        return breachOffences.toString();
    }

    private String getCaseNumber(Integer caseId) {
        XhbCase theCase = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        return theCase.getCaseType() + theCase.getCaseNumber();
    }

    private String getOffenceDesc(XhbOffence offence) throws BisRefControllerException {
        String rtn;
        Integer refOffenceId = offence.getRefOffenceId();
        // get the controller
        BisRefControllerLocal bisRefController = (BisRefControllerLocal) CSServices.getEJBServices()
                .createLocalSession(BisRefControllerLocalHome.class);
        // set the criteria to search by
        RefOffenceCriteria criteria = new RefOffenceCriteria();
        criteria.setPrimaryKey(refOffenceId);

        Collection refOffences = bisRefController.findOffences(criteria);

        // should be only one code as we've searched by pk
        if (refOffences.size() != 1) {
            throw new CSUnrecoverableException("Found " + refOffences.size() + " refOffences for id " + refOffenceId
                    + " expected 1");
        }
        RefOffenceBasicValue refOffence = ((RefOffenceBasicValue) (refOffences.iterator().next()));

        if (refOffence.getOffenceCode().equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
            rtn = offence.getCrestOffenceFreetext();
        } else {
            rtn = refOffence.getOffenceDesc();
        }
        return rtn;
    }

    private String findCrestOffenceSequenceNumber(Integer offenceId) throws CSUnrecoverableException {
        // get newly created offence entity
        XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(offenceId);
        String offenceNoDesc = "" + offenceBean.getCrestOffenceSeqNo();
        return offenceNoDesc;
    }

    private Integer findCrestChargeSequenceNumber(Integer chargeId) {
        // get newly created offence entity
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
        return chargeBean.getCrestChargeSeqNo();
    }

    private void setPropertyMap(CourtLogCRUDValue entry, LinkCountDefValue linkVal) {
        if (log.isDebugEnabled()) {
            log.debug("setPropertyMap() - entry = " + entry);
            log.debug("setPropertyMap() - linkVal = " + linkVal);
        }

        Map<String, String> propertyMap = new HashMap<String, String>();
        String isAddDefendantToCountFlag = linkVal.isAddDefendantToCount() ? "Y" : "N";

        propertyMap.put("E" + entry.getEventType() + "_isAddDefendantToCount", isAddDefendantToCountFlag);

        entry.setPropertyMap(propertyMap);
    }

    /**
     * 
     * @param chargeType
     * @param logEntry
     * @param chargeId
     * @param offenceId
     * @throws CourtLogBusinessException
     */
    public void renumberCounts(ChargeValue chargeValue) throws CourtLogBusinessException {
        if (log.isDebugEnabled()) {
            log.debug("renumberCounts() start with indicment : " + chargeValue.getCrestChargeSeqNo());
        }
        // create the log entry for this event
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();
        // set the value which notifies if the case is active in court
        logEntry.setInCourt(chargeValue.isInCourt());
        logEntry.setCaseId(chargeValue.getCaseID());
        logEntry.setEventType(CourtLogEvents.RENUMBER_COUNTS);
        logEntry.setEntryDate(Calendar.getInstance().getTime());
        logEntry.setEntryFreeText(MessageFormat.format(messages.getString("renumberCounts"), new Object[] { chargeValue
                .getCrestChargeSeqNo() }));
        
        CourtLogWorkFlow.newEntry(logEntry, false);
        log.debug("renumberCounts() end");
    }

    /**
     * 
     * @param defendantValue
     * @param logEntry
     * @throws CourtLogBusinessException
     */
    public void removeDefendantsLog(XhbDefendantOnOffenceBasicValue defendantValue, CourtLogCRUDValue logEntry)
            throws CourtLogBusinessException {

        if (log.isDebugEnabled()) {
            log.debug("removeDefendantsLog starrt");
        }
        // find the defendant
        XhbDefendantOnOffence doo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defendantValue
                .getDefendantOnOffenceId());
        XhbDefendant defendant = doo.getXhbDefendantOnCase().getXhbDefendant();
        String defendantName = getDefendantName(defendant);
        Integer offenceId = defendantValue.getOffenceId();
        XhbOffence offence = XhbOffenceBeanHelper2.findByPrimaryKey(offenceId);

        logEntry.setEntryFreeText(MessageFormat.format(messages.getString("removeDefendant"), new Object[] {
                offence.getCrestOffenceSeqNo(), offence.getXhbCharge().getCrestChargeSeqNo(), defendantName }));
        logEntry.setEventType(CourtLogEvents.REMOVE_DEFENDANT_ON_COUNT);
        logEntry.setEntryDate(Calendar.getInstance().getTime());
        
        CourtLogWorkFlow.newEntry(logEntry, false);

        log.debug("removeDefendantsLog() end");
    }

}
