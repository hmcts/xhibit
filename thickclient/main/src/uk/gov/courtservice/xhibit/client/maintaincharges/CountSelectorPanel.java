package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.SelectorPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
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
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class CountSelectorPanel extends SelectorPanel {
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    public CountSelectorPanel(XPanel parentPanel, DefaultListModel allListModel, DefaultListModel targetListModel) {
        super(parentPanel, allListModel, targetListModel);
    }

    public CountSelectorPanel(XPanel parentPanel) {
        // Initialises list models for lists to prevent Null Pointer Exception.
        super(parentPanel, new DefaultListModel(), new DefaultListModel());
    }

    public JList getTargetList() {
        super.getTargetList();
        CountListRenderer targetListRenderer = new CountListRenderer();
        super.getTargetList().setCellRenderer(targetListRenderer);
        return super.getTargetList();
    }

    public JList getAllList() {
        super.getAllList();
        CountListRenderer allListRenderer = new CountListRenderer();

        super.getAllList().setCellRenderer(allListRenderer);
        return super.getAllList();
    }

    class CountListRenderer extends JLabel implements ListCellRenderer {
        public CountListRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            OffenceValue selectedCount = (OffenceValue) value;
            setText(XHIBITConstant.getResource(myResources, "count") + " " + selectedCount.getCrestOffenceSeqNo());
            return this;
        }
    }

}