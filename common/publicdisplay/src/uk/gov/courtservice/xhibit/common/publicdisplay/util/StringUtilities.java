package uk.gov.courtservice.xhibit.common.publicdisplay.util;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: StringUtilities.java,v 1.5 2006/06/05 12:28:26 bzjrnl Exp $
 */

public class StringUtilities {
    private StringUtilities() {
    }

    /**
     * Converts a string into Sentence Case
     * 
     * @param value
     * @return
     */
    public static String toSentenceCase(String value) {
        if (value != null) {
            String displayName = value.toLowerCase().trim();
            boolean nextLetterUpperCase = true;

            char[] displayChars = displayName.toCharArray();
            char[] tempChars = displayName.toCharArray();

            for (int i = 0; i < displayName.length(); i++) {
                if (nextLetterUpperCase) {
                    tempChars[i] = Character.toUpperCase(displayChars[i]);
                } else {
                    tempChars[i] = displayChars[i];
                }
                nextLetterUpperCase = !Character.isLetter(displayChars[i]);
            }
            return new String(tempChars);
        }
        return null;
    }

}
