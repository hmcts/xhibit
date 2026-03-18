package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DisposalControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonOrdDisposalsValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderInitialDataException;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderDefendantWrapper;
//import uk.gov.courtservice.xhibit.client.results.ResultsReferenceFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;


/**
 * <p>
 * Title: OrderInitialDataHelper
 * </p>
 * <p>
 * Description: Load the model from the framework. Utility class to populate the
 * initial data required by orders (defendant and order types, although
 * disposals may be a future requirement)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderInitialDataHelper {
    private static final Logger log = CSServices.getLogger(OrderInitialDataHelper.class);
    private static final String MAGISTRATE_COURT = "M"; 

    private XhibitApplicationController xacp = null;

    private OrderInitialDataVO model = null;

    private OrderDefendantWrapper[] defendants = null;

    // Stores the order type list
    private XhbOrderTypeValue[] orderTypeValues;

    // Stores a reference to the order types to display in the dropdown
    private String[] orderTypes = null;
    
    private HashMap disposalDataMap;

    private HashMap orderTypeMap;

    private HashMap defendantMap;

    private HashMap defendantOnCaseMap = new HashMap();
    
    /**
     * Constructor
     * 
     * @param xac   the controller
     */
    public OrderInitialDataHelper(XhibitApplicationController xac) {
        xacp = xac;
        
    }

    /**
     * Populates the OrderInitialDataVO
     * 
     * @param odm
     *            The OrderInitialDataVO to populate
     * @throws OrderInitialDataException
     */
    public void loadModel(boolean isMonetaryOrder, boolean isD20Order) throws OrderInitialDataException, CSRecoverableException {
        log.debug("OrderInitialDataHelper*** entering loadModel()");
        // If model is null, create a new one defaulting to VIEW mode
        if (this.model == null) {
            log.debug("OrderInitialDataHelper*** creating model");
            this.model = new OrderInitialDataVO();
            this.model.setMode(OrderInitialDataVO.VIEW_MODE);
            this.model.setHelper(this);
        }
        
        boolean isBCase = xacp.getApplicationCaseModel().getCaseType().equals("B");
        boolean isACase = xacp.getApplicationCaseModel().getCaseType().equals("A");
        boolean isTCase = xacp.getApplicationCaseModel().getCaseType().equals("T");
        boolean isSCase = xacp.getApplicationCaseModel().getCaseType().equals("S");
        this.model.setMonetaryOrder(isMonetaryOrder);
        this.model.setD20Order(isD20Order);
        this.model.setBCase(isBCase);
        this.model.setHearingID(xacp.getApplicationCaseModel().getScheduledHearingId().intValue());
        this.model.setACase(isACase);
        this.model.setTCase(isTCase);
        this.model.setSCase(isSCase);
        this.model.setCaseSubType(xacp.getApplicationCaseModel().getCaseSubType());
        this.model.setCaseTitle(xacp.getApplicationCaseModel().getCaseTitle());
        if (isMonetaryOrder) {
           loadModelWithDisposalData();
        }
        loadModelFromFramework(this.model);
        loadModelWithOrderTypes();
        log.debug("OrderInitialDataHelper*** leaving loadModel()");
    }

    /**
     * Populates the model from the Xhibit framework classes
     * 
     * @param odm
     * @throws OrderInitialDataException
     */
    public void loadModelFromFramework(OrderInitialDataVO model) throws OrderInitialDataException,
            CSRecoverableException {
        log.debug("ORDERS***: enterng loadModelFromFramework\n");
        if (xacp == null) {
            throw new CSRecoverableException("ORDERS_XXX", "XhibitApplicationController is null");
        }

        setCourtDetails(model, xacp.getApplicationCaseModel(), XhibitSingleton.getInstance());
        log.debug("ORDERS***: leaving loadModelFromFramework");
    }
    
    /**
     * Load the disposal data necessary for monetary orders
     * 
     * @throws OrderInitialDataException
     */
    public void loadModelWithDisposalData() throws OrderInitialDataException {
        log.debug("OrderInitialDataHelper*** entering " + "loadModelWithDisposalData()");
        // if disposals data has not been loaded then load it
        if (this.model.isMonetaryOrder()) { // Load it regardless if it has been loaded or not for monetary orders
        	setOrderDisposalData(getOrderDisposalData());
        } else if (getOrderDisposalData() == null) {
            setOrderDisposalData(getOrderDisposalData());
        }
        log.debug("OrderInitialDataHelper*** leaving " + "loadModelWithDisposalData()");
    }

    /**
     * 
     * @return
     */
    public boolean checkContainsAnyDO() {
    	boolean valid = !model.getOffenceValidationReturnValue().isNoOffences();
    	log.debug("checkContainsAnyDO() = "+(valid ? "True" : "False"));
    	return valid;
	}

    
    /**
     * Test that we have a case with this defendant id.
     * 
     * @param 	defendantId					The defendant ID
     */
    public List<OffenceValue> getOffencesForDefendant(final Integer defendantId){
		List<OffenceValue> offencesForDefendant = new ArrayList<OffenceValue>();
		try{
    		Integer caseId = xacp.getApplicationCaseModel().getCaseId();
    		Collection<ChargeValue> charges = XhibitDelegateHelper.getChargeDelegate().getChargesList(caseId);
    		
    		if ( charges != null && charges.size() > 0){
    			ChargeValue chargeValue = null;
    			Iterator<ChargeValue> cv = charges.iterator();
    			
    			while( cv.hasNext()){
    				chargeValue = cv.next();
    				
    				Collection<OffenceValue> offenceValues = chargeValue.getOffenceValues();
    				
    				if ( offenceValues != null && offenceValues.size() > 0 ){
    					OffenceValue thisOffence = null;
    					Iterator<OffenceValue> ov = offenceValues.iterator();
    					
    					while (ov.hasNext()){
    						thisOffence = ov.next();
    						Collection<Integer> defendantIds = thisOffence.getDefendantIDs();
    						
    						if ( defendantIds.contains( defendantId )){
    							offencesForDefendant.add( thisOffence );
    						}
    					}
    				}
    			}
    		}
    	} catch(ChargeControllerException ex) {
    		ex.printStackTrace();
    	}
    	
    	log.debug( String.format("Found %d offences for defendant: %d", offencesForDefendant.size(), defendantId));
    	
    	return offencesForDefendant;
    }
    
    /**
     * Method determines whether records have valid DVLA codes 
     * @return true if a DVLA offence / case has a DVLA disposal against it and if one of them has a DVLA disposal
     */
    public boolean checkCaseHasDVLA() {
    	boolean valid = !model.getOffenceValidationReturnValue().isNoDVLA();
    	log.debug("checkCaseHasDVLA() = "+(valid ? "True" : "False"));
    	return valid;
	}

    /**
     * Converts defendant value to defendant basic value
     * @param dv the defendant value to convert 
     * @return the converted Defendant value as Defendant Basic Value
     */
    public DefendantBasicValue convertToDBV(DefendantValue dv) {
        DefendantBasicValue dbv = new DefendantBasicValue();
        dbv.setId(dv.getDefendantID());
        dbv.setCourtID(dv.getCourtID());
        dbv.setFirstName(dv.getFirstName());
        dbv.setMiddleName(dv.getMiddleName());
        dbv.setSurname(dv.getSurName());
        dbv.setAddressID(dv.getAddressId());
        dbv.setGender(dv.getGender());
        dbv.setDateOfBirth(dv.getDateOfBirth());
        
        return dbv;
    }   
    
    /**
	 * Gets the current selected defendants ID
	 * @return the defendant's ID if it can be retrieved
	 */
	 public Integer getDefendantId() {
	        if (this.model.isBCase()) {
	            log.debug("OrderInitialDataHelper.getDefendantId: Getting defendantid for a b case");
	            // Need to get the defendantId
	            try {
	                Collection allDefdts = XhibitDelegateHelper.getCaseDelegate().getDefendants(this.model.getXhibitCaseId());
	                if (allDefdts != null) {
	                    for (Object currDef : allDefdts) {
	                        DefendantValue thisDef = (DefendantValue) currDef;
	                        if ((thisDef != null) && (thisDef.getSurName().equals(this.model.getDefendantName()))) {
	                            log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: "+thisDef.getDefendantID());
	                            return thisDef.getDefendantID();
	                        }
	                    }
	                }
	            } catch (DefendantControllerException dce) {
	                log.error("Error getting defendant for B case "+this.model.getXhibitCaseId());
	                dce.printStackTrace();
	            } catch (CaseControllerException cce) {
	                log.error("Error getting defendant for B case "+this.model.getXhibitCaseId());
	                cce.printStackTrace();
	            }
	        }

	        log.debug("OrderInitialDataHelper.getDefendantId: defendantid returned is: "+this.model.getDefendantID());
	        return this.model.getDefendantID();
	    }
	                 
    /**
     * Retrieves defendant on case ID for current defendant and case
     * @param defendantId -1 means we dont yet know the defendantid
     * @param caseId the case to search
     * @return the defendant on offence ID if it is found
     */
    public Integer getDefendantOnCaseId(Integer defendantId, Integer caseId){
    	
    	if(defendantId!=null) {
	        if (defendantId.intValue() == -1) { 
	            defendantId = getDefendantId();
	        }
    	}
    	
        if (this.model.isBCase() || this.model.getDefendantOnCaseID() == null) {
            if (defendantId != null) {
                try {
                    // Now need to get the defendant on case using defid an caseid
                    log.debug("OrderInitialDataHelper.getDefendantOnCaseId: Finding defoncase with defid="+defendantId+" and caseid="+caseId);
                    DefendantOnCaseValue docv = XhibitDelegateHelper.getDefendantDelegate().getDefendantOnCaseDetails(defendantId, caseId);
                    return docv.getDefendantOnCaseBVO().getId();
                } catch (DefendantControllerException dce) {
                    log.error("Error getting defendant for B case "+this.model.getXhibitCaseId());
                    dce.printStackTrace();
                }
            } else {
                log.debug("No defendant found for the B caseid: " + this.model.getXhibitCaseId());
            }
        } else {
            return this.model.getDefendantOnCaseID();
        }
        
        return null;
    }

	    		
    /**
     * Get the XhbOrderTypeValue objects as retrieved from the database
     * The logic is that for each defendant on case retrieve all disposal data that is monetary related, ignore other disposal data
     * 
     * @return An array of order type objects
     */
    public HashMap getOrderDisposalData() {
        log.debug("OrderInitialDataHelper*** entering getOrderDisposalData()");
        if (disposalDataMap != null && disposalDataMap.size() > 0) {
            return disposalDataMap;
        } else {
            // Get all defendants on this case
            Integer caseId = xacp.getApplicationCaseModel().getCaseId();
            try {
                // Get all defendants listed on this case
                ChargeCompositeValue ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseId, true);
                Collection<DefendantValue> allDefendants = ccv.getAllDefendants();
                
                // For each defendant:
                // a) populate a hashmap with the following information:
                //      Key = "surname, firstname middlename"
                //
                // b)   Value = new hashmap with the following:
                //      Key = "hasMonetaryDisposals"
                //      Value = "true" if this defendant has monetary disposal
                //      Value = "false" if this defendant does not have a monetary disposal
                //
                //      Key = "monetaryDisposalInfoForDisplayOnOrder"
                //      Value = ArrayList of information on monetary disposals
                //              
                //              Each ArrayList entry will consist of a HashMap
                //              Key = "monetaryDisposalsText"
                //              Value = if available, list of all monetary disposals text for display in the monetary order
                //              Value = null, if not available
                //
                //              Key = "parentPaid" (determined from looking at the disposal lines and where there is a ref_disposal_line_id for this check the value)
                //              Value = "Y" or "N" if known or null
                //
                //              Key = "amount" (determined from looking at the disposal lines and where there is a ref_disposal_line_id for this check the value)
                //              Value = amount if known or null
                //
                //              Key = "dateOfResult" (determined from looking at the disposal lines and where there is a ref_disposal_line_id for this check the value)
                //              Value = "Y" or "N" if known or null
                //				
                //
                //		Key = "amounts" (there are 4 categories of amounts that need to be totalled and sent to CREST: Fines, Compensation, Costs and Confiscation)
				// 		Value = "HashMap" : 4 entries with key of "FINE", "COMPENSATION", "COSTS" and "CONFISCATION" with the value being a monetary total.
                if ((allDefendants != null) && (allDefendants.size() > 0)) {
                    disposalDataMap = new HashMap();
                    DefendantValue thisDefendant = null;
                    Iterator<DefendantValue> dv = allDefendants.iterator();
                    HashMap defendantDisposalHashMap;
                    while (dv.hasNext()) {
                        defendantDisposalHashMap = new HashMap();
                        boolean foundMonetaryDisposalForThisDefendant = false;
                        thisDefendant = dv.next();
                        
                        DefendantBasicValue dbv = convertToDBV(thisDefendant);
                        // For each defendant on this case get all the disposals.                        
                        DisposalValue[] disposals = XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnCase(getDefendantOnCaseId(thisDefendant.getDefendantID(), caseId), caseId);
                        
                        if (disposals != null && disposals.length > 0) {
                            // Filter disposals to remove all non-monetary related disposals                    
                            log.debug("ORDER****: Num monetary order disposals returned = " + disposals.length);
                            StringBuffer monetaryDisposalsText = new StringBuffer();
                            //monetaryDisposalsText.append("\n\n"); // Need 2 new lines at the top
                            ArrayList monetaryDisposalInfoForDisplayOnOrder = new ArrayList();
                            HashMap runningTotals = new HashMap();
                            for (int i=0; i<disposals.length; i++) {
                            	
                            	Collection<RefMonOrdDisposalsValue> c = XhibitDelegateHelper.getBizRefDelegate().findRefMonOrderByDisposalCode(disposals[i].getRefDisposalType().getDisposalCode());
                            	if (!c.isEmpty()) { // i.e. this is a monetary disposal
                            		
                                    String courtType = disposals[i].getCourtType();
                                    log.info("courtType of case is " + courtType);
                                    if (courtType.equals(MAGISTRATE_COURT)) {
                                    	log.info("courtType is Magistrate, don't add disposal");
                                    	// Court type is Magistrate so don't add this disposal
                                    	// Means that only Crown Court disposals are shown on Appeal Monetary orders
                                    	continue;
                                    }
                            		
                                    HashMap thisDisposalDisplayInfo = new HashMap();
                                    // We should have all details necessary to create the offence text
                                    boolean parentPaid = didParentPay(disposals[i].getDisposalLines(), disposals[i].getRefDisposalLines());
                                    thisDisposalDisplayInfo.put("parentPaid", parentPaid);
                                    String amount = getDisposalAmount(disposals[i].getDisposalLines(), disposals[i].getRefDisposalLines());
                                    thisDisposalDisplayInfo.put("amount", amount);
                                    String dateOfResult = getDateOfResult(disposals[i].getDisposalLines(), disposals[i].getRefDisposalLines());
                                    thisDisposalDisplayInfo.put("dateOfResult", dateOfResult);
                                    String countNo = disposals[i].getCrestOffenceSequenceNo()+"";
                                    thisDisposalDisplayInfo.put("countNo", countNo);
                                    
                                    String offenceCode = getOffenceCode(disposals[i].getDefendantOnCaseId(), disposals[i].getDefendantOnOffenceId());
                                    String offenceDesc = getOffenceDesc(disposals[i].getDefendantOnCaseId(), disposals[i].getDefendantOnOffenceId());
                                    String recipientName = getDisposalRecipientName(disposals[i].getDisposalLines(), disposals[i].getRefDisposalLines());;
                                    String recipientAddress = getDisposalRecipientAddress(disposals[i].getDisposalLines(), disposals[i].getRefDisposalLines());;;

                                    monetaryDisposalsText.append(getDisplayableDisposalLine(disposals[i], parentPaid, amount, dateOfResult, countNo, offenceCode, offenceDesc, recipientName, recipientAddress));
                                    thisDisposalDisplayInfo.put("disposalDisplayText", monetaryDisposalsText.toString());
                                    
                                    foundMonetaryDisposalForThisDefendant = true;
                                    monetaryDisposalInfoForDisplayOnOrder.add(thisDisposalDisplayInfo);
                                    
                                    String moType = "";
                                    // The collection of "RefMonOrdDisposalsValue" should have only 1 element; get the MO_TYPE
                                    for (Iterator<RefMonOrdDisposalsValue> it = c.iterator(); it.hasNext();) {
                                    	RefMonOrdDisposalsValue thisRow = it.next();
                                    	moType = thisRow.getMOType();
                                    }
                                    runningTotals = addToRunningTotals(amount, moType, runningTotals);
                                }
                            }
                            // Add HashMap of all data we have about each disposal
                            defendantDisposalHashMap.put("monetaryDisposalFullInfoForDisplayOnOrder", monetaryDisposalInfoForDisplayOnOrder);
                            defendantDisposalHashMap.put("monetaryDisposalTotals", runningTotals);
                            

                            // Add concatenated string(s) of order data to be displayed on monetary order
                            defendantDisposalHashMap.put("monetaryDisposalsDisplayOnOrder", monetaryDisposalsText.toString());
                            defendantDisposalHashMap.put("monetaryTotalsDisplayOnOrder", getDisplayableTotals(runningTotals));
                        }
                        
                        if (!foundMonetaryDisposalForThisDefendant) {
                            defendantDisposalHashMap.put("hasMonetaryDisposals", false);
                        } else {
                            defendantDisposalHashMap.put("hasMonetaryDisposals", true);
                        }
                        disposalDataMap.put(new OrderDefendantWrapper(dbv).toString(), defendantDisposalHashMap);
                    }
                }
            } catch (DisposalControllerException dce) {
                dce.printStackTrace();
            } catch (ChargeControllerException cce) {
                cce.printStackTrace();
            }
            
            log.debug("OrderInitialDataHelper*** leaving getOrderDisposalData()");
            return disposalDataMap;
        }
    }
    
    /**
     * Adjust the running totals necessary in monetary orders based on the amount passed in and the disposal code associated.
     * Each (monetary order) disposal code is associated with either Fines, Compensation, Costs or Confiscation as per the table
     * xhb_ref_mon_ord_disposals
     * @param amount
     * @param disposalCode
     * @param currentTotals
     * @return
     */
    private HashMap addToRunningTotals(String amount, String moType, HashMap currentTotals) {
    	
    	// First convert the amount to an int, if possible
    	double dAmountToBeAdded = 0;
    	try {
    		
    		dAmountToBeAdded = new Double(amount).doubleValue();
    		
    		double dRunningAmount = 0.0;
    		String sRunningAmount = "";
    		if (currentTotals.get(moType) != null) {
    			sRunningAmount = currentTotals.get(moType).toString();
    		
    			if (sRunningAmount.length() > 0)
    				dRunningAmount = new Double(sRunningAmount).doubleValue();
    		}
    		
    		double newAmount = dRunningAmount + dAmountToBeAdded;
        	
        	// Round to 2 dec places
        	DecimalFormat df = new DecimalFormat("#####0.00");
        	df.setRoundingMode(RoundingMode.HALF_UP);
        	String sNewAmount = df.format(newAmount);
        	
        	currentTotals.put(moType, sNewAmount);
        	
    	} catch (NumberFormatException nfe) {
    		log.error("The amount " + amount + " associated with this disposal is non-numeric so running totals for monetary order cannot be updated");
    	} finally {
    		// Can't add this amount so just return totals as is - not fatal
    		return currentTotals;
    	}
    }
    
    /**
     * 
     * @return
     */
    @SuppressWarnings("finally")
    private boolean didParentPay(XhbDisposalLineBasicValue[] xhbdlbv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        boolean parentPaid = false;
        try {
            // Loop through ref disposal lines and find if there's a "Parent Paid"
            for (int i=0; i< xhbrdlbv.length; i++) {
                XhbRefDisposalLineBasicValue thisRefDisposalLine = xhbrdlbv[i];
                if (thisRefDisposalLine.getPrompt() != null && thisRefDisposalLine.getPrompt().trim().equalsIgnoreCase("Parent Paid?")) {
                    // Now need to find out whether parent paid or not
                    for (int j=0; j<xhbdlbv.length; j++) {
                        XhbDisposalLineBasicValue thisDisposalLine = xhbdlbv[j];
                        if (thisDisposalLine.getRefDisposalLineId().equals(thisRefDisposalLine.getRefDisposalLineId())) {
                            if (thisDisposalLine.getLineData().trim().equalsIgnoreCase("Y")) {
                                // Parent Paid
                                parentPaid = true;
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("One of the elements is null when trying to determine if the parent paid.");
            npe.printStackTrace();
        } finally {
            return parentPaid;
        }
    }
    
    /**
     * 
     * @param xhbdlbv
     * @param xhbrdlbv
     * @return
     */
    @SuppressWarnings("finally")
    private String getDisposalAmount(XhbDisposalLineBasicValue[] xhbdlbv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        String amount = "N/A";
        
        try {
            // Loop through ref disposal lines and find if there's an "Amount"
            for (int i=0; i< xhbrdlbv.length; i++) {
                XhbRefDisposalLineBasicValue thisRefDisposalLine = xhbrdlbv[i];
                if (thisRefDisposalLine.getPrompt() != null && thisRefDisposalLine.getPrompt().trim().toUpperCase().indexOf("AMOUNT") >= 0) {
                    // Now need to find out the amount
                    for (int j=0; j<xhbdlbv.length; j++) {
                        XhbDisposalLineBasicValue thisDisposalLine = xhbdlbv[j];
                        if (thisDisposalLine.getRefDisposalLineId().equals(thisRefDisposalLine.getRefDisposalLineId())) {
                            // Amount found
                            amount = thisDisposalLine.getLineData().trim();
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("One of the elements is null when trying to determine the amount.");
            npe.printStackTrace();
        } finally {
            return amount;
        }
    }
    
    /**
     * 
     * @param xhbdlbv
     * @param xhbrdlbv
     * @return
     */
    @SuppressWarnings("finally")
    private String getDateOfResult(XhbDisposalLineBasicValue[] xhbdlbv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        String dateOfResult = "N/A";
        
        try {
            // Loop through ref disposal lines and find if there's an "Date of result"
            for (int i=0; i< xhbrdlbv.length; i++) {
                XhbRefDisposalLineBasicValue thisRefDisposalLine = xhbrdlbv[i];
                if (thisRefDisposalLine.getPrompt() != null && thisRefDisposalLine.getPrompt().trim().toUpperCase().indexOf("DATE OF RESULT") >= 0) {
                    // Now need to find out the amount
                    for (int j=0; j<xhbdlbv.length; j++) {
                        XhbDisposalLineBasicValue thisDisposalLine = xhbdlbv[j];
                        if (thisDisposalLine.getRefDisposalLineId().equals(thisRefDisposalLine.getRefDisposalLineId())) {
                            // Amount found
                            dateOfResult = thisDisposalLine.getLineData().trim();
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("One of the elements is null when trying to determine the date of result.");
            npe.printStackTrace();
        } finally {
            return dateOfResult;
        }
    }
    
    /**
     * 
     * @param xhbdlbv
     * @param xhbrdlbv
     * @return
     */
    @SuppressWarnings("finally")
    private String getDisposalRecipientName(XhbDisposalLineBasicValue[] xhbdlbv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        String recipientName = "";
        
        try {
            // Loop through ref disposal lines and find if there's an "RECIPIENT NAME"
            for (int i=0; i< xhbrdlbv.length; i++) {
                XhbRefDisposalLineBasicValue thisRefDisposalLine = xhbrdlbv[i];
                if (thisRefDisposalLine.getPrompt() != null && thisRefDisposalLine.getPrompt().trim().toUpperCase().indexOf("RECIPIENT NAME") >= 0) {
                    // Now need to find out the recipient name
                    for (int j=0; j<xhbdlbv.length; j++) {
                        XhbDisposalLineBasicValue thisDisposalLine = xhbdlbv[j];
                        if (thisDisposalLine.getRefDisposalLineId().equals(thisRefDisposalLine.getRefDisposalLineId())) {
                            // Amount found
                        	recipientName = thisDisposalLine.getLineData().trim();
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("One of the elements is null when trying to determine the recipient name.");
            npe.printStackTrace();
        } finally {
            return recipientName;
        }
    }
    
    /**
     * 
     * @param xhbdlbv
     * @param xhbrdlbv
     * @return
     */
    @SuppressWarnings("finally")
    private String getDisposalRecipientAddress(XhbDisposalLineBasicValue[] xhbdlbv, XhbRefDisposalLineBasicValue[] xhbrdlbv) {
        String recipientAddress = "";
        
        try {
            // Loop through ref disposal lines and find if there's an "ADDRESS"
            for (int i=0; i< xhbrdlbv.length; i++) {
                XhbRefDisposalLineBasicValue thisRefDisposalLine = xhbrdlbv[i];
                if (thisRefDisposalLine.getPrompt() != null && thisRefDisposalLine.getPrompt().trim().toUpperCase().indexOf("ADDRESS") >= 0) {
                    // Now need to find out the recipient address
                    for (int j=0; j<xhbdlbv.length; j++) {
                        XhbDisposalLineBasicValue thisDisposalLine = xhbdlbv[j];
                        if (thisDisposalLine.getRefDisposalLineId().equals(thisRefDisposalLine.getRefDisposalLineId())) {
                            // Amount found
                        	recipientAddress = thisDisposalLine.getLineData().trim();
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("One of the elements is null when trying to determine the recipient address.");
            npe.printStackTrace();
        } finally {
            return recipientAddress;
        }
    }
    
    /**
     * 
     * @param defOnCaseId
     * @param defOnOffId
     * @return
     */
    @SuppressWarnings("finally")
    private String getOffenceCode(Integer defOnCaseId, Integer defOnOffId) {
    	String offenceCode = "";
    	
    	try {
    		// Try and get the offence code using the def on offence id

    		if (defOnOffId != null) {
    			// Get the offence code
    			offenceCode = XhibitDelegateHelper.getDefendantDelegate().getOffenceForDefendantOnOffence("code", defOnOffId);
    		} else if (defOnCaseId != null) {
    			offenceCode = "Unrelated";
    		}

    	} catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("Cannot determine the offence code:");
            if (defOnCaseId != null) {
            	log.error("::defOnCaseId="+defOnCaseId.intValue());
            } else {
            	log.error("::defOnCaseId=null");
            }
            if (defOnOffId != null) {
            	log.error("::defOnOffId="+defOnOffId.intValue());
            } else {
            	log.error("::defOnOffId=null");
            }
            npe.printStackTrace();
        } finally {
            return offenceCode;
        }
    }
    
    /**
     * 
     * @param defOnCaseId
     * @param defOnOffId
     * @return
     */
    @SuppressWarnings("finally")
    private String getOffenceDesc(Integer defOnCaseId, Integer defOnOffId) {
    	String offenceCode = "";
    	
    	try {
    		// Try and get the offence desc using the def on offence id

    		if (defOnOffId != null) {
    			// Get the offence code
    			offenceCode = XhibitDelegateHelper.getDefendantDelegate().getOffenceForDefendantOnOffence("desc", defOnOffId);
    		} else if (defOnCaseId != null) {
    			offenceCode = "";
    		}

    	} catch (NullPointerException npe) {  // Not a show stopper but should be logged
            log.error("Cannot determine the offence description:");
            if (defOnCaseId != null) {
            	log.error("::defOnCaseId="+defOnCaseId.intValue());
            } else {
            	log.error("::defOnCaseId=null");
            }
            if (defOnOffId != null) {
            	log.error("::defOnOffId="+defOnOffId.intValue());
            } else {
            	log.error("::defOnOffId=null");
            }
            npe.printStackTrace();
        } finally {
            return offenceCode;
        }
    }
    
    /**
     * Populate a disposal row that is to be displayed for a monetary order based on the disposal
     * @param thisDisposal
     * @return
     */
    private String getDisplayableDisposalLine(DisposalValue thisDisposal, boolean parentPaid, String amount, String dateOfResult, String countNo, String offenceCode, String offenceDesc, String recipientName, String recipientAddress) {
        StringBuffer returnLine = new StringBuffer();
        
        
        // 1. Display the Count No that this relates to (if any)
        if (countNo != null && countNo.length() > 0) {
        	try {
        		if (countNo.equals("0")) {
        			returnLine.append("Count No: N/A; ");
        		} else {
        			returnLine.append("Count No: "+ countNo+"; ");
        		}
        	} catch (Exception e) {
        		// Just make sure we don't bomb out trivially here but log the exception
        		log.error("getDisplayableDisposalLine::Error with countNo="+countNo);
        		e.printStackTrace();
        	}
        }
        
        // 1. Get the offence code, offence desc and the ref disposal type text
        returnLine.append(offenceCode + " - " + offenceDesc +";\n" + thisDisposal.getRefDisposalType().getTitle());
        
        // 2. Parent paid
        if (parentPaid) {
            returnLine.append("; Parent paid");
        }
        
        // 3. Amount and 4. Date of Result
        if (recipientName.length() > 0) {
        	returnLine.append("; Amount: £"+ amount + " to\n");
        	returnLine.append(recipientName + ", " + recipientAddress + ";\n");
        	returnLine.append("Date of result: "+ dateOfResult +";\n\n");
        } else {
        	returnLine.append("; Amount: £"+ amount);
        	returnLine.append("; Date of result: "+ dateOfResult +";\n\n");
        }
        
        return returnLine.toString();
    }
    
    /**
     * Populate the data for displaying the totals on the monetary orders
     * @param totals
     * @return
     */
    private String getDisplayableTotals(HashMap totals) {
    	StringBuffer retStr = new StringBuffer();

    	// FINE
    	retStr.append("Fine:          ");
    	if (totals.get("FINE") != null) {
    		retStr.append("&#xA3;"+totals.get("FINE")+"\n");
    	} else {
    		retStr.append("\n");
    	}
    	
    	// COMPENSATION
    	retStr.append("Compensation:  ");
    	if (totals.get("COMPENSATION") != null) {
    		retStr.append("£"+totals.get("COMPENSATION")+"\n");
    	} else {
    		retStr.append("\n");
    	}
    	
    	// COSTS
    	retStr.append("Costs:         ");
    	if (totals.get("COSTS") != null) {
    		retStr.append("£"+totals.get("COSTS")+"\n");
    	} else {
    		retStr.append("\n");
    	}
    	
    	// CONFISCATION
    	retStr.append("Confiscation:  ");
    	if (totals.get("CONFISCATION") != null) {
    		retStr.append("£"+totals.get("CONFISCATION")+"\n");
    	} else {
    		retStr.append("\n");
    	}
    	
    	return retStr.toString();    	
    }
    
        

    
    
    /**
     * Sets the court details on the model. These are used to display on the
     * summary panel on the orders wizard
     * 
     * @param odm
     *            the model
     * @param acm
     *            the application case model
     * @param xSingleton
     *            XhibitSingleton
     * @throws CSRecoverableException
     */
    private void setCourtDetails(OrderInitialDataVO ordModel, ApplicationCaseModel appModel, XhibitSingleton xSingleton)
            throws CSRecoverableException {
        // set the case id on the model
        ordModel.setCaseID(appModel.getCaseType() + appModel.getCaseNumber());
        ordModel.setXhibitCaseId(appModel.getCaseId());
        ordModel.setScheduledHearingId( appModel.getScheduledHearingId());
        log.debug("ORDERS***: CaseID: " + appModel.getCaseType() + appModel.getCaseNumber());

        try {
            // Set the court id on the model
            ordModel.setCrestCourtID(Integer.parseInt(xSingleton.getCourtBasicValue().getCrestCourtId()));
            ordModel.setXhibitCourtId(xSingleton.getCourtBasicValue().getId());
            log.debug("ORDERS***: Court ID: " + xSingleton.getCourtBasicValue().getCrestCourtId());
        } catch (NumberFormatException nfe) {
            // Continue as Court ID is display only
            log.debug("ORDERS***: Crest Court ID is not numeric: " + xSingleton.getCourtBasicValue().getCrestCourtId());
            throw new CSRecoverableException("ORDERS_XXX", "Crest Court ID is not numeric", nfe);
        }

        // set the court name on the model
        ordModel.setCourtName(xSingleton.getCourtBasicValue().getCourtName());
        log.debug("ORDERS***: Court Name: " + xSingleton.getCourtBasicValue().getCourtName());
    }

    /**
     * Load the order types
     * 
     * @throws OrderInitialDataException
     */
    public void loadModelWithOrderTypes() throws OrderInitialDataException {
        log.debug("OrderInitialDataHelper*** entering " + "loadModelWithOrderTypes()");
        // if Order types have not been loaded, load them
        if (getOrderTypes() == null) {
            setOrderTypes(getOrderTypes());
        }
        log.debug("OrderInitialDataHelper*** leaving " + "loadModelWithOrderTypes()");
    }

    /**
     * Creates a list of defendants for the case
     */
    private void setDefendantListForCase() {
        // get all defendants on the case
        

        /** RFS4417 - 2015. Need to cater for B cases now for Bail Orders. Therefore need to get defendant data
         *  populated whilst it normally wouldnt as there is no defendant on case. 
         *  Assuming only 1 "defendant" for a B case which is the case title.
         */
        if (xacp.getApplicationCaseModel().getCaseType().equals("B")) {
            
            log.debug("ORDER***: setDefendantListForCase (B case)");
    
            int index = 0;
            int collSize = 1; // Just the 1 defendant which is the Case Title
    
            defendants = new OrderDefendantWrapper[collSize];
            DefendantBasicValue dbv = new DefendantBasicValue();
            dbv.setSurname(xacp.getApplicationCaseModel().getCaseTitle());
            
            // Now populate dbv with dummy values
            dbv.setFirstName("");
            dbv.setAddressID(null);
            dbv.setCourtID(null);
            
            // Populate the defendant array
            for (int i = index; i < collSize; i++) {
                defendants[0] = new OrderDefendantWrapper(dbv);
            }

        } else {
            Collection collection = xacp.getApplicationCaseModel().getScheduledHearingValue().getDefendantsOnCase();

            Iterator iter = collection.iterator();
    
            log.debug("ORDER***: setDefendantListForCase: Defendant Collection size: " + collection.size());
    
            int index = 0;
            int collSize = collection.size();
    
            // If more than 1 defendant for the case, need to insert a blank
            // defendant into the array
            if (collection.size() > 1) {
                collSize++;
                index++;
            }
            defendants = new OrderDefendantWrapper[collSize];
            // if there is more than one defendant, create an empty 'dummy'
            // defendant as the first in the list
            if (collSize > 1) {
                defendants[0] = getDummyDefendant();
            }
    
            // Populate the defendant array
            for (int i = index; i < collSize; i++) {
                defendants[i] = new OrderDefendantWrapper((DefendantBasicValue) iter.next());
            }
        }
        
        // load the defendants into a hashmap so that we can look up the
        // correct ids based on the name
        populateDefendantMap();
    }

    /**
     * Populates a HashMap to hold the defendantoncasevalues. These are required
     * to supply the correct id to the middle tier
     */
    private void setDefendantOnCaseValues() {
        Collection collection = xacp.getApplicationCaseModel().getScheduledHearingValue()
                .getDefendantOnCaseBasicValues();
        Iterator iter = collection.iterator();

        log.debug("ORDER***: setDefendantOnCaseValues: Defendant Collection size: " + collection.size());

        defendantOnCaseMap.clear();
        while (iter.hasNext()) {
            DefendantOnCaseBasicValue defOnCase = (DefendantOnCaseBasicValue) iter.next();
            log.debug("$$$ defOnCase.toString(): " + defOnCase + " $$$");
            defendantOnCaseMap.put(defOnCase.getDefendantID(), defOnCase);
        }
    }

    /**
     * Return the list of defendants The Defendant On Case Id is passed through
     * to the middle tier, but the Def On Case objects do not hold the defendant
     * name. We have to go through a convoluted process to match up the names
     * with the ids
     * 
     * @return the defendants
     */
    public OrderDefendantWrapper[] getDefendants() {
        // set the DefendantBasicValues
        setDefendantListForCase();
        // set the DefendantOnCaseBasicValues
        setDefendantOnCaseValues();
        return defendants;
    }

    /**
     * Returns the defendant_on_case_id given the defendant name
     * 
     * @param key
     *            The defendant name
     * @return The defendant_on_case_id used to create the orders
     */
    public Integer getDefendantOnCaseID(String key) {
        Integer defOnCaseId = null;
        OrderDefendantWrapper defWrapper = (OrderDefendantWrapper) defendantMap.get(key);
        log.debug("$$$ getDefendantOnCaseID key " + key + " $$$");
        log.debug("$$$ (OrderDefendantWrapper)defendantMap.get(key)) " + (OrderDefendantWrapper) defendantMap.get(key)
                + " $$$");
        // If the defendant can be found return the id, otherwise return null
        if ((OrderDefendantWrapper) defendantMap.get(key) != null) {
            DefendantOnCaseBasicValue defOnCase = (DefendantOnCaseBasicValue) defendantOnCaseMap.get(defWrapper.getDefendant().getId());
            if (defOnCase != null) {
                defOnCaseId = defOnCase.getId();
            }
        } else {
            log.debug("<<<<<<<<>>>>>>> DEFENDANT " + key + " NOT FOUND <<<<<<<>>>>>>>");
            Iterator defs = defendantMap.values().iterator();
            while (defs.hasNext()) {
                log.debug("<<<<<>>>>> DEFENDANT MAP " + (OrderDefendantWrapper) defs.next());
            }
        }
        return defOnCaseId;
    }

    /**
     * Get the XhbOrderTypeValue objects as retrieved from the database
     * 
     * @return An array of order type objects
     */
    public XhbOrderTypeValue[] getOrderTypes() {
        log.debug("OrderInitialDataHelper*** entering getOrderTypes()");
        if (orderTypeValues != null && orderTypeValues.length > 0) {
            return orderTypeValues;
        } else {
            orderTypeValues = XhibitDelegateHelper.getOrdersDelegate().getOrderTypes();
            
            log.debug("getOrderTypes****: OrderTypes Returned = " + orderTypeValues.length);
            orderTypeMap = new HashMap();
            populateOrderTypeMap();
            if (this.model.isMonetaryOrder()) {
                this.model.setOrderType("Monetary Order");
                this.model.setOrderID("Monetary Order");
            } else if (this.model.isD20Order()) {
                this.model.setOrderType("D20 Order");
                this.model.setOrderID("D20");
            }  else if (this.xacp.getApplicationCaseModel().getCaseType().equals("B")) {
                this.model.setOrderType("Bail Conditions");
                this.model.setOrderID("Bail Conditions");
            }

            log.debug("OrderInitialDataHelper*** leaving getOrderTypes()");
            return orderTypeValues;
        }
    }

    /**
     * Return the order types
     * 
     * @param type
     *            true for all order types
     * @return array of order types
     */
    public String[] getOrderTypes(boolean type) {
        if (orderTypes == null) {
            if (type == true) {
                orderTypes = new String[getOrderTypes().length];
                for (int i = 0; i < orderTypes.length; i++) {
                    orderTypes[i] = getOrderTypes()[i].getDescription();
                }
            } else {
                orderTypes = new String[getOrderTypes().length];
                for (int i = 0; i < orderTypes.length; i++) {
                    orderTypes[i] = getOrderTypes()[i].getDescription();
                }
            }
            java.util.Arrays.sort(orderTypes);
        }
        return orderTypes;
    }

    /**
     * Return the current (not obselete) order types
     * 
     * @return array of order types
     */
     public String[] getCurrentOrderTypes() 
     {
         XhbOrderTemplateValue[] validTemplates = XhibitDelegateHelper.getOrdersDelegate().getValidTemplates();
         
         ArrayList <String> currentOrderList = new ArrayList <String> ();
         
         for (int i = 0; i < validTemplates.length; i++) 
         {
             //if the OBS_IND is y/Y ignore the entry
             if ( validTemplates[i].getObsInd() == null || !validTemplates[i].getObsInd().equalsIgnoreCase("Y"))
             {
                 // if its a Monetary Order or a D20 then also ignore it, i.e. do not display on drop down list
                 if ((validTemplates[i].getXhbOrderType() != null) && (!validTemplates[i].getXhbOrderType().getCode().equals("MO"))
                         && (!validTemplates[i].getXhbOrderType().getCode().equals("D20")) && (!validTemplates[i].getXhbOrderType().getCode().equals("BCBCase")))
                     currentOrderList.add(validTemplates[i].getXhbOrderType().getDescription());
             } 
         }
         
         String[] a = new String[1];
         String[] currentOrderType = currentOrderList.toArray(a);
         
         return currentOrderType;
    }
    
    
    
    /**
     * Populate the HashMap to hold the Order Types
     * ensure to remove Monetary Orders from the list as they are not shown on the drop-down for normal orders
     */
    private void populateOrderTypeMap() {
        for (int i = 0; i < orderTypeValues.length; i++) {
            String thisOrderTypeCode = orderTypeValues[i].getCode(); 
            orderTypeMap.put(thisOrderTypeCode, orderTypeValues[i]);
        }
    }

    /**
     * Populate the HashMap to hold the defendants
     */
    private void populateDefendantMap() {
        defendantMap = new HashMap();
        log.debug("$$$ defendants.length: " + defendants.length + " $$$");
        for (int i = 0; i < defendants.length; i++) {
            log.debug("$$$ i: " + i + " $$$");
            log.debug("$$$ defendants[i].toString(): " + defendants[i] + " $$$");
            defendantMap.put(defendants[i].toString(), defendants[i]);
        }
    }

    /**
     * Returns a particular Order Type
     * 
     * @param key
     *            The Order Type to retrieve
     * @return XhbOrderTypeValue
     */
    public XhbOrderTypeValue getOrderType(String key) {
        log.debug("Getting order type : " + key);
        String orderTypeCode = getOrderTypeCode(key);
        return (XhbOrderTypeValue) orderTypeMap.get(orderTypeCode);
    }
    
    public String getOrderTypeCode(String orderText) {
        log.debug("Getting order code: " + orderText);
        String orderTypeCode = "Not found";
        for (int i = 0; i<orderTypeValues.length; i++) {
            if (orderTypeValues[i] != null && ((XhbOrderTypeValue) orderTypeValues[i]).getDescription().equals(orderText)) {
                return ((XhbOrderTypeValue) orderTypeValues[i]).getCode();
            }
        }
        return orderTypeCode;
    }

    /**
     * Returns the Orders for the current defendant
     * 
     * @return XhbOrderValue[]
     */
    public XhbOrderValue[] getOrdersForDefendant() {

    	Integer defendantOnCaseId = 0;
    	
    	if (model.isD20Order() && model.getMode() == OrderInitialDataVO.CREATE_MODE) {
    		defendantOnCaseId = model.getOffenceValidationReturnValue().getDefendantOnCase();
    	} else {
    		defendantOnCaseId = getDefendantOnCaseId(new Integer(-1), this.model.getXhibitCaseId());
    	}
    	
        return XhibitDelegateHelper.getOrdersDelegate().getOrdersForDefendantOnCase(defendantOnCaseId);
    }
    
    
    /**
     * Way more complex than it needs to be.
     * Could setup a new call to midtier but decided to just strip out the unwanted orders after retrieval
     * However as orders are retrieved into an array and removing from an array is not trivial the solution is array -> ArrayList -> array
     * @return
     */
    public XhbOrderValue[] getNonObsOrdersForDefendant() {
        Integer defendantOnCaseId = 0;
        
        if (model.isD20Order() && model.getMode() == OrderInitialDataVO.CREATE_MODE) {
        	defendantOnCaseId = model.getOffenceValidationReturnValue().getDefendantOnCase();
        } else {
        	defendantOnCaseId = getDefendantOnCaseId(new Integer(-1), this.model.getXhibitCaseId());
        }
        
        XhbOrderValue[] tempOrders = XhibitDelegateHelper.getOrdersDelegate().getOrdersForDefendantOnCase(defendantOnCaseId);
        ArrayList arrOrders = new ArrayList();
        
        // Strip out any with obsolete order templates
        for (int i=0; i<tempOrders.length; i++) {
            XhbOrderValue ti = tempOrders[i];
            if (
                    ((ti.getXhbOrderTemplate() != null) && (ti.getXhbOrderTemplate().getObsInd() == null)) ||
                     !(ti.getXhbOrderTemplate().getObsInd().equals("Y"))
               ) {
                arrOrders.add(ti);
            }
        }
        
        // Now repopulate new array from ArrayList now minus the orders we dont want 
        XhbOrderValue[] nonObsOrders = new XhbOrderValue[arrOrders.size()];
        for (int i=0; i<arrOrders.size(); i++) {
            nonObsOrders[i] = (XhbOrderValue) arrOrders.get(i);
        }
        
        return nonObsOrders;
    }

    /**
     * Returns the Orders for the current defendant
     * 
     * @return XhbOrderValue[]
     */
    public XhbOrderValue[] getReplaceableOrdersForDefendantAndOrderType() {
        log.debug("Start - getReplaceableOrdersForDefendantAndOrderType");
        log.debug("Defendant ID: " + this.model.getDefendantID());
        log.debug("Defendant On Case ID: " + this.model.getDefendantOnCaseID());
        log.debug("Order Type: " + this.model.getOrderTypeID());

        XhbOrderValue[] values = XhibitDelegateHelper.getOrdersDelegate()
                .getReplaceableOrdersForDefendantOnCaseAndOrderTypeAndStatus(this.model.getDefendantOnCaseID(),
                        this.model.getOrderTypeID(), new Integer(OrderStatus.SIGNED));
        log.debug("values.length: " + values.length);

        return values;
    }

    /**
     * Set the order types
     * 
     * @param types
     *            the order types
     */
    private void setOrderTypes(XhbOrderTypeValue[] types) {
        orderTypeValues = types;
    }
    
    /**
     * Set the disposal data
     * 
     * @param types
     *            the disposal data
     */
    private void setOrderDisposalData(HashMap disposalData) {
        disposalDataMap = disposalData;
    }

    /**
     * Get the controller for the application
     * 
     * @return the current XhibitApplicationController
     */
    public XhibitApplicationController getXAppController() {
        return xacp;
    }

    /**
     * Sets the model for the case
     * 
     * @param oidvo
     *            the model
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
    }

    /**
     * Dummy method to return an empty defendant wrapper
     * 
     * @return
     */
    private OrderDefendantWrapper getDummyDefendant() {
        DefendantBasicValue db = new DefendantBasicValue(new Integer(-999), new Integer(0), new Integer(0), "", "", "",
                "", null, new Integer(0), null, new Integer(0), new Integer(0), "");
        OrderDefendantWrapper def = new OrderDefendantWrapper(db);
        return def;

    }
}

	