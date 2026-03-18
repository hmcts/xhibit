package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
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
public class CopyDefOnOffenceSelectorPanel extends SelectorPanel {
    private ResultsRowValue rrv;

    private String offenceName;

    private String defendantName;

    public CopyDefOnOffenceSelectorPanel(XPanel parentPanel, DefaultListModel allListModel,
            DefaultListModel targetListModel) {
        super(parentPanel, allListModel, targetListModel);
    }

    public CopyDefOnOffenceSelectorPanel(XPanel parentPanel) {
        // Initialises list models for lists to prevent Null Pointer Exception.
        super(parentPanel, new DefaultListModel(), new DefaultListModel());
    }

    public JList getTargetList() {
        super.getTargetList();
        DefOffenceListRenderer targetListRenderer = new DefOffenceListRenderer();
        super.getTargetList().setCellRenderer(targetListRenderer);
        return super.getTargetList();
    }

    public JList getAllList() {
        super.getAllList();
        DefOffenceListRenderer allListRenderer = new DefOffenceListRenderer();

        super.getAllList().setCellRenderer(allListRenderer);
        return super.getAllList();
    }

    class DefOffenceListRenderer extends JLabel implements ListCellRenderer {
        public DefOffenceListRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            rrv = (ResultsRowValue) value;
            offenceName = rrv.getDisposalOffenceText();
            defendantName = rrv.getDefendantValue().getSurName() + " " + rrv.getDefendantValue().getFirstName();
            setText(offenceName + ", " + defendantName);
            return this;
        }
    }

}