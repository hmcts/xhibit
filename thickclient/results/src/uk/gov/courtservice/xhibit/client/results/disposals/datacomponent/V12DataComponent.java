package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V8DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers from 6 to 24
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class V12DataComponent extends TextFieldDataComponent implements DataComponent {

    /**
     * DefaultDataComponent
     */
    public V12DataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                // Check if the candidate could be valid, set error if not in
                // range
                if (candidate != null && isInteger(candidate, 2)) {
                    setError(candidate.length() > 0 && !between(candidate, 6, 24));
                    return true;
                }
                return false;
            }
        });
        setColumns(2);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v12DataComponentToolTip");
    }
}
