package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;

/**
 *
 * <p>
 * Title: ResultsCompositeValue
 * </p>
 * <p>
 * Description: Contains the results recorded in Xhibit against the charges on a
 * given case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Paul Morris
 * @author Sarah Tong
 * @version $Id: ResultsCompositeValue.java,v 1.22 2005/01/17 07:47:00 rzvddy
 *          Exp $
 */
public class ResultsCompositeValue extends CSAbstractValue {
    
	static final long serialVersionUID = -1630233985012067516L;
	
	private ChargeCompositeValue chargeComposite;

    /**
     * pleas for defendant on offence.
     */
    private Map pleas = new HashMap();

    /**
     * pleas for defendant on charge (for Breaches)
     */
    private Map chargePleas = new HashMap();

    private Map verdicts = new HashMap();

    private Map chargeVerdicts = new HashMap();

    private Map caseVerdicts = new HashMap();

    /**
     * These are appeal results against Magistrates Courts general disposals.
     */
    private Map disposalVerdicts = new HashMap();

    private SortedListMap disposalByOffence = new SortedListMap();

    private SortedListMap variationDisposalByOffence = new SortedListMap();

    private SortedListMap unrelatedDisposals = new SortedListMap();

    private SortedList caseAppReasonValues = new SortedList();

    private Integer scheduledHearingId = null;

    public ResultsCompositeValue() {
    }

