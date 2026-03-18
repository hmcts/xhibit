package uk.gov.courtservice.xhibit.client.maintaincharges;

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

public class AddDefendantsToCounts_Title extends JPanel {
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    private Insets defaultInset = new Insets(4, 4, 4, 4);

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel title1Label;

    private JLabel title2Label;

    public AddDefendantsToCounts_Title() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AddDefendantsToCounts_Title(String title2) {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle2Label(title2);
    }

    private void jbInit() throws Exception {
        this.setPreferredSize(new Dimension(400, 80));
        this.setLayout(gridBagLayout1);
        this.add(getTitle1Label(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getTitle2Label(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 10, 2), 0, 0));
    }

    public JLabel getTitle1Label() {
        if (title1Label == null) {
            title1Label = new JLabel(XHIBITConstant.getResource(myResources, "addDefendantsToCountLabel"));
        }
        return title1Label;
    }

    public JLabel getTitle2Label() {
        if (title2Label == null) {
            // this should be passed through the constructor
            title2Label = new JLabel("Count 1: Manslaughter");
        }
        return title2Label;
    }

    public void setTitle2Label(String text) {
        title2Label.setText(text);
    }
}