package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ScheduledHearingsPanel extends XPanel {
    private HearingRecordModel model;

    private XTable shTable;

    public ScheduledHearingsPanel(HearingRecordModel model) {
        super();
        this.model = model;

        this.setMaximumSize(new Dimension(320, 240));
        this.setMinimumSize(new Dimension(320, 240));
        this.setPreferredSize(new Dimension(320, 240));
        try {
            stepInitialise();
            jbInit();
            stepActivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jbInit() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), XHIBITConstant
                .getResource(XhibitBundles.HearingRecord, "scheduledHearings")));

        // Sort the scheduled hearing values saved in the model in ascending
        // orignial
        // time sequence
        Sorter.sort((List) model.getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue()
                .getHrScheduledHearingValues(), new String[] { "originalTime" }, Sorter.ASCENDING);

        this.shTable = XTableFactory.getInstance().createDefaultTable(new ScheduledHearingsTableModel(this.model));
        this.shTable.setPreferredScrollableViewportSize(new Dimension(200, 180));
        JScrollPane scrollPane = new JScrollPane(this.shTable);
        scrollPane.setMinimumSize(new Dimension(200, 180));
        panel.add(scrollPane);// , new GridBagConstraints(0, 0, 1, 1, 0.0,
        // 0.0, GridBagConstraints.WEST,
        // GridBagConstraints.NONE, new Insets(4, 4, 4,
        // 4), 0, 0));

        this.add(panel);
    }

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        // saving selected scheduled hearing id in model to be used to open
        // maintain hearing header
        int index = shTable.getSelectedRow();
        if (index != -1) // if a row is selected
        {
            Vector schedHearings = (Vector) model.getHearingRecordVal().getHearingRecordDisplayValue()
                    .getHrHearingDisplayValue().getHrScheduledHearingValues();
            model.setScheduledHearingId(((HRScheduledHearingValue) (schedHearings.get(index))).getScheduledHearingId());
        }
    }

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }
}