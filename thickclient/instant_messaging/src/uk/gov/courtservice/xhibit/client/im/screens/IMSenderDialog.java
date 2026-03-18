package uk.gov.courtservice.xhibit.client.im.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.helper.IMExternalSenderHelper;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.im.helper.IMSenderHelper;
import uk.gov.courtservice.xhibit.client.im.util.IMResourceHelper;
import uk.gov.courtservice.xhibit.client.im.util.IMStringFormatter;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: IMSenderDialog
 * </p>
 * <p>
 * Description: Instant / Ad-Hoc Messaging main dialog Sends instnat and ad-hoc
 * messages from the client to the selected destinations
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMSenderDialog extends XDialog {

    private static final String IM_TITLE = "im.dialog.title";

    private static final String IM_DLG_BTN_TXT = "im.dialog.btn.text";

    private static final String IM_BTN_SEND_LBL = "im.send.btn.txt";

    private static final String IM_OK_BTN_TXT = "OK";

    private static final String IM_CAN_BTN_TXT = "Cancel";

    private static final String IM_TAB_LABEL = "im.tab.label";

    private static final String IM_ADHOC_TAB_LABEL = "im.tab.adhoc.label";

    private static final String IM_INTERNAL_ERROR = "im.internal.error.label";

    private static final String IM_EXTERNAL_ERROR = "im.external.error.label";

    private static final int IM_MESSAGE_LEN = 120;

    private static final int IM_DLG_HEIGHT = 350;

    private static final int IM_DLG_WIDTH = 450;

    private static final GridBagConstraints defaultGridBag = new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);

    private IMSenderHelper imHelper;

    private IMExternalSenderHelper imExtHelper;

    private double xCoord = 0.0;

    private double yCoord = 0.0;

    private XhibitApplicationController xac;

    private static Logger log = CSServices.getLogger(IMSenderDialog.class);

    /**
     * Constructor for Messaging dialog
     * 
     * @throws CSRecoverableException
     */
    public IMSenderDialog(XhibitApplicationController xac) throws CSRecoverableException {
        // super(xac,
        // RL: 31/10/03 - changed to be new XFrame as the parent
        // is not linked to a particular XAC.
        // XFrame used so that we get the correct icon.
        super(new uk.gov.courtservice.xhibit.client.util.XFrame(), IMResourceHelper.getResourceString(IM_TITLE), false,
                XDialog.OKCANCEL, XDialog.DEFAULTOK);
        this.xac = xac;

        imHelper = new IMSenderHelper();
        imExtHelper = new IMExternalSenderHelper();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        if (this.xac != null) {
            xCoord = getCoord(this.xac.getBounds().getX(), this.xac.getBounds().getWidth(), IM_DLG_WIDTH);
            yCoord = getCoord(this.xac.getBounds().getY(), this.xac.getBounds().getHeight(), IM_DLG_HEIGHT);
        }
        initialise();
    }

    /**
     * Sets up the Messaging dialog
     * 
     * @throws JMSException
     * @throws NamingException
     */
    private void initialise() {
        GridBagConstraints gbc = (GridBagConstraints) this.defaultGridBag.clone();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JTabbedPane tabbedPane = new JTabbedPane();
        IMDisplayPanel imPanel = new IMDisplayPanel(/* xac */);
        imPanel.setLayout(new GridBagLayout());

        // Bug 53949 - removed the label.
        // imPanel.add(new
        // JLabel(IMStringFormatter.formatName(IMLocationHelper.getLocationID()
        // + " - " + IMLocationHelper.getTerminalName())));

        gbc.gridy++;
        try {
            // Create the Internal (instant) messaging tab
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), imHelper.makeSendInternalPanel(
                    getContext(), this));
        } catch (JMSException jmse) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getIMErrorPanel());
        } catch (NamingException ne) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getIMErrorPanel());
        } catch (CSRecoverableException csre) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getIMErrorPanel());
        }

        try {
            // Create the External (ad-hoc) messaging tab
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_ADHOC_TAB_LABEL), imExtHelper
                    .makeSendExternalPanel(getContext(), this));
        } catch (JMSException jmse) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getExtErrorPanel());
        } catch (NamingException ne) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getExtErrorPanel());
        } catch (CSRecoverableException csre) {
            tabbedPane.addTab(IMResourceHelper.getResourceString(IM_TAB_LABEL), getExtErrorPanel());
        }

        imPanel.add(tabbedPane, gbc);

        // Reset the TooltipText on the XDialog OK button
        this.getButton(IM_CAN_BTN_TXT).setToolTipText(IMResourceHelper.getResourceString(IM_DLG_BTN_TXT));
        // Rename the XDialog OK button
        this.getButton(IM_CAN_BTN_TXT).setText(IMResourceHelper.getResourceString(IM_DLG_BTN_TXT));
        // Hide the XDialog Cancel button
        this.getButton(IM_OK_BTN_TXT).setVisible(false);

        this.addBodyPanel(imPanel);
        this.setBounds((int) xCoord, (int) yCoord, IM_DLG_WIDTH, IM_DLG_HEIGHT);
        this.setVisible(true);
    }

    private String getContext() throws CSRecoverableException {
        // sets the context to be the root context (e.g. jms/im) and the court
        // name.
        // This only shows locations under the court name, so the actual court
        // name is
        // not displayed in the tree.
        return IMLocationHelper.getRootContext() + "/"
                + IMStringFormatter.formatTreeNode(IMLocationHelper.getCourtID());
    }

    /**
     * Returns the appropriate button from the XDialog depending on the text
     * required
     * 
     * @param buttonType
     *            "OK" or "Cancel"
     * @return the button
     */
    private JButton getButton(String buttonType) {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getText().equals(buttonType))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
    }

    /**
     * Reset the entry fields on redisplay
     * 
     * @throws CSRecoverableException
     */
    public void reset() throws CSRecoverableException {
        try {
            imHelper.reset(getContext());
        } catch (JMSException ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        } catch (NamingException ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        } catch (CSRecoverableException ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        } catch (Exception ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        }

        imExtHelper.reset();
    }

    private double getXCoord(XhibitApplicationController xac) {
        double start = xac.getBounds().getX();
        double cWidth = xac.getBounds().getWidth();
        return start + ((cWidth - IM_DLG_WIDTH) / 2);
    }

    private double getCoord(double start, double measure, int param) {
        return start + ((measure - param) / 2);
    }

    private JPanel getIMErrorPanel() {
        return getErrorPanel(IMResourceHelper.getResourceString(IM_INTERNAL_ERROR));
    }

    private JPanel getExtErrorPanel() {
        return getErrorPanel(IMResourceHelper.getResourceString(IM_EXTERNAL_ERROR));
    }

    private JPanel getErrorPanel(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(message);
        label.setHorizontalTextPosition(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

}