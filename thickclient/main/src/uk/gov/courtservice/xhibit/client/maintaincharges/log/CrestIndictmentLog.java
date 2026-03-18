package uk.gov.courtservice.xhibit.client.maintaincharges.log;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: XHIBIT 2 - Crest Indictment Log
 * </p>
 * <p>
 * Description: Responsible for holding and grouping the logs related to
 * indictment operations.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
/*
 * Changed all references to 'this.instance.groups' and
 * 'this.instance.caseIdChanges' to just 'groups/caseIdChanges' to avoid
 * confusion, since in a singleton there will only ever be one HashMap created
 * (reality checked by Neil!)
 * 
 * @author Jon Powell (Electronic Data Systems) @date 14-Oct-03
 */
public class CrestIndictmentLog {
    // Operation List.
    public static final int ADD_COUNT = 0;

    public static final int ADD_COUNTS_TO_DEFENDANT = 1;

    public static final int ADD_DEFENDANTS_TO_COUNT = 2;

    public static final int ADD_INDICTMENT = 3;

    public static final int CHANGE_COUNT = 4;

    public static final int CHANGE_DEFENDANT = 5;

    public static final int COUNT_PARTICULARS_AMENDED = 6;
    
    public static final int RENUMBER_COUNT = 7;

    public static final int LIE_ON_FILE_COUNT = 8;

    public static final int LIE_ON_FILE_ON_DEFENDANT_ON_COUNT = 9;

    public static final int LIE_ON_FILE_ON_DEFENDANT_ON_INDICTMENT = 10;

    public static final int QUASH_COUNT = 11;

    public static final int QUASH_DEFENDANT_ON_COUNT = 12;

    public static final int QUASH_DEFENDANT_ON_INDICTMENT = 13;

    public static final int QUASH_INDICTMENT = 14;

    public static final int REMOVE_COUNT = 15;
    
    public static final int REMOVE_DEFENDANTS_FROM_COUNT = 16;

    public static final int REMOVE_INDICTMENT = 17;

    public static final int JOINDER_INDICTMENT = 18;

    public static final int STAY_COUNT = 19;

    public static final int STAY_DEFENDANT_ON_COUNT = 20;

    public static final int STAY_DEFENDANT_ON_INDICTMENT = 21;

    public static final int STAY_INDICTMENT = 22;
    
    public static final int ADD_COUNT_TO_JOINDER = 23;

    /** The maximum length for an indictment info field in CREST. */
    private static final int MAX_INDICTMENT_INFO = 78;

    /**
     * Stores the cases and their associated logs for indictment operations.
     * KEY: CaseBasicValueKey - containts the CaseBasicValue VALUE: Log of
     * indictment operations (type String)
     * 
     * NOTE(19-03-03) As of this date, the only cases that will be stored will
     * be the opened case and the joined cases from the joinder indictment. Was
     * implemented in this way so that no code change will have to be made if
     * future requirements enforce more logs of other cases to be written, who
     * knows...
     */
    // private HashMap caseLogs = null;
    /*
     * Contains the list of operations in the order in which their logs will be
     * recorded.
     */
    private final static int[] operationOrderOfPrecedence;

    /**
     * The total length of an indictment log. 78 is the maximum length for an
     * indictmentinfo field in CREST. There are 6 info fields in a
     * CaseBasicValue, so the limit is the product of the two.
     */
    public final int logLengthLimit = MAX_INDICTMENT_INFO * 6;

    /*
     * The singleton reference to the instance of this class.
     */
    private static CrestIndictmentLog instance = null;

    /*
     * KEY: operation type+"."+caseID. VALUE: an arraylist of sentence parts,
     * that together will compose one sentence. e.g Inictment 5 added to Case
     * <Case_title>
     */
    private HashMap groups = null;

    /*
     * List of case ids for cases that have changed.
     */
    private ArrayList caseIdChanges = null;

