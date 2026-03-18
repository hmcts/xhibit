package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: V22DataComponent
 * </p>
 * <p>
 * Description: Accept data numbers from 1 to 999
 * </p>
 * <p>
 * </p>
 */
public class V22DataComponent extends TextFieldDataComponent implements DataComponent {

    /**
	 * default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * DefaultDataComponent
     */
    public V22DataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                // Check if the candidate could be valid, set error if not in
                // range
                if (candidate != null && isInteger(candidate, 3)) {
                    setError(candidate.length() > 0 && !between(candidate, 1, 999));
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
        return DisposalUtil.getToolTipText("v22DataComponentToolTip");
    }

}
