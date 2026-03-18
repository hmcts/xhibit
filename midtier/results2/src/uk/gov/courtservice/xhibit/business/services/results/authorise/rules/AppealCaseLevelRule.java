package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.CaseRule;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: Rule for validating that an appeal result as case level has been
 * recorded
 * </p>
 * <p>
 * Description: This rule will be called for all types of appeals, but only
 * processes criminal appeals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AppealCaseLevelRule.java,v 1.6 2006/06/05 12:29:55 bzjrnl Exp $
 */
public class AppealCaseLevelRule implements CaseRule {

    private static final Logger LOG = CSServices.getLogger(AppealCaseLevelRule.class);

    private static AppealCaseLevelRule instance = new AppealCaseLevelRule();

    private AppealCaseLevelRule() {
    }

    public static AppealCaseLevelRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, Map <Integer,DefendantChargesCompositeVO> selectedDefendantChargesCompositeMap) {
        ArrayList returnString = new ArrayList();

        // Process case level verdict.
        VerdictValue cvv = rcv.getCaseVerdict(rcv.getCaseId());
        debugVerdict(cvv);
        if (cvv == null) {
            returnString.add("authorise.appealcaselevel.appealresult.notrecorded");
        }

        return (String[]) returnString.toArray(new String[returnString.size()]);
    }

    private void debugVerdict(VerdictValue vv) {
        if (LOG.isDebugEnabled()) {
            if (vv == null)
                LOG.debug("Verdict Value is null");
            else
                LOG.debug("Verdict Value: " + vv.toString());
        }
    }
}