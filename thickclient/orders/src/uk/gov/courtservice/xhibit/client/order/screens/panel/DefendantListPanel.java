package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderComboBoxListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderDefendantWrapper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 DefendantListPanel
 * </p>
 * <p>
 * Description: Shows list of orders for a defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class DefendantListPanel extends AbstractOrdersPanel implements ActionListener

{
    private static final Logger log = CSServices.getLogger(DefendantListPanel.class);

    private static final String DEFENDANT_PANEL_TITLE = "DefendantPanelTitle";

    private static final String DEFENDANT_LABEL = "DefendantLabel";

    private static final String DROP_DOWN_MAX_ROWS = "orders.drop.down.max.rows";

    private static final int DEFAULT_DROP_DOWN_SELECTION = 0;

    private static final String DEFAULT_DROP_DOWN_MAX = "4";

    private static final String NO_DEFS_ERROR = "orders.defendant.empty";

    private JComboBox comboBox;

    private OrderComboBoxListener listener;

    /**
     * Constructor
     * 
     * @param odm
     *            the model
     */
    public DefendantListPanel(OrderInitialDataVO odm) throws CSRecoverableException {
        super(odm);

        initialisePanel();

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(DEFENDANT_PANEL_TITLE)));
    }

    /**
     * Initialise the Defendant panel
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();
        addComponents();
    }

    /**
     * Add components to the panel
     * 
     * @throws CSRecoverableException
     */
    private void addComponents() throws CSRecoverableException {
        add(getDefendantLabel(), getConstraints());
        ++getConstraints().gridx;
        add(getDefendantList(), getConstraints());
    }

    /**
     * Return the defendant field label
     * 
     * @return the label
     */
    private JLabel getDefendantLabel() {
        return new JLabel(ResourceHelper.getResourceString(DEFENDANT_LABEL));
    }

    /**
     * Returns a JComboBox containing a list of defendant names relating to the
     * case.
     * 
     * @return A JComboBox containing the defendants
     * @throws CSRecoverableException
     */
    public JComboBox getDefendantList() throws CSRecoverableException {
        log.debug("ORDERS***: entering getDefendantList");
        if (comboBox == null) {
            log.debug("ORDERS***: getDefendantList: creating Defendant List");
            comboBox = new JComboBox(this.model.getHelper().getDefendants());
            comboBox.setEditable(false);
            comboBox.setMaximumRowCount(4);
            if (comboBox.getItemCount() == 1) {
                resetDefendantList();

                // Des Johnston SCR 52556
                comboBox.setEditable(false);
            }
        } else {
            // Reset the list
            resetDefendantList();

            // Des Johnston SCR 52556
            comboBox.setEditable(false);// DJ
        }
        log.debug("ORDERS***: leaving getDefendantList");
        return comboBox;
    }

    /**
     * Reset the defendant combobox
     * 
     * @throws CSRecoverableException
     */
    private void resetDefendantList() throws CSRecoverableException {
        if (this.model.getDefendantOnCaseID() == null) {
            comboBox.removeAllItems();
            OrderDefendantWrapper[] defs = this.model.getHelper().getDefendants();
            if (defs == null || defs.length == 0) {
                throw new CSRecoverableException(NO_DEFS_ERROR, "No defendants could be retrieved for this case.");
            }
            Arrays.sort(defs, new DefendantComparator());
            for (int i = 0; i < defs.length; i++) {
                comboBox.addItem(defs[i]);
            }
            if (defs.length == 1) {
                this.model.setDefendantName(defs[0].toString());
            }
        }
    }

    /**
     * Return all the defendants for the case
     * 
     * @param xac
     *            the XhibitApplicationController
     * @return an array of wrapped defendants
     */
    private OrderDefendantWrapper[] getDefendantsForCase(XhibitApplicationController xac) {
        return this.model.getHelper().getDefendants();
    }

    /**
     * Add a listener to the combobox
     * 
     * @param cBL
     *            the listener
     * @throws CSRecoverableException
     */
    public void addListener(OrderComboBoxListener cBL) throws CSRecoverableException {
        this.listener = cBL;
        getDefendantList().addActionListener(cBL);
    }

    /**
     * Returns the listener from the combobox
     * 
     * @return
     */
    public OrderComboBoxListener getListener() {
        return listener;
    }

    /**
     * Generic action method
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        // NO implementation
    }

    /**
     * Sets the data on the panel from the model
     * 
     * @param odm
     * @throws CSRecoverableException
     */
    public void setPanelFromModel(OrderInitialDataVO odm) throws CSRecoverableException {
        try {
            getDefendantList().setSelectedItem(odm.getDefendantName());
        } catch (CSRecoverableException ex) {
            log.error("No defendants were found for this case.", ex);
            throw ex;
        }
    }

    /**
     * Sets the model
     * 
     * @param odm
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
        this.getListener().setModel(this.model);
    }

    /**
     * Update the model from the panel
     */
    public void updateModel() {
        // Check that the defendant name is not blank - can happen
        // when the screen is first loaded
        // NOT VALID for B cases
        if (!this.model.getCaseID().startsWith("B")) {
            if (null != this.model.getDefendantName() && !this.model.getDefendantName().trim().equals("")) {
                int id = this.model.getHelper().getDefendantOnCaseID(this.model.getDefendantName()).intValue();
                this.model.setDefendantOnCaseID(id);
            }
        }
    }

    /**
     * Framework method - update the model
     * 
     * @CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("Defendant Panel stepDeactivate");
        updateModel();
    }

    /**
     * 
     * <p>
     * Title: DefendantComparator
     * </p>
     * <p>
     * Description: Compares two OrderDefendantWrapper - used to correctly order
     * the defendant list drop down.
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
    private static class DefendantComparator implements Comparator {
        // set up comparator
        /**
         * Compare two objects
         * 
         * @param o1
         * @param o2
         * @return O if same, -1 if n1 < n2, 1 if n1 > n2
         */
        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof OrderDefendantWrapper) || !(o2 instanceof OrderDefendantWrapper)) {
                return 0;
            }
            return compare((OrderDefendantWrapper) o1, (OrderDefendantWrapper) o2);
        }

        /**
         * Compare method to compare two OrderDefendantWrapper objects to
         * display in the Defendant List drop down.
         * 
         * @param n1
         *            The first OrderDefendantWrapper to compare
         * @param n2
         *            The second OrderDefendantWrapper to compare
         * @return O if same, -1 if n1 < n2, 1 if n1 > n2
         */
        public int compare(OrderDefendantWrapper n1, OrderDefendantWrapper n2) {
            // Compare the Defendants
            int orderCodeResult = n1.toString().compareTo(n2.toString());
            return orderCodeResult;
        }

    }
}