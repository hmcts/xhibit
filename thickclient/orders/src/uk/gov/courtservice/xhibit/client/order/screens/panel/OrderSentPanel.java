package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.GridBagConstraints;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderReferenceDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SentDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Xhibit2 OrderSentPanel
 * </p>
 * <p>
 * Description: Captures order sented data
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

public class OrderSentPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderSentPanel.class);

    private static final String ORDER_SENT_PANEL_TITLE = "Details";

    private static final String ORDER_SENT_DATE_LABEL = "Order Sent Date";

    private static final String ORDER_SENT_TIME_LABEL = "Order Sent Time";

    private static final String ORDER_SENT_SENT_LABEL = "";

    private static final String ORDER_SENT_SNAME_LABEL = "order.sentby.surname";

    private static final String ORDER_SENT_FNAME_LABEL = "order.sentby.forename";

    private static final String ORDER_SENT_INIT_LABEL = "order.sentby.initial";

    private static final String ORDER_SENT_TITLE_LABEL = "order.sentby.title";

    // The linit is 35 as pecified in the Court Service PersonalDetailsTypes
    // schema
    private static final int ORDER_SENT_TEXT_LEN = 35;

    private XDatePanel datePanel = null;

    private static OrderReferenceDataHelper helper;

    private JComboBox sentToList = null;

    private SentDetailsVO sentDetails = null;

    private JTextField sentSname = null;

    private JTextField sentFname = null;

    private JTextField sentTitle = null;

    private JTextField sentInitial = null;

    private JTextField sentatory = null;

    private boolean judgeToSign = false;

    /**
     * Indicates whether a jusge is to sent the order or not
     * 
     * @return true if a judge has to sent the order (BW)
     */
    public boolean isJudgeToSign() {
        return judgeToSign;
    }

    /**
     * Sets the indicator that the judge has to sent the order
     * 
     * @param judgeToSign
     *            true if the judge has to sent the ordre
     */
    public void setJudgeToSign(boolean judgeToSign) {
        this.judgeToSign = judgeToSign;
    }

    /**
     * Public constructor
     * 
     * @throws CSRecoverableException
     */
    public OrderSentPanel() throws CSRecoverableException {
        super();
        helper = new OrderReferenceDataHelper();
        initialisePanel();
    }

    /**
     * Initialise the panel
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();

        addBorder();

        addDate();

        addSignatory();

        addSurname();

        addForename();

        addTitle();
    }

    private void addBorder() {
        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(ORDER_SENT_PANEL_TITLE)));
    }

    private void addSignatory() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;

        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_SENT_LABEL)), getConstraints());
        getConstraints().gridx++;
        getConstraints().fill = GridBagConstraints.HORIZONTAL;
    }

    private void addDate() {
        getConstraints().gridx = 0;
        getConstraints().gridy = 0;
        getConstraints().anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_DATE_LABEL)));

        getConstraints().gridx++;

        getConstraints().anchor = GridBagConstraints.WEST;
        add(getDatePanel(), getConstraints());
    }

    private void addSurname() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_SNAME_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignSurname(), getConstraints());
    }

    private void addForename() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_FNAME_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignForename(), getConstraints());
    }

    private void addInitial() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_INIT_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignInitial(), getConstraints());
    }

    private void addTitle() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SENT_TITLE_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignTitle(), getConstraints());
    }

    /**
     * Returns the field containing the sentatory of the order
     * 
     * @return the JTextField containing the sentatory
     */
    public JTextField getSignatory() {
        if (sentatory == null) {
            sentatory = new JTextField();
            sentatory.setDocument(new LimitedTextDocument(ORDER_SENT_TEXT_LEN));
        }
        return sentatory;
    }

    /**
     * Return the Surname
     * 
     * @return The field containg the surname
     */
    public JTextField getSignSurname() {
        if (sentSname == null) {
            sentSname = new JTextField();
            sentSname.setDocument(new LimitedTextDocument(ORDER_SENT_TEXT_LEN));
        }
        return sentSname;
    }

    /**
     * Return the Title
     * 
     * @return The field containg the title
     */
    public JTextField getSignTitle() {
        if (sentTitle == null) {
            sentTitle = new JTextField();
            sentTitle.setDocument(new LimitedTextDocument(ORDER_SENT_TEXT_LEN));
        }
        return sentTitle;
    }

    /**
     * Return the Forename
     * 
     * @return The field containg the forename
     */
    public JTextField getSignForename() {
        if (sentFname == null) {
            sentFname = new JTextField();
            sentFname.setDocument(new LimitedTextDocument(ORDER_SENT_TEXT_LEN));
        }
        return sentFname;
    }

    /**
     * Return the Initial
     * 
     * @return The field containg the initialsurname
     */
    public JTextField getSignInitial() {
        if (sentInitial == null) {
            sentInitial = new JTextField();
            sentInitial.setDocument(new LimitedTextDocument(ORDER_SENT_TEXT_LEN));
        }
        return sentInitial;
    }

    /**
     * @todo initialise dropdown to current user
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSentPanel: stepInitialise()");
        this.getDatePanel().setDate(Calendar.getInstance());
    }

    private void setSignatory() {
        if (isJudgeToSign() == false) {
            getSignatory().setText("Default User");
        } else {
            getSignatory().setText("Default Judge");
        }

    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSentPanel: stepDeactivate()");
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     * @throws
     *             uk.gov.courtservice.framework.services.validation.CSValidationException
     */
    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        XHIBITConstant.debug("OrderSentPanel: stepValidate()");
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSentPanel: stepUpdateViewState()");
    }

    /**
     * 
     * @param parm1
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSentPanel: stepDeinitialise(" + parm1 + ")");
        sentDetails.setCancelledBy(!parm1);
        sentDetails.setSentSurname(getSignSurname().getText());
        sentDetails.setSentForename(getSignForename().getText());
        // sentDetails.setSentInitial(getSignInitial().getText());
        sentDetails.setSentTitle(getSignTitle().getText());

        sentDetails.setSentDate(getDatePanel().getDateComponent().getText());
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSentPanel: stepActivate()");
    }

    /**
     * Get the instance of XDatePanel to be added to the panel and set editable
     * to false
     * 
     * @return XDatePanel
     */
    public XDatePanel getDatePanel() {
        if (datePanel == null) {
            datePanel = new XDatePanel(this);
            datePanel.getDateComponent().setEditable(false);
        }
        return datePanel;
    }

    /**
     * Sets the sented details to the value object
     * 
     * @param sent
     *            The value object containing the data
     */
    public void setSentDetails(SentDetailsVO sent) {
        log.debug("$$$ setSentDetails " + sent);
        sentDetails = sent;
    }

    /**
     * Return the sent details value object
     * 
     * @return
     */
    private SentDetailsVO getSentDetails() {
        return sentDetails;
    }

}