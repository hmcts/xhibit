package uk.gov.courtservice.xhibit.business.vos.services.publicnotice;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Description of the Class
 * 
 * @author tzxbys
 * @created 24 February 2003
 */
public class DefinitivePublicNoticeStatusValue extends CSAbstractValue {
	private static final long serialVersionUID = 2715038451448616559L;
    Integer definitivePublicNoticeId;

    boolean isActive;

    /**
     * Constructor for the DefinitivePublicNoticeStatusValue object
     * 
     * @param definitivePublicNoticeId
     *            Description of the Parameter
     * @param isActive
     *            Description of the Parameter
     */
    public DefinitivePublicNoticeStatusValue(Integer definitivePublicNoticeId, boolean isActive) {
        this.definitivePublicNoticeId = definitivePublicNoticeId;
        this.isActive = isActive;
    }

    /**
     * Gets the definitivePublicNoticeId attribute of the
     * DefinitivePublicNoticeStatusValue object
     * 
     * @return The definitivePublicNoticeId value
     */
    public Integer getDefinitivePublicNoticeId() {
        return this.definitivePublicNoticeId;
    }

    /**
     * Gets the isActive attribute of the DefinitivePublicNoticeStatusValue
     * object
     * 
     * @return The isActive value
     */
    public boolean getIsActive() {
        return this.isActive;
    }

}
