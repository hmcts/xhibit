package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.client.util.SelectorPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;

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

public class DefendantSelectorPanel extends SelectorPanel {

    public DefendantSelectorPanel(XPanel parentPanel, DefaultListModel allListModel, DefaultListModel targetListModel) {
        super(parentPanel, allListModel, targetListModel);
    }

    public JList getTargetList() {
        super.getTargetList();
        DefendantListRenderer targetListRenderer = new DefendantListRenderer();
        // targetListRenderer.setPreferredSize(new Dimension(250, 20));
        super.getTargetList().setCellRenderer(targetListRenderer);
        return super.getTargetList();
    }

    public JList getAllList() {
        super.getAllList();
        DefendantListRenderer allListRenderer = new DefendantListRenderer();
        // targetListRenderer.setPreferredSize(new Dimension(250, 20));
        super.getAllList().setCellRenderer(allListRenderer);
        return super.getAllList();
    }

    class DefendantListRenderer extends JLabel implements ListCellRenderer {
        public DefendantListRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue selectedDefendant = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) value;
            setText(selectedDefendant.getFirstName() + " " + selectedDefendant.getMiddleName() + " "
                    + selectedDefendant.getSurName());
            return this;
        }
    }

}