package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.AppealCaseLevelRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.AppealResultDisposalRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.AppealResultRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.CaseClosedRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.DisposalRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.PleaRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.VerdictRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.DefendantAsnRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.OffenceSeqNoRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.OriginalChargeRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.UnrelatedDisposalRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.OffenceAddressRule;
import uk.gov.courtservice.xhibit.business.services.results.authorise.rules.OffenceStartDateRule;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthorisationRuleFactory.java,v 1.4 2005/01/28 08:55:14 rzvddy
 *          Exp $
 */

public class AuthorisationRuleFactory {
    private static final Logger log = CSServices.getLogger(AuthorisationWorkFlow.class);

    private static final String APPEAL_CASE_TYPE = "A";
    
    private static final String TRIAL_CASE_TYPE = "T";

    private static final String MISC_APPEAL_SUB_TYPE = "O";

    private static HashMap<String, OffenceRule[]> offenceRuleMap = 
        new HashMap<String, OffenceRule[]>();
    
    private static HashMap<String, OffenceRule[]> bichardOffenceRuleMap = 
        new HashMap<String, OffenceRule[]>();

    private AuthorisationRuleFactory() {
        // empty
    }

    public static CaseRule[] getCaseLevelRules(String caseType, String caseSubType) {
        if (caseSubType != null && caseType.equals(APPEAL_CASE_TYPE) && !caseSubType.equals(MISC_APPEAL_SUB_TYPE)) {
            return new CaseRule[] { AppealCaseLevelRule.getInstance(), CaseClosedRule.getInstance() };
        } else {
            return new CaseRule[] { CaseClosedRule.getInstance() };
        }
    }

    public static OffenceRule[] getOffenceLevelRules(boolean checkBichardData, String chargeType) {
        if (checkBichardData) {
            if (bichardOffenceRuleMap.containsKey(chargeType)) {
                return bichardOffenceRuleMap.get(chargeType);
            } else {
                OffenceRule[] rules = getBichardOffenceRules(chargeType);
                bichardOffenceRuleMap.put(chargeType, rules);
                return rules;
            }
        } else {
            if (offenceRuleMap.containsKey(chargeType)) {
                return offenceRuleMap.get(chargeType);
            } else {
                OffenceRule[] rules = getOffenceRules(chargeType);
                offenceRuleMap.put(chargeType, rules);
                return rules;
            }
        }
    }

    private static OffenceRule[] getBichardOffenceRules(String chargeType) {
        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            return new OffenceRule[] { 
                    PleaRule.getInstance(), 
                    VerdictRule.getInstance(), 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance(),
                    OffenceAddressRule.getInstance(), // checks bichard data
                    OffenceStartDateRule.getInstance() // checks bichard data
                    };
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
            return new OffenceRule[] { 
                    AppealResultRule.getInstance(), 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance() 
                    };
        } else if (chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())) { 
            return new OffenceRule[] { 
                    DisposalRule.getInstance(), 
                    //OffenceSeqNoRule.getInstance()
                    };
        } else if (chargeType.equals(ChargeTypes.BREACH.getChargeType())
                || chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())
                || chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            return new OffenceRule[] { 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance(),
                    OffenceAddressRule.getInstance(), // checks bichard data
                    OffenceStartDateRule.getInstance() // checks bichard data
                    };
        } else {
            return new OffenceRule[] { OffenceSeqNoRule.getInstance() };
        }
    }
    
    private static OffenceRule[] getOffenceRules(String chargeType) {
        if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            return new OffenceRule[] { 
                    PleaRule.getInstance(), 
                    VerdictRule.getInstance(), 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance() };
        } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
            return new OffenceRule[] { 
                    AppealResultRule.getInstance(), 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance() };
        } else if(chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType())){
            return new OffenceRule[] {
                    DisposalRule.getInstance()};            
        }else if (
                chargeType.equals(ChargeTypes.BREACH.getChargeType())
                || chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())
                || chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            return new OffenceRule[] { 
                    DisposalRule.getInstance(), 
                    OffenceSeqNoRule.getInstance() };
        } else {
            return new OffenceRule[] { OffenceSeqNoRule.getInstance() };
        }
    }

    public static DefendantRule[] getDefendantLevelRules(String caseType, String caseSubType) {
        log.debug("getDefendantLevelRules - BEGIN");

        if (caseSubType != null && caseType.equals(APPEAL_CASE_TYPE) && !caseSubType.equals(MISC_APPEAL_SUB_TYPE)) {
            return new DefendantRule[] { AppealResultDisposalRule.getInstance() };
        } else {
            return new DefendantRule[] {};
        }
    }

    public static DefOnCaseAndOffenceRule[] getDefOnCaseAndOffenceLevelRules(String caseType) {
        log.debug("getDefendantOnCaseAndOffenceLevelRules - BEGIN, caseType: " + caseType);
        if(caseType.trim().equalsIgnoreCase(TRIAL_CASE_TYPE))
        {
            return new DefOnCaseAndOffenceRule[] {DefendantAsnRule.getInstance(), OriginalChargeRule.getInstance(), UnrelatedDisposalRule.getInstance()};
        }
        else
        {
            return new DefOnCaseAndOffenceRule[] {DefendantAsnRule.getInstance()};
        }
    }
}