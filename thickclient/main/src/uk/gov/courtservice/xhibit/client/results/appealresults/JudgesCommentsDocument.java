package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * Title: JudgesCommentsDocument
 * </p>
 * <p>
 * JudgesCommentsDocument ensures that lines are less than 78 bytes in length
 * when encoded as utf8
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class JudgesCommentsDocument extends PlainDocument {
    /**
     * Default maximum number of UTF-8 bytes in a given line is 78
     */
    private static final int MAX_UTF8_BYTES = 78;

    /**
     * The maximum number of UTF-8 bytes in a given line!
     */
    private final int maxUtf8Bytes;

    /**
     * Construct a DisposalDocument with the default values
     */
    public JudgesCommentsDocument() {
        this(MAX_UTF8_BYTES);
    }

    /**
     * Construct a DisposalDocument with the default values
     */
    public JudgesCommentsDocument(int maxUtf8Bytes) {
        if (maxUtf8Bytes < 0) {
            throw new IllegalArgumentException("maxUtf8Bytes: " + maxUtf8Bytes);
        }
        this.maxUtf8Bytes = maxUtf8Bytes;
    }

    /**
     * Get the max UTF-8 bytes for
     */
    public int getMaxUtf8Bytes() {
        return maxUtf8Bytes;
    }

    /**
     * Inserts a string into the document. This will cause a DocumentEvent of
     * type DocumentEvent.EventType.INSERT to be sent to the registered
     * DocumentListers, unless an exception is thrown. If the Document supports
     * undo/redo, an UndoableEditEvent will also be generated.
     * 
     * @param offset
     *            the offset into the document to insert the content str, whose
     *            length >= 0. All positions that track change at or after the
     *            given location will move.
     * @param str
     *            the string to insert.
     * @param a
     *            the attributes to associate with the inserted content. This
     *            may be null if there are no attributes.
     * @throws BadLocationException
     *             the given insert position is not a valid position within the
     *             document
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if (checkInsert(offset, str)) {
            super.insertString(offset, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean checkInsert(int offset, String str) throws BadLocationException {
        Element root = getDefaultRootElement();
        Element row = root.getElement(root.getElementIndex(offset));

        int leadingLen = getUtf8Length(getText(row.getStartOffset(), offset - row.getStartOffset()));
        int trailingLen = getUtf8Length(getText(offset, row.getEndOffset() - offset - 1)); // - 1
                                                                                            // as
                                                                                            // \n
                                                                                            // is
                                                                                            // included
        // 
        // 

        int startIndex = 0;
        int endIndex = str.indexOf('\n');

        if (endIndex != -1) {
            // Insert includes new line ...

            // ... check leading
            int len = leadingLen + getUtf8Length(str.substring(startIndex, endIndex));
            if (len > maxUtf8Bytes) {
                return false;
            }
            startIndex = endIndex + 1; // '\n' is 1 char
            endIndex = str.indexOf('\n', startIndex);

            // ... check middle lines if any
            while (endIndex != -1) {
                len = getUtf8Length(str.substring(startIndex, endIndex));
                if (len > maxUtf8Bytes) {
                    return false;
                }
                startIndex = endIndex + 1; // '\n' is 1 char
                endIndex = str.indexOf('\n', startIndex);
            }

            // ... check trailing
            len = getUtf8Length(str.substring(startIndex)) + trailingLen;
            return len <= maxUtf8Bytes;
        } else {
            // Insert does not include new line
            int len = leadingLen + getUtf8Length(str) + trailingLen;
            return len <= maxUtf8Bytes;
        }
    }

    /**
     * Removes a portion of the content of the document. This will cause a
     * DocumentEvent of type DocumentEvent.EventType.REMOVE to be sent to the
     * registered DocumentListeners, unless an exception is thrown. The
     * notification will be sent to the listeners by calling the removeUpdate
     * method on the DocumentListeners. If the Document supports undo/redo, an
     * UndoableEditEvent will also be generated.
     * 
     * @param offset
     *            the offset from the begining ( >= 0 )
     * @param length
     *            the number of characters to remove ( >= 0 )
     * @throws BadLocationException
     *             some portion of the removal range was not a valid part of the
     *             document. The location in the exception is the first bad
     *             position encountered.
     */
    public void remove(int offset, int length) throws BadLocationException {
        if (checkRemove(offset, length)) {
            super.remove(offset, length);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean checkRemove(int offset, int length) throws BadLocationException {
        // Get the first and last row effected by remove
        Element root = getDefaultRootElement();
        Element startRow = root.getElement(root.getElementIndex(offset));
        Element endRow = root.getElement(root.getElementIndex(offset + length));

        // Calculate how much is left at the start of the first row effected and
        // end of the last row effected
        int len = getUtf8Length(getText(startRow.getStartOffset(), offset - startRow.getStartOffset()))
                + getUtf8Length(getText(offset + length, endRow.getEndOffset() - offset - length - 1)); // - 1
                                                                                                        // as
                                                                                                        // \n
                                                                                                        // is
                                                                                                        // included

        return len <= maxUtf8Bytes;
    }

    /**
     * Utility method for calculating the number of bytes used by the string
     * when encoded in UTF-8
     */
    private static int getUtf8Length(String str) {
        // Note this is not a very efficient way of calculating this but should
        // not matter
        // for the small number of strings used in judges comments if we cant
        // work it out
        // assume worst case, ie each char is encoded over 3 bytes!
        try {
            return str.length() == 0 ? 0 : str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException uee) {
            return str.length() * 3;
        }
    }

    /*
     * // // Test Method // public static void main (String args[]) throws
     * Exception {
     * javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
     * 
     * javax.swing.JFrame frame = new
     * javax.swing.JFrame(JudgesCommentsDocument.class.getName());
     * frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
     * frame.setSize(800, 200); frame.getContentPane().setLayout(new
     * java.awt.GridBagLayout());
     * 
     * java.awt.GridBagConstraints constraints = new
     * java.awt.GridBagConstraints(); constraints.gridx = 0; constraints.gridy =
     * 0; constraints.weightx = 1.0; constraints.weighty = 1.0; constraints.fill =
     * java.awt.GridBagConstraints.BOTH;
     * 
     * javax.swing.JTextArea textArea = new javax.swing.JTextArea(new
     * JudgesCommentsDocument());
     * textArea.setText("123456789012345678901234567890123456789012345678901234567890123456789012345678\n" +
     * "123456789012345678901234567890123456789012345678901234567890123456789012345678\n" +
     * "123456789012345678901234567890123456789012345678901234567890123456789012345678\n");
     * frame.getContentPane().add(new javax.swing.JScrollPane(textArea),
     * constraints);
     * 
     * java.awt.Rectangle screen = frame.getGraphicsConfiguration().getBounds();
     * frame.setLocation(screen.x + ((screen.width - frame.getWidth()) / 2),
     * screen.y + ((screen.height - frame.getHeight()) / 2)); frame.show(); } // //
     * Overridden for test to produce debug info // public String getText(int
     * offset, int length) throws BadLocationException { try { String text =
     * super.getText(offset, length); System.out.println("getText(" + offset +
     * "," + length + "): \"" + text + "\" - " + getUtf8Length(text)); return
     * text; } catch (BadLocationException ble) { System.out.println("getText(" +
     * offset + ", " + length + "):-"); ble.printStackTrace(System.out); throw
     * ble; } }
     */
}
