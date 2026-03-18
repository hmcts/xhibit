package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.authorise.DefOnCaseAndOffenceRule;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AsnFailureMessage;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: DefendantAsnRule.
 * </p>
 * <p>
 * Description: Validates ASN is present on the Defendant On Case
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: DefendantAsnRule.java,v 1.0 2007/08/18 18:44:58 qz4rwx
 *          Exp $
 */

public class DefendantAsnRule implements DefOnCaseAndOffenceRule {
    private static final Logger log = CSServices.getLogger(DefendantAsnRule.class);

    private static DefendantAsnRule instance = null;

    private static final String TRIAL_CASE_TYPE = "T";    
    private static final String SENTENCE_CASE_TYPE = "S";
    private static final String APPEAL_CASE_TYPE = "A";
    
    private DefendantAsnRule() {
    }

    public static DefendantAsnRule getInstance() {
        if (instance == null) {
            instance = new DefendantAsnRule();
        }
        return instance;
    }
        
    public FailureMessage[] process(DefendantChargesCompositeVO dcc, boolean isUnrelatedDisposal) {
        log.debug("process DefendantAsnRule - BEGIN");

        List<AsnFailureMessage> failures = new ArrayList<AsnFailureMessage>();

        AsnFailureMessage message = null;
        
        if(dcc.getDefendantOnCase().getAsn()==null || 
           dcc.getDefendantOnCase().getAsn().trim().equals(""))
        {
            if(log.isDebugEnabled())
            {
                log.debug("Defendant has no ASN recorded, Defendant ID is: " + 
                          dcc.getDefendantOnCase().getDefendantId() + " and the Case ID is: " + 
                          dcc.getDefendantOnCase().getCaseId());
            }
   
            log.debug("Case Type is: " + dcc.getDefendantOnCase().getCaseType());
            
            if(dcc.getDefendantOnCase().getCaseType().equalsIgnoreCase(TRIAL_CASE_TYPE) ||
               dcc.getDefendantOnCase().getCaseType().equalsIgnoreCase(SENTENCE_CASE_TYPE))
            {
                log.debug("Case is a Trial or Sentence Case");
                
                message = new AsnFailureMessage(
                    "authorise.asn.notrecordeddefendant", new Object[] { getDetail(dcc) });
            }
            else if(dcc.getDefendantOnCase().getCaseType().equalsIgnoreCase(APPEAL_CASE_TYPE))
            {
                log.debug("Case is an Appeal Case");
                     
                message = new AsnFailureMessage(
                    "authorise.asn.notrecordedappellant", new Object[] { getDetail(dcc) });
            }            
            else
            {
                log.debug("Case is an Unknown Case");
                
                message = new AsnFailureMessage(
                    "authorise.asn.notrecordeddefendant", new Object[] { getDetail(dcc) });
            }
            
            failures.add(message);
        }
        else
        {
            if(log.isDebugEnabled())
            {
                log.debug("Defendant has an ASN recorded:" + dcc.getDefendantOnCase().getAsn() + ", where the Defendant ID is: " + 
                    dcc.getDefendantOnCase().getDefendantId() + " and the Case ID is: " + 
                    dcc.getDefendantOnCase().getCaseId());
            }
        }
        
        return failures.toArray(new FailureMessage[failures.size()]);
    }
    
    private String getDetail(DefendantChargesCompositeVO dcc)
    {
        return "";
    }
}