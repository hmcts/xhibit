package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.DefOnCaseAndOffenceRule;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnrelatedDisposalFailureMessage;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: UnrelatedDisposalRule
 * </p>
 * <p>
 * Description:
 * Where no Indictment counts exists for a particular defendant on a Trial Case:
 * A disposal must be recorded against the defendant on a particular case
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: UnrelatedDisposalRule.java,v 1.3 2007/08/29 16:58:25 qz4rwx Exp $
 */

public class UnrelatedDisposalRule implements DefOnCaseAndOffenceRule {
    private static final Logger log = CSServices.getLogger(UnrelatedDisposalRule.class);

    private static final UnrelatedDisposalRule instance = new UnrelatedDisposalRule();

    private UnrelatedDisposalRule() {
    }

    public static UnrelatedDisposalRule getInstance() {
        return instance;
    }

    public FailureMessage[] process(DefendantChargesCompositeVO dcc, boolean isUnrelatedDisposal) {
        log.debug("process UnrelatedDisposalRule - BEGIN, isUnrelatedDisposal:" + isUnrelatedDisposal);

        if(log.isDebugEnabled())
        {
            log.debug("Total Indictments: " + dcc.getDefendantOnCase().getTotalIndictments());
        }
        
        List<UnrelatedDisposalFailureMessage> failures = new ArrayList<UnrelatedDisposalFailureMessage>();
        UnrelatedDisposalFailureMessage message = null;
        
       if((dcc.getDefendantOnCase().getTotalIndictments()==null ||
           dcc.getDefendantOnCase().getTotalIndictments().intValue()==0) &&      
           !isUnrelatedDisposal)
        {
            if(log.isDebugEnabled())
            {
                log.debug("Defendant has no Charges of type Indictment and has no Unrelated Disposals, Defendant ID is: " + 
                          dcc.getDefendantOnCase().getDefendantId() + " and the Case ID is: " + 
                          dcc.getDefendantOnCase().getCaseId());
            }
            message = new UnrelatedDisposalFailureMessage(
                "authorise.unrelateddisposal.notrecorded", new Object[] { getDetail(dcc) });
            failures.add(message);
        }

        return failures.toArray(new FailureMessage[failures.size()]);
    }
    
    private String getDetail(DefendantChargesCompositeVO dcc)
    {
        return "";
    }
}
