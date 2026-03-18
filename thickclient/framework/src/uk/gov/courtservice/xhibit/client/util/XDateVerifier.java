package uk.gov.courtservice.xhibit.client.util;

import java.awt.Toolkit;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Date Verifier used by XDatePanel
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
 * @version $Id: XDateVerifier.java,v 1.4 2006/06/05 12:30:39 bzjrnl Exp $
 */

public class XDateVerifier extends InputVerifier {
    XDatePanel _datePanel;

    public XDateVerifier(XDatePanel datePanel) {
        _datePanel = datePanel;
    }

    public boolean verify(JComponent input) {
        if (_datePanel.isDateValidate()) {
            return true;
        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane
                    .showMessageDialog(_datePanel, ResourceBundleHelper.getResource(XhibitBundles.ErrorText,
                            XDatePanel.dateError, new String[] { XDateFormat.simpleDateFormat }), ResourceBundleHelper
                            .getResource(XhibitBundles.XhibitConstant, "exception.validation.title"),
                            JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}