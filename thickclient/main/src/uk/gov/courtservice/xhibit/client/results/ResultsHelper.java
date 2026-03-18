package uk.gov.courtservice.xhibit.client.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: Helper class used to get the data for the results screens.
 * </p>
 * <p>
 * Description: This helper class is used by the results screens (Pleas,
 * Verdicts, Sentence, Appeals and Case Progress) to retrieve the data to
 * populate the tables on these screens.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Rakesh Lakhani
 * @version $Revision: 1.93 $
 */
public class ResultsHelper {
    public static final String DISPOSAL = "disposal";

    public static final String CHARGETYPE_UNRELATED = "Z";

    public static final String CHARGETYPE_UNRELATED_MAGISTRATE = "ZM";

    public static final String CHARGETYPE_UNRELATED_PROGRESS = "ZP";

    private static final Logger log = CSServices.getLogger(ResultsHelper.class);

    private static final String CHARGETYPE_DISPOSAL = "D";

    private final HashMap<String, ArrayList<ResultsRowValue>> resultsMap = 
        new HashMap<String, ArrayList<ResultsRowValue>>();

    private ResultsRowValue caseResultsRowValue;

    private final ApplicationCaseModel acm;

    private final ResultsCompositeValue rcv;

    private List<String> chargeTypeList;

    private static final String emptyStr = "";

    private Integer currPsdDisposalId = null;

    private Integer currMagDisId = null;

    /**
     * Default constructor that will call the business delegate and retrieve the
     * requested results.
     *
     * @param caseId
     *            Id of the case.
     * @param resultType
     *            Pass an empty string if all results are required
     */
    public ResultsHelper(ApplicationCaseModel acm) throws ResultsControllerException {
        // Mid tier calls
        this.acm = acm;
        
        this.rcv = getResults(acm.getCaseId(), acm.getScheduledHearingId());
        buildRows(rcv);
    }

    /**
     * @return BisRefControllerBusinessDelegate
     */
    public static BisRefControllerBeanBusinessDelegate getBisRefDelegate() {
        return XhibitDelegateHelper.getBizRefDelegate();
    }

    /**
     * Gets the case controller delegate
     *
     * @return the case controller delegate
     */
    public static CaseControllerBeanBusinessDelegate getCaseControllerDelegate() {
        return XhibitDelegateHelper.getCaseDelegate();
    }

    private void buildRows(ResultsCompositeValue rcv) throws ResultsControllerException {
        // get Case Level information for Miscellaneous/Criminal Appeals
        processCase(rcv);

        ChargeCompositeValue ccv = rcv.getChargeCompositeValue();
        if (ccv.getCharges() != null) {
            Iterator chargeIterator = ccv.getCharges().iterator();
            while (chargeIterator.hasNext()) {
                ChargeValue chargeValue = (ChargeValue) chargeIterator.next();
                if (chargeValue != null) {
                    if (chargeValue.getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
                        processBreach(chargeValue);
                    } else if (chargeValue.getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
                        processFail2Appear(chargeValue);
                    } else if (chargeValue.getOffenceValues() != null) {
                        // process rows specific to pleas/results/disposals
                        // depending on charge type
                        processCharges(chargeValue);
                    }
                }
            }
        }

        // Process unrelated disposals
        if (ccv.getAllDefendants() != null) {
            Iterator iter = ccv.getAllDefendants().iterator();
            ArrayList<ResultsRowValue> al = getChargeArrayFromMap(CHARGETYPE_UNRELATED);
            while (iter.hasNext()) {
                DefendantValue item = (DefendantValue) iter.next();
                Integer defOnCaseId = item.getDefOnCaseBasicValue().getId();

                if (defOnCaseId != null) {
                    ResultsRowValue rrv = createResultsRowValue();
                    rrv.setChargeType(CHARGETYPE_UNRELATED);
                    rrv.setDefendantValue(item);
                    rrv.setDefendantOnCaseId(defOnCaseId);
                    addResultsRowPerUnrelatedDisposal(al, rrv, defOnCaseId);
                }
            }
        }
        sortLists();
    }

