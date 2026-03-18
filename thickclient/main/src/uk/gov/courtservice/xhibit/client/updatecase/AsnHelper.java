package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.util.helpers.RefSystemCodeHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: ASNHelper
 * </p>
 * <p>
 * Description: Used to validate an ASN using the method validateASN.
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: AsnHelper.java,v 1.4 2009/01/22 18:17:37 hewittm Exp $
 */

public class AsnHelper {

    public static final String ASN_REGEX = "[0-9]{2}[0-9]{2}[A-Za-z0-9]{2}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}";// asn
                                                                                                            // regular
                                                                                                            // expression

    /**
     * letter array used to resolve check character 1 = A, 2 = B, 3 = C, 4 = D,
     * 5 = E, 6 = F, 7 = G, 8 = H, 9 = J, 10 = K, 11 = L, 12 = M, 13 = N, 14 =
     * P, 15 = Q, 16 = R, 17 = T, 18 = U, 19 = V, 20 = W, 21 = X, 22 = Y, 0 = Z
     * I,S,O are missing
     */
    private static final String[] letters = { "Z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N",
            "P", "Q", "R", "T", "U", "V", "W", "X", "Y" };

    private static Logger log = CSServices.getLogger(AsnHelper.class);

    private List<RefSystemCodeBasicValue> sortedForceLocationCodes;

    public AsnHelper() {
        super();
        setUpRefData();
    }

    /**
     * Validates the ASN against the following format<br>
     * 
     * ASN<br>
     * “[0-9]{2}[0-9]{2} [A-Za-z0-9]{2}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}”<br>
     * YYFFUUSSNNNNNNNNNNNA where: <br>
     * YY - year <br>
     * FF - police force code (numeric)<br>
     * UU – division / subdivision / ASU Code (alpha-numeric)<br>
     * SS - unique system code (numeric: 00-99)<br>
     * NNNNNNNNNNN - 11 digit sequence code (1-99999999999)<br>
     * A - Check sum - single character (alpha-numeric)<br>
     * 
     * @param asn
     * @throws CSValidationException
     */

    public void validateAsn(String asn) throws CSValidationException {
        // If it is blank this is valid so return now.
        log.debug("top: validateAsn");
        if (asn == null || asn.length() == 0) {
            log.debug("validateASN: asn is null or length zero");
            return;// this is valid
        }
        log.debug("validateASN: asn is NOT null or length zero");

        // (a) validate against regular expression
        String regEx = ASN_REGEX;

        if (!stringMatchesRegex(regEx, asn)) {
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",// error
                                                                                            // message
                                                                                            // displayed
                                                                                            // in
                                                                                            // message
                                                                                            // dialog
                    "invalid ASN: failed regular expression validation"); // error
                                                                            // message
                                                                            // shown
                                                                            // in
                                                                            // debug
                                                                            // output
        }
        log.debug("validateASN: asn matches regular expression");
        try {
            // (b) check that NNNNNNNNNNN is not all zeros, i.e. must be greater
            // than zero
            checkAsnSeqNoNotZero(asn);

        } catch (CSValidationException e) {
            log.debug("validateASN: sequence number inside asn is less than 00000000001");
            throw e;
        }
        log.debug("validateASN: sequence number within asn is not zero");

