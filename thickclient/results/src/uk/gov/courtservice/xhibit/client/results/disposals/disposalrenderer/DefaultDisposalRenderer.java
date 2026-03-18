package uk.gov.courtservice.xhibit.client.results.disposals.disposalrenderer;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalComponentFactory;

/**
 * <p>
 * Title: AbstractDisposalRenderer
 * </p>
 * <p>
 * Description: Provide common renderer behaviour.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public class DefaultDisposalRenderer extends AbstractDisposalRenderer {
    /**
     * AbstractDisposalRenderer Implementation
     */
    protected DisposalComponent createDisposalComponent(boolean hateCrimeTabVisible, boolean aggravatingTabVisible) {
        return DisposalComponentFactory.create(hateCrimeTabVisible, aggravatingTabVisible);
    }

}
