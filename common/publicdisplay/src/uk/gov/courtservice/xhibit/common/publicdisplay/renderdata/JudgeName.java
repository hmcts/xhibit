package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata;

import java.io.Serializable;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author pznwc5 The class represents a judge name
 */
public class JudgeName implements Serializable {
	
	static final long serialVersionUID = 8035508514989899210L;
	
    /**
     * Column name for judge first name
     * 
     * private static final String JUDGE_FIRST_NAME = "JUDGE_FIRST_NAME";
     * 
     * /** Column name for judge middle name
     * 
     * private static final String JUDGE_MIDDLE_NAME = "JUDGE_MIDDLE_NAME";
     */

    /**
     * Column name for judge surname
     */
    private static final String JUDGE_SURNAME = "JUDGE_SURNAME";

    /**
     * Column name for full list title column 1. The one to be used when
     * available for listing the case.
     */
    private static final String FULL_LIST_TITLE1 = "FULL_LIST_TITLE1";

    /**
     * Judge name
     */
    private String name = "";

    /**
     * Judge name
     * 
     * @param row
     *            Current row
     */
    public JudgeName(Row row) {
        name = row.getString(FULL_LIST_TITLE1);
        if (name == null || name.length() == 0) {
            name = row.getString(JUDGE_SURNAME);
            if (name == null)
                name = "";
        }
    }
    
    /**
     * Sets up a JudgeName object from a String value
     * @param judgeName
     */
    public JudgeName(String judgeName) {
        name = judgeName;
        if (judgeName == null) {
        	name = "";   
        }
    }

    /**
     * Returns the name
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name
     */
    public String toString() {
        return getName();
    }
}