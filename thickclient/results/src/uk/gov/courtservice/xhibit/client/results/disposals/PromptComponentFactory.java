package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.promptcomponent.DefaultPromptComponent;

/**
 * <p>
 * Title: PromptComponentFactory
 * </p>
 * <p>
 * Description: Used to create prompts
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
public class PromptComponentFactory {
    /**
     * Stop createion of this static class
     */
    private PromptComponentFactory() {
        // Change access permision of default constructor
    }

    /**
     * Create the default prompt component
     * 
     * @return a new prompt component
     */
    public static PromptComponent create() {
        return new DefaultPromptComponent();
    }
}
