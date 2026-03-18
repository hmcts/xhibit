package uk.gov.courtservice.xhibit.client.linkedcases;

import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.client.util.SelectorPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Panel is used to contain and manipulate all available and selected
 * cases for linking
 * </p>
 * <p>
 * Description: The cases can be moved between the available and target list
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class LinkedCasesSelectorPanel extends SelectorPanel {
    /**
     * Constructor takes in parent panel, and 2 lists for to display
     */
    public LinkedCasesSelectorPanel(XPanel parentPanel, DefaultListModel allListModel, DefaultListModel targetListModel) {
        super(parentPanel, allListModel, targetListModel);
    }

    /**
     * getTargetList - returns list of all selected cases for linking
     * 
     * @return the returned JList
     */
    public JList getTargetList() {
        super.getTargetList();
        LinkedCaseListRenderer targetListRenderer = new LinkedCaseListRenderer();
        // targetListRenderer.setPreferredSize(new Dimension(250, 20));
        super.getTargetList().setCellRenderer(targetListRenderer);
        return super.getTargetList();
    }

    /**
     * getAllList - returns available cases for linking
     * 
     * @return the returned JList
     */
    public JList getAllList() {
        super.getAllList();
        LinkedCaseListRenderer allListRenderer = new LinkedCaseListRenderer();
        // targetListRenderer.setPreferredSize(new Dimension(250, 20));
        super.getAllList().setCellRenderer(allListRenderer);
        return super.getAllList();
    }

    class LinkedCaseListRenderer extends JLabel implements ListCellRenderer {
        /**
         * <init>
         */
        public LinkedCaseListRenderer() {
            setOpaque(true);
        }

        /**
         * getListCellRendererComponent
         * 
         * @param list
         *            parameter for getListCellRendererComponent
         * @param value
         *            parameter for getListCellRendererComponent
         * @param index
         *            parameter for getListCellRendererComponent
         * @param isSelected
         *            parameter for getListCellRendererComponent
         * @param cellHasFocus
         *            parameter for getListCellRendererComponent
         * @return the returned Component
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            CaseSchedHearingValue selectedDefendant = (CaseSchedHearingValue) value;
            setText(selectedDefendant.getCaseType() + selectedDefendant.getCaseNumber());
            return this;
        }
    }
}