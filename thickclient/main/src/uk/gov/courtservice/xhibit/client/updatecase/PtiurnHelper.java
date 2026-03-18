package uk.gov.courtservice.xhibit.client.updatecase;

// java

//import java.util.Calendar;
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
 * @version $Id: PtiurnHelper.java,v 1.4 2009/01/22 18:17:38 hewittm Exp $
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
    
    public static final String PTIURN_REGEX = "[0-9]{2}[A-Za-z0-9]{2}[0-9]{5}[0-9]{2}";//ptiurn regular expression

    private static Logger log = CSServices.getLogger(PtiurnHelper.class);

    private List<RefSystemCodeBasicValue> sortedForceLocationCodes; 
    
    public PtiurnHelper() {
        super();
        setUpRefData();
    }

    public void validatePtiurn(String ptiurn) throws CSValidationException
    {
        log.info("top: validatePtiurn()");
        if (ptiurn == null || ptiurn.length()==0)
        {
            log.debug("validatePtiurn: ptiurn is null or length zero and is therefore valid");
            log.info("exit: validatePtiurn()");
            return;
        }
        log.debug("validatePtiurn: ptiurn is NOT null or zero length so continuing with validation");

        //(a) validate against regular expression
        String regEx = PTIURN_REGEX;
        
        if (!stringMatchesRegex(regEx, ptiurn)){
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
    }
    
      /**
       * @param regEx
       * @throws CSValidationException
       */
      private static void checkPtiurnSeqNoNotZero(String ptiurn) throws CSValidationException {
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
      
      private static boolean stringMatchesRegex(String regex, String value) throws PatternSyntaxException {
          boolean match_found = false;
          try{
              Pattern p = Pattern.compile(regex);//throws PatternSyntaxException
              Matcher  m = p.matcher(value);
              match_found = m.matches();
              
          }
          catch(PatternSyntaxException e){
              //the syntax of the expression passed into compile is invalid
              
              throw e;
          }
          return match_found;
      }

      public void setUpRefData(){
           
          
          sortedForceLocationCodes = RefSystemCodeHelper.getSortedForceLocationCodes(
                  XhibitSingleton.getInstance().getCourtId());
      }
   
      public boolean isValidForceLocationCode(String forceLocCode){
          if (forceLocCode != null){
              //create Search Data
              RefSystemCodeBasicValue rscbv = new RefSystemCodeBasicValue();
              rscbv.setCode(forceLocCode);

              //get sorted reference data & search reference data
              return RefSystemCodeHelper.isValidRefSystemCode(sortedForceLocationCodes, rscbv);
          }
          return false;
      }
      
      private static String extractForceLocCode(String ptiurn){
          String return_val = "";
          if (ptiurn != null){
              return_val = ptiurn.substring(0,2);
          }
          return return_val;
      }
}