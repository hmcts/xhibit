package uk.gov.courtservice.xhibit.client.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//import javax.swing.text.html.HTMLDocument;
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

public class HtmlLabelTableCellRenderer implements TableCellRenderer {

    private Color backgroundColor = null;

    private Color foregroundColor = null;

    private Font font = null;

    private String fontName;

    private String fontSize;

    private JLabel myLabel = new JLabel();

    public HtmlLabelTableCellRenderer(String fontName, String fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    public HtmlLabelTableCellRenderer() {
        this.fontName = "dialog";
        this.fontSize = "2";
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        String colorCode;
        StringBuffer buf = new StringBuffer();

        myLabel.setOpaque(true);

        if (isSelected) {
            colorCode = calculateRGBCode(table.getSelectionForeground());
            myLabel.setBackground(table.getSelectionBackground());
            myLabel.setForeground(table.getSelectionForeground());
        } else {
            colorCode = calculateRGBCode(table.getForeground());
            myLabel.setBackground(table.getBackground());
            myLabel.setForeground(table.getForeground());
        }

        buf.append("<html><body><table border=0 width='100%'><tr><td><font face=");
        buf.append(fontName);
        buf.append(" size=");
        buf.append(fontSize);
        buf.append(" color=");
        buf.append(colorCode);
        buf.append(">");

        buf.append((String) value);

        buf.append("</font></td></tr></table></body></html>");

        myLabel.setText(buf.toString());

        return myLabel;
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