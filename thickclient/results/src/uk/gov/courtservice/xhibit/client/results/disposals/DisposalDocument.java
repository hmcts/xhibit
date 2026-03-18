package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * Title: DisposalDocument
 * </p>
 * <p>
 * Disposal Document adds the following functionality to PlainDocument
 * </p>
 * <ul>
 * <li>Hard Tabs: Replaces tabs with spaces.</li>
 * <li>Rows: Efficiently get the row count and row data.</li>
 * <li>Max Columns: Specify a maximum number of columns.</li>
 * </ul>
 * <p>
 * Note: The tab size is NOT synched with the tab size on the text area control!
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
public class DisposalDocument extends PlainDocument {
    /**
     * The size of the tab indent to use
     */
    private static final int DEFAULT_TAB_SIZE = 8;

    /**
     * The size of the tab indent to use
     */
    private static final int DEFAULT_MAX_COLUMNS = 255;

    /**
     * The size of the tab
     */
    private int tabSize;

    /**
     * The Maximum Columns
     */
    private int maxColumns;

    /**
     * Construct a DisposalDocument with the default values
     */
    public DisposalDocument() {
        this(DEFAULT_MAX_COLUMNS, DEFAULT_TAB_SIZE);
    }

    /**
     * Construct a DisposalDocument with the default values
     */
    public DisposalDocument(int maxColumns) {
        this(maxColumns, DEFAULT_TAB_SIZE);
    }

    /**
     * Construct a DisposalDocument with the specified values
     */
    public DisposalDocument(int maxColumns, int tabSize) {
        if (maxColumns < 0) {
            throw new IllegalArgumentException("maxColumns: " + maxColumns);
        }
        if (tabSize < 0) {
            throw new IllegalArgumentException("tabSize: " + tabSize);
        }
        this.maxColumns = maxColumns;
        this.tabSize = tabSize;
    }

    /**
     * Get the tab size
     */
    public int getTabSize() {
        return tabSize;
    }

    /**
     * Set the tab size
     */
    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    /**
     * Get the max columns
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * Set the max columns
     */
    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
    }

    /**
     * Get the number of lines in the document.
     */
    public int getLineCount() {
        Element root = getDefaultRootElement();
        int count = root.getElementCount();
        Element lastRow = root.getElement(count - 1);
        // Do not include the last line if it is empty '\n'
        if (lastRow.getEndOffset() - lastRow.getStartOffset() == 1) {
            return count - 1;
        } else {
            return count;
        }
    }

    /**
     * Get the line at the specified index
     */
    public String getLine(int lineIndex) {
        // Note this will eroneously return the line at 'count' if it is empty!
        // This is not worth checking as safe.
        try {
            Element line = getDefaultRootElement().getElement(lineIndex);
            int offset = line.getStartOffset();
            return getText(offset, line.getEndOffset() - offset - 1); // -1 as
            // discard
            // \n
        } catch (BadLocationException ise) {
            throw new IllegalStateException("Bad location in document element: " + ise.getMessage());
        }
    }

    /**
     * Insert the line at the specified index
     */
    public void setLine(int lineIndex, String lineText) {
        try {
            if (lineText != null) {
                Element root = getDefaultRootElement();
                int count = root.getElementCount();
                if (lineIndex < count) {
                    // Already contains that line so remove old and insert
                    // new
                    Element line = root.getElement(lineIndex);
                    int startOffset = line.getStartOffset();
                    int length = line.getEndOffset() - startOffset - 1;
                    if (length != 0) {
                        remove(startOffset, length);
                    }
                    insertString(startOffset, lineText, null);
                } else {
                    // No line at that index insert enough lines for that
                    // line
                    Element line = root.getElement(count - 1);
                    insertString(line.getEndOffset() - 1, createNewLines(lineIndex - count + 1) + lineText, null);
                }
            }
        } catch (BadLocationException ise) {
            throw new IllegalStateException("Bad location in document element: " + ise.getMessage());
        }
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
        String con = processInsert(offset, str);
        if (con != null) {
            super.insertString(offset, con, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private String processInsert(int offset, String str) {
        int column = getColumnIndex(offset);

        StringBuffer buffer = new StringBuffer(str.length());

        for (int i = 0, l = str.length(); i < l; i++) {
            // Convert the tabs
            char c = str.charAt(i);
            switch (c) {
            case '\n':
                buffer.append(c);
                column = 0;
                break;
            case '\t':
                int hardTabSize = tabSize - (column % tabSize);
                buffer.append(createHardTab(hardTabSize));
                column += hardTabSize;
                break;
            default:
                buffer.append(c);
                column += 1;
                break;
            }
            // check the column is not to long
            if (column > maxColumns) {
                return null;
            }
        }

        // check trailing characters does not make it to long
        int trailing = getRowLength(getRowIndex(offset)) - getColumnIndex(offset);
        if ((column + trailing) > maxColumns) {
            return null;
        }

        return buffer.toString();
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
        if (processRemove(offset, length)) {
            super.remove(offset, length);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean processRemove(int offset, int length) throws BadLocationException {
        int startRow = getRowIndex(offset);
        int endRow = getRowIndex(offset + length);
        // Check removing \n does not make new line longer than maxColumns
        if (startRow != endRow) {
            int startColumn = getColumnIndex(offset);
            int endColumn = getColumnIndex(offset + length);
            int newlength = startColumn + getRowLength(endRow) - endColumn;
            if (newlength > maxColumns) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the column index of the specified offset
     */
    public int getColumnIndex(int offset) {
        Element root = getDefaultRootElement();
        Element row = root.getElement(root.getElementIndex(offset));
        return offset - row.getStartOffset();
    }

    /**
     * Get the row index
     */
    public int getRowIndex(int offset) {
        return getDefaultRootElement().getElementIndex(offset);
    }

    /**
     * Get the length of the row
     */
    public int getRowLength(int rowIndex) {
        Element row = getDefaultRootElement().getElement(rowIndex);
        return row.getEndOffset() - row.getStartOffset() - 1; // -1 to not
        // include \n
    }

    /**
     * Utilility method to create the hard tab: Note returns char[] no need to
     * create String object as added to string buffer then discarded
     */
    private static char[] createHardTab(int size) {
        char[] hardTab = new char[size];
        for (int i = 0; i < hardTab.length; i++) {
            hardTab[i] = ' ';
        }
        return hardTab;
    }

    /**
     * Utilility method to create the new lines
     */
    private static String createNewLines(int size) {
        char[] newLines = new char[size];
        for (int i = 0; i < newLines.length; i++) {
            newLines[i] = '\n';
        }
        return new String(newLines);
    }
}
