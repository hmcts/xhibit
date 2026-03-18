package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: Verifies that an appeal result is recorded for the offence
 * </p>
 * <p>
 * Description: Business Rule RESULT58
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AppealResultRule.java,v 1.6 2009/04/13 13:53:48 hewittm Exp $
 */

public class AppealResultRule implements OffenceRule {

    private static final Logger LOG = CSServices.getLogger(AppealResultRule.class);

    private static AppealResultRule instance = new AppealResultRule();

    private AppealResultRule() {
        // empty
    }

    public static AppealResultRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        Integer defendantOnOffenceId = defendantOnOffence.getDefendantOnOffenceId();
        debugVerdict(rcv.getVerdict(defendantOnOffenceId));
        if (rcv.getVerdict(defendantOnOffenceId) == null) {
            return new String[] { "authorise.appealresult.notrecorded" };
        } else {
            return new String[] {};
        }
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