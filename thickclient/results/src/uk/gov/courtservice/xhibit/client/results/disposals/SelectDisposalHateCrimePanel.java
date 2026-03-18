package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent.DefaultDisposalComponent;

/**
 * <p>
 * Title: SelectDisposalHateCrimePanel
 * </p>
 * <p>
 * Description: Display Hate Crime Options
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston 
 * @version 1.0 $
 */
public class SelectDisposalHateCrimePanel extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    private DisposalComponent parentDisposalComponent;

    //
    // Panel Constraints
    //
    private static GridBagConstraints createHateCrimeCheckBoxConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 1;
        constaints.insets = new Insets(0, 0, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox1Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 2;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox2Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox3Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 4;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox4Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 5;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox5Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 6;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox6Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 7;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    private static GridBagConstraints createHateCrimeCheckBox7Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 8;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox8Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 9;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createHateCrimeCheckBox9Constraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 10;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    //Screen Controls
    private JCheckBox hateCrimeCheckBox;
    private JLabel hateCrimelabel;
    private JCheckBox generalDisabilityCheckBox;
    private JCheckBox victimDisabilityCheckBox;
    private JCheckBox raciallyAggravatedCheckBox;
    private JCheckBox raceAndReligiousAggravatedCheckBox;
    private JCheckBox religiousAggravatedCheckBox;
    private JCheckBox generalSexualCheckBox;
    private JCheckBox victimSexualCheckBox;
    private JCheckBox generalTransgenderCheckBox;
    private JCheckBox victimTransgenderCheckBox;
    private JLabel generalDisabilityLabel = new JLabel();
    private JLabel victimDisabilityLabel = new JLabel();
    private JLabel raciallyAggravatedLabel = new JLabel();
    private JLabel raceAndReligiousAggravatedLabel = new JLabel();
    private JLabel religiouslyAggravatedLabel = new JLabel();
    private JLabel generalSexualLabel = new JLabel();
    private JLabel victimSexualLabel = new JLabel();
    private JLabel victimeSexualLabel = new JLabel();
    private JLabel generalTransgenderLabel = new JLabel();
    private JLabel victimeTransgenderLabel = new JLabel();
    private boolean hateCrimeTabVisiblility;

    
    /**
     * Description: This constructor is used when creating the edit disposal dialog screen from the EditDisposalPanel.
     * @param parentDisposalComponent
     */
    public SelectDisposalHateCrimePanel(DisposalComponent parentDisposalComponent){
        this();
        this.parentDisposalComponent = parentDisposalComponent;
    }

    /**
     * Description:  Constructor calls the super constructor and passes a new gridbay layout.
     *               The constructor also creates the check box and radio buttons that relate to the deportation options.
     *
     */
    public SelectDisposalHateCrimePanel(){
        super(new GridBagLayout());
        
        
        hateCrimeCheckBox = DisposalUtil.createCheckBox("selectDisposalHateCrimeCheckBox");
        hateCrimeCheckBox.addActionListener(this);
        add(hateCrimeCheckBox, createHateCrimeCheckBoxConstraints());
        
        generalDisabilityCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox1");
        generalDisabilityCheckBox.addActionListener(this);
        add(generalDisabilityCheckBox, createHateCrimeCheckBox1Constraints());
        
        victimDisabilityCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox2");
        victimDisabilityCheckBox.addActionListener(this);
        add(victimDisabilityCheckBox, createHateCrimeCheckBox2Constraints());
        
        raciallyAggravatedCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox3");
        raciallyAggravatedCheckBox.addActionListener(this);
        add(raciallyAggravatedCheckBox, createHateCrimeCheckBox3Constraints());
        
        raceAndReligiousAggravatedCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox4");
        raceAndReligiousAggravatedCheckBox.addActionListener(this);
        add(raceAndReligiousAggravatedCheckBox, createHateCrimeCheckBox4Constraints());
        
        religiousAggravatedCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox5");
        religiousAggravatedCheckBox.addActionListener(this);
        add(religiousAggravatedCheckBox, createHateCrimeCheckBox5Constraints());
        
        generalSexualCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox6");
        generalSexualCheckBox.addActionListener(this);
        add(generalSexualCheckBox, createHateCrimeCheckBox6Constraints());
        
        victimSexualCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox7");
        victimSexualCheckBox.addActionListener(this);
        add(victimSexualCheckBox, createHateCrimeCheckBox7Constraints());
        
        generalTransgenderCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox8");
        generalTransgenderCheckBox.addActionListener(this);
        add(generalTransgenderCheckBox, createHateCrimeCheckBox8Constraints());
        
        victimTransgenderCheckBox = DisposalUtil.createCheckBox("selectHateCrimeCheckBox9");
        victimTransgenderCheckBox.addActionListener(this);
        add(victimTransgenderCheckBox, createHateCrimeCheckBox9Constraints());
        
        
        
        //Assign default check box selection
        
        setCheckBoxes();
        setEmptySelection();
        
    }
    /**
     *Description:  If radio buttons are enabled, then it will disable the radio button and visa versa.
     */
    private void setCheckBoxes(){
        if(isCheckBoxSelected()){
            enableCheckBoxes(true);
        }
        else{
            enableCheckBoxes(false);
        }
    }
    
   
    private void setEmptySelection(){
        generalDisabilityCheckBox.setSelected(false);
        victimDisabilityCheckBox.setSelected(false);
        raciallyAggravatedCheckBox.setSelected(false);
        raceAndReligiousAggravatedCheckBox.setSelected(false);
        religiousAggravatedCheckBox.setSelected(false);
        generalSexualCheckBox.setSelected(false);
        victimSexualCheckBox.setSelected(false);
        generalTransgenderCheckBox.setSelected(false);
        victimTransgenderCheckBox.setSelected(false);
    }
    
    /**
     * Description: Returns a true value if the master Hate Crime check box is selected and at least
     * one reason check box has been selected.
     * @return boolean 
     */
    public boolean isHateCrimeSelected(){
        boolean isSelected = true;
        
        if(isCheckBoxSelected()){
             if(generalDisabilityCheckBox.isSelected()){
                isSelected = true;
            }else if(victimDisabilityCheckBox.isSelected()){
                isSelected = true;
            }else if(raciallyAggravatedCheckBox.isSelected()){
                isSelected = true;
            }else if(raceAndReligiousAggravatedCheckBox.isSelected()){
                isSelected = true;
            }if(religiousAggravatedCheckBox.isSelected()){
                isSelected = true;
            }else if(generalSexualCheckBox.isSelected()){
                isSelected = true;
            }else if(victimSexualCheckBox.isSelected()){
                isSelected = true;
            }else if(generalTransgenderCheckBox.isSelected()){
                isSelected = true;
            }else if(victimTransgenderCheckBox.isSelected()){
                isSelected = true;
            }else{
                isSelected = false;
            }
        }
        
        return isSelected;
    }
    
    /**
     * Description: Returns true is the CheckBox is Selected.
     * @Returns true if Checkbox is selected.
     */
    public boolean isCheckBoxSelected(){
        return hateCrimeCheckBox.isSelected();
    }
    
    /**
     *   ActionListener implementation
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(hateCrimeCheckBox)){
            setCheckBoxes();
        }
        
        //ensure event listener is notified of change.
        fireDisposalChanged();        
    }
    
    /**
     * Description: Retrieves Hate Crime Value/Reason
     * @return DeportationModel: Reason
     */
    public HateCrimeModel getHateCrimeReasons(){

        HateCrimeModel hateCrimeReasons = new HateCrimeModel();
        if(isCheckBoxSelected()){
            hateCrimeReasons.setHateCrimeFlag(true);
            if(generalDisabilityCheckBox.isSelected()){
                hateCrimeReasons.setGeneralDisability(true);
            }
            if(victimDisabilityCheckBox.isSelected()){
                hateCrimeReasons.setVictimDisability(true);
            }
            if(raciallyAggravatedCheckBox.isSelected()){
                hateCrimeReasons.setRacialAggravated(true);
            }
            if(raceAndReligiousAggravatedCheckBox.isSelected()){
                hateCrimeReasons.setRaceAndReligionAggravated(true);
            }
            if(religiousAggravatedCheckBox.isSelected()){
                hateCrimeReasons.setReligionAggravated(true);
            }
            if(generalSexualCheckBox.isSelected()){
                hateCrimeReasons.setGeneralSexual(true);
            }
            if(victimSexualCheckBox.isSelected()){
                hateCrimeReasons.setVictimSexual(true);
            }
            if(generalTransgenderCheckBox.isSelected()){
                hateCrimeReasons.setGeneralTransgender(true);
            }
            if(victimTransgenderCheckBox.isSelected()){
                hateCrimeReasons.setVictimTransgender(true);
            }
        }
        
        return hateCrimeReasons;
    }
    

    /**
     * Description: This method is used to set the default selected value for the hate crime reasons
     * @param HateCrimeModel hateCrimeReason
     * @return void
     */
    public void setHateCrimeReasons(HateCrimeModel hateCrimeReasons){
        if (hateCrimeReasons.getHateCrimeFlag()) {
            hateCrimeCheckBox.setSelected(true);
            enableCheckBoxes(true);
        }else{
            hateCrimeCheckBox.setSelected(false);
            enableCheckBoxes(false);
        }
        
        if(hateCrimeReasons.getGeneralDisability()){
            generalDisabilityCheckBox.setSelected(true);
        }else{
            generalDisabilityCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getVictimDisability()){
            victimDisabilityCheckBox.setSelected(true);
        }else{
            victimDisabilityCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getRacialAggravated()){
            raciallyAggravatedCheckBox.setSelected(true);
        }else{
            raciallyAggravatedCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getRaceAndReligionAggravated()){
            raceAndReligiousAggravatedCheckBox.setSelected(true);
        }else{
            raceAndReligiousAggravatedCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getReligionAggravated()){
            religiousAggravatedCheckBox.setSelected(true);
        }else{
            religiousAggravatedCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getGeneralSexual()){
            generalSexualCheckBox.setSelected(true);
        }else{
            generalSexualCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getVictimSexual()){
            victimSexualCheckBox.setSelected(true);
        }else{
            victimSexualCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getGeneralTransgender()){
            generalTransgenderCheckBox.setSelected(true);
        }else{
            generalTransgenderCheckBox.setSelected(false);
        }
        if(hateCrimeReasons.getVictimTransgender()){
            victimTransgenderCheckBox.setSelected(true);
        }else{
            victimTransgenderCheckBox.setSelected(false);
        }
    }
    
    
    /**
     * Description: Set checkboxes 
     * @param enabled
     * @return void
     */
    private void enableCheckBoxes(boolean enabled){
        generalDisabilityCheckBox.setEnabled(enabled);
        victimDisabilityCheckBox.setEnabled(enabled);
        raciallyAggravatedCheckBox.setEnabled(enabled);
        raceAndReligiousAggravatedCheckBox.setEnabled(enabled);
        religiousAggravatedCheckBox.setEnabled(enabled);
        generalSexualCheckBox.setEnabled(enabled);
        victimSexualCheckBox.setEnabled(enabled);
        generalTransgenderCheckBox.setEnabled(enabled);
        victimTransgenderCheckBox.setEnabled(enabled);
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
    
    
    public void setHateCrimeTabVisibility(boolean hateCrimeTabVisiblility) {
        this.hateCrimeTabVisiblility = hateCrimeTabVisiblility;
    }
    
    public boolean isHateCrimeTabVisible() {
        return this.hateCrimeTabVisiblility;
    }
}

