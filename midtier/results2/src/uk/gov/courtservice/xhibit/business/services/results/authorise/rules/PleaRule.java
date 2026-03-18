package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: Validates if a plea is recorded for the defendant on offence
 * </p>
 * <p>
 * Description: Business Rule RESULT54
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PleaRule.java,v 1.6 2009/04/13 13:49:43 hewittm Exp $
 */

public class PleaRule implements OffenceRule {

    private static PleaRule instance = new PleaRule();

    private PleaRule() {
        // empty
    }

    public static PleaRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        Integer defendantOnOffenceId = defendantOnOffence.getDefendantOnOffenceId();
        if (rcv.getPlea(defendantOnOffenceId) == null) {
            return new String[] { "authorise.plea.notrecorded" };
        } else {
            return new String[] {};
        }
    }
}