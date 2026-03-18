package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: DataComponent
 * </p>
 * <p>
 * Description: Used to render data components
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DataComponent.java,v 1.21 2006/06/05 12:32:15 bzjrnl Exp $
 */
public interface DataComponent {
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
     * Name of temp group1
     */
    public String SINGLE_GROUP1_NAME = "Single Group 1";

    /**
     * The color used to render the delete strike through
     */
    public Color DELETE_COLOR = new Color(64, 64, 64);

    /**
     * The color used to render the background of mandatory components strike
     * through
     */
    public Color MANDATORY_COLOR = new Color(255, 255, 200);

    /**
     * The color used to render data labels
     */
    public Color DATA_LABEL_COLOR = new Color(0, 0, 99);

    /**
     * Get the AWT component
     */
    public Component getComponent();

    /**
     * Return true if the component is fixed size
     */
    public boolean isFixedSize();

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
     * allways complete)
     */
    public boolean isComplete();

    /**
     * Return true if the component should be displayed
     */
    public boolean isScreenPrint();

    /**
     * Set to true to display the component
     */
    public void setScreenPrint(boolean screenPrint);

    /**
     * Set the max number of characters
     */
    public void setMaxChars(int maxChars);

    /**
     * Get the max number of characters
     */
    public int getMaxChars();

    /**
     * Set the data
     */
    public String getData();

    /**
     * Set the data
     */
    public void setData(String data);

    /**
     * Set name of group 1
     */
    public void setNameG1(String nameG1);

    /**
     * Set name of group 2
     */
    public void setNameG2(String nameG2);

    /**
     * Get name of group 1
     */
    public String getNameG1();

    /**
     * Get name of group 2
     */
    public String getNameG2();

    /**
     * Get name of group 1
     */
    public void setColorG1(XColor colorG1);

    /**
     * Get name of group 2
     */
    public void setColorG2(XColor colorG2);

    /**
     * Get name of group 1
     */
    public XColor getColorG1();

    /**
     * Get name of group 2
     */
    public XColor getColorG2();

    /**
     * Return true if the component has been deleted as part of group 1
     */
    public boolean isDeletedG1();

    /**
     * Return true if the component has been deleted as part of group 2
     */
    public boolean isDeletedG2();

    /**
     * Delete or undelete the component as part of groupG1
     */
    public void setDeletedG1(boolean deletedG1);

    /**
     * Delete or undelete the component as part of groupG2
     */
    public void setDeletedG2(boolean deletedG2);

    /**
     * Add the listener
     */
    public void addDataComponentListener(DataComponentListener listener);

    /**
     * Remove the listener
     */
    public void removeDataComponentListener(DataComponentListener listener);

    /**
     * Paint the component
     */
    public void paint(Graphics g);

    /**
     * Set the previous data component.
     */
    public void setPreviousDataComponent(DataComponent dataComponent);
}
