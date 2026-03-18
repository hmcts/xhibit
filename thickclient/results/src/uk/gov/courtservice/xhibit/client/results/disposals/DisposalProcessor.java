package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DisposalProcessor
 * </p>
 * <p>
 * Description: Used to process disposal
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
public interface DisposalProcessor {
    /**
     * Process the disposal, alter the disposal to improve presentation etc.
     * 
     * @param the
     *            disposal to process
     * @throws IllegalArgumentException
     *             if
     */
    public void process(DisposalReferenceValue disposal);
}
