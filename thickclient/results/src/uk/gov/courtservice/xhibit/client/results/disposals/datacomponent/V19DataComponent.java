package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V19DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers greater than zero.
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @version $Id: V19DataComponent.java,v 1.2 2009/03/31 14:33:10 hewittm Exp $
 */
public class V19DataComponent extends TextFieldDataComponent implements DataComponent {

    private static final long serialVersionUID = 1L;

    /**
     * DefaultDataComponent
     */
    public V19DataComponent() {
        setDocument(new ValidatingDocument() {

            private static final long serialVersionUID = 1L;

            public boolean validate(String candidate) {
                // Check if the candidate could be valid, set error if not in
                // range
                if (candidate != null && isInteger(candidate, 2)) {
                    setError(candidate.length() > 0 && !between(candidate, 1, Integer.MAX_VALUE));
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
        return DisposalUtil.getToolTipText("v19DataComponentToolTip");
    }

}

