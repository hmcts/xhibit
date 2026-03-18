/*
 * Created on 08-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

/**
 * This class provides the data for summary by name document
 * 
 * @author pznwc5
 */
public class SummaryByNameValue extends PublicDisplayValue {
	
	static final long serialVersionUID = -2334222330489778358L;
	
    /**
     * Reporting restricted
     */
    private boolean reportingRestricted;

    /**
     * Defendant name
     */
    private DefendantName defendantName;

    /**
     * Whether the case is floating.
     */
    private String floating;

    /**
     * Returns the is floating string
     * 
     * @return The string as it appears in the DB
     */
    public String getFloating() {
        return floating;
    }

    /**
     * Sets the is floating flag from the DB
     */
    public void setFloating(String isFloating) {
        floating = isFloating;
    }

    public boolean isFloating() {
        // TODO: make the query return a boolean - Neil Ellis
        return floating.equals(IS_FLOATING);
    }

    /**
     * Gets the name of the defendant
     * 
     * @param Name
     *            of the defendant
     */
    public void setDefendantName(DefendantName val) {
        defendantName = val;
    }

    /**
     * Gets the name of the defendant
     * 
     * @return Name of the defendant
     */
    public DefendantName getDefendantName() {
        return defendantName;
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
     * Gets reporting restriction
     * 
     */
    public boolean getReportingRestricted() {
        return reportingRestricted;
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
    	if (other instanceof SummaryByNameValue) {
    		return this.getDefendantName().compareTo((DefendantName)((SummaryByNameValue) other).getDefendantName());
    	}
		return 0;
	}
}