package uk.gov.courtservice.xhibit.client.results.disposals.promptcomponent;

import java.awt.Component;

import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.client.results.disposals.PromptComponent;

/**
 * <p>
 * Title: DefaultPromptComponent
 * </p>
 * <p>
 * Description: Use a label for the prompt
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
public class DefaultPromptComponent extends JLabel implements PromptComponent {
    /**
     * PromptComponent Implementation
     */
    public Component getComponent() {
        return this;
    }

    /**
     * PromptComponent Implementation
     */
    public void setPrompt(String prompt) {
        setText(prompt);
    }
}
