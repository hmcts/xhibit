package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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
 * @version 1.1
 *          <P>
 *          1.1 17/04/2003 CO - Bug ID: 52481, added a NULL check on ButtonModel
 *          as the buttons may not be selected at all.
 *          </P>
 */
public class SevenFourteenDayOrderPanel extends CourtLogEventPanel {
    private final String sfdoSchema, sfdoType, sfdoDate;

    private final SevenFourteenDayOrderModel model;

    private JLabel orderDateLabel = null;

    private JLabel numberOfDaysLabel = null;

    private JPanel sevenFourteenRadioPanel = new JPanel();

    private ButtonGroup sevenFourteenRadioButtonGroup = null;

    private JRadioButton sevenRadio = null;

    private JRadioButton fourteenRadio = null;

    private JLabel eventNameLabel = null;

    private XDatePanel orderDatePanel;

    public SevenFourteenDayOrderPanel(XDialog parent, SevenFourteenDayOrderModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        // Set XSD schema fields
        sfdoSchema = ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders,
                "E40601_7_14_Day_Order_Options");
        sfdoType = ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "E40601_7_14_Type");
        sfdoDate = ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "E40601_7_14_Date");

        stepInitialise();
        jbInit();
        stepActivate();
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        HashMap hashMap = (HashMap) propertyMap.get(sfdoSchema);

        try {
            String dateString = (String) hashMap.get(sfdoDate);
            model.setOrderDate(XDateFormat.parse(dateString));
        } catch (ParseException ex) {
            // Ignore. Will be rectified when new date saved.
        }

        model.setSevenFourteenRadio(((String) hashMap.get(sfdoType)));
    }

    private void jbInit() {
        orderDatePanel = new XDatePanel(this); // Generic date panel

        sevenFourteenRadioPanel.setLayout(new GridBagLayout());
        this.add(getEventNameLabel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(5, 2, 25, 2), 0, 0));
        this.add(getNumberOfDaysLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSevenRadio(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFourteenRadio(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getOrderDateLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 0, 2), 0, 0));
        this.add(orderDatePanel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        getSevenFourteenRadioButtonGroup().add(getSevenRadio());
        getSevenFourteenRadioButtonGroup().add(getFourteenRadio());
    }

    private JLabel getEventNameLabel() {
        if (eventNameLabel == null) {
            eventNameLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders,
                    "eventName"));
        }

        return eventNameLabel;
    }

    private ButtonGroup getSevenFourteenRadioButtonGroup() {
        if (sevenFourteenRadioButtonGroup == null) {
            sevenFourteenRadioButtonGroup = new ButtonGroup();
        }

        return sevenFourteenRadioButtonGroup;
    }

    private JRadioButton getSevenRadio() {
        if (sevenRadio == null) {
            sevenRadio = new JRadioButton();
            sevenRadio.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders,
                    "tt7DayOrder"));
            sevenRadio.setActionCommand("E40601_7_Day_Order");
            sevenRadio.setMnemonic('7');
            sevenRadio.setText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "lbl7"));
            sevenRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sevenFourteenRadio_actionPerformed();
                }
            });
        }

        return sevenRadio;
    }

    private JRadioButton getFourteenRadio() {
        if (fourteenRadio == null) {
            fourteenRadio = new JRadioButton();
            fourteenRadio.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders,
                    "tt14DayOrder"));
            fourteenRadio.setActionCommand("E40601_14_Day_Order");
            fourteenRadio.setMnemonic('1');
            fourteenRadio.setText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "lbl14"));
            fourteenRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sevenFourteenRadio_actionPerformed();
                }
            });
        }

        return fourteenRadio;
    }

    void sevenFourteenRadio_actionPerformed() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, daysToAdd(getSevenFourteenRadioButtonGroup().getSelection().getActionCommand()));
        orderDatePanel.setDate(now);

        stepUpdateViewStateHandleExceptions();
    }

    private JLabel getOrderDateLabel() {
        if (orderDateLabel == null) {
            orderDateLabel = new JLabel();
            orderDateLabel.setText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "lblDate"));
        }

        return orderDateLabel;
    }

    private JLabel getNumberOfDaysLabel() {
        if (numberOfDaysLabel == null) {
            numberOfDaysLabel = new JLabel();
            numberOfDaysLabel
                    .setText(ResourceBundleHelper.getResource(XhibitBundles.SevenFourteenDayOrders, "lblDays"));
        }

        return numberOfDaysLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getSevenRadio().setSelected(is7DayOrder(model.getSevenFourteenRadio()));
            getFourteenRadio().setSelected(is14DayOrder(model.getSevenFourteenRadio()));

            orderDatePanel.setDate(model.getOrderDate());
        }
    }

    protected boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (getSevenFourteenRadioButtonGroup().getSelection() == null) {
            result = false;
        } else if (orderDatePanel.isMandatoryFieldsCompleted()) {
            // NoAction
        } else {
            result = false;
        }

        return result;
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        orderDatePanel.stepValidate();
        Calendar tomorrow = getLogAuditPanel().getDate();
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);
        tomorrow.set(Calendar.SECOND, 59);
        if (orderDatePanel.getDate().before(tomorrow)) {
            orderDatePanel.requestFocus();
            throw new CSValidationException("validation.date.afterlog", new String[] { orderDatePanel.getText() },
                    "Date is in the past");
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();
        model.setOrderDate(orderDatePanel.getDate());
        // CO - 52481
        // model.setSevenFourteenRadio( getSevenFourteenRadioButtonGroup(
        // ).getSelection( ).getActionCommand( ) );
        ButtonModel bm = getSevenFourteenRadioButtonGroup().getSelection();
        if (bm != null) {
            model.setSevenFourteenRadio(bm.getActionCommand());
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        // Specific fields
        HashMap sfdoOptionsType = new HashMap();
        sfdoOptionsType.put(sfdoType, model.getSevenFourteenRadio());
        sfdoOptionsType.put(sfdoDate, XDateFormat.format(model.getOrderDate(), XDateFormat.DATEFORMAT));

        propertyMap.put(sfdoSchema, sfdoOptionsType);
    }

    public JComponent getFirstEnterableComponent() {
        return getSevenRadio();
    }

    private int daysToAdd(String param) {
        int result = 0;

        if (is7DayOrder(param)) {
            result = 7;
        } else if (is14DayOrder(param)) {
            result = 14;
        }

        return result;
    }

    private boolean is7DayOrder(String param) {
        return "E40601_7_Day_Order".equalsIgnoreCase(param);
    }

    private boolean is14DayOrder(String param) {
        return "E40601_14_Day_Order".equalsIgnoreCase(param);
    }
}
