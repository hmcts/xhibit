package datamigration1745.helpers;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import datamigration1745.vos.HOPoliceForceVO;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

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
 * @version $Id: AsnHelper.java,v 1.5 2009/04/21 17:00:43 hewittm Exp $
 */

/* ASN
 * “[0-9]{2}[0-9]{2} [A-Za-z0-9]{2}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}”
 * YYFFUUSSNNNNNNNNNNNA where:
 * YY - year
 * FF - police force code (numeric)
 * UU – division / subdivision / ASU Code (alpha-numeric)
 * SS - unique system code (numeric: 00-99)
 * NNNNNNNNNNN - 11 digit sequence code (1-99999999999)
 * A - Check sum - single character (alpha-numeric)
 */
public class AsnHelper {

    public static final String ZERO_ZERO_NP = "00NP";
    
    public static final int LENGTH = 20;

    private static final String ASN_REGEX = "[0-9]{2}[0-9]{2}[A-Za-z0-9]{2}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}";//asn regular expression

    /**
     * letter array used to resolve check character 1 = A, 2 = B, 3 = C, 4 = D,
     * 5 = E, 6 = F, 7 = G, 8 = H, 9 = J, 10 = K, 11 = L, 12 = M, 13 = N, 14 =
     * P, 15 = Q, 16 = R, 17 = T, 18 = U, 19 = V, 20 = W, 21 = X, 22 = Y, 0 = Z
     * I,S,O are missing
     */
    private static final String[] letters = { "Z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N",
            "P", "Q", "R", "T", "U", "V", "W", "X", "Y" };

    private static Logger log = CSServices.getLogger(AsnHelper.class);

    private HashSet<String> forceLocationCodes = new HashSet<String>();

    public AsnHelper(HOPoliceForceVO[] policeForceCodes) {
        for(HOPoliceForceVO x : policeForceCodes) {
            forceLocationCodes.add(x.getRefCode());
        }
    }

    public boolean isValidAsn(String asn) throws CSValidationException
    {
        //CAS: maybe check the length first
        // first check that it's not blank.  If it is blank this is valid so return now.
        log.debug("top: validateAsn");
        if (asn == null || asn.length()==0)
        {
            log.debug("validateASN: asn is null or length zero");
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",//error message displayed in message dialog
            "invalid ASN: null or zero length"); //error message shown in debug output
        }
        log.debug("validateASN: asn is NOT null or length zero");
        // TO DO
        //(a) validate against regular expression where FF is numeric
        if (!stringMatchesRegex(ASN_REGEX, asn)){
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",//error message displayed in message dialog
                    "invalid ASN: failed regular expression validation"); //error message shown in debug output
        }
        log.debug("validateASN: asn matches regular expression");
        try{
            //(b) check that NNNNNNNNNNN is not all zeros, i.e. must be greater than zero
            checkAsnSeqNoNotZero(asn);

        }catch (CSValidationException e){
            log.debug("validateASN: sequence number inside asn is less than 00000000001");
            throw e;
        }
        log.debug("validateASN: sequence number within asn is not zero");

        if (isZeroZeroNPCode(asn)) {
            log.debug("force location code is " + ZERO_ZERO_NP);
        } else {
            //(c) do DB lookup of FF to see if
            //it matches a value in the DB
            //if ( FF doesn't match value in the db)
            String forceLocCode = extractForceLocCode(asn);
            log.debug("force location code is " + forceLocCode);
            if (!isValidForceLocationCode(forceLocCode)){
                log.debug("validateASN: invalid force location Code");
                throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                "validateASN: invalid Force Location Code");
            }
            log.debug("force location code is valid");
        }
        
        //(d) validate A the checksum //in existing code on Add Defendant to Count screen
        if (!isValidCheckSum(asn))
        {
            throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
            "validateASN: check sum value incorrect");
        }
        log.debug("validateASN: check sum value correct");

        return true;
    }

    /*
     * The force location code of 00 is valid only if followed
     * by NP, e.g. 00NP is to be migrated.  00XX is not to 
     * be migrated.
     */
    private boolean isZeroZeroNPCode(String asn) {
        return asn.substring(2,6).equals(ZERO_ZERO_NP);
    }
    
    private boolean isValidCheckSum(String asn) {
        String methodName = "isValidCheckSum(String asn):";
        log.debug(methodName + " entered, asn=" + asn);
        try {
            return getCheckCharacter(asn).equals(asn.substring(19, 20));
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
//CAS start
    private String getCheckCharacter(String asn) {
        String methodName = "getCheckCharacter(String asn):";
        log.debug(methodName + " entered, asn = " + asn);
        try {
            int checkNo = (int) (Long.parseLong(asn.substring(2, 4) + asn.substring(6, 8)
                    + asn.substring(0, 2) + asn.substring(8, 19)) % 23);
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
      private void checkAsnSeqNoNotZero(String asn) throws CSValidationException {
          log.debug("top:checkAsnSeqNoNotZero()");
          try
          {
              String seqNo = asn.substring(8,19);
              log.debug("seqNo from asn is " + seqNo);
              if (Long.parseLong(asn.substring(8,19)) < 1){
                  log.debug("checkAsnSeqNoNotZero(): sequence number is less than 1");
                  throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
                  "invalid ASN: sequence number is less than 00000000001");
              }
          }
          catch (NumberFormatException e){//string does not contain a parsable integer
              log.debug("checkAsnSeqNoNotZero(): exception thrown by parseLong performed on asn");
              // if regex validation is done first shouldn't get here.
              throw new CSValidationException("gui.updateDefendantDialog.invalid.asn.format",
              "invalid ASN: sequence number string does not contain a parsable integer, " +
              "compare against regex before call this method");
          }
      }

      private boolean stringMatchesRegex(String regex, String value) throws PatternSyntaxException {
          // compare asn field with regular expression
          boolean match_found = false;
          try{
              //regex = "[A-Za-z0-9]{4}[0-9]{5}[0-9]{2}";
              Pattern p = Pattern.compile(regex);//throws PatternSyntaxException
              Matcher  m = p.matcher(value);
              match_found = m.matches();
              System.out.println("Pattern is " + m.pattern());
          }
          catch(PatternSyntaxException e){
                  //the syntax of the expression passed into compile is invalid
                  System.out.println("syntax of regular expression incorrect");
                  throw e;
          }
          return match_found;
      }

      private boolean isValidForceLocationCode(String forceLocCode){
          return forceLocationCodes.contains(forceLocCode);
      }

      private String extractForceLocCode(String asn){
          String return_val = "";
          if (asn != null){
              return_val = asn.substring(2,4);
          }
          return return_val;
      }
}


