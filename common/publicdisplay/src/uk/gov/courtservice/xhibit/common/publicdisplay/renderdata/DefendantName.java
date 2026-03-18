package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

import java.io.Serializable;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author pznwc5 The class represents a defendant name
 */
public class DefendantName implements Serializable, Comparable<DefendantName> {

    private static final long serialVersionUID = 1L;

    /**
     * Column name for defendant first name
     */
    private static final String DEFENDANT_FIRST_NAME = "DEFENDANT_FIRST_NAME";

    /**
     * Column name for defendant middle name
     */
    private static final String DEFENDANT_MIDDLE_NAME = "DEFENDANT_MIDDLE_NAME";

    /**
     * Column name for defendant surname
     */
    private static final String DEFENDANT_SURNAME = "DEFENDANT_SURNAME";

    /**
     * Column name indicating if the defendant should be hidden from 
     * public view.
     */
    private static final String HIDE_IN_PUBLIC_DISPLAY = "HIDE_IN_PUBLIC_DISPLAY";
    
    private static final String DEFENDANT_ON_CASE_ID = "DEFENDANT_ON_CASE_ID";
    
    /**
     * Defendant name
     */
    private String firstName;

    private String middleName;

    private String surname;
    
    private boolean hideInPublicDisplay;
    
    private int defendant_on_case_id;

    /**
     * Creates a defendant. The constructor is not visible outside the package
     * 
     * @param row
     */
    public DefendantName(Row row) {
        firstName = row.getString(DEFENDANT_FIRST_NAME);
        middleName = row.getString(DEFENDANT_MIDDLE_NAME);
        surname = row.getString(DEFENDANT_SURNAME);
        String hips = row.getString(HIDE_IN_PUBLIC_DISPLAY);
        hideInPublicDisplay = (hips != null && hips.equals("Y"));
        defendant_on_case_id = row.getInt(DEFENDANT_ON_CASE_ID);
    }
    
    /**
     * Creates a defendant from String values
     * @param firstName
     * @param middleName
     * @param surname
     * @param hide
     */
    public DefendantName(String firstName, String middleName, String surname, boolean hide) {
        this.firstName = (null == firstName) ? "" : firstName;
        this.middleName = (null == middleName) ? "" : middleName;
        this.surname = (null == surname) ? "" : surname;
        hideInPublicDisplay = hide;
        defendant_on_case_id = -1;
    }

    /**
     * Returns the name
     * 
     * @return
     */
    public String getName() {
        if (firstName != null || middleName != null || surname != null) {
            return NameHelper.buildName(firstName, middleName, surname);
        } else {
            return "";
        }
    }

    /**
     * Returns the name
     */
    public String toString() {
        return getName();
    }

    public String getNameWithSurnameFirst() {
        if (firstName != null || middleName != null || surname != null) {
            return NameHelper.buildNameWithSurnameFirst(firstName, middleName, surname);
        } else {
            return "";
        }

    }

    public boolean hasValue() {
        return firstName != null || middleName != null || surname != null;
    }

    public boolean isHideInPublicDisplay() {
        return hideInPublicDisplay;
    }
    
    public int getDefendantOnCaseId(){
    	return defendant_on_case_id;
    }
    
    public int compareTo(DefendantName other) {
		return this.getNameWithSurnameFirst().compareTo(other.getNameWithSurnameFirst());
	}
	
	@Override
    public boolean equals(Object o) {
    	if (o instanceof DefendantName) {
    		if ( this.getNameWithSurnameFirst().equals(((DefendantName) o).getNameWithSurnameFirst()) ) {
    			return true;
    		}
    	}
    	return false; 
    }
    
    @Override
    public int hashCode() {
    	String hash = this.getNameWithSurnameFirst();
    	return hash.hashCode();
    }
}
