package uk.gov.courtservice.xhibit.client.order.gui.components;

import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * <p>
 * Title: CustomTextArea
 * </p>
 * <p>
 * Description: Custom text area to provide a multi-lined view of plain text,
 * supporting tab traversal.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */

public class CustomTextArea extends JTextArea {
    /**
     * Constructs a new CustomTextArea with the specified text and number of
     * rows and columns.
     * 
     * @param str1
     *            the text to be displayed, or null
     * @param rows
     *            the number of rows >= 0
     * @param cols
     *            the number of columns >= 0
     */
    public CustomTextArea(String str1, int rows, int cols) {
        super(str1, rows, cols);
    }

    /**
     * Constructs a new CustomTextArea with the specified text and number of
     * rows and columns.
     * 
     * @param doc
     *            the document
     * @param str1
     *            the text to be displayed, or null
     * @param rows
     *            the number of rows >= 0
     * @param cols
     *            the number of columns >= 0
     */
    public CustomTextArea(Document doc, String str1, int rows, int cols) {
        super(doc, str1, rows, cols);
    }

    /**
     * Ensures tab traversal is supported when focus is gained.
     * 
     * @return false if supporting tab traversal
     */
    public boolean isManagingFocus() {
        return false;
    }
}