package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: LineInsertDisposalLineProcessor
 * </p>
 * <p>
 * Description: Set line insert to the specified value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class LineInsertDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final boolean lineInsert;

    /**
     * Construct a new processor to set the prompt as specified
     */
    public LineInsertDisposalLineProcessor(boolean lineInsert) {
        this.lineInsert = lineInsert;
    }

    /**
     * Process implementation, line must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setLineInsert(lineInsert);
    }
}
