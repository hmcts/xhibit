package uk.gov.courtservice.xhibit.client.results;

// Java
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalListener;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.results.disposals.EditDisposalPanel;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.client.results.disposals.SelectDisposalPanel;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

/**
 * <p>
 * Title: DisposalDialog
 * </p>
 * <p>
 * Description: Dialog for selecting editing and updating disposals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell
 * @version 1.0
 */
public class DisposalDialog extends XWizardDialog {
    // Panels
    private final EditDisposalPanel editDisposalPanel;

    private final SelectDisposalPanel selectDisposalPanel;

    // Create Data
    private final Integer id;

    private final boolean related;

    private final boolean magistrate;

    private final DisposalValue magistrateValue;

    // Set to true when disposal is edited
    private boolean edited;
    

    /**
     * Create a DisposalDialog for creating a criminal disposal
     */
    public DisposalDialog(XhibitApplicationController xac, Integer id, boolean related, DeportationModel deportationDetails, HateCrimeModel hateCrimeReasons, boolean hateCrimePanelEnabled,
    		AggravatingReasonsModel aggravatingReasons, boolean aggravatingPanelEnabled) throws CSRecoverableException {
        this(xac, id, related, false, null, deportationDetails, hateCrimeReasons, hateCrimePanelEnabled, 
        		aggravatingReasons, aggravatingPanelEnabled);
    }

    /**
     * Create a DisposalDialog for creating a magistrate or criminal disposal
     */
    public DisposalDialog(XhibitApplicationController xac, Integer id, boolean related, boolean magistrate, DeportationModel deportationDetails, HateCrimeModel hateCrimeReasons, boolean hateCrimePanelEnabled,
    		AggravatingReasonsModel aggravatingReasons, boolean aggravatingPanelEnabled)
            throws CSRecoverableException {
        this(xac, id, related, magistrate, null, deportationDetails, hateCrimeReasons, hateCrimePanelEnabled,
        		aggravatingReasons, aggravatingPanelEnabled);
    }

    /**
     * Create a DisposalDialog for creating a variation of a magistrate disposal
     */
    public DisposalDialog(XhibitApplicationController xac, Integer id, boolean related, DisposalValue magistrateValue, DeportationModel deportationDetails, HateCrimeModel hateCrimeReasons, boolean hateCrimePanelEnabled,
    		AggravatingReasonsModel aggravatingReasons, boolean aggravatingPanelEnabled)
            throws CSRecoverableException {
        this(xac, id, related, false, magistrateValue, deportationDetails, hateCrimeReasons, hateCrimePanelEnabled,
        		aggravatingReasons, aggravatingPanelEnabled);
    }
    

