package uk.gov.courtservice.xhibit.business.services.results.authorise;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: Rule template for Offence rules
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: OffenceRule.java,v 1.6 2009/04/13 13:57:42 hewittm Exp $
 */
public interface OffenceRule {
    public String[] process(
            ResultsCompositeValue rcv,
            OffenceValue offence,
            DefendantOnOffenceComplexValue defendantOnOffence);
}
