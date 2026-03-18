package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: Verifies at least one disposal is recorded for the offence
 * </p>
 * <p>
 * Description: Business Rule RESULT59 & RESULT60
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisposalRule.java,v 1.6 2009/04/13 13:52:46 hewittm Exp $
 */

public class DisposalRule implements OffenceRule {

    private static DisposalRule instance = new DisposalRule();

    private DisposalRule() {
        // empty
    }

    public static DisposalRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        Integer defendantOnOffenceId = defendantOnOffence.getDefendantOnOffenceId();
         
        if (rcv.getDisposalCount(defendantOnOffenceId) > 0) {
            return new String[] {};
        } else {
            return new String[] { "authorise.disposal.notrecorded" };
        }
    }
}