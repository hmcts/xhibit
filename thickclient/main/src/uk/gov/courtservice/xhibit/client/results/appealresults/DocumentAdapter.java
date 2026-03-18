package uk.gov.courtservice.xhibit.client.results.appealresults;

/**
 * <p>Title: DocumentAdapter</p>
 * <p>Description: A class missing from swing... why has every multiple method
 * listener except this one got and adapter????</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Paul Morris
 * @version 1.0
 */

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentAdapter implements DocumentListener {
    /**
     * constructor
     */
    public DocumentAdapter() {
    }

    /**
     * the overridden insert update menthod
     * 
     * @param de
     *            the event passed
     */
    public void insertUpdate(DocumentEvent de) {
    }

    /**
     * the overridden remove update menthod
     * 
     * @param de
     *            the event passed
     */
    public void removeUpdate(DocumentEvent de) {
    }

    /**
     * the overridden changed update menthod
     * 
     * @param de
     *            the event passed
     */
    public void changedUpdate(DocumentEvent de) {
    }
}