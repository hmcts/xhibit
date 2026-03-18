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
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Xhibit2 OrderSignedPanel
 * </p>
 * <p>
 * Description: Captures order signed data
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

public class OrderSignedPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderSignedPanel.class);

    private static final String ORDER_SIGNED_PANEL_TITLE = "OrderSignedTitle";

    private static final String ORDER_SIGNED_DATE_LABEL = "OrderSignedDate";

    private static final String ORDER_SIGNED_TIME_LABEL = "OrderSignedTime";

    private static final String ORDER_SIGNED_SIGN_LABEL = "OrderSignedBy";

    private static final String ORDER_SIGN_SNAME_LABEL = "order.signby.surname";

    private static final String ORDER_SIGN_FNAME_LABEL = "order.signby.forename";

    private static final String ORDER_SIGN_INIT_LABEL = "order.signby.initial";

    private static final String ORDER_SIGN_TITLE_LABEL = "order.signby.title";

    // The linit is 35 as pecified in the Court Service PersonalDetailsTypes
    // schema
    private static final int ORDER_SIGNED_TEXT_LEN = 35;

    private XDatePanel datePanel = null;

    private static OrderReferenceDataHelper helper;

    private JComboBox signatoryList = null;

    private SignedDetailsVO signDetails = null;
    
    private SentDetailsVO sentDetails = null;

    private JTextField signSname = null;

    private JTextField signFname = null;

    private JTextField signTitle = null;

    private JTextField signInitial = null;

    private JTextField signatory = null;

    private boolean judgeToSign = false;

    /**
     * Indicates whether a jusge is to sign the order or not
     * 
     * @return true if a judge has to sign the order (BW)
     */
    public boolean isJudgeToSign() {
        return judgeToSign;
    }

    /**
     * Sets the indicator that the judge has to sign the order
     * 
     * @param judgeToSign
     *            true if the judge has to sign the ordre
     */
    public void setJudgeToSign(boolean judgeToSign) {
        this.judgeToSign = judgeToSign;
    }

    /**
     * Public constructor
     * 
     * @throws CSRecoverableException
     */
    public OrderSignedPanel() throws CSRecoverableException {
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
                .getResourceString(ORDER_SIGNED_PANEL_TITLE)));
    }

    private void addSignatory() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;

        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGNED_SIGN_LABEL)), getConstraints());
        getConstraints().gridx++;
        getConstraints().fill = GridBagConstraints.HORIZONTAL;
    }

    private void addDate() {
        getConstraints().gridx = 0;
        getConstraints().gridy = 0;
        getConstraints().anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGNED_DATE_LABEL)));

        getConstraints().gridx++;

        getConstraints().anchor = GridBagConstraints.WEST;
        add(getDatePanel(), getConstraints());
    }

    private void addSurname() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGN_SNAME_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignSurname(), getConstraints());
    }

    private void addForename() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGN_FNAME_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignForename(), getConstraints());
    }

    private void addInitial() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGN_INIT_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignInitial(), getConstraints());
    }

    private void addTitle() {
        getConstraints().gridx = 0;
        getConstraints().gridy++;
        add(new JLabel(ResourceHelper.getResourceString(ORDER_SIGN_TITLE_LABEL)), getConstraints());
        getConstraints().gridx++;

        add(getSignTitle(), getConstraints());
    }

    /**
     * Returns the field containing the signatory of the order
     * 
     * @return the JTextField containing the signatory
     */
    public JTextField getSignatory() {
        if (signatory == null) {
            signatory = new JTextField();
            signatory.setDocument(new LimitedTextDocument(ORDER_SIGNED_TEXT_LEN));
        }
        return signatory;
    }

    /**
     * Return the Surname
     * 
     * @return The field containg the surname
     */
    public JTextField getSignSurname() {
        if (signSname == null) {
            signSname = new JTextField();
            signSname.setDocument(new LimitedTextDocument(ORDER_SIGNED_TEXT_LEN));
        }
        return signSname;
    }

    /**
     * Return the Title
     * 
     * @return The field containg the title
     */
    public JTextField getSignTitle() {
        if (signTitle == null) {
            signTitle = new JTextField();
            signTitle.setDocument(new LimitedTextDocument(ORDER_SIGNED_TEXT_LEN));
        }
        return signTitle;
    }

    /**
     * Return the Forename
     * 
     * @return The field containg the forename
     */
    public JTextField getSignForename() {
        if (signFname == null) {
            signFname = new JTextField();
            signFname.setDocument(new LimitedTextDocument(ORDER_SIGNED_TEXT_LEN));
        }
        return signFname;
    }

    /**
     * Return the Initial
     * 
     * @return The field containg the initialsurname
     */
    public JTextField getSignInitial() {
        if (signInitial == null) {
            signInitial = new JTextField();
            signInitial.setDocument(new LimitedTextDocument(ORDER_SIGNED_TEXT_LEN));
        }
        return signInitial;
    }

    /**
     * @todo initialise dropdown to current user
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSignedPanel: stepInitialise()");
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
        XHIBITConstant.debug("OrderSignedPanel: stepDeactivate()");
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     * @throws
     *             uk.gov.courtservice.framework.services.validation.CSValidationException
     */
    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        XHIBITConstant.debug("OrderSignedPanel: stepValidate()");
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSignedPanel: stepUpdateViewState()");
    }

    /**
     * 
     * @param parm1
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSignedPanel: stepDeinitialise(" + parm1 + ")");
        if (signDetails != null) {
	        signDetails.setCancelledBy(!parm1);
	        signDetails.setSignedSurname(getSignSurname().getText());
	        signDetails.setSignedForename(getSignForename().getText());
	        // signDetails.setSignedInitial(getSignInitial().getText());
	        signDetails.setSignedTitle(getSignTitle().getText());
	        signDetails.setSignedDate(getDatePanel().getDateComponent().getText());
        } else {
        	sentDetails.setCancelledBy(!parm1);
	        sentDetails.setSentSurname(getSignSurname().getText());
	        sentDetails.setSentForename(getSignForename().getText());
	        // signDetails.setSignedInitial(getSignInitial().getText());
	        sentDetails.setSentTitle(getSignTitle().getText());
	        sentDetails.setSentDate(getDatePanel().getDateComponent().getText());
        }
        
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        XHIBITConstant.debug("OrderSignedPanel: stepActivate()");
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
     * Returns a JComboBox containing the list of available signatories
     * 
     * @return JComboBox
     */
    public JComboBox getSignatoryList() {
        if (signatoryList == null) {
            signatoryList = new JComboBox(getSignatoriesForCourt());
        }
        return signatoryList;
    }

    /**
     * Sets the signed details to the value object
     * 
     * @param sign
     *            The value object containing the data
     */
    public void setSignedDetails(SignedDetailsVO sign) {
        log.debug("$$$ setSignedDetails " + sign);
        signDetails = sign;
    }

    /**
     * Return the sign details value object
     * 
     * @return
     */
    private SignedDetailsVO getSignedDetails() {
        return signDetails;
    }

    /**
     * @todo retrieve reference data from middle tier Dummy method
     * @return
     */
    private String[] getSignatoriesForCourt() {
        Iterator iter = helper.getSignatoryList().iterator();
        String[] names = new String[helper.getSignatoryList().size()];
        for (int i = 0; iter.hasNext(); i++) {
            names[i] = (String) iter.next();

        }
        return names;
    }

	public void setSignedDetails(SentDetailsVO sentVO) {
		log.debug("$$$ setSentDetails " + sentVO);
        sentDetails = sentVO;
	}
}