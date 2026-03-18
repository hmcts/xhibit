package uk.gov.courtservice.xhibit.client.publicdisplayconfig.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisplayDocumentRenderer.java,v 1.4 2005/11/25 15:07:19 szfnvt
 *          Exp $
 */

public class DisplayDocumentRenderer extends JLabel implements ListCellRenderer {
    public DisplayDocumentRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        XhbDisplayDocumentBasicValue basicValue = ((RotationSetDDComplexValue) value).getDisplayDocumentBasicValue();
        String text = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT
                + ((XhbDisplayDocumentBasicValue) basicValue).getDescriptionCode()
                + nulltoString(((XhbDisplayDocumentBasicValue) basicValue).getLanguage())
                + nulltoString(((XhbDisplayDocumentBasicValue) basicValue).getCountry()));
        setText(text);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    private Object nulltoString(Object value) {
        return value != null ? value : "";
    }

}
