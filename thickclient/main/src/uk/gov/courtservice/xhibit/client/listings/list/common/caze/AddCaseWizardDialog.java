package uk.gov.courtservice.xhibit.client.listings.list.common.caze;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.schedule.addhearing.AddHearingWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;


/**
 * Wizard to add a U Case.
 * Base on AddHearingWizardDialog.
 * @see AddHearingWizardDialog
 * 
 * @author westalll
 *
 */

public class AddCaseWizardDialog extends XWizardDialog {
	
 
	private static final long serialVersionUID = 1L;
	private AddCaseDataModel model = new AddCaseDataModel();
	private final String U_CASE = "U";
	private final String B_CASE = "B";
	private final String userDisplayName = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
	

	public AddCaseWizardDialog(XhibitApplicationController xac, final String caseType, final AddCaseDataModel model) throws CSRecoverableException {
        super(xac, "", true);
        this.model = model;
        //Only enable the buttons we need.
        stepUpdateViewState();
        
		if (caseType.equals(U_CASE)) {
			setTitle(getBundleEntry("AddUCaseTitleBarLabel"));
		} else {
			setTitle(getBundleEntry("AddBCaseTitleBarLabel"));
		}
        

        // set up the model...
        model.setXac(xac);
        AddCaseEnterCaseNumber caseNumber = new AddCaseEnterCaseNumber(model, super.getButtonPanel(), caseType);
       

        // add the the required panels to a Collection...
        final Collection<XPanel> panels = new ArrayList<XPanel>();
        panels.add(caseNumber);

        addBodyPanels(panels);
        setWizardPanelImage("xwizardimage.jpg");
        pack();
    }
	
	@Override
	public void finish() throws CSRecoverableException {
		setLatestEvent(XWizardDialog.FINISH_EVENT);
		if (xPanels[currentPanel] instanceof XPanel) {
			XPanel xp = (XPanel) xPanels[currentPanel];
			xp.stepValidate();
			xp.stepDeactivate();
			xp.stepDeinitialise(true);

			CaseBasicValue cs = null;
			try {
				cs = XhibitDelegateHelper.getListingsDelegate().createNewUorBCase(createAddCaseVauelObject(), userDisplayName);
				model.setCaseId(cs.getCaseId());
			} catch (final Exception lce) {
				if (model.getCaseType().equals(U_CASE)) {
					XMessageBox.alert(model.getXac(), getBundleEntry("ErrorSaveTitle"), false, XMessageBox.ICONERROR,
							getBundleEntry("ErrorSaveUCaseMessage"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
				} else {
					XMessageBox.alert(model.getXac(), getBundleEntry("ErrorSaveTitle"), false, XMessageBox.ICONERROR,
							getBundleEntry("ErrorSaveBCaseMessage"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
				}

			}
		}
	}

	private AddCaseValue createAddCaseVauelObject() {
		AddCaseValue acv = new AddCaseValue();
		acv.setCaseNumber(null);
		acv.setCaseType(model.getCaseType());
		acv.setCaseTitle(model.getCaseTitle());
		acv.setHearingType(model.getHearingTypeCode());
		acv.setCourtID(model.getCourtId());
		acv.setCaseTitle(model.getCaseTitle());
		acv.setHearingTypeId(model.getHearingTypeId());
		acv.setCreateCaseOnCrest(false);
		return acv;
	}
    
    
    /**
     * Change the state of the screen components depending upon available data
     */
    private void stepUpdateViewState() {
    	getButtonPanel().getBack().setVisible(false);
    	getButtonPanel().getNext().setVisible(false);
    }
    

    /**
     * Returns a value from the resource bundle associated with this task
     * 
     * @param param
     * @return
     */
    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.Listings, param);
    }
}

