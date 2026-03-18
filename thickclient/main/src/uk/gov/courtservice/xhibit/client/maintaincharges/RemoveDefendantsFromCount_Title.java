package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
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
 * Description: Remove Defandants from Count Title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 
 */
public class RemoveDefendantsFromCount_Title extends JPanel implements ChildOfXPanel {
 
    private static final long serialVersionUID = 1L;
    
    private final String STATUTE_LABEL = "Statute:";

    private final int STATUTE_VALUE_NO_OF_ROWS = 1;

    private final String SECTION_LABEL = "Act/Section:";

    private final int SECTION_VALUE_NO_OF_ROWS = 1;

    private final int NO_OF_COLS = 30;

    private final String DESC_LABEL = "Description:";

    private final int DESC_VALUE_NO_OF_ROWS = 3;
    
    private RemoveDefendantsFromCountPanel parent;
    
    private OffenceValue offenceValue;
    
    private JTextArea statuteValue;
    
    private JTextArea sectionValue;
    
    private JTextArea descValue;
    

    public RemoveDefendantsFromCount_Title() {
        //Empty
    }

    public RemoveDefendantsFromCount_Title(RemoveDefendantsFromCountPanel parent, XhibitApplicationController xac, OffenceValue offenceValue) {
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
        String instructionText = ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources,
                "removeDefendantsToCountLabel.value");
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
        String offenceDetailsBorder = ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources,
                "offenceDetailsBorderTitle.value");
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
    
    
    
    protected void moveModelToScreen(){
        statuteValue.setText(offenceValue.getStatute());
        sectionValue.setText(offenceValue.getActSection());
        descValue.setText(offenceValue.getOffenceDescription());

    }
    
    private String nullToString(String str) {
        if (str == null)
            return "";
        return str;
    }
    
}