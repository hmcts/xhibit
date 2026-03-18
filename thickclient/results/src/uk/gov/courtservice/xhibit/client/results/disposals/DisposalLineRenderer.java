package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalLineRenderer
 * </p>
 * <p>
 * Description: Used to render disposal lines processors and renderers.
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
public interface DisposalLineRenderer {
    /**
     * Set the reference value that is controlling rendering
     */
    public void setReference(DisposalLineReferenceValue reference);

    /**
     * Get the reference value that is controlling rendering
     */
    public DisposalLineReferenceValue getReference();

    /**
     * Set the line basic value (update the component values)
     */
    public void setLine(XhbDisposalLineBasicValue value);

    /**
     * Get the number of lines (update the model values)
     */
    public int getLineCount();

    /**
     * Get the line basic value (update the model values) return null if data
     * has not modified reference value or index out of range
     */
    public XhbDisposalLineBasicValue getLine(int index);

    /**
     * Get the component, used to display/edit the prompt
     */
    public DataComponent getDataComponent();

    /**
     * Get the component, used to display/edit the prompt
     */
    public PromptComponent getPromptComponent();

    /**
     * Get the component, used to display/edit the insert text
     */
    public InsertComponent getInsertComponent();

}