    /*
     * Initialise the log references. Also sets the precedence order of the
     * operations - the order in which they will be written to the log.
     */
    static {
        // this.crestIndictmentLog.caseLogs = new HashMap ();
        operationOrderOfPrecedence = new int[] { ADD_COUNT, ADD_INDICTMENT, ADD_COUNTS_TO_DEFENDANT,
                ADD_DEFENDANTS_TO_COUNT, CHANGE_COUNT, CHANGE_DEFENDANT, COUNT_PARTICULARS_AMENDED, RENUMBER_COUNT,
                LIE_ON_FILE_COUNT,LIE_ON_FILE_ON_DEFENDANT_ON_COUNT, LIE_ON_FILE_ON_DEFENDANT_ON_INDICTMENT, 
                QUASH_COUNT,QUASH_DEFENDANT_ON_COUNT, QUASH_DEFENDANT_ON_INDICTMENT, QUASH_INDICTMENT, 
                REMOVE_COUNT,REMOVE_DEFENDANTS_FROM_COUNT, REMOVE_INDICTMENT, JOINDER_INDICTMENT, STAY_COUNT, 
                STAY_DEFENDANT_ON_COUNT,STAY_DEFENDANT_ON_INDICTMENT, STAY_INDICTMENT };
    }

    /*
     * Private to prevent instantiation (see 'getInstance')
     */
    private CrestIndictmentLog() {
        groups = new HashMap();
        caseIdChanges = new ArrayList();
    }

    /**
     * @return the singleton instance.
     */
    public static synchronized CrestIndictmentLog getInstance() {
        if (instance == null) {
            instance = new CrestIndictmentLog();
        }
        return instance;
    }

    /**
     * Will add a sentence or sentence-part of a specific operation to the log
     * of the given caseBasicValue.
     * 
     * @param caseId
     *            the id of the case to which the log will appended
     * @param operation
     *            the type of operation the log is referring to, e.g
     *            <code>REMOVE_INDICTMENT</code>
     * @param sentencePart
     *            the sentence/sentencePart that will be written to the log.
     */
    private synchronized void addCaseIndictmentSentencePart(Integer caseId, int operation, String sentencePart) {
        String existingSentence = (String) instance.groups.get(operation + "." + caseId);
        if (existingSentence == null) {
            existingSentence = "";
        }
        if (sentencePart == null) {
            sentencePart = "";
        }
        groups.put(operation + "." + caseId, (existingSentence + sentencePart));
        if (!this.isIndictmentActionOnCase(caseId)) {
            caseIdChanges.add(caseId);
        }
    }

    /**
     * Will retrieve the logged collection sentence/sentence parts for a
     * particular operation. Will return null if no particular sentence has been
     * logged.
     * 
     * @param caseBasicValue
     *            the value from which the logged operation will be retrieved.
     * @param operation
     *            the type of operation the log related to.
     * @return null if no sentence has been logged, otherwise return the logged
     *         sentence.
     */
    public synchronized String getLogForOperation(CaseBasicValue caseBasicValue, int operation) {
        return (String) groups.get(operation + "." + caseBasicValue.getId().intValue());
    }

    /**
     * Will store the given log in the CaseBasicValue.
     * 
     * @param caseBasicValue
     *            the value object to which its log will be stored.
     * @param log
     *            the indictment log to be stored.
     */
    public synchronized void setLog(CaseBasicValue caseBasicValue, String log) throws CSRecoverableException {
        String infoField = null;
        int infoCount = 0;
        if (log.length() % MAX_INDICTMENT_INFO == 0){
            infoCount = log.length() / MAX_INDICTMENT_INFO; // The number of indicmtnet log rows required 
        // by the CaseBasicValue
        } else {
            infoCount = (log.length() / MAX_INDICTMENT_INFO) + 1;
        }
        //Create collection of Indictment Logs to populate with the Indictment Log from the screen
        Collection<IndictmentLogValue> iLogs = new Vector<IndictmentLogValue>();
        
        int offset = 0;
        int endIndex = 0;
        for (int i = 1; i <= infoCount; i++) {
            int toReadLength = log.length() - offset;
            if (toReadLength > 0) {
                endIndex = (toReadLength >= MAX_INDICTMENT_INFO) ? (offset + MAX_INDICTMENT_INFO) : log.length();
                infoField = log.substring(offset, endIndex);
                offset = endIndex;
            } else {
                infoField = "";
            }
            IndictmentLogValue iLog = new IndictmentLogValue();
            iLog.setCaseID(caseBasicValue.getId());
            iLog.setSequenceNo(new Integer(i));
            iLog.setIndictmentInfo(infoField);
            iLogs.add(iLog);
        }
        caseBasicValue.setIndictmentLog(iLogs);
        //code removal for CTX-403
        //caseBasicValue.setIndChangeStatus(CaseBasicValue.READY);
    }

