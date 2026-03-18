package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.services.defendant.DisposalControllerException;
import uk.gov.courtservice.xhibit.business.services.results.Results2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.client.order.exceptions.PanelNotInitialisedException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.AbstractOrdersPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.D20TypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.DefendantListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderTypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersSummaryPanel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.CaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationFailureValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantOnCaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: Xhibit2 OrdersWizardDialog
 * </p>
 * <p>
 * Description: Constructs oredrs wizard
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

public class OrdersWizardDialog extends AbstractOrdersPanel {

	private static final long serialVersionUID = 1L;
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final Logger log = CSServices.getLogger(OrdersWizardDialog.class);
    
    private static final String D20_ORDER_TYPE_SHORT = "D20";
    private static final String D20_ORDER_TYPE_LONG = "D20 Order";

    private OrdersScreenFactory osf = null;

    private OrdersSummaryPanel oSPanel = null;

    private DefendantListPanel defPanel = null;

    private OrderTypeListPanel ordPanel = null;
    
    private D20TypeListPanel d20Type= null;
    
    private static final String FAILURE_TO_APPEAR_TYPE = "F";
    

    /**
     * Constructor
     * 
     * @throws CSRecoverableException
     */
    public OrdersWizardDialog() throws CSRecoverableException {
        super();
        initialise();
    }

    /**
     * Constructor
     * 
     * @param odm
     *            The model
     * @throws CSRecoverableException
     */
    public OrdersWizardDialog(OrderInitialDataVO odm) throws CSRecoverableException {
        super(odm);
        initialise();
        try {
            this.stepActivate();
        } catch (CSRecoverableException e) {
            log.debug(e.getMessage());// Nothing done
        }
    }

