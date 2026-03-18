package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderComboBoxListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

public class D20TypeListPanel extends AbstractOrdersPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = CSServices.getLogger(D20TypeListPanel.class);
	private static final String D20_TYPE_TITLE = "orders.panel.d20.title"; 
	private static final String D20_TYPE_LABEL = "orders.panel.d20.label"; 
	private static final String D20_TYPE_COMBO_HELP= "orders.panel.d20.combo.help";
	private static final int TOTAL_D20_TYPE = 2; 
    
    private static final String ORDER_D20_TYPE_0 = "orders.d20.dropdown4.value0"; 
    
    private static final String ORDER_D20_TYPE_1 = "orders.d20.dropdown4.value1";
    
	private JComboBox comboBox;
    private JCheckBox checkBox;
    private OrderInitialDataHelper helper = null;
    private OrderComboBoxListener listener;

    /**
     * Constructor
     * 
     * @param odm
     *            the model
     * @throws CSRecoverableException
     */
    public D20TypeListPanel(OrderInitialDataVO model) throws CSRecoverableException {
        super(model);

        helper = model.getHelper();
       
        initialisePanel();

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper.getResourceString(D20_TYPE_TITLE)));
    }
    
    public String getSelectedState()
    {
    	return comboBox.getSelectedIndex()==1? "N":"Y";
    }
    
    public int getSelectedIndex(String val)
    {
    	return val.equals("N")? 1: 0;
    }

    /**
     * Framework method - initialises the combo box
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("D20 Type Panel stepInitialise");
        // Only set the D20 Type if the defendant name has also
        // been set
        if (null != model.getDefendantName() && !model.getDefendantName().trim().equals("")) {
            this.getD20TypeList().setSelectedIndex(-1);
        }
    }

    /**
     * Framework method - update the model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("D20 Type Panel stepDeactivate");
        updateModel();
		getD20TypeList().setSelectedIndex(-1);
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        log.debug("D20 Type Panel stepValidate");
        
    }

    /**
     * Framework method - sets combo box
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("D20 Type Panel stepUpdateViewState");
        // If no D20 Type selected set to first on list, otherwise set to
        // selected
        getD20TypeList().setEnabled(true);
        if (model.getD20State().isEmpty() &&!(model.isACase())) {
            getD20TypeList().setSelectedIndex(0);
        } else if(!model.getD20State().isEmpty()&& !model.isACase()) {
            getD20TypeList().setSelectedItem(getSelectedIndex(model.getD20State()));
        } else if (model.isACase()) {
        	getD20TypeList().setSelectedIndex(1);
        	getD20TypeList().setEnabled(false);
        }
    }

    /**
     * Framework method
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        log.debug("D20 Type Panel stepDeinitialise");
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("D20 Type Panel stepActivate");
    }

    /**
     * Initialise the panel
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();
        addComponents();
    }

    /**
     * Add the compoinents to the panel
     */
    private void addComponents() {
        add(getOrderTypeLabel(), getConstraints());
        getConstraints().gridx++;
        add(getD20TypeList(), getConstraints());
        getConstraints().gridx++;
    }

    /**
     * Return the D20 Type label
     * 
     * @return the label
     */
    private JLabel getOrderTypeLabel() {
        return new JLabel(ResourceHelper.getResourceString(D20_TYPE_LABEL));
    }

    /**
     * Return the D20 Type combo box
     * 
     * @return the combo box
     */
    public JComboBox getD20TypeList() {
        if (comboBox == null) {
            
        	comboBox = new JComboBox(getD20TypeValues());
            comboBox.setSelectedIndex(-1);

            comboBox.setEditable(false);
            comboBox.setMaximumRowCount(4);
            comboBox.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, COMBO_BOX_HEIGHT));
        }
        return comboBox;
    }
    
    private String[] getD20TypeValues() {
    	String[] list = new String[TOTAL_D20_TYPE];
    	//list[0] = "";
        list[0] = ResourceHelper.getResourceString(ORDER_D20_TYPE_0);
        list[1] = ResourceHelper.getResourceString(ORDER_D20_TYPE_1);
        
        return list;
    }

    /**
     * Return the order check box
     * 
     * @return the check box
     */
    public JCheckBox getOrderCheckBox() {
        if (checkBox == null) {
            checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBox.setText(ResourceHelper.getResourceString(D20_TYPE_COMBO_HELP));
        }
        return checkBox;
    }

    /**
     * Add a listener to the panel
     * 
     * @param cBL
     *            the listener
     */
    public void addListener(OrderComboBoxListener cBL) {
        this.listener = cBL;
        getD20TypeList().addActionListener(cBL);
    }

    /**
     * Return the listener
     * 
     * @return the listener
     */
    public OrderComboBoxListener getListener() {
        return listener;
    }

    /**
     * Update the model with the selected Order Type
     */
    public void updateModel() {
        model.setD20State(getSelectedState());
    }

    
    /**
     * Set the panel data from the model
     * 
     * @param odm
     *            the model
     */
    public void setPanelFromModel(OrderInitialDataVO model) {
        getD20TypeList().setSelectedItem(-1);
    }

    /**
     * Set the reference to the model
     * 
     * @param odm
     *            the model
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
        this.getListener().setModel(model);
    }

}
