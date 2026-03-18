package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.charges.AdditionalOffenceInfoAction;
import uk.gov.courtservice.xhibit.client.util.ChildOfXPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 112 01-04-2003 AW Daley Modified to display Offence statute, description and
 * ActSection.
 */
public class AddDefendantsToOffence_Title extends JPanel implements ChildOfXPanel {
   /**
    * TODO
    * This screen needs REFACTORING - Remove configTextArea and replace with simpler approach.
    * Need to keep consistent with other GUI screens 
    * Get resource from bundle.
    */
    
    private final String STATUTE_LABEL = "Statute:";

    private final int STATUTE_VALUE_NO_OF_ROWS = 1;

    private final String SECTION_LABEL = "Act/Section:";

    private final int SECTION_VALUE_NO_OF_ROWS = 1;

    private final int NO_OF_COLS = 30;

    private final String DESC_LABEL = "Description:";

    private final int DESC_VALUE_NO_OF_ROWS = 3;
    
    private final String END_DATE_LABEL = "End Date:";
    
    private AddDefendantsToOffencePanel parent;
    
    private DateFormat df = DateFormat.getDateInstance();
    
    private OffenceValue offenceValue;
    
    private JTextArea statuteValue;
    
    private JTextArea sectionValue;
    
    private JTextArea descValue;
    
    private JLabel endDateValueLabel;
    

    public AddDefendantsToOffence_Title() {
    }

    public AddDefendantsToOffence_Title(AddDefendantsToOffencePanel parent, XhibitApplicationController xac, OffenceValue offenceValue) {
        if (parent == null || xac == null || offenceValue == null){
            throw new IllegalArgumentException 
            ("AddDefendantsToOffence_Title - parent, xac, offenceValue parameters must contain values.");
        }
        
        this.parent = parent;
        this.offenceValue = offenceValue;        
        
        build();
    }

    public void stepUpdateViewState() throws CSRecoverableException{
        parent.stepUpdateViewState();
    }
    private void build() {
        this.setLayout(new GridBagLayout());

        // Create Instruction label
        String instructionText = ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources,
                "addDefendantsToCountLabel");
        JPanel instructionLabelPanel = new JPanel();
        instructionLabelPanel.setLayout(new BorderLayout());
        JLabel instructionLabel = new JLabel(instructionText);
        instructionLabelPanel.add(instructionLabel, BorderLayout.WEST);
        this.add(instructionLabelPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Create Offence Details Panel
        JPanel offenceDetailsPanel = new JPanel();

        JPanel additionalDetailsPanel = new JPanel();
        
        // Create Titled Border for panel
        String offenceDetailsBorder = ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources,
                "offenceDetailsBorderTitle");
        offenceDetailsPanel.setBorder(createdTitledBorder(offenceDetailsBorder));

        // Build Offence Details Panel
        offenceDetailsPanel.setLayout(new GridBagLayout());

        additionalDetailsPanel.setLayout(new GridBagLayout());
        
        // Add Statute Information
        JLabel statuteLabel = new JLabel(STATUTE_LABEL);
        offenceDetailsPanel.add(statuteLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Statute Value Field
        statuteValue = configTextArea(offenceValue.getStatute(), STATUTE_VALUE_NO_OF_ROWS, NO_OF_COLS, statuteLabel
                .getFont());

        offenceDetailsPanel.add(statuteValue, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Add Section Information
        JLabel sectionLabel = new JLabel(SECTION_LABEL);
        offenceDetailsPanel.add(sectionLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Section Value Field
        sectionValue = configTextArea(offenceValue.getActSection(), SECTION_VALUE_NO_OF_ROWS, NO_OF_COLS, sectionLabel
                .getFont());

        offenceDetailsPanel.add(sectionValue, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        // Add Statute Information
        JLabel descLabel = new JLabel(DESC_LABEL);
        offenceDetailsPanel.add(descLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Statute Value Field
        descValue = configTextArea(offenceValue.getOffenceDescription(), DESC_VALUE_NO_OF_ROWS, NO_OF_COLS, descLabel.getFont());

        offenceDetailsPanel.add(descValue, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        
        // Add Statute Information
        JLabel endDateLabel = new JLabel(END_DATE_LABEL);
        
        offenceDetailsPanel.add(endDateLabel, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        offenceDetailsPanel.add(getEndDateValueLabel(), new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        //Add additionalDetailsPanel to offenceDetailsPanel
        offenceDetailsPanel.add(additionalDetailsPanel, new GridBagConstraints(0, 3, 2, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        
        // Offence Details Panel 
        this.add(offenceDetailsPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Creates a lowered etched titled border with the title specified
     * 
     * @param title
     *            Border title
     * @retuns a lowered etched titled border
     */
    private Border createdTitledBorder(String title) {
        return BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), title);
    }

    /**
     * Creates a multi line read only text area
     * 
     * @param text
     *            text to display in text field
     * @param noRows
     *            number of rows of text
     * @param noCols
     *            number of columns of text
     * @param font
     *            text font.
     * @retuns a text area
     */
    private JTextArea configTextArea(String text, int noRows, int noCols, Font font) {
        
        JTextArea textArea = new JTextArea(nullToString(text), noRows, noCols);

        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setVisible(true);
        textArea.setFont(font);

        return textArea;
    }
    
    public JLabel getEndDateValueLabel(){
        if (endDateValueLabel == null){
            endDateValueLabel = new JLabel();
            endDateValueLabel.setPreferredSize(new Dimension(100, 20));
        }
        return endDateValueLabel;
    }
    
    protected void moveModelToScreen(){
        statuteValue.setText(offenceValue.getStatute());
        sectionValue.setText(offenceValue.getActSection());
        descValue.setText(offenceValue.getOffenceDescription());

        if (offenceValue.getOffenceEndDateTime()!= null){
            getEndDateValueLabel().setText(df.format(offenceValue.getOffenceEndDateTime().getTime()));
        }
    }
    
    private String nullToString(String str) {
        if (str == null)
            return "";
        return str;
    }
    
    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
}