    /**
     * Process Results which are on the case
     *
     * @param rcv
     */
    private void processCase(ResultsCompositeValue rcv) {
        this.caseResultsRowValue = createResultsRowValue();

        // Appeal Cases have Verdict Values at a case level.
        this.caseResultsRowValue.setVerdictValue(rcv.getCaseVerdict(acm.getCaseId()));

        // There will only ever be 1 defendant for a Result on Case
        Iterator iter = rcv.getChargeCompositeValue().getAllDefendants().iterator();
        if (iter.hasNext()) {
            DefendantValue item = (DefendantValue) iter.next();
            Integer defOnCaseId = item.getDefOnCaseBasicValue().getId();
            if (defOnCaseId != null) {
                this.caseResultsRowValue.setDefendantValue(item);
                this.caseResultsRowValue.setDefendantOnCaseId(defOnCaseId);
            }
        }
    }

    /**
     * For the given charge value (except those of type Breach), adds a
     * ResultsRowValue for each offence/defendant/disposal combination.
     *
     * @param chargeValue
     *            The ChargeValue.
     */
    private void processCharges(ChargeValue chargeValue) throws ResultsControllerException {
        // get the relevant array from the hash map.
        ArrayList<ResultsRowValue> al = getChargeArrayFromMap(chargeValue.getChargeType());
        ArrayList<ResultsRowValue> al2 = null;
        if (!chargeValue.getChargeType().equals(ChargeTypes.MISC_APPEAL.getChargeType())) {
            al2 = getChargeArrayFromMap(chargeValue.getChargeType() + CHARGETYPE_DISPOSAL);
        }

        Iterator offenceIterator = chargeValue.getOffenceValues().iterator();
        while (offenceIterator.hasNext()) {
            OffenceValue offenceValue = (OffenceValue) offenceIterator.next();

            if (offenceValue.getDefendantValues() != null && !offenceValue.getDefendantValues().isEmpty()) {
                Iterator defendantIterator = offenceValue.getDefendantValues().iterator();
                while (defendantIterator.hasNext()) {
                    DefendantValue defendant = (DefendantValue) defendantIterator.next();

                    /**
                     * @todo REVISIT Assumption made only one record for a
                     *       defendant/Offence pair
                     */
                    DefendantOnOffenceComplexValue defOnOffence = offenceValue.getDefendantOnOffence(defendant
                            .getDefendantID());

                    Integer defOnOffenceId = defOnOffence.getDefendantOnOffenceId();

                    ResultsRowValue rrv = createResultsRowValue();

                    // also set the defendant on case id...
                    if (defendant.getDefOnCaseBasicValue() != null) {
                        rrv.setDefendantOnCaseId(defendant.getDefOnCaseBasicValue().getId());
                    }
                    rrv.setDefendantOnCaseId(defendant.getDefOnCaseBasicValue().getId());
                    rrv.setChargeSequenceNumber(chargeValue.getCrestChargeSeqNo());
                    rrv.setChargeType(chargeValue.getChargeType());
                    rrv.setChargeValue(chargeValue);
                    rrv.setDefendantOnOffenceId(defOnOffenceId);
                    rrv.setDefendantOnOffenceValue(defOnOffence);
                    rrv.setDefendantValue(defendant);
                    rrv.setOffenceSequenceNumber(offenceValue.getCrestOffenceSeqNo());
                    rrv.setOffenceValue(offenceValue);

                    // populate with plea/verdict/disposals
                    rrv.setPleaValue(rcv.getPlea(defOnOffenceId));

                    rrv.setVerdictValue(rcv.getVerdict(defOnOffenceId));

                    // At this point add row to the array of chargetype
                    al.add(rrv);

                    // Create rows for disposal for each defendant/offence
                    // pair
                    if (al2 != null) {
                        addResultsRowPerDisposal(al2, rrv, defOnOffenceId);
                    }
                }
            }
        }
    }

    /**
     * Adds a ResultsRowValue for each Disposal, based on the given
     * ResultsRowValue, to the given results list depending on ref disposal
     * type.
     *
     * @param results
     * @param rrv
     * @param defendantOnOffenceId
     */
    private void addResultsRowPerDisposal(List<ResultsRowValue> results, ResultsRowValue rrv, Integer defendantOnOffenceId)
            throws ResultsControllerException {
        final int disposalCount = rcv.getDisposalCount(defendantOnOffenceId);
        int actualVariationDisposalCount = 0;

        if (disposalCount > 0) {
            for (int i = 0; i < disposalCount; i++) {
                ResultsRowValue newRrv = (ResultsRowValue) rrv.clone();
                newRrv.setDisposalValue(rcv.getDisposal(defendantOnOffenceId, i));
                newRrv.setDisposalReferenceValue(ResultsReferenceFactory.getInstance().getRecordSheetDisposal(
                        newRrv.getDisposalValue().getRefDisposalTypeId()));
                results.add(newRrv);

                if (newRrv.getDisposalValue().isVariationDisposal()) {
                    actualVariationDisposalCount++;
                    // Need to find magistrates disposal to get DisId
                    Integer magistratesDisId = getMagistratesDisIdForVariation(newRrv.getDisposalValue());
                    if (magistratesDisId != null) {
                        newRrv.setPsdDisId(magistratesDisId);
                    } else {
                        log.error("Variation Disposal does not have corresponding Magistrates Disposal");
                    }
                }
            }

            rrv.setDisposalCount(disposalCount);
            rrv.setVariationDisposalCount(actualVariationDisposalCount);
        } else {
            results.add(rrv);
        }
    }

