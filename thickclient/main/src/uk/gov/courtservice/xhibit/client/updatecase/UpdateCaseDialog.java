package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenUpdateCasePropertiesAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: UpdateCaseDialog is the component top level class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * 
 * @version 1.1 rework for bug_release_x010102_030113 - adding documentation to
 *          clarify design and to-do tasks. - providing better support for empty
 *          strings and null values.
 */
public class UpdateCaseDialog extends XDialog {
	
	private static final long serialVersionUID = 1L;

	public static final String HEARINGPROGRESS_TO_STATIC_RSC_PREFIX = "statics_hp_";

	public static final String HEARINGPROGRESS_RSC_PREFIX = "hp_";

	public static final String HEARINGPROGRESS_OFFENCE_GROUP_CODE_PREFIX = "offence_group_code_";

	public static final String HEARINGPROGRESS_CLASS_CODE_PREFIX = "class_code_";

	private final Logger log = CSServices.getLogger(UpdateCaseDialog.class);

	// only to be set in the constructor
	protected final OpenUpdateCasePropertiesAction openingAction;

	protected final XhibitApplicationController xac;

	// @TODO - do we really want internal debug enabled?
	// Shouldn't this be set up in the log4j properties anyway?
	protected boolean internalDebug = true;

	private UpdateCasePanel updateCaseXPanel;

