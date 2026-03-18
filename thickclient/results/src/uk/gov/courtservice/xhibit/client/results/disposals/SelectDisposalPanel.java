package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.client.results.ResultsReferenceFactory;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XTree;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;


/**
 * <p>
 * Title: SelectDisposalPanel
 * </p>
 * <p>
 * Description: This form displays the Dispsoal panel which allows the user to select a disposal to create.  The disposal panel
 * is made up of three individual panels: search, tree and deportation.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.12 $
 */
public class SelectDisposalPanel extends DisposalPanel {
    //
    // Panel Constraints
    //
    private static GridBagConstraints createSearchConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(4, 4, 2, 4);
        return constaints;
    }

    private static GridBagConstraints createTreeConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 1;
        constaints.weightx = 1.0;
        constaints.weighty = 1.0;
        constaints.fill = GridBagConstraints.BOTH;
        constaints.insets = new Insets(2, 4, 4, 4);
        return constaints;
    }

    private static GridBagConstraints createDeportationConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 2;
        constaints.weightx = 0;
        constaints.weighty = 0.0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(0, 0, 0, 0);
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.weightx = 0;
        constaints.weighty = 0.0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(0, 0, 0, 0);
        return constaints;
    }
    
    private static GridBagConstraints createTabConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.weightx = 0;
        constaints.weighty = 0.0;
        constaints.fill = GridBagConstraints.HORIZONTAL;
        constaints.insets = new Insets(0, 0, 0, 0);
        return constaints;
    }
    

    
    // Components
    private final SelectDisposalTreeModel model;
    private final XTree tree;
    private final JTabbedPane sdTabbedPane = new JTabbedPane();
    private SelectDisposalDeportationPanel deportationPanel;
    private SelectDisposalHateCrimePanel hateCrimePanel;
    private SelectDisposalAggravatingPanel aggravatingPanel;
    private boolean hateCrimeTabEnabled;
    private boolean aggravatingTabEnabled;


    

    /**
     * Construct a new Disposal Panel defendantOnCase
     */
    public SelectDisposalPanel(boolean hateCrimeTabEnabled, boolean aggravatingTabEnabled) throws CSRecoverableException {
        model = new SelectDisposalTreeModel();
        tree = new XTree(model);
        
        add(new SelectDisposalSearchPanel(tree), createSearchConstraints());
        add(new JScrollPane(tree), createTreeConstraints());
        sdTabbedPane.addTab("Deportation", deportationPanel = new SelectDisposalDeportationPanel());
        sdTabbedPane.addTab("Hate Crime", hateCrimePanel = new SelectDisposalHateCrimePanel());
        if (!hateCrimeTabEnabled) {
            sdTabbedPane.setEnabledAt(1,false);
        }
        sdTabbedPane.addTab("Aggravating Features", aggravatingPanel = new SelectDisposalAggravatingPanel());
        if (!aggravatingTabEnabled) {
            sdTabbedPane.setEnabledAt(2,false);
        }
        add(sdTabbedPane, createTabConstraints());
    }
    
    /**
     * Set the default action to be run when a disposal is selected
     */
    public void setDefaultDisposalAction(XAction disposalAction) {
        model.setDefaultDisposalAction(disposalAction);
    }

    /**
     * Listen for changes in selection
     */
    public void addTreeSelectionListener(TreeSelectionListener listener) {
        tree.addTreeSelectionListener(listener);
    }

    /**
     * Return true if a disposal has been selected
     */
    public boolean isDisposalSelected() {
        DisposalMenuReferenceValue menu = (DisposalMenuReferenceValue) tree.getSelectedNode();
        return menu != null && menu.isDisposal();
    }

    /**
     * Get the reference disposal data
     */
    public DisposalReferenceValue getReference() throws ResultsControllerException {
        DisposalMenuReferenceValue menu = (DisposalMenuReferenceValue) tree.getSelectedNode();
        
        if (menu == null || !menu.isDisposal()) {
            throw new IllegalStateException("menu: " + menu);
        }
        DisposalReferenceValue temp = ResultsReferenceFactory.getInstance().getLatestRecordSheetDisposal(menu.getDisposalCode());
        
        //Get the deportation reason entered and assign to the reference value
        DeportationModel deportReasons = deportationPanel.getDeportationReason();
        temp.setCustodial(deportReasons.getCustodial());
        temp.setSuspended(deportReasons.getSuspended());
        temp.setSeriousDrugOffence(deportReasons.getSeriousDrugOffence());
        temp.setRecommendedDeportation(deportReasons.getRecommendedDeportation());
        
        boolean aggravatingTabVisibility = aggravatingPanel.isAggravatingTabVisible();
        temp.setAggravatingTabVisibility(aggravatingTabVisibility);
        AggravatingReasonsModel aggravatingReasons = aggravatingPanel.getAggravatingReasons();
        temp.setAggravatingAssaultOnWorkers(aggravatingReasons.isAssaultOnWorkers());
        temp.setAggravatingTerroristConnection(aggravatingReasons.isTerroristConnection());
        temp.setAggravatingEmergencyWorkers(aggravatingReasons.isEmergencyWorkers());
        temp.setAggravatingHostility(aggravatingReasons.isHostility());
        temp.setAggravatingSexualOrientation(aggravatingReasons.isSexualOrientation());
        temp.setAggravatingSexualOrientationOfVictim(aggravatingReasons.isSexualOrientationOfVictim());
        temp.setAggravatingTransgender(aggravatingReasons.isTransgender());
        temp.setAggravatingTransgenderOfVictim(aggravatingReasons.isTransgenderOfVictim());
        
        boolean hateCrimeTabVisibility = hateCrimePanel.isHateCrimeTabVisible();
        temp.setHateCrimeTabVisibility(hateCrimeTabVisibility);
        HateCrimeModel hateCrimeReasons = hateCrimePanel.getHateCrimeReasons();
        temp.setGeneralDisability(hateCrimeReasons.getGeneralDisability());
        temp.setGeneralSexual(hateCrimeReasons.getGeneralSexual());
        temp.setGeneralTransgender(hateCrimeReasons.getGeneralTransgender());
        temp.setHateCrimeFlag(hateCrimeReasons.getHateCrimeFlag());
        temp.setRaceAndReligionAggravated(hateCrimeReasons.getRaceAndReligionAggravated());
        temp.setReligionAggravated(hateCrimeReasons.getReligionAggravated());
        temp.setRacialAggravated(hateCrimeReasons.getRacialAggravated());
        temp.setVictimDisability(hateCrimeReasons.getVictimDisability());
        temp.setVictimSexual(hateCrimeReasons.getVictimSexual());
        temp.setVictimTransgender(hateCrimeReasons.getVictimTransgender());
        
        return temp;
    }
    
    /**
     * Description:  CCN0400 Set Deportation Value for the deportation panel
     * @param DeportationModel:   reason
     * @return void
     *      
     */
    public void setDeportationReason(DeportationModel reason){
        deportationPanel.setDeportationReason(reason);
    }
    
    /**
     * Description: Return a boolean value that highlights if the user has selected the deportation check box. 
     * @return boolean 
     */
    public boolean isDeportationReasonSelected(){
        return deportationPanel.isDeportationReasonSelected();
    }
    
    /**
     * Description: Set Hate Crime Reasons
     * @param ArrayList <HateCrimeModel>:   reasons
     * @return void
     *      
     */
    public void setHateCrimeReasons(HateCrimeModel hateCrimeReasons){
        hateCrimePanel.setHateCrimeReasons(hateCrimeReasons);
    }
    
    public void setHateCrimeTabEnabled(boolean hateCrimeTabVisiblility) {
        hateCrimePanel.setHateCrimeTabVisibility(hateCrimeTabVisiblility);
    }
    
    public boolean isHateCrimeTabEnabled() {
        return hateCrimePanel.isHateCrimeTabVisible();
    }
 
    /**
     * Description: Set Aggravating Reasons
     * @param ArrayList <AggravatingReasonsModel>:   reasons
     * @return void
     *      
     */
    public void setAggravatingReasons(AggravatingReasonsModel model){
    	aggravatingPanel.setAggravatingReasons(model);
    }
    
    public void setAggravatingTabEnabled(boolean tabVisiblility) {
        aggravatingPanel.setAggravatingTabVisibility(tabVisiblility);
    }
    
    public boolean isAggravatingTabEnabled() {
        return aggravatingPanel.isAggravatingTabVisible();
    }
}