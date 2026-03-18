package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.order.actions.OrderSummaryDefendantListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderSummaryTextField;

/**
 * <p>
 * Title: Xhibit2 Orders: OrderSummaryPanel
 * </p>
 * <p>
 * Description: Orders Wizard Summary Panel This panel is included in all of the
 * the Orders wizard and dialog screens (except for the actual Order Screen).
 * The following data is populated, either from the wizard panels, or from an
 * OrderDataModel: CaseID CourtID CourtName DefendantName OrderType
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

public class OrdersSummaryPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrdersSummaryPanel.class);

    private static final String CASE_ID_LABEL = "CaseIDLabel";

    private static final String COURT_ID_LABEL = "CourtCodeLabel";

    private static final String COURT_NAME_LABEL = "CourtNameLabel";

    private static final String DEFENDANT_NAME_LABEL = "DefendantLabel";

    private static final String ORDER_TYPE_LABEL = "OrderTypeLabel";

    private static final String SUMMARY_PANEL_TITLE = "SummaryTitle";

    private static final int TEXT_FIELD_EXTENSION = 60;

    private JTextField caseIDTxt;

    private JTextField courtIDTxt;

    private JTextField courtNmTxt;

    private JTextField deftNmTxt;

    private JTextField ordrTypeTxt;

    private OrderSummaryTextField deftNm;

    private OrderSummaryDefendantListener listener;

    /**
     * Constructor
     * 
     * @throws CSRecoverableException
     */
    public OrdersSummaryPanel() throws CSRecoverableException {
        super();
        initialisePanel();
    }

    /**
     * Framework method - initialises the panel
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("Summary Panel stepInitialise");
        initialisePanel();
    }

    /**
     * Framework method - update the model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("Summary Panel stepDeactivate");
        updateModel();
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        log.debug("Summary panel stepValidate");
    }

    /**
     * Framework method - set the defendant name and the order type from the
     * model
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("Summary Panel stepUpdateViewState");
        this.getDefendantNameData().setText(this.model.getDefendantName());
        this.getOrderTypeData().setText(this.model.getOrderType());
        log.debug("Model data: Def name: " + this.model.getDefendantName());
        log.debug("Model data: Ord Type: " + this.model.getOrderType());
    }

    /**
     * Framework method
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        log.debug("Summary Panel stepDeinitialise");
    }

    /**
     * Framework method - set the panel visible
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("Summary Panel stepActivate");
        setVisible(true);

    }

    /**
     * Initialise the components on the panel
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();

        setCaseDetails(getConstraints());

        setCourtDetails(getConstraints());

        setLocalConstraints(2, 0, 1.0, getConstraints().weighty);
        add(new JPanel(), getConstraints());

        setDefendantDetails(getConstraints());

        setOrderDetails(getConstraints());

        setLocalConstraints(5, getConstraints().gridy, 1.0, getConstraints().weighty);
        add(new JPanel(), getConstraints());

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(SUMMARY_PANEL_TITLE)));
    }

    /**
     * Set th ecase details
     * 
     * @param gbc
     *            the constraints
     */
    private void setCaseDetails(GridBagConstraints gbc) {
        setLocalConstraints(getConstraints().gridx, getConstraints().gridy, 1.0, getConstraints().weighty);

        add(getCaseIDLabel(), getConstraints());

        setLocalConstraints(++getConstraints().gridx, getConstraints().gridy, 0.1, getConstraints().weighty);

        add(getCaseIDData());
    }

    /**
     * Set the court details
     * 
     * @param gbc
     *            the constraints
     */
    private void setCourtDetails(GridBagConstraints gbc) {
        setLocalConstraints(0, 1, 1.0, getConstraints().weighty);
        add(getCourtCodeLabel(), getConstraints());

        setLocalConstraints(++getConstraints().gridx, getConstraints().gridy, 0.1, getConstraints().weighty);
        add(getCourtCodeData());

        setLocalConstraints(0, ++getConstraints().gridy, 1.0, getConstraints().weighty);
        add(getCourtNameLabel(), getConstraints());

        setLocalConstraints(++getConstraints().gridx, getConstraints().gridy, 0.1, getConstraints().weighty);
        add(getCourtNameData());
    }

    /**
     * Set the defendant details
     * 
     * @param gbc
     *            the constraints
     */
    private void setDefendantDetails(GridBagConstraints gbc) {
        setLocalConstraints(3, getConstraints().gridy, 1.0, getConstraints().weighty);
        add(getDefendantNameLabel(), getConstraints());

        setLocalConstraints(++getConstraints().gridx, getConstraints().gridy, getConstraints().weightx,
                getConstraints().weighty);
        deftNm = getDefendantNameData();
        listener = new OrderSummaryDefendantListener(this.model, deftNm);
        deftNm.addActionListener((ActionListener) listener);
        add(deftNm, TEXT_FIELD_WIDTH + TEXT_FIELD_EXTENSION);

    }

    /**
     * Set the order type details
     * 
     * @param gbc
     *            the constraints
     */
    private void setOrderDetails(GridBagConstraints gbc) {
        setLocalConstraints(3, 1, 1.0, getConstraints().weighty);
        add(getOrderTypeLabel(), getConstraints());

        setLocalConstraints(++getConstraints().gridx, getConstraints().gridy, getConstraints().weightx,
                getConstraints().weighty);
        JTextField ordType = getOrderTypeData();
        add(ordType, TEXT_FIELD_WIDTH + TEXT_FIELD_EXTENSION);
    }

    /**
     * Return the case id
     * 
     * @return the case id
     */
    private String getCaseID() {
        return this.model.getCaseID();
    }

    /**
     * Return the court code
     * 
     * @return the court code
     */
    private String getCourtCode() {
        return Integer.toString(this.model.getCrestCourtID());
    }

    /**
     * Return the court name
     * 
     * @return the court name
     */
    private String getCourtName() {
        return this.model.getCourtName();
    }

    /**
     * Return the defendant name
     * 
     * @return the defendant name
     */
    private String getDefendantName() {
        return this.model.getDefendantName();
    }

    /**
     * Return the order type
     * 
     * @return the order type
     */
    private String getOrderType() {
        return this.model.getOrderType();
    }

    /**
     * Return the case id label
     * 
     * @return the label
     */
    private JLabel getCaseIDLabel() {
        return new JLabel(getLabelText(CASE_ID_LABEL));
    }

    /**
     * Return the court code label
     * 
     * @return the label
     */
    private JLabel getCourtCodeLabel() {
        return new JLabel(getLabelText(COURT_ID_LABEL));
    }

    /**
     * Return the court name label
     * 
     * @return the label
     */
    private JLabel getCourtNameLabel() {
        return new JLabel(getLabelText(COURT_NAME_LABEL));
    }

    /**
     * Return the defendant name label
     * 
     * @return the label
     */
    private JLabel getDefendantNameLabel() {
        return new JLabel(getLabelText(DEFENDANT_NAME_LABEL));
    }

    /**
     * Return the order type label
     * 
     * @return the label
     */
    private JLabel getOrderTypeLabel() {
        return new JLabel(getLabelText(ORDER_TYPE_LABEL));
    }

    /**
     * Return the case id text field
     * 
     * @return the case id text field
     */
    public JTextField getCaseIDData() {
        if (caseIDTxt == null) {
            caseIDTxt = new JTextField(getCaseID());
        }
        return caseIDTxt;
    }

    /**
     * Return the court code text field
     * 
     * @return the court code text field
     */
    public JTextField getCourtCodeData() {
        if (courtIDTxt == null) {
            courtIDTxt = new JTextField(getCourtCode());
            courtIDTxt.setHorizontalAlignment(JTextField.LEFT);
        }
        return courtIDTxt;
    }

    /**
     * Return the court name text field
     * 
     * @return the court name text field
     */
    public JTextField getCourtNameData() {
        if (courtNmTxt == null) {
            courtNmTxt = new JTextField(getCourtName());
        }
        return courtNmTxt;
    }

    /**
     * Return the order summary text field
     * 
     * @return the order summary text field
     */
    public OrderSummaryTextField getDefendantNameData() {
        if (deftNmTxt == null) {
            deftNmTxt = new OrderSummaryTextField(getDefendantName());
        }
        return (OrderSummaryTextField) deftNmTxt;
    }

    /**
     * Return the order type text field
     * 
     * @return the order type text field
     */
    public OrderSummaryTextField getOrderTypeData() {
        if (ordrTypeTxt == null) {
            ordrTypeTxt = new OrderSummaryTextField(getOrderType());
        }
        return (OrderSummaryTextField) ordrTypeTxt;
    }

    /**
     * Return the label text
     * 
     * @param s
     *            the key of the resource to find
     * @return the label taxt
     */
    private String getLabelText(String s) {
        return ResourceHelper.getResourceString(s);
    }

    /**
     * Sets the panel from the model
     */
    private void setPanelFromModel() {
        // Empty implementation
    }

    /**
     * Upadte the model
     */
    public void updateModel() {
        log.debug("Set Defendant " + getDefendantNameData().getText());
        log.debug("Set Order " + getOrderTypeData().getText());
        this.model.setCaseID(getCaseIDData().getText());
        this.model.setCrestCourtID(Integer.parseInt(getCourtCodeData().getText()));
        this.model.setCourtName(getCourtNameData().getText());
        this.model.setDefendantName(getDefendantNameData().getText());
    }

    /**
     * Sets the reference to the model
     * 
     * @param odm
     *            the model
     */
    public void setOrderDataModel(OrderInitialDataVO model) {
        this.model = model;
        deftNm.removeActionListener(listener);
        listener = new OrderSummaryDefendantListener(this.model, deftNm);
        deftNm.addActionListener((ActionListener) listener);
    }

    /**
     * Set the panel from the model
     * 
     * @param odm
     *            the model
     */
    public void setPanelFromModel(OrderInitialDataVO odm) {
        getCaseIDData().setText(this.model.getCaseID());
        getCourtCodeData().setText(new Integer(this.model.getCrestCourtID()).toString());
        getCourtNameData().setText(this.model.getCourtName());
        getDefendantNameData().setText((this.model.getDefendantName()));
        
        if (odm.isMonetaryOrder()) {
            getOrderTypeData().setText("Monetary Order");
        } else if (odm.isD20Order()) {
            getOrderTypeData().setText("D20 Order");
        } else {
            getOrderTypeData().setText(this.model.getOrderType());
        }
        validate();
    }

    /**
     * Clear the input fields ready for more data to be input
     */
    public void resetInputs() {
        this.getDefendantNameData().setText("");
        if (!this.model.isMonetaryOrder() && !this.model.isD20Order()) {
            this.getOrderTypeData().setText("");
        }
    }
}