	/**
	 * This is THE constructor of the Update Case / Maintain Hearing Header
	 * component. May only be called by an XAction that passes itself as
	 * argument. The action's exception handling will be used to report any
	 * instantiation exceptoins. The action's model must be an
	 * ApplicationCaseModel that holds the id of the scheduled hearing to
	 * retrieve. Retrieval of that id is done using the getScheduledHearingId()
	 * method.
	 * 
	 * @param openingAction
	 * @throws CSRecoverableException
	 */
	public UpdateCaseDialog(OpenUpdateCasePropertiesAction openingAction) throws CSRecoverableException {
		// By passing a kind of JFrame as first argument to the super
		// constructor, this window becomes modal relative to it.
		// The JFrame passed on represents the 'currently active' XHIBIT
		// application instance (others may be in the background
		// and be made active.)
		super((XhibitApplicationController) openingAction.getController(), "Case Properties", true, APPLYOKCANCEL,
				DEFAULTYES);
		// the 'update case properties' title will, once the resource file has
		// been loaded, be reset
		try {
			this.openingAction = openingAction;
			
			int height = 550;
			int width = 550;
			try {
				this.xac = (XhibitApplicationController) openingAction.getController();
				String name = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "name");

				if (name == null) {
					log.error(
							"MaintainHearingHeader properties file does not contain property 'name'! Default name 'Update Case Properties' set.");
				} else {
					super.setTitle(name);
				}

				try {
					width = Integer.parseInt(XHIBITConstant.getProperty(XhibitProperties.MaintainHearingHeader,
							"updateCaseDialogWidth"));
				} catch (Exception e) {
					throw (new CSConfigurationException("Width property not parseable to int.", e));
				}

				try {
					width = Integer.parseInt(XHIBITConstant.getProperty(XhibitProperties.MaintainHearingHeader,
							"updateCaseDialogHeight"));
				} catch (Exception e) {
					throw (new CSConfigurationException("Height property not parseable to int.", e));
				}

			} catch (Exception e) {
				throw (new CSConfigurationException("Exception during instantiation of UpdateCase!", e));
			}
			// This is THE method call of importance; creating the XPanel
			// containing a tabbed inner pannel.
			// The XPanel has the Xstep life cycle methods that will load
			// and save data as identified from
			// the CourtLogHeaderValue passed on.
			setSize(width, height);
			centreDialog();
			setResizable(false);
			setModal(true);
			createAndAddTabs();
		} catch (Exception e) {
			log.error(e);
			throw (new CSRecoverableException("gui.updateCaseDialog.ConstructorFailed",
					"Exception during instantiation of UpdateCase!", e));
		}
	}

	private void createAndAddTabs() throws Exception {
		try {
			updateCaseXPanel = new UpdateCasePanel(this);
			addBodyPanel(updateCaseXPanel);
			validate();
			setVisible(true);
		} catch (Exception e) {
			log.error(e);
			throw (e);
		}
	}

	private void recreatedAndAddTabs() throws Exception {
		createAndAddTabs();
	}

	public void applyClicked(ActionEvent e) throws Exception {
		// overriding the super's applyCicked(...) to delegate calls to the
		// inner XPanel (UpdateCase), rather than the XDialog's XPanel
		// save any and all updates, refresh screen's data
		if (internalDebug)
			log.debug("UpdateCaseDialog.applyClicked(ActionEvent " + e
					+ ") - triggers updateCaseXPanel.stepUpdateViewState()");
		this.buttonClicked = XDialog.DEFAULTAPPLY;
		try {
			int selectedTab = updateCaseXPanel.getUpdateCaseTabbedPane().getSelectedIndex();
			this.updateCaseXPanel.stepValidate();
			boolean cleanSave = this.updateCaseXPanel.stepDeactivate(true);
			if (!cleanSave) {
				String msgStr = "Exception whlist saving (apply) case/hearing schedule data. Not closeing the comonent.";
				log.error(msgStr);
			} else {
				// only if clean save, else we would loose the unsaved changes
				// on the screen.
				this.recreatedAndAddTabs();
				updateCaseXPanel.getUpdateCaseTabbedPane().setSelectedIndex(selectedTab);
			}
		} catch (CSValidationException csVe) {
			XHIBITConstant.handleError(csVe);
		} catch (CSRecoverableException csRe) {
			XHIBITConstant.handleError(csRe);
		}
	}

	public void okClicked(ActionEvent e) throws Exception {
		// overriding the super's applyCicked(...) to delegate calls to the
		// inner XPanel (UpdateCase), rather than the XDialog's XPanel
		// save any and all updates and close component dialog
		if (this.internalDebug)
			log.debug("UpdateCaseDialog.okClicked(ActionEvent " + e + ") - triggers updateCaseXPanel.stepDeactivate()");
		this.buttonClicked = XDialog.DEFAULTOK;
		try {
			this.updateCaseXPanel.stepValidate();
			boolean cleanSave = this.updateCaseXPanel.stepDeactivate(true);
			if (!cleanSave) {
				String title = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "onSaveFailed.title");
				String message = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
						"onSaveFailed.message");
				JOptionPane.showMessageDialog(this.getParentFrame(), message, title, JOptionPane.ERROR_MESSAGE);
			} else {
				// clease save, close the window
				this.dispose();
			}
		} catch (CSValidationException csVe) {
			XHIBITConstant.handleError(csVe);
		}
	}

	public void cancelClicked(ActionEvent e) throws Exception {
		if (!this.updateCaseXPanel.cancelledPrompt) {
			if (internalDebug)
				log.debug("UpdateCaseDialog.cancelClicked(ActionEvent " + e + ")");

			if (this.buttonClicked != XDialog.DEFAULTAPPLY)
				this.buttonClicked = XDialog.DEFAULTCANCEL;

			if (this.updateCaseXPanel.getModified()) {
				if (internalDebug)
					log.debug(
							"updateCaseXPanel.getModified() == true. Now questioning the user on whether or not to save the changes.");
				int stillSave = JOptionPane.showConfirmDialog(this,
						XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"onCancelClicked.saveChangedDataQuestion"),
						XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
								"onCancelClicked.saveChangedDataWindowTitle"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				switch (stillSave) {
				case JOptionPane.YES_OPTION:
					this.okClicked(e);
					break;
				case JOptionPane.NO_OPTION:
					super.clearStatusBarScreenCode();
					dispose();
					break;
				case JOptionPane.CLOSED_OPTION:
				default:
					// do nothing and return to current screen
					this.updateCaseXPanel.cancelledPrompt = true;
					break;
				}
			} else {
				super.clearStatusBarScreenCode();
				this.dispose();
			}
		} else {
			// reset prompt boolean
			this.updateCaseXPanel.cancelledPrompt = false;
		}
	}

	public XhibitApplicationController getXac() {
		return xac;
	}
}