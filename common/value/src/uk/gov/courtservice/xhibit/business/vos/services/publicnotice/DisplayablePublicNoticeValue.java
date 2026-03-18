package uk.gov.courtservice.xhibit.business.vos.services.publicnotice;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * 
 * Title: DisplayablePublicNoticeValue
 * </p>
 * <p>
 * 
 * Description: This VO is to contain a Public Notice that is displayable for a
 * particular court. Note : there is a set of definitive public notices but
 * these are not . These are not the public notice that actaully gets displayed
 * on screen it is the equivalent displayable public notice for this court. eg
 * No smoking --- maps to an actual displayed message for court in Manchester
 * --> No Smoking in the Court Room .
 * </p>
 * <p>
 * 
 * Company: EDS
 * </p>
 * 
 * @author Pat Fox
 * @created 17 February 2003
 * @version 1.0
 */

public class DisplayablePublicNoticeValue extends CSAbstractValue implements Comparable {
	private static final long serialVersionUID = 4470489343477594601L;
    private String desc;

    private boolean isActive;

    private int priority;

    private Integer definitivePublicNoticeID;

    private boolean dirty;

    /**
     * Constructor for the DisplayablePublicNoticeValue object
     */
    public DisplayablePublicNoticeValue() {
    }

    /**
     * Constructor for the DisplayablePublicNoticeValue object
     * 
     * @param configuredPNID
     *            Description of the Parameter
     * @param desc
     *            Description of the Parameter
     * @param isActive
     *            Description of the Parameter
     * @param versionNumber
     *            Description of the Parameter
     * @param definitivePublicNoticeID
     *            Description of the Parameter
     */
    public DisplayablePublicNoticeValue(Integer configuredPNID, String desc, boolean isActive, Integer versionNumber,
            Integer definitivePublicNoticeID) {
        super(configuredPNID, versionNumber);
        this.desc = desc;
        this.isActive = isActive;
        this.definitivePublicNoticeID = definitivePublicNoticeID;
        this.dirty = false;
    }

    /**
     * Constructor for the DisplayablePublicNoticeValue object
     * 
     * @param configuredPNID
     *            Description of the Parameter
     * @param desc
     *            Description of the Parameter
     * @param isActive
     *            Description of the Parameter
     * @param versionNumber
     *            Description of the Parameter
     * @param definitivePublicNoticeID
     *            Description of the Parameter
     * @param priority
     *            Description of the Parameter
     */
    public DisplayablePublicNoticeValue(Integer configuredPNID, String desc, boolean isActive, Integer versionNumber,
            Integer definitivePublicNoticeID, int priority) {
        this(configuredPNID, desc, isActive, versionNumber, definitivePublicNoticeID);
        this.priority = priority;
    }

    /**
     * Gets the desc attribute of the DisplayablePublicNoticeValue object
     * 
     * @return The desc value
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the isActive attribute of the DisplayablePublicNoticeValue object
     * 
     * @return The isActive value
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Gets the priority attribute of the DisplayablePublicNoticeValue object
     * 
     * @return The priority value
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the desc attribute of the DisplayablePublicNoticeValue object
     * 
     * @param desc
     *            The new desc value
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Sets the isActive attribute of the DisplayablePublicNoticeValue object
     * 
     * @param isActive
     *            The new isActive value
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Sets the priority attribute of the DisplayablePublicNoticeValue object
     * 
     * @param priority
     *            The new priority value
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Sets the definitivePublicNotice attribute of the
     * DisplayablePublicNoticeValue object
     * 
     * @param definitivePublicNotice
     *            The new definitivePublicNotice value
     */
    public void setDefinitivePublicNotice(Integer definitivePublicNotice) {
        this.definitivePublicNoticeID = definitivePublicNotice;
    }

    /**
     * Gets the definitivePublicNotice attribute of the
     * DisplayablePublicNoticeValue object
     * 
     * @return The definitivePublicNotice value
     */
    public Integer getDefinitivePublicNotice() {
        return this.definitivePublicNoticeID;
    }

    /**
     * Gets the dirty attribute of the DisplayablePublicNoticeValue object
     * 
     * @return The dirty value
     */
    public boolean getDirty() {
        return dirty;
    }

    /**
     * Sets the dirty attribute of the DisplayablePublicNoticeValue object
     * 
     * @param dirty
     *            The new dirty value
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Used for the sort of the array.
     * 
     * @param obj
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    public int compareTo(Object obj) {

        DisplayablePublicNoticeValue specifiedObj = (DisplayablePublicNoticeValue) obj;

        if (this.getPriority() < specifiedObj.getPriority()) {
            return -1;
        }
        if (this.getPriority() == specifiedObj.getPriority()) {
            return 0;
        }

        return 1;
    }
}
