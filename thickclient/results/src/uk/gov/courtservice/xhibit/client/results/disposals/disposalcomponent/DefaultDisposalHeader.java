package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;

/**
 * <p>
 * Title: DefaultDisposalHeader
 * </p>
 * <p>
 * Description: Show header info about disposal
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class DefaultDisposalHeader extends JPanel {
    // Constraints
    private static final GridBagConstraints createCodeLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 4, 4, 2);
        return constraints;
    }

    private static final GridBagConstraints createCodeConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 2, 4, 2);
        return constraints;
    }

    private static final GridBagConstraints createVersionLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 2, 4, 2);
        return constraints;
    }

    private static final GridBagConstraints createVersionConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 2, 4, 2);
        return constraints;
    }

    private static final GridBagConstraints createTitleLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 2, 4, 2);
        return constraints;
    }

    private static final GridBagConstraints createTitleConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 2, 4, 4);
        return constraints;
    }

    // Components
    private final JLabel codeLabel = DisposalUtil.createDataLabel();

    private final JLabel versionLabel = DisposalUtil.createDataLabel();

    private final JLabel titleLabel = DisposalUtil.createDataLabel();

    /**
     * Construct a new instance
     */
    public DefaultDisposalHeader() {
        super(new GridBagLayout());

        add(DisposalUtil.createTitleLabel("defaultDisposalHeaderCode"), createCodeLabelConstraints());
        add(codeLabel, createCodeConstraints());

        add(DisposalUtil.createTitleLabel("defaultDisposalHeaderVersion"), createVersionLabelConstraints());
        add(versionLabel, createVersionConstraints());

        add(DisposalUtil.createTitleLabel("defaultDisposalHeaderTitle"), createTitleLabelConstraints());
        add(titleLabel, createTitleConstraints());
    }

    /**
     * Set the disposal code
     */
    public void setCode(String code) {
        codeLabel.setText(code);
    }

    /**
     * Set the title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     * Set the version
     */
    public void setVersion(int version) {
        versionLabel.setText(String.valueOf(version));
    }
}
