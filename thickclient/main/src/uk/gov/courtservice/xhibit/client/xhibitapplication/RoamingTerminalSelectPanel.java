package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.RoamingTerminalInterface;
import uk.gov.courtservice.xhibit.client.util.security.RoamingTerminalValue;
import uk.gov.courtservice.xhibit.client.widgetfactory.JComboBoxFactory;

/**
 * <p>
 * Title: Roaming Terminal Court Room selection panel
 * </p>
 * <p>
 * Description: Depending on the amount of roaming ability the user can select
 * the court, court site and court room that they wish to run XHIBIT in
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RoamingTerminalSelectPanel.java,v 1.1 2004/12/09 15:21:17
 *          sz0t7n Exp $
 */

public class RoamingTerminalSelectPanel extends XPanel {
    private static final Logger log = CSServices.getLogger(RoamingTerminalSelectPanel.class);

    private static final String COURT_PROP = "roam.courtid";

    private static final String COURT_SITE_PROP = "roam.courtSiteId";

    private static final String COURT_ROOM_PROP = "roam.courtRoomId";

    // Set these now and keep final, so if hot switching we know what
    // roaming priveledges they had before
    private static final boolean canRoamCourts = (XhibitSingleton.getInstance().getCourtId() == null);

    private static final boolean canRoamCourtSites = (XhibitSingleton.getInstance().getCourtSiteId() == null);

    private static final boolean canRoamCourtRooms = (XhibitSingleton.getInstance().getCourtRoomId() == null);

    private JLabel lblCourt = null;

    private JLabel lblCourtSite = null;

    private JLabel lblCourtRoom = null;

    private JComboBox cmbCourt = null;

    private JComboBox cmbCourtSite = null;

    private JComboBox cmbCourtRoom = null;

    private OkCancelPanel buttonPanel;

    private Dimension labelDim = new Dimension(100, 25);

    private Dimension comboDim = new Dimension(200, 25);

    private CourtStructureValue courtStructureValue = null;

    private XhbCourtBasicValue[] courtList = null;

    private Properties userProps;

    private boolean isInitialised = false;

    public RoamingTerminalSelectPanel(OkCancelPanel buttonPanel) throws CSRecoverableException {
        super();
        this.buttonPanel = buttonPanel;
        // Check if the user session is one that supports updating of roaming
        // attributes
        if (!(XhibitSingleton.getInstance().getUserSession() instanceof RoamingTerminalInterface)) {
            throw new CSUnrecoverableException(new Message("usersession.roaming.unsupported"),
                    "The user session stored in Xhibit Singleton does not support updating for roaming terminals. Unable to continue login");
        }
        userProps = Xhibit.getClientPropertyFile();
        stepInitialise();
        jbInit();
    }

    // Lifecycle methods

    public void stepInitialise() {
        Integer courtId = XhibitSingleton.getInstance().getCourtId();
        if (canRoamCourts) {
            courtList = XhibitDelegateHelper.getUserSessionDelegate().getAllCourts();
            Sorter.sort(courtList, new String[] { "displayName" }, Sorter.ASCENDING);
            populateCourt();
            preselectCourt();
            courtStructureValue = getCourtStructureValue(((XhbCourtBasicValue) getCourtCombo().getSelectedItem())
                    .getCourtId());
        } else {
            courtStructureValue = getCourtStructureValue(courtId);
            courtList = new XhbCourtBasicValue[] { courtStructureValue.getCourt() };
            populateCourt();
            preselectCourt();
        }
        populateCourtSites();
        preselectCourtSite();
        populateCourtRooms();
        preselectCourtRoom();
    }

    private void preselectCourt() {
        if (canRoamCourts && userProps.getProperty(COURT_PROP) != null) {
            try {
                preselectCourt(Integer.valueOf(userProps.getProperty(COURT_PROP)));
            } catch (NumberFormatException ex) {
                log.error("Non-numeric value stored in property file for court id \""
                        + userProps.getProperty(COURT_PROP) + "\"");
            }
        } else {
            getCourtCombo().setSelectedIndex(0);
        }
    }

