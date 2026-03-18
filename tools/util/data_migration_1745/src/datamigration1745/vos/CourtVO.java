package datamigration1745.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: CourtVO </p>
 * <p>Description: A VO that describes a Court.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Electronic Data Systems</p>
 *
 * @author Simon Gilmore
 * @version 1.0
 */
public class CourtVO extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private String displayName;
    private String crestCourtId;
    private String status;

    /**
     * Empty constructor
     */
    public CourtVO() { }

    public CourtVO(Integer courtId, String displayName, String crestCourtId, String status) {
        setId(courtId);
        setDisplayName(displayName);
        setCrestCourtId(crestCourtId);
        setStatus(status);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCrestCourtId() {
        return crestCourtId;
    }

    public void setCrestCourtId(String crestCourtId) {
        this.crestCourtId = crestCourtId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Court[id=");
        builder.append(getId());
        builder.append(",displayName=");
        builder.append(displayName);
        builder.append(",crestCourtId=");
        builder.append(crestCourtId);
        builder.append(",status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }
}
