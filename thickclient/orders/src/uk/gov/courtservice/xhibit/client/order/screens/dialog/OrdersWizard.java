package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValidationReturnValue;
import uk.gov.courtservice.xhibit.client.order.exceptions.PanelNotInitialisedException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.model.OffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.util.OffenceLinkD20Validation;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrdersDocumentListener;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkHelperValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkValue;

/**
 * <p>
 * Title: Xhibit2 OrdersWizard
 * </p>
 * <p>
 * Description: Builds the orders wizard
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

public class OrdersWizard extends XWizardDialog {

    private static final Logger log = CSServices.getLogger(OrdersWizard.class);

    /**
     * Constant that identifies an escape key
     */
    private static final String ESCAPE_ACTION = "escape";

    public static final String WIZ_WARN_TITLE = "orders.wizard.waring.title";

    public static final String WIZ_WARN_MESSAGE = "orders.wizard.waring.message";
    
    public static final String WIZ_NO_INTERIM = "orders.wizard.warning.no.interim.allowed";
    
    public static final String WIZ_INCOMPLETE_DEFENDANT = "orders.wizard.warning.incomplete.defendant";
    public static final String WIZ_INCOMPLETE_APPELLANT = "orders.wizard.warning.incomplete.appellant";
    
    public static final String WIZ_NO_DRIVING_DISPOSALS_DEFENDANT = "orders.wizard.warning.no.disposals.defendant";
    public static final String WIZ_NO_DRIVING_DISPOSALS_APPELLANT = "orders.wizard.warning.no.disposals.appellant";
    
    public static final String WIZ_NO_DVLA_DEFENDANT = "orders.wizard.warning.no.dvla.defendant";
    public static final String WIZ_NO_DVLA_APPELLANT = "orders.wizard.warning.no.dvla.appellant";
    
    public static final String WIZ_OFFENCE_MANADTORY = "orders.wizard.warning.offence.mandantory.disqualification"; 
    
    public static final String WIZ_CROWN_DISPOSALS = "orders.wizard.warning.crown.court.disposals";
    
    private static final String DISPOSAL_MISSING_MSG ="results.authorise.d20.disposalsMissing";
	private static final String DRIVING_OFF_NO_DISP_MSG ="results.authorise.d20.noDOWithDisposals";
	private static final String NO_DISINT_FOUND_MSG ="results.authorise.d20.noDisIntFound";
	private static final String NO_OFF_MSG="results.authorise.d20.noValidOffences"; 
	private static final String NO_DVLA_MSG="results.authorise.d20.noDVLA";

    private static final String YES = "Y";

    private OrderInitialDataVO model;
    
    private OffenceLinkD20Validation validator;   
    
    private XhibitApplicationController xacp;

 
    /**
     * Constructor
     * 
     * @param frame
     *            The parent of the dialog
     * @param odm
     *            The model
     * @throws CSRecoverableException
     */
    public OrdersWizard(java.awt.Frame frame, OrderInitialDataVO model) throws CSRecoverableException {
        super(frame, "Orders Wizard", true);
        validator = new OffenceLinkD20Validation(model, this);
        this.model = model;
        initialise();
    }

    /**
     * Sets the local reference to the supplied model
     * 
     * @param odm
     *            The model
     * @throws CSRecoverableException
     */
    public void setData(OrderInitialDataVO model) throws CSRecoverableException {
        this.model = model;
        initialise();
    }

    /**
     * Add the various screens (currently Defendant and OrderType) to the Wizard
     * 
     * @throws CSRecoverableException
     */
    private void initialise() throws CSRecoverableException {
        OrdersScreenFactory factory = OrdersScreenFactory.getinstance();

        ArrayList al = new ArrayList();
        OrdersWizardDialog firstScreen = (OrdersWizardDialog) factory.getOrdersWizardDefendantScreen(this.model, getButtonPanel().getFinish());
        //System.out.println(firstScreen.getSummaryPanel().getOrderTypeData().getText());
        Document firstScreenDocument = firstScreen.getSummaryPanel().getDefendantNameData().getDocument();
        //System.out.println(firstScreen.getSummaryPanel().getOrderTypeData().getText());
        firstScreenDocument.addDocumentListener(new OrdersDocumentListener(getButtonPanel().getNext(), this.model.getMode(), this.model.isMonetaryOrder(), this.model.isD20Order(), this.model.isACase()));
        firstScreenDocument.addDocumentListener(new OrdersDocumentListener(getButtonPanel().getFinish(), this.model.getMode(), this.model.isMonetaryOrder(), this.model.isD20Order(), this.model.isACase()));
        //System.out.println(firstScreen.getSummaryPanel().getOrderTypeData().getText());
        populateDefendantScreen(firstScreen);
        //System.out.println(firstScreen.getSummaryPanel().getOrderTypeData().getText());
        al.add(firstScreen);

        OrdersWizardDialog secondScreen = null;
        if(model.isD20Order() && !model.isACase())
        {
        	secondScreen = populateD20TypeScreen(factory, secondScreen);
        	al.add(secondScreen);
        }
        else if(!model.isD20Order())
        {
        	secondScreen = populateOrderTypeScreen(factory, secondScreen);
        	al.add(secondScreen);
        }

        addBodyPanels(al);

        enableButtons(firstScreen);
        // React when the user presses Escape.
        this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESCAPE_ACTION);
        this.getRootPane().getActionMap().put(ESCAPE_ACTION, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ((XAction) getButtonPanel().getCancel().getAction()).actionPerformed(new ActionEvent(this, 0, ""));
            }
        });
    }

    /** 
     * Enables the finish/ next buttons depending on the case type
     * Case type determines workflow for the order dialog 
     * @param firstScreen
     * @throws PanelNotInitialisedException
     */
    private void enableButtons(OrdersWizardDialog firstScreen) throws PanelNotInitialisedException {
        // Something in the lifecycle methods seems to be setting the visibility off
        // This line was added to force the first screen to be visible
        log.debug("OrdersWizard.enableButtons enter.");
        firstScreen.setVisible(true);

        // Deal with D20's first
        if (this.model.isD20Order()&& !(model.isBCase())) {
        	
        	// If its "create" and its T or S cases then next is enabled and finish is disabled
        	// If its "create" and its an A case then next is disabled and finish is enabled
        	// If its "view" then next is disabled and finish is enabled
        	// There is no longer a "copy" option for D20's
        	if(model.isACase() || model.getMode() == OrderInitialDataVO.VIEW_MODE) {
        		log.debug("OrdersWizard.enableButtons : d20 case orders process begin. Appeal or \"View\" buttons set");
        		if (firstScreen.getSummaryPanel().getDefendantNameData().getText().equals("")) {
                    log.debug("OrdersWizard.enableButtons : no deft selected; next=false, finish=false.");
                    this.getButtonPanel().getNext().getAction().setEnabled(false);
                    this.getButtonPanel().getFinish().getAction().setEnabled(false);
                } else {
	        		log.debug("OrdersWizard.enableButtons : next=false, finish=true.");
	        		
	        		this.getButtonPanel().getNext().getAction().setEnabled(false);
	                this.getButtonPanel().getFinish().getAction().setEnabled(true);
                }
        	} else {
        		log.debug("OrdersWizard.enableButtons : d20 case orders process begin.");
	            if (firstScreen.getSummaryPanel().getDefendantNameData().getText().equals("")) {
	                log.debug("OrdersWizard.enableButtons : next=false, finish=false.");
	                this.getButtonPanel().getNext().getAction().setEnabled(true);
	                this.getButtonPanel().getFinish().getAction().setEnabled(false);
	            } else {
	                log.debug("OrdersWizard.enableButtons : next=false, finish=true.");
	                this.getButtonPanel().getNext().setEnabled(true);
	                this.getButtonPanel().getFinish().setEnabled(false);
	            }
        	}
        }
        
        // If this is a monetary order or its a B case then its simple - next is disabled, finish is enabled
        else if (this.model.isMonetaryOrder() || this.model.isBCase()) {
            log.debug("OrdersWizard.enableButtons : monetary/d20/b case orders process begin.");
            if (firstScreen.getSummaryPanel().getDefendantNameData().getText().equals("")) {
                log.debug("OrdersWizard.enableButtons : next=false, finish=false.");
                this.getButtonPanel().getNext().getAction().setEnabled(false);
                this.getButtonPanel().getFinish().getAction().setEnabled(false);
            } else {
                log.debug("OrdersWizard.enableButtons : next=false, finish=true.");
                this.getButtonPanel().getNext().setEnabled(false);
                this.getButtonPanel().getFinish().setEnabled(true);
            }
        } 
        
        else {
            log.debug("OrdersWizard.enableButtons : normal orders process begin.");
            // If in view mode, disable the next button to prevent the user from
            // viewing the order type screen
            if ((this.model.getMode() == OrderInitialDataVO.VIEW_MODE) || (this.model.getMode() == OrderInitialDataVO.COPY_MODE)){
                log.debug("OrdersWizard.enableButtons : view/copy mode; next=false, finish=true.");
                this.getButtonPanel().getNext().setEnabled(false);
                this.getButtonPanel().getFinish().setEnabled(true);
            }
            setButtonEnabled();
            if (this.model.getMode() == OrderInitialDataVO.CREATE_MODE) {
                log.debug("OrdersWizard.enableButtons : create mode; next=untouched, finish=false.");
                this.getButtonPanel().getFinish().getAction().setEnabled(false);
            }
    
            if (firstScreen.getSummaryPanel().getDefendantNameData().getText().equals("")) {
                log.debug("OrdersWizard.enableButtons : no deft selected; next=false, finish=false.");
                this.getButtonPanel().getNext().getAction().setEnabled(false);
                this.getButtonPanel().getFinish().getAction().setEnabled(false);
            }
        }
        log.debug("OrdersWizard.enableButtons exit.");
    }

    /**
     * Creates an OrderType screen and populates it
     * 
     * @param factory
     *            The screen factory
     * @param secondScreen
     *            Reference to the dialog holding the screen
     * @return The dialog holding the screen
     * @throws CSRecoverableException
     */
    private OrdersWizardDialog populateOrderTypeScreen(OrdersScreenFactory factory, OrdersWizardDialog secondScreen)
            throws CSRecoverableException {
        try {
            secondScreen = (OrdersWizardDialog) factory.getOrdersWizardTypeScreen(this.model, getButtonPanel()
                    .getFinish());
        } catch (CSRecoverableException ex) {
            log.error("Error retrieving OrdersTypeScreen", ex);
            throw ex;
        }
        try {
            secondScreen.getSummaryPanel().resetInputs();
        } catch (PanelNotInitialisedException pnie) {
            // Do nothing as panel will be reset
            log.info(pnie);
        }
        return secondScreen;
    }
    
    /** 
     * Method adds D20 report type to the order dialog
     * @param factory factory object which creates the new D20Template
     * @param secondScreen the template second screen 
     * @return Returns updated orders dialog with D20 type selection screen added 
     * @throws CSRecoverableException
     */
    private OrdersWizardDialog populateD20TypeScreen(OrdersScreenFactory factory, OrdersWizardDialog secondScreen)
            throws CSRecoverableException {
        try {
			secondScreen = (OrdersWizardDialog) factory.getD20TypeScreen(this.model, getButtonPanel().getFinish());
        } catch (CSRecoverableException ex) {
            log.error("Error retrieving OrdersTypeScreen", ex);
            throw ex;
        }
        try {
            secondScreen.getSummaryPanel().resetInputs();
        } catch (PanelNotInitialisedException pnie) {
            // Do nothing as panel will be reset
            log.info(pnie);
        }
        return secondScreen;
    }

    /**
     * Populates the Defendant selection screen
     * 
     * @param firstScreen
     *            The dialog holding the screen
     * @throws CSRecoverableException
     */
    private void populateDefendantScreen(OrdersWizardDialog firstScreen) throws CSRecoverableException {
        try {
            if (this.model.getDefendantName() == null // Defendant name is null
                    || // OR
                    this.model.getDefendantName().equals("") == true) // Defendant name is empty
            {
                firstScreen.getDefendantPanel().getDefendantList().setSelectedIndex(0);
            }

            if (firstScreen.getDefendantPanel().getDefendantList().getItemCount() > 1) {
                firstScreen.getSummaryPanel().resetInputs();
            }
        } catch (PanelNotInitialisedException pnie) {
            throw new CSRecoverableException("OrdersWizard.populateDefendantScreen", "Could not retrieve Orders Defendant Panel", pnie);
        }
    }

    /**
     * Overrides prev() from XWizardDialog. The enabling/disabling of certain
     * buttons was not correct in superclass
     * 
     * @throws CSRecoverableException
     */
    public void prev() throws CSRecoverableException {
        // Do not do anything to current panel if going backwards.
        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepValidate();
            xp.stepDeactivate();
        }
        super.prev();
        setButtonEnabled();
    }

    /**
     * Overrides next() from XWizardDialog Enables/disables the next button
     * 
     * @throws CSRecoverableException
     */
    public void next() throws CSRecoverableException {
        super.next();
        if (getButtonPanel().getNext().isEnabled() && currentPanel == numPanels) {
            getButtonPanel().getNext().setEnabled(false);
        }
    }

    /**
     * Checks if all required fields have been entered
     * 
     * @throws CSRecoverableException
     */
    public void finish() throws CSRecoverableException {
        
    	boolean haveWeCalledSuper = false;
        if(!model.isD20Order()) { 
        	super.finish();
        	
        	haveWeCalledSuper = true;
        }
        
        if (this.model.getMode() == OrderInitialDataVO.CREATE_MODE && 
                (this.model.getDefendantName().equals("") || this.model.getOrderType().equals("")) // Def Name or Ord type is empty
            ||
            (
            	((this.model.getMode() == OrderInitialDataVO.VIEW_MODE && !model.isD20Order()) 
            				|| this.model.getMode() == OrderInitialDataVO.COPY_MODE)
                && this.model.getDefendantName().equals("")) // View or Copy and Def name is empty
           )
        
        {
            log.debug("Defendant Name = " + this.model.getDefendantName());
            log.debug("Order Type = " + this.model.getOrderType());
            throw new CSRecoverableException("OrderWizard", "OrderWizard");
        }
        
        if(model.isD20Order() && model.getMode() == OrderInitialDataVO.CREATE_MODE) {	
        	AuthorisationReturnValue returnValue =null;
        	AuthorisationRequestValue request = new AuthorisationRequestValue();

            Integer caseId = model.getXhibitCaseId();
            request.setCaseId(caseId);
            request.setCourtLogDate(Calendar.getInstance(Locale.getDefault()));
            
            AuthorisationValue[] authorisation =  XhibitDelegateHelper.getResults2Delegate().getAuthorisableByDefendantOnCaseId(model.getDefendantOnCaseID());
            request.setDefendantsToAuthorise(authorisation);
            request.setCourtLogDate(Calendar.getInstance());
             
            Integer shid =  model.getHearingID();

            returnValue = XhibitDelegateHelper.getResults2Delegate().validateD20Order(request, shid);
            
        	D20OffenceLinkHelperValue d20OffenceHelper = new D20OffenceLinkHelperValue(); 
       		Collection<String> refD20Offences = XhibitDelegateHelper.getBizRefDelegate().getOffenceCodes();
       		d20OffenceHelper.setRefD20Offences(refD20Offences);
       		d20OffenceHelper.setCaseId(model.getCaseID());
       		d20OffenceHelper.setXhibitCaseId(model.getXhibitCaseId());
       		d20OffenceHelper.setDefendantId(model.getDefendantID());
       		d20OffenceHelper.setDefendantOnCaseId(model.getDefendantOnCaseID());
       		d20OffenceHelper.setUserName(XhibitSingleton.getInstance().getUserSession().getUserName());
       		d20OffenceHelper.setHearingID(model.getScheduledHearingId());
       		d20OffenceHelper.setDefendantName(model.getDefendantName());
       		d20OffenceHelper.setXhibitCourtId(model.getXhibitCourtId());
			d20OffenceHelper.setUserName(XhibitSingleton.getInstance().getUserSession().getUserName());
			d20OffenceHelper.setHearingID(model.getScheduledHearingId());
			d20OffenceHelper.setDefendantName(model.getDefendantName());
			d20OffenceHelper.setXhibitCourtId(model.getXhibitCourtId());
			d20OffenceHelper.setBreachCase(model.isBCase());
			d20OffenceHelper.setSentenceCase(model.isSCase());
			d20OffenceHelper.setTrialCase(model.isTCase());
			d20OffenceHelper.setaCase(model.isACase());
			d20OffenceHelper.setD20Interim(model.getd20Interim());
			D20OffenceLinkReturnValue D20OffenceReturnValue = XhibitDelegateHelper.getResults2Delegate()
					.generateD20OffenceLink(request, shid, d20OffenceHelper);
			setOffenceLinkReturnValue(D20OffenceReturnValue);

       		super.finish();
       		haveWeCalledSuper = true;

       		processValid20Order();
       		
       	    validateConvictingCourt();
       	    
        	if(!model.isHasFailed()){
        		if (D20OffenceReturnValue!=null) {
        			OrderOffenceModel d20OffenceModel = setOffenceList(D20OffenceReturnValue);
        			OrderOffenceDialog dialog = new OrderOffenceDialog(this.xacp, d20OffenceModel);
        			dialog.setVisible(true);
        			
        			if(!d20OffenceModel.getFinished()){
        				log.debug("finish() - Failed");
        				model.setHasFailed(true);
        			} else {
        				log.debug("finish() - PopulateModel");
        			    model.setOffenceData(d20OffenceModel);
        			}
        		}
        	}
        } 
        
        if (!haveWeCalledSuper) {
        	super.finish();
        }    

    }
    
    /**
     * Displays an error message for the Miscellaneous Appeal case where Reference Court id is null
     */
	private void validateConvictingCourt() throws CaseControllerException {
        if(model.getCaseID().startsWith("A")) {
		    try{
			    CaseBasicValue caseItem = XhibitDelegateHelper.getCaseDelegate().getCase(model.getXhibitCaseId());
		        if(caseItem.getCaseSubType().equals("O")){
			        if(caseItem.getRefCourtID()==null){
				        String title = Resource.getOrdersClientBundle("orders.d20.appealErrorTitleA");
				        String message = Resource.getOrdersClientBundle("orders.d20.appealErrorNoConvictingCourt");
				        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
				        log.debug("validateConvictingCourt() - Failed"); 
				        model.setHasFailed(true);
			        }  
		        }
		    }
		    catch(NullPointerException npe){
			    model.setHasFailed(true);
			    log.error("Error getting case item");
			    npe.printStackTrace();
		    }
        }    
	}
    
    /**
     * Method converts return values from validation to a form ready to be used in the offence link selection process
     * @param returnValue The return data processed as a result of offence link validation
     * @return 
     */
    private OrderOffenceModel setOffenceList(D20OffenceLinkReturnValue returnValue) {
    	log.debug("setOffenceList() - D20OffenceLinks="+returnValue.getD20Offences() != null ? returnValue.getD20Offences().size() : 0);
		OrderOffenceModel offences = new OrderOffenceModel();
		for (D20OffenceLinkValue offence : returnValue.getD20Offences()) {

			// Skip over obsolete offences
			if(YES.equalsIgnoreCase(offence.getObsInd())) continue;

			OffenceModel offenceModel = new OffenceModel();
			offenceModel.setDvlaOffence(offence.getDvlaOffence());
			offenceModel.setOffence(offence.getOffence());
			offenceModel.setOffenceDescription(offence.getOffenceDescription());
			offenceModel.setDateOfConviction(offence.getDateOfConviction());
			offenceModel.setDateOffence(offence.getDateOffence());
			offenceModel.setOffenceID(offence.getRefOffenceID());
			offenceModel.setSeqNo(offence.getSequenceNo());
			offenceModel.setFinD20(YES.equals(offence.getFinalD20()));
			offenceModel.setIntD20(YES.equals(offence.getInterimD20()));
		
			if (offences.getOffences().isEmpty() && !YES.equals(offence.getObsInd())) {
				offences.addOffence(offenceModel);
			} else if (!YES.equals(offence.getObsInd())) {
				offences.addOffence(offenceModel);
			}
		}
		
		offences.setCaseID(model.getXhibitCaseId());
		offences.setDefendantID(model.getDefendantID());
		offences.sortOffenceModel();
		log.debug("setOffenceList() - Valid D20Links="+offences.getOffences() != null ? offences.getOffences().size() : 0);
		return offences;
	}

    /**
     * Method checks offence link validation messages and returns a binary result for specific errors 
     * @param D20OffenceReturnValue returns data model containing the boolean results of error checking
     */
    private void setOffenceLinkReturnValue(D20OffenceLinkReturnValue D20OffenceReturnValue) {
		OffenceValidationReturnValue offenceReturnValue = new OffenceValidationReturnValue();
		offenceReturnValue.setDefendantOnCase(model.getDefendantOnCaseID());

		for (String messages : D20OffenceReturnValue.getCaseFailures()) {
			log.debug("Failure:"+messages);
			if (DISPOSAL_MISSING_MSG.equals(messages))
				offenceReturnValue.setDisposalsMissing(true);
			if (DRIVING_OFF_NO_DISP_MSG.equals(messages))
				offenceReturnValue.setDrivingOffencesWithoutDisposals(true);
			if (NO_DISINT_FOUND_MSG.equals(messages))
				offenceReturnValue.setNoDisINTFound(true);
			if (NO_OFF_MSG.equals(messages))
				offenceReturnValue.setNoOffences(true);
			if (NO_DVLA_MSG.equals(messages))
				offenceReturnValue.setNoDVLA(true);
		 
		}

		model.setOffenceValidationReturnValue(offenceReturnValue);
	}
   
    /**
     * Method carries out validation check for D20 offences.
     * Failed results will display appropriate error messages and set the OrderInitalDataVO
     * model to failed if any validations fail
     */
	private void processValid20Order() {
    	
		if (validator.getValidationResults(model.getOffenceValidationReturnValue())) {
			log.debug("processValid20Order() - Failed");
			model.setHasFailed(true);
		 }
	}
    	
    
    	
    /**
     * Enable/disable the various wizard buttons
     */
    private void setButtonEnabled() {
        if (currentPanel == 0) {
            getButtonPanel().getBack().getAction().setEnabled(false);
        } else {
            getButtonPanel().getBack().getAction().setEnabled(true);
        }
        if (currentPanel == numPanels) {
            getButtonPanel().getNext().getAction().setEnabled(false);
        } else {
            getButtonPanel().getNext().getAction().setEnabled(true);
        }
        validate();
    }

}