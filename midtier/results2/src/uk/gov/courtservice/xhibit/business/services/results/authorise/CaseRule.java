package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.Map;

import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: Interface for rules that process at a case level
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
 * @version 1.0
 */

public interface CaseRule {
    public String[] process(ResultsCompositeValue rcv, Map <Integer,DefendantChargesCompositeVO> selectedDefendantChargesCompositeMap);
}