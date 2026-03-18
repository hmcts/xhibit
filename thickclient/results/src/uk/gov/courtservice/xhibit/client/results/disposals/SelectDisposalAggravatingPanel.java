package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * <p>
 * Title: SelectDisposalAggravatingPanel
 * </p>
 * <p>
 * Description: Display Aggravating
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris 
 * @version 1.0 $
 */
public class SelectDisposalAggravatingPanel extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    private DisposalComponent parentDisposalComponent;

    //
    // Panel Constraints
    //
    private static GridBagConstraints createAggravatingCheckBox1Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 2;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox2Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox3Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 4;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox4Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 5;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox5Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 6;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox6Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 7;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    private static GridBagConstraints createAggravatingCheckBox7Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 8;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createAggravatingCheckBox8Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 9;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    //Screen Controls
    private JCheckBox assaultOnWorkersCheckBox;
    private JCheckBox terroristConnectionCheckBox;
    private JCheckBox emergencyWorkersCheckBox;
    private JCheckBox hostilityCheckBox;
    private JCheckBox sexualOrientationCheckBox;
    private JCheckBox sexualOrientationOfVictimCheckBox;
    private JCheckBox transgenderCheckBox;
    private JCheckBox transgenderOfVictimCheckBox;
    private boolean aggravatingTabVisiblility;

    
    /**
     * Description: This constructor is used when creating the edit disposal dialog screen from the EditDisposalPanel.
     * @param parentDisposalComponent
     */
    public SelectDisposalAggravatingPanel(DisposalComponent parentDisposalComponent){
        this();
        this.parentDisposalComponent = parentDisposalComponent;
    }

    /**
     * Description:  Constructor calls the super constructor and passes a new gridbay layout.
     *               The constructor also creates the check box and radio buttons that relate to the deportation options.
     *
     */
    public SelectDisposalAggravatingPanel(){
        super(new GridBagLayout());
        
        assaultOnWorkersCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox1");
        assaultOnWorkersCheckBox.addActionListener(this);
        add(assaultOnWorkersCheckBox, createAggravatingCheckBox1Constraints());
        
        terroristConnectionCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox2");
        terroristConnectionCheckBox.addActionListener(this);
        add(terroristConnectionCheckBox, createAggravatingCheckBox2Constraints());
        
        emergencyWorkersCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox3");
        emergencyWorkersCheckBox.addActionListener(this);
        add(emergencyWorkersCheckBox, createAggravatingCheckBox3Constraints());
        
        hostilityCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox4");
        hostilityCheckBox.addActionListener(this);
        add(hostilityCheckBox, createAggravatingCheckBox4Constraints());
        
        sexualOrientationCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox5");
        sexualOrientationCheckBox.addActionListener(this);
        add(sexualOrientationCheckBox, createAggravatingCheckBox5Constraints());
        
        sexualOrientationOfVictimCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox6");
        sexualOrientationOfVictimCheckBox.addActionListener(this);
        add(sexualOrientationOfVictimCheckBox, createAggravatingCheckBox6Constraints());
        
        transgenderCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox7");
        transgenderCheckBox.addActionListener(this);
        add(transgenderCheckBox, createAggravatingCheckBox7Constraints());
        
        transgenderOfVictimCheckBox = DisposalUtil.createCheckBox("selectAggravatingCheckBox8");
        transgenderOfVictimCheckBox.addActionListener(this);
        add(transgenderOfVictimCheckBox, createAggravatingCheckBox8Constraints());
        
        //Assign default check box selection
        
        setCheckBoxes();
        setEmptySelection();
        
    }
    /**
     *Description:  If radio buttons are enabled, then it will disable the radio button and visa versa.
     */
    private void setCheckBoxes(){
        enableCheckBoxes(true);
    }
    
   
    private void setEmptySelection(){
        assaultOnWorkersCheckBox.setSelected(false);
        terroristConnectionCheckBox.setSelected(false);
        emergencyWorkersCheckBox.setSelected(false);
        hostilityCheckBox.setSelected(false);
        sexualOrientationCheckBox.setSelected(false);
        sexualOrientationOfVictimCheckBox.setSelected(false);
        transgenderCheckBox.setSelected(false);
        transgenderOfVictimCheckBox.setSelected(false);
    }
    
    /**
     * Description: Returns a true value if the master Hate Crime check box is selected and at least
     * one reason check box has been selected.
     * @return boolean 
     */
    public boolean isAggravatingSelected(){
        if(assaultOnWorkersCheckBox.isSelected()){
            return true;
        }else if(terroristConnectionCheckBox.isSelected()){
        	return true;
        }else if(emergencyWorkersCheckBox.isSelected()){
        	return true;
        }else if(hostilityCheckBox.isSelected()){
        	return true;
        }if(sexualOrientationCheckBox.isSelected()){
        	return true;
        }else if(sexualOrientationOfVictimCheckBox.isSelected()){
        	return true;
        }else if(transgenderCheckBox.isSelected()){
        	return true;
        }else if(transgenderOfVictimCheckBox.isSelected()){
        	return true;
        }else{
            return false;
        }
    }
       
    /**
     * Description: Retrieves Hate Crime Value/Reason
     * @return DeportationModel: Reason
     */
    public AggravatingReasonsModel getAggravatingReasons(){

        AggravatingReasonsModel aggravatingReasons = new AggravatingReasonsModel();
        if(assaultOnWorkersCheckBox.isSelected()){
            aggravatingReasons.setAssaultOnWorkers(true);
        }
        if(terroristConnectionCheckBox.isSelected()){
            aggravatingReasons.setTerroristConnection(true);
        }
        if(emergencyWorkersCheckBox.isSelected()){
            aggravatingReasons.setEmergencyWorkers(true);
        }
        if(hostilityCheckBox.isSelected()){
            aggravatingReasons.setHostility(true);
        }
        if(sexualOrientationCheckBox.isSelected()){
            aggravatingReasons.setSexualOrientation(true);
        }
        if(sexualOrientationOfVictimCheckBox.isSelected()){
            aggravatingReasons.setSexualOrientationOfVictim(true);
        }
        if(transgenderCheckBox.isSelected()){
            aggravatingReasons.setTransgender(true);
        }
        if(transgenderOfVictimCheckBox.isSelected()){
            aggravatingReasons.setTransgenderOfVictim(true);
        }
        
        return aggravatingReasons;
    }
    

    /**
     *   ActionListener implementation
     */
    public void actionPerformed(ActionEvent e) {
        //ensure event listener is notified of change.
        fireDisposalChanged();        
    }
    
    /**
     * Description: This method is used to set the default selected value for the aggravating reasons
     * @param AggravatingReasonsModel aggravatingReason
     * @return void
     */
    public void setAggravatingReasons(AggravatingReasonsModel aggravatingReasons){
        enableCheckBoxes(true);
        
        assaultOnWorkersCheckBox.setSelected(aggravatingReasons.isAssaultOnWorkers());
        terroristConnectionCheckBox.setSelected(aggravatingReasons.isTerroristConnection());
        emergencyWorkersCheckBox.setSelected(aggravatingReasons.isEmergencyWorkers());
        hostilityCheckBox.setSelected(aggravatingReasons.isHostility());
        sexualOrientationCheckBox.setSelected(aggravatingReasons.isSexualOrientation());
        sexualOrientationOfVictimCheckBox.setSelected(aggravatingReasons.isSexualOrientationOfVictim());
        transgenderCheckBox.setSelected(aggravatingReasons.isTransgender());
        transgenderOfVictimCheckBox.setSelected(aggravatingReasons.isTransgenderOfVictim());
    }
    
    
    /**
     * Description: Set checkboxes 
     * @param enabled
     * @return void
     */
    private void enableCheckBoxes(boolean enabled){
        assaultOnWorkersCheckBox.setEnabled(enabled);
        terroristConnectionCheckBox.setEnabled(enabled);
        emergencyWorkersCheckBox.setEnabled(enabled);
        hostilityCheckBox.setEnabled(enabled);
        sexualOrientationCheckBox.setEnabled(enabled);
        sexualOrientationOfVictimCheckBox.setEnabled(enabled);
        transgenderCheckBox.setEnabled(enabled);
        transgenderOfVictimCheckBox.setEnabled(enabled);
    }
    
    /**
     * Add the listener
     */
    public void addDisposalListener(DisposalListener listener) {
        listenerList.add(DisposalListener.class, listener);
    }

    /**
     * Remove the listener
     */
    public void removeDisposalListener(DisposalListener listener) {
        listenerList.remove(DisposalListener.class, listener);
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    protected void fireDisposalChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            DisposalEvent event = null;
            if (listeners[i] == DisposalListener.class) {
                // Lazily create the event:
                if (event == null) {
                    // We are effectively doing this for the
                    // parentDisposalComponent
                    event = new DisposalEvent(parentDisposalComponent, parentDisposalComponent.isComplete());
                }
                ((DisposalListener) listeners[i + 1]).disposalChanged(event);
            }
        }
    }
    
    
    public void setAggravatingTabVisibility(boolean aggravatingTabVisiblility) {
        this.aggravatingTabVisiblility = aggravatingTabVisiblility;
    }
    
    public boolean isAggravatingTabVisible() {
        return this.aggravatingTabVisiblility;
    }
}

