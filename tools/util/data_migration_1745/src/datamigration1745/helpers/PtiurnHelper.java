package datamigration1745.helpers;

// java

//import java.util.Calendar;
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
 * Title: PtiurnHelper
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
 * @version $Id: PtiurnHelper.java,v 1.3 2007/09/19 08:48:51 szn20z Exp $
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

/* PTIURN
 * FFUUNNNNNYY, where:
 * FF is force location code (numeric)
 * UU – division / subdivision/ASU Code (alpha-numeric)
 * NNNNN is in range 1-99999 and zero padded
 * Y is the year
 *
 * "[0-9]{2}[A-Za-z0-9]{2}[0-9]{5}[0-9]{2}"      
 */
public class PtiurnHelper {

    public static final int LENGTH = 11;

    private static final String PTIURN_REGEX = "[0-9]{2}[A-Za-z0-9]{2}[0-9]{5}[0-9]{2}";//ptiurn regular expression

    private static Logger log = CSServices.getLogger(PtiurnHelper.class);

    private HashSet<String> forceLocationCodes = new HashSet<String>();
    
    public PtiurnHelper(HOPoliceForceVO[] policeForceCodes) {
        for(HOPoliceForceVO x : policeForceCodes) {
            forceLocationCodes.add(x.getRefCode());
        }
    }

    public boolean isValidPtiurn(String ptiurn) throws CSValidationException
    {
        //CAS: maybe check the length first?????
        log.info("top: validatePtiurn()");
        if (ptiurn == null || ptiurn.length()==0)
        {
            log.debug("validatePtiurn: ptiurn is null or length zero and is therefore valid");
            log.info("exit: validatePtiurn()");
            throw new CSValidationException("gui.updateDefendantDialog.invalid.ptiurn.format",//error message displayed in message dialog
            "invalid PTIURN: null or zero length"); //error message shown in debug output           
        }
        log.debug("validatePtiurn: ptiurn is NOT null or zero length so continuing with validation");

        //(a) validate against regular expression
        if (!stringMatchesRegex(PTIURN_REGEX, ptiurn)){
            log.debug("validatePtiurn: ptiurn doesn't match the regular expression");
            throw new CSValidationException("gui.updateDefendantDialog.invalid.ptiurn.format",//error message displayed in message dialog
                    "invalid ptiurn: failed regular expression validation"); //error message shown in debug output           
        }       
        log.debug("validatePtiurn: ptiurn matches regular expression");
        try{
            //(b) check that NNNNN is not all zeros, i.e. must be greater than zero
            checkPtiurnSeqNoNotZero(ptiurn);
            
        }catch (CSValidationException e){
            log.debug("validatePtiurn: sequence number inside ptiurn is less than 00001");
            throw e;
        }
        log.debug("validatePtiurn: sequence number within ptiurn is not zero");
              
        //(c) do DB lookup of FF to see if
        //it matches a value in the DB
        //if ( FF doesn't match value in the db)
        String forceLocCode = extractForceLocCode(ptiurn);
        log.debug("force location code is " + forceLocCode);
        if (!isValidForceLocationCode(forceLocCode)){
            log.debug("validatePtiurn: invalid force location Code");
            throw new CSValidationException("gui.updateDefendantDialog.invalid.ptiurn.format",
                    "validatePtiurn: invalid Force Location Code");
        }
        log.debug("force location code is valid");
        log.info("exit: validatePtiurn()");
        
        return true;
    }
    
      /**
       * @param regEx
       * @throws CSValidationException
       */
      private void checkPtiurnSeqNoNotZero(String ptiurn) throws CSValidationException {
          log.debug("top:checkPtiurnSeqNoNotZero()");
          try
          {   
              String seqNo = ptiurn.substring(4,9);
              log.debug("seqNo from ptiurn is " + seqNo);
              if (Long.parseLong(ptiurn.substring(4,9)) < 1){
                  log.debug("checkPtiurnSeqNoNotZero(): sequence number is less than 1");
                  throw new CSValidationException("gui.updateDefendantDialog.invalid.ptiurn.format",
                  "invalid ptiurn: sequence number is less than 00001");
              }           
          }
          catch (NumberFormatException e){//string does not contain a parsable integer
              log.debug("checkPtiurnSeqNoNotZero(): exception thrown by parseLong performed on ptiurn");
              // if regex validation is done first shouldn't get here.
              throw new CSValidationException("gui.updateDefendantDialog.invalid.ptiurn.format",
              "invalid PTIURN: sequence number string does not contain a parsable integer, " +
              "compare against regex before call this method");
          }
      }
      
      private boolean stringMatchesRegex(String regex, String value) throws PatternSyntaxException {
          boolean match_found = false;
          try{
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
      
      private String extractForceLocCode(String ptiurn){
          String return_val = "";
          if (ptiurn != null){
              return_val = ptiurn.substring(0,2);
          }
          return return_val;
      }
}