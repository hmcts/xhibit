package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtList;
import uk.gov.courtservice.xhibit.client.order.gui.helpers.KeyIdentifier;

/**
 * <p>
 * Title: ComboBoxAgent for enhanced use of JComboBox.
 * </p>
 * <p>
 * Description: Class to provide useful searching functionality which is invoked
 * when KeyEvents are trigerred by the user of the CustomComboBox.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan & Desmon Johnston
 * @version 1.0
 */
public class ComboBoxAgent extends KeyAdapter {
    // ComboBox containing list of court names.
    private JComboBox boxCb;

    // textText field component belonging to boxCb.
    private JTextField textText;

    // courtlist containing a list of court names...to be replaced with real
    // data!!!!!!!
    private CourtList list;

    private String[] contents;

    // Utility class with useful searching and display methods.
    private ComboBoxUtility util;

    // true if user is restricted to only those court names from list.
    private boolean limited;

    // count of backspace key presses, used to disable/enable search.
    private int backSpaceCount;

    // count of valid key presses, used to disable/enable search.
    private int validKeyCount;

    /**
     * Construct a ComboBoxAgent from an existing combobox, specifying the
     * nature of the searching utility, true if the user is restricted to only
     * those in list.
     * 
     * @param comboBox
     *            a JComboBox.
     * @param limited
     *            true if the user is restricted to only those in list.
     */
    public ComboBoxAgent(JComboBox comboBox, boolean limited) {
        setUp(comboBox, limited);
    }

    /**
     * Construct a ComboBoxAgent from an existing combobox, restricting the
     * court selection to only those in list.
     * 
     * @param box
     *            a JComboBox.
     */
    public ComboBoxAgent(JComboBox box) {
        this(box, true);
    }

    public void setContents(String[] str) {
        this.contents = str;
    }

    public String[] getContents() {
        return this.contents;
    }

    /**
     * Setup all component, adding the necessary listeners.
     * 
     * @param comboBox
     *            a JComboBox.
     * @param limited
     *            true if the user is restricted to only those in list.
     */
    private void setUp(JComboBox comboBox, boolean limited) {
        this.boxCb = comboBox;
        this.boxCb.setMaximumRowCount(10);
        this.limited = limited;
        this.textText = (JTextField) comboBox.getEditor().getEditorComponent();
        this.textText.addKeyListener(this);
        this.list = ((CustomComboBox) boxCb).getCourtNames();
        this.util = new ComboBoxUtility();
        this.backSpaceCount = 0;
    }

    /**
     * Determines whether the search facility should be enabled or not.
     * 
     * @return boolean true or false.
     */
    public boolean isSearchEnabled() {
        if ((validKeyCount > 1) && (!limited) && (util.getPreviousResultCount() == 0)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Uses user input as a search string, performs a search if enabled and
     * limits the drop down list to those matching the search criteria. If a
     * search returns no match, all court names are restored to the original
     * list.
     * 
     * @param e
     *            a key event.
     */
    public void keyReleased(KeyEvent e) {
        int pos = textText.getCaretPosition();
        String search = textText.getText();

        if (!KeyIdentifier.isValidKey(e)) {
            validKeyCount = 0;
            processControlKey(e, search);
        } else {
            validKeyCount++;
            backSpaceCount = 0;
            Vector results = util.performSearch(search, getContents(), isSearchEnabled());
            util.limitCourtList(results, search, boxCb, textText, pos, limited);
            // util.alterDisplay(results, boxCb, textText, pos, search,
            // getContents(), limited);
            for (int k = 0; k < boxCb.getItemCount(); k++) {
                String item = boxCb.getItemAt(k).toString();
                if (item.toUpperCase().startsWith(search.toUpperCase())) {
                    textText.setText(item);
                    textText.setCaretPosition(item.length());
                    textText.moveCaretPosition(pos);
                    break;
                }
            }
        }
    }

    /**
     * If a control key is pressed, identify if backspace or return and perform
     * various actions to help useablility e.g. hide or show popup.
     * 
     * @param e
     *            a key event.
     * @param search
     *            search string.
     */
    public void processControlKey(KeyEvent e, String search) {
        if (KeyIdentifier.isKeyBackspace(e)) {
            if (limited) {
                util.restoreNames(boxCb, getContents());
                textText.setText("");
                boxCb.showPopup();
            } else {
                if (backSpaceCount == 0) {
                    util.restoreNames(boxCb, getContents());
                    textText.setText(search);
                    boxCb.showPopup();
                } else {
                    textText.setText(search);
                    boxCb.showPopup();
                }
            }
            backSpaceCount++;
        } else if (KeyIdentifier.isKeyReturn(e)) {
            boxCb.hidePopup();
        } else if (KeyIdentifier.isKeyLeftArrow(e)) // DJ SCR 53113
        {
            boxCb.hidePopup(); // DJ SCR 53113
        } else if (KeyIdentifier.isKeyRighttArrow(e)) // DJ SCR 53113
        {
            boxCb.hidePopup(); // DJ SCR 53113
        } else if (KeyIdentifier.isKeyEscape(e)) {
            boxCb.hidePopup(); // DJ SCR 53113
        } else {
            boxCb.showPopup();
            return;
        }
    }

}
