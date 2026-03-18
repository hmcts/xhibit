package uk.gov.courtservice.xhibit.business.vos.services.charge;

//JDK
import java.util.HashMap;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 * @version $Id: JoinderIndictmentValue.java,v 1.2 2005/02/10 13:54:59 sz0t7n
 *          Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 05/03/03 - ARH - First release.
 * </P>
 */
public class JoinderIndictmentValue extends CSAbstractValue {

    /**
     * new Indictment charge value object that is to be created for each case.
     * The DefendantOnOffenceBasicValues that are held in the offenceValues
     * Collection and keyed on defendantId are the actual defendants (see
     * defendantOnCaseIdAliases)
     */
    private ChargeValue newChargeValue;

    /**
     * Integer array of cases that will be joined.
     */
    private Integer[] originalJoiningCaseIds;

    /**
     * Integer array of infdictmentIds that will be joined. These indictments
     * will be stayed and their results will be moved to the results of the new
     * indictment.
     */
    private Integer[] originalJoiningChargeIds;

    /**
     * A hashmap of integer pairs keyed on caseId. The pair of Integers
     * represents the alias for each defendant that is merged. The first integer
     * represents the lead/display defendantOnCaseId. The second integer
     * represents the actual defendantOnCaseId i.e the one that the defendant
     * was also known as.
     */
    private java.util.HashMap defendantOnCaseAliases;

    private Integer refJudgeId;
    private static final long serialVersionUID = -8891740230222929242L;

    public JoinderIndictmentValue() {
    }

    public JoinderIndictmentValue(ChargeValue newChargeValue, Integer[] originalJoiningCaseIds,
            Integer[] originalJoiningChargeIds, HashMap defendantOnCaseAliases) {
        this.newChargeValue = newChargeValue;
        this.originalJoiningCaseIds = originalJoiningCaseIds;
        this.originalJoiningChargeIds = originalJoiningChargeIds;
        this.defendantOnCaseAliases = defendantOnCaseAliases;

    }

    public ChargeValue getNewChargeValue() {
        return newChargeValue;
    }

    public void setNewChargeValue(ChargeValue newChargeValue) {
        this.newChargeValue = newChargeValue;
    }

    public void setOriginalJoiningCaseIds(Integer[] originalJoiningCaseIds) {
        this.originalJoiningCaseIds = originalJoiningCaseIds;
    }

    public Integer[] getOriginalJoiningCaseIds() {
        return originalJoiningCaseIds;
    }

    public void setOriginalJoiningChargeIds(Integer[] originalJoiningChargeIds) {
        this.originalJoiningChargeIds = originalJoiningChargeIds;
    }

    public Integer[] getOriginalJoiningChargeIds() {
        return originalJoiningChargeIds;
    }

    public void setDefendantOnCaseAliases(java.util.HashMap defendantOnCaseAliases) {
        this.defendantOnCaseAliases = defendantOnCaseAliases;
    }

    public java.util.HashMap getDefendantOnCaseAliases() {
        return defendantOnCaseAliases;
    }

    public void setRefJudgeId(Integer refJudgeId) {
        this.refJudgeId = refJudgeId;
    }

    public Integer getRefJudgeId() {
        return refJudgeId;
    }
}