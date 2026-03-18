package datamigration1745.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: CourtVO </p>
 * <p>Description: A VO that describes a HO Police Force code.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 *
 * @author GJS
 * @version 1.0
 */
public class HOPoliceForceVO extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer courtId;
    private String refCode;

    /**
     * Empty constructor
     */
    public HOPoliceForceVO() { }

    public HOPoliceForceVO(Integer courtId, String refCode) {
        setCourtId(courtId);
        setRefCode(refCode);
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }


    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Court[id=");
        builder.append(getCourtId());
        builder.append(",Code=");
        builder.append(refCode);
        builder.append("]");
        return builder.toString();
    }
}