    /**
     * Will retrieve the latest log, as it is in memory - NOT how the
     * CaseBasicValue holds it. This log is a composition of the how the log was
     * recorded before, and the latest log of events from the user's session.
     * 
     * @param caseBasicValue
     *            the value from which its associated log will be retrieved.
     * @return the latest log, as it is represented in memory.
     */
    public synchronized String getLatestLogStoredInMemory(CaseBasicValue caseBasicValue) {
        StringBuffer currentLog = new StringBuffer(this.getCaseIndictmentLog(caseBasicValue));
        int caseBasicValueId = caseBasicValue.getId().intValue();
        String sentence = null;
        for (int i = 0; i < operationOrderOfPrecedence.length; i++) {
            int operation = operationOrderOfPrecedence[i];
            sentence = (String) groups.get(operation + "." + caseBasicValueId);
            if (sentence == null) {
                continue;
            }
            currentLog.append("\n");
            currentLog.append(sentence);
        }
        return currentLog.toString();
    }

    /**
     * Will derive the current log from the given CaseBasicValue. The log being
     * the composition of any rows present in the XHB_Indictment_Log table.
     * 
     * @param caseBasicValue
     *            the value from which the log will be derived
     * @return the log
     */
    public String getCaseIndictmentLog(CaseBasicValue caseBasicValue) {
        //Retrieve the Indictment Log
        Collection<IndictmentLogValue> indictmentLog = caseBasicValue.getIndictmentLog();    
        StringBuffer log = new StringBuffer();
        if (indictmentLog != null){
            for (IndictmentLogValue ilog : indictmentLog){
                log.append(ilog.getIndictmentInfo());
            }
        }

        return log.toString();
    }

    /**
     * This will clear all logs for the given case.
     */
    public void clear(CaseBasicValue caseBasicValue) {
        // this.crestIndictmentLog.caseLogs.remove(new
        // CaseBasicValueKey(caseBasicValue));
        Iterator keys = groups.keySet().iterator();
        String key = null;
        while (keys.hasNext()) {
            key = (String) keys.next();
            int firstDotIndex = key.indexOf(".");
            int caseId = Integer.parseInt(key.substring((firstDotIndex + 1), key.length()));
            if (caseId == caseBasicValue.getId().intValue()) {
                groups.remove(key);
                break;
            }
        }
    }

    public void clearChanges(CaseBasicValue caseBasicValue) {
        // Clear case id if in change list.
        caseIdChanges.remove(caseBasicValue.getId());

        // Clear the data from the groups
        for (int i = 0; i < operationOrderOfPrecedence.length; i++) {
            int operation = operationOrderOfPrecedence[i];
            groups.remove(operation + "." + caseBasicValue.getId());
        }
    }

    // ------------- LOG METHODS
    // -----------------------------------------------

