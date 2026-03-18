package uk.gov.courtservice.xhibit.client.caseprogress;

import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2 - Case Progress Table Model
 * </p>
 * <p>
 * Description: Provides functionality for all Case Progress table models to
 * have. The specific table, will have its table model extend this class, so to
 * use the truncating function as well as being able to switch the details of
 * the content.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

abstract public class CaseProgressTableModel extends XHIBITDefaultTableModel {
    /** The index of the content string from which truncating starts. */
    protected int truncateIndex;

    /** The flag that determines whether this model should truncate its data. */
    protected boolean truncateMode;

    /**
     * The flag that determines whether the model should display its data in
     * more detail.
     */
    protected boolean detailsOn;

    private final static String emptyStr = "";

    /**
     * Default constructor, intialising flags to default values: No truncate
     * mode, and no detailed content.
     */
    public CaseProgressTableModel(Object[] data) {
        super();
        this.truncateIndex = 50;
        this.truncateMode = true;
        this.detailsOn = false;
        setData(data);
        this.initColumnNames();
    }

    /**
     * Will truncate the given content if the truncateIndex is less than the
     * length of the content.
     * 
     * @param content
     *            the content to be truncated.
     * 
     * @return the newly formed truncated content.
     */
    protected String truncate(String content) {
        if (content != null) {
            if (content.length() > this.truncateIndex) {
                StringBuffer strBuffer = new StringBuffer(content);
                strBuffer.replace(this.truncateIndex, content.length(), "...");
                return strBuffer.toString();
            }
            return content;
        } else {
            return emptyStr;
        }
    }

    /**
     * Sets the truncate mode
     * 
     * @param truncateMode
     *            the mode to be set: - setting to <code>true></code>will
     *            truncate.
     * @param truncateIndex
     *            the index of the string to start truncating.
     */
    public void setTruncateMode(boolean truncateMode, int truncateIndex) {
        this.truncateMode = truncateMode;
        this.truncateIndex = truncateIndex;
    }

    /**
     * @return the truncate mode of this model.
     */
    public boolean getTruncateMode() {
        return this.truncateMode;
    }

    /**
     * Will set the flag for the model to display its data in more detail.
     * 
     * @param detailsOn
     *            the details flag - true will display content in more detail.
     */
    public void setDetailsOn(boolean detailsOn) {
        if (detailsOn) {
            setTruncateMode(false, 0);
        } else {
            setTruncateMode(true, 50);
        }
        this.detailsOn = detailsOn;
    }

    /**
     * @return the details flag predicate.
     */
    public boolean getDetailsOn() {
        return this.detailsOn;
    }

    /**
     * Method placeholder for subclass to create its columnNames. Note that all
     * subclasses should store their column names in the columnNames array of
     * this class.
     */
    abstract protected void initColumnNames();
}