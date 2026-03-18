package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomScrollPane;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomTextArea;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;

/**
 * <p>
 * Title: TextLimitedJTextArea
 * </p>
 * <p>
 * Description: Class to utilise a CustomTextArea to provide a multi-lined view
 * of plain text, supporting tab traversal. This will limit the amount of text
 * that can be entered
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan, Neil Ellis & Neil Entwistle
 * @version 1.0
 */

public class TextLimitedJTextArea extends AbstractOrderComponent implements KeyListener {
    private static final Logger log = CSServices.getLogger(TextLimitedJTextArea.class);

    private int maxLength;

    private int keyCode;

    private CustomTextArea textArea;

    private String validChars1;

    /**
     * Constructor
     * 
     * @param text
     *            text to display
     * @param rows
     *            number of rows to display
     * @param cols
     *            number of cols to display
     * @param length
     *            limit of text that can be entered
     */
    public TextLimitedJTextArea(String text, int rows, int cols, String length) {
        log.debug("$$$>>> TextLimitedJTextArea text |" + text + "|");
        if (length == null) {
            this.maxLength = 10;
        } else {
            this.maxLength = Integer.parseInt(length);
        }
        // The LimitedTextDocument will prevent paste from going
        // over the text limit.
        textArea = new CustomTextArea(new LimitedTextDocument(maxLength), text, rows, cols);
        initComponent();
    }

    /**
     * Constructor
     * 
     * @param s
     *            text to display
     * @param l
     *            columns
     */
    public TextLimitedJTextArea(String s, int l) {
        this(s, 2, l, "10");
    }

    /**
     * Constructor
     * 
     * @param s
     *            text to display
     */
    public TextLimitedJTextArea(String s) {
        this(s, 10);
    }

    /**
     * Constructs a new CustomTextArea with the specified text and number of
     * rows and columns. These attributes are obtained via the
     * OrderComponentHelper class. The text area always has a vertical scroll
     * bar.
     */
    public void initComponent() {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(Color.gray));
        CustomScrollPane scroll = new CustomScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints constraints = getDefaultConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridy = 1;
        add(scroll, constraints);
        setVisualComponent(this);
        textArea.addKeyListener(this);
    }

    /**
     * Invoked when a component loses the keyboard focus and changes the value
     * of the name/value pair corresponding to the text area.
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    /**
     * Check if the maximum length of the text field has been exceeded and also
     * also if keyCode = 8
     * 
     * @param e
     */
    public void keyTyped(KeyEvent e) {
        String text = textArea.getText();

        if ((text.length() > maxLength) && (this.keyCode != 8)) {
            e.consume();
        }

    }

    /**
     * Empty implementation of keyReleased
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        // no implementation
    }

    /**
     * TextLimitedJTextArea have the label defined in the XML. We do not want
     * any additional lebels for these components.
     * 
     * @return boolean
     */
    public boolean isLabelled() {
        return false;
    }

    public CustomTextArea getTextArea() {
        return textArea;
    }

}
