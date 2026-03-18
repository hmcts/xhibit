package uk.gov.courtservice.xhibit.client.results.disposals.insertcomponent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalDocument;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentListener;

/**
 * <p>
 * Title: InsertComponent
 * </p>
 * <p>
 * Description: Used to render line inserts
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * 
 */
public class DefaultInsertComponent extends JScrollPane implements InsertComponent, DocumentListener {

	private static final long serialVersionUID = 1L;

	/**
     * Used to cache the foregroundColor if an error ocures (also used for
     * maintaining error state) no real need for seperate flag
     */
    private Color foregroundCache = null;

    /**
     * Construct a new DefaultInsertComponent
     */
    public DefaultInsertComponent() {
        DisposalDocument document = new DisposalDocument();
        document.addDocumentListener(this);
        setViewportView(new JTextArea(document, "", 4, 0));
    }

    /**
     * InsertComponent implementation
     */
    public Component getComponent() {
        return this;
    }

    /**
     * InsertComponent implementation
     */
    public boolean hasError() {
        return foregroundCache != null;
    }

    /**
     * InsertComponent implementation
     */
    public void setError(boolean error) {
        if (hasError()) {
            if (!error) {
                JTextArea textArea = getTextArea();
                textArea.setForeground(foregroundCache);
                foregroundCache = null;
            }
        } else {
            if (error) {
                JTextArea textArea = getTextArea();
                foregroundCache = textArea.getForeground();
                textArea.setForeground(ERROR_COLOR);
            }
        }
    }

    /**
     * InsertComponent implementation
     */
    public void setMaxChars(int maxChars) {
        if (maxChars < 0) {
            getDisposalDocument().setMaxColumns(DEFAULT_MAX_CHARS);
            setColumns(DEFAULT_MAX_CHARS);
            setToolTipText(DEFAULT_MAX_CHARS);
        } else {
            getDisposalDocument().setMaxColumns(maxChars);
            setColumns(maxChars);
            setToolTipText(maxChars);
        }
    }

    /**
     * InsertComponent implementation
     */
    public int getMaxChars() {
        return getDisposalDocument().getMaxColumns();
    }

    /**
     * InsertComponent implementation
     */
    public int getLineCount() {
        return getDisposalDocument().getLineCount();
    }

    /**
     * InsertComponent implementation
     */
    public String getLine(int index) {
        return getDisposalDocument().getLine(index);
    }

    /**
     * InsertComponent implementation
     */
    public void setLine(int index, String data) {
        getDisposalDocument().setLine(index, data);
    }

    /**
     * InsertComponent implementation
     */
    public void setEnabled(boolean enabled) {
        getTextArea().setEnabled(enabled);
        fireInsertChanged();
    }

    /**
     * InsertComponent implementation
     */
    public boolean isEnabled() {
        return getTextArea().isEnabled();
    }

    /**
     * Set the number of columns
     */
    public void setColumns(int columns) {
        getTextArea().setColumns(columns < 0 || columns > MAX_DISPLAY_LENGTH ? MAX_DISPLAY_LENGTH : columns);
    }

    /**
     * Get the number of columns
     */
    public int getColumns() {
        return getTextArea().getColumns();
    }

    // Util
    private void setToolTipText(int maxChars) {
        getTextArea().setToolTipText(DisposalUtil.getToolTipText("insertComponentToolTip", new Integer(maxChars)));
    }

    private DisposalDocument getDisposalDocument() {
        return (DisposalDocument) getTextArea().getDocument();
    }

    protected JTextArea getTextArea() {
        return (JTextArea) getViewport().getView();
    }

    /**
     * Add the listener
     */
    public void addInsertComponentListener(InsertComponentListener listener) {
        listenerList.add(InsertComponentListener.class, listener);
    }

    /**
     * Remove the listener
     */
    public void removeInsertComponentListener(InsertComponentListener listener) {
        listenerList.remove(InsertComponentListener.class, listener);
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    protected void fireInsertChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            InsertComponentEvent event = null;
            if (listeners[i] == InsertComponentListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new InsertComponentEvent(this);
                }
                ((InsertComponentListener) listeners[i + 1]).insertChanged(event);
            }
        }
    }

    /**
     * Document Listener Implementation
     */
    public void insertUpdate(DocumentEvent e) {
        fireInsertChanged();
    }

    /**
     * Document Listener Implementation
     */
    public void removeUpdate(DocumentEvent e) {
        fireInsertChanged();
    }

    /**
     * Document Listener Implementation
     */
    public void changedUpdate(DocumentEvent e) {
        fireInsertChanged();
    }

	@Override
	public boolean isMandatory() {
		return false;
	}

	@Override
	public void setMandatory(boolean mandatory) {
		// Do nothing
	}

	@Override
	public boolean isComplete() {
		return true;
	}
	
	protected boolean isPopulated() {
		return !"".equals(getTextArea().getText().trim());
	}
}