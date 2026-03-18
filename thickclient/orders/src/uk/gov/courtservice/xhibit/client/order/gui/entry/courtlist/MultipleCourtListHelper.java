package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;

/**
 * <p>
 * Title: MultipleCourtListHelper. A helper class that is called by class
 * MultipleCourtListPanel. The main purpose of this class is to display court
 * combo box list(s)
 * </p>
 * <p>
 * Description: This class uses two JPanels to display court radio buttons and
 * court combo box. Components are arranged by using gridbag layout, initially
 * all components are dispalyed on the screen. The components 'setVisible'
 * methods are updated by calling method 'checkXml' in class CourtListHelper
 * which checks xml values. An int value is returned from this method and is
 * passed as an argument to method setLayoutOrder in class CourtLayoutHelper.
 * The CourtLayoutHelper class performs the the actual arrangement of court
 * combo box components.
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
public class MultipleCourtListHelper extends JPanel {

    // Components to display a radio button.
    private CrownCourtOption crownCourtOption;

    private CourtOption magsCourtOption;

    private CourtOption youthCourtOption;

    private GridBagConstraints constraints;

    // A int value
    private int checkNo = -1;

    private CourtListHelper courtXml;

    // Xml values that are either true or false, if set to true the dsiplay
    // the court list
    private String crownCourt;

    private String magistratesCourt;

    private String youthCourt;

    private String orderTypes;

    // Component to display the court list.
    private JComboBox boxCb;

    // Xml values that contain default court names for each default court
    private String crownReference;

    private String magistrateReference;

    // JLabel components

    private static final String ORDER_MAG_COURT_LABEL = "order.magistrate.court.label";

    private static final String ORDER_YOUTH_COURT_LABEL = "order.youth.court.label";

    private static final String ORDER_CROWN_COURT_LABEL = "order.crown.court.label";

    private static final String ORDER_TYPE_BREACHED_LABEL = "order.type.breached.label";

    private static final String ORDER_COURT_NAME_LABEL = "order.court.name.label";

    private JLabel courtNameLabel;

    private JLabel magistrateName = new JLabel(ResourceHelper.getResourceString(ORDER_MAG_COURT_LABEL));

    private JLabel youthName = new JLabel(ResourceHelper.getResourceString(ORDER_YOUTH_COURT_LABEL));

    private JLabel crownCourtName = new JLabel(ResourceHelper.getResourceString(ORDER_CROWN_COURT_LABEL));

    private JLabel orderTypeName = new JLabel(ResourceHelper.getResourceString(ORDER_TYPE_BREACHED_LABEL));

    private ButtonGroup group = new ButtonGroup();

    private String courtLabel;

    /**
     * Returns true if key pressed is not a valid character.
     * 
     * @param opt1
     *            A CourtOption component
     * @param opt2
     *            A CourtOption component
     * @param opt3
     *            A CourtOption component
     * @param c
     *            A Gridbag constraints component
     * @param magistrate
     *            XML value (either true or false)
     * @param crown
     *            XML value (either true or false)
     * @param youth
     *            XML value (either true or false)
     * @param boxCb
     *            A JComboBox component
     * @param magistrateRef
     *            XML value (contains the default court name)
     * @param crownRef
     *            XML value (contains the default court name)
     * @param youthRef
     *            XML value (contains the default court name)
     * @param orderType
     *            XML value (contains the default court name)
     * @param courtLabel
     *            General court label that is used for various orders
     * @param courtName
     *            JLabel used to identify the court list
     * @return JPanel component
     */
    public JPanel createPanel(CourtOption opt1, CourtOption opt2, CourtOption opt3, GridBagConstraints c, String crown,
            String magistrate, String youth, JComboBox boxCb, String crownRef, String magistrateRef, String youthRef,
            String orderType, String courtLabel, JLabel courtName) {
        this.crownCourtOption = (CrownCourtOption) opt1;
        this.magsCourtOption = opt2;
        this.youthCourtOption = opt3;
        this.constraints = c;
        this.crownCourt = crown;
        this.magistratesCourt = magistrate;
        this.youthCourt = youth;
        this.boxCb = boxCb;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        this.orderTypes = orderType;
        this.courtLabel = courtLabel;
        group.add(crownCourtOption);
        group.add(magsCourtOption);
        group.add(youthCourtOption);
        courtName.setText(ResourceHelper.getResourceString(ORDER_COURT_NAME_LABEL));
        courtNameLabel = courtName;

        GridBagLayout thisLayout = new GridBagLayout();
        GridBagLayout gb = new GridBagLayout();

        // JPanel to hold the combobox component
        JPanel comboPanel = new JPanel();
        // JPanel to hold the radio button components
        JPanel radioPanel = new JPanel();

        comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.setLayout(gb);
        this.setLayout(thisLayout);

        // Initially display all components on the screen
        for (int i = 0; i < 3; i++) {
            constraints.gridx = 0;
            constraints.gridy = 0;

            radioPanel.add(crownCourtOption, constraints);

            constraints.gridy++;
            radioPanel.add(magsCourtOption, constraints);

            constraints.gridy++;
            radioPanel.add(youthCourtOption, constraints);
        }

        comboPanel.add(crownCourtName, constraints);
        comboPanel.add(courtName, constraints);
        comboPanel.add(magistrateName, constraints);
        comboPanel.add(youthName, constraints);

        comboPanel.add(boxCb); 
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(radioPanel, constraints);
        constraints.gridy++;
        this.add(comboPanel, constraints);

        setCourtComponents();
        return this;
    }

    /**
     * A method that checks combinations of xml values by instantiating class
     * CourtListHelper and calling its 'checkAttributes' method. This method
     * returns a int value and is passed into another instantiated class
     * CourtLayouthelper which arranges components by calling method
     * 'setLayoutOrder'.
     */
    private void setCourtComponents() {

        courtXml = new CourtListHelper();
        checkNo = courtXml.checkAttributes(crownCourt, magistratesCourt, youthCourt);

        CourtLayoutHelper layoutHelper = new CourtLayoutHelper();
        layoutHelper.setLayoutOrder(crownCourtOption, magsCourtOption, youthCourtOption,
                // option4,
                boxCb, courtNameLabel, crownCourtName, magistrateName, orderTypeName, youthName, checkNo,
                crownReference, magistrateReference, courtLabel);
    }

    /**
     * Enable Magistrate name label
     * 
     * @param enabled
     *            Enables the component.
     */
    public void setEnabled(boolean enabled) {
        crownCourtName.setEnabled(enabled);
        magistrateName.setEnabled(enabled);
        youthName.setEnabled(enabled);
    }
}
