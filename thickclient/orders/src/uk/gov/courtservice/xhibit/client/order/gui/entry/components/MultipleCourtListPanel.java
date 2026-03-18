package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomCourtOption;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

import uk.gov.courtservice.xhibit.client.order.gui.helpers.MultipleCourtListHelper;


/**
 * <p>Title: MultipleCourtList.  A JPanel component that displays three court radio buttons
 * (as part of a group) and one combobox that displays a court list.</p>
 * <p>Description: This class implements the CustomCourtOption class to create the components
 * mentioned above. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Des
 * @version 1.0
 */
public class MultipleCourtListPanel extends JPanel implements ChangeListener{

    private static final long serialVersionUID = 1L;
    
    // Component to display the court list.
    private JComboBox boxCb;
     // Components to display a radio button.
    private CustomCourtOption option1;
    private CustomCourtOption option2;
    private CustomCourtOption option3;
    private CustomCourtOption option4;
    private CustomCourtOption selectedCourt;
    //Gridbag constraints
    private GridBagConstraints constraints;
    //Xml values that are either true or false, if set to true the dsiplay the court list
    private String crownCourt;
    private String magistratesCourt;
    private String youthCourt;
    private String orderTypes;
    //Xml values that contain default court names for each default court
    private String crownReference;
    private String magistrateReference;
    private String youthReference;
    //General court label that is used for various orders
    private String courtLabel;
    private JLabel courtName;
    /**
     * Construtor to create radiobuttons and court combobox - derived form class CustomCourtOption.
     * Also create dataentrytemplate XML references that hold true/false values and default court
     * names
     * @param opt1  		A CustomCourtOption component
     * @param opt2  		A CustomCourtOption component
     * @param opt3  		A CustomCourtOption component
     * @param opt4  		A CustomCourtOption component
     * @param boxCb 		A JComboBox component
     * @param c   		    A Gridbag constraints component
     * @param magistrate   	XML value (either true or false)
     * @param crown   		XML value (either true or false)
     * @param youth		   	XML value (either true or false)
     * @param magistrateRef XML value (contains the default court name)
     * @param crownRef 		XML value (contains the default court name)
     * @param youthRef 		XML value (contains the default court name)
    */

    public MultipleCourtListPanel(CustomCourtOption opt1,
                                  CustomCourtOption opt2,
                                  CustomCourtOption opt3,
                                  CustomCourtOption opt4,
                                  JComboBox boxCb,
                                  GridBagConstraints c,
                                  String crown,
                                  String magistrate,
                                  String youth,
                                  String crownRef,
                                  String magistrateRef,
                                  String youthRef,
                                  String ordertypes,
                                  String courtLabel,
                                  JLabel courtName)
    {
        this.crownCourt = crown;
        this.magistratesCourt = magistrate;
        this.youthCourt = youth;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        this.youthReference = youthRef;
        this.option1 = opt1;
        this.selectedCourt = opt1;
        this.option1.setSelected(true);
        this.option2 = opt2;
        this.option3 = opt3;
        this.option4 = opt4;
        this.option1.addChangeListener(this);
        this.option2.addChangeListener(this);
        this.option3.addChangeListener(this);
        this.boxCb = boxCb;
        this.constraints = c;
        this.orderTypes = ordertypes;
        this.courtLabel = courtLabel;
        this.courtName = courtName;
        displayPanel();

    }

    /**
     * Call method 'createPanel' in class MultipleCourtListHelper to create court combobox components.
    */

     public JPanel displayPanel(){

        MultipleCourtListHelper helper = new MultipleCourtListHelper();

        this.add(helper.createPanel(option1,
                                    option2,
                                    option3,
                                    option4,
                                    constraints,
                                    crownCourt,
                                    magistratesCourt,
                                    youthCourt,
                                    boxCb,
                                    crownReference,
                                    magistrateReference,
                                    youthReference,
                                    orderTypes,
                                    courtLabel,
                                    courtName));
        return this;
     }

    /**
     * Returns a JComboBox.
     * @return  boxCb
     */
    public JComboBox getboxCb(){
        return this.boxCb;
    }

    /**
     * Sets the court list to the selected radio button.
     */
    public void stateChanged(ChangeEvent event)
    {
        this.selectedCourt = (CustomCourtOption) event.getSource();
    }

    /**
     * Returns the selected court list.
     * @return selectedCourt
     */
    public CustomCourtOption getSelectedCourt()
    {
        return this.selectedCourt;
    }

    public void setEnabled(boolean enabled) {
        this.option2.setEnabled(enabled);
        this.option1.setEnabled(enabled);
        this.option3.setEnabled(enabled);
    }
}
