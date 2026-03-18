package uk.gov.courtservice.xhibit.client.linkedcases;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Panel provides the functionality to link cases to the currrently
 * opened case
 * </p>
 * <p>
 * Description: There are 2 lists, one containing all cases that are availble to
 * link to current case one empty which will contain all selected cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */
public class LinkedCasesPanel extends XPanel {
    /**
     * ResourceBundle resources
     */
    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);

    /**
     * LinkedCases_Title topPanel
     */
    private LinkedCases_Title topPanel;

    /**
     * LinkedCasesSelectorPanel middlePanel
     */
    private LinkedCasesSelectorPanel middlePanel;

    /**
     * XhibitApplicationController xac
     */
    private XhibitApplicationController xac;

    /**
     * Vector allCases
     */
    private Vector allCases = null;

    /**
     * Vector linkedCases
     */
    private Vector linkedCases = null;

    /**
     * DefaultListModel notLinkedModel
     */
    private DefaultListModel notLinkedModel = new DefaultListModel();

    /**
     * DefaultListModel linkedModel
     */
    private DefaultListModel linkedModel = new DefaultListModel();

    /**
     * GridBagLayout gridBagLayout
     */
    private GridBagLayout gridBagLayout = new GridBagLayout();

    /**
     * Insets defaultInsets
     */
    private Insets defaultInsets = new Insets(8, 4, 8, 4);

    /**
     * JPanel myOKCancelPanel
     */
    public JPanel buttonPanel;

    /**
     * LinkedCasesDialog parent
     */
    private LinkedCasesDialog parent;

    /**
     * Integer scheduleHearingValueID
     */
    private Integer scheduleHearingValueID;

    /**
     * CaseSchedHearingValue caseSchedHearingValue
     */
    private CaseSchedHearingValue caseSchedHearingValue;

    /**
     * CaseSchedHearingValue currentCase
     */
    // private CaseSchedHearingValue currentCase;
    /**
     * LinkedCasesHelper linkedCaseHelper
     */
    private LinkedCasesHelper linkedCaseHelper;

    /**
     * Constructor takes in parameters of containing dialog and xac <init>
     * 
     * @param parent
     *            parameter for <init>
     * @param xac
     *            parameter for <init>
     * @param buttonPanel
     *            parameter for <init>
     * @throws CSRecoverableException -
     */
    public LinkedCasesPanel(LinkedCasesDialog parent, XhibitApplicationController xac) throws CSRecoverableException {
        this.parent = parent;
        this.xac = xac;
        buttonPanel = parent.getButtonPanel();
        stepInitialise();
        jbInit();
    }

    /**
     * jbInit
     */
    private void jbInit() {
        this.setLayout(gridBagLayout);
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    /**
     * getTopPanel
     * 
     * @return the returned LinkedCases_Title
     */
    private LinkedCases_Title getTopPanel() {
        if (topPanel == null) {
            topPanel = new LinkedCases_Title();
        }
        return topPanel;
    }

    /**
     * getMiddlePanel
     * 
     * @return the returned LinkedCasesSelectorPanel
     */
    private LinkedCasesSelectorPanel getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new LinkedCasesSelectorPanel(this, notLinkedModel, linkedModel);
            middlePanel.setTargetDescription(XHIBITConstant.getResource(resources, "lblLinked"));
            middlePanel.setAllDescription(XHIBITConstant.getResource(resources, "lblNotLinked"));
            middlePanel.setButtonEnable();
        }
        return middlePanel;
    }

    /**
     * stepInitialise - set up listmodel for screen
     * 
     * @throws CSRecoverableException -
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("in stepInitialise");

        // set up helper which is used thorughtout class
        linkedCaseHelper = new LinkedCasesHelper(xac);

        // get lead case id
        scheduleHearingValueID = xac.getApplicationCaseModel().getScheduledHearingId();

        // get hold of all available cases that can be linked to
        LinkSuggestionValue lsv = linkedCaseHelper.suggestLinkCases(scheduleHearingValueID);
        allCases = new Vector(lsv.getAllSuggestions());

        // get hold of cases that are already in linked chain
        linkedCases = new Vector(lsv.getPreviouslyLinked());
        // 'currentCase' never used
        // currentCase = lsv.getCurrentCase();

        // set up noLinkedModel for list
        if (allCases != null) {
            for (int i = 0; i < allCases.size(); i++) {
                caseSchedHearingValue = (CaseSchedHearingValue) allCases.get(i);
                notLinkedModel.addElement(caseSchedHearingValue);
            }
        } else {
            XHIBITConstant.debug("LinkedCasesPanel: There are no cases to link");
            throw new UserCancelException();
        }

        // set up linkedModel for list
        if (linkedCases != null) {
            for (int i = 0; i < linkedCases.size(); i++) {
                caseSchedHearingValue = (CaseSchedHearingValue) linkedCases.get(i);
                linkedModel.addElement(caseSchedHearingValue);
            }
        }
        stepUpdateViewState();
    }

    /**
     * stepActivate
     */
    public void stepActivate() {
        XHIBITConstant.debug("in stepActivate");
    }

    /**
     * stepDeactivate
     */
    public void stepDeactivate() {
    }

    /**
     * stepDeinitialise - set linked cases if updating
     * 
     * @param update
     *            parameter for stepDeinitialise
     * @throws CSRecoverableException -
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        XHIBITConstant.debug("in stepDeinitialise");

        if (update) {
            // Create an array from the items in the linked model
            CaseSchedHearingValue[] linkedCases = new CaseSchedHearingValue[linkedModel.size()];
            linkedModel.copyInto(linkedCases);

            // Link cases using helper
            linkedCaseHelper.linkCases(linkedCases, scheduleHearingValueID);

            // Set linked cases on dialog. Linked case information needs to
            // be
            // passed to LinkedCaseAction for further processing
            parent.setLinkedCases(linkedCases);
        }
    }

    /**
     * stepUpdateViewState - Enable ok button if cases selected for linking
     */
    public void stepUpdateViewState() {
        if (getMiddlePanel().getTargetListModel() == null || getMiddlePanel().getTargetListModel().isEmpty()) {
            ((OkCancelPanel) buttonPanel).okButton.setEnabled(false);
        } else {
            ((OkCancelPanel) buttonPanel).okButton.setEnabled(true);
        }
    }

    /**
     * stepValidate
     */
    public void stepValidate() {
    }
}