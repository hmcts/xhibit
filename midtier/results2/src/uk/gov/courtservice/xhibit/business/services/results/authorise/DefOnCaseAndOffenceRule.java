package uk.gov.courtservice.xhibit.business.services.results.authorise;

import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: DefOnCaseAndOffenceRule
 * </p>
 * <p>
 * Description: Interface for rules that process at a Defendant On Case and Defendant On Offence level
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version 1.0
 */

public interface DefOnCaseAndOffenceRule {
    public FailureMessage[] process(DefendantChargesCompositeVO dcc, boolean isUnrelatedDisposal);
}