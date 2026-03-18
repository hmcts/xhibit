package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.services.version.VersionControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.RoamingTerminalSelectPanel.ComboRenderer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class LoginPanel extends XPanel implements KeyListener {

    private JLabel lblUserName = null; // = new JLabel();

    private JLabel lblPassword = null; // = new JLabel();
    
    private JLabel lblCourt = null; // = new JLabel();

    private JTextField txtUserName = null; // = new JTextField();

    private JPasswordField txtPassword = null; // = new JTextField();
    
    private JComboBox cboCourt = null; // = new JComboBox();
  
    private OkCancelPanel buttonPanel;

    private boolean buttonClicked;

    private Dimension labelDim = new Dimension(100, 25);

    private Dimension textDim = new Dimension(200, 25);
    
    private Integer defaultCourtId = null;
    
    private String defaultCourtLocation = "";
    
    private Integer defaultCourtRoomId = null;
    
    private Integer defaultCourtSiteId = null;
    
    private String defaultRoomOrSite = "";

    public LoginPanel(OkCancelPanel buttonPanel) {
        super();
        this.buttonPanel = buttonPanel;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    public void setUserName(String uname) {
        getTxtUserName().setText(uname);
    }

    public String getUserName() {
        return getTxtUserName().getText();
    }

    public String getPassword() {
        return getTxtPassword().getText();
    }

    public boolean getButtonClicked() {
        return buttonClicked;
    }

    public void stepInitialise() {
    }

    public void stepActivate() {
        // Do Nothing
    }

    public void stepUpdateViewState() {
        if ((getUserName().length() > 0) && (getPassword().length() > 0)) {
            buttonPanel.okButton.setEnabled(true);
        } else {
            buttonPanel.okButton.setEnabled(false);
        }

    }

    public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException {
        // May need to validate that the username and password fit guidelines.
    }

    public void stepDeactivate() {
        // Nothing to do
    }

    public void stepDeinitialise(boolean update) {
        if (!update)
            System.exit(0);
        buttonClicked = update;
    }

    public JTextField getTxtUserName() {
        if (txtUserName == null) {
            txtUserName = new JTextField();
            txtUserName.setPreferredSize(textDim);
            txtUserName.addKeyListener(this);
        }
        return txtUserName;
    }

    public JTextField getTxtPassword() {
        if (txtPassword == null) {
            txtPassword = new JPasswordField();
            txtPassword.setPreferredSize(textDim);
            txtPassword.addKeyListener(this);
        }
        return txtPassword;
    }
    
    public JComboBox getCboCourt() {
        if (cboCourt == null) {
            //          1. Get all courts
            XhbCourtBasicValue[] courtList = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().getAllCourts();
            
            // Sort the list for display
            Sorter.sort(courtList, new String[] { "courtName" }, Sorter.ASCENDING);
            cboCourt = new JComboBox();//courtModel);
            cboCourt.setPreferredSize(textDim);
            cboCourt.setRenderer(new ComboRenderer());
            cboCourt.addKeyListener(this);
            cboCourt.setModel(new DefaultComboBoxModel(courtList));
            
            
//          2. select row from XHB_TERMINAL_DEFAULT using terminal name
            String terminalName = getTerminalID();
            // Note if there is no entry in XHB_TERMINAL_DEFAULT then 'null' will be returned so that will have to be handled properly here!
            XhbTerminalDefaultBasicValue[] terminalDefault = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().getTerminalDefaultByName(terminalName);
            // How many results have we got back?
            if (terminalDefault.length != 1) {
                // Error - is this fatal?
            }
            defaultCourtId = terminalDefault[0].getCourtId();
            defaultCourtLocation = terminalDefault[0].getLocation();
            defaultCourtRoomId = terminalDefault[0].getCourtRoomId();
            defaultCourtSiteId = terminalDefault[0].getCourtSiteId();
            defaultRoomOrSite = terminalDefault[0].getCourtroomOrSite();
            boolean selected = false;
            for (int i = 0; !selected && i < courtList.length; i++) {
                if (courtList[i].getCourtId().equals(defaultCourtId)) {
                    cboCourt.setSelectedItem(courtList[i]);
                    selected = true;
                }
            }
            
        }
        return cboCourt;
    }

    public JLabel getLblUserName() {
        if (lblUserName == null) {
            lblUserName = new JLabel();
            lblUserName.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                    "lblUserName"));
            lblUserName.setPreferredSize(labelDim);
        }
        return lblUserName;

    }

    public JLabel getLblPassword() {
        if (lblPassword == null) {
            lblPassword = new JLabel();
            lblPassword.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                    "lblPassword"));
            lblPassword.setPreferredSize(labelDim);
        }
        return lblPassword;

    }
    
    public JLabel getLblCourt() {
        if (lblCourt == null) {
            lblCourt = new JLabel();
            lblCourt.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                    "lblLogonCourt"));
            lblCourt.setPreferredSize(labelDim);
        }
        return lblCourt;

    }

    private void jbInit() {
        GridBagConstraints gbc;

        this.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints(0, 0, 5, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(0, 1, 1, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblUserName(), gbc);
        gbc = new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getTxtUserName(), gbc);

        gbc = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblPassword(), gbc);
        gbc = new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getTxtPassword(), gbc);
        
        gbc = new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblCourt(), gbc);
        gbc = new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getCboCourt(), gbc);

        gbc = new GridBagConstraints(3, 1, 1, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(0, 4, 4, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);
    }
    
    class ComboRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String text = null;
            if (value instanceof XhbCourtBasicValue)
                text = ((XhbCourtBasicValue) value).getCourtName();
            

            if (text != null)
                setText(text);
            return this;
        }
    }
    
    public String getTerminalID() {
        String hostname = "";
        try {
            //log.debug("looking up terminal name");
            hostname = InetAddress.getLocalHost().getHostName();
            //log.debug("terminal name=" + hostname);
            return hostname.toLowerCase();
        } catch (UnknownHostException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException(e);
        }
    }
    
    public Integer getSelectedCourt() {
        XhbCourtBasicValue cBV = (XhbCourtBasicValue) getCboCourt().getSelectedItem();
        return cBV.getCourtId();
    }
    
    public String getSelectedCourtName() {
        XhbCourtBasicValue cBV = (XhbCourtBasicValue) getCboCourt().getSelectedItem();
        return cBV.getCourtName();
    }
    
    public Integer getHomeCourtID(){
        return defaultCourtId;
    }
    
    public String getHomeLocation(){
        return defaultCourtLocation;
    }
    
    public Integer getHomeCourtRoomID(){
        return defaultCourtRoomId;
    }
    
    public Integer getHomeCourtSiteID(){
        return defaultCourtSiteId;
    }
    
    public String getHomeRoomOrSite(){
        return defaultRoomOrSite;
    }

    public void keyReleased(KeyEvent ke) {
        stepUpdateViewState();
    }

    public void keyPressed(KeyEvent fe) {
    }

    public void keyTyped(KeyEvent fe) {
    }

}