package uk.gov.courtservice.xhibit.client.im.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.im.entry.IMTextArea;
import uk.gov.courtservice.xhibit.client.im.util.IMDocumentListener;
import uk.gov.courtservice.xhibit.client.im.util.IMExternalSendMessageActionListener;
import uk.gov.courtservice.xhibit.client.im.util.IMLimitedTextDocument;
import uk.gov.courtservice.xhibit.client.im.util.IMNumericKeyListener;
import uk.gov.courtservice.xhibit.client.im.util.IMResourceHelper;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

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

public class IMExternalSenderHelper extends XDialog {

    private static final Logger log = CSServices.getLogger(IMExternalSenderHelper.class);

    private static final String IM_TITLE = "im.dialog.title";

    private static final String IM_DLG_BTN_TXT = "im.dialog.btn.text";

    private static final String IM_BTN_SEND_LBL = "im.send.btn.txt";

    private static final String IM_OK_BTN_TXT = "OK";

    private static final String IM_CAN_BTN_TXT = "Cancel";

    private static final String IM_TAB_LABEL = "im.tab.label";

    private static final String IM_ADHOC_TAB_LABEL = "im.tab.adhoc.label";

    private static final String IM_PAGER_BTN_LBL = "im.rbtn.pager.label";

    private static final String IM_SMS_BTN_LBL = "im.rbtn.sms.label";

    private static final String NETWORK_LABEL = "im.cbx.network.label";

    private static final String DESTINATION_LABEL = "im.cbx.destination.label";

    private static final String NUMBER_LABEL = "im.txt.number.label";

    private static final String DEST_LABEL = "im.dest.label";

    private static final String TEXT_LABEL = "im.text.label";

    private static final int IM_MESSAGE_LEN = 120;

    private static final int IM_P_NUM_LEN = 20;

