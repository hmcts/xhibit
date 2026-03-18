package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Component;

/**
 * <p>
 * Title: PromptComponent
 * </p>
 * <p>
 * Description: Used to render prompts
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
public interface PromptComponent {
    /**
     * Get the AWT component
     */
    public Component getComponent();

    /**
     * Set the prompt text
     */
    public void setPrompt(String prompt);
}
