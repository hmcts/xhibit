package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.updatecase.DefenceRepTableModel;


/**
 * <p>
 * Title: Wizard for finding counsel.
 * </p>
 * <p>
 * Description: For finding counsel. The wizard may also add to the solicitors
 * or barristers, depending on the type of counsel choosen.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class FindRepresentativeWizard extends XWizardDialog implements FindRepresentativeWizardController {

    private static final long serialVersionUID = 1L;

    private static final int TYPE_SELECTOR_PANEL = 0;
    private static final int FIND_SOLICITOR_PANEL = 1;
    private static final int FIND_SUBSTITUTE_BARRISTER_PANEL = 2;
    private static final int FIND_SUBSTITUTED_BARRISTER_PANEL = 3;
    private static final int FIND_INSTRUCTED_BARRISTER_PANEL = 4;
    private static final int FIND_BARRISTER_PANEL = 5;

    private String frwTypeSelectorTitle;
    private String frwSubstituteBarristerTitle;
    private String frwInstructedBarristerTitle;
    private String frwSolicitorTitle;
    private String frwSubstitutedBarristerTitle;
    private String frwBarristerTitle;

    private XhibitApplicationController xac;

    private FindLegalRepresentativeModel model;
    
    private boolean legalAidOrderGranted;
    
    private Integer defendantId;
    
    DefenceRepTableModel defenceRepTableModel;
    
    private FindRepresentativeWizardTypeSelectorPanel typeSelectorPanel;
    private FindRepresentativeWizardFindSolicitorPanel findSolicitorPanel;
    private FindRepresentativeWizardFindBarristerPanel findSubstituteBarristerPanel;
    private FindRepresentativeWizardFindInstructedAdvocatePanel findInstructedBarristerPanel;
    private FindRepresentativeWizardFindInstructedAdvocatePanel findSubstitutedBarristerPanel;
    private FindRepresentativeWizardFindBarristerPanel findBarristerPanel;

    // As read from CREST
    private Vector<FindInstructedAdvocateTableRowModel> crestAdvocatesVec = 
        new Vector<FindInstructedAdvocateTableRowModel>();
    
    // The changed set of CREST records
    private Vector<FindInstructedAdvocateTableRowModel> editedAdvocatesVec = 
        new Vector<FindInstructedAdvocateTableRowModel>();
    
    
    /**
     * Constructor to create the FindRepresentativeWizard
     * 
     * @param ccm
     *            ChargesControllerModel which contains details of the state of
     *            the charges screen.
     * @throws CSRecoverableException
     */
    public FindRepresentativeWizard(
            DefenceRepTableModel defenceRepTableModel,
            FindLegalRepresentativeModel model, 
            java.lang.Integer defendantId,
            boolean legalAidOrderGranted) 
    throws CSRecoverableException {
        super(model.getXac(), "", true);
        this.model = model;
        this.xac = model.getXac();
        this.legalAidOrderGranted = legalAidOrderGranted;
        this.defendantId = defendantId;
        this.defenceRepTableModel = defenceRepTableModel;
        this.crestAdvocatesVec = this.getAdvocateMatches();
        this.editedAdvocatesVec = (Vector<FindInstructedAdvocateTableRowModel>)crestAdvocatesVec.clone();


        if (legalAidOrderGranted) {
            frwTypeSelectorTitle = ResourceBundleHelper
                    .getResource(XhibitBundles.CounselFacilities, "frwTypeSelectorTitleLegalAid");
        } else {
            frwTypeSelectorTitle = ResourceBundleHelper
                    .getResource(XhibitBundles.CounselFacilities, "frwTypeSelectorTitlePrivate");
        }
        
        frwSubstituteBarristerTitle = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities,
                "frwSubstituteBarristerTitle");
        frwInstructedBarristerTitle = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities,
                "frwInstructedBarristerTitle");
        frwSolicitorTitle = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities, "frwSolicitorTitle");
        frwSubstitutedBarristerTitle = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities,
                "frwSubstitutedBarristerTitle");
        frwBarristerTitle = ResourceBundleHelper.getResource(XhibitBundles.CounselFacilities,
                "frwBarristerTitle");

        // create panels to add to wizard
        typeSelectorPanel = new FindRepresentativeWizardTypeSelectorPanel(this, model, legalAidOrderGranted);
        findSolicitorPanel = new FindRepresentativeWizardFindSolicitorPanel(this, this, model);
        findSubstituteBarristerPanel = new FindRepresentativeWizardFindBarristerPanel(this, model);
        findInstructedBarristerPanel = new FindRepresentativeWizardFindInstructedAdvocatePanel(this, model, editedAdvocatesVec, false);
        findSubstitutedBarristerPanel = new FindRepresentativeWizardFindInstructedAdvocatePanel(this, model, editedAdvocatesVec, true);
        findBarristerPanel = new FindRepresentativeWizardFindBarristerPanel(this, model);
        Dimension dim = new Dimension(550, 400);
        findBarristerPanel.setPreferredSize(dim);

        // Array to contain all of the panels for the wizard. The panels will
        // be displayed in the wizard in the order that they are added to the
        // List.
        ArrayList<XPanel> al = new ArrayList<XPanel>();
        al.add(typeSelectorPanel);
        al.add(findSolicitorPanel);
        al.add(findSubstituteBarristerPanel);
        al.add(findSubstitutedBarristerPanel);
        al.add(findInstructedBarristerPanel);
        al.add(findBarristerPanel);
        addBodyPanels(al);

        getButtonPanel().getNext().setEnabled(false);
        setWizardPanelImage("xwizardimage.jpg");
        pack();
    }

    private Vector<FindInstructedAdvocateTableRowModel> getAdvocateMatches() 
    throws CSRecoverableException {
        
        final Integer caseId = 
            xac.getApplicationCaseModel().getCaseId();
        
        Vector<FindInstructedAdvocateTableRowModel> instructedAdvocates =
            InstructedAdvocateHelper.getInstructedAdvocateMatches(defendantId, caseId);
        
        return instructedAdvocates;
    }
    
    /**
     * FindRepresentativeWizardController implementation of life cycle method,
     * called when the user moves from one panel to another in the wizard to set
     * the title and enable/disable the wizard buttons.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() {
        // Need to check what the current screen is
        switch (currentPanel) {
        case TYPE_SELECTOR_PANEL:
            this.setTitle(frwTypeSelectorTitle);
            getButtonPanel().getBack().setEnabled(false);
            if (model.isInPersonSelected() || model.isNonAttendanceSelected()) {
                getButtonPanel().getNext().setEnabled(false);
                getButtonPanel().getFinish().setEnabled(true);
            } else {
                getButtonPanel().getNext().setEnabled(model.getRepTypeRadio() != null);
                getButtonPanel().getFinish().setEnabled(false);
            }
            break;

        case FIND_SUBSTITUTE_BARRISTER_PANEL:
            this.setTitle(frwSubstituteBarristerTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(
                    model.getFindLegalRepresentativeTableRowModel() != null
                    && model.getFindLegalRepresentativeTableRowModel().getLegalRepId() != null);
            getButtonPanel().getFinish().setEnabled(false);
            break;
            
        case FIND_SUBSTITUTED_BARRISTER_PANEL:
            this.setTitle(frwSubstitutedBarristerTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    model.getFindLegalRepresentativeTableRowModel() != null
                    && model.getInstructedAdvocateTableRowModel() != null
                    && model.getFindLegalRepresentativeTableRowModel().getLegalRepId() != null
                    && model.getInstructedAdvocateTableRowModel().getLegalRepId() != null);
            break;
            
        case FIND_INSTRUCTED_BARRISTER_PANEL:
            this.setTitle(frwInstructedBarristerTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    model.getFindLegalRepresentativeTableRowModel() != null
                    && model.getFindLegalRepresentativeTableRowModel().getLegalRepId() != null);
            break;

        case FIND_SOLICITOR_PANEL:
            this.setTitle(frwSolicitorTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    model.getFindLegalRepresentativeTableRowModel() != null
                    && model.getFindLegalRepresentativeTableRowModel().getLegalRepId() != null);
            break;
            
        case FIND_BARRISTER_PANEL:
            this.setTitle(frwBarristerTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    model.getFindLegalRepresentativeTableRowModel() != null
                    && model.getFindLegalRepresentativeTableRowModel().getLegalRepId() != null);
            break;
        }
    }

    /**
     * Move to the next panel.  This method overrides the base class
     * method, which moves though the panels one at a time in a linear
     * order.
     * 
     * @throws CSRecoverableException
     */
    public void next() throws CSRecoverableException {
        switch (currentPanel) {
        case TYPE_SELECTOR_PANEL:
            if (model.isBarristerSelected()) {
                if (!legalAidOrderGranted) {
                    setPanelIndex(FIND_BARRISTER_PANEL, NEXT_EVENT);
                } else if (model.getBarristerType().equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG)) {
                    setPanelIndex(FIND_INSTRUCTED_BARRISTER_PANEL, NEXT_EVENT);
                } else {
                    setPanelIndex(FIND_SUBSTITUTE_BARRISTER_PANEL, NEXT_EVENT);
                }
            } else if (model.isSolicitorSelected()) {
                setPanelIndex(FIND_SOLICITOR_PANEL, NEXT_EVENT);
            }
            break;
            
        case FIND_SUBSTITUTE_BARRISTER_PANEL:
            setPanelIndex(FIND_SUBSTITUTED_BARRISTER_PANEL, NEXT_EVENT);
            break;
            
        case FIND_INSTRUCTED_BARRISTER_PANEL:
        case FIND_SUBSTITUTED_BARRISTER_PANEL:
        case FIND_SOLICITOR_PANEL:
        case FIND_BARRISTER_PANEL:
            // next button not available on these screens so will never be here
            throw new CSRecoverableException();
        }
    }
    
    /**
     * Move to the previous panel.  This method overrides the base class
     * method, which moves though the panels one at a time in a linear
     * order.
     * 
     * @throws CSRecoverableException
     */
    public void prev() throws CSRecoverableException {
        switch (currentPanel) {
        case FIND_SUBSTITUTED_BARRISTER_PANEL:
            this.findSubstitutedBarristerPanel.clearSelection();
            setPanelIndex(FIND_SUBSTITUTE_BARRISTER_PANEL, PREV_EVENT);
            break;
            
        case FIND_INSTRUCTED_BARRISTER_PANEL:
            this.findInstructedBarristerPanel.clearSelection();
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case FIND_SUBSTITUTE_BARRISTER_PANEL:
            this.findSubstituteBarristerPanel.clearSelection();
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case FIND_SOLICITOR_PANEL:
            this.findSolicitorPanel.clearSelection();
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case FIND_BARRISTER_PANEL:
            this.findBarristerPanel.clearSelection();
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case TYPE_SELECTOR_PANEL:
            // prev button not available on these screens so will never be here
            throw new CSRecoverableException();     
        }
    }
    
    /**
     * FindRepresentativeWizardController implementation that is executed when
     * the wizard is closing after the Finish action is fired.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeinitialise() throws CSRecoverableException {
        final Integer caseId = 
            xac.getApplicationCaseModel().getCaseId();
        
        InstructedAdvocateHelper.saveEdits(
                editedAdvocatesVec,
                crestAdvocatesVec,
                defendantId,
                caseId,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    public void stepValidate() throws CSRecoverableException {
        if (model.getFindLegalRepresentativeTableRowModel() != null
                && model.getInstructedAdvocateTableRowModel() != null
                && model.getBarristerType() != null
                && model.getBarristerType().equals(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG)) {
            
            if (model.getFindLegalRepresentativeTableRowModel().getLegalRepId().equals(
                        model.getInstructedAdvocateTableRowModel().getLegalRepId())) {
            
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.chooseSubInstError", 
                        "User chose same rep as subsitute and substituted");
            }
            
            if (this.defenceRepTableModel.hasSubstitutedAdvocate(
                    defendantId, model.getInstructedAdvocateTableRowModel().getLegalRepId())) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.mutlipleSubsitutesError", 
                        "User chose a substitute for an already substituted advocate");
            }
            
            if (this.defenceRepTableModel.hasInstructedAdvocate(
                    defendantId, model.getInstructedAdvocateTableRowModel().getLegalRepId())) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.substitutueForASignedIncounsel", 
                        "User chose a substitute for an advocate that is already signed in");
            }
        }
        
        if (model.getFindLegalRepresentativeTableRowModel() != null
                && model.getInstructedAdvocateTableRowModel() != null
                && model.getBarristerType() != null
                && model.getBarristerType().equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG)) {
            
            if (this.defenceRepTableModel.hasSubstitutedAdvocate(
                    defendantId, model.getInstructedAdvocateTableRowModel().getLegalRepId())) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.instructedAdvocateWhenAlreadySubstituted", 
                        "User chose an instructed advocate which has already been substituted");
            }
            
            if (this.defenceRepTableModel.hasInstructedAdvocate(
                    defendantId, model.getInstructedAdvocateTableRowModel().getLegalRepId())) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.multipleInstructedAdvocate", 
                        "User signed in an instructed advocate twice");
            }
        }
        
        if (model.getFindLegalRepresentativeTableRowModel() != null
                && this.defenceRepTableModel.hasAdvocate(
                        defendantId, 
                        model.getFindLegalRepresentativeTableRowModel().getLegalRepId())) {
            
            throw new CSRecoverableException(
                    "gui.updateDefendantCaseData.multipleAdvocate", 
                    "User signed in an advocate twice");
        }
    }
    
    /**
     * FindRepresentativeWizardController implementation that returns the
     * XhibitApplicationController i.e. the main application.
     * 
     * @return the XhibitApplicationController.
     */
    public XhibitApplicationController getXac() {
        return xac;
    }
}