    /**
     * Adds a ResultsRowValue for each Unrelated Disposal, based on the given
     * ResultsRowValue, to the given results list.
     *
     * @param results
     * @param rrv
     * @param defendantOnOffenceId
     */
    private void addResultsRowPerUnrelatedDisposal(List<ResultsRowValue> results, ResultsRowValue rrv, Integer defendantOnCaseId)
            throws ResultsControllerException {
        final int disposalCount = rcv.getUnrelatedDisposalCount(defendantOnCaseId);
        // This counter is used becuase and magistrates disposals and variation
        // disposals will not be included here.
        int actualDisposalCount = 0;

        ArrayList<ResultsRowValue> al = getChargeArrayFromMap(CHARGETYPE_UNRELATED_MAGISTRATE);

        if (disposalCount > 0) {
            for (int i = 0; i < disposalCount; i++) {
                DisposalValue currDisposalValue = rcv.getUnrelatedDisposal(defendantOnCaseId, i);

                ResultsRowValue newRrv = (ResultsRowValue) rrv.clone();
                newRrv.setDisposalValue(currDisposalValue);
                newRrv.setDisposalReferenceValue(ResultsReferenceFactory.getInstance().getRecordSheetDisposal(
                        newRrv.getDisposalValue().getRefDisposalTypeId()));

                if (currDisposalValue.isVariationDisposal()) {
                    // Need to find magistrates disposal to get DisId
                    Integer magistratesDisId = getMagistratesDisIdForUnrelatedVariation(currDisposalValue);
                    if (magistratesDisId != null) {
                        newRrv.setPsdDisId(magistratesDisId);
                    } else {
                        log.error("Variation Disposal does not have corresponding Magistrates Disposal");
                    }
                } else {
                    newRrv.setVariationDisposalCount(rcv.getUnrelatedVariationDisposalCount(defendantOnCaseId,
                            currDisposalValue.getDisposal2Id()));
                }

                results.add(newRrv);

                if (currDisposalValue.isMagistrateDisposal()) {
                    // Get Appeal Result for Disposal if one exists.
                    final Integer disposal2Id = currDisposalValue.getDisposal2Id();
                    final VerdictValue verdictValue = rcv.getVerdictForDisposal(disposal2Id);
                    if (verdictValue != null) {
                        newRrv.setVerdictValue(verdictValue);
                    }

                    // For the Appeal Results screen (Criminal Appeal)
                    ResultsRowValue magsRrv = (ResultsRowValue) newRrv.clone();
                    magsRrv.setChargeType(CHARGETYPE_UNRELATED_MAGISTRATE);
                    al.add(magsRrv);
                }

                // For displaying unrelated disposals for Criminal Appeals
                // on the Case Progress screen.
                if (currDisposalValue.isMagistrateDisposal()
                        || (currDisposalValue.isCriminalDisposal() && !currDisposalValue.isVariationDisposal())) {
                    ArrayList<ResultsRowValue> disposalProgress = getChargeArrayFromMap(CHARGETYPE_UNRELATED_PROGRESS);
                    ResultsRowValue cpRrv = (ResultsRowValue) newRrv.clone();
                    cpRrv.setChargeType(CHARGETYPE_UNRELATED_PROGRESS);

                    DisposalValue[] variations = getVariationDisposals(currDisposalValue);
                    if (variations.length == 0) {
                        disposalProgress.add(cpRrv);
                    } else {
                        cpRrv.setVariationDisposalValue(variations[0]);
                        disposalProgress.add(cpRrv);
                    }

                    for (int v = 1; v < variations.length; v++) {
                        ResultsRowValue variationRrv = (ResultsRowValue) cpRrv.clone();
                        variationRrv.setVariationDisposalValue(variations[v]);
                        disposalProgress.add(variationRrv);
                    }
                }

                actualDisposalCount++;
            }
            if (actualDisposalCount == 0) {
                // Disposals exist but they are not record sheet disposals so
                // add an rrv so that the user can add disposals.
                results.add(rrv);
            } else {
                rrv.setDisposalCount(actualDisposalCount);
            }
        } else {
            // No unrelated disposals exist so add an rrv so that the user
            // can
            // add disposals.
            results.add(rrv);
        }
    }