    /**
     * Get the case id
     *
     * @throws IllegalStateException
     *             if it encounters any null data.
     */
    public Integer getCaseId() {
        if (chargeComposite == null) {
            throw new IllegalStateException("chargeComposite: null");
        }

        XhbCaseBasicValue caseBasicValue = chargeComposite.getCaseBasicValue();
        if (caseBasicValue == null) {
            throw new IllegalStateException("chargeComposite.caseBasicValue: null");
        }

        Integer caseId = caseBasicValue.getCaseId();
        if (caseId == null) {
            throw new IllegalStateException("chargeComposite.caseBasicValue.caseId: null");
        }

        return caseId;
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    /**
     * Get the charge ids
     *
     * @throws IllegalStateException
     *             if it encounters any null data.
     */
    public Integer[] getChargeIds() {
        if (chargeComposite == null) {
            throw new IllegalStateException("chargeComposite: null");
        }

        Collection charges = chargeComposite.getCharges();
        if (charges == null) {
            throw new IllegalStateException("chargeComposite.charges: null");
        }

        Vector chargeIds = new Vector();
        int index = 0;

        Iterator iterator = charges.iterator();
        while (iterator.hasNext()) {
            ChargeValue charge = (ChargeValue) iterator.next();
            if (charge == null) {
                throw new IllegalStateException("chargeComposite.charges[" + index + "]: null");
            }
            JoinderChargeInfoValue[] joinderChargeInfoValue = charge.getJoinderChargeInfoValues();
            if (joinderChargeInfoValue != null)// is this a joinder indictment
            {
                // if so get results for all charges in joinder
                for (int i = 0, len = joinderChargeInfoValue.length; i < len; i++) {
                    chargeIds.add(joinderChargeInfoValue[i].getChargeId());
                }
            } else {
                Integer chargeId = charge.getChargeID();
                if (chargeId == null) {
                    throw new IllegalStateException("chargeComposite.charges[" + index + "].chargeId: null");
                }
                chargeIds.add(chargeId);
            }
        }

        return (Integer[]) chargeIds.toArray(new Integer[chargeIds.size()]);
    }

    public void addCaseAppReasonValue(CaseAppReasonValue value) {
        if (value == null || !getCaseId().equals(value.getCaseId())) {
            throw new IllegalArgumentException("value: " + value);
        }

        caseAppReasonValues.add(value);
    }

    /**
     * Get the number of CaseAppReasonValue
     */
    public int getCaseAppReasonValueCount() {
        return caseAppReasonValues.size();
    }

    /**
     * Get the list
     */
    public List getCaseAppReasonValues() {
        List list = new ArrayList();

        for (int i = 0, c = getCaseAppReasonValueCount(); i < c; i++) {
            list.add(getCaseAppReasonValue(i));
        }

        return list;
    }

    /**
     * Get the indexed CaseAppReasonValue
     */
    public CaseAppReasonValue getCaseAppReasonValue(int index) {
        return (CaseAppReasonValue) caseAppReasonValues.get(index);
    }

    /**
     * Gets a plea value for a defendant on offence
     *
     * @param defendantOnOffenceId
     * @return the plea
     */
    public PleaValue getPlea(Integer defendantOnOffenceId) {
        return (PleaValue) pleas.get(defendantOnOffenceId);
    }

    /**
     * Gets a plea value for a defendant on charge - used for Breaches
     *
     * @param defendantOnChargeId
     * @return the plea
     */
    public PleaValue getChargePlea(Integer defendantOnChargeId) {
        return (PleaValue) chargePleas.get(defendantOnChargeId);
    }

    /**
     * Gets a verdict value for a defendant on offence
     *
     * @param defendantOnOffenceId
     * @return the verdict
     */
    public VerdictValue getVerdict(Integer defendantOnOffenceId) {
        return (VerdictValue) verdicts.get(defendantOnOffenceId);
    }
    
    
    public VerdictValue getVerdictByDefOnOff(Integer defendantOnOffenceId)
    {
    	for(VerdictValue verdict:  new ArrayList<VerdictValue>(verdicts.values()))
    	{
    		if(verdict.getXhbVerdictBasicValue().getDefendantOnOffenceId().equals(defendantOnOffenceId))
    		{
    			return verdict;
    		}
    	}
    	
    	return null;
    }

    /**
     * Gets a verdict value for a case
     *
     * @param caseId
     * @return the verdict
     */
    public VerdictValue getCaseVerdict(Integer caseId) {
        return (VerdictValue) caseVerdicts.get(caseId);
    }

    /**
     * Gets a verdict value for a defendant on offence
     *
     * @param defendantOnOffenceId
     * @return the verdict
     */
    public VerdictValue getChargeVerdict(Integer defendantOnChargeId) {
        return (VerdictValue) chargeVerdicts.get(defendantOnChargeId);
    }

    /**
     * Gets a verdict value (Appeal Result) for a Magistrates Court general
     * disposal.
     *
     * @param disposal2Id
     * @return the verdict
     */
    public VerdictValue getVerdictForDisposal(Integer disposal2Id) {
        return (VerdictValue) disposalVerdicts.get(disposal2Id);
    }

    /**
     * Get the number of disposals for the given defendant on Offence id
     */
    public int getDisposalCount(int defendantOnOffenceId) {
        return getDisposalCount(new Integer(defendantOnOffenceId));
    }

    public int getDisposalCount(Integer defendantOnOffenceId) {
        if (defendantOnOffenceId == null) {
            throw new IllegalAccessError("defendantOnOffenceId: null");
        }
        return disposalByOffence.getCount(defendantOnOffenceId);
    }

    /**
     * Gets the disposals for the defendant on Offence
     *
     * @param defendantOnOffenceId
     * @return the disposals
     */
    public DisposalValue getDisposal(int defendantOnOffenceId, int index) {
        return getDisposal(new Integer(defendantOnOffenceId), index);
    }

    public DisposalValue getDisposal(Integer defendantOnOffenceId, int index) {
        return (DisposalValue) disposalByOffence.get(defendantOnOffenceId, index);
    }

    /**
     * Get the number of unrelated disposals for the given defendant on case id
     */
    public int getUnrelatedDisposalCount(int defendantOnCaseId) {
        return getUnrelatedDisposalCount(new Integer(defendantOnCaseId));
    }

    public int getUnrelatedDisposalCount(Integer defendantOnCaseId) {
        if (defendantOnCaseId == null) {
            throw new IllegalAccessError("defendantOnCaseId: null");
        }
        return unrelatedDisposals.getCount(defendantOnCaseId);
    }

    /**
     * Get the number of variation disposals for the given unrelated disposal
     * id.
     *
     * @param defendantOnCaseId
     * @param disposal2Id
     * @return number of variation disposals
     */
    public int getUnrelatedVariationDisposalCount(final Integer defendantOnCaseId, final Integer disposal2Id) {
        int variationCount = 0;
        DisposalValue disposalValue;

        final int count = getUnrelatedDisposalCount(defendantOnCaseId);
        for (int i = 0; i < count; i++) {
            disposalValue = getUnrelatedDisposal(defendantOnCaseId, i);
            if (disposalValue.getPsdDisposal2Id() != null && disposalValue.getPsdDisposal2Id().equals(disposal2Id)) {
                variationCount++;
            }
        }
        return variationCount;
    }

    /**
     * Gets the unrelated disposals for the defendant on case
     *
     * @param defendantOnCaseId
     * @return the unrelated disposals
     */
    public DisposalValue getUnrelatedDisposal(int defendantOnCaseId, int index) {
        return getUnrelatedDisposal(new Integer(defendantOnCaseId), index);
    }

    public DisposalValue getUnrelatedDisposal(Integer defendantOnCaseId, int index) {
        return (DisposalValue) unrelatedDisposals.get(defendantOnCaseId, index);
    }

    /**
     * Get the number of variation disposals for the given defendant on Offence
     * id
     */
    public int getVariationDisposalCount(int defendantOnOffenceId) {
        return getVariationDisposalCount(new Integer(defendantOnOffenceId));
    }

    public int getVariationDisposalCount(Integer defendantOnOffenceId) {
        if (defendantOnOffenceId == null) {
            throw new IllegalAccessError("defendantOnOffenceId: null");
        }
        return variationDisposalByOffence.getCount(defendantOnOffenceId);
    }

    /**
     * Adds a plea value for the defendant on offence
     *
     * @param defendantOnOffenceId
     * @param pleaValue
     */
    public void addPlea(Integer defendantOnOffenceId, PleaValue pleaValue) {
        pleas.put(defendantOnOffenceId, pleaValue);
    }

    /**
     * Adds a plea value for the defendant on charge, used for Breaches
     *
     * @param defendantOnChargeId
     * @param pleaValue
     */
    public void addChargePlea(Integer defendantOnChargeId, PleaValue pleaValue) {
        chargePleas.put(defendantOnChargeId, pleaValue);
    }

    /**
     * Adds a verdict value for case
     *
     * @param defendantOnChargeId
     * @param pleaValue
     */
    public void addCaseVerdict(Integer caseId, VerdictValue verdictValue) {
        caseVerdicts.put(caseId, verdictValue);
    }

    /**
     * Adds a verdict value for the defendant on charge
     *
     * @param defendantOnChargeId
     * @param pleaValue
     */
    public void addChargeVerdict(Integer defendantOnChargeId, VerdictValue verdictValue) {
        chargeVerdicts.put(defendantOnChargeId, verdictValue);
    }

    /**
     * Adds a verdict value for the defendant on offence
     *
     * @param defendantOnOffenceId
     * @param verdictDetailValue
     */
    public void addVerdict(Integer defendantOnOffenceId, VerdictValue verdictValue) {
        verdicts.put(defendantOnOffenceId, verdictValue);
    }

    /**
     * Adds a verdict value (Appeal Result) for a Magistrates Court general
     * disposal.
     *
     * @param disposal2Id
     * @param verdictDetailValue
     */
    public void addVerdictForDisposal(Integer disposal2Id, VerdictValue verdictValue) {
        disposalVerdicts.put(disposal2Id, verdictValue);

    }

    /**
     * Adds disposal value for a defendant on offence (related)
     *
     * @param disposalValue
     *            the value to add
     */
    public void addDisposal(DisposalValue disposalValue) {
        if (disposalValue == null || !disposalValue.isRelatedDisposal()) {
            throw new IllegalArgumentException("disposalValue: " + disposalValue);
        }

        disposalByOffence.add(new Integer(disposalValue.getDefendantOnOffenceId()), disposalValue);

        // If variation disposal then add to count
        if (disposalValue.isVariationDisposal()) {
            variationDisposalByOffence.add(new Integer(disposalValue.getDefendantOnOffenceId()), disposalValue);
        }
    }

    /**
     * Add disposal values for a defendant on offence (related)
     *
     * @param disposalValues
     *            the values to add
     */
    public void addDisposals(DisposalValue[] disposalValues) {
        if (disposalValues == null) {
            throw new IllegalArgumentException("disposalValues: null");
        }

        for (int i = 0; i < disposalValues.length; i++) {
            addDisposal(disposalValues[i]);
        }
    }

    /**
     * Add disposal value for a defendant on case (unrelated)
     *
     * @param disposalValue
     *            the value to add
     */
    public void addUnrelatedDisposal(DisposalValue disposalValue) {
        if (disposalValue == null || !disposalValue.isUnrelatedDisposal()) {
            throw new IllegalArgumentException("disposalValue: " + disposalValue);
        }

        unrelatedDisposals.add(new Integer(disposalValue.getDefendantOnCaseId()), disposalValue);
    }

    /**
     * Add disposal values for a defendant on case (unrelated)
     *
     * @param disposalValues
     *            the values to add
     */
    public void addUnrelatedDisposals(DisposalValue[] disposalValues) {
        if (disposalValues == null) {
            throw new IllegalArgumentException("disposalValues: null");
        }
        for (int i = 0; i < disposalValues.length; i++) {
            addUnrelatedDisposal(disposalValues[i]);
        }
    }

    /**
     * @param chargeCompositeValue
     */
    public void setChargeCompositeValue(ChargeCompositeValue chargeCompositeValue) {
        this.chargeComposite = chargeCompositeValue;
    }

    public ChargeCompositeValue getChargeCompositeValue() {
        return this.chargeComposite;
    }
}