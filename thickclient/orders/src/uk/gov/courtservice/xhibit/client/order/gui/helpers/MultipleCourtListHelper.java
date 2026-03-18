package uk.gov.courtservice.xhibit.client.order.gui.helpers;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomCourtOption;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListHelper;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Title: MultipleCourtListHelper.  A helper class that is called by class MultipleCourtList.
 * The main purpose of this class is to display court combo box list(s) </p>
 * <p>Description: This class uses two JPanels to display court radio buttons and court combo box.
 * Components are arranged by using gridbag layout, initially all components are dispalyed on the screen.
 * The components 'setVisible' methods are updated by calling method 'checkXml' in class CourtListHelper which checks
 * xml values, these are set as true or false.  A int value is returned from this method and is passed as an argument
 * to method ?????? in class ?????.  This class performs the the actual arrangement of court combo box components.
 * in
 * It also . </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Des
 * @version 1.0
 */
public class MultipleCourtListHelper extends JPanel  {

    // Components to display a radio button.
    private CustomCourtOption option1;
    private CustomCourtOption option2;
    private CustomCourtOption option3;
    private CustomCourtOption option4;
    private GridBagConstraints constraints;
    // A int value
    private int checkNo = -1;
    private CourtListHelper courtXml;
    //Xml values that are either true or false, if set to true the dsiplay the court list
    private String crownCourt;
    private String magistratesCourt;
    private String youthCourt;
    private String orderTypes;
     // Component to display the court list.
    private JComboBox boxCb;
    //Xml values that contain default court names for each default court
    private String crownReference;
    private String magistrateReference;
      //JLabel components
    //private JLabel courtName = new JLabel("Court Name: ");  /new
    private JLabel courtName; // new
    private JLabel magistrateName = new JLabel("Magistrate Court: ");
    private JLabel youthName = new JLabel("Youth Court: ");
    private JLabel crownCourtName = new JLabel("Crown Court Name: ");
    private JLabel orderTypeName = new JLabel("Order Type Breached: ");
    private ButtonGroup group = new ButtonGroup();
    private String courtLabel;


    /**
     * Returns true if key pressed is not a valid character.
     * @param opt1  		A CustomCourtOption component
     * @param opt2  		A CustomCourtOption component
     * @param opt3  		A CustomCourtOption component
     * @param opt4  		A CustomCourtOption component
     * @param c   		    A Gridbag constraints component
     * @param magistrate   	XML value (either true or false)
     * @param crown   		XML value (either true or false)
     * @param youth		   	XML value (either true or false)
     * @param boxCb 		A JComboBox component
     * @param magistrateRef XML value (contains the default court name)
     * @param crownRef 		XML value (contains the default court name)
     * @param youthRef 		XML value (contains the default court name)
     * @param orderType 	XML value (contains the default court name)
     * @param courtLabel 	General court label that is used for various orders
     * @return   JPanel component
     */
    public JPanel createPanel(CustomCourtOption opt1,
                              CustomCourtOption opt2,
                              CustomCourtOption opt3,
                              CustomCourtOption opt4,
                              GridBagConstraints c,
                              String crown,
                              String magistrate,
                              String youth,
                              JComboBox boxCb,
                              String crownRef,
                              String magistrateRef,
                              String youthRef,
                              String orderType,
                              String courtLabel,
                              JLabel courtName){
        this.option1 = opt1;
        this.option2 = opt2;
        this.option3 = opt3;
        this.option4 = opt4;
        this.constraints = c;
        this.crownCourt = crown;
        this.magistratesCourt = magistrate;
        this.youthCourt = youth;
        this.boxCb = boxCb;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        //this.orderTypes = orderType;
        this.courtLabel = courtLabel;
        this.courtName = courtName;
        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        GridBagLayout thisLayout = new GridBagLayout();
        GridBagLayout gb = new GridBagLayout();

        // JPanel to hold the combobox component
        JPanel comboPanel = new JPanel();
        // JPanel to hold the radio button components
        JPanel radioPanel = new JPanel();

        comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        radioPanel.setLayout(gb);
        FlowLayout f = new FlowLayout();
        this.setLayout(thisLayout);

        //Initially display all components on the screen
        for (int i = 0; i<3; i++){
            constraints.gridx = 0;
            constraints.gridy = 0;

            radioPanel.add(option1,constraints);

            constraints.gridy++;
            radioPanel.add(option2,constraints);

            constraints.gridy++;
            radioPanel.add(option3,constraints);
        }

        comboPanel.add(crownCourtName,constraints);
        comboPanel.add(this.courtName,constraints);
        comboPanel.add(magistrateName,constraints);
        comboPanel.add(youthName,constraints);
        //comboPanel.add(orderTypeName,constraints);

        comboPanel.add(boxCb);

        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(radioPanel,constraints);
        constraints.gridy++;
        this.add(comboPanel,constraints);

        setCourtComponents();
        return this;
    }

    /**
     * A method that checks combinations of xml values by instantiating class CourtListHelper and calling
     * its 'checkAttributes' method.  This method returns a int value and is passed into another instantiated
     * class CourtLayouthelper, and calling method 'setLayoutOrder'.
     */
    private void setCourtComponents(){

    courtXml = new CourtListHelper();
    checkNo = courtXml.checkAttributes(crownCourt,magistratesCourt,youthCourt);

    CourtLayoutHelper layoutHelper = new CourtLayoutHelper();
    layoutHelper.setLayoutOrder(option1,
                                option2,
                                option3,
                                option4,
                                boxCb,
                                this.courtName,
                                crownCourtName,
                                magistrateName,
                                orderTypeName,
                                youthName,
                                checkNo,
                                crownReference,
                                magistrateReference,
                                courtLabel);
    }

     public void setEnabled(boolean enabled) {
        magistrateName.setEnabled(enabled);
        this.courtName.setEnabled(enabled);
    }

}