    private void preselectCourtSite() {
        if (canRoamCourtSites && userProps.getProperty(COURT_SITE_PROP) != null) {
            try {
                preselectCourtSite(Integer.valueOf(userProps.getProperty(COURT_SITE_PROP)));
            } catch (NumberFormatException ex) {
                log.error("Non-numeric value stored in property file for court site id \""
                        + userProps.getProperty(COURT_SITE_PROP) + "\"");
            }
        } else {
            preselectCourtSite(XhibitSingleton.getInstance().getCourtSiteId());
        }
    }

    private void preselectCourtRoom() {
        if (userProps.getProperty(COURT_ROOM_PROP) != null) {
            try {
                preselectCourtRoom(Integer.valueOf(userProps.getProperty(COURT_ROOM_PROP)));
            } catch (NumberFormatException ex) {
                log.error("Non-numeric value stored in property file for court room id \""
                        + userProps.getProperty(COURT_ROOM_PROP) + "\"");
            }
        }
    }

    public void stepActivate() {
        stepUpdateViewState();
        buttonPanel.okButton.requestFocus();
        isInitialised = true;
    }

    public void stepUpdateViewState() {
        if (getCourtRoomCombo().isEnabled() && getCourtRoomCombo().getItemCount() > 0
                && getCourtRoomCombo().getSelectedIndex() >= 0) {
            buttonPanel.getOkAction().setEnabled(true);
            buttonPanel.okButton.requestFocus();
        } else {
            buttonPanel.getOkAction().setEnabled(false);
        }
    }

    public void stepValidate() {
        // Empty implementation
    }

    public void stepDeactivate() {
        // Empty implementation
    }

    public void stepDeinitialise(boolean update) throws UserCancelException, CSRecoverableException {
        if (update) {
            RoamingTerminalValue roamValue = new RoamingTerminalValue();
            XhbCourtBasicValue courtValue = (XhbCourtBasicValue) getCourtCombo().getSelectedItem();
            XhbCourtSiteBasicValue courtSiteValue = (XhbCourtSiteBasicValue) getCourtSiteCombo().getSelectedItem();
            XhbCourtRoomBasicValue courtRoomValue = (XhbCourtRoomBasicValue) getCourtRoomCombo().getSelectedItem();
            if (canRoamCourts) {
                roamValue.setCourtId(courtValue.getCourtId());
                roamValue.setCourtName(courtValue.getDisplayName());
            }
            if (canRoamCourtSites) {
                roamValue.setCourtSiteId(courtSiteValue.getCourtSiteId());
            }
            if (canRoamCourtRooms) {
                roamValue.setCourtRoomId(courtRoomValue.getCourtRoomId());
            }
            roamValue.setTerminalLocation(courtValue.getDisplayName(), courtSiteValue.getDisplayName(), courtRoomValue
                    .getDisplayNameNoSite());

            // Update the user session with the new information.
            // Note don't need to check before casting as already done in
            // constructor.
            ((RoamingTerminalInterface) XhibitSingleton.getInstance().getUserSession())
                    .updateRoamingTerminalProperties(roamValue);

            // Store in user properties for next login
            userProps.setProperty(COURT_PROP, String.valueOf(courtValue.getCourtId()));
            userProps.setProperty(COURT_SITE_PROP, String.valueOf(courtSiteValue.getCourtSiteId()));
            userProps.setProperty(COURT_ROOM_PROP, String.valueOf(courtRoomValue.getCourtRoomId()));
            Xhibit.storeClientPropertyFile(userProps);

        }
    }

    // Utility Methods

    private CourtStructureValue getCourtStructureValue(Integer courtId) {
        return XhibitDelegateHelper.getUserSessionDelegate().getCourtStructure(courtId);
    }

    private void populateCourt() {
        log.debug("populateCourt started");
        getCourtCombo().setModel(new DefaultComboBoxModel(courtList));
        getCourtCombo().setEnabled(canRoamCourts);
        // Disable site and room until a court has been selected
        // This will either be done by a preselect process or the user
        getCourtSiteCombo().setEnabled(false);
        getCourtRoomCombo().setEnabled(false);
    }

