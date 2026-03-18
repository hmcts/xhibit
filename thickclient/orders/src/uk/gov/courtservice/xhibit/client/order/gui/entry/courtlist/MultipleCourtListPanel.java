package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>
 * Title: MultipleCourtList. A JPanel component that displays three court radio
 * buttons (as part of a group) and one combobox that displays a court list.
 * </p>
 * <p>
 * Description: This class implements the CourtOption class to create the
 * components mentioned above.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */
public class MultipleCourtListPanel extends JPanel implements ItemListener {

    public static final String YOUTH = "Youth";

	public static final String MAGISTRATE = "Magistrate";

	// Component to display the court list.
    private JComboBox boxCb;

    // Components to display a radio button.
    private CourtOption option1;

    private CourtOption option2;

    private CourtOption option3;

    private CourtOption selectedCourt;

    // Gridbag constraints
    private GridBagConstraints constraints;

    // Xml values that are either true or false, if set to true the dsiplay
    // the court list
    private String crownCourt;

    private String magistratesCourt;

    private String youthCourt;

    private String orderTypes;

    // Xml values that contain default court names for each default court
    private String crownReference;

    private String magistrateReference;

    private String youthReference;

    // General court label that is used for various orders
    private String courtLabel;

    private JLabel courtNameLabel = new JLabel();

    private JPanel courtListPanel;

    /**
     * Construtor to create radiobuttons and court combobox - derived form class
     * CourtOption. Also create dataentrytemplate XML references that hold
     * true/false values and default court names
     * 
     * @param opt1
     *            A CourtOption component
     * @param opt2
     *            A CourtOption component
     * @param opt3
     *            A CourtOption component
     * @param boxCb
     *            A JComboBox component
     * @param c
     *            A Gridbag constraints component
     * @param magistrate
     *            XML value (either true or false)
     * @param crown
     *            XML value (either true or false)
     * @param youth
     *            XML value (either true or false)
     * @param magistrateRef
     *            XML value (contains the default court name)
     * @param crownRef
     *            XML value (contains the default court name)
     * @param youthRef
     *            XML value (contains the default court name)
     */

    public MultipleCourtListPanel(CourtOption opt1, CourtOption opt2, CourtOption opt3, JComboBox boxCb,
            GridBagConstraints c, String crown, String magistrate, String youth, String crownRef, String magistrateRef,
            String youthRef, String ordertypes, String courtLabel) {
        this.crownCourt = crown;
        this.magistratesCourt = magistrate;
        this.youthCourt = youth;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        this.youthReference = youthRef;
        this.option1 = opt1;
        // this.selectedCourt = opt1;
        // this.option1.setSelected(true); SCR53244
        this.option2 = opt2;
        this.option3 = opt3;

        // S. Bachra 27/5/03 SCR 52933 & 52926 - Setting selected attribute for
        // the selected option
        if (this.option1.isSelected()) {
            this.selectedCourt = this.option1;
        }

        if (this.option2.isSelected()) {
            this.selectedCourt = this.option2;
        }

        if (this.option3.isSelected()) {
            this.selectedCourt = this.option3;
        }

        this.option1.addItemListener(this);
        this.option2.addItemListener(this);
        this.option3.addItemListener(this);
        
        this.boxCb = boxCb;
        
        this.constraints = c;
        this.orderTypes = ordertypes;
        this.courtLabel = courtLabel;
        
        
       
        displayPanel();
    }

    /**
     * Call method 'createPanel' in class MultipleCourtListHelper to create
     * court combobox components.
     */

    public JPanel displayPanel() {

        MultipleCourtListHelper helper = new MultipleCourtListHelper();

        courtListPanel = helper.createPanel(option1, option2, option3, constraints, crownCourt, magistratesCourt,
                youthCourt, boxCb, crownReference, magistrateReference, youthReference, orderTypes, courtLabel,
                courtNameLabel);

        this.add(courtListPanel);
        return this;
    }

    /**
     * Returns a JComboBox.
     * 
     * @return boxCb
     */
    public JComboBox getboxCb() {
        return this.boxCb;
    }

    /**
     * Sets the court list to the selected radio button.
     */
    public void itemStateChanged(ItemEvent event) {
        this.selectedCourt = (CourtOption) event.getSource();
    }

    /**
     * Returns the selected court list.
     * 
     * @return selectedCourt
     */
    public CourtOption getSelectedCourt() {
        return this.selectedCourt;
    }

    /**
     * Enable Radio Buttons
     * 
     * @param enabled
     *            Enables radiobuttons.
     */
    public void setEnabled(boolean enabled) {
        this.option1.setEnabled(enabled);
        this.option2.setEnabled(enabled);
        this.option3.setEnabled(enabled);
        this.boxCb.setEnabled(enabled);
        // Explicitely disable the label
        courtNameLabel.setEnabled(enabled);
        courtListPanel.setEnabled(enabled);
    }
    
    public void setSelectedCourtType(String courtType){
    	clearAllRadioButtons();
    	if(courtType.equals(YOUTH))
    		option3.setSelected(true);
    	else if(courtType.equals(MAGISTRATE))
    		option2.setSelected(true);
    	else 
    		option1.setSelected(true);
    }
    
    private void clearAllRadioButtons(){
    	this.crownCourt="false";
    	this.magistratesCourt="false";
    	this.youthCourt ="false";
    	
    	option1.setSelected(false);
    	option2.setSelected(false);
    	option3.setSelected(false);
    }

}
