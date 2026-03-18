package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.DefOnCaseAndOffenceRule;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.OriginalChargeFailureMessage;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: OriginalChargeRule
 * </p>
 * <p>
 * Description:
 * Where no Indictment counts exists for a particular defendant on a Trial Case:
 * At least one Original Charge consisting of a Sequence Number and Textual Description
 * must be supplied for the defendant on a particular case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: OriginalChargeRule.java,v 1.3 2007/08/29 16:58:25 qz4rwx Exp $
 */

public class OriginalChargeRule implements DefOnCaseAndOffenceRule {
    private static final Logger log = CSServices.getLogger(OriginalChargeRule.class);

    private static final OriginalChargeRule instance = new OriginalChargeRule();

    private OriginalChargeRule() {
    }

    public static OriginalChargeRule getInstance() {
        return instance;
    }

    public FailureMessage[] process(DefendantChargesCompositeVO dcc, boolean isUnrelatedDisposal) {
        
        log.debug("process OriginalChargeRule - BEGIN");
        
        if(log.isDebugEnabled())
        {
            log.debug("Def On Case ID is: " + dcc.getDefendantOnCase().getDefendantOnCaseId());
            log.debug("Total Indictments: " + dcc.getDefendantOnCase().getTotalIndictments());
            log.debug("Total Original Charges: " + dcc.getDefendantOnCase().getTotalOriginalCharges());
        }
        
        List<OriginalChargeFailureMessage> failures = new ArrayList<OriginalChargeFailureMessage>();
        OriginalChargeFailureMessage message = null;
        
        if((dcc.getDefendantOnCase().getTotalIndictments()==null ||
            dcc.getDefendantOnCase().getTotalIndictments().intValue()==0) &&      
           (dcc.getDefendantOnCase().getTotalOriginalCharges()==null ||
            dcc.getDefendantOnCase().getTotalOriginalCharges().intValue()==0))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Defendant has no Charges of type Indictment or Original Charge, Defendant ID is: " + 
                          dcc.getDefendantOnCase().getDefendantId() + " and the Case ID is: " + dcc.getDefendantOnCase().getCaseId());
            }
            message = new OriginalChargeFailureMessage(
                "authorise.originalcharge.notrecorded", new Object[] { getDetail(dcc) });
            failures.add(message);
        }

        return failures.toArray(new FailureMessage[failures.size()]);
    }
    
    private String getDetail(DefendantChargesCompositeVO dcc)
    {
        return "";
    }
}
