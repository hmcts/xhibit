package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLog2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: CourtLogEventPanel
 * </p>
 * <p>
 * Description: Intended to be a super class for court log events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public abstract class CourtLogEventPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String SentenceForBroadcastEventType = "20937";
	private static final String SentenceForBroadcastOptions = "E20937_Sentence_Recorded_For_Broadcast_Options";
	private static final String SentenceForBroadcastType = "E20937_SRB_Type";
	private static final String SentenceForBroadcastYes = "E20937_YES";
	private static final String SentenceForBroadcastAppGrantedNo = "N";
	

	protected final Logger log = CSServices.getLogger(getClass());

    private final CourtLogEventLevelPanel clep;

    private final FreeTextModel model;

    private final XDialog parent;

    private CourtLogAuditPanel logAuditPanel = null;

    private OkCancelPanel buttonPanel = null;

    private JLabel panelTitleLabel = null;

    /**
     * Constructor to initialise all of the required properties of this class.
     * 
     * @param parent
     *            The <code>XDialog</code> that is the parent container of
     *            this panel.
     * @param model
     *            The <code>FreeTextModel</code> that holds the details to be
     *            displayed or stored.
     * @throws CSRecoverableException
     *             if any problems occur whilst creating the log event or audit
     *             panels.
     */
    public CourtLogEventPanel(XDialog parent, FreeTextModel model) throws CSRecoverableException {
        this.setLayout(new GridBagLayout());

        this.model = model;
        this.parent = parent;

        // only create the audit and button panels if we have a parent dialog
        if (parent != null) {
            this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

            Date defDate = model.getXac().getApplicationCaseModel().getScheduledHearingDateFrom();
            if (defDate == null) {
                defDate = Calendar.getInstance().getTime();
            }

            ApplicationCaseModel acm = ((XhibitApplicationController) getParentFrame()).getApplicationCaseModel();

            this.logAuditPanel = new CourtLogAuditPanel(CourtLogAuditPanel.getValues(acm.getCaseId()), acm
                    .getScheduledHearingId());
        }

        // Create an instance of the event level panel to capture CJSE info
        this.clep = new CourtLogEventLevelPanel(model, this);
    }

    /**
     * Initialises variables and models required to load screen information
     * 
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("stepInitialise::start");

        clep.stepInitialise();

        if (model.isInEditMode()) {
            log.debug("stepInitialise::isInEditMode = true");
            CourtLogCRUDValue courtLogCRUDValue = getCLCDelegate().getEntry(model.getEventId());

            populateModel(courtLogCRUDValue);
            clep.populateEditableModel(courtLogCRUDValue);
        }

        log.debug("stepInitialise::finished");
    }

    /**
     * Life-cycle method that is executed whenever the screen is made invisible.
     * 
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeactivate()
     */
    public final void stepDeactivate() throws CSRecoverableException {
        log.debug("stepDeactivate::start");

        clep.stepDeactivate();
        moveScreenToModel();

        log.debug("stepDeactivate::finished");
    }

    /**
     * Life-cycle method to ensure user input is valid.
     * 
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepValidate()
     */
    public void stepValidate() throws CSRecoverableException {
        log.debug("stepValidate::start");

        clep.stepValidate();

        // Validate log date
        if (logAuditPanel != null) {
            logAuditPanel.stepValidate();
        }

        log.debug("stepValidate::finished");
    }

    /**
     * Life-cycle method to enable/disable screen components depending on user
     * input, this implementation ensures that the button panel is enabled as
     * required, or sets the panel to modified if required.
     * 
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("stepUpdateViewState::start");

        clep.stepUpdateViewState();

        if (buttonPanel != null) {
            buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
        } else {
            setModified(isMandatoryFieldsCompleted());
        }

        log.debug("stepUpdateViewState::finished");
    }

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * generally as a result of the user clicking the OK/Cancel buttons. It
     * constructs a CourtLogCRUDValue from the model and calls the appropriate
     * method on the business delegate to update or create a new entry.
     * 
     * @param update
     *            A <code>boolean</code> parameter used to indicate if the
     *            data should be persisted (<i>true</i>) or not (<i>false</i>).
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise(boolean)
     * @see #createCRUDFromModel()
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        log.debug("stepDeinitialise::start - update = " + update);
        
        if(specificEventTypeValidation()){
	        clep.stepDeinitialise(update);
	
	        if (update) {
	            CourtLogCRUDValue courtLogCRUDValue = createCRUDFromModel();
	            persistCourtLogCRUDValue(courtLogCRUDValue);
	        }
        
        }
        log.debug("stepDeinitialise::finished");
    }

	private Boolean specificEventTypeValidation() throws UserCancelException {
		if(null != model.getEventType() && model.getEventType().equals(SentenceForBroadcastEventType)){
			try {
				CaseBasicValue caseBV = getCCDelegate().getCase(model.getXac().getApplicationCaseModel().getCaseId());
			
		        if(null == caseBV.getTelevisedAppGranted() || SentenceForBroadcastAppGrantedNo.equals(caseBV.getTelevisedAppGranted())){
			        JOptionPane.showMessageDialog(new JFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.sentencebroadcast.telappnotgranted"), "The Court Log Event cannot be saved", JOptionPane.ERROR_MESSAGE);
			        return false;
		        }
		        //if there already exists one (i.e. the 
		        if(null != caseBV.getTelevisedRemarksFilmed() && !model.isInEditMode()){
			        JOptionPane.showMessageDialog(new JFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.sentencebroadcast.duplicateEvent"), "Court log event already exists", JOptionPane.ERROR_MESSAGE);
			        return false;
		        }
			} catch (CaseControllerException e) {
				log.error("specificEventTypeValidation - ERROR retrieving case- " + model.getXac().getApplicationCaseModel().getCaseId() +  " " + model.getEventId());
			}
        }
		return true;
	}

    /**
     * Extracted method used to save the changes made to the passed in court log
     * CRUD value to the database. If we are editting, then an update call will
     * be made, after first populating the primary key, otherwise a new log
     * entry will be created.
     * 
     * @param courtLogCRUDValue
     * @throws CourtLogBusinessException
     */
    protected void persistCourtLogCRUDValue(CourtLogCRUDValue courtLogCRUDValue) throws CourtLogBusinessException {
        if (model.isInEditMode()) {
            log.debug("persistCourtLogCRUDValue(CourtLogCRUDValue)::updating entry - " + model.getEventId());
            courtLogCRUDValue.setLogEntryId(model.getEventId());
            getCLCDelegate().updateEntry(courtLogCRUDValue);
        } else {
            log.debug("persistCourtLogCRUDValue(CourtLogCRUDValue)::creating a new entry");
            getCLCDelegate().newEntry(courtLogCRUDValue);
        }
        
        persistEventLogExtraProcessing(courtLogCRUDValue);
        
    }

	private void persistEventLogExtraProcessing(CourtLogCRUDValue courtLogCRUDValue) {
		log.debug("persistEventLogExtraProcessing:start");
        String eventType = model.getEventType();
        if(null != eventType && eventType.equals(SentenceForBroadcastEventType)){
        	// If SentenceForBroadcastEventType - need to update the case object/cases table 
	        processSentenceForBroadcast(courtLogCRUDValue);
        }
	}

	private void processSentenceForBroadcast(CourtLogCRUDValue courtLogCRUDValue) {
		log.debug("processSentenceForBroadcast:start");
		try {
			// get latest case value
			CaseBasicValue caseBV = getCCDelegate().getCase(courtLogCRUDValue.getCaseId());

			// retrieve the value selected by the user. It has been stored on the courtLogCRUDValue 
			Map propertyMap = (Map) courtLogCRUDValue.getPropertyMap();
			Map sentenceRecordedOptionsMap = (Map)propertyMap.get(SentenceForBroadcastOptions);				
			String sentenceRecordedTypeMap = (String) sentenceRecordedOptionsMap.get(SentenceForBroadcastType);
			
			String sentencingeRemarks = null;
			if(null != sentenceRecordedTypeMap && "" != sentenceRecordedTypeMap){
				if(sentenceRecordedTypeMap.equals(SentenceForBroadcastYes)){
					sentencingeRemarks = ("Y");
				}
				else{
					sentencingeRemarks = ("N");
				}					
			}
			// only set if the value has changed or is not null
			if(null != sentencingeRemarks && !sentencingeRemarks.equals(caseBV.getTelevisedRemarksFilmed())){
				caseBV.setTelevisedRemarksFilmed(sentencingeRemarks);
				getCCDelegate().updateCase(caseBV, "XHIBIT");
			}
		} catch (CaseControllerException e) {
			log.error("processSentenceForBroadcast - ERROR retrieving/updating case - " + courtLogCRUDValue.getCaseId());
		}
		 log.debug("processSentenceForBroadcast:end");
	}
    
    
    //
    // Delegate Management
    //
    private static CaseControllerBeanBusinessDelegate getCCDelegate() {
        return XhibitDelegateHelper.getCaseDelegate();
    }

    /**
     * Life-cycle method executed whenever the screen is made visible. This
     * generally entails moving data from the model to the screen. Finally, a
     * life-cycle method is executed to manage the enabled state of any screen
     * components.
     * 
     * @throws CSRecoverableException
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepActivate()
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("stepActivate::start");

        clep.stepActivate();
        moveModelToScreen(); 
        stepUpdateViewState();

        log.debug("stepActivate::finished");
    }

    /**
     * Helper method used to allow the free text of the log audit panel (if it
     * has been created) to be altered.
     * 
     * @param text
     *            The text to set the free text to.
     */
    public void setFreeText(String text) {
        if (logAuditPanel != null) {
            log.debug("setFreeText - Setting free text to \"" + text + "\"");
            logAuditPanel.setFreeTextString(text);
        }
    }

    /**
     * Performs the stepUpdateViewState method call, but with errors handled.
     * This is for use by event listeners that do not want to handle their own
     * exceptions, but instead leave to the default error handling of the
     * <code>XHIBITErrorHandler</code>.
     * 
     * @see #stepUpdateViewState()
     * @see uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler
     *      .handleError(java.lang.Exception);
     */
    protected final void stepUpdateViewStateHandleExceptions() {
        log.debug("stepUpdateViewStateHandleExceptions::start");

        try {
            stepUpdateViewState();
        } catch (CSRecoverableException ex) {
            XHIBITErrorHandler.handleError(ex);
        }

        log.debug("stepUpdateViewStateHandleExceptions::finished");
    }

    /**
     * Helper method used to create a new <code>CourtLogCRUDValue</code> and
     * populate it with the values from the model
     * 
     * @return
     * @throws CSRecoverableException
     *             if there are any problems populating the
     *             <code>CourtLogCRUDValue</code> from the model.
     * @see #populateCRUD(uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue)
     */
    protected CourtLogCRUDValue createCRUDFromModel() throws CSRecoverableException {
        log.debug("createCRUDFromModel::start");

        CourtLogCRUDValue courtLogCRUDValue = new CourtLogCRUDValue();
        populateCRUD(courtLogCRUDValue);

        if (log.isDebugEnabled()) {
            log.debug("createCRUDFromModel::finished - returning CRUD - " + courtLogCRUDValue);
        }

        return courtLogCRUDValue;
    }

    /**
     * Accessor method for the <code>CourtLogEventLevelPanel</code> created in
     * the constructor.
     * 
     * @return The <code>CourtLogEventLevelPanel</code> created in the
     *         constructor.
     */
    protected final CourtLogEventLevelPanel getCourtLogEventLevelPanel() {
        return clep;
    }

    /**
     * Accessor method for the <code>CourtLogAuditPanel</code>
     * 
     * @return The <code>CourtLogAuditPanel</code>, or <i>null</i> if it was
     *         not created (if the parent was <i>null</i> originally).
     */
    protected CourtLogAuditPanel getLogAuditPanel() {
        return this.logAuditPanel;
    }

    /**
     * Moves the component details from the model to the screen if the model is
     * in edit mode. This implementation only populates the log audit panels
     * details with the details from the model, provided it is not <i>null</i>.
     */
    protected void moveModelToScreen() {
        log.debug("moveModelToScreen::start");

        if ((logAuditPanel != null) && model.isInEditMode()) {
            logAuditPanel.setFreeTextString(model.getFreeText());
            logAuditPanel.setDateDefault(model.getDateTime());
            logAuditPanel.setTimeDefault(model.getDateTime());
            logAuditPanel.setScheduledHearingId(model.getScheduledHearingId());
        }

        log.debug("moveModelToScreen::finished");
    }

    /**
     * Moves the component details from the screen to the model. This
     * implementation only moves the log audit panels details to the model,
     * provided it is not <i>null</i>.
     * 
     * @throws CSRecoverableException
     */
    protected void moveScreenToModel() throws CSRecoverableException {
        log.debug("moveScreenToModel::start");

        if (logAuditPanel != null) {
            model.setDateTime(logAuditPanel.getDateTime());
            model.setFreeText(logAuditPanel.getFreeTextString());
            model.setScheduledHearingId(logAuditPanel.getScheduledHearingId());
        }

        log.debug("moveScreenToModel::finished");
    }

    /**
     * Used to determine if all of the mandatory fields have been complemeted.
     * This is primarily used to determine if the okay button should be enabled
     * or not.
     * 
     * The default implementation simply returns <i>true</i>, indicating that
     * all mandatory fields are completed by default.
     * 
     * @return <i>true</i> if all mandatory fields have been completed,
     *         <i>false</i> otherwise.
     */
    protected boolean isMandatoryFieldsCompleted() {
        log.debug("isMandatoryFieldsCompleted::start::finished - returning true");

        return true;
    }

    /**
     * Method used to allow sub-classes to set properties to the
     * <code>CourtLogCRUDValue</code> other than the standard ones (set in the
     * populateCRUD method) from the model.
     * 
     * This method should not be called directly, as it is handled by the
     * framework. By default, this method has no implementation.
     * 
     * @param propertyMap
     *            The <code>Map</code> that the custom properties are to be
     *            set to.
     * @throws CSRecoverableException
     *             If an implementing class has problems setting the custom
     *             properties.
     * @see #populateCRUD(uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue)
     */
    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        // no specific properties by default...
    }

    /**
     * Method used to allow sub-classes to set properties to the model from the
     * <code>CourtLogCRUDValue</code> other than the standard ones, populated
     * by the populateModel method.
     * 
     * This method should not be called directly, as it is handled by the
     * framework. By default, this method has no implementation.
     * 
     * @param propertyMap
     *            The <code>Map</code> that the custom properties are to be
     *            taken from.
     * @throws CSRecoverableException
     *             If an implementing class has problems retrieving the custom
     *             properties.
     * @see #populateModel(uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue)
     */
    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // no specific properties by default...
    }

    /**
     * Creates the panel title label if not already created, and returns it. It
     * is created from the panel text in the model.
     * 
     * @return The <code>JLabel</code> of the panel title label.
     */
    protected JLabel getPanelTitle() {
        if (panelTitleLabel == null) {
            log.debug("getPanelTitle - Creating panel title with label of " + model.getPanelText());
            panelTitleLabel = new PanelTitleLabel(model.getPanelText());
        }

        return panelTitleLabel;
    }

    /**
     * Helper method used to acquire the parent frame of the parent
     * <code>XDialog</code> passed into this panels constructor (if not
     * <i>null</i>).
     * 
     * @return The parent <code>Frame</code>
     */
    protected final Frame getParentFrame() {
        return ((this.parent != null) ? this.parent.getParentFrame() : null);
    }

    /**
     * Delegate method used to acquire the court log controller business
     * delegate so subsequent calls may use it.
     * 
     * @return The <code>CourtLog2ControllerBeanBusinessDelegate</code>
     * @see uk.gov.courtservice.xhibit.client.xhibitapplication
     *      .XhibitDelegateHelper.getCourtLogDelegate2()
     */
    protected final CourtLog2ControllerBeanBusinessDelegate getCLCDelegate() {
        return XhibitDelegateHelper.getCourtLogDelegate2();
    }

    /**
     * Private utility method used to populate the passed in
     * <code>CourtLogCRUDValue</code> with all of the standard details from
     * the model.
     * 
     * If custom properties are required to be set, these can be done in an
     * overridden version of <code>populateCRUDProperties()</code>.
     * 
     * @param courtLogCRUDValue
     *            The <code>CourtLogCRUDValue</code> that the values are to
     *            written to
     * @throws CSRecoverableException
     *             If there is a problem loading the custom properties for a
     *             panel.
     * @see #populateCRUDProperties(java.util.Map)
     */
    private final void populateCRUD(CourtLogCRUDValue courtLogCRUDValue) throws CSRecoverableException {
        log.debug("populateCRUD::start");

        clep.populateCRUD(courtLogCRUDValue);

        courtLogCRUDValue.setCaseId(model.getXac().getApplicationCaseModel().getCaseId());
        courtLogCRUDValue.setEntryDate(model.getDateTime().getTime());
        courtLogCRUDValue.setEntryFreeText(model.getFreeText());
        courtLogCRUDValue.setEventType(new Integer(model.getEventType()));
        courtLogCRUDValue.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        courtLogCRUDValue.setVersion(model.getVersion());
        courtLogCRUDValue.setScheduledHearingId(model.getScheduledHearingId());

        // finally, allow for any custom properties to be set...
        populateCRUDProperties(courtLogCRUDValue.getPropertyMap());

        // @todo - possibly print the crud if debug enabled...
        log.debug("populateCRUD::finished");
    }

    /**
     * Utility method used to populate the panels model with the standard
     * properties from the passed in <code>CourtLogCRUDValue</code>.
     * 
     * If custom properties are required to be set, these can be done in an
     * overridden version of <code>populateModelProperties()</code>.
     * 
     * @param courtLogCRUDValue
     *            The <code>CourtLogCRUDValue</code> that the values are to be
     *            taken from.
     * @throws CSRecoverableException
     *             If there is a problem loading the custom properties for a
     *             panel.
     * @see #populateModelProperties(java.util.Map)
     */
    protected final void populateModel(CourtLogCRUDValue courtLogCRUDValue) throws CSRecoverableException {
        log.debug("populateModel::start");

        final Calendar logDate = Calendar.getInstance();
        logDate.setTime(courtLogCRUDValue.getEntryDate());
        model.setDateTime(logDate);

        model.setEventId(courtLogCRUDValue.getLogEntryId());
        model.setEventType(courtLogCRUDValue.getEventType().toString());
        model.setFreeText(courtLogCRUDValue.getEntryFreeText());
        model.setVersion(courtLogCRUDValue.getVersion());
        model.setScheduledHearingId(courtLogCRUDValue.getScheduledHearingId());

        // finally, allow for any custom properties to be set...
        populateModelProperties(courtLogCRUDValue.getPropertyMap());

        // @todo - possibly print the model if debug enabled...
        log.debug("populateModel::finished");
    }
}
