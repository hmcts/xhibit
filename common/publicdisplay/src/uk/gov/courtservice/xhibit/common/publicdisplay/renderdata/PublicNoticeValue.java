package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 Public notices value object
 */
public class PublicNoticeValue extends AbstractValue {
	
	static final long serialVersionUID = 8862116573773182259L;
	
    /**
     * Public notice description
     */
    private String publicNoticeDesc;

    /**
     * Active flag
     */
    private boolean active;

    /**
     * Priority
     */
    private int priority;

    /**
     * DOCUMENT ME!
     * 
     * @param publicNoticeDesc
     *            DOCUMENT ME!
     */
    public void setPublicNoticeDesc(String publicNoticeDesc) {
        this.publicNoticeDesc = publicNoticeDesc;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getPublicNoticeDesc() {
        return publicNoticeDesc;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param active
     *            DOCUMENT ME!
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return active;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param priority
     *            DOCUMENT ME!
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public int getPriority() {
        return priority;
    }

    public boolean hasInformationForDisplay() {
        return true;
    }
}
