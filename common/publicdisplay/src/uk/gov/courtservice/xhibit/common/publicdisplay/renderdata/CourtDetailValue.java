package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 Value object for court detail
 */
public class CourtDetailValue extends AllCourtStatusValue {
	
	static final long serialVersionUID = 754560959152416644L;

    /**
     * Judge name
     */
    private JudgeName judgeName;

    /**
     * Hearing description
     */
    private String hearingDescription;

    /**
     * Public notices
     */
    private PublicNoticeValue[] publicNotices;

    /**
     * DOCUMENT ME!
     * 
     * @param judgeName
     *            DOCUMENT ME!
     */
    public void setJudgeName(JudgeName judgeName) {
        this.judgeName = judgeName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public JudgeName getJudgeName() {
        return judgeName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param hearingDescription
     *            DOCUMENT ME!
     */
    public void setHearingDescription(String hearingDescription) {
        this.hearingDescription = hearingDescription;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getHearingDescription() {
        return hearingDescription;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param publicNoticeValue
     *            DOCUMENT ME!
     */
    public void setPublicNotices(PublicNoticeValue[] publicNotices) {
        this.publicNotices = publicNotices;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public PublicNoticeValue[] getPublicNotices() {
        return publicNotices;
    }

    public boolean hasPublicNotices() {
        return publicNotices != null && publicNotices.length > 0;
    }
}