    /**
     * Create a DisposalDialog for creating an existing disposal (private as
     * ambiquous)
     */
    private DisposalDialog(XhibitApplicationController xac, Integer id, boolean related, boolean magistrate,
            DisposalValue magistrateValue, DeportationModel deportationDetails, HateCrimeModel hateCrimeDetails, boolean hateCrimePanelEnabled,
            AggravatingReasonsModel aggravatingReasons, boolean aggravatingPanelEnabled) throws CSRecoverableException {
        super(xac, DisposalUtil.getResource("createDisposalDialogTitle"), true);

        setSize(900, 600);
        centreDialog();

        if (id == null) {
            throw new IllegalArgumentException("id: null");
        }

        this.id = id;
        this.related = related;
        this.magistrate = magistrate;
        this.magistrateValue = magistrateValue;

        selectDisposalPanel = new SelectDisposalPanel(hateCrimePanelEnabled, aggravatingPanelEnabled);
        selectDisposalPanel.setDefaultDisposalAction(new XAction("SelectDisposal") {
            public void xActionPerformed(ActionEvent e) throws Exception {
                next();
            }
        });
        selectDisposalPanel.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                setNextEnabled(selectDisposalPanel.isDisposalSelected());
            }
        });
        
        //Assign current deportation reason
        selectDisposalPanel.setDeportationReason(deportationDetails);
        
        // Assign the current hate crime reasons
        selectDisposalPanel.setHateCrimeReasons(hateCrimeDetails);
        selectDisposalPanel.setHateCrimeTabEnabled(hateCrimePanelEnabled);
        
        // Assign the current aggravating reasons
        selectDisposalPanel.setAggravatingReasons(aggravatingReasons);
        selectDisposalPanel.setAggravatingTabEnabled(aggravatingPanelEnabled);
        
        editDisposalPanel = new EditDisposalPanel();
        editDisposalPanel.addDisposalListener(new DisposalListener() {
            public void disposalChanged(DisposalEvent e) {
                setFinishEnabled(e.isComplete());
                edited = true;
            }
        });
        
        List panelList = new ArrayList();
        panelList.add(selectDisposalPanel);
        panelList.add(editDisposalPanel);
        addBodyPanels(panelList);

        setButtonVisible(true, true, true, true);
        setButtonEnabled(false, false, false, true);
        edited = false;
    }

    /**
     * Create a DisposalDialog for editing an existing disposal
     */
    public DisposalDialog(XhibitApplicationController xac, DisposalReferenceValue originalReference,
            DisposalValue originalValue, DeportationModel deportationDetails, HateCrimeModel hateCrimeDetails, boolean hateCrimeTabEnabled, 
            AggravatingReasonsModel aggravatingReasonsModel, boolean aggravatingTabEnabled) throws CSRecoverableException {
        super(xac, DisposalUtil.getResource("editDisposalDialogTitle"), true);

        setSize(900, 600);
        centreDialog();

        if (originalReference == null) {
            throw new IllegalArgumentException("originalReference: null");
        }

        if (originalValue == null) {
            throw new IllegalArgumentException("originalValue: null");
        }

        this.id = null;
        this.related = false;
        this.magistrate = false;
        this.magistrateValue = null;

        selectDisposalPanel = new SelectDisposalPanel(hateCrimeTabEnabled, aggravatingTabEnabled);
        selectDisposalPanel.setHateCrimeTabEnabled(hateCrimeTabEnabled);
        selectDisposalPanel.setAggravatingTabEnabled(aggravatingTabEnabled);
        
        editDisposalPanel = new EditDisposalPanel();
        editDisposalPanel.addDisposalListener(new DisposalListener() {
            public void disposalChanged(DisposalEvent e) {
                setFinishEnabled(e.isComplete());
                edited = true;
            }
        });
                 
        //Set deportation Reason & Visibility
        originalReference.setCustodial(deportationDetails.getCustodial());
        originalReference.setSuspended(deportationDetails.getSuspended());
        originalReference.setSeriousDrugOffence(deportationDetails.getSeriousDrugOffence());
        originalReference.setRecommendedDeportation(deportationDetails.getRecommendedDeportation());
        originalReference.setDeportationVisibilty(true);
        originalReference.setHateCrimeTabVisibility(hateCrimeTabEnabled);
        originalReference.setAggravatingTabVisibility(aggravatingTabEnabled);
          
        // Assign the current hate crime reasons
        try {
        	originalReference.setHateCrimeFlag(hateCrimeDetails.getHateCrimeFlag());
            selectDisposalPanel.setHateCrimeReasons(hateCrimeDetails);
            selectDisposalPanel.setHateCrimeTabEnabled(hateCrimeTabEnabled);
            // Add Hate Crime Details To Original Reference
            boolean hateCrimeFlag = hateCrimeDetails.getHateCrimeFlag();
            if (hateCrimeDetails.getGeneralDisability()) {
                originalReference.setGeneralDisability(hateCrimeDetails.getGeneralDisability());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getGeneralSexual()) {
                originalReference.setGeneralSexual(hateCrimeDetails.getGeneralSexual());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getGeneralTransgender()) {
                originalReference.setGeneralTransgender(hateCrimeDetails.getGeneralTransgender());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getRaceAndReligionAggravated()) {
                originalReference.setRaceAndReligionAggravated(hateCrimeDetails.getRaceAndReligionAggravated());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getReligionAggravated()) {
                originalReference.setReligionAggravated(hateCrimeDetails.getReligionAggravated());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getRacialAggravated()) {
                originalReference.setRacialAggravated(hateCrimeDetails.getRacialAggravated());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getVictimDisability()) {
                originalReference.setVictimDisability(hateCrimeDetails.getVictimDisability());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getVictimSexual()) {
                originalReference.setVictimSexual(hateCrimeDetails.getVictimSexual());
                hateCrimeFlag = true;
            }
            if (hateCrimeDetails.getVictimTransgender()) {
                originalReference.setVictimTransgender(hateCrimeDetails.getVictimTransgender());
                hateCrimeFlag = true;
            }
            originalReference.setHateCrimeFlag(hateCrimeFlag);
        } catch (NullPointerException npe) {
            System.err.println("Shouldnt happen!!");
        }
        
        // Assign the current aggravating reasons
        try {
        	selectDisposalPanel.setAggravatingReasons(aggravatingReasonsModel);
            selectDisposalPanel.setAggravatingTabEnabled(aggravatingTabEnabled);
            // Add Aggravating Reasons To Original Reference
            originalReference.setAggravatingAssaultOnWorkers(aggravatingReasonsModel.isAssaultOnWorkers());
            originalReference.setAggravatingTerroristConnection(aggravatingReasonsModel.isTerroristConnection());
            originalReference.setAggravatingEmergencyWorkers(aggravatingReasonsModel.isEmergencyWorkers());
            originalReference.setAggravatingHostility(aggravatingReasonsModel.isHostility());
            originalReference.setAggravatingSexualOrientation(aggravatingReasonsModel.isSexualOrientation());
            originalReference.setAggravatingSexualOrientationOfVictim(aggravatingReasonsModel.isSexualOrientationOfVictim());
            originalReference.setAggravatingTransgender(aggravatingReasonsModel.isTransgender());
            originalReference.setAggravatingTransgenderOfVictim(aggravatingReasonsModel.isTransgenderOfVictim());
        } catch (NullPointerException npe) {
            System.err.println("Shouldnt happen!!");
        }
        editDisposalPanel.setReference(originalReference);
        editDisposalPanel.setValue(originalValue);

        List panelList = new ArrayList();
        panelList.add(editDisposalPanel);
        addBodyPanels(panelList);

        setButtonVisible(false, false, true, true);
        setButtonEnabled(false, false, false, true);
        edited = false;
    }

    /**
     * XWizard Override
     */
    // Assume we are on EditDisposalPanel as only enabled here
    public void prev() throws CSRecoverableException {
        // If data has been entered warn as we are going to lose it!
        if (!edited
                || XMessageBox.alert(this, DisposalUtil.getResource("backConfirmTitle"), true,
                        XMessageBox.ICONQUESTION, DisposalUtil.getResource("backConfirmText"), XDialog.YESNO,
                        XDialog.DEFAULTNO)) {
            super.prev();
            setButtonEnabled(false, true, false, true);
            edited = false;
        }
    }

    /**
     * XWizard Override
     */
    // Assume we are on SelectDisposalPanel as only enabled here
    //This method is only called from the Select disposal Panel.
    public void next() throws CSRecoverableException {
        DisposalReferenceValue reference = selectDisposalPanel.getReference();
 
        //check if deportation reason has been selected.
        if(selectDisposalPanel.isDeportationReasonSelected()){
            
            // ensures deportation panel is only displayed for editing a existing disposal 
            reference.setDeportationVisibilty(false);
                    
            editDisposalPanel.setReference(reference);
             
            if (magistrate) {
                editDisposalPanel.setValue(reference.createDisposal(id, related, magistrate));
            } else {
                if (magistrateValue == null) {
                    editDisposalPanel.setValue(reference.createDisposal(id, related));
                } else {
                    editDisposalPanel.setValue(reference.createDisposal(id, related, magistrateValue.getDisposal2Id()));
                }
            }
            super.next();
            setButtonEnabled(true, false, editDisposalPanel.isComplete(), true);
            edited = false;
        }else{
            //Display message to user
            JOptionPane.showMessageDialog(this,DisposalUtil.getResource("selectDeportationReason"), 
                    DisposalUtil.getResource("selectDeportationReasonTitle"),1);
        }
    }
    

    /**
     * Return true if the cancel button should alert the user
     */
    public boolean showCancelAlert() {
        // If data has been entered warn as we are going to lose it!
        return edited;
    }

    /**
     * Set the buttons visibility
     */
    public void setButtonVisible(boolean visiblePrev, boolean visibleNext, boolean visibleFinish, boolean visibleCancel) {
        WizardButtonPanel wizardButtonPanel = getButtonPanel();
        wizardButtonPanel.getBack().setVisible(visiblePrev);
        wizardButtonPanel.getNext().setVisible(visibleNext);
        wizardButtonPanel.getFinish().setVisible(visibleFinish);
        wizardButtonPanel.getCancel().setVisible(visibleCancel);
    }

    /**
     * Set the buttons enabledness
     */
    public void setButtonEnabled(boolean enablePrev, boolean enableNext, boolean enableFinish, boolean enableCancel) {
        WizardButtonPanel wizardButtonPanel = getButtonPanel();
        wizardButtonPanel.getBack().setEnabled(enablePrev);
        wizardButtonPanel.getNext().setEnabled(enableNext);
        wizardButtonPanel.getFinish().setEnabled(enableFinish);
        wizardButtonPanel.getCancel().setEnabled(enableCancel);
    }

    /**
     * Return true if the finish button is enabled
     */
    public boolean isFinishEnabled() {
        return getButtonPanel().getFinish().isEnabled();
    }

    /**
     * Enable / Disable the finish button
     */
    public void setFinishEnabled(boolean enableFinish) {
        getButtonPanel().getFinish().setEnabled(enableFinish);
    }

    /**
     * Enable / Disable the next button
     */
    public void setNextEnabled(boolean enableFinish) {
        getButtonPanel().getNext().setEnabled(enableFinish);
    }

    /**
     * return true if cancel was clicked
     */
    public boolean isCancelClicked() {
        return getLatestEvent() == CANCEL_EVENT;
    }

    /**
     * return true if cancel was clicked
     */
    public boolean isFinishedClicked() {
        return getLatestEvent() == FINISH_EVENT;
    }

    /**
     * Get the disposal reference
     */
    public DisposalReferenceValue getReference() {
        if (!isFinishedClicked()) {
            throw new IllegalStateException("finished: false");
        }
        return editDisposalPanel.getReference();
    }

    /**
     * Get the disposal value
     */
    public DisposalValue getValue() {
        if (!isFinishedClicked()) {
            throw new IllegalStateException("finished: false");
        }
        return editDisposalPanel.getValue();
    }

    /**
     * Get a reference to the application controller
     */
    public XhibitApplicationController getXhibitApplicationController() {
        return (XhibitApplicationController) getParent();
    }
    
   
    /**
     * Description: CCN0400 - KD - Sets the defendants current deportation reason
     * @returns String reason
     */
    private void setDeportationReason(DeportationModel reason){
        selectDisposalPanel.setDeportationReason(reason);
    }
    

    

 

}