    /**
     * For the charge value of type Breach, adds a ResultsRowValue for each the
     * single defendant on the Breach purpose. All Charge information is level
     * information is set the ResultRowValue. All descriptions for the offences
     * on the Breach are set to the same row.
     *
     * @param chargeValue
     *            The ChargeValue of type Breach.
     */
    private void processBreach(ChargeValue chargeValue) throws ResultsControllerException {
        // Get the relevant array from the hash map.
        ArrayList<ResultsRowValue> al = getChargeArrayFromMap(ChargeTypes.BREACH.getChargeType());
        ArrayList<ResultsRowValue> al2 = getChargeArrayFromMap(ChargeTypes.BREACH_DISPOSAL.getChargeType());
        ResultsRowValue rrv = createResultsRowValue();
        rrv.setChargeSequenceNumber(chargeValue.getCrestChargeSeqNo());
        rrv.setChargeType(chargeValue.getChargeType());
        rrv.setChargeValue(chargeValue);
        rrv.setDefendantOnChargeId(chargeValue.getDefendantOnChargeID());

        final DefendantValue dv = getDefendantValue(chargeValue.getDefendantID());
        rrv.setDefendantValue(dv);

        // also set the defendant on case id...
        if ((dv != null) && (dv.getDefOnCaseBasicValue() != null)) {
            rrv.setDefendantOnCaseId(dv.getDefOnCaseBasicValue().getId());
        }

        rrv.setPleaValue(rcv.getChargePlea(chargeValue.getDefendantOnChargeID()));

        // Retrieve Offence Descriptions for all offences on breach
        if (chargeValue.getOffenceValues() != null && chargeValue.getOffenceValues().size() > 0) {
            // Sort offences in order of crestOffenceSeqNo
            OffenceValue[] offenceValues = new OffenceValue[chargeValue.getOffenceValues().size()];
            chargeValue.getOffenceValues().toArray(offenceValues);
            Sorter.sort(offenceValues, new String[] { "crestOffenceSeqNo" }, new Boolean(true));

            for (int i = 0; i < offenceValues.length; i++) {
                // Set Offence Information for Defendant on Charge
                rrv.addBreachOffenceText(offenceValues[i].getCrestOffenceSeqNo() + "."
                        + offenceValues[i].getOffenceDescription());

                Iterator defendantIterator = offenceValues[i].getDefendantValues().iterator();
                ResultsRowValue rrvClone = (ResultsRowValue) rrv.clone();
                while (defendantIterator.hasNext()) {
                    DefendantValue defendant = (DefendantValue) defendantIterator.next();
                    DefendantOnOffenceComplexValue defOnOffence = offenceValues[i].getDefendantOnOffence(defendant
                            .getDefendantID());
                    Integer defOnOffenceId = defOnOffence.getDefendantOnOffenceId();

                    rrvClone.setDefendantOnOffenceId(defOnOffenceId);
                    rrvClone.setDefendantOnOffenceValue(defOnOffence);
                    rrvClone.setOffenceSequenceNumber(offenceValues[i].getCrestOffenceSeqNo());
                    rrvClone.setOffenceValue(offenceValues[i]);
                    addResultsRowPerDisposal(al2, rrvClone, defOnOffenceId);
                }
            }
            al.add(rrv);
        } else {
            // No offences - no text will appear for pleas in this case.
            al.add(rrv);
            al2.add(rrv);
        }
    }

