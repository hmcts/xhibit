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
 * Title: SelectDisposalDeportationPanel
 * </p>
 * <p>
 * Description: Display Deportation Options
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Kelvin Davies CCN0400 (2009) 
 * @version $Revision: 1.6 $
 */
public class SelectDisposalDeportationPanel extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    private DisposalComponent parentDisposalComponent;

    //
    // Panel Constraints
    //
    private static GridBagConstraints createDeportationCheckBoxConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 1;
        constaints.insets = new Insets(0, 0, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createDeportationLabelConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 2;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createDeportationSentenceConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 3;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints labelConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 4;
        constaints.insets = new Insets(0, 60, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createDeportationCustodialConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 5;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints labelConstraints1() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 6;
        constaints.insets = new Insets(0, 60, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createDeportationSeriousDrugOffenceConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 7;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    private static GridBagConstraints labelConstraints2() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 8;
        constaints.insets = new Insets(0, 60, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    private static GridBagConstraints createDeportationRecommendedDeportationConstraints() {
        GridBagConstraints constaints = new GridBagConstraints();
        constaints.gridx = 0;
        constaints.gridy = 9;
        constaints.insets = new Insets(0, 40, 1, 0);
        constaints.anchor = GridBagConstraints.FIRST_LINE_START;
        return constaints;
    }
    
    //Screen Controls
    private JCheckBox deportCheckBox;
    private JLabel deportLabel;
    private ButtonGroup deportButtonGroup;
    private JRadioButton deportCustodialRadioButton;
    private JRadioButton deportSentenceRadioButton;
    private JRadioButton deportSeriousDrugOffenceRadioButton;
    private JRadioButton deportRecommendedDeportationRadioButton;
    private JLabel deportSentence = new JLabel();
    private JLabel deportCustodial = new JLabel();
    private JLabel deportSeriousDrugOffence = new JLabel();
    
    /**
     * Description: This constructor is used when creating the edit disposal dialog screen from the EditDisposalPanel.
     * @param parentDisposalComponent
     */
    public SelectDisposalDeportationPanel(DisposalComponent parentDisposalComponent){
        this();
        this.parentDisposalComponent = parentDisposalComponent;
    }

    /**
     * Description:  Constructor calls the super constructor and passes a new gridbay layout.
     *               The constructor also creates the check box and radio buttons that relate to the deportation options.
     *
     */
    public SelectDisposalDeportationPanel(){
        super(new GridBagLayout());
        
        deportCheckBox = DisposalUtil.createCheckBox("selectDisposalDeportationCheckBox");
        deportCheckBox.addActionListener(this);
        add(deportCheckBox, createDeportationCheckBoxConstraints());
        
        deportLabel = new JLabel(DisposalUtil.getResource("selectDisposalDeportationLabel"));
        add(deportLabel, createDeportationLabelConstraints());
        
        deportSentenceRadioButton = DisposalUtil.createRadioButton("selectDeportationSentenceRadioButton1");
        deportSentenceRadioButton.addActionListener(this);
        add(deportSentenceRadioButton, createDeportationCustodialConstraints());
        
        deportSentence.setText(DisposalUtil.getResource("selectDeportationSentenceRadioButton2"));
        add(deportSentence, labelConstraints());
        
        deportCustodialRadioButton = DisposalUtil.createRadioButton("selectDeportationCustodialRadioButton1");
        deportCustodialRadioButton.addActionListener(this);
        add(deportCustodialRadioButton, createDeportationSentenceConstraints());
        
        deportCustodial.setText(DisposalUtil.getResource("selectDeportationCustodialRadioButton2"));
        add(deportCustodial, labelConstraints1());
        
        deportSeriousDrugOffenceRadioButton = DisposalUtil.createRadioButton("selectDeportationSeriousDrugOffenceRadioButton1");
        deportSeriousDrugOffenceRadioButton.addActionListener(this);
        add(deportSeriousDrugOffenceRadioButton, createDeportationSeriousDrugOffenceConstraints());
        
        deportSeriousDrugOffence.setText(DisposalUtil.getResource("selectDeportationSeriousDrugOffenceRadioButton2"));
        add(deportSeriousDrugOffence, labelConstraints2());
        
        deportRecommendedDeportationRadioButton = DisposalUtil.createRadioButton("selectDeportationRecommendedDeportationRadioButton");
        deportRecommendedDeportationRadioButton.addActionListener(this);
        add(deportRecommendedDeportationRadioButton, createDeportationRecommendedDeportationConstraints());
        
        deportButtonGroup = new ButtonGroup();
        deportButtonGroup.add(deportSentenceRadioButton);
        deportButtonGroup.add(deportCustodialRadioButton);
        deportButtonGroup.add(deportSeriousDrugOffenceRadioButton);
        deportButtonGroup.add(deportRecommendedDeportationRadioButton);
        
        //Assign default radio button selection
        enableRadioButtons(false);
        setRadioButtons(false);
        setEmptySelection();
        
    }
    /**
     *Description:  If radio buttons are enabled, then it will disable the radio button and visa versa.
     */
    private void setRadioButtons(){
        if(isCheckBoxSelected()){
            deportLabel.setEnabled(true);
            enableRadioButtons(true);
        }
        else{
            deportLabel.setEnabled(false);
            enableRadioButtons(false);
        }
    }
    
   
    private void setEmptySelection(){
        deportSentenceRadioButton.setSelected(false);
        deportRecommendedDeportationRadioButton.setSelected(false);
        deportCustodialRadioButton.setSelected(false);
        deportSeriousDrugOffenceRadioButton.setSelected(false);
    }
    
    /**
     * Description: Returns a true value is Deportation check box is selected and a deportation radio
     *              button has been selected.
     * @return boolean 
     */
    public boolean isDeportationReasonSelected(){
        boolean isSelected = true;
        
        if(isCheckBoxSelected()){
             if(deportSentenceRadioButton.isSelected()){
                isSelected = true;
            }else if(deportSeriousDrugOffenceRadioButton.isSelected()){
                isSelected = true;
            }else if(deportRecommendedDeportationRadioButton.isSelected()){
                isSelected = true;
            }else if(deportCustodialRadioButton.isSelected()){
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
        return deportCheckBox.isSelected();
    }
    
    /**
     *   ActionListener implementation
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(deportCheckBox)){
            setRadioButtons();
        }
        
        //ensure event listener is notified of change.
        fireDisposalChanged();        
    }
    
    /**
     * Description: Retrieves Deportation Value/Reason
     * @return DeportationModel: Reason
     */
    public DeportationModel getDeportationReason(){

        DeportationModel deportationReason = new DeportationModel();
        if(isCheckBoxSelected()){
            if(deportCustodialRadioButton.isSelected()){
                deportationReason.setCustodial("Y");
                deportationReason.setSuspended("");
                deportationReason.setSeriousDrugOffence("");
                deportationReason.setRecommendedDeportation("");
            }else if(deportSentenceRadioButton.isSelected()){
                deportationReason.setSuspended("Y");
                deportationReason.setCustodial("");
                deportationReason.setSeriousDrugOffence("");
                deportationReason.setRecommendedDeportation("");
            }else if(deportSeriousDrugOffenceRadioButton.isSelected()){
                deportationReason.setSeriousDrugOffence("Y");
                deportationReason.setSuspended("");
                deportationReason.setCustodial("");
                deportationReason.setRecommendedDeportation("");
            }else if(deportRecommendedDeportationRadioButton.isSelected()){
                deportationReason.setRecommendedDeportation("Y");
                deportationReason.setSuspended("");
                deportationReason.setSeriousDrugOffence("");
                deportationReason.setCustodial("");
            }else{
                setRadioButtons(false); 
            }
        }else{
            setRadioButtons(false);
        }
        
        return deportationReason;
    }
    

    /**
     * Description: This method is used to set the default selected value for the deportation reason
     * @param DeportationModel deportReason
     * @return void
     */
    public void setDeportationReason(DeportationModel deportReasons){
        String value ="Y";
        if ((deportReasons.getSuspended() != null) && (deportReasons.getSuspended().equals(value))){
            deportCheckBox.setSelected(true);
            deportSentenceRadioButton.setSelected(true);
            enableRadioButtons(true);
        }else if ((deportReasons.getCustodial() != null) && (deportReasons.getCustodial().equals(value))){
            deportCheckBox.setSelected(true);
            deportCustodialRadioButton.setSelected(true);
            enableRadioButtons(true);
        }else if ((deportReasons.getSeriousDrugOffence() != null) && (deportReasons.getSeriousDrugOffence().equals(value))){
            deportCheckBox.setSelected(true);
            deportSeriousDrugOffenceRadioButton.setSelected(true);
            enableRadioButtons(true);
        }else if ((deportReasons.getRecommendedDeportation() != null) && (deportReasons.getRecommendedDeportation().equals(value))){
            deportCheckBox.setSelected(true);
            deportRecommendedDeportationRadioButton.setSelected(true);
            enableRadioButtons(true);
        }else{
            deportLabel.setEnabled(false);
            deportCheckBox.setSelected(false);

            setRadioButtons(false);
            enableRadioButtons(false);
        }
        
    }
    private void setRadioButtons(boolean flag){
        deportSentenceRadioButton.setSelected(flag);
        deportCustodialRadioButton.setSelected(flag);
        deportSeriousDrugOffenceRadioButton.setSelected(flag);
        deportRecommendedDeportationRadioButton.setSelected(flag);
    }
    
    
    /**
     * Description: Set Radio Buttons 
     * @param enabled
     * @return void
     */
    private void enableRadioButtons(boolean enabled){
        deportSentenceRadioButton.setEnabled(enabled);
        deportSentence.setEnabled(enabled);
        deportCustodialRadioButton.setEnabled(enabled);
        deportCustodial.setEnabled(enabled);
        deportSeriousDrugOffenceRadioButton.setEnabled(enabled);
        deportSeriousDrugOffence.setEnabled(enabled);
        deportRecommendedDeportationRadioButton.setEnabled(enabled);
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
 
}

