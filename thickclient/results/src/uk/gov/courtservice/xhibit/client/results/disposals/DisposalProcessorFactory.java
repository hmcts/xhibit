package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposalprocessor.LineAvailDisposalProcessor;

/**
 * <p>
 * Title: DisposalProcessorFactory
 * </p>
 * <p>
 * Description: DisposalProcessorFactory used to create disposal processors.
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
public class DisposalProcessorFactory {
    /**
     * Stop static factory creation
     */
    private DisposalProcessorFactory() {
        // Change permisions of default constructor
    }

    /**
     * Create a new processore to modify a disposal
     * 
     * @param lineAvail
     *            the new value
     * @return the new processor
     */
    public static DisposalProcessor createLineAvail() {
        return new LineAvailDisposalProcessor();
    }

}
