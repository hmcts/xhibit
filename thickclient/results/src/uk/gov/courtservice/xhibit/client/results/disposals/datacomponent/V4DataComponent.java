package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V4DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers from 0 to 99999999.99
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.7 $
 */
public class V4DataComponent extends TextFieldDataComponent implements DataComponent {
    /**
     * DefaultDataComponent
     */
    public V4DataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                return candidate != null && isNumber(candidate, 8, 2);
            }
        });
        setColumns(10);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v4DataComponentToolTip");
    }

}