    private void populateCourtSites() {
        log.debug("populateCourtSites started");
        XhbCourtSiteBasicValue[] courtSites = courtStructureValue.getCourtSites();
        Sorter.sort(courtSites, new String[] { "courtSiteCode" }, Sorter.ASCENDING);
        getCourtSiteCombo().setModel(new DefaultComboBoxModel(courtSites));
        getCourtSiteCombo().setEnabled(canRoamCourtSites && getCourtSiteCombo().getModel().getSize() > 1);
        // Disable room until a court site has been selected
        // This will either be done by a preselect process or the user
        getCourtRoomCombo().setEnabled(false);
    }

    private void populateCourtRooms() {
        log.debug("populateCourtRooms started");
        XhbCourtRoomBasicValue[] courtRooms = courtStructureValue
                .getCourtRoomsForSite(((XhbCourtSiteBasicValue) getCourtSiteCombo().getSelectedItem()).getCourtSiteId());
        Sorter.sort(courtRooms, new String[] { "crestCourtRoomNo" }, Sorter.ASCENDING);
        getCourtRoomCombo().setModel(new DefaultComboBoxModel(courtRooms));
        getCourtRoomCombo().setEnabled(canRoamCourtRooms);
    }

    private void preselectCourt(Integer courtId) {
        if (getCourtCombo().getModel().getSize() > 0) {
            boolean selected = false;
            for (int i = 0; !selected && i < courtList.length; i++) {
                if (courtList[i].getCourtId().equals(courtId)) {
                    getCourtCombo().setSelectedItem(courtList[i]);
                    selected = true;
                }
            }
            if (!selected)
                getCourtCombo().setSelectedIndex(0);
        }
    }

    private void preselectCourtSite(Integer courtSiteId) {
        if (getCourtSiteCombo().getModel().getSize() > 0) {
            XhbCourtSiteBasicValue[] courtSites = courtStructureValue.getCourtSites();
            boolean selected = false;
            for (int i = 0; !selected && i < courtSites.length; i++) {
                if (courtSites[i].getCourtSiteId().equals(courtSiteId)) {
                    getCourtSiteCombo().setSelectedItem(courtSites[i]);
                    selected = true;
                }
            }
            if (!selected)
                getCourtSiteCombo().setSelectedIndex(0);
        }
    }

    private void preselectCourtRoom(Integer courtRoomId) {
        if (getCourtRoomCombo().getModel().getSize() > 0) {
            Integer courtSiteId = ((XhbCourtSiteBasicValue) getCourtSiteCombo().getSelectedItem()).getCourtSiteId();
            XhbCourtRoomBasicValue[] courtRooms = courtStructureValue.getCourtRoomsForSite(courtSiteId);
            boolean selected = false;
            for (int i = 0; !selected && i < courtRooms.length; i++) {
                if (courtRooms[i].getCourtRoomId().equals(courtRoomId)) {
                    getCourtRoomCombo().setSelectedItem(courtRooms[i]);
                    selected = true;
                }
            }
            if (!selected)
                getCourtRoomCombo().setSelectedIndex(0);
        }
    }

    // Accessor methods

    private JComboBox getCourtCombo() {
        if (cmbCourt == null) {
            cmbCourt = JComboBoxFactory.getComboBox();
            cmbCourt.setPreferredSize(comboDim);
            cmbCourt.addActionListener(new CourtActionListener(this));
            cmbCourt.setRenderer(new ComboRenderer());
        }
        return cmbCourt;
    }

    private JComboBox getCourtSiteCombo() {
        if (cmbCourtSite == null) {
            cmbCourtSite = JComboBoxFactory.getComboBox();
            cmbCourtSite.setPreferredSize(comboDim);
            cmbCourtSite.addActionListener(new CourtSiteActionListener(this));
            cmbCourtSite.setRenderer(new ComboRenderer());
        }
        return cmbCourtSite;
    }

