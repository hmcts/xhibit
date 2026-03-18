package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: Validates if a verdict is corrently recorded for the defendant on
 * offence
 * </p>
 * <p>
 * Description: Business Rule RESULT57 Verdict only required to be recorded for
 * pleas of NG, CPNG, CPGJ
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: VerdictRule.java,v 1.8 2009/04/13 13:48:47 hewittm Exp $
 */

public class VerdictRule implements OffenceRule {
    private static final Logger LOG = CSServices.getLogger(AppealResultRule.class);

    private static final VerdictRule instance = new VerdictRule();

    private VerdictRule() {
        // empty
    }

    public static VerdictRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        Integer defendantOnOffenceId = defendantOnOffence.getDefendantOnOffenceId();
        if (requiresVerdict(rcv, defendantOnOffenceId) && rcv.getVerdict(defendantOnOffenceId) == null) {
            return new String[] { "authorise.verdict.notrecorded" };
        } else {
            return new String[] {};
        }
    }

    private boolean requiresVerdict(ResultsCompositeValue rcv, Integer defendantOnOffenceId ) {
        PleaValue pv = rcv.getPlea(defendantOnOffenceId);
        boolean required = (pv != null && pv.getRefPleaCode() != null && (!pv.isGuilty()));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Verdict required for this pleacode " + (pv == null ? "No plea" : pv.getRefPleaCode()) + ": "
                    + required);
        }
        return required;
    }
}
