package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V3DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers from 0 to 9999
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.8 $
 */
public class V3DataComponent extends TextFieldDataComponent implements DataComponent {

    /**
     * DefaultDataComponent
     */
    public V3DataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                return candidate != null && isInteger(candidate, 7);
            }
        });
        setColumns(7);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v3DataComponentToolTip");
    }

}
