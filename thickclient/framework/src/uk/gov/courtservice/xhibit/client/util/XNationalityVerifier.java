package uk.gov.courtservice.xhibit.client.util;

import java.awt.Toolkit;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;


public class XNationalityVerifier extends InputVerifier {
    XNationalityPanel _nationalityPanel;

    public XNationalityVerifier(XNationalityPanel nationalityPanel) {
        _nationalityPanel = nationalityPanel;
    }

    public boolean verify(JComponent input) {
        if (_nationalityPanel.isNationalityValid()) {
            return true;
        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(
                    _nationalityPanel, 
                    ResourceBundleHelper.getResource(XhibitBundles.ErrorText, XNationalityPanel.nationalityError), 
                    ResourceBundleHelper.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}