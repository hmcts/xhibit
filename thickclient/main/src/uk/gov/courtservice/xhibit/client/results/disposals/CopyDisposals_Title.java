package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * @author Bal Bhamra
 * @version 1.0
 */
public class CopyDisposals_Title extends JPanel {
    private static final Dimension prefDim = new Dimension(200, XHIBITConstant.getLineHeight());

    private static final Insets defaultInset = XHIBITConstant.nonContainerInsets;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel disposalDescriptionLabel;

    private JLabel userInstructionsLabel;

    private String disposalDescription;

    private String userInstructions;

    public CopyDisposals_Title() {
        jbInit();
    }

    public CopyDisposals_Title(String disposalDescription, String userInstructions) {
        this.disposalDescription = disposalDescription;
        this.userInstructions = userInstructions;
        jbInit();
    }

    private void jbInit() {
        // this.setPreferredSize(new Dimension(400,80));
        this.setLayout(gridBagLayout1);
        this.add(getDisposalDescriptionLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 10));
        this.add(getUserInstructionsLabel(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 10, 2), 0, 10));
    }

    public JLabel getDisposalDescriptionLabel() {
        if (disposalDescriptionLabel == null) {
            disposalDescriptionLabel = new JLabel(disposalDescription);
            disposalDescriptionLabel.setPreferredSize(prefDim);
        }
        return disposalDescriptionLabel;
    }

    public JLabel getUserInstructionsLabel() {
        if (userInstructionsLabel == null) {
            userInstructionsLabel = new JLabel(userInstructions);
            disposalDescriptionLabel.setPreferredSize(prefDim);
        }
        return userInstructionsLabel;
    }

    public void setdisposalDescription(String text) {
        disposalDescriptionLabel.setText(text);
    }

    public void setUserInstructions(String text) {
        userInstructionsLabel.setText(text);
    }
}