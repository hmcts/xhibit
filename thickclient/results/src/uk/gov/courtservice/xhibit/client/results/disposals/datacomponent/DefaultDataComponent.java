package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: DefaultDataComponent
 * </p>
 * <p>
 * Description: Use a label for the data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.14 $
 */
public class DefaultDataComponent extends TextFieldDataComponent implements DataComponent {
    /**
     * DefaultDataComponent
     */
    public DefaultDataComponent() {
        setDocument(new ValidatingDocument() {
            public boolean validate(String candidate) {
                return candidate.length() <= getMaxChars();
            }
        });
    }

    /**
     * DataComponent Implementation
     */
    public boolean isFixedSize() {
        return getMaxChars() < MAX_DISPLAY_LENGTH;
    }

    /**
     * TextFieldDataComponent Implementation
     */
    public void setMaxChars(int maxChars) {
        super.setMaxChars(maxChars);
        setColumns((maxChars > 0 && maxChars < MAX_DISPLAY_LENGTH) ? maxChars : MAX_DISPLAY_LENGTH);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("defaultDataComponentToolTip", new Integer(getMaxChars()));
    }

}
