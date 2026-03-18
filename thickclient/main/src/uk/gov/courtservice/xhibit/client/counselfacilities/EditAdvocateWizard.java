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
import uk.gov.courtservice.xhibit.client.counselfacilities.InstructedAdvocateHelper;

/**
 * <p>
 * Title: Wizard for editing counsel.
 * </p>
 * <p>
 * Description: For editing counsel, specifically the I/S flag
 * on a barrister.
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

public class EditAdvocateWizard extends XWizardDialog implements EditAdvocateWizardController {

    private static final long serialVersionUID = 1L;
    
    private static final int TYPE_SELECTOR_PANEL = 0;
    private static final int NEW_INSTRUCTED_ADVOCATE_PANEL = 1;
    private static final int FIND_SUBSTITUTED_BARRISTER_PANEL = 2;
    private static final int ADVOCATE_IS_INSTRUCTED_PANEL = 3;

    private String frwTypeSelectorTitle;
    private String frwNewInstructedAdvocateTitle;
    private String frwSubstitutedBarristerTitle;
    private String frwAdvocateIsInstrucedTitle;

    private XhibitApplicationController xac;

    private uk.gov.courtservice.xhibit.client.updatecase.DefenceRepTableModel caseDefenceRepTableModel;
    private uk.gov.courtservice.xhibit.client.hearingrecord.DefenceRepTableModel hearingDefenceRepTableModel;
    private EditAdvocateWizardModel model;
    
    private Integer defendantId;
    private Integer legalRepId;
    
    private EditAdvocateWizardTypeSelectorPanel typeSelectorPanel;
    private EditAdvocateWizardNewInstructedAdvocate newInstructedAdvocatePanel;
    private EditAdvocateWizardFindInstructedAdvocatePanel findSubstitutedBarristerPanel;
    private EditAdvocateWizardExistingInstructedAdvocate advocateIsInstructedPanel;
    
    private boolean advocateIsAnInstructedAdvocate;
    
    // As read from CREST
    private Vector<FindInstructedAdvocateTableRowModel> crestAdvocatesVec = 
        new Vector<FindInstructedAdvocateTableRowModel>();
    
    // The changed set of CREST records
    private Vector<FindInstructedAdvocateTableRowModel> editedAdvocatesVec = 
        new Vector<FindInstructedAdvocateTableRowModel>();



    
    /**
     * Constructor to create the EditAdvocateWizard
     * 
     * @param ccm
     *            ChargesControllerModel which contains details of the state of
     *            the charges screen.
     * @throws CSRecoverableException
     */
    public EditAdvocateWizard(
            uk.gov.courtservice.xhibit.client.updatecase.DefenceRepTableModel defenceRepTableModel,
            EditAdvocateWizardModel model, 
            java.lang.Integer legalRepId,
            java.lang.Integer defendantId) 
    throws CSRecoverableException {
        super(model.getXac(), "", true);
        this.caseDefenceRepTableModel = defenceRepTableModel;
        setup(model, legalRepId, defendantId);
    }
    
    public EditAdvocateWizard(
            uk.gov.courtservice.xhibit.client.hearingrecord.DefenceRepTableModel defenceRepTableModel,
            EditAdvocateWizardModel model, 
            java.lang.Integer legalRepId,
            java.lang.Integer defendantId) 
    throws CSRecoverableException {
        super(model.getXac(), "", true);
        this.hearingDefenceRepTableModel = defenceRepTableModel;
        setup(model, legalRepId, defendantId);
    }
    
    
    private void setup(
            EditAdvocateWizardModel model, 
            java.lang.Integer legalRepId,
            java.lang.Integer defendantId) 
    throws CSRecoverableException {
        this.model = model;
        this.xac = model.getXac();
        this.defendantId = defendantId;
        this.legalRepId = legalRepId;
        this.crestAdvocatesVec = this.getAdvocateMatches();
        this.editedAdvocatesVec = (Vector<FindInstructedAdvocateTableRowModel>)crestAdvocatesVec.clone();
        this.advocateIsAnInstructedAdvocate = isInstructedAdvocate(legalRepId);
        
        frwTypeSelectorTitle = 
            ResourceBundleHelper.getResource(
                    XhibitBundles.CounselFacilities, 
                    "frwTypeSelectorEdit");

        frwSubstitutedBarristerTitle = 
            ResourceBundleHelper.getResource(
                    XhibitBundles.CounselFacilities,
                    "frwSubstitutedBarristerTitle");

        frwNewInstructedAdvocateTitle =
            ResourceBundleHelper.getResource(
                    XhibitBundles.CounselFacilities,
                    "frwNewInstructedAdvocateTitle");
        
        frwAdvocateIsInstrucedTitle =
            ResourceBundleHelper.getResource(
                    XhibitBundles.CounselFacilities,
                    "frwAdvocateIsInstrucedTitle");

        // create panels to add to wizard
        typeSelectorPanel = 
            new EditAdvocateWizardTypeSelectorPanel(this, model);
        
        newInstructedAdvocatePanel = 
            new EditAdvocateWizardNewInstructedAdvocate(
                    this, 
                    model, 
                    editedAdvocatesVec,
                    legalRepId);
 
        findSubstitutedBarristerPanel = 
            new EditAdvocateWizardFindInstructedAdvocatePanel(
                    this, 
                    model, 
                    editedAdvocatesVec);
        
        advocateIsInstructedPanel =
            new EditAdvocateWizardExistingInstructedAdvocate(
                    this, model);
        
        Dimension dim = new Dimension(550, 400);
        findSubstitutedBarristerPanel.setPreferredSize(dim);

        
        ArrayList<XPanel> al = new ArrayList<XPanel>();
        al.add(typeSelectorPanel);
        al.add(newInstructedAdvocatePanel);
        al.add(findSubstitutedBarristerPanel);
        al.add(advocateIsInstructedPanel);
        addBodyPanels(al);
        
        getButtonPanel().getNext().setEnabled(false);
        setWizardPanelImage("xwizardimage.jpg");
        pack();
        
        if (advocateIsAnInstructedAdvocate) {
            setPanelIndex(ADVOCATE_IS_INSTRUCTED_PANEL, NEXT_EVENT);
            model.setBarristerType(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG);
            advocateIsInstructedPanel.stepActivate();
            this.stepUpdateViewState();
        }
    }

    /**
     * EditAdvocateWizardController implementation of life cycle method,
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
            if (this.advocateIsAnInstructedAdvocate) {
                // already an instruced advocate
                getButtonPanel().getNext().setEnabled(false);
                getButtonPanel().getFinish().setEnabled(true);
            } else if (model.isInstructedAdvocate()) {
                // next assign the instructed advocate to a post
                getButtonPanel().getNext().setEnabled(true);
                getButtonPanel().getFinish().setEnabled(false);
            } else if (model.isSubstituteAdvocate()) {
                // next choose who we are substituting for
                getButtonPanel().getNext().setEnabled(true);
                getButtonPanel().getFinish().setEnabled(false);
            } else {
                getButtonPanel().getNext().setEnabled(false);
                getButtonPanel().getFinish().setEnabled(false);
            }
            break;
            
        case FIND_SUBSTITUTED_BARRISTER_PANEL:
            this.setTitle(frwSubstitutedBarristerTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    findSubstitutedBarristerPanel.isMandatoryFieldsCompleted());
            break;
            
        case ADVOCATE_IS_INSTRUCTED_PANEL:
            this.setTitle(frwAdvocateIsInstrucedTitle);
            getButtonPanel().getBack().setEnabled(false);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(true);
            break;
            
        case NEW_INSTRUCTED_ADVOCATE_PANEL:
            this.setTitle(frwNewInstructedAdvocateTitle);
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            getButtonPanel().getFinish().setEnabled(
                    newInstructedAdvocatePanel.isMandatoryFieldsCompleted());
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
            if (model.isSubstituteAdvocate()) {
                setPanelIndex(FIND_SUBSTITUTED_BARRISTER_PANEL, NEXT_EVENT);
            } else if (model.isInstructedAdvocate()) {
                setPanelIndex(NEW_INSTRUCTED_ADVOCATE_PANEL, NEXT_EVENT);
            }
            break;

        case FIND_SUBSTITUTED_BARRISTER_PANEL:
        case ADVOCATE_IS_INSTRUCTED_PANEL:
        case NEW_INSTRUCTED_ADVOCATE_PANEL:
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
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case NEW_INSTRUCTED_ADVOCATE_PANEL:
            this.newInstructedAdvocatePanel.clearSelection();
            setPanelIndex(TYPE_SELECTOR_PANEL, PREV_EVENT);
            break;
            
        case TYPE_SELECTOR_PANEL:
        case ADVOCATE_IS_INSTRUCTED_PANEL:
            // prev button not available on these screens so will never be here
            throw new CSRecoverableException();     
        }
    }
    
    
    public void stepValidate() throws CSRecoverableException {
        
        if (model.getSubstitutedInstructedAdvocateTableRowModel() != null
                && model.getBarristerType() != null
                && model.getBarristerType().equals(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG)) {

            if (model.getSubstitutedInstructedAdvocateTableRowModel().getLegalRepId().equals(
                    legalRepId)) {

                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.chooseSubInstError", 
                        "User chose same rep as subsitute and substituted");
            }

            if (this.caseDefenceRepTableModel != null
                    && this.caseDefenceRepTableModel.hasSubstitutedAdvocate(
                            defendantId, model.getSubstitutedInstructedAdvocateTableRowModel().getLegalRepId())) {

                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.mutlipleSubsitutesError", 
                        "User chose a substitute for an already substituted advocate");
            }

            if (this.hearingDefenceRepTableModel != null
                    && this.hearingDefenceRepTableModel.hasSubstitutedAdvocate(
                            model.getSubstitutedInstructedAdvocateTableRowModel().getLegalRepId())) {

                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.mutlipleSubsitutesError", 
                        "User chose a substitute for an already substituted advocate");
            }
            
            if (this.caseDefenceRepTableModel != null
                    && this.caseDefenceRepTableModel.hasInstructedAdvocate(
                            defendantId, model.getSubstitutedInstructedAdvocateTableRowModel().getLegalRepId())) {

                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.substitutueForASignedIncounsel", 
                        "User chose a substitute for an advocate that is already signed in");
            }
            
            if (this.hearingDefenceRepTableModel != null
                    && this.hearingDefenceRepTableModel.hasInstructedAdvocate(
                            model.getSubstitutedInstructedAdvocateTableRowModel().getLegalRepId())) {

                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.substitutueForASignedIncounsel", 
                        "User chose a substitute for an advocate that is already signed in");
            }
        }
        
        if (model.getBarristerType() != null
                && model.getBarristerType().equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG)) {
            
            if (this.caseDefenceRepTableModel != null
                    && this.caseDefenceRepTableModel.hasSubstitutedAdvocate(
                            defendantId, legalRepId)) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.instructedAdvocateWhenAlreadySubstituted", 
                        "User chose an instructed advocate which has already been substituted");
            }
            
            if (this.hearingDefenceRepTableModel != null
                    && this.hearingDefenceRepTableModel.hasSubstitutedAdvocate(
                            legalRepId)) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.instructedAdvocateWhenAlreadySubstituted", 
                        "User chose an instructed advocate which has already been substituted");
            }
            
            if (this.caseDefenceRepTableModel != null
                    && this.caseDefenceRepTableModel.hasInstructedAdvocate(
                            defendantId, legalRepId)) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.multipleInstructedAdvocate", 
                        "User signed in an instructed advocate twice");
            }
            
            if (this.hearingDefenceRepTableModel != null
                    && this.hearingDefenceRepTableModel.hasInstructedAdvocate(
                            legalRepId)) {
                
                throw new CSRecoverableException(
                        "gui.updateDefendantCaseData.multipleInstructedAdvocate", 
                        "User signed in an instructed advocate twice");
            }
        }
    }
    
    private Vector<FindInstructedAdvocateTableRowModel> getAdvocateMatches() 
    throws CSRecoverableException {
        
        final Integer caseId = 
            xac.getApplicationCaseModel().getCaseId();
        
        Vector<FindInstructedAdvocateTableRowModel> instructedAdvocates =
            InstructedAdvocateHelper.getInstructedAdvocateMatches(defendantId, caseId);
        
        return instructedAdvocates;
    }
    
    /*
     * Returns all the details of the instructed advocate, or null if the
     * legalRepId is not an instructed advocate.
     */
    private FindInstructedAdvocateTableRowModel getInstructeAdvocateRowModel(Integer legalRepId) {
        
        for (FindInstructedAdvocateTableRowModel item : crestAdvocatesVec) {
            if (item.isAvailable() && item.getLegalRepId().equals(legalRepId)) {
                return item;
            }
        }
        
        return null;
    }
    
    private boolean isInstructedAdvocate(Integer legalRepId) {

        FindInstructedAdvocateTableRowModel advocate = 
            getInstructeAdvocateRowModel(legalRepId);
        
        return advocate != null && advocate.isAvailable();
    }
    
    /**
     * EditAdvocateWizardController implementation that is executed when
     * the wizard is closing after the Finish action is fired.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeinitialise() throws CSRecoverableException {
        
        final Integer caseId = 
            xac.getApplicationCaseModel().getCaseId();
        
        Integer newInstructedAdvocateCrestPostNumber = 
            model.getNewInstructedAdvocateCrestPostNumber();
        
        boolean addNewInstructedAdvocate = false;
        
        if (newInstructedAdvocateCrestPostNumber != null) {
            
            boolean found = false;
            
            for (int i = 0; i < editedAdvocatesVec.size(); i++) {
                FindInstructedAdvocateTableRowModel trm = editedAdvocatesVec.elementAt(i);
            
                if (trm.getLegalRepId().equals(legalRepId)
                        && trm.getCrestPostNumber().equals(newInstructedAdvocateCrestPostNumber)) {
                    
                    found = true;
                    
                    if (trm.isWithdrawn()) {
                        // not allowed to bring back a withdrawn advocate
                        throw new CSRecoverableException(
                                "gui.counselSignIn.advocateIsWithdrawn",
                                "Attempt to add a withdrawn advocate to a post");
                    }
                    
                    if (!trm.isAvailable()) {
                        // A post is choosen where the advocate was previously
                        // set unavailable.  So set them to available again on that post.
                        FindInstructedAdvocateTableRowModel newTrm =
                            new FindInstructedAdvocateTableRowModel(trm);
                        newTrm.setAvailable(null);
                        editedAdvocatesVec.setElementAt(newTrm, i);
                    }
                }
            }
            
            if (!found) {
                addNewInstructedAdvocate = true;
            }
        } 

        InstructedAdvocateHelper.saveEdits(
                editedAdvocatesVec,
                crestAdvocatesVec,
                defendantId,
                caseId,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

        if (addNewInstructedAdvocate) {
            // This must be done after the saveEdits call for the pathalogical
            // case where the user edits a post to remove the advocate and
            // assigns the new advocate back into the post.
            InstructedAdvocateHelper.addNewInstructedAdvocate(
                    defendantId, 
                    caseId, 
                    legalRepId, 
                    model.getNewInstructedAdvocateDefenceCategory(), 
                    null, // available
                    newInstructedAdvocateCrestPostNumber);
        }
    }

    /**
     * EditAdvocateWizardController implementation that returns the
     * XhibitApplicationController i.e. the main application.
     * 
     * @return the XhibitApplicationController.
     */
    public XhibitApplicationController getXac() {
        return xac;
    }
}