    /**
     * Initialise the components on the dialog
     * 
     * @throws CSRecoverableException
     */
    protected void initialise() throws CSRecoverableException {
        super.initialisePanel();

        // Make sure that the consistant L&F is set
        setLookAndFeel();

        getConstraints().fill = GridBagConstraints.BOTH;
        getConstraints().weightx = 1.0;
        getConstraints().weighty = 1.0;
        osf = OrdersScreenFactory.getinstance();
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    /**
     * Constructor for the OrderExistsDialog
     * 
     * @param f
     *            Frame (may be null)
     * @param title
     *            To be displayed
     * @param odm
     *            OrderInitialDataVO
     * @throws CSRecoverableException
     */
    public XPanel getOrderTypePanel(OrderInitialDataVO model) throws CSRecoverableException {
        return osf.getOrdersWizardTypeScreen(model);
    }

    /**
     * Update the model - if required
     */
    public void updateModel() {
        // Not implemented
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("OrdersWizardDialog:stepInitialise");
        setVisible(true);
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepInitialise();
        }
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("OrdersWizardDialog:stepDeactivate");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
        	if(comps[x] instanceof D20TypeListPanel)
        	{
        		((D20TypeListPanel)comps[x]).stepDeactivate();
        	}
        	else
        	{
        		((XPanel) comps[x]).stepDeactivate();
        	}
        }
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        log.debug("OrdersWizardDialog:stepValidate");
    }


    private AuthorisationReturnValue authorise(Integer xhibitCaseId) throws ResultsControllerException {
		log.debug("authorise(caseId="+xhibitCaseId+")");
    	AuthorisationRequestValue request = new AuthorisationRequestValue();
		Integer scheduledHearingId = model.getScheduledHearingId();
		request.setCaseId(xhibitCaseId);
		
		AuthorisationValue[] allAuthorisedDefendants = getResultsDelegate().getAuthorisableByDefendantOnCaseId(model.getDefendantOnCaseID());
		request.setDefendantsToAuthorise( allAuthorisedDefendants );        							

		return getResultsDelegate().authoriseSentenceOutcome( request, scheduledHearingId );
    }
   
    /**
     * Final Order first check (Authorisation)
     * 
     * @return	true if successful
     * 
     * @throws ResultsControllerException
     */
	private boolean firstFinalValdationCheck(AuthorisationReturnValue returnValue) throws ResultsControllerException {	
		log.debug("firstFinalValdationCheck()");
		CaseAuthorisationReturnValue caseAuthorisationValue = returnValue.getCaseAuthorisationReturnValue();
		boolean caseFailure = caseAuthorisationValue.hasCaseLevelFailed();
		log.debug("caseFailure="+(caseFailure ? "True" : "False"));
		boolean defendantFailure = processD20DefendantFailures( returnValue.getDefendantAuthorisationReturnValue());
		log.debug("defendantFailure="+(defendantFailure ? "True" : "False"));
		boolean defendantOncaseFailure = processDefendantOnCaseFailures(returnValue.getDefOnCaseAuthorisationReturnValue());
		log.debug("defendantOncaseFailure="+(defendantOncaseFailure ? "True" : "False"));
		
		return !(caseFailure || defendantFailure || defendantOncaseFailure);
	}
	
	private List<String> getAuthorisationMsgsToShow() {
		List<String> authMessages = new ArrayList<String>();
		authMessages.add("authorise.asn.notrecordeddefendant");
		authMessages.add("authorise.asn.notrecordedappellant");
		
		return authMessages;
	}
	
	/**
     * Fra,ework method
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("OrdersWizardDialog:stepUpdateViewState");
        // Loop through all sub components and trigger the appropriate
        // lifecycle method
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepUpdateViewState();
        }
    }

    /**
     * Depending on the mode (create or view or copy), check if everything has been
     * completed. If not, display an eror message.
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        log.debug("stepDeinitialise(" + (parm1 ? "True" : "False")+")");
        if (parm1 == true) {
            int mode = model.getMode();
            
            //	Initial validation:
            boolean valid = true;
            String messageKey = OrdersWizard.WIZ_WARN_MESSAGE;
            String message = null;
            
            switch( mode ){
            	case OrderInitialDataVO.CREATE_MODE:{
            		String orderType = model.getOrderType();
            		valid = !model.getDefendantName().equals("") && !model.getOrderType().equals("") && !model.getOrderType().equals(" ") && orderType != null;
            		log.debug("stepDeinitialise().Defendant-Valid= " + (valid ? "True" : "False"));
            		
        			////// ------ WARNING ------
        			///// For D20 changes only change the contents of this 'if' statement
        			///// Other order types also use the logic is in this method so need to be very, very careful!!!!! 
        			if ( valid && ( D20_ORDER_TYPE_SHORT.equals(orderType) || D20_ORDER_TYPE_LONG.equals(orderType) ) ) {
        				String d20Interim = model.getd20Interim();
        				String caseId = model.getCaseID();
        				
        				valid = d20Interim != null;
        				log.debug("stepDeinitialise().Interim-Valid= " + (valid ? "True" : "False"));
        				
        				if ( valid ){
    		            	Integer xhibitCaseId = model.getXhibitCaseId();
    		            	Integer defendantOnCaseId = model.getDefendantOnCaseID();
    		            	Integer scheduledHearingId = model.getScheduledHearingId();

    		            	if (YES.equals(d20Interim) ){
        						//	Interim cases are only Sentencing cases or Trial cases
        						if ( caseId.startsWith("S") || caseId.startsWith("T") ){
        							DisposalValue[] disposals = XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnCase( defendantOnCaseId, xhibitCaseId );
        							
        							//	For interim orders, at least 
        							valid = false;
        			            	
        			            	for ( int index = 0; index < disposals.length && !valid; index++ ) {
        			            		
        			            		XhbRefDisposalTypeBasicValue value = disposals[ index ].getRefDisposalType();
        			            		String disposalCode = value.getDisposalCode();
        			            		
        			            		log.debug( "Disposal code: " + disposalCode );
        			            		valid = disposalCode.equals("DISINT");
        			            		log.debug("stepDeinitialise().DISINT-Valid= " + (valid ? "True" : "False"));
        			            	}
        			            	
        			            	if ( !valid ){
        			            		//	Raise a message static this cannot proceed...
        			            		JOptionPane.showMessageDialog(this, ResourceHelper.getResourceString(OrdersWizard.WIZ_NO_INTERIM),
        			            				ResourceHelper.getResourceString(OrdersWizard.WIZ_WARN_TITLE), JOptionPane.WARNING_MESSAGE);
        			            		
        			            		throw new UserCancelException();
        			            	}	
        						}
        						
        					} else if (NO.equals(d20Interim) ) {
        						messageKey = caseId.startsWith( "A" ) ? OrdersWizard.WIZ_INCOMPLETE_APPELLANT : OrdersWizard.WIZ_INCOMPLETE_DEFENDANT;
        						
        						//	Full orders can apply to all the three main types S, T or A
        						if ( caseId.startsWith("S") || caseId.startsWith("T") || caseId.startsWith("A")){
        							//	First check: Pass authorisation tests:
        							AuthorisationReturnValue authReturnValue = authorise(xhibitCaseId);
        							valid = firstFinalValdationCheck( authReturnValue );
        							
        							if(!valid){
        								for(DefendantOnCaseAuthorisationReturnValue defOnCaseAuth :authReturnValue.getDefOnCaseAuthorisationReturnValue()){
        									for(FailureMessage failure : defOnCaseAuth.getFailures()){
        										if (getAuthorisationMsgsToShow().contains(failure.getFailureKey())){
        											messageKey = failure.getFailureKey();
        											message = XHIBITConstant.getResource(XhibitBundles.CaseProgressResources, messageKey);
        											break;
        										}
        									}
        								}
        							}
        							
        							log.debug("stepDeinitialise().firstFinalValdationCheck-Valid= " + (valid ? "True" : "False"));
        	            			
        	            			if ( valid ){
        	            				ResultsCompositeValue results = null;
        	            				OrderInitialDataHelper helper = model.getHelper();
        	            				valid = helper != null;
        	            				if (helper != null) {
        	            					if ( valid && caseId.startsWith( "A" )) {
        	            						results = ResultsHelper.getResults(xhibitCaseId, scheduledHearingId); 
        	            						if (!hasAppealResults(xhibitCaseId, results)) {
        	            							log.debug("stepDeinitialise().hasAppealResults-Valid= False");
	        	            						messageKey = OrdersWizard.WIZ_INCOMPLETE_APPELLANT;	
	        	            						valid = false;
        	            						 }
        	            					}
	        	            				if ( valid && !helper.checkCaseHasDVLA()) {
	        	            					log.debug("stepDeinitialise().checkCaseHasDVLA-Valid= False");
	        	            					messageKey = caseId.startsWith( "A" ) ? OrdersWizard.WIZ_NO_DVLA_APPELLANT : OrdersWizard.WIZ_NO_DVLA_DEFENDANT;
	        	            					valid = false;
	        	            				}
	        	            				if (valid && !helper.checkContainsAnyDO()) {
	        	            					log.debug("stepDeinitialise().checkContainsAnyDO-Valid= False");
	        	            					messageKey = caseId.startsWith( "A" ) ? OrdersWizard.WIZ_NO_DRIVING_DISPOSALS_APPELLANT : OrdersWizard.WIZ_NO_DRIVING_DISPOSALS_DEFENDANT;
	        	            					valid = false;
	        	            				}
        	            				}
    	            					if ( valid && caseId.startsWith( "A" )){
    	            						if (results == null) {
    	            							results = ResultsHelper.getResults(xhibitCaseId, scheduledHearingId);
    	            						}
	            							if ( !checkOffences(xhibitCaseId, defendantOnCaseId, results)) {
	            								log.debug("stepDeinitialise().checkOffences-Valid= False");
	            								messageKey = OrdersWizard.WIZ_CROWN_DISPOSALS;
	            								valid = false;
	            							}
    	            					}
        	            			}
        						}
        					}
            			}
            		} // End of 'if' to handle D20 orders
            	}
            	break;
            	
            	case OrderInitialDataVO.VIEW_MODE:{
            		valid = !model.getDefendantName().equals("");
            	}
            	break;
            	
            	case OrderInitialDataVO.COPY_MODE:{
            		valid = !model.getDefendantName().equals("");
            	}
            	break;
            	
            	default:
            		break;
            }
            
            if ( !valid){
            	if (message == null){
            		message = ResourceHelper.getResourceString(messageKey);
            	}
                JOptionPane.showMessageDialog(this, message,
                        ResourceHelper.getResourceString(OrdersWizard.WIZ_WARN_TITLE), JOptionPane.WARNING_MESSAGE);
                
                throw new UserCancelException();        	
            }
        } else {
        	stepDeactivate();
        }
        
        model.setFinished(parm1);
    }

    /**
     * Check the offences for a verdict.
     * If the verdict was varied, check that there is at least one Crown Court disposal.
     * 
     * @param 	xhibitCaseId				The case ID
     * @param   defendantOnCaseId			Defendant on case ID
     * @param   ResultsCompositeValue		ResultsCompositeValue
     * 
     * @return	true if valid
     * 
     * @throws ResultsControllerException 
     * @throws DisposalControllerException 
     */
    private boolean checkOffences(Integer xhibitCaseId, Integer defendantOnCaseId, ResultsCompositeValue results) throws ResultsControllerException, DisposalControllerException {
    	
    	if (isVaried(xhibitCaseId, results)) {
    		log.debug("checkOffences() - Varied");
    		// Get the disposals
    		DisposalValue[] disposals = XhibitDelegateHelper.getDefendantDelegate().getDisposalsForDefendantOnCase( defendantOnCaseId, xhibitCaseId );
			log.debug(String.format("Have %s disposals", disposals.length));
			
			//	Varied must have a Crown Court disposal
			if (!hasCrownCourtDisposal(disposals)) {
				return false;
			}
    	}
    	
		return true;
	}
    
    private boolean isVaried(Integer xhibitCaseId, ResultsCompositeValue results) {
    	Map<Integer,List<VerdictValue>> map = ResultsHelper.getVerdicts(xhibitCaseId, results);
    	
    	if (map.size() > 0 ){
    		for(Map.Entry<Integer, List<VerdictValue>> entry : map.entrySet()) {
    			Integer defendantOnOffenceId = entry.getKey();
    			log.debug("Checking Verdicts for DefendantOnOffenceId "+ defendantOnOffenceId);
    			List<VerdictValue> verdicts = entry.getValue();
    			String appResultCode;
	    		for (VerdictValue verdict : verdicts) {
	    			// Get the ref app result code - the value from XHB_REF_APP_RESULT.APP_RESULT_CODE
	    			appResultCode = verdict.getRefAppealOffenceCode();
	    		
	    			if (appResultCode != null) {
	    				// Determine if the XHB_REF_APP_RES_D20_MAP.D20_RESULT='Varied'
	    				boolean isVaried = XhibitDelegateHelper.getResults2Delegate().isThisAppResultVariedForD20(appResultCode);
	
	    				if (isVaried) {
	    					return true;
	    				}
	    			}
	    		}
	    	}
    	}
    
    	return false;
    }
    
    private boolean hasCrownCourtDisposal(DisposalValue[] disposals) {
    	boolean isCrownCourt = false;
		for ( int index = 0; index < disposals.length && !isCrownCourt; index++){
			if (!YES.equals(disposals[index].getObsInd())) {
				if ("C".equals(disposals[ index ].getCourtType())) {
				    isCrownCourt = true;
					break;
				}
			}
		}
		log.debug("hasCrownCourtDisposal() = " +(isCrownCourt ? "True" : "False"));
		return isCrownCourt;
    }
    
	/**
     * Process the defendant on case failures
     * 
     * @param 	defOnCaseFailures			The list of DoC failures
     * 
     * @return	true if at least one entry has failures
     * 
     * Notes:  	This code is a simplified version of the "debugDefendantLevel" in "AuthoriseResultsPanel"
     * 		  	We don't need to know the reasons, only that one has a defendant-on-case failure, so the first 
     * 			we find is good enough
     */
    private boolean processDefendantOnCaseFailures( DefendantOnCaseAuthorisationReturnValue[] defOnCaseFailures) {
    	boolean resultCode = false;			//	Assume no failures
    	
    	for (int i = 0; i < defOnCaseFailures.length && !resultCode; i++) {
    		DefendantOnCaseAuthorisationReturnValue ret = defOnCaseFailures[i];
    		
    		resultCode = ret.hasDefendantFailed();
    	}
    	
		return resultCode;
	}
    
    private boolean processD20DefendantFailures(DefendantAuthorisationReturnValue[] allDefendantEntries) {
    	
		return processDefendantFailures(allDefendantEntries, true);
	}

	/**
     * Process all the defendant return values.
     * 
     * @param 	allDefendantEntries	The defendant return values
     * 
     * @return	true if at least one entry has failures
     * 
     * Notes:  	This code is a simplified version of the "debugOffenceLevel" in "AuthoriseResultsPanel"
     * 		  	We don't need to know the reasons, only that one has a defendant failure, so the first 
     * 			we find is good enough
     */
    private boolean processDefendantFailures(DefendantAuthorisationReturnValue[] allDefendantEntries, boolean isD20) {
    	boolean resultCode = false;				//	Assume no failures
    	Arrays.sort(allDefendantEntries, DefendantComparator.getInstance());
    	
    	for (int i = 0; i < allDefendantEntries.length && !resultCode; i++) {
    		DefendantAuthorisationReturnValue ret = allDefendantEntries[i];
    		if(isD20)
    		{
    			if(ret.getFailures()!=null)
    			{
					for(int j=0; j < ret.getFailures().length; j++)
					{
		    			if(!ret.getFailures()[j].getCharge().getChargeType().equals(FAILURE_TO_APPEAR_TYPE))
		    			{
		    				resultCode = ret.hasDefendantFailed();
		    			}
					}
    			}
    		}
    		else 
    		{
    			resultCode = ret.hasDefendantFailed();
    		}
    	}
    	
		return resultCode;
	}

	/**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("OrdersWizardDialog:stepActivate");
    }

    /**
     * Add an Xpanle to the dialog
     * 
     * @param xPanel
     */
    
    public void add(XPanel xPanel) {
        if (xPanel instanceof OrdersSummaryPanel) {
            oSPanel = (OrdersSummaryPanel) xPanel;
        } else if (xPanel instanceof DefendantListPanel) {
            defPanel = (DefendantListPanel) xPanel;
        } else if (xPanel instanceof OrderTypeListPanel) {
            ordPanel = (OrderTypeListPanel) xPanel;
        }
        else if (xPanel instanceof D20TypeListPanel)
        {
        	d20Type = (D20TypeListPanel)xPanel;
        }

        super.add(xPanel);
    }

    /**
     * Get the summary panel - common to all wizard screens
     * 
     * @return The summary panel
     * @throws PanelNotInitialisedException
     */
    public OrdersSummaryPanel getSummaryPanel() throws PanelNotInitialisedException {
        if (oSPanel == null) {
            throw new PanelNotInitialisedException(this.getClass().getName());
        }
        return oSPanel;
    }

    /**
     * Retrun the defendant panel
     * 
     * @return The panel
     * @throws PanelNotInitialisedException
     */
    public DefendantListPanel getDefendantPanel() throws PanelNotInitialisedException {
        if (defPanel == null) {
            throw new PanelNotInitialisedException(this.getClass().getName());
        }
        return defPanel;
    }

    /**
     * Return the order type panel
     * 
     * @return the panel
     * @throws PanelNotInitialisedException
     */
    public OrderTypeListPanel getOrderTypePanel() throws PanelNotInitialisedException {
        if (ordPanel == null) {
            throw new PanelNotInitialisedException(this.getClass().getName());
        }
        return ordPanel;
    }
    
    public D20TypeListPanel getD20TypePanel() throws PanelNotInitialisedException {
    	if(d20Type==null)
    	{
    		 throw new PanelNotInitialisedException(this.getClass().getName());
    	}
    	
    	return d20Type;
    }
    
    /**
     * Check if the order type panel has been created, used to switch between D20 and other order types
     * @return
     * @throws PanelNotInitialisedException
     */
    public boolean isOrderTypePanel() throws PanelNotInitialisedException {
        return ordPanel != null;
    }

    /**
     * Check if the D20 type panel has been created, used to switch between D20 and other order types
     * @return
     * @throws PanelNotInitialisedException
     */
    public boolean isD20TypePanel() throws PanelNotInitialisedException {
    	return d20Type != null;
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log.error("OrdersWizardDialog: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }

    private Results2ControllerBeanBusinessDelegate getResultsDelegate() {
        return XhibitDelegateHelper.getResults2Delegate();
    }

    private boolean hasAppealResults(Integer xhibitCaseId, ResultsCompositeValue results) {
    	Map<Integer,List<VerdictValue>> map = ResultsHelper.getVerdicts(xhibitCaseId, results);
    	// Are there any verdicts
    	boolean isValid = map.size() > 0;
    	return isValid;   	
    }
}