package uk.gov.courtservice.xhibit.business.vos.services.charge;

// java

import java.util.Calendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CrnValue
 * </p>
 * <p>
 * Description: Self contained, immutable value object, for creating and storing
 * a valid CRN. Constructer is private since instantiation may throw an
 * Exception
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: CrnValue.java,v 1.4 2009/01/07 10:26:41 hewittm Exp $
 */

public class CrnValue extends CSAbstractValue {

    public static final int LENGTH = 23;

    public static final int FFUU_LENGTH = 4;

    public static final int SS_LENGTH = 2;

    public static final long MAX_GENERATED_SEQ_NO = 99999999999L; // 11

    // digits

    public static final int SEQ_NO_LENGTH = 3;

    public static final int GENERATED_SEQ_NO_LENGTH = 11;

    public static final int ASN_LENGTH = 20;

    public static final String MASK = "##AAAA#############?###";

    /**
     * letter array used to resolve check character 1 = A, 2 = B, 3 = C, 4 = D,
     * 5 = E, 6 = F, 7 = G, 8 = H, 9 = J, 10 = K, 11 = L, 12 = M, 13 = N, 14 =
     * P, 15 = Q, 16 = R, 17 = T, 18 = U, 19 = V, 20 = W, 21 = X, 22 = Y, 0 = Z
     * I,S,O are missing
     */
    private static final String[] letters = { "Z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N",
            "P", "Q", "R", "T", "U", "V", "W", "X", "Y" };

    private static Logger log = CSServices.getLogger(CrnValue.class);

    /**
     * Immutable CRN string to hold the created CRN
     */
    private String crnStr;
    
    private static final long serialVersionUID = -4563874414242173159L;

    /**
     * Private contructor since instanstation may throw an exception
     * 
     * @param crnStr
     */
    private CrnValue(String crnStr) {
        this.crnStr = crnStr;
    }

    /**
     * Accessor for crnStr
     * 
     * @return CRN string
     */
    public String getCrnStr() {
        return crnStr;
    }

    public String getAsnStr() {
        return crnStr.substring(0, ASN_LENGTH);
    }

    /**
     * Use to create an instance of CRN using an end user supplied string
     * 
     * @param crnStr
     * @return CRN
     * @throws InstantiationException
     */
    public static CrnValue newInstance(String crnString) throws InstantiationException {
        String methodName = "newInstance(String crnString):";

        log.debug(methodName + " entered, crnString = " + crnString);

        if (CrnValue.isValid(crnString)) {
            log.debug(methodName + " exited.");
            return new CrnValue(crnString);
        } else {
            throw new InstantiationException("Could not create CRN, invalid CRN format passed");
        }
    }

    /**
     * Use to create an instance of CRN using an end user supplied string
     * 
     * @param crnStr
     * @return CRN
     * @throws InstantiationException
     */
    public static CrnValue newInstance(String ffuu, String ss, long generatedSequenceNo, String seqNo)
            throws InstantiationException {
        String methodName = "newInstance(String ffuu, String ss, long generatedSequenceNo, String seqNo):";
        log.debug(methodName + " entered, ffuu=" + ffuu + " ss=" + ss + " generatedSequenceNo=" + generatedSequenceNo
                + " seqNo=" + seqNo);

        // check arguments
        if (ffuu == null || ffuu.length() != FFUU_LENGTH // must be 4 digits
                || ss == null || ss.length() != SS_LENGTH // must be 2 digits
                || generatedSequenceNo < 0 || generatedSequenceNo > MAX_GENERATED_SEQ_NO // must
                                                                                            // be
                                                                                            // less
                // than 12
                // digits
                || seqNo == null || seqNo.length() != SEQ_NO_LENGTH // must
        // be 3
        // digits
        )
            throw new IllegalArgumentException();

        // extract last 2 digits of year
        String year = ("" + Calendar.getInstance().get(Calendar.YEAR)).substring(2);

        // pad generatedSequenceNo with 0's to get 11 digits
        StringBuffer genSeqNoBuf = new StringBuffer("" + generatedSequenceNo);
        while (genSeqNoBuf.length() < GENERATED_SEQ_NO_LENGTH) {
            genSeqNoBuf.insert(0, 0);
        }

        String crnString = year + ffuu + ss + genSeqNoBuf.toString();
        crnString += CrnValue.getCheckCharacter(crnString) + seqNo;

        if (CrnValue.isCorrectFormat(crnString)) {
            log.debug("crnString=" + crnString);
            log.debug(methodName + " exited.");
            return new CrnValue(crnString);
        } else {
            log.debug("Could not create CRN, invalid CRN format passed");
            throw new InstantiationException("Could not create CRN, invalid CRN format passed");
        }
    }

