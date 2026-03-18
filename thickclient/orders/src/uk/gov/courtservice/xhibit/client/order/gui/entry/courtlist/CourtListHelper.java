package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

/**
 * <p>
 * Title: CourtListHelper. A helper class that evaluates combinations of xml
 * refernece values. This class is utilised by class MultipleCourtListPanel that
 * determines which courts list(s) should be displayed
 * </p>
 * <p>
 * Description: This class checks combinations of xml values that are either set
 * as true or false, if the xml values are set to false then the related courts
 * radiobutton should not be displayed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Des Johnston
 * @version 1.0
 */

public class CourtListHelper {
    /**
     * Method that checks xml values set in the dataentrytemplate.
     * 
     * @param crown
     *            True or false xml value
     * @param magistrate
     *            True or false xml value
     * @param youth
     *            True or false xml value
     * @return int Return an integer value
     */
    public int checkAttributes(String crown, String magistrate, String youth) {
        // Check for all courts
        if (crown.equals("true") && (magistrate.equals("true") && (youth.equals("true")))) {
            return 1;
        }
        // Check for crown and magistrate court

        if (crown.equals("true") && (magistrate.equals("true") && (youth.equals("false")))) {
            return 2;
        }
        // Check for magistrate court
        if (crown.equals("false") && (magistrate.equals("true") && (youth.equals("false")))) {
            return 3;
        }
        // Check for crown court
        if (crown.equals("true") && (magistrate.equals("false") && (youth.equals("false")))) {
            return 4;
        }
        
        if(crown.equals("false") && magistrate.equals("false") && youth.equals("true"))
        	return 5;
        return 0;
    }
}