    public void addIndictmentLog(CaseBasicValue caseValue, ChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.ADDEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.ADDEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.INDICTMENT;
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_INDICTMENT, sentence);
    }

    public void addCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_COUNT + "." + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.ADDEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.ADDEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " " + getOffenceCode(count);
        // count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_COUNT, sentence);
    }
    
    public void addJoinderCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_COUNT + "." + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.ADDEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.ADDEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " " + getOffenceCode(count);
        // count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_COUNT_TO_JOINDER, sentence);
    }

    public void editCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_COUNT + "." + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.EDITEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.EDITEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " " + getOffenceCode(count);
        // count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_COUNT, sentence);
    }

    public void addCountsToDefendantLog(CaseBasicValue caseValue, Collection counts, DefendantValue defendant) {
        OffenceValue count = null;
        Object[] countList = counts.toArray();
        for (int i = 0; i < countList.length; i++) {
            count = (OffenceValue) countList[i];
            String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_COUNTS_TO_DEFENDANT + "."
                    + caseValue.getId().intValue());
            initialSentence = (initialSentence == null) ? IndictmentLogResource.ADDEDBIG : " "
                    + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.ADDEDSMALL;
            String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " "
                    + count.getCrestOffenceSeqNo() + " " + IndictmentLogResource.TO + " "
                    + IndictmentLogResource.DEFENDANTSMALL + " " + defendant.getFirstName() + " " + defendant.getSurName();
            this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_COUNTS_TO_DEFENDANT, sentence);
        }
    }

    public void addDefendantsToCountLog(CaseBasicValue caseValue, Collection defendants, OffenceValue count) {
        DefendantValue defendantValue = null;
        Object[] defendantList = defendants.toArray();
        for (int i = 0; i < defendantList.length; i++) {
            defendantValue = (DefendantValue) defendantList[i];
            String initialSentence = (String) groups.get(CrestIndictmentLog.ADD_DEFENDANTS_TO_COUNT + "."
                    + caseValue.getId().intValue());
            initialSentence = (initialSentence == null) ? IndictmentLogResource.ADDEDBIG : " "
                    + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.ADDEDSMALL;
            String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " "
                    + defendantValue.getFirstName() + " " + defendantValue.getSurName() + " "
                    + IndictmentLogResource.TO + " " + IndictmentLogResource.COUNTSMALL + " "
                    + count.getCrestOffenceSeqNo();
            this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.ADD_DEFENDANTS_TO_COUNT, sentence);
        }
    }
    
    public void removeDefendantsFromCountLog(CaseBasicValue caseValue, String defendant, OffenceValue count, ChargeValue charge) {
        String defendantName = defendant;
        String initialSentence = (String) groups.get(CrestIndictmentLog.REMOVE_DEFENDANTS_FROM_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.REMOVEDBIG : " "
            + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.REMOVEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " "
            + defendantName + " "
            + IndictmentLogResource.FROM + " " + IndictmentLogResource.COUNTSMALL + " "
            + count.getCrestOffenceSeqNo() + " " + IndictmentLogResource.ON + " " 
            + IndictmentLogResource.INDICTMENT + " " + charge.getCrestChargeSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.REMOVE_DEFENDANTS_FROM_COUNT, sentence);
     }

    public void changeCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.CHANGE_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.CHANGEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.CHANGEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " " + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.CHANGE_COUNT, sentence);
    }
    
    public void renumberCountLog(CaseBasicValue caseValue, HashMap<Integer, Integer> currentState, Map<Integer, Integer> startState, ChargeValue charge) {
        boolean firstLine = true;
        String sentence = "";
        
        Iterator iter = startState.keySet().iterator();
        while (iter.hasNext()){
            Integer rowNum = (Integer) iter.next();
            Integer originalValue = startState.get(rowNum);
            Integer currentValue = currentState.get(rowNum);
            //Current State only contains changed records so if record is absent
            //set to same value as start state.
            if (currentValue == null){
                currentValue = originalValue;
            }
            
            if (originalValue != currentValue){
                String initialSentence = "";
                if(firstLine){
                    initialSentence = IndictmentLogResource.COUNTBIG; 
                    firstLine = false;
                }else{
                    initialSentence = IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.COUNTSMALL;
                }
                sentence = sentence + initialSentence + " " + originalValue + " "  
                + IndictmentLogResource.RENUMBEREDSMALL + " "
                + IndictmentLogResource.TO + " " + IndictmentLogResource.COUNTSMALL + " "
                + currentValue + " " + IndictmentLogResource.ON + " " 
                + IndictmentLogResource.INDICTMENT + " " + charge.getCrestChargeSeqNo();
            }
        }
        sentence = sentence + ". ";
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.RENUMBER_COUNT, sentence);
        firstLine = true;
     }

    public void changeDefendantLog(CaseBasicValue caseValue, DefendantValue defendant) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.CHANGE_DEFENDANT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.CHANGEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.CHANGEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " " + defendant.getFirstName()
                + " " + defendant.getSurName();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.CHANGE_DEFENDANT, sentence);
    }

    public void countParticularsAmendedLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.COUNT_PARTICULARS_AMENDED + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.COUNTBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.COUNTSMALL;
        String sentence = initialSentence + " " + count.getCrestOffenceSeqNo() + " "
                + IndictmentLogResource.PARTICULARSAMMENED;
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.COUNT_PARTICULARS_AMENDED, sentence);
    }

    public void lieOnFileCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.LIE_ON_FILE_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.COUNTBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.COUNTSMALL;
        String sentence = initialSentence + " " + count.getCrestOffenceSeqNo() + " " + IndictmentLogResource.LIEONFILE;
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.LIE_ON_FILE_COUNT, sentence);
    }

    public void lieOnFileOnDefendantOnCountLog(CaseBasicValue caseValue, DefendantValue defendant, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.LIE_ON_FILE_ON_DEFENDANT_ON_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.LIEONFILEBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.LIEONFILE;
        String sentence = initialSentence + " " + IndictmentLogResource.ON + " " + IndictmentLogResource.DEFENDANTSMALL
                + " " + defendant.getFirstName() + " " + defendant.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.COUNTSMALL + " " + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.LIE_ON_FILE_ON_DEFENDANT_ON_COUNT, sentence);
    }

    public void lieOnFileOnDefendantOnIndictmentLog(CaseBasicValue caseValue, DefendantValue defendant,
            ChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.LIE_ON_FILE_ON_DEFENDANT_ON_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.LIEONFILEBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.LIEONFILE;
        String sentence = initialSentence + " " + IndictmentLogResource.ON + " " + IndictmentLogResource.DEFENDANTSMALL
                + " " + defendant.getFirstName() + " " + defendant.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.INDICTMENT + " " + indictment.getCrestChargeSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.LIE_ON_FILE_ON_DEFENDANT_ON_INDICTMENT,
                sentence);
    }

    public void removeCountLog(CaseBasicValue caseValue, Integer crestOffenceSeqNum, Integer crestChargeSeqNum) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.REMOVE_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.COUNTBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.COUNTSMALL;
        String sentence = initialSentence + " " + crestOffenceSeqNum + " " + IndictmentLogResource.DELETEDSMALL + " "
                + IndictmentLogResource.FROM + " " + IndictmentLogResource.INDICTMENT + " " + crestChargeSeqNum;
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.REMOVE_COUNT, sentence);
    }

    public void removeIndictmentLog(CaseBasicValue caseValue, DelChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.REMOVE_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.DELETEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.DELETEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.INDICTMENT + " "
                + indictment.getCrestChargeSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.REMOVE_INDICTMENT, sentence);
    }

    public void quashIndictmentLog(CaseBasicValue caseValue, ChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.QUASH_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.QUASHEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.QUASHEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.INDICTMENT + " "
                + indictment.getCrestChargeSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.QUASH_INDICTMENT, sentence);
    }

    public void quashCountLog(CaseBasicValue caseValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.QUASH_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.QUASHEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.QUASHEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.COUNTSMALL + " " + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.QUASH_COUNT, sentence);
    }

    public void quashDefendantOnCountLog(CaseBasicValue caseValue, DefendantValue defendantValue, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.QUASH_DEFENDANT_ON_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.QUASHEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.QUASHEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " " + defendantValue.getFirstName()
                + " " + defendantValue.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.COUNTSMALL + " " + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.QUASH_DEFENDANT_ON_COUNT, sentence);
    }

    public void quashDefendantOnIndictmentLog(CaseBasicValue caseValue, DefendantValue defendantValue,
            ChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.QUASH_DEFENDANT_ON_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.QUASHEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.QUASHEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " " + defendantValue.getFirstName()
                + " " + defendantValue.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.INDICTMENT + " " + indictment.getCrestChargeSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.QUASH_DEFENDANT_ON_INDICTMENT, sentence);
    }

    /**
     * Bug 54574 - entry in Crest Indictment Log for stay indictment action
     * 
     * @param caseValue
     *            Used to identify the case
     * @param indictment
     *            Contains information required for the log
     */
    public void stayIndictmentLog(CaseBasicValue caseValue, ChargeValue indictment) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.STAY_INDICTMENT + "."
                + caseValue.getId().intValue());
        if (initialSentence == null) {
            initialSentence = IndictmentLogResource.STAYEDBIG;
        } else {
            initialSentence = " " + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.STAYEDSMALL;
        }
        String sentence = initialSentence + " " + IndictmentLogResource.INDICTMENT + " "
                + indictment.getCrestChargeSeqNo();
        addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.STAY_INDICTMENT, sentence);
    }

    /**
     * Bug 54574 - entry in Crest Indctment Log for stay action
     * 
     * @param caseValue
     *            Used to identify the case
     * @param count
     *            Contains information required for the log
     */
    public void stayCountLog(CaseBasicValue caseValue, ChargeValue indictment, OffenceValue count) {
        String initialSentence = (String) groups
                .get(CrestIndictmentLog.STAY_COUNT + "." + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.STAYEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.STAYEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.INDABBREVBIG + " "
                + indictment.getCrestChargeSeqNo() + IndictmentLogResource.FORWARDSLASH
                + IndictmentLogResource.COUNTBIG + " " + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.STAY_COUNT, sentence);
    }

    /**
     * Bug 54574 - entry in Crest Indctment Log for stay action
     * 
     * @param caseValue
     *            Used to identify the case
     * @param defendantValue
     *            Contains information required for the log
     * @param count
     *            Contains information required for the log
     */
    public void stayDefendantOnCountLog(CaseBasicValue caseValue, DefendantValue defendantValue,
            ChargeValue indictment, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.STAY_DEFENDANT_ON_COUNT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.STAYEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.STAYEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " " + defendantValue.getFirstName()
                + " " + defendantValue.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.INDABBREVBIG + " " + indictment.getCrestChargeSeqNo()
                + IndictmentLogResource.FORWARDSLASH + IndictmentLogResource.COUNTBIG + " "
                + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.STAY_DEFENDANT_ON_COUNT, sentence);
    }

    /**
     * Bug 54574 - entry in Crest Indctment Log for stay action
     * 
     * @param caseValue
     *            Used to identify the case
     * @param defendantValue
     *            Contains information required for the log
     * @param indictment
     *            Contains information required for the log
     * @param count
     *            Contains Count information required for the log
     */
    public void stayDefendantOnIndictmentLog(CaseBasicValue caseValue, DefendantValue defendantValue,
            ChargeValue indictment, OffenceValue count) {
        String initialSentence = (String) groups.get(CrestIndictmentLog.STAY_DEFENDANT_ON_INDICTMENT + "."
                + caseValue.getId().intValue());
        initialSentence = (initialSentence == null) ? IndictmentLogResource.STAYEDBIG : " "
                + IndictmentLogResource.ANDSMALL + " " + IndictmentLogResource.STAYEDSMALL;
        String sentence = initialSentence + " " + IndictmentLogResource.DEFENDANTSMALL + " " + defendantValue.getFirstName()
                + " " + defendantValue.getSurName() + " " + IndictmentLogResource.ON + " "
                + IndictmentLogResource.INDABBREVBIG + " " + indictment.getCrestChargeSeqNo()
                + IndictmentLogResource.FORWARDSLASH + IndictmentLogResource.COUNTBIG + " "
                + count.getCrestOffenceSeqNo();
        this.addCaseIndictmentSentencePart(caseValue.getId(), CrestIndictmentLog.STAY_DEFENDANT_ON_INDICTMENT, sentence);
    }

    public void joinderIndictmentLog(Collection<XhbCaseBasicValue> caseBasicValues, String joinderLog) {
        for (XhbCaseBasicValue caseBasicValue : caseBasicValues) {
            this.addCaseIndictmentSentencePart(caseBasicValue.getCaseId(), CrestIndictmentLog.JOINDER_INDICTMENT, joinderLog);
        }
    }

    public boolean isIndictmentActionOnCase(Integer caseId) {
        for (int i = 0; i < caseIdChanges.size(); i++) {
            Integer foundCaseId = (Integer) caseIdChanges.get(i);
            if (foundCaseId.equals(caseId)) {
                return true;
            }
        }
        return false;
    }

    public void debug(CaseBasicValue cbv) {
        int i = 0;
        while (i < 10) {
            this.addIndictmentLog(cbv, getChargeValue());
            this.addCountLog(cbv, getOffenceValue());
            this.quashCountLog(cbv, getOffenceValue());
            // this.quashDefendantOnCountLog(cbv,g);
            this.changeCountLog(cbv, getOffenceValue());
            i++;
        }
    }

    private String getOffenceCode(OffenceValue offence) {
        // If there is no HOClass or HOSubclass then use the Offence Code,
        // otherwsie it is an uncoded offence and use the Offence Description
        if (null == offence.getCrestHOClass() && null == offence.getCrestHOSubclass()) {
            return offence.getOffenceCode();
        } else {
            return offence.getOffenceDescription();
        }
    }

    // --------DEBUG METHODS
    // ----------------------------------------------------------------------
    public OffenceValue getOffenceValue() {
        return new OffenceValue(new Integer(5), new Integer(5), new Integer(5), null, "TEST", new Integer(5),
                new Integer(5), new Integer(5), "A TEST");
    }

    public ChargeValue getChargeValue() {
        return new ChargeValue(new Integer(3), new Integer(3), null, new Integer(3), new Integer(3), null, null, null,
                new Integer(3), new Integer(3), null, null, "CHARGE");
    }
}