    /**
     * For the charge value of type Fail2Appear, adds a ResultsRowValue for each the
     * single defendant on the Charge. All Charge information is level
     * information is set the ResultRowValue. All descriptions for the offences
     * on the Breach are set to the same row.
     *
     * @param chargeValue
     *            The ChargeValue of type FAIL2APPEAR.
     */
    private void processFail2Appear(ChargeValue chargeValue) throws ResultsControllerException {
        // Get the relevant array from the hash map.
        ArrayList<ResultsRowValue> chargeList = getChargeArrayFromMap(ChargeTypes.FAIL2APPEAR.getChargeType());
        ArrayList<ResultsRowValue> disposalList = getChargeArrayFromMap(ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
        ResultsRowValue rrv = createResultsRowValue();
        
        rrv.setChargeSequenceNumber(chargeValue.getCrestChargeSeqNo());
        rrv.setChargeType(chargeValue.getChargeType());
        rrv.setChargeValue(chargeValue);
        rrv.setDefendantOnChargeId(chargeValue.getDefendantOnChargeID());
        final DefendantValue dv = getDefendantValue(chargeValue.getDefendantID());
        rrv.setDefendantValue(dv);

        // also set the defendant on case id...
        if ((dv != null) && (dv.getDefOnCaseBasicValue() != null)) {
            rrv.setDefendantOnCaseId(dv.getDefOnCaseBasicValue().getId());
        }

        rrv.setPleaValue(rcv.getChargePlea(chargeValue.getDefendantOnChargeID()));

        // Retrieve Offence Descriptions for Offence on Charge *NOTE only 1 offence per Charge allowed *
        if (chargeValue.getOffenceValues() != null && chargeValue.getOffenceValues().size() == 1) {
            // Sort offences in order of crestOffenceSeqNo
            OffenceValue[] offenceValues = new OffenceValue[chargeValue.getOffenceValues().size()];
            chargeValue.getOffenceValues().toArray(offenceValues);
            OffenceValue bailActOffVal = offenceValues[0];
            
            // Set Offence Information for Defendant on Charge
            rrv.addBreachOffenceText( bailActOffVal.getOffenceDescription());

            Iterator defendantIterator = bailActOffVal.getDefendantValues().iterator();
            ResultsRowValue rrvClone = (ResultsRowValue) rrv.clone();

            while (defendantIterator.hasNext()) {
                DefendantValue defendant = (DefendantValue) defendantIterator.next();
                DefendantOnOffenceComplexValue defOnOffence = bailActOffVal.getDefendantOnOffence(defendant
                        .getDefendantID());
                Integer defOnOffenceId = defOnOffence.getDefendantOnOffenceId();

                rrvClone.setDefendantOnOffenceId(defOnOffenceId);
                rrvClone.setDefendantOnOffenceValue(defOnOffence);
                rrvClone.setOffenceSequenceNumber(bailActOffVal.getCrestOffenceSeqNo());
                rrvClone.setOffenceValue(bailActOffVal);
                addResultsRowPerDisposal(disposalList, rrvClone, defOnOffenceId);
            }
            chargeList.add(rrv);
        } else {
           throw new ResultsControllerException("ResultsHelper.BailActValidationError", 
                   "1:1 relation between F2A Charge and BAO invalidated.");
        }
    }
    
    private ResultsRowValue createResultsRowValue() {
        return new ResultsRowValue(acm);
    }

    private void sortLists() {
        Iterator iter = resultsMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            ArrayList item = getResultsForCharge(key);
            if (key.equals(CHARGETYPE_UNRELATED)) {
                Sorter.sort(item, new String[] { "surName", "firstName" });
            } else {
                Sorter.sort(item, new String[] { "chargeSequenceNumber", "offenceSequenceNumber", "surName",
                        "firstName" });
            }
        }
    }

    private ArrayList<ResultsRowValue> getChargeArrayFromMap(String chargeType) {
        ArrayList<ResultsRowValue> al;
        if (resultsMap.containsKey(chargeType)) {
            al = resultsMap.get(chargeType);
        } else {
            al = new ArrayList<ResultsRowValue>();
            resultsMap.put(chargeType, al);
        }
        return al;
    }

    private DefendantValue getDefendantValue(Integer defId) {
        if (defId == null) {
            XHIBITConstant.error("Breach does not have a defendant. Defendant id = " + defId);
        } else {
            Collection col = getResultsCompositeValue().getChargeCompositeValue().getAllDefendants();
            Iterator iter = col.iterator();
            while (iter.hasNext()) {
                DefendantValue item = (DefendantValue) iter.next();
                if (item.getDefendantID().equals(defId)) {
                    return item;
				}
            }
        }
        return null;
    }

    public ResultsRowValue getCaseResultsRowValue() {
        return this.caseResultsRowValue;
    }

