package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: ScreenPrintDisposalLineProcessor
 * </p>
 * <p>
 * Description: Set screen print to the specified value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class ScreenPrintDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final boolean screenPrint;

    /**
     * Construct a new processor to set the prompt as specified
     */
    public ScreenPrintDisposalLineProcessor(boolean screenPrint) {
        this.screenPrint = screenPrint;
    }

    /**
     * Process implementation, line must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setScreenPrint(screenPrint);
    }
}
