package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: PromptDisposalLineProcessor
 * </p>
 * <p>
 * Description: PromptDisposalLineProcessor set the prompt to the specified
 * value
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
public class PromptDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final String prompt;

    /**
     * Construct a new processor to set the prompt as specified
     */
    public PromptDisposalLineProcessor(String prompt) {
        this.prompt = prompt;
    }

    /**
     * Process implementation, line must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setPrompt(prompt);
    }
}