    /**
     * Checks that the CrnValue is valid YYFFUUSSNNNNNNNNNNND
     * 
     * @param crnStr
     * @return true or false
     */
    public static boolean isValid(String crnStr) {
        String methodName = "isValid(String crnStr):";
        log.debug(methodName + " entered, crnStr=" + crnStr);
        try {
            return isCorrectFormat(crnStr) && getCheckCharacter(crnStr).equals(crnStr.substring(19, 20));
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * Gets the check character. This method should only be used for system
     * generated numbers. Use isValid to validate check character of user
     * entered crn. <p/> Rules for the check character: <p/> The check character
     * D, generated from YYFFUUSSNNNNNNNNNNN, will be put through the standard
     * Modulus 23 validation. <p/> Modulus 23 Validation Rules are as follows:
     * a) Take the 2 digits from the Force Id at positions 3 and 4. b) Add the 2
     * digits from the System Id at positions 7 and 8 to the end of (a). c) Add
     * the 2 digits from the Year at positions 1 and 2 to the end of (b). d) Add
     * the 11 digits from the Numeric Id at positions 9 to 19 to the end of (c)
     * e) Take the check sum character at position 20. f) Divide the number in
     * (d) above by 23 and record the remainder. g) Convert the remainder (for
     * example, 4 = D) with the following conversion: 1 = A, 2 = B, 3 = C, 4 =
     * D, 5 = E, 6 = F, 7 = G, 8 = H, 9 = J, 10 = K, 11 = L, 12 = M, 13 = N, 14 =
     * P, 15 = Q, 16 = R, 17 = T, 18 = U, 19 = V, 20 = W, 21 = X, 22 = Y, 0 = Z
     * h) The converted remainder should be the same as the check sum character
     * D.
     */
    public static String getCheckCharacter(String crnStr) {
        String methodName = "getCheckCharacter(String crnStr):";
        log.debug(methodName + " entered, crnStr=" + crnStr);
        try {
            String numberString = crnStr.substring(2, 4) + crnStr.substring(6, 8)
                + crnStr.substring(0, 2) + crnStr.substring(8, 19);
            // parseLong is ok here because numberString will never be too large for
            // a 64 bit int.
            int checkNo = (int) (Long.parseLong(numberString) % 23);
            log.debug("checkcharacter=" + letters[checkNo]);
            log.debug(methodName + " exited.");
            return letters[checkNo];
        } catch (NumberFormatException ex) {
            log.debug("CRN is not the expected format: YYFFUUSSNNNNNNNNNNNOOO");
            throw new IllegalArgumentException("CRN is not the expected format: YYFFUUSSNNNNNNNNNNNOOO");
        }
    }

    public static boolean isCorrectFormat(String crnStr) {
        String methodName = "isCorrectFormat(String crnStr):";
        log.debug(methodName + " entered, crnStr=" + crnStr);

        // check argument is correct size.
        if (crnStr == null || crnStr.length() != LENGTH)
            return false;

        
        // check numbers are in the appropriate places
        
        // We cannot use Long.parseLong(numberString) to check that the string is composed of
        // only digits, because numberString is too large for a long int, and results in 
        // NumberFormatException exceptions being thrown.
        String numberString = crnStr.substring(0, 4) + crnStr.substring(6, 19) + crnStr.substring(20, 23);

        return numberString.matches("[0-9]*");
    }
}