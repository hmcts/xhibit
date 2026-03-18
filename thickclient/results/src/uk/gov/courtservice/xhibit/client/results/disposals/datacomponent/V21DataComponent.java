package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V21DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers from 1 to 120
 * </p>
 * <p>
 * </p>
 */
public class V21DataComponent extends TextFieldDataComponent implements DataComponent {

    /**
	 * default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * DefaultDataComponent
     */
    public V21DataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                // Check if the candidate could be valid, set error if not in
                // range
                if (candidate != null && isInteger(candidate, 3)) {
                    setError(candidate.length() > 0 && !between(candidate, 1, 120));
                    return true;
                }
                return false;
            }
        });
        setColumns(3);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v21DataComponentToolTip");
    }

}
