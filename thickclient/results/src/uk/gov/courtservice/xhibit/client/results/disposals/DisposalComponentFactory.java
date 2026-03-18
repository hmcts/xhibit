package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent.DefaultDisposalComponent;

/**
 * <p>
 * Title: DisposalComponentFactory
 * </p>
 * <p>
 * Description: Used to create Disposals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class DisposalComponentFactory {
    /**
     * Stop createion of this static class
     */
    private DisposalComponentFactory() {
        // Change permisions of default constructor
    }

    /**
     * Create the default Disposal component
     * 
     * @return a new Disposal component
     */
    public static DisposalComponent create(boolean hateCrimeTabVisible, boolean aggravatingTabVisible) {
        return new DefaultDisposalComponent(hateCrimeTabVisible, aggravatingTabVisible);
    }
}
