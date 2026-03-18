package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V5DataComponent
 * </p>
 * <p>
 * Description: Use a date control to ensure we have a valid date
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class V5DataComponent extends DateFieldDataComponent {
    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v5DataComponentToolTip");
    }
}
