package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: HearingRecordPanel
 * </p>
 * <p>
 * Description: The panel for the entire hearing record with all the forms.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class HearingRecordPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	private static final String NO = "N";

	private final Logger log = CSServices.getLogger(HearingRecordPanel.class);

	// only to be set in the constructor
	private final HearingRecordModel model;

	private HearingDetails hearingDetails;

	private DefendantDetails defDetails;

	private BailCustody bailCustody;

	private CaseGeneral caseGeneral;

	private Other other;

	private Representation representation;

	private XhibitApplicationController xac;

	private JTabbedPane hrTabbedPane;
	
	private HearingRecordDialog parent;
	
	public HearingRecordPanel(HearingRecordDialog parent, HearingRecordModel model, XhibitApplicationController xac) throws CSRecoverableException {
		super();
		this.parent = parent;
		this.model = model;
		this.model.setIsAppealType(model.getCaseNumber());
		this.xac = xac;
		model.setHearingRecordPanel(this);
		final int width = 725;
		final int height = 500;
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		stepInitialise();
		jbInit();
	}

	public void jbInit() throws CSRecoverableException {
		final JTabbedPane hrTabbedPane = getTabPane();

		this.defDetails = new DefendantDetails(this.model);
		this.hearingDetails = new HearingDetails(this.model, xac);
		this.bailCustody = new BailCustody(this.model);
		this.caseGeneral = new CaseGeneral(this.model);
		this.other = new Other(this.model);
		this.representation = new Representation(this.model);
		
		if (!model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			this.defDetails.setScreenReadOnly();
			this.hearingDetails.setScreenReadOnly();
			this.bailCustody.setScreenReadOnly();
			this.other.setScreenReadOnly();
			this.representation.setScreenReadOnly();
			this.parent.setOkEnabled(false);
		}

		hrTabbedPane.add(getResource("caseGeneralTab"), caseGeneral);
		hrTabbedPane.add(getResource("hearingDetailsTab"), hearingDetails);

		if (model.isAppealType()) {
			hrTabbedPane.add(getResource("appellantDetailsTab"), defDetails);
		} else {
			hrTabbedPane.add(getResource("defendantDetailsTab"), defDetails);
		}

		hrTabbedPane.add(getResource("representationTab"), representation);
		hrTabbedPane.add(getResource("bailCustodyTab"), bailCustody);
		hrTabbedPane.add(getResource("otherTab"), other);

		setLayout(new BorderLayout());
		add(hrTabbedPane, BorderLayout.CENTER);
		setSize(hrTabbedPane.getPreferredSize());
	}

	public JTabbedPane getTabPane() {
		if (hrTabbedPane == null) {
			hrTabbedPane = new JTabbedPane();
		}
		return hrTabbedPane;
	}
	
	public void stepInitialise() throws CSRecoverableException {
		log.debug("HearingId " + model.getHearingId());
		log.debug("DefendantId " + model.getDefendantId());
	}

	public void stepDeactivate() throws CSRecoverableException {
		log.debug("Saving data from screens in stepDeactivate");
		
		// Only attempt to save data if the case has been loaded in editable mode
		if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			// save data from screens to HearingRecordUpdateValue in model.
			this.hearingDetails.setUpdateData();
			this.defDetails.setUpdateData();
			this.bailCustody.setUpdateData();
			this.other.setUpdateData();
			this.representation.setUpdateData();
			
			// reintroducing removed code as need to save the Hearing Record to the database here as per it did originally
			if (model != null) {
				if (model.isUpdated() || model.isDurationUpdated()) { 				
					model.getHearingRecordVal().getHearingRecordUpdateValue().getDefHearingRecordValue()
							.setFormACourtClerk(XhibitSingleton.getInstance().getUserSession()
									.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
					log.debug("Changes made.  Calling update hr");
					final XhbDirectionsForCaseBasicValue dfcbv = model.getHearingRecordUpdateVal()
							.getDirectionsForCaseValue().getDirectionsForCaseBasicValue();
	
					// Need to check if the user has tried to remove the
					// estimated trial time.
					// if so warn the user that it will not be removed but can
					// be removed from CL.
					if ((dfcbv != null) && (dfcbv.getTrialTimeEstimate() != null)
							&& (dfcbv.getTrialTimeEstimate().floatValue() == -1)) {
						model.getHearingRecordUpdateVal().getDirectionsForCaseValue().setCourtLogCRUDValues(null);
						JOptionPane.showMessageDialog(this,
								getErrorResource("gui.hearingrecord.timeestimatenotchanged"),
								getErrorResource("gui.hearingrecord.timeestimatenotchanged.title"),
								JOptionPane.WARNING_MESSAGE);
						
					}				
					// Reset the status back to "N"
					model.getHearingRecordUpdateVal().getDefHearingRecordValue().setFormAStatus(NO);
					XhibitDelegateHelper.getHearingDelegate().updateHearingRecord(model.getHearingRecordUpdateVal(), model.getDefOnCaseBasicValue(),
							model.getHearingRecordVal().getHearingRecordDisplayValue()
							.getHrDefendantValue().getInCustody(),XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				}
				
			}
			((LinkedHearingSummaryPanel) model.getCallingClass()).modelCallback(model);
		}
	}

	public void stepValidate() // throws CSValidationException,
	// CSRecoverableException
	{
		// not implemented
	}

	public void stepUpdateViewState()// throws CSRecoverableException
	{
		// not implemented
	}

	public void stepDeinitialise(boolean update)// throws CSRecoverableException
	{
		// not implemented
	}

	/**
	 * Method that delegates down to populateScreens, passing a value of
	 * <i> true</i>, indicating that we do want to a full clean population of
	 * the screens
	 * 
	 * @throws A
	 *             <code>CSRecoverableException</code> if the delegated method
	 *             throws the same error
	 * @see populateScreens(boolean populateAll)
	 */
	public void stepActivate() throws CSBusinessException // throws
															// CSRecoverableException
	{
		// make a call indicating we wish to do a full populate/update of
		// screens
		populateScreens(true, false);

		final UpdateListener updateListener = new UpdateListener(this.model);

		// add the listeners
		this.hearingDetails.addListeners(updateListener);
		this.defDetails.addListeners(updateListener);
		this.bailCustody.addListeners(updateListener);
		this.other.addListeners(updateListener);
		this.representation.addListeners(updateListener);
	}

	/**
	 * Method to populate all of the data to be displayed on the screen. The
	 * passed in parameter determines whether we wish to refresh everything (as
	 * in the first time into the screen) or just those details that could
	 * change from a subsequently displayed popup.
	 * 
	 * @param populateAll
	 *            A <code>boolean</code> value of <i>true</i> if we want to
	 *            refresh everything, or <code>false</code> if we only want to
	 *            refresh the read-only components
	 * @param updateModelData boolean to determine if require to update the data
	 *            
	 * @throws CSRecoverableException
	 */
	// would have preferred to overload, but if the framework later gets
	// changed
	// to overload with a boolean this would cause problems, so just adding
	// a
	// new method instead!
	public void populateScreens(boolean populateAll, boolean updateModelData) throws CSBusinessException {
		// populating screens
		// always refresh the general case details tab, as there are no
		// writeable components
		// retrieve the data
		
		// CTX-2830 update the data when returning from a popup screen (edit screen)
		if(updateModelData){
			model.setHearingRecordVal(XhibitDelegateHelper.getHearingDelegate().retrieveHearingRecord(
					model.getHearingId(), model.getDefendantId(), XhibitSingleton.getInstance().getUserSession()
							.getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
		}
		
		this.caseGeneral.populateScreen();

		if (populateAll) {
			this.hearingDetails.populateScreen();
			this.defDetails.populateScreen();
			this.representation.populateScreen();
			this.bailCustody.populateScreen();
			this.other.populateScreen();
			this.other.stepUpdateViewState();
		} else {
			this.hearingDetails.populateReadOnlyComponents();
			this.defDetails.populateReadOnlyComponents();
			this.representation.populateReadOnlyComponents();
			// not needed, as there are no read-only components!
			// this.bailCustody.populateScreen();
			this.other.populateReadOnlyComponents();
			// see if some of the components need to be activated
			this.other.stepUpdateViewState();
		}

	}

	public BailCustody getBailCustody() {
		return this.bailCustody;
	}

	/**
	 * Accessor method for the hearing details panel, to allow direct calling of
	 * methods from actions
	 * 
	 * @return The <code>HearingDetails</code> panel
	 */
	public HearingDetails getHearingDetails() {
		return this.hearingDetails;
	}

	/**
	 * Accessor method for the Representation panel, to allow direct calling of methods
	 * from actions
	 * 
	 * @return The <code>Other</code> panel
	 */
	public Representation getRepresentation() {
		return this.representation;
	}
	
	/**
	 * Accessor method for the Other panel, to allow direct calling of methods
	 * from actions
	 * 
	 * @return The <code>Other</code> panel
	 */
	public Other getOther() {
		return this.other;
	}
	
	/**
	 * Accessor method for the DefendantDetails panel, to allow direct calling of methods
	 * from actions
	 * 
	 * @return The <code>DefendantDetails</code> panel
	 */
	public DefendantDetails getDefDetails() {
		return this.defDetails;
	}

	/**
	 * Gets the resource for the given key from the HearingRecord resource
	 * bundle.
	 * 
	 * @param key
	 *            the key to lookup in the HearingRecord resource bundle
	 * @return the resource string for the given key
	 */
	private String getResource(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.HearingRecord, key);
	}
	
	/**
	 * Gets the resource for the given key from the ErrorText resource bundle.
	 * 
	 * @param key
	 *            the key to lookup in the ErrorText resource bundle
	 * @return the resource string for the given key
	 */
	private String getErrorResource(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.ErrorText, key);
	}

	/**
	 * A class that implements the required listeners for use as the single
	 * update listener for use by this form
	 * 
	 * @see java.awt.event.ActionListener
	 * @see javax.swing.event.DocumentListener
	 * @see mseries.ui.MChangeListener
	 */
	protected class UpdateListener implements java.awt.event.ActionListener, javax.swing.event.DocumentListener,
			mseries.ui.MChangeListener, javax.swing.event.TableModelListener {
		private final HearingRecordModel hearingRecordModel;

		/**
		 * Only constructor, sets up the required
		 * <code>HearingRecordModel</code> for use by the listener events
		 * 
		 * @param model
		 *            A <code>HearingRecordModel</code>
		 */
		public UpdateListener(final HearingRecordModel hearingRecordModel) {
			this.hearingRecordModel = hearingRecordModel;
		}

		/** @see ActionListener */
		public void actionPerformed(final java.awt.event.ActionEvent e) {
			this.hearingRecordModel.setUpdated(true);
		}

		/** @see DocumentListener */
		public void changedUpdate(final javax.swing.event.DocumentEvent e) {
			this.hearingRecordModel.setUpdated(true);
		}

		/** @see DocumentListener */
		public void removeUpdate(final javax.swing.event.DocumentEvent e) {
			this.hearingRecordModel.setUpdated(true);
		}

		/** @see DocumentListener */
		public void insertUpdate(final javax.swing.event.DocumentEvent e) {
			this.hearingRecordModel.setUpdated(true);
		}

		/** @see MChangeListener */
		public void valueChanged(final mseries.ui.MChangeEvent e) {
			this.hearingRecordModel.setUpdated(true);
		}

		/** @see TableModelListener */
		public void tableChanged(final javax.swing.event.TableModelEvent tme) {
			this.hearingRecordModel.setUpdated(true);
		}
	}
}
