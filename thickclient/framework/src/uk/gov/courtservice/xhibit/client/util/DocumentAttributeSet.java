package uk.gov.courtservice.xhibit.client.util;

import java.util.ArrayList;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

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

public class DocumentAttributeSet {

    private static DocumentAttributeSet thisAttributeSet = null;

    private ArrayList attributeList = null;

    public static final int NOFORMATTING = 0;

    public static final int BOLD = 1;

    public static final int ITALICS = 2;

    public static final int UNDERLINE = 3;

    public static final int BOLD_ITALICS = 4;

    public static final int BOLD_ITALICS_UNDERLINE = 5;

    private DocumentAttributeSet() {
        attributeList = new ArrayList(6);

        // No formatting - item 0
        SimpleAttributeSet defaultSas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(defaultSas, XHIBITConstant.getCurrentFont().getFamily());
        StyleConstants.setFontSize(defaultSas, XHIBITConstant.getCurrentFont().getSize());
        attributeList.add(defaultSas);

        // Bold formatting - item 1
        SimpleAttributeSet sas = new SimpleAttributeSet(defaultSas);
        StyleConstants.setBold(sas, true);
        attributeList.add(sas);

        // Italics formatting - item 2
        sas = new SimpleAttributeSet();
        StyleConstants.setItalic(sas, true);
        attributeList.add(sas);

        // Underline formatting - item 3
        sas = new SimpleAttributeSet();
        StyleConstants.setUnderline(sas, true);
        attributeList.add(sas);

        // Bold & Italics formatting - item 4
        sas = new SimpleAttributeSet();
        StyleConstants.setBold(sas, true);
        StyleConstants.setItalic(sas, true);
        attributeList.add(sas);

        // Bold, Italics & Underline formatting - item 5
        sas = new SimpleAttributeSet();
        StyleConstants.setBold(sas, true);
        StyleConstants.setItalic(sas, true);
        StyleConstants.setUnderline(sas, true);
        attributeList.add(sas);

    }

    public static DocumentAttributeSet getInstance() {
        if (thisAttributeSet == null) {
            thisAttributeSet = new DocumentAttributeSet();
        }
        return thisAttributeSet;
    }

    public SimpleAttributeSet getAttibute(int formatAttribute) {
        if (formatAttribute < 0 || formatAttribute > (attributeList.size() - 1)) {
            // Pass back no formatting
            return (SimpleAttributeSet) attributeList.get(0);
        }
        return (SimpleAttributeSet) attributeList.get(formatAttribute);
    }
}