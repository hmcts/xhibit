package uk.gov.courtservice.xhibit.business.services.results.authorise;

import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: Interface for rules that process at a defendant level.
 * </p>
 * <p>
 * Description: Interface for rules that process at a defendant level. E.g. for
 * unrelated disposals that are not related to a charge or an offence.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Id: DefendantRule.java,v 1.3 2006/06/05 12:29:55 bzjrnl Exp $
 */

public interface DefendantRule {
    public FailureMessage[] process(ResultsCompositeValue rcv, Integer defendantOnCaseId);
}