    /**
     * Get all the results for the given charge type
     *
     * @param chargeType
     *            Type of charge.
     * @return list of ResultsRowValues.
     */
    public ArrayList<ResultsRowValue> getResultsForCharge(String chargeType) {
        return resultsMap.get(chargeType);
    }

    /**
     * List of ChargeTypes
     *
     * @return List
     */
    public List getChargeTypeList() {
        if (chargeTypeList == null) {
            chargeTypeList = new ArrayList<String>();

            populateChargeTypes(chargeTypeList, ChargeTypes.INDICTMENT.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.SECTION_41.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.BREACH.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.BREACH_DISPOSAL.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.FAIL2APPEAR.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.FAIL2APPEAR_DISPOSAL.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.CRIMINAL_APPEAL.getChargeType());
            populateChargeTypes(chargeTypeList, ChargeTypes.MISC_APPEAL.getChargeType());
            populateChargeTypes(chargeTypeList, ResultsHelper.CHARGETYPE_UNRELATED);
            populateChargeTypes(chargeTypeList, ResultsHelper.CHARGETYPE_UNRELATED_MAGISTRATE);
            populateChargeTypes(chargeTypeList, ResultsHelper.CHARGETYPE_UNRELATED_PROGRESS);
        }
        return chargeTypeList;
    }

    /**
     * Adds the given charge type to the given list of charge types if the
     * results contain the given charge type.
     *
     * @param chargeTypeList
     *            List of charge types
     * @param chargeType
     *            type of charge
     */
    private void populateChargeTypes(List<String> chargeTypeList, String chargeType) {
        if (resultsMap.containsKey(chargeType)) {
            chargeTypeList.add(chargeType);
        }
    }

    private DisposalValue[] getVariationDisposals(DisposalValue currDisposalValue) {
        final List<DisposalValue> disposals = new ArrayList<DisposalValue>();

        log.debug("getVariationDisposals Count = "
                + rcv.getUnrelatedDisposalCount(currDisposalValue.getDefendantOnCaseId()));
        for (int i = 0; i < rcv.getUnrelatedDisposalCount(currDisposalValue.getDefendantOnCaseId()); i++) {
            DisposalValue variationDisposal = rcv.getUnrelatedDisposal(currDisposalValue.getDefendantOnCaseId(), i);
            if (variationDisposal.getPsdDisposal2Id() != null
                    && variationDisposal.getPsdDisposal2Id().equals(currDisposalValue.getDisposal2Id())) {
                disposals.add(variationDisposal);
            }
        }
        log.debug("getVariationDisposals size = " + disposals.size());
        return disposals.toArray(new DisposalValue[disposals.size()]);
    }

    private Integer getMagistratesDisIdForUnrelatedVariation(DisposalValue currDisposalValue) {
        // Only process if currDisposalValue is a variation disposal or does not
        // belong to the
        // same magistrate disposal as the last processed variation.

        if (currDisposalValue.getPsdDisposal2Id() != null
                && !currDisposalValue.getPsdDisposal2Id().equals(currPsdDisposalId)) {
            for (int i = 0; i < rcv.getUnrelatedDisposalCount(currDisposalValue.getDefendantOnCaseId()); i++) {
                DisposalValue unrelatedDisposal = rcv.getUnrelatedDisposal(currDisposalValue.getDefendantOnCaseId(), i);

                // Determine if related magistrates disposal by checking
                // psdDisposal2Id is null and disposal2Id equals the variation
                // psdDisposal2Id
                if (unrelatedDisposal.getPsdDisposal2Id() == null
                        && unrelatedDisposal.getDisposal2Id().equals(currDisposalValue.getPsdDisposal2Id())) {
                    log.debug("Found magistrates");
                    currPsdDisposalId = currDisposalValue.getPsdDisposal2Id();
                    currMagDisId = unrelatedDisposal.getDisId();
                    break;
                } else {
                    log.debug("NOT Found magistrates");
                    currMagDisId = null;
                }
            }
        }
        log.debug("getMagistratesDisIdForUnrelatedVariation - currMagDisId: " + currMagDisId);
        return currMagDisId;
    }

