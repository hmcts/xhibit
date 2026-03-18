package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Common superclass for public display data classes Note: This extends public
 * display value, but only uses the court site information from this value
 * object. The row processor calls a separate method on the abstractRowProcessor
 * to acheive this.
 * 
 * @author pznwc5
 */
public class AllCourtStatusValue extends PublicDisplayValue {

    private static final long serialVersionUID = 1L;

    /**
     * Reporting restricted
     */
    private boolean reportingRestricted;

    /**
     * Defendant names
     */
    private Collection<DefendantName> defendantNames = new ArrayList<DefendantName>();

    /**
     * Case title
     */
    private String caseTitle;

    /**
     * Case number
     */
    private String caseNumber;

    /**
     * Name of the court room
     */
    private String courtRoomName;


    /**
     * Sets the case title
     * 
     * @param val
     *            the Case title
     */
    public void setCaseTitle(String val) {
        caseTitle = val;
    }

    /**
     * Sets the case number
     * 
     * @param val
     *            the Case number
     */
    public void setCaseNumber(String val) {
        caseNumber = val;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @param val
     *            the Court room in which the case is heard
     */
    public void setCourtRoomName(String val) {
        courtRoomName = val;
    }

    /**
     * Gets the case title
     * 
     * @return val the Case title
     */
    public String getCaseTitle() {
        return caseTitle;
    }

    /**
     * Gets the case number
     * 
     * @return the case number
     */
    public String getCaseNumber() {
        return caseNumber == null ? "" : caseNumber;
    }

    /**
     * Gets the court room in which the case is heard
     * 
     * @return Court room in which the case is heard
     */
    public String getCourtRoomName() {
        return courtRoomName;
    }

    /**
     * Gets the defendant names
     * 
     * @return Defendant names
     */
    public Collection<DefendantName> getDefendantNames() {
        return defendantNames;
    }

    /**
     * Adds the defendant name
     * 
     * @param val
     *            the DefendantName name
     */
    public void addDefendantName(DefendantName val) {
        defendantNames.add(val);
    }

    public boolean hasInformationForDisplay() {
        return caseNumber != null;
    }

    public boolean hasDefendants() {
        return !defendantNames.isEmpty();
    }

    public boolean hasCaseTitle() {
        return caseTitle != null ? caseTitle.trim().length() > 0 : false;
    }

    /**
     * Sets reporting restriction
     * 
     * @param val
     *            Reporting restriction
     */
    public void setReportingRestricted(boolean val) {
        reportingRestricted = val;
    }

    /**
     * Returns reporting restriction
     * 
     * @return Reporting restriction
     */
    public boolean isReportingRestricted() {
        return reportingRestricted;
    }
    
    @Override
    public int compareTo(PublicDisplayValue other) {
		if ( !this.getCourtSiteCode().equals(other.getCourtSiteCode()) ) {
			// Different court sites, alphabetic court site code comparison
			return this.getCourtSiteCode().compareTo(other.getCourtSiteCode());
		}
		if ( this.getCrestCourtRoomNo() != other.getCrestCourtRoomNo() ) {
			// Different court room no, numeric court room number comparison
			return this.getCrestCourtRoomNo() - other.getCrestCourtRoomNo();
		}
		if ( !this.getEventTime().equals(other.getEventTime()) ) {
			// Different event time, sort by timestamp (descending so most recent first)
			return other.getEventTime().compareTo(this.getEventTime());
		}
		return 0;
	}

}