    private JComboBox getCourtRoomCombo() {
        if (cmbCourtRoom == null) {
            cmbCourtRoom = JComboBoxFactory.getComboBox();
            cmbCourtRoom.setPreferredSize(comboDim);
            cmbCourtRoom.addActionListener(new CourtRoomActionListener(this));
            cmbCourtRoom.setRenderer(new ComboRenderer());
        }
        return cmbCourtRoom;
    }

    public JLabel getLblCourt() {
        if (lblCourt == null) {
            lblCourt = new JLabel();
            lblCourt.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, "lblCourt"));
            lblCourt.setPreferredSize(labelDim);
        }
        return lblCourt;
    }

    public JLabel getLblCourtSite() {
        if (lblCourtSite == null) {
            lblCourtSite = new JLabel();
            lblCourtSite.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                    "lblCourtSite"));
            lblCourtSite.setPreferredSize(labelDim);
        }
        return lblCourtSite;
    }

    public JLabel getLblCourtRoom() {
        if (lblCourtRoom == null) {
            lblCourtRoom = new JLabel();
            lblCourtRoom.setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                    "lblCourtRoom"));
            lblCourtRoom.setPreferredSize(labelDim);
        }
        return lblCourtRoom;
    }

    private void jbInit() {
        GridBagConstraints gbc;

        this.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(0, 1, 1, 3, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblCourt(), gbc);
        gbc = new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getCourtCombo(), gbc);

        gbc = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblCourtSite(), gbc);
        gbc = new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getCourtSiteCombo(), gbc);

        gbc = new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getLblCourtRoom(), gbc);
        gbc = new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(getCourtRoomCombo(), gbc);

        gbc = new GridBagConstraints(3, 1, 1, 3, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);

        gbc = new GridBagConstraints(0, 4, 4, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                XHIBITConstant.nonContainerInsets, 0, 0);
        this.add(XHIBITConstant.getSpacer(), gbc);
    }

    class CourtActionListener extends SynchXAction {
        RoamingTerminalSelectPanel panel;

        public CourtActionListener(RoamingTerminalSelectPanel panel) {
            this.panel = panel;
        }

        public void synchActionPerformed(ActionEvent ae) {
            if (panel.isInitialised && panel.getCourtCombo().getSelectedIndex() >= 0) {
                XhbCourtBasicValue court = (XhbCourtBasicValue) panel.getCourtCombo().getSelectedItem();
                panel.courtStructureValue = getCourtStructureValue(court.getCourtId());
            }
        }

        public void postSynchActionPerformed(ActionEvent ae) {
            if (panel.isInitialised && panel.getCourtCombo().getSelectedIndex() >= 0) {
                panel.populateCourtSites();
                panel.getCourtSiteCombo().setSelectedIndex(0);
            }
        }
    }

    class CourtSiteActionListener implements ActionListener {
        RoamingTerminalSelectPanel panel;

        public CourtSiteActionListener(RoamingTerminalSelectPanel panel) {
            this.panel = panel;
        }

        public void actionPerformed(ActionEvent ae) {
            if (panel.isInitialised && panel.getCourtSiteCombo().getSelectedIndex() >= 0) {
                panel.populateCourtRooms();
                panel.getCourtRoomCombo().setSelectedIndex(0);
            }
        }
    }

    class CourtRoomActionListener implements ActionListener {
        RoamingTerminalSelectPanel panel;

        public CourtRoomActionListener(RoamingTerminalSelectPanel panel) {
            this.panel = panel;
        }

        public void actionPerformed(ActionEvent ae) {
            if (panel.isInitialised) {
                panel.stepUpdateViewState();
            }
        }
    }

    class ComboRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String text = null;
            if (value instanceof XhbCourtBasicValue)
                text = ((XhbCourtBasicValue) value).getDisplayName();
            else if (value instanceof XhbCourtSiteBasicValue)
                text = ((XhbCourtSiteBasicValue) value).getDisplayName();
            else if (value instanceof XhbCourtRoomBasicValue)
                text = ((XhbCourtRoomBasicValue) value).getDisplayName();

            if (text != null)
                setText(text);
            return this;
        }
    }
}
