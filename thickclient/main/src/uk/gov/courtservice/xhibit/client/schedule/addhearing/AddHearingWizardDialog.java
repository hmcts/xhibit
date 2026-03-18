package uk.gov.courtservice.xhibit.client.schedule.addhearing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class AddHearingWizardDialog extends XWizardDialog {
    public AddHearingWizardDialog(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, "", true);
        setTitle(getBundleEntry("AddHearingTitleBarLabel"));

        // set up the model...
        final AddHearingModel model = new AddHearingModel();
        model.setTime(new Date());
        model.setXAC(xac);
        AddHearingEnterCaseNumber caseNumber = new AddHearingEnterCaseNumber(model, super.getButtonPanel());
        AddHearingSelectDefendants defendants = new AddHearingSelectDefendants(model, super.getButtonPanel());
        AddHearingConfirmationPanel confirmation = new AddHearingConfirmationPanel(model, super.getButtonPanel());

        // add the the required panels to a Collection...
        final Collection panels = new ArrayList();
        panels.add(caseNumber);
        panels.add(defendants);
        panels.add(confirmation);

        addBodyPanels(panels);
        setWizardPanelImage("xwizardimage.jpg");
        pack();
    }

    /**
     * Overrides prev() in XWizardDialog to first call stepValidate() and
     * stepDeactivate() before calling prev() in the super class.
     * 
     * @throws CSRecoverableException
     */
    public void prev() throws CSRecoverableException {
        final XPanel xp = (XPanel) xPanels[currentPanel];
        xp.stepValidate();
        xp.stepDeactivate();
        super.prev();
    }

    /**
     * Returns a value from the resource bundle associated with this task
     * 
     * @param param
     * @return
     */
    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.TodaysSchedule, param);
    }
}