    private Integer getMagistratesDisIdForVariation(DisposalValue currDisposalValue) {
        // Only process if currDisposalValue is a variation disposal or does not
        // belong to the
        // same magistrate disposal as the last processed variation.

        if (currDisposalValue.getPsdDisposal2Id() != null
                && !currDisposalValue.getPsdDisposal2Id().equals(currPsdDisposalId)) {
            for (int i = 0; i < rcv.getDisposalCount(currDisposalValue.getDefendantOnOffenceId()); i++) {
                DisposalValue disposal = rcv.getDisposal(currDisposalValue.getDefendantOnOffenceId(), i);

                // Determine if related magistrates disposal by checking
                // psdDisposal2Id is null and disposal2Id equals the variation
                // psdDisposal2Id
                if (disposal.getPsdDisposal2Id() == null
                        && disposal.getDisposal2Id().equals(currDisposalValue.getPsdDisposal2Id())) {
                    log.debug("Found magistrates");
                    currPsdDisposalId = currDisposalValue.getPsdDisposal2Id();
                    currMagDisId = disposal.getDisId();
                    break;
                } else {
                    log.debug("NOT Found magistrates");
                    currMagDisId = null;
                }
            }
        }
        log.debug("getMagistratesDisIdForVariation - currMagDisId: " + currMagDisId);
        return currMagDisId;
    }

    /**
     *
     * @return ResultsCompositeValue
     */
    public ResultsCompositeValue getResultsCompositeValue() {
        return rcv;
    }

    /**
     * Return empty String if value is null.
     *
     * @param toCheck
     * @return
     */
    public static String checkNull(String toCheck) {
        if (toCheck == null) {
            return emptyStr;
        } else {
            return toCheck;
        }
    }

    /**
     * Concatenates all elements of defendant name and returns as a single
     * string
     *
     * @param dv
     * @return
     */
    public static String getName(DefendantValue dv) {
        String defendantName;
        if (dv == null) {
            defendantName = "";
        } else {
            String firstName = checkNull(dv.getFirstName());
            String middle = checkNull(dv.getMiddleName());
            String lastName = checkNull(dv.getSurName());
            if (middle.equals(emptyStr)) {
                defendantName = firstName + " " + lastName;
            } else {
                defendantName = firstName + " " + middle + " " + lastName;
            }
        }
        return defendantName;
    }

    public static Map<Integer,List<VerdictValue>> getVerdicts(final Integer caseId, final ResultsCompositeValue results) {
    	Map<Integer,List<VerdictValue>> map = new HashMap<Integer,List<VerdictValue>>();
    	// Add the charges verdicts (appeal results)
    	ChargeCompositeValue ccv = results.getChargeCompositeValue();
    	if (ccv.getCharges() != null) {
    		Iterator chargeIterator = ccv.getCharges().iterator();
            while (chargeIterator.hasNext()) {
                ChargeValue chargeValue = (ChargeValue) chargeIterator.next();
                if (chargeValue != null) {
                    if (chargeValue.getOffenceValues() != null) {
                    	Iterator offenceIterator = chargeValue.getOffenceValues().iterator();
                        while (offenceIterator.hasNext()) {
                            OffenceValue offenceValue = (OffenceValue) offenceIterator.next();
                            if (offenceValue.getDefendantValues() != null && !offenceValue.getDefendantValues().isEmpty()) {
                                Iterator defendantIterator = offenceValue.getDefendantValues().iterator();
                                while (defendantIterator.hasNext()) {
                                    DefendantValue defendant = (DefendantValue) defendantIterator.next();
                                    DefendantOnOffenceComplexValue defOnOffence = offenceValue.getDefendantOnOffence(defendant
                                            .getDefendantID());
                                    Integer defOnOffenceId = defOnOffence.getDefendantOnOffenceId();
                                    if (results.getVerdict(defOnOffenceId)!= null) {
                                    	// Update the map
                                    	List<VerdictValue> verdicts = map.get(defOnOffenceId) != null ? map.get(defOnOffenceId) : new ArrayList<VerdictValue>();
                                    	verdicts.add(results.getVerdict(defOnOffenceId));
                                    	map.put(defOnOffenceId, verdicts);
                                    }
                                }
                            }
                        }
                    }
                }
            }
    	}
        return map;
    }
    
    public static ResultsCompositeValue getResults(final Integer caseId, final Integer scheduledHearingId) throws ResultsControllerException {
    	if (scheduledHearingId != null && scheduledHearingId > 0) {
        	return XhibitDelegateHelper.getResults2Delegate().getResults(caseId, scheduledHearingId);
        } else {
        	return XhibitDelegateHelper.getResults2Delegate().getResults(caseId);
        }
    }
}