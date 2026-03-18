package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class DisableTableCellRender implements TableCellRenderer {
    JLabel jl = new JLabel();

    public DisableTableCellRender() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        jl.setOpaque(true);
        ScheduledHearingValue shv = ((ScheduledHearingValueHelper) ((XHIBITTableModelInterface) table.getModel())
                .getDataAt(row)).getModel();
        if (shv.getIsFloating() != null && shv.getIsFloating().booleanValue()) {
            jl.setBackground(SystemColor.inactiveCaption);
            jl.setForeground(SystemColor.inactiveCaptionText);
        } else {
            if (isSelected) {
                jl.setBackground(table.getSelectionBackground());
                jl.setForeground(table.getSelectionForeground());
            } else {
                jl.setBackground(table.getBackground());
                jl.setForeground(table.getForeground());
            }
        }

        jl.setText((String) value);

        return jl;
    }
}