package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class PublicNoticeDetailValue extends CSAbstractValue {
	private static final long serialVersionUID = 7152310564704314541L;
	private Integer publicNoticeId;

    private Integer configuredPublicNoticeId;

    private String isActive;

    private String publicNoticeDesc;

    /**
     * @roseuid 3DC96B3403AB
     */
    public PublicNoticeDetailValue() {

    }

    public Integer getConfiguredPublicNoticeId() {
        return configuredPublicNoticeId;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getPublicNoticeDesc() {
        return publicNoticeDesc;
    }

    public Integer getPublicNoticeId() {
        return publicNoticeId;
    }

    public void setPublicNoticeId(Integer publicNoticeId) {
        this.publicNoticeId = publicNoticeId;
    }

    public void setPublicNoticeDesc(String publicNoticeDesc) {
        this.publicNoticeDesc = publicNoticeDesc;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setConfiguredPublicNoticeId(Integer configuredPublicNoticeId) {
        this.configuredPublicNoticeId = configuredPublicNoticeId;
    }
}
