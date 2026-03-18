package uk.gov.courtservice.xhibit.client.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class TextPaneTableCellRenderer implements TableCellRenderer {

    private Color backgroundColor = null;

    private Color foregroundColor = null;

    private Font font = null;

    private JTextPane textPane = new JTextPane();

    public TextPaneTableCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        String colorCode;

        if (isSelected) {
            colorCode = calculateRGBCode(table.getSelectionForeground());
            textPane.setBackground(table.getSelectionBackground());
            textPane.setForeground(table.getSelectionForeground());
        } else {
            colorCode = calculateRGBCode(table.getForeground());
            textPane.setBackground(table.getBackground());
            textPane.setForeground(table.getForeground());
        }

        textPane.setContentType("text/html");
        textPane.setText("<font face=dialog size=2 color=" + colorCode + ">" + (String) value + "</font>");

        return textPane;
    }

    private String calculateRGBCode(Color colorCodeIn) {
        String red;
        String green;
        String blue;
        StringBuffer rGBCode = new StringBuffer();

        red = padString(Integer.toHexString(colorCodeIn.getRed()));
        green = padString(Integer.toHexString(colorCodeIn.getGreen()));
        blue = padString(Integer.toHexString(colorCodeIn.getBlue()));

        rGBCode.append(red);
        rGBCode.append(green);
        rGBCode.append(blue);

        return rGBCode.toString();
    }

    private String padString(String code) {
        return (code.length() == 1 ? "0".concat(code) : code);
    }
}