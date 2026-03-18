package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: BorderedTextAreaPanel
 * </p>
 * <p>
 * Description: Creates a panel with a text area, and gives the border the name
 * specified
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version 1.0
 */

public class BorderedTextAreaPanel extends JPanel {

    /** The text area */
    private JTextArea textArea = null;

    /**
     * Creates a bordered text area panel.
     * 
     * @param borderName
     *            the name to put on the border.
     */
    public BorderedTextAreaPanel(String borderName) {
        setBorder(new TitledBorder(borderName));

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 1.0f;
        gbc.weighty = 1.0f;
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JScrollPane(getTextArea()), gbc);
    }

    /**
     * Get the text area. Instantiate it if it doesn't already exist.
     * 
     * @return the text area.
     */
    public JTextArea getTextArea() {
        if (textArea == null) {
            textArea = new JTextArea(new JudgesCommentsDocument());

        }
        return textArea;
    }

}