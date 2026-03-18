package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTableCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.tablemodel.VersionTableModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Splash panel to show version and copyrigth<br>
 * Also setStatus(String s) will display loading messages
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XhibitSplashPanel extends uk.gov.courtservice.xhibit.client.util.XPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(XhibitSplashPanel.class);

    private static final int screenWidth = 500;

    private JLabel lblUser = new JLabel();

    private JLabel lblMessage = new JLabel();

    private JLabel lblVersion = new JLabel();

    private JLabel lblVersionDate = new JLabel();

    private JLabel lblCopyright = new JLabel();

    private Image image;

    protected Dimension d = new Dimension(screenWidth, 330);

    private Insets userNameInsets = new Insets(4, 10, 15, 4);

    private Insets versionInsets = new Insets(4, 10, 0, 4);

    private Insets copyrightInsets = new Insets(4, 10, 10, 4);

    private Color foregroundColorWhite = Color.white;

    private Color foregroundColorBlack = Color.black;

    private XTable versionTable = null;

    private JPanel containingPanel;

    public XhibitSplashPanel() {
        containingPanel = new JPanel(new GridBagLayout()) {

            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g); // paint background

                g.drawImage(image, 0, 0, this);
            }

        };
        GridBagConstraints gbc;

        URL u = Xhibit.class.getClassLoader().getResource(XHIBITConstant.imageRoot + "xhibit_splash_his_majesty.jpg");

        MediaTracker mt = new MediaTracker(containingPanel);
        image = Toolkit.getDefaultToolkit().getImage(u);
        mt.addImage(image, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ie) {
            log.info(ie);
        }

        setLayout(new GridBagLayout());

        // Messages
        gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.rootContainerInsets, 0, 0);
        lblMessage.setForeground(foregroundColorWhite);
        containingPanel.add(lblMessage, gbc);

        // User
        if (XhibitSingleton.getInstance().getUserSession() != null
                && XhibitSingleton.getInstance().getUserSession().isLoggedIn()) {
            lblUser.setText("User: " + XhibitSingleton.getInstance().getUserSession().getUserName());
            lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD));
        }

        gbc = new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                userNameInsets, 0, 0);
        lblUser.setForeground(foregroundColorBlack);
        containingPanel.add(lblUser, gbc);

        // Version
        gbc = new GridBagConstraints(0, 3, 2, 1, 0.75, 0.0, GridBagConstraints.SOUTHWEST,
                GridBagConstraints.HORIZONTAL, versionInsets, 0, 0);
        lblVersion.setForeground(foregroundColorBlack);
        lblVersion.setText(getProperty("version"));
        containingPanel.add(lblVersion, gbc);

        // Date
        gbc = new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                versionInsets, 0, 0);
        lblVersionDate.setForeground(foregroundColorBlack);
        lblVersionDate.setText(getProperty("releaseDate"));
        containingPanel.add(lblVersionDate, gbc);

        // Copywrite
        gbc = new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                copyrightInsets, 0, 0);
        lblCopyright.setForeground(foregroundColorBlack);
        lblCopyright.setText(XHIBITConstant.getProperty(XhibitProperties.XhibitClientProject, "copyright"));
        containingPanel.add(lblCopyright, gbc);

        containingPanel.setPreferredSize(d);
        this.add(containingPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        setVisible(true);
    }

    private String getProperty(String propName) {
        String s = PropertyHelper.getProperty(XhibitProperties.XhibitClientProject, propName);
        return s;
    }

    public XhibitSplashPanel(XDialog d) {
        this();
        ((ApplyOkCancelPanel) d.getButtonPanel()).applyButton.setAction(new ShowMoreAction(d));
    }

    public void setStatus(String message) {
        lblMessage.setText(message);
    }

    private XTable getVersionTable() {
        if (versionTable == null) {
            VersionTableModel tModel = new VersionTableModel();
            XTable table = XTableFactory.getInstance().createDefaultTable(tModel);
            table.getColumnModel().getColumn(VersionTableModel.COL_UPDATE_DATE).setCellRenderer(
                    new XDateTableCellRenderer(table));
            table.initColumnSizes(tModel.getLongValues(), screenWidth);
            versionTable = table;
        }
        return versionTable;
    }

    JScrollPane spVersion = null;

    private JScrollPane getVersionScroll() {
        if (spVersion == null) {
            spVersion = new JScrollPane(getVersionTable(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            spVersion.setPreferredSize(new Dimension(screenWidth, XHIBITConstant.getLineHeight() * 6));
        }
        return spVersion;
    }

    private void addVersionTable() {
        removeVersionTable();
        add(getVersionScroll(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        XSwingUtilities.getWindowAncestor(this).pack();
    }

    private void removeVersionTable() {
        this.remove(getVersionScroll());
        XSwingUtilities.getWindowAncestor(this).pack();
    }

    // Empty life cycle methods
    public void stepInitialise() {
        // empty
    }

    public void stepActivate() {
        // empty
    }

    public void stepUpdateViewState() {
        // empty
    }

    public void stepValidate() {
        // empty
    }

    public void stepDeactivate() {
        // empty
    }

    public void stepDeinitialise(boolean boolean0) {
        // empty
    }

    class ShowMoreAction extends SynchXAction {

        private static final long serialVersionUID = 1L;

        XDialog dialog;

        VersionValue[] vv;

        boolean showMore = true;

        public ShowMoreAction(XDialog dialog) {
            setMore();
            this.dialog = dialog;
        }

        private void setMore() {
            showMore = true;
            setName("More >>");
            setMnemonicKey('M');
        }

        private void setLess() {
            showMore = false;
            setName("<< Less ");
            setMnemonicKey('L');
        }

        public void preSynchActionPerformed(ActionEvent ae) throws Exception {
            if (showMore) {
                // Show Table
                addVersionTable();
                // Allow OK to be clicked even though Midtier call still being
                // made
                dialog.unshield();
            } else {
                removeVersionTable();
            }
        }

        public void synchActionPerformed(ActionEvent ae) throws Exception {
            if (showMore) {
                // Get Data
                vv = XhibitDelegateHelper.getVersionDelegate().getVersions();       
                updateClientVersion(vv);
                VersionValue[] newVV = new VersionValue[vv.length + 1];
                System.arraycopy(vv, 0, newVV, 0, vv.length);
                newVV[newVV.length - 1] = getJavaVersion();
                Sorter.sort(newVV, new String[] { "displaySeq" }, Sorter.ASCENDING);
                vv = newVV;
            }
        }
        
        private boolean isJavaClient(VersionValue versionValue) {
            return 
                versionValue.getSchemaName() != null &&
                versionValue.getSchemaName().equals(
                    ComponentValue.THICK_CLIENT.getComponentName());
        }
        
        /*
         * The VersionValue records displayed in the version table are read
         * from the XHB_VERSION table in Xhibit.  This is ok except that the
         * JAVACLIENT version read from the table is not guaranteed to be
         * the same as the actual client version the user is using.  This 
         * method updates the VersionValue record to match the actual client
         * in use.
         */
        private void updateClientVersion(VersionValue[] versionValues) {
            for (VersionValue versionValue : versionValues) {
                if (isJavaClient(versionValue)) {

                    String version = getProperty("full_version");
                    if (version == null || version.equals("")) {
                        log.error("unable to find version");
                        return;
                    }

                    java.sql.Timestamp timestamp;
                    try {
                        String releaseDate = getProperty("releaseDate");
                        if (releaseDate == null || releaseDate.equals("")) {
                            log.error("unable to find releaseDate");
                            return;
                        }
                        // The ant tstamp task provided releaseDate is of the form
                        //    12 August 2009
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                        timestamp = new java.sql.Timestamp(dateFormat.parse(releaseDate).getTime());
                    } catch (java.text.ParseException e) {
                        log.error("unable to parse release date");
                        return;
                    }

                    versionValue.setSchemaVersion(version);
                    versionValue.setLastUpdateDate(timestamp);
                }
            }
        }

        private VersionValue getJavaVersion() {
            VersionValue javaVersion = new VersionValue();
            javaVersion.setDisplayName("Java Runtime");
            javaVersion.setDisplaySeq(0);
            javaVersion.setSchemaName("JRE");
            javaVersion.setSchemaVersion(System.getProperty("java.version"));
            return javaVersion;
        }

        public void postSynchActionPerformed(ActionEvent ae) throws Exception {
            if (showMore) {
                // Show data in table
                if (vv != null) {
                    ((XHIBITTableModelInterface) getVersionTable().getModel()).setData(vv);
                }
                setLess();
            } else {
                setMore();
            }
        }
    }
}
