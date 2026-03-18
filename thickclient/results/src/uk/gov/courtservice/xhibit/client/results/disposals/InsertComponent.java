package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Color;
import java.awt.Component;

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
 */
public interface InsertComponent {
    /**
     * The maximum number of columns to display (Stops large max chars
     * disapering to right)
     */
    public int MAX_DISPLAY_LENGTH = 20;

    /**
     * The default max number of chars
     */
    public int DEFAULT_MAX_CHARS = 240;

    /**
     * The color used to render the text if an error occures
     */
    public Color ERROR_COLOR = new Color(244, 0, 0);

    /**
     * Get the AWT component
     */
    public Component getComponent();

    /**
     * Return true if an error has occured
     */
    public boolean hasError();

    /**
     * Set to true if an error occures (generally more lines have been inserted
     * than allowed)
     */
    public void setError(boolean error);

    /**
     * Set the max data length
     */
    public void setMaxChars(int maxChars);

    /**
     * Get the max number of characters
     */
    public int getMaxChars();

    /**
     * Get the number of data lines
     */
    public int getLineCount();

    /**
     * Disable the component
     */
    public void setEnabled(boolean enabled);

    /**
     * Return true if the componenet is enabled
     */
    public boolean isEnabled();

    /**
     * Get the data
     */
    public String getLine(int index);

    /**
     * Set the data
     */
    public void setLine(int index, String line);

    /**
     * Add the listener
     */
    public void addInsertComponentListener(InsertComponentListener listener);

    /**
     * Remove the listener
     */
    public void removeInsertComponentListener(InsertComponentListener listener);
    
    /**
     * Return true if the component is mandatory
     */
    public boolean isMandatory();
    
    /**
     * Set the mandatoryness of the component
     */
    public void setMandatory(boolean mandatory);
    
    /**
     * Return true if the component is complete (not mandatory components are
     * always complete)
     */
    public boolean isComplete();

}
