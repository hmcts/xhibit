package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodes;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_codes.XhbD20OffenceCodesHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLink;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2Home;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.D20OrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DateOfBirth;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Name;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Offence1;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Offence2;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Offence3;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Offence4;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceAlcoholLevel1;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceAlcoholLevel2;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceAlcoholLevel3;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceAlcoholLevel4;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence1;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence1Section;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence2;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence2Section;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence3;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence3Section;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence4;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OffenceOtherSentence4Section;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.D20AlcoholLevelTypes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.D20DisqTestPassedDropdown;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.D20InterimFinalDropdown;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.D20LicenceTypeProducedInCourt;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: D20OrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a D20 Order
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class D20OrderHelper {
	private static ResourceBundle res = CSServices.getConfigServices().getBundle("XHIBITOrderOffenceResources", Locale.UK);
	
    private static final Logger log = CSServices.getLogger(D20OrderHelper.class);
    
    private static final String FORM_MESSAGE = "ORDER_D20_Form_Invalid_Nos";
    
    private static final String OFFENCECODE_MESSAGE = "ORDER_D20_Offence_Code_Invalid";
    
    private static final String OFFENCEDURATION_MESSAGE = "ORDER_D20_Offence_Duration_Invalid";
    
    private static final String OFFENCEALCOHOL_MESSAGE = "ORDER_D20_Offence_Alcohol_Invalid";
    
    private static final String OTHERTITLE_MESSAGE = "ORDER_D20_Invalid_Other_Title";
    
    private static final String DRIVERNUMBER_MESSAGE = "ORDER_D20_Invalid_Driver_Number";
    
    private static final String LICENCE_MESSAGE = "ORDER_D20_Invalid_Number";
    
    private static final String LICENCE_MESSAGE2 = "ORDER_D20_Must_Have_Licence_No";
    
    private static final String NOOFFENCESELECTED_MESSAGE = "ORDER_D20_No_Offence_Entered";
    
    private static final String OFFENCEDATE_MESSAGE = "ORDER_D20_Offence_Date_Must_Be_Entered";
    
    private static final String DTETP_MESSAGE = "ORDER_D20_DTETP_Must_Be_Entered";
    
    private static final String ALCOHOLLEVEL_MESSAGE = "ORDER_D20_Alcohol_Level_Must_Be_Entered";
    
    private static final String INTERIM_FINALSENTENCE_MESSAGE = "ORDER_D20_Interim_FinalSentence_Must_Be_Set";
    
    private static final String MISSING_MANDATORY_ITEM = "ORDER_D20_Missing_Mandatory_Item";
    
    private static final String CONVICTING_COURT = "order_convicting_court";
    
    private static final String CONVICTION_DATE = "order_conviction_date";
    
    private static final String DEFENDANT_GENDER = "order_defendant_gender";
    
    private static final String DEFENDANT_DOB = "order_defendant_dob";
    
    private static final String DEFENDANT_SURNAME = "order_defendant_surname";
    
    private static final String DEFENDANT_FORENAMES = "order_defendant_forenames";
    
    private static final String MANDATORY_MESSAGE = "ORDER_D20_Mandatory_Required";
    
    private static final String PENALTY_POINTS_MESSAGE = "ORDER_D20_PenaltyPoints";
    
    private static final String PENALTY_POINTS_NOT_ALLOWED = "ORDER_D20_PenaltyPointsNotAllowed";
    
    private static String[] DATEOFOFFENCE_NONMANDATORYCODES = {"TT99"};
    
    //private static BisRefControllerBean bisRefControllerBean = (BisRefControllerBean)CSServices.getEJBServices().createLocalSession(BisRefControllerBean.class);
    private static XhbDefendantOnOffenceHome defendantOnOffenceHome = null;
    private static XhbD20OffenceCodesHome offenceCodesHome = null;
    private static XhbCaseHome caseHome = null;
    private static XhbChargeHome chargeHome = null;
    private static XhbOffenceHome offenceHome = null;
    private static XhbCourtHome courtHome = null;
    private static XhbDisposal2Home disposal2Home = null;
    private static XhbD20OffenceLinkHome d20OffenceLinkHome = null;
    
    private static Class clazz = D20OrderHelper.class;
    
    static{
    	Context ctx = null;
    	
    	try{
        	ctx = new InitialContext();

        	defendantOnOffenceHome = (XhbDefendantOnOffenceHome)ctx.lookup(XhbDefendantOnOffenceHome.JNDI_NAME);
    		offenceCodesHome = (XhbD20OffenceCodesHome)ctx.lookup(XhbD20OffenceCodesHome.JNDI_NAME);
        	caseHome = (XhbCaseHome)ctx.lookup(XhbCaseHome.JNDI_NAME);
        	chargeHome = (XhbChargeHome)ctx.lookup(XhbChargeHome.JNDI_NAME);
        	offenceHome = (XhbOffenceHome)ctx.lookup(XhbOffenceHome.JNDI_NAME);
        	courtHome = (XhbCourtHome)ctx.lookup(XhbCourtHome.JNDI_NAME);
        	disposal2Home = (XhbDisposal2Home)ctx.lookup(XhbDisposal2Home.JNDI_NAME);
        	d20OffenceLinkHome = (XhbD20OffenceLinkHome)ctx.lookup(XhbD20OffenceLinkHome.JNDI_NAME);
    	}catch(NamingException ex) {
    		CSServices.getDefaultErrorHandler().handleError(ex, clazz);
    		throw new EJBException(ex);
	    }finally {
	    	try {
	    		if(ctx != null) ctx.close();
	    	}catch(NamingException ignore) {
	    		CSServices.getDefaultErrorHandler().handleError(ignore, clazz);
	    		ignore.printStackTrace();
	      }
	    }
    }
    
    /**
     * Utility methodValidation
     * 
     * @param mos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateD20Order(D20OrderStructure dos, Integer defendantOnCaseID,  String typeCode, OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************D20 Order: Logical Validation");
        validateD20HeaderFooterDetails(dos, helper);
        validateD20OffenceDetails(dos,defendantOnCaseID, helper);
    }
    
	/**
     * 
     * @param dos
     * @param helper
     */
    private static void validateD20HeaderFooterDetails(D20OrderStructure dos, ValidationHelper helper) {
        log.debug("********************D20 Order: Logical header/footer Validation");
        
        // Check that the form no's are valid
        if (dos.getForms() != null) {
            if (new Integer(dos.getForms().getTotalForms()).intValue() < new Integer(dos.getForms().getFormNumber()).intValue()) {
                // This is a validation error
                helper.addLogicalProblem(FORM_MESSAGE, null);
            }
        }
        
        // Check that the other title is ok if "Other" is selected as the title
        if (dos.getD20PersonalDetails() != null && dos.getD20PersonalDetails().getTitle().equals("Other")) {
            if (dos.getD20PersonalDetails().getOtherTitle() != null && dos.getD20PersonalDetails().getOtherTitle().trim().length() > 0) {
                // All ok
            } else {
                helper.addLogicalProblem(OTHERTITLE_MESSAGE, null);
            }
        }
        
        // Driver number not mandatory but ensure correct format if value has been entered
        // AND Licence type is 1 (provisional) or 2 (Full)
        // Format (16 chars) is [A-Z,0-9]{5}[0-9][0,1,5,6][0-9]([0][1-9]|[1-2][0-9]|[3][0,1])[0-9][A-Z,0-9]{3}[A-Z]{2}
        if (((dos.getDriverNumber() != null) && (dos.getDriverNumber().trim().length() > 0)) &&
        		(dos.getLicenceType().equals(D20LicenceTypeProducedInCourt.VALUE_1) || 
        				dos.getLicenceType().equals(D20LicenceTypeProducedInCourt.VALUE_2))) {
            String pattern = "[A-Z,0-9]{5}[0-9][0,1,5,6][0-9]([0][1-9]|[1-2][0-9]|[3][0,1])[0-9][A-Z,0-9]{3}[A-Z]{2}";
            
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(dos.getDriverNumber().trim().toUpperCase());
            if (!m.matches()) {
            	// Adding log message for Live issue that is being reported
            	log.warn("Invalid D20 Driver number message for case: '"+dos.getOrderHeader().getCaseNumber()+"' and Driver number='"+dos.getDriverNumber()+"'");
                helper.addLogicalProblem(DRIVERNUMBER_MESSAGE, null);
            } else {
                dos.setDriverNumber(dos.getDriverNumber().toUpperCase());
            }
        }
        
        // Check the format of the licence no
        // Format (2 chars) is [0-9][0-9]
        // If it is a single digit, prefix it with a 0
        if ((dos.getLicenceIssueNo() != null) && (dos.getLicenceIssueNo().trim().length() > 0)) {
            String pattern = "[0-9]";
            
            if ( dos.getLicenceIssueNo().trim().length() > 1){
            	pattern += "[0-9]";
            }
            Pattern r = Pattern.compile(pattern);
            if (dos.getLicenceIssueNo().trim().length()==1) {
            	dos.setLicenceIssueNo("0"+dos.getLicenceIssueNo().trim());
            }
            Matcher m = r.matcher(dos.getLicenceIssueNo().trim().toUpperCase());
            if (!m.matches()) {
                helper.addLogicalProblem(LICENCE_MESSAGE, null);
            } else {
                dos.setLicenceIssueNo(dos.getLicenceIssueNo().toUpperCase());
            }
        }
        
        // Check that the licence no has been entered if certain options have been selected from the licence type dropdown
        // Commenting out after session with the business, even though guidance docs for filling in the form states that this logic is needed
        if (dos.getLicenceType() != null) {
        	if (dos.getLicenceType().equals(D20LicenceTypeProducedInCourt.VALUE_1) || dos.getLicenceType().equals(D20LicenceTypeProducedInCourt.VALUE_2)) {
        		if (dos.getLicenceIssueNo().length() < 1) {	//	A single digit is valid. ("n" = "0n")
        			helper.addLogicalProblem(LICENCE_MESSAGE2, null);
        		}
        	}
        }
    }
    
    /**
     * Validate the 4 offences
     * 
     * @param dos
     * @param helper
     */
    private static void validateD20OffenceDetails(D20OrderStructure dos, Integer defendantOnCaseID, ValidationHelper helper) {
        log.debug("********************D20 Order: Logical header/footer Validation");
        
        String convictingCourt = dos.getOrderHeader().getCourtHouse().getCourtHouseName();
        
        if ( convictingCourt == null || convictingCourt.isEmpty() || !dos.getD20CommittingCourtSection().getSelected()){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString( CONVICTING_COURT)});
        }
        
        //	Conviction Date:
        Date convictionDate = dos.getD20CommittingCourtSection().getD20CommittingCourt().getDate();
        
        if ( convictionDate == null || "0000-00-00".equals(convictionDate.toString()) || !dos.getD20CommittingCourtSection().getSelected()){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString(CONVICTION_DATE)});
        }
        
        //	Defendant gender:
        String gender = dos.getD20PersonalDetails().getGender();
        
        if ( gender == null || gender.isEmpty()){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString(DEFENDANT_GENDER)});
        }
        
        //	Defendant Date of birth
        DateOfBirth dateOfBirth = dos.getOrderHeader().getDefendant().getPersonalDetails().getDateOfBirth();
        
        if ( dateOfBirth == null ){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString(DEFENDANT_DOB)});
        }
        
        //	Defendant Surname
        Name defendantName = dos.getOrderHeader().getDefendant().getPersonalDetails().getName();
        
        if ( defendantName == null || defendantName.getCitizenNameSurname() == null || defendantName.getCitizenNameSurname().isEmpty()){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString(DEFENDANT_SURNAME)});
        }
        
        //	Defendant First Name(s)
        if ( defendantName == null || defendantName.getCitizenNameForename() == null || defendantName.getCitizenNameForename().length == 0){
        	helper.addLogicalProblem(MISSING_MANDATORY_ITEM, new String[]{res.getString(DEFENDANT_FORENAMES)});
        }
        
        // Make sure at least 1 offence is selected
        if (!dos.getOffence1().getSelected() && !dos.getOffence2().getSelected() && !dos.getOffence3().getSelected() && !dos.getOffence4().getSelected()) {
            helper.addLogicalProblem(NOOFFENCESELECTED_MESSAGE, null);
        }

        //	Save this for case type checks.
        String caseNumber = dos.getOrderHeader().getCaseNumber();
        String courtName = dos.getOrderHeader().getCourtHouse().getCourtHouseName();
                
        Collection courtHouses = findNamedCourt( courtName );
        XhbCourt court = (XhbCourt)courtHouses.iterator().next();
        
        // Validate Offence 1
        if (dos.getOffence1().getSelected()) {
            Offence1 thisOffence = dos.getOffence1();
  
            // Check that the offence code is not empty
            if (thisOffence.getOffenceCode1() != null && thisOffence.getOffenceCode1().trim().length() > 0) {
                // It's ok
            	String offenceCode = thisOffence.getOffenceCode1();
            	
            	log.debug( String.format("Offence code: %s", offenceCode));

            	// Some checks for Final Sentence only
                if (!isInterim(thisOffence.getOffenceInterimFinal1())) {
                	
                	// This check only does mandatory disq at the moment and we want to ignore that check for interim D20's
            		validateDisposals( helper, defendantOnCaseID, offenceCode, caseNumber, court.getCourtId() );
            		
		            //	XLC2-70: Check Penalty points.
	            	if ( penaltyPointsLogged( helper, defendantOnCaseID, offenceCode, caseNumber,  court.getCourtId() )){
		                Integer recordedPenaltyPoints = thisOffence.getOffencePenaltyPoints1();
		            	validatePenaltyPoints( helper, offenceCode, recordedPenaltyPoints );
	            	}
                }
            } else {
                helper.addLogicalProblem(OFFENCECODE_MESSAGE, null);
            }
            
            
            // Check that the date of offence has been entered unless the offence code is TT99
            if ((thisOffence.getOffenceDate1Section() == null) || (!thisOffence.getOffenceDate1Section().getSelected())) {
            	if (!checkEntry(thisOffence.getOffenceCode1(), DATEOFOFFENCE_NONMANDATORYCODES)) {
            		helper.addLogicalProblem(OFFENCEDATE_MESSAGE, null);
            	}
            }
            
            
            // Some checks for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal1())) {
            	// Check that the alcohol level has been entered for some offence codes
            	String alcoholDrugLevel = thisOffence.getOffenceAlcoholLevel1() != null ? thisOffence.getOffenceAlcoholLevel1().getD20AlcoholLevelValue() : null;
	            validateAlcoholDrugsOffence(helper, thisOffence.getOffenceCode1(), alcoholDrugLevel);
	            
	            // Check that the DTETP has been entered for some offence codes
	            D20DisqTestPassedDropdown dropDown = thisOffence.getOffenceDisqTestPassed1();
	            validateMandatoryTest( helper, thisOffence.getOffenceCode1(), dropDown);
            }
            
            
            // These checks are for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal1())) {
	            // Check that the duration is not empty unless Not Applicable duration is selected
	            if (thisOffence.getOffenceOtherSentence1Section() != null) {
	                OffenceOtherSentence1Section offenceOtherSentenceSection = thisOffence.getOffenceOtherSentence1Section();
	                
	                if (offenceOtherSentenceSection.getOffenceOtherSentence1() != null) {
	                    OffenceOtherSentence1 offenceOtherSentence = offenceOtherSentenceSection.getOffenceOtherSentence1();
	                    if (!offenceOtherSentence.getD20OffenceOtherSentenceDurationType().equals("Not Applicable")) {
	                        if (offenceOtherSentence.getD20OffenceOtherSentenceValue() != null && offenceOtherSentence.getD20OffenceOtherSentenceValue().trim().length() > 0) {
	                            // All ok
	                        } else {
	                            helper.addLogicalProblem(OFFENCEDURATION_MESSAGE, null);
	                        }
	                    }
	                }
	            }
	            
	            // Check that the alcohol level is not empty unless Not Applicable Alcohol type is selected; check for Final Sentence only
	            if (thisOffence.getOffenceAlcoholLevel1() != null) {
	                OffenceAlcoholLevel1 offenceAlcoholLevel1 = thisOffence.getOffenceAlcoholLevel1();
	                
	                if (!offenceAlcoholLevel1.getD20AlcoholLevelDropdown().equals(D20AlcoholLevelTypes.VALUE_3)) {
	                    if (offenceAlcoholLevel1.getD20AlcoholLevelValue() != null && offenceAlcoholLevel1.getD20AlcoholLevelValue().trim().length() > 0) {
	                        // All ok
	                    } else {
	                        helper.addLogicalProblem(OFFENCEALCOHOL_MESSAGE, null);
	                    }
	                }
	            }
	            
	            // Check that the Interim / Final Sentence field is populated with either Interim or Final Sentence (it cannot be set to Not Applicable or null)
	            if (thisOffence.getOffenceInterimFinal1() != null) {
	            	if (!isInterimFinalValid(thisOffence.getOffenceInterimFinal1())) {
	            		helper.addLogicalProblem(INTERIM_FINALSENTENCE_MESSAGE, null);
	            	}
	            }
            }
        }
        
        // Validate Offence 2
        if (dos.getOffence2().getSelected()) {
            Offence2 thisOffence = dos.getOffence2();
            
            // Check that the offence code is not empty
            if (thisOffence.getOffenceCode2() != null && thisOffence.getOffenceCode2().trim().length() > 0) {
                // Its ok
            	String offenceCode = thisOffence.getOffenceCode2();
            	
            	log.debug( String.format("Offence code: %s", offenceCode));
            	
            	// Some checks for Final Sentence only
                if (!isInterim(thisOffence.getOffenceInterimFinal2())) {
                	
                	// This check only does mandatory disq at the moment and we want to ignore that check for interim D20's
            		validateDisposals( helper, defendantOnCaseID, offenceCode, caseNumber, court.getCourtId() );
            		
		            //	XLC2-70: Check Penalty points.
	            	if ( penaltyPointsLogged( helper, defendantOnCaseID, offenceCode, caseNumber,  court.getCourtId() )){
		                Integer recordedPenaltyPoints = thisOffence.getOffencePenaltyPoints2();
		            	validatePenaltyPoints( helper, offenceCode, recordedPenaltyPoints );
	            	}
                }
            } else {
                helper.addLogicalProblem(OFFENCECODE_MESSAGE, null);
            }
            
            // Check that the date of offence has been entered unless the offence code is TT99
            if ((thisOffence.getOffenceDate2Section() == null) || (!thisOffence.getOffenceDate2Section().getSelected())) {
            	if (!checkEntry(thisOffence.getOffenceCode2(), DATEOFOFFENCE_NONMANDATORYCODES)) {
            		helper.addLogicalProblem(OFFENCEDATE_MESSAGE, null);
            	}
            }
            
            // Some checks for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal2())) {
	            // Check that the alcohol level has been entered for some offence codes
	            String alcoholDrugLevel = thisOffence.getOffenceAlcoholLevel2() != null ? thisOffence.getOffenceAlcoholLevel2().getD20AlcoholLevelValue() : null;
	            validateAlcoholDrugsOffence(helper, thisOffence.getOffenceCode2(), alcoholDrugLevel);
	            
	            // Check that the DTETP has been entered for some offence codes
	            D20DisqTestPassedDropdown dropDown = thisOffence.getOffenceDisqTestPassed2();
	            validateMandatoryTest( helper, thisOffence.getOffenceCode2(), dropDown);
            }
            
            
            // These checks are for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal2())) {
	            // Check that the duration is not empty unless Not Applicable duration is selected
	            if (thisOffence.getOffenceOtherSentence2Section() != null) {
	                OffenceOtherSentence2Section offenceOtherSentenceSection = thisOffence.getOffenceOtherSentence2Section();
	                
	                if (offenceOtherSentenceSection.getOffenceOtherSentence2() != null) {
	                    OffenceOtherSentence2 offenceOtherSentence = offenceOtherSentenceSection.getOffenceOtherSentence2();
	                    if (!offenceOtherSentence.getD20OffenceOtherSentenceDurationType().equals("Not Applicable")) {
	                        if (offenceOtherSentence.getD20OffenceOtherSentenceValue() != null && offenceOtherSentence.getD20OffenceOtherSentenceValue().trim().length() > 0) {
	                            // All ok
	                        } else {
	                            helper.addLogicalProblem(OFFENCEDURATION_MESSAGE, null);
	                        }
	                    }
	                }
	            }
	            
	            // Check that the alcohol level is not empty unless Not Applicable Alcohol type is selected; check for Final Sentence only
	            if (thisOffence.getOffenceAlcoholLevel2() != null) {
	                OffenceAlcoholLevel2 offenceAlcoholLevel2 = thisOffence.getOffenceAlcoholLevel2();
	                
	                if (!offenceAlcoholLevel2.getD20AlcoholLevelDropdown().equals(D20AlcoholLevelTypes.VALUE_3)) {
	                    if (offenceAlcoholLevel2.getD20AlcoholLevelValue() != null && offenceAlcoholLevel2.getD20AlcoholLevelValue().trim().length() > 0) {
	                        // All ok
	                    } else {
	                        helper.addLogicalProblem(OFFENCEALCOHOL_MESSAGE, null);
	                    }
	                }
	            }
	            
	            // Check that the Interim / Final Sentence field is populated with either Interim or Final Sentence (it cannot be set to Not Applicable or null)
	            if (thisOffence.getOffenceInterimFinal2() != null) {
	            	if (!isInterimFinalValid(thisOffence.getOffenceInterimFinal2())) {
	            		helper.addLogicalProblem(INTERIM_FINALSENTENCE_MESSAGE, null);
	            	}
	            	
	            }
            }
        }
        
        // Validate Offence 3
        if (dos.getOffence3().getSelected()) {
            Offence3 thisOffence = dos.getOffence3();
            
            // Check that the offence code is not empty
            if (thisOffence.getOffenceCode3() != null && thisOffence.getOffenceCode3().trim().length() > 0) {
                // Its ok
            	String offenceCode = thisOffence.getOffenceCode3();
            	
            	log.debug( String.format("Offence code: %s", offenceCode));
          
            	// Some checks for Final Sentence only
                if (!isInterim(thisOffence.getOffenceInterimFinal3())) {
                	
                	// This check only does mandatory disq at the moment and we want to ignore that check for interim D20's
            		validateDisposals( helper, defendantOnCaseID, offenceCode, caseNumber, court.getCourtId() );
                	
		            //	XLC2-70: Check Penalty points.
	            	if ( penaltyPointsLogged( helper, defendantOnCaseID, offenceCode, caseNumber,  court.getCourtId() )){
		                Integer recordedPenaltyPoints = thisOffence.getOffencePenaltyPoints3();
		            	validatePenaltyPoints( helper, offenceCode, recordedPenaltyPoints );
	            	}
                }
            } else {
                helper.addLogicalProblem(OFFENCECODE_MESSAGE, null);
            }
            
            // Check that the date of offence has been entered unless the offence code is TT99
            if ((thisOffence.getOffenceDate3Section() == null) || (!thisOffence.getOffenceDate3Section().getSelected())) {
            	if (!checkEntry(thisOffence.getOffenceCode3(), DATEOFOFFENCE_NONMANDATORYCODES)) {
            		helper.addLogicalProblem(OFFENCEDATE_MESSAGE, null);
            	}
            }
            
            // Some checks for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal3())) {
	            // Check that the alcohol level has been entered for some offence codes
	            String alcoholDrugLevel = thisOffence.getOffenceAlcoholLevel3() != null ? thisOffence.getOffenceAlcoholLevel3().getD20AlcoholLevelValue() : null;
	            validateAlcoholDrugsOffence(helper, thisOffence.getOffenceCode3(), alcoholDrugLevel);
	            
	            // Check that the DTETP has been entered for some offence codes
	            D20DisqTestPassedDropdown dropDown = thisOffence.getOffenceDisqTestPassed3();
	            validateMandatoryTest( helper, thisOffence.getOffenceCode3(), dropDown);
            }
            
            
            // These checks are for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal3())) {
	            // Check that the duration is not empty unless Not Applicable duration is selected
	            if (thisOffence.getOffenceOtherSentence3Section() != null) {
	                OffenceOtherSentence3Section offenceOtherSentenceSection = thisOffence.getOffenceOtherSentence3Section();
	                
	                if (offenceOtherSentenceSection.getOffenceOtherSentence3() != null) {
	                    OffenceOtherSentence3 offenceOtherSentence = offenceOtherSentenceSection.getOffenceOtherSentence3();
	                    if (!offenceOtherSentence.getD20OffenceOtherSentenceDurationType().equals("Not Applicable")) {
	                        if (offenceOtherSentence.getD20OffenceOtherSentenceValue() != null && offenceOtherSentence.getD20OffenceOtherSentenceValue().trim().length() > 0) {
	                            // All ok
	                        } else {
	                            helper.addLogicalProblem(OFFENCEDURATION_MESSAGE, null);
	                        }
	                    }
	                }
	            }
	            
	            // Check that the alcohol level is not empty unless Not Applicable Alcohol type is selected
	            if (thisOffence.getOffenceAlcoholLevel3() != null) {
	                OffenceAlcoholLevel3 offenceAlcoholLevel3 = thisOffence.getOffenceAlcoholLevel3();
	                
	                if (!offenceAlcoholLevel3.getD20AlcoholLevelDropdown().equals(D20AlcoholLevelTypes.VALUE_3)) {
	                    if (offenceAlcoholLevel3.getD20AlcoholLevelValue() != null && offenceAlcoholLevel3.getD20AlcoholLevelValue().trim().length() > 0) {
	                        // All ok
	                    } else {
	                        helper.addLogicalProblem(OFFENCEALCOHOL_MESSAGE, null);
	                    }
	                }
	            }
	            
	            // Check that the Interim / Final Sentence field is populated with either Interim or Final Sentence (it cannot be set to Not Applicable or null)
	            if (thisOffence.getOffenceInterimFinal3() != null) {
	            	if (!isInterimFinalValid(thisOffence.getOffenceInterimFinal3())) {
	            		helper.addLogicalProblem(INTERIM_FINALSENTENCE_MESSAGE, null);
	            	}
	            	
	            }
            }
        }
        
        // Validate Offence 4
        if (dos.getOffence4().getSelected()) {
            Offence4 thisOffence = dos.getOffence4();
            
            // Check that the offence code is not empty
            if (thisOffence.getOffenceCode4() != null && thisOffence.getOffenceCode4().trim().length() > 0) {
                // Its ok
            	String offenceCode = thisOffence.getOffenceCode4();
            	
            	log.debug( String.format("Offence code: %s", offenceCode));
          
            	// Some checks for Final Sentence only
                if (!isInterim(thisOffence.getOffenceInterimFinal4())) {

                	// This check only does mandatory disq at the moment and we want to ignore that check for interim D20's
            		validateDisposals( helper, defendantOnCaseID, offenceCode, caseNumber, court.getCourtId() );
                	
		            //	XLC2-70: Check Penalty points.
	            	if ( penaltyPointsLogged( helper, defendantOnCaseID, offenceCode, caseNumber,  court.getCourtId() )){
		                Integer recordedPenaltyPoints = thisOffence.getOffencePenaltyPoints4();
		            	validatePenaltyPoints( helper, offenceCode, recordedPenaltyPoints );
	            	}
                }
            } else {
                helper.addLogicalProblem(OFFENCECODE_MESSAGE, null);
            }
            
            // Check that the date of offence has been entered unless the offence code is TT99
            if ((thisOffence.getOffenceDate4Section() == null) || (!thisOffence.getOffenceDate4Section().getSelected())) {
            	if (!checkEntry(thisOffence.getOffenceCode4(), DATEOFOFFENCE_NONMANDATORYCODES)) {
            		helper.addLogicalProblem(OFFENCEDATE_MESSAGE, null);
            	}
            }
            
            // Some checks for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal4())) {
	            // Check that the alcohol level has been entered for some offence codes
	            String alcoholDrugLevel = thisOffence.getOffenceAlcoholLevel4() != null ? thisOffence.getOffenceAlcoholLevel4().getD20AlcoholLevelValue() : null;
	            validateAlcoholDrugsOffence(helper, thisOffence.getOffenceCode4(), alcoholDrugLevel);
	            
	            // Check that the DTETP has been entered for some offence codes
	            D20DisqTestPassedDropdown dropDown = thisOffence.getOffenceDisqTestPassed4();
	            validateMandatoryTest( helper, thisOffence.getOffenceCode4(), dropDown);
            }
            
            
            // These checks are for Final Sentence only
            if (!isInterim(thisOffence.getOffenceInterimFinal4())) {
	            // Check that the duration is not empty unless Not Applicable duration is selected
	            if (thisOffence.getOffenceOtherSentence4Section() != null) {
	                OffenceOtherSentence4Section offenceOtherSentenceSection = thisOffence.getOffenceOtherSentence4Section();
	                
	                if (offenceOtherSentenceSection.getOffenceOtherSentence4() != null) {
	                    OffenceOtherSentence4 offenceOtherSentence = offenceOtherSentenceSection.getOffenceOtherSentence4();
	                    if (!offenceOtherSentence.getD20OffenceOtherSentenceDurationType().equals("Not Applicable")) {
	                        if (offenceOtherSentence.getD20OffenceOtherSentenceValue() != null && offenceOtherSentence.getD20OffenceOtherSentenceValue().trim().length() > 0) {
	                            // All ok
	                        } else {
	                            helper.addLogicalProblem(OFFENCEDURATION_MESSAGE, null);
	                        }
	                    }
	                }
	            }
	            
	            // Check that the alcohol level is not empty unless Not Applicable Alcohol type is selected
	            if (thisOffence.getOffenceAlcoholLevel4() != null) {
	                OffenceAlcoholLevel4 offenceAlcoholLevel4 = thisOffence.getOffenceAlcoholLevel4();
	                
	                if (!offenceAlcoholLevel4.getD20AlcoholLevelDropdown().equals(D20AlcoholLevelTypes.VALUE_3)) {
	                    if (offenceAlcoholLevel4.getD20AlcoholLevelValue() != null && offenceAlcoholLevel4.getD20AlcoholLevelValue().trim().length() > 0) {
	                        // All ok
	                    } else {
	                        helper.addLogicalProblem(OFFENCEALCOHOL_MESSAGE, null);
	                    }
	                }
	            }
	            
	            // Check that the Interim / Final Sentence field is populated with either Interim or Final Sentence (it cannot be set to Not Applicable or null)
	            if (thisOffence.getOffenceInterimFinal4() != null) {
	            	if (!isInterimFinalValid(thisOffence.getOffenceInterimFinal4())) {
	            		helper.addLogicalProblem(INTERIM_FINALSENTENCE_MESSAGE, null);
	            	}
	            	
	            }
            }
        }
    }

    /**
     * Do we have penalty points logged for this offence?
     * 
     * @param 	helper
     * @param 	defendantOnCaseID
     * @param 	offenceCode
     * @param 	caseNumber
     * @param 	courtId
     * 
     * @return	true if a disposal with penalty points (LENDPP) was found for this offence
     */
    private static boolean penaltyPointsLogged(ValidationHelper helper, Integer defendantOnCaseID, String offenceCode, String caseNumber, Integer courtId) {
		boolean outcome = offenceCodesHome != null && caseHome != null;
		
		if ( outcome ){
			XhbD20OffenceCodes offenceCodeInst = findNamedOffenceCode( offenceCode );
			
			if ( offenceCodeInst != null ){
				log.debug( String.format("Found offence code for code: %s", offenceCode));
				
				Integer caseNumberInt = Integer.parseInt( caseNumber.substring(1) );
				String caseType = caseNumber.substring(0,1);
				
				XhbCase thisCase = findThisCase( caseHome, caseNumberInt, caseType, courtId );

				outcome = thisCase != null;
				
				if ( outcome ) {
					DisposalValue[] disposalValues = getDisposalsForOffence( defendantOnCaseID, thisCase.getCaseId(), offenceCode);//getDisposalsForDefendantOnCase(defendantOnCaseID, thisCase.getCaseId());
					
					log.debug( String.format("Have %d disposals.", disposalValues.length));
					
					outcome = checkForDisposal( disposalValues, new String[] { "LENDPP"});
				}
			}
		}
		
		return outcome;
	}

    /**
     * Get the list of disposal values for the defendant on the case, the case and the offence code
     * 
     * @param 	defendantOnCaseID			The defendant on case ID
     * @param 	caseID						The case ID
     * @param 	offenceCode					The offence code
     * 
     * @return	The list of disposal values (Might be null)
     */
    private static DisposalValue[] getDisposalsForOffence( final Integer defendantOnCaseID, final Integer caseID, final String offenceCode){
    	DisposalValue[] validDisposalValues = null;
    	
    	//	Get all the disposal values.
    	DisposalValue[] disposalValues = getDisposalsForDefendantOnCase(defendantOnCaseID, caseID);
    	int numberOdDisposals = disposalValues.length;
		
		Collection links = null;
		try {
			links = d20OffenceLinkHome.findByDefOnCaseIdAndOffenceCode(defendantOnCaseID, offenceCode);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
		}
   	
		List<DisposalValue> listOfDisposal = new ArrayList<DisposalValue>();
		
		if ( links != null ){
			for( int index = 0; index < numberOdDisposals; index++){
				if (disposalValues[ index ].getDefendantOnOffenceId() != null) {
		    		try {
						XhbDefendantOnOffence defendantOnOffence = defendantOnOffenceHome.findByPrimaryKey( disposalValues[ index ].getDefendantOnOffenceId());
						
						Integer seqNumber = defendantOnOffence.getSeqNo();
						
						log.debug(String.format("Sequence number: %d", seqNumber ));
						
						log.debug(String.format("Found %s records", links.size()));
						Iterator iter = links.iterator();
						boolean linked = false;
						
						while ( iter.hasNext() && !linked){
							XhbD20OffenceLink d20OffenceLink = (XhbD20OffenceLink)iter.next();
							
							Integer linkSeqNo = d20OffenceLink.getSeqNo();
							
							//	Compare sequence numbers.
							log.debug(String.format("Sequuence no: %d", linkSeqNo));
							
							if ( linkSeqNo.equals(seqNumber)){
								listOfDisposal.add( disposalValues[ index]);
								linked = true;
							}
						}
					} catch (FinderException ex) {
						CSServices.getDefaultErrorHandler().handleError(ex, clazz);
					    throw new EJBException(ex);
					}
	    		}
	    	}
		}
    	
		validDisposalValues = new DisposalValue[listOfDisposal.size()];
		int index = 0;
		
		for (DisposalValue disposalValue : listOfDisposal) {
			validDisposalValues[ index++] = disposalValue;
		}
		
    	return validDisposalValues;
    }
    
	/**
     * Validate Mandatory re-test requirements
     * 
     * @param 	helper					The message helper
     * @param 	offenceCode				The offence code
     * @param 	dropDown				The Test Passed dropdown value
     */
    private static void validateMandatoryTest(ValidationHelper helper, String offenceCode, D20DisqTestPassedDropdown dropDown){
        XhbD20OffenceCodes offenceCodeInst = findNamedOffenceCode( offenceCode);
        
        if ( offenceCodeInst != null ){
        	String mandDTETP = offenceCodeInst.getMandDTETP();
        	
	        if ( mandDTETP != null &&  mandDTETP.equals("Y")){
		        if (dropDown == null || dropDown.getType() != D20DisqTestPassedDropdown.DTETP___4_TYPE) {
		    		helper.addLogicalProblem(DTETP_MESSAGE, new String[] {offenceCode});
		        }
	        }
        }
	}

	/**
     * Validate the alcohol/drug level where the offence code has a mandatory alcohol/drug level flag set
     * 
     * @param 	helper				Message helper
     * @param 	offenceCode			The offence code
     * @param 	alcoholDrugLevel	The alcohol/drug level
     */
    private static void validateAlcoholDrugsOffence(ValidationHelper helper, String offenceCode, String alcoholDrugLevel) {
        XhbD20OffenceCodes offenceCodeInst = findNamedOffenceCode( offenceCode);
        
        if ( offenceCodeInst != null ){
        	String alcholDrugLevel = offenceCodeInst.getMandAlcDrugLevel();
        	
	        if ( alcholDrugLevel != null && alcholDrugLevel.equals("Y")){
	            if (alcoholDrugLevel == null ||alcoholDrugLevel.isEmpty()) {
	        		helper.addLogicalProblem(ALCOHOLLEVEL_MESSAGE, new String[] {offenceCode});
	            }           	
	        }
        }
	}

	/**
     * Validate the penalty points.
     * If the charge has a penalty points range, then the recorded value must fall within that range.
     * 
     * @param 	helper					The validation Helper instance
     * @param 	offenceCode				The offence code recorded with teh offence
     * @param 	recordedPenaltyPoints	The prnalty points recorded with the charge
     */
    private static void validatePenaltyPoints(ValidationHelper helper, String offenceCode, Integer recordedPenaltyPoints) {
        XhbD20OffenceCodes offenceCodeInst = findNamedOffenceCode( offenceCode);
        
        String offencePenaltyPoints =offenceCodeInst != null ? offenceCodeInst.getPenaltyPoints() : null;
        
        log.debug(String.format("Offence penalty points: %s, recorded penalty points: %d", offencePenaltyPoints, recordedPenaltyPoints));
        if ( offencePenaltyPoints == null || offencePenaltyPoints.length() == 0 || offencePenaltyPoints.equals("NA")){
        	if ( recordedPenaltyPoints != null && !recordedPenaltyPoints.equals(0)) {
        		helper.addLogicalProblem(PENALTY_POINTS_NOT_ALLOWED, new String[] { offenceCode });
        	}
        }
        else {
        	//	Get the start/stop
        	Integer[] pointsRange = extractRange(offencePenaltyPoints);
        	
        	if ( pointsRange != null ){
        		if ( pointsRange.length == 1 ){
        			if ( recordedPenaltyPoints != pointsRange[0]){
        				helper.addLogicalProblem(PENALTY_POINTS_MESSAGE, new String[]{offenceCode, offencePenaltyPoints});
        			}
        		}
        		else{
        			if ( recordedPenaltyPoints.compareTo(pointsRange[0]) < 0 || recordedPenaltyPoints.compareTo( pointsRange[ 1 ]) > 0){
        				helper.addLogicalProblem(PENALTY_POINTS_MESSAGE, new String[]{offenceCode, offencePenaltyPoints});
        			}
        		}
        	}
        }
	}
    
    
	/**
     * Extract the range of penalty points
     * 
     * @param 	offencePenaltyPoints			The points range (i.e. "1-3", "3")
     * 
     * @return	An array of Integer (At most two entries) or null if the points cannot be evaluated.
     */
    private static Integer[] extractRange(String offencePenaltyPoints) {
		Integer[] outcome = null;
		
		if ( offencePenaltyPoints != null && offencePenaltyPoints.length() > 0 ){
			String[] elements = offencePenaltyPoints.split("-");
			
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			
			boolean success = true;
			for (int index = 0; index < elements.length && success; index++){
				try {
					candidates.add( Integer.parseInt(elements[index]));
				} catch( NumberFormatException ex){
					log.error(elements[index] +" is not parsable as an integer: " + ex);
					success = false;
				}
			}
			
			if ( success) {
				outcome = new Integer[candidates.size()];
				for ( int index = 0; index < candidates.size(); index++){
					outcome[ index] = candidates.get(index);
				}
			}
		}
		
		return outcome;
	}

	/**
     * Validate disposals on this case for the defendant.
     * 
     * @param	helper							Message helper
     * @param 	offenceCodesHome				Offence code home pointer
     * @param 	caseHome						Case home pointer
     * @param 	offenceCode						The offence code
     */
    private static void validateDisposals(ValidationHelper helper, Integer defendantOnCaseID, String offenceCode, String caseNumber, Integer courtId) {
    	if ( offenceCodesHome != null ){
    		XhbD20OffenceCodes offenceCodeInst = findNamedOffenceCode( offenceCode);
    		
    		if ( offenceCodeInst != null){
    			log.debug( String.format("Found offence code for code: %s", offenceCode));
    			
    			if ( offenceCodeInst.getMandDisq() != null && offenceCodeInst.getMandDisq().equals("Y")) {
    				if ( caseHome != null ){
    					Integer caseNumberInt = Integer.parseInt( caseNumber.substring(1) );
    					String caseType = caseNumber.substring(0,1);
    					
    					XhbCase thisCase = findThisCase( caseHome, caseNumberInt, caseType, courtId );
						
    					if ( thisCase != null ){
	        				try {
								DisposalValue[] disposalValues = getDisposalsForOffence(defendantOnCaseID, thisCase.getCaseId(), offenceCode);// getDisposalsForDefendantOnCase(defendantOnCaseID, thisCase.getCaseId());
								
								log.debug( String.format("Have %d disposals.", disposalValues.length));
								boolean hasDisOblg = checkForDisposal( disposalValues, new String[] { "DISOBLG" } );
								
								if ( !hasDisOblg){
									if ( caseType.equals("S") || caseType.equals("T")){
										//	Check for other codes (MITCIRC or SPECREA)
										if ( !checkForDisposal( disposalValues, new String[] { "MITCIRC", "SPECREA"})){
											helper.addLogicalProblem(MANDATORY_MESSAGE, new String[]{offenceCode});
										}
									}else if ( caseType.equals("A")){
										helper.addLogicalProblem(MANDATORY_MESSAGE, new String[]{offenceCode});
									}else{
										//@TODO raise an exception?
									}
								}
							} catch (Exception ex) {
								CSServices.getDefaultErrorHandler().handleError(ex, clazz);
							    throw new EJBException(ex);
							}
    					}
    				}
    			}
    		}
    	}
	}

    /**
     * Get the relevant disposals for the case.
     * 
     * @param defendantOnCaseID
     * @param caseId
     * 
     * @return	An array of DisposalValue
     */
    private static DisposalValue[] getDisposalsForDefendantOnCase(Integer defendantOnCaseID, Integer caseId) {
		ArrayList<DisposalValue> disposalsFound = new ArrayList<DisposalValue>();
		
    	try{
    		ArrayList  listOfDisposals = getDefendantOffenceDisposals(defendantOnCaseID);
    		
    		XhbDisposal2BasicValue[] returnedDisposalsCollDoC = XhbDisposal2BeanHelper2.findRSByDefendantOnCaseIdValue(defendantOnCaseID);
    		
    		// Now concatenate all the arrays into one
    		XhbDisposal2BasicValue[] tempDisposalsList = new XhbDisposal2BasicValue[listOfDisposals.size()];
    		tempDisposalsList = (XhbDisposal2BasicValue[]) listOfDisposals.toArray(tempDisposalsList);

    		XhbDisposal2BasicValue[] returnedDisposalsColl = concat(returnedDisposalsCollDoC, tempDisposalsList);
    		
    		for (int i = 0; i < returnedDisposalsColl.length; i++){
    			// Get the disposal
    			XhbDisposal2BasicValue thisDisposalRow = returnedDisposalsColl[i];

    			// Get the ref disposal
    			XhbRefDisposalTypeBasicValue thisRefDisposalType = XhbRefDisposalTypeBeanHelper2.findByPrimaryKeyValue(thisDisposalRow.getRefDisposalTypeId());
    			
    			// Get the disposal lines
    			XhbDisposalLineBasicValue[] disposalLines = XhbDisposalLineBeanHelper2.findByDisposal2IdValue(thisDisposalRow.getDisposal2Id());

    			// Get the ref disposal lines
    			XhbRefDisposalLineBasicValue[] refDisposalLines = new XhbRefDisposalLineBasicValue[disposalLines.length];
    			for (int j = 0; j < disposalLines.length; j++) {
    				XhbRefDisposalLineBasicValue thisRefDisposalLine = XhbRefDisposalLineBeanHelper2
    						.findByPrimaryKeyValue(disposalLines[j].getRefDisposalLineId());
    				refDisposalLines[j] = thisRefDisposalLine;
    			}
    			
    			ArrayList<Integer> crestOffenceSeqNos = new ArrayList<Integer>();
    			Integer crestOffenceSequenceNo = new Integer(0); // Default to 0
    			// Get the count number for this disposal (crest_offence_seq_no
    			// taken from xhb_offence)
    			
    			if (thisDisposalRow.getDisposal2Id() != null) {
    				log.debug("getDisposalsForDefendantOnCase: found a disposal row; about to get the count no");

    				// Get the defendant on offence
    				Integer defOnOffId = thisDisposalRow.getDefendantOnOffenceId();
    				
    				if (defOnOffId != null) {
    					// Get def on offence row to get the offence
    					log.debug("getDisposalsForDefendantOnCase: defendant on offence id=" + defOnOffId);
    					XhbDefendantOnOffence xdoo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defOnOffId);
    					
    					// Find crest_offence_seq_no for the offence
    					if (xdoo != null) {
    						log.debug("getDisposalsForDefendantOnCase: got the defendant on offence object");
    						XhbOffence xo = XhbOffenceBeanHelper2.findByPrimaryKey(xdoo.getOffenceId());
    						
    						if (xo != null) {
    							log.debug(
    									"getDisposalsForDefendantOnCase: got the offence object - about to check for a crestOffenceSeqNo");
    							crestOffenceSequenceNo = xo.getCrestOffenceSeqNo();
    							log.debug(
    									"getDisposalsForDefendantOnCase: crestOffenceSequenceNo=" + crestOffenceSequenceNo);
    							crestOffenceSeqNos.add(crestOffenceSequenceNo);
    						}
    					}
    				}

    				// Do the same for defendant on case and union results - ideally
    				// there will only be one result!
    				Integer defOnCaseId = thisDisposalRow.getDefendantOnCaseId();
    				
    				if (defOnCaseId != null) {
    					log.debug(
    							"getDisposalsForDefendantOnCase: (def on case route) defendant on case id=" + defOnCaseId);
    					// Get def on case row to get the case
    					XhbDefendantOnCase xdoc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId);
    					if (xdoc != null) {
    						crestOffenceSequenceNo = getDefOnCaseRow(crestOffenceSeqNos, crestOffenceSequenceNo, xdoc);
    					}
    				}
    			}
    			
    			// Loop round the resultset - hopefully only one row returned
    			// though! Taking the first result regardless and will warn about
    			// the existence of others
    			if (crestOffenceSeqNos != null && crestOffenceSeqNos.size() > 0) {
    				crestOffenceSequenceNo = getDisposalCrestResults(thisDisposalRow, crestOffenceSeqNos,
    						crestOffenceSequenceNo);
    			}

    			disposalsFound.add(new DisposalValue(thisDisposalRow, thisRefDisposalType, disposalLines, refDisposalLines,
    					crestOffenceSequenceNo));
    		}
    	}catch(Exception ex){
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
    	}
    	
		return disposalsFound.toArray(new DisposalValue[disposalsFound.size()]);
	}

	/**
	 * @param thisDisposalRow
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @return
	 */
	private static Integer getDisposalCrestResults(XhbDisposal2BasicValue thisDisposalRow,
			ArrayList<Integer> crestOffenceSeqNos, Integer crestOffenceSequenceNo) {
		if (crestOffenceSeqNos.size() > 1) {
			log.warn("More than one result found when finding the offence for a disposal2Id = "
					+ thisDisposalRow.getDisposal2Id());
			// Use the first result anyway
			if (crestOffenceSeqNos.get(0) == null) {
				crestOffenceSequenceNo = 0;
			} else {
				crestOffenceSequenceNo = crestOffenceSeqNos.get(0);
			}
		} else if (crestOffenceSeqNos.size() == 0) {
			log.warn("No results found when finding the offence for a disposal2Id = "
					+ thisDisposalRow.getDisposal2Id());
		} else {
			if (crestOffenceSeqNos.get(0) == null) {
				log.debug(
						"getDisposalCrestResults: one and only one crestoffenceseqno found; but value is null");
				crestOffenceSequenceNo = 0;
			} else {
				log.debug("getDisposalCrestResults: one and only one crestoffenceseqno found; value is "
						+ crestOffenceSeqNos.get(0));
				crestOffenceSequenceNo = crestOffenceSeqNos.get(0);
			}
		}
		return crestOffenceSequenceNo;
	}

	/**
	 * @param crestOffenceSeqNos
	 * @param crestOffenceSequenceNo
	 * @param xdoc
	 * @return
	 */
	private static Integer getDefOnCaseRow(ArrayList<Integer> crestOffenceSeqNos, Integer crestOffenceSequenceNo,
			XhbDefendantOnCase xdoc) {
		log.debug("getDefOnCaseRow: (def on case route) got the defendant on case object");
		
		try{
			// Get the case for this def on case
			XhbCase xc = caseHome.findByPrimaryKey(xdoc.getCaseId());
			
			if (xc != null) {
				log.debug("getDefOnCaseRow: (def on case route) got the case object for this defendant on case; caseid="
								+ xdoc.getCaseId());
				
				// Get the charge
				XhbCharge xch = null;
				try {
					xch = chargeHome.findByPrimaryKey(xc.getCaseId());
				} catch (ObjectNotFoundException ex) {
					// This is ok!
					log.debug("No charge found for case id: " + xc.getCaseId());
				}
				if (xch != null) {
					log.debug(
							"getDefOnCaseRow: (def on case route) got the charge object for the case; chargeid="
									+ xch.getChargeId());
					// Find crest_offence_seq_no for the offence
					XhbOffence xo = null;
					try {
						xo = offenceHome.findByPrimaryKey(xch.getChargeId());
					} catch (ObjectNotFoundException ex) {
						// This is ok!
						log.debug("No offence found for charge id: " + xch.getChargeId());
					}
					if (xo != null) {
						log.debug(
								"getDefOnCaseRow: got the offence object (def on case route) - about to check for a crestOffenceSeqNo");
						crestOffenceSequenceNo = xo.getCrestOffenceSeqNo();
						log.debug(
								"getDefOnCaseRow: (def on case route) crestOffenceSequenceNo="
										+ crestOffenceSequenceNo);
						crestOffenceSeqNos.add(crestOffenceSequenceNo);
					}
				}
			}
		}catch( FinderException ex){
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
		}
		
		return crestOffenceSequenceNo;
	}

	private static XhbDisposal2BasicValue[] concat(XhbDisposal2BasicValue[] arr1, XhbDisposal2BasicValue[] arr2) {
		int arr1Len = arr1.length;
		int arr2Len = arr2.length;
		XhbDisposal2BasicValue[] returnArr = new XhbDisposal2BasicValue[arr1Len + arr2Len];
		System.arraycopy(arr1, 0, returnArr, 0, arr1Len);
		System.arraycopy(arr2, 0, returnArr, arr1Len, arr2Len);

		return returnArr;
	}

	private static ArrayList getDefendantOffenceDisposals(Integer defendantOnCaseID) {
		ArrayList result = new ArrayList();;
		
		try{
			Collection selected = defendantOnOffenceHome.findByDefOnCaseId( defendantOnCaseID );
			
			Iterator iter = selected.iterator();
			while( iter.hasNext()){
				XhbDefendantOnOffenceBasicValue value = (((XhbDefendantOnOffence)iter.next()).getData());
				
				if ( value != null){
					Integer thisDoOId = value.getDefendantOnOffenceId(); 
					
					ArrayList disposals = (ArrayList) disposal2Home.findRSByDefendantOnOffenceId( thisDoOId );
		    		
		    		for (int j = 0; j < disposals.size(); j++){
		    			XhbDisposal2BasicValue thisDisposal = ((XhbDisposal2) disposals.get(j)).getData();
						
		    			if (thisDisposal != null) {
		    				result.add(thisDisposal);
						}
		    		}
				}
			}
		}
		catch( FinderException ex){
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
		}
		
		return result;
	}

	/**
     * Check for a DISOBLG disposal.
     * 
     * @param 	disposalValues				The list of Disposals
     * @param	codes						The list of codes to check
     * 	
     * @return	true if DISOBLG discovered
     */
	private static boolean checkForDisposal(DisposalValue[] disposalValues, String[] codes) {
		boolean outcome = false;			//	Assume not found
		List<String> codesList = Arrays.asList( codes );
		
		for( int index = 0; index < disposalValues.length && !outcome; index++){
			XhbRefDisposalTypeBasicValue value = disposalValues[index].getRefDisposalType();
			
			outcome = codesList.indexOf( value.getDisposalCode() ) >= 0;
		}
		
		return outcome;
	}

	/**
     * Find the case given the parameters
     * 
     * @param 	caseHome					The Case Home
     * @param 	caseNumber					The case number
     * @param 	caseType					The case type
     * @param 	courtId						The court ID
     * 
     * @return	The XhbCase instance (or null if no match)
     */
    private static XhbCase findThisCase(XhbCaseHome caseHome, Integer caseNumber, String caseType, Integer courtId) {
		XhbCase outcome = null;
		try{
			Collection cases = caseHome.findByCaseNumber( caseNumber );
			
			Iterator itr = cases.iterator();
			
			while( itr.hasNext() && outcome == null){
				XhbCase candidate = (XhbCase) itr.next();
				
				if ( candidate.getCaseNumber().equals( caseNumber ) && 
					 candidate.getCaseType().equals( caseType) && 
					 candidate.getCourtId().equals(courtId)){
					outcome = candidate;
				}
			}
		}catch( Exception ex){
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
		}

		return outcome;
	}

	/**
     * Find the named court
     * 
     * @param 	courtName				The court name
     * @return
     */
    private static Collection findNamedCourt(String courtName) {
		List<XhbCourt> result = new ArrayList<XhbCourt>();
        
        if ( courtHome != null ){
        	try {
				Collection courtHouses = courtHome.findAll();
				
				Iterator iter = courtHouses.iterator();
				
				while( iter.hasNext()){
					XhbCourt court = (XhbCourt)iter.next();
					
					if ( court != null && court.getCourtName().equals( courtName )){
						result.add( court );
					}
				}
			} catch (FinderException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, clazz);
			    throw new EJBException(ex);
			}
        }

		return result;
	}

	/**
     * Find the named offence code in the list of offence codes.
     * 
     * @param 	offenceCodesHome				The D20 Offence codes hoime (I hope)
     * @param 	offenceCode						The named offence code.
     * 
     * @return	An XhbD20OffenceCodes instance (or null if the code doesn't exist)
     */
    private static XhbD20OffenceCodes findNamedOffenceCode(String offenceCode) {
    	XhbD20OffenceCodes offenceCodeInst = null;
    	
    	try {
			Collection allCodes = offenceCodesHome.findAll();
			
			Iterator itr = allCodes.iterator();
			
			while( itr.hasNext() && offenceCodeInst == null){
				XhbD20OffenceCodes candidate = (XhbD20OffenceCodes) itr.next();
				
				if ( candidate.getOffenceCode().equals( offenceCode )){
					offenceCodeInst = candidate;
				}
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, clazz);
		    throw new EJBException(ex);
		}
    	
		return offenceCodeInst;
	}

	/**
     * Utility method to check that any elements whose value depends on other
     * elements are set correctly.
     * 
     * @param mos
     *            the D20 Order
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     * @return the validated xml
     * @throws ValidationException
     * @throws MarshalException
     * @throws OrderXMLException
     */
    public static String structuralValidateD20(D20OrderStructure mos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) throws ValidationException, MarshalException, OrderXMLException {
        log.debug("$$$ D20OrderHelper.structuralValidateD20Order");
        // Nothing to do here as all elements are independent
        return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
    }

    /**
     * Checks whether the data entered is in the list of offence codes
     * This check is not case sensitive
     * 
     * @return
     */
    private static boolean checkEntry(String dataEntered, String[] offenceCodes) {
    	if (dataEntered == null) {
    		return false;
    	}
    	
    	for (int i=0; i<offenceCodes.length; i++) {
    		if (dataEntered.equalsIgnoreCase(offenceCodes[i])) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private static boolean isInterimFinalValid(D20InterimFinalDropdown offenceInterimFinal) {
    	return (offenceInterimFinal.equals(D20InterimFinalDropdown.VALUE_0) || 
    			offenceInterimFinal.equals(D20InterimFinalDropdown.VALUE_1) ||
    			offenceInterimFinal.equals(D20InterimFinalDropdown.VALUE_2));
    }
    
    
    /**
     * Return true if this is an Interim D20
     * 
     * @param offenceInterimFinal
     * @return
     */
    private static boolean isInterim(D20InterimFinalDropdown offenceInterimFinal) {
    	boolean isInterim = false;
    	
    	if ((offenceInterimFinal != null) && (offenceInterimFinal.equals(D20InterimFinalDropdown.VALUE_0))) {
    		return true;
    	}
    	
    	return isInterim;    	
    }
}