        // (c) do DB lookup of FF to see if it matches a value in the DB
        String forceLocCode = extractForceLocCode(asn);
        log.debug("force location code is " + forceLocCode);
        if (!isValidForceLocationCode(forceLocCode)) {
            log.debug("validateASN: invalid force location Code");
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                    "validateASN: invalid Force Location Code");
        }
        log.debug("force location code is valid");

        // (d) validate the checksum
        if (!isValidCheckSum(asn)) {
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                    "validateASN: check sum value incorrect");
        }
        log.debug("validateASN: check sum value correct");
    }

    public static boolean isValidCheckSum(String asn) {
        String methodName = "isValidCheckSum(String asn):";
        log.debug(methodName + " entered, asn=" + asn);
        try {
            return getCheckCharacter(asn).equals(asn.substring(19, 20));
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * Gets the check character. Rules for the check character: <p/> The check
     * character D, generated from YYFFUUSSNNNNNNNNNNN, will be put through the
     * standard Modulus 23 validation. <p/> Modulus 23 Validation Rules are as
     * follows: a) Take the 2 digits from the Force Id at positions 3 and 4. b)
     * Add the 2 digits from the System Id at positions 7 and 8 to the end of
     * (a). c) Add the 2 digits from the Year at positions 1 and 2 to the end of
     * (b). d) Add the 11 digits from the Numeric Id at positions 9 to 19 to the
     * end of (c) e) Take the check sum character at position 20. f) Divide the
     * number in (d) above by 23 and record the remainder. g) Convert the
     * remainder (for example, 4 = D) with the following conversion: 1 = A, 2 =
     * B, 3 = C, 4 = D, 5 = E, 6 = F, 7 = G, 8 = H, 9 = J, 10 = K, 11 = L, 12 =
     * M, 13 = N, 14 = P, 15 = Q, 16 = R, 17 = T, 18 = U, 19 = V, 20 = W, 21 =
     * X, 22 = Y, 0 = Z h) The converted remainder should be the same as the
     * check sum character D.
     */

    public static String getCheckCharacter(String asn) {
        String methodName = "getCheckCharacter(String asn):";
        log.debug(methodName + " entered, asn = " + asn);
        try {
            int checkNo = (int) (Long.parseLong(asn.substring(2, 4) + asn.substring(6, 8) + asn.substring(0, 2)
                    + asn.substring(8, 19)) % 23);
            log.debug("checkcharacter=" + letters[checkNo]);
            log.debug(methodName + " exited.");
            return letters[checkNo];
        } catch (NumberFormatException ex) {
            log.debug("ASN is not the expected format: YYFFUUSSNNNNNNNNNNN");
            throw new IllegalArgumentException("ASN is not the expected format: YYFFUUSSNNNNNNNNNNN");
        }
    }

    /**
     * @param regEx
     * @throws CSValidationException
     */
    private static void checkAsnSeqNoNotZero(String asn) throws CSValidationException {
        log.debug("top:checkAsnSeqNoNotZero()");
        try {
            String seqNo = asn.substring(8, 19);
            log.debug("seqNo from asn is " + seqNo);
            if (Long.parseLong(asn.substring(8, 19)) < 1) {
                log.debug("checkAsnSeqNoNotZero(): sequence number is less than 1");
                throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                        "invalid ASN: sequence number is less than 00000000001");
            }
        } catch (NumberFormatException e) {// string does not contain a
                                            // parsable integer
            log.debug("checkAsnSeqNoNotZero(): exception thrown by parseLong performed on asn");
            // if regex validation is done first shouldn't get here.
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                    "invalid ASN: sequence number string does not contain a parsable integer, "
                            + "compare against regex before call this method");
        }
    }

    private static boolean stringMatchesRegex(String regex, String value) throws PatternSyntaxException {
        // compare asn field with regular expression
        boolean match_found = false;
        try {
            // regex = "[A-Za-z0-9]{4}[0-9]{5}[0-9]{2}";
            Pattern p = Pattern.compile(regex);// throws PatternSyntaxException
            Matcher m = p.matcher(value);
            match_found = m.matches();
            
        } catch (PatternSyntaxException e) {
            // the syntax of the expression passed into compile is invalid
            
            throw e;
        }
        return match_found;
    }

    public void setUpRefData() {
        sortedForceLocationCodes = RefSystemCodeHelper.getSortedForceLocationCodes(
                XhibitSingleton.getInstance().getCourtId());
    }

    public boolean isValidForceLocationCode(String forceLocCode) {
        if (forceLocCode != null) {
            // create Search Data
            RefSystemCodeBasicValue rscbv = new RefSystemCodeBasicValue();
            rscbv.setCode(forceLocCode);

            // get sorted reference data & search reference data
            return RefSystemCodeHelper.isValidRefSystemCode(sortedForceLocationCodes, rscbv);
        }
        return false;
    }

    private static String extractForceLocCode(String asn) {
        String return_val = "";
        if (asn != null) {
            return_val = asn.substring(2, 4);
        }
        return return_val;
    }
}