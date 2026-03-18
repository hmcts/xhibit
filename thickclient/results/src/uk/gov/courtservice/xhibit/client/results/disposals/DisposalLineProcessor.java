package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalLineProcessor
 * </p>
 * <p>
 * Description: Used to process disposal lines
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
public interface DisposalLineProcessor {
    /**
     * Process the disposal line, alter the line to improve presentation etc.
     * 
     * @param the
     *            disposal line to process
     * @throws IllegalArgumentException
     *             if
     */
    public void process(DisposalLineReferenceValue line);
}