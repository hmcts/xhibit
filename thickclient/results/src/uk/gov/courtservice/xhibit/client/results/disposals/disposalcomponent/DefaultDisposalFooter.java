package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentListener;

/**
 * <p>
 * Title: DefaultDisposalFooter
 * </p>
 * <p>
 * Description: Show footer info about disposal
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 */
public class DefaultDisposalFooter extends JPanel implements InsertComponentListener {
    // Constants
    private static Color ERROR_COLOR = new Color(255, 0, 0);

    // Constraints
    private static final GridBagConstraints createLineLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        return constraints;
    }

    private static final GridBagConstraints createLineConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        return constraints;
    }

    // Data
    private final List inserts = new ArrayList();

    // Component
    private final JLabel lineLabel = DisposalUtil.createDataLabel("0");

    private final Color okColor = lineLabel.getForeground();

    // The number of lines avail
    private int lineAvail;

    // The number of lines used
    private int lineUsed;

    public DefaultDisposalFooter() {
        super(new GridBagLayout());
        add(DisposalUtil.createTitleLabel("defaultDisposalFooterLines"), createLineLabelConstraints());
        add(lineLabel, createLineConstraints());
    }

    public boolean tooManyLines() {
        return lineUsed > lineAvail;
    }

    public void setLineAvail(int lineAvail) {
        this.lineAvail = lineAvail;
        setLineRemain(lineAvail - lineUsed);
    }

    public int getLineAvail() {
        return lineAvail;
    }

    public void setLineUsed(int lineUsed) {
        this.lineUsed = lineUsed;
        setLineRemain(lineAvail - lineUsed);
    }

    public int getLineUsed() {
        return lineUsed;
    }

    public void setLineRemain(int lineRemain) {
        lineLabel.setText(String.valueOf(lineRemain));
        setError(lineRemain < 0);
    }

    public int getLineRemain() {
        return Integer.parseInt(lineLabel.getText());
    }

    /**
     * Register the insert component to monitor total number of lines
     */
    public void add(InsertComponent insert) {
        if (insert != null) {
            inserts.add(insert);
            insert.addInsertComponentListener(this);
        }
    }

    public void insertChanged(InsertComponentEvent e) {
        int lineUsed = 0;
        for (int i = 0, s = inserts.size(); i < s; i++) {
            InsertComponent insert = (InsertComponent) inserts.get(i);
            if (insert.isEnabled()) {
                lineUsed += insert.getLineCount();
            }
        }
        setLineUsed(lineUsed);
    }

    public void setError(boolean error) {
        lineLabel.setForeground(error ? ERROR_COLOR : okColor);
        for (int i = 0, s = inserts.size(); i < s; i++) {
            ((InsertComponent) inserts.get(i)).setError(error);
        }
    }
}