    private static final GridBagConstraints defaultGridBag = new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);

    private JButton sendButton = null;

    private JComboBox cbxDestination = null;

    private JTextField txtNumber = null;

    private IMTextArea messageTextArea = null;

    private MessagingControllerBeanBusinessDelegate messagingControllerBeanBusinessDelegate;

    public IMExternalSenderHelper() {
        super(new Frame(), IMResourceHelper.getResourceString(IM_TITLE), false, XDialog.OKCANCEL, XDialog.DEFAULTOK);
    }

    /**
     * Constructs a JPanel for External (ad-hoc) messaging. The panel contains a
     * drop-down for the destination, a text field to enter the number and a
     * text area for the message. The message is limited to 120 characters.
     * 
     * @param startingContextName
     *            The prefix for the JMS store
     * @return The formatted JPanel
     * @throws javax.jms.JMSException
     * @throws javax.naming.NamingException
     */
    public JPanel makeSendExternalPanel(String startingContextName, XDialog parent) throws javax.jms.JMSException,
            javax.naming.NamingException {
        log.debug("MESSAGING***: entering makeSendExternalPanel");

        GridBagConstraints gbc = (GridBagConstraints) this.defaultGridBag.clone();
        GridBagConstraints gbcc = (GridBagConstraints) this.defaultGridBag.clone();
        Border border = BorderFactory.createLineBorder(Color.black);
        sendButton = new JButton(IMResourceHelper.getResourceString(IM_BTN_SEND_LBL));
        sendButton.setEnabled(false);

        JLabel destLabel = new JLabel(IMResourceHelper.getResourceString(DEST_LABEL));
        destLabel.setMinimumSize(new Dimension(200, 17));
        JLabel textLabel = new JLabel(IMResourceHelper.getResourceString(TEXT_LABEL));
        textLabel.setMinimumSize(new Dimension(200, 17));

        JPanel adhocDetailPanel = getAdHocDetailPanel((GridBagConstraints) gbc.clone());
        adhocDetailPanel.setMinimumSize(new Dimension(250, 40));
        adhocDetailPanel.setPreferredSize(new Dimension(300, 200));

        JPanel sendPanel = new JPanel(new GridBagLayout(), true);

        messageTextArea = new IMTextArea(10, 10);
        messageTextArea.setBorder(border);
        messageTextArea.setPreferredSize(new Dimension(200, 100));
        messageTextArea.setMaximumSize(new Dimension(300, 370));
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);

        sendButton.addActionListener(new IMExternalSendMessageActionListener(messageTextArea, cbxDestination,
                txtNumber, parent));

        sendPanel.add(destLabel, gbcc.clone());

        gbcc.gridx++;

        sendPanel.add(textLabel, gbcc.clone());

        gbcc.gridx = 0;
        gbcc.gridy++;
        gbcc.gridheight = 2;
        gbcc.gridwidth = 1;
        gbcc.weightx = 0.0;
        gbcc.weighty = 0.0;

        sendPanel.add(adhocDetailPanel, gbcc.clone());

        gbcc.gridx++;
        gbcc.gridheight = 1;
        gbcc.gridwidth = 1;
        gbcc.weightx = 1.0;
        gbcc.weighty = 1.0;
        gbcc.fill = GridBagConstraints.BOTH;
        sendPanel.add(messageTextArea, gbcc.clone());

        gbcc.gridy++;
        gbcc.fill = GridBagConstraints.HORIZONTAL;
        gbcc.weightx = 0.0;
        gbcc.weighty = 0.0;
        sendPanel.add(sendButton, gbcc.clone());

        sendPanel.doLayout();

        log.debug("MESSAGING***: leaving makeSendExternalPanel");

        return sendPanel;
    }

    /**
     * Retrun the list of available destinations for external messages
     * 
     * @return a string array of destinations
     */
    private String[] getDestinationList() {
        log.debug("MESSAGING***: entering getDestinationList");
        String[] devices = null;

        devices = XhibitDelegateHelper.getMessagingDelegate().getDeviceTypes();

        if (devices == null) {
            log.debug("IMExternalSenderHelper: getDestinationList(): No devices returned");
            throw new CSConfigurationException("IMExternalSenderHelper: getDestinationList(): No devices returned");
        }

        // Sort list alphabetically
        Arrays.sort(devices);
        log.debug("MESSAGING***: leaving getDestinationList");
        return devices;
    }

    /**
     * Constructs the drop downs to add to the external message panel
     * 
     * @param gbc
     *            GridBagConstraints to position the componennts
     * @return The JPanel containing the componennts
     */
    private JPanel getAdHocDetailPanel(GridBagConstraints gbc) {
        log.debug("MESSAGING***: entering getAdHocDetailPanel");

        JPanel adHocDetailPanel = new JPanel(new GridBagLayout(), true);

        Dimension dim = new Dimension(100, XHIBITConstant.getLineHeight());

        cbxDestination = new JComboBox(getDestinationList());
        cbxDestination.setMinimumSize(dim);
        cbxDestination.setPreferredSize(dim);
        JLabel destinationLabel = new JLabel(IMResourceHelper.getResourceString(DESTINATION_LABEL));
        destinationLabel.setMinimumSize(dim);

        JLabel numberLabel = new JLabel(IMResourceHelper.getResourceString(NUMBER_LABEL));
        numberLabel.setMinimumSize(dim);
        txtNumber = new JTextField();
        txtNumber.setMinimumSize(dim);
        txtNumber.setPreferredSize(dim);
        txtNumber.addKeyListener(new IMNumericKeyListener());
        txtNumber.setDocument(new IMLimitedTextDocument(IM_P_NUM_LEN));
        txtNumber.getDocument().addDocumentListener(new IMDocumentListener(sendButton, true));

        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridy++;

        adHocDetailPanel.add(destinationLabel, gbc.clone());

        gbc.gridx++;

        adHocDetailPanel.add(cbxDestination, gbc.clone());

        gbc.gridy++;
        gbc.gridx = 0;

        adHocDetailPanel.add(numberLabel, gbc.clone());

        gbc.gridx++;

        adHocDetailPanel.add(txtNumber, gbc.clone());

        log.debug("MESSAGING***: leaving getAdHocDetailPanel");

        return adHocDetailPanel;
    }

    /**
     * Reset the text area when the dialog is redisplayed. If there has been a
     * problem creating the panel, e.g. the topic cannot be found in JMS, the
     * text fields will be null.
     */
    public void reset() {
        if (txtNumber != null) {
            txtNumber.setText("");
        }

        if (messageTextArea != null) {
            messageTextArea.setText("");
        }
    }
}