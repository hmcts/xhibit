package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * <p>
 * Title: ComboBoxUtility
 * </p>
 * <p>
 * Description: Utility class to provide searching and auto-completion of text
 * for the CustomComboBox component. The searching facility is only supported in
 * the forward direction, i.e as the user enters valid text.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David & Des
 * @version 1.0
 */
public class ComboBoxUtility {
    // store of previous result.
    private int previousResultCount;

    /**
     * default constructor.
     */
    public ComboBoxUtility() {
    }

    /**
     * Return the stored result count from the previous search.
     * 
     * @return int count of search hits.
     */
    public int getPreviousResultCount() {
        return this.previousResultCount;
    }

    /**
     * Restores the list of court names to the original list.
     * 
     * @param box
     *            JComboBox
     * @param allCourtNames
     *            String array containing all court names.
     */
    public void restoreNames(JComboBox box, String[] allCourtNames) {
        box.removeAllItems();

        for (int i = 0; i < allCourtNames.length; i++) {
            box.addItem(allCourtNames[i]);
        }

        box.hidePopup();
        box.setMaximumRowCount(10);
    }

    /**
     * Perform search and return a vector of all search results.
     * 
     * @param name
     *            Search string.
     * @param allCourtNames
     *            String array containing all court names.
     * @param enabled
     *            true if search is enabled.
     * @return Vector of search results.
     */
    public Vector performSearch(String name, String[] allCourtNames, boolean enabled) {
        if ((name.length() != 0) && (enabled)) {
            Vector courts = new Vector();
            for (int k = 0; k < allCourtNames.length; k++) {

                if (checkMatch(allCourtNames[k], name)) {
                    courts.add(allCourtNames[k]);
                }

            }
            this.previousResultCount = courts.size();
            return courts;
        } else {
            this.previousResultCount = 0;
            return null;
        }

    }

    /**
     * Limits list of court names available for selection.
     * 
     * @param results
     *            Vector of search results.
     * @param name
     *            Search string.
     * @param box
     *            JComboBox
     * @param text
     *            JTextField
     * @param pos
     *            Position of cursor within text.
     * @param limited
     *            true if user can only select from available list.
     */
    public void limitCourtList(Vector results, String name, JComboBox box, JTextField text, int pos, boolean limited) {
        if (results != null) {
            if (results.size() > 0) {
                doLimit(box, results);
            } else if ((results.size() == 0) && (!limited)) {
                doLimit(box, results);
            }
        }
    }

    /**
     * Remove all items in list of JComboBox, and add those which only exist
     * within the results vector.
     * 
     * @param box
     *            JComboBox
     * @param results
     *            Vector of results.
     */
    private void doLimit(JComboBox box, Vector results) {
        box.removeAllItems();
        Enumeration enumeration = results.elements();

        while (enumeration.hasMoreElements()) {
            box.addItem(enumeration.nextElement().toString());
        }
    }

    /**
     * Perform a case-insensitive match between two strings.
     * 
     * @param item
     *            Item string.
     * @param name
     *            Search string.
     * @return true if case-insensitive match returns true.
     */
    public boolean checkMatch(String item, String name) {
        try {
            if (name.equalsIgnoreCase(item.substring(0, name.length()))) {
                return true;
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Display result from search.
     * 
     * @param results
     * @param box
     * @param text
     * @param pos
     * @param search
     * @param allCourts
     * @param limited
     */
    public void alterDisplay(Vector results, JComboBox box, JTextField text, int pos, String search,
            String[] allCourts, boolean limited) {
        if (results != null) {
            if (results.size() == 1) {
                showMatch(results.firstElement().toString(), pos, box, text);
            } else if (results.size() > 1) {
                text.setText(search);
            } else if ((results.size() == 0) && (limited)) {
                restoreNames(box, allCourts);
                text.setText("");
                box.showPopup();
            } else if ((results.size() == 0) && (!limited)) {
                restoreNames(box, allCourts);
                text.setText(search);
                box.hidePopup();
            }
        }
    }

    private void showMatch(String match, int pos, JComboBox box, JTextField text) {
        text.setText(match);
        text.setCaretPosition(match.length());
        text.moveCaretPosition(pos);
        box.hidePopup();
    }

}
