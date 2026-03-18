package uk.gov.courtservice.xhibit.client.linkedcases;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Linked Case Title panel contains the a message instructing user
 * </p>
 * <p>
 * Description: Linked Case Title panel contains the a message instructing user
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class LinkedCases_Title extends JPanel {
    /**
     * ResourceBundle myResources
     */
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);

    /**
     * Insets defaultInset
     */
    private Insets defaultInset = new Insets(4, 4, 4, 4);

    /**
     * GridBagLayout gridBagLayout1
     */
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
     * JLabel title1Label
     */
    private JLabel title1Label;

    /**
     * <init>
     */
    public LinkedCases_Title() {
        jbInit();
    }

    /**
     * jbInit
     */
    private void jbInit() {
        this.setPreferredSize(new Dimension(400, 80));
        this.setLayout(gridBagLayout1);
        this.add(getTitle1Label(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
    }

    /**
     * getTitle1Label
     * 
     * @return the returned JLabel
     */
    public JLabel getTitle1Label() {
        if (title1Label == null) {
            title1Label = new JLabel(XHIBITConstant.getResource(myResources, "lblLinkedCasesTitle"));
        }
        return title1Label;
    }
}