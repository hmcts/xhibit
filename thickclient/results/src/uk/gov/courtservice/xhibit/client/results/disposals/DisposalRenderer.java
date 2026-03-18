package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

/**
 * <p>
 * Title: DisposalRenderer
 * </p>
 * <p>
 * Description: Used to render disposal s processors and renderers.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.9 $
 */
public interface DisposalRenderer {
    /**
     * Set the reference value that is controlling rendering
     */
    public void setReference(DisposalReferenceValue reference);

    /**
     * Get the reference value that is controlling rendering
     */
    public DisposalReferenceValue getReference();

    /**
     * Set the basic value (update the component values)
     */
    public void setDisposal(DisposalValue value);

    /**
     * Get the basic value (update the model values) return null if data is
     * incomplete
     */
    public DisposalValue getDisposal();

    /**
     * Get the component, used to display/edit the disposal
     */
    public DisposalComponent getDisposalComponent();

    /**
     * Return true if the disposal has errors
     */
    public boolean isComplete();

    /**
     * Add the listener
     */
    public void addDisposalListener(DisposalListener listener);

    /**
     * Remove the listener
     */
    public void removeDisposalListener(DisposalListener listener);


    

}
