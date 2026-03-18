package uk.gov.courtservice.xhibit.client.viewinfopages;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.HTMLHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.viewinfopages.util.RadioButtonAction;
import uk.gov.courtservice.xhibit.client.viewinfopages.util.VIPDisplayDocumentComparator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfiguration;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationCourtRoom;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.VIPDisplayConfigurationDisplayDocument;

/**
 * <p>
 * Title: Panel showing available documents and court room selection
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: InformationPagesPanel.java,v 1.15 2006/05/10 12:17:21 bzjrnl
 *          Exp $
 */
public class InformationPagesPanel extends XPanel {
    private static final Logger log = CSServices.getLogger(InformationPagesPanel.class);

    private static final String TITLE = "pd.vip.label.choosepage";

    private static final String COURTROOM_LABEL = "pd.vip.label.selectcourtroom";

    private static final String URL_PATH = "/FileServlet?uri=";

    private static final String VIP_FLAG = "&displayType=browser";

    private static final String RB_KEY = "RB_KEY";

    private VIPDisplayConfiguration vipDisplayConfiguration;

    private VIPDisplayConfigurationDisplayDocument[] documents;

    private VIPDisplayConfigurationCourtRoom[] courtRooms;

    private JPanel multiCourtRoomPanel;

    private JPanel singleCourtRoomPanel;

    private JComboBox courtroomCombo;

    private int[] allCourtrooms;

    private ButtonGroup documentGroup = new ButtonGroup();

    private RadioButtonAction rbAction;

    private JLabel noDisplayDocumentsLabel;

    private JLabel noCourtRoomsLabel;

    private boolean enableShow = true;

    private ApplyOkCancelPanel buttonPanel;

    public InformationPagesPanel(ApplyOkCancelPanel buttonPanel) throws CSRecoverableException {
        super(new GridBagLayout());
        this.buttonPanel = buttonPanel;
        stepInitialise();
        init();
        stepUpdateViewState();
    }

    // GUI Components
    private void init() {
        int multiCount = 0;
        int singleCount = 0;

        // Check if court rooms exist. If they do not then display message and
        // disable Show button
        if (courtRooms.length <= 0) {
            this.add(getNoCourtRoomsLabel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            enableShow = false;
        } else {
            for (int i = 0; i < documents.length; i++) {
                if (documents[i].isMultipleCourt()) {
                    getMultiCourtRoomPanel().add(
                            getDocumentRadio(documents[i]),
                            new GridBagConstraints(0, i, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
                    multiCount++;

                } else {
                    getSingleCourtRoomPanel().add(
                            getDocumentRadio(documents[i]),
                            new GridBagConstraints(0, i, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                    GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
                    singleCount++;
                }
            }

            // Check if documents exist. If none exist then display message
            // and disable Show button
            getSingleCourtRoomPanel().add(
                    getCourtroomCombo(),
                    new GridBagConstraints(0, GridBagConstraints.RELATIVE, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

            if (multiCount == 0 && singleCount == 0) {
                this.add(getNoDisplayDocumentsLabel(),
                        new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
                enableShow = false;
            } else {
                if (multiCount > 0) {
                    this.add(getMultiCourtRoomPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets,
                            0, 0));
                }

                if (singleCount > 0) {
                    this.add(getSingleCourtRoomPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets,
                            0, 0));
                }
            }
        }
    }

    private JLabel getNoDisplayDocumentsLabel() {
        noDisplayDocumentsLabel = new JLabel(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_LABEL
                .concat("noDisplayDocuments")));
        noDisplayDocumentsLabel.setPreferredSize(new Dimension(300, 20));
        return noDisplayDocumentsLabel;
    }

    private JLabel getNoCourtRoomsLabel() {
        noCourtRoomsLabel = new JLabel(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_LABEL
                .concat("noCourtRooms")));
        noCourtRoomsLabel.setPreferredSize(new Dimension(300, 20));
        return noCourtRoomsLabel;
    }

    private JPanel getMultiCourtRoomPanel() {
        if (multiCourtRoomPanel == null) {
            multiCourtRoomPanel = new JPanel(new GridBagLayout());
            TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(
                    SystemColor.controlLtHighlight, SystemColor.controlShadow), PublicDisplayUtils
                    .getResource(PublicDisplayUtils.PRE_LABEL.concat("multicourtroompages")));
            multiCourtRoomPanel.setBorder(titledBorder);
        }
        return multiCourtRoomPanel;
    }

    private JPanel getSingleCourtRoomPanel() {
        if (singleCourtRoomPanel == null) {
            singleCourtRoomPanel = new JPanel(new GridBagLayout());
            TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(
                    SystemColor.controlLtHighlight, SystemColor.controlShadow), PublicDisplayUtils
                    .getResource(PublicDisplayUtils.PRE_LABEL.concat("singlecourtroompages")));
            singleCourtRoomPanel.setBorder(titledBorder);
        }
        return singleCourtRoomPanel;
    }

    private JRadioButton getDocumentRadio(VIPDisplayConfigurationDisplayDocument value) {

        String documentName = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT.concat(
                value.getDescriptionCode() == null ? "" : value.getDescriptionCode()).concat(
                value.getLanguage() == null ? "" : value.getLanguage()).concat(
                value.getCountry() == null ? "" : value.getCountry()));

        JRadioButton rb = new JRadioButton();
        rb.setPreferredSize(new Dimension(200, 20));
        rb.setMinimumSize(new Dimension(100, 20));
        rb.addActionListener(getRadioButtonAction());
        rb.setText(documentName);
        rb.setToolTipText(documentName);
        rb.putClientProperty(RB_KEY, value);
        documentGroup.add(rb);
        return rb;
    }

    private RadioButtonAction getRadioButtonAction() {
        if (rbAction == null) {
            rbAction = new RadioButtonAction(this);
        }
        return rbAction;
    }

    private JComboBox getCourtroomCombo() {
        if (courtroomCombo == null) {
            JComboBox temp = new JComboBox(courtRooms);
            temp.setRenderer(new CourtRoomCellRenderer());
            courtroomCombo = temp;
            courtroomCombo.setToolTipText(PublicDisplayUtils.getResource("courtRoomsListToolTip"));
        }
        return courtroomCombo;
    }

    // helpers
    private DisplayDocumentURI buildDisplayDocumentURI() {
        DisplayDocumentURI uri = null;
        JRadioButton selection = getSelectedRadio(documentGroup);
        if (selection != null) {
            VIPDisplayConfigurationDisplayDocument value = (VIPDisplayConfigurationDisplayDocument) selection
                    .getClientProperty(RB_KEY);
            if (value != null) {
                Locale locale = null;
                if (value.getLanguage() != null) {
                    if (value.getCountry() != null) {
                        locale = new Locale(value.getLanguage(), value.getCountry());
                    } else {
                        // Cannot have langauge set without country.
                        log.error("Either both language and country can be null or neither.  Country must be set.");
                    }
                } else {
                    locale = Locale.getDefault();
                }

                uri = new DisplayDocumentURI(locale, PublicDisplayUtils.getCourtId().intValue(), DisplayDocumentType
                        .getDisplayDocumentType(value.getDescriptionCode(), value.getLanguage(), value.getCountry()),
                        getCourtRoomArray(value));
            }
        }
        return uri;
    }

    /**
     * If the document type can only be for one courtroom return a single
     * element array with the court room id Otherwise get the array of all court
     * rooms including unassigned cases.
     * 
     * @param value
     * @return
     */
    private int[] getCourtRoomArray(VIPDisplayConfigurationDisplayDocument value) {
        if (value.isMultipleCourt()) {
            return getAllCourtrooms();
        } else {
            return new int[] { ((VIPDisplayConfigurationCourtRoom) getCourtroomCombo().getSelectedItem())
                    .getCourtRoomId().intValue() };
        }
    }

    private int[] getAllCourtrooms() {
        if (allCourtrooms == null) {
            // Set length depending on unassigned selection
            if (vipDisplayConfiguration.isUnassignedCases()) {
                allCourtrooms = new int[courtRooms.length + 1];
                allCourtrooms[courtRooms.length] = DisplayDocumentURI.UNASSIGNED;
            } else {
                allCourtrooms = new int[courtRooms.length];
            }

            for (int i = 0; i < courtRooms.length; i++) {
                allCourtrooms[i] = courtRooms[i].getCourtRoomId().intValue();
            }
        }
        return allCourtrooms;
    }

    /**
     * Return the selected radio button.
     * 
     * @param bGroup
     *            The group to examine
     * @return The selected radio, Null if none selected
     */
    public static JRadioButton getSelectedRadio(ButtonGroup bGroup) {
        if (bGroup.isSelected(bGroup.getSelection())) {
            JRadioButton selectedRB = null;
            Enumeration enumeration = bGroup.getElements();
            while (enumeration.hasMoreElements()) {
                JRadioButton item = (JRadioButton) enumeration.nextElement();
                if (item.isSelected()) {
                    selectedRB = item;
                    break;
                }
            }
            return selectedRB;
        }
        return null;
    }

    // Life cycle methods

    /**
     * Get display documents and court rooms
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        vipDisplayConfiguration = XhibitDelegateHelper.getPDConfigurationDelegate().getVIPDisplayConfiguration(
                PublicDisplayUtils.getCourtSiteId());
        // Get Display Document
        documents = vipDisplayConfiguration.getVIPDisplayConfigurationDisplayDocuments();
        // Get Court Rooms
        courtRooms = vipDisplayConfiguration.getVIPDisplayConfigurationCourtRooms();

        Arrays.sort(documents, VIPDisplayDocumentComparator.getInstance());
    }

    /**
     * Calls stepUpdateViewState
     */
    public void stepActivate() {
        if (documentGroup.getElements().hasMoreElements()) {
            ((JRadioButton) documentGroup.getElements().nextElement()).setSelected(true);
            selectCurrentCourtroom();
            stepUpdateViewState();
        }
    }

    /**
     * If the user is in a courtroom, preselect that one from the list
     */
    private void selectCurrentCourtroom() {
        if (XhibitSingleton.getInstance().getCourtRoomId() != null) {
            int currentCourtRoomId = XhibitSingleton.getInstance().getCourtRoomId().intValue();
            for (int i = 0; i < courtRooms.length; i++) {
                if (courtRooms[i].getCourtRoomId().intValue() == currentCourtRoomId) {
                    getCourtroomCombo().setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * If RB selected is a doc with multipleCourtYn=N Enable court drop down
     * else disable court dropdown
     */
    public void stepUpdateViewState() {
        JRadioButton rb = getSelectedRadio(documentGroup);

        if (rb != null) {
            VIPDisplayConfigurationDisplayDocument basic = (VIPDisplayConfigurationDisplayDocument) rb
                    .getClientProperty(RB_KEY);
            if (basic.isMultipleCourt()) {
                getCourtroomCombo().setEnabled(false);
            } else {
                getCourtroomCombo().setEnabled(true);
            }
        } else {
            getCourtroomCombo().setEnabled(false);
        }

        buttonPanel.applyButton.setEnabled(enableShow);
    }

    /**
     * Build DisplayDocumentURI Get uri string and append vip=true call html
     * helper to launch browser
     * 
     * @param execute
     *            whether or not to launch a browser
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean execute) throws CSRecoverableException {
        if (execute) {
            DisplayDocumentURI uri = buildDisplayDocumentURI();
            if (uri != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(getServer());
                buffer.append(uri.toString());
                buffer.append(VIP_FLAG);
                log.debug("URL=" + buffer.toString());

                String urlLaunchCommand = HTMLHelper.getWindowsURLLauncher(buffer.toString());
                log.debug("Launch command for information pages is: "+urlLaunchCommand);
                
                HTMLHelper.runCommand(urlLaunchCommand);
            }
        }
    }

    private String getServer() {
        String pubDispServer;
        if ((System.getProperty("pubDispServer") != null) && System.getProperty("pubDispServer").length() > 0) {
            pubDispServer = System.getProperty("pubDispServer");
        } else {
            pubDispServer = CSServices.getConfigServices().getProperty("pubDispServer");
        }
        return pubDispServer.concat(URL_PATH);
    }

    /**
     * Empty Implementation
     */
    public void stepValidate() {
    }

    /**
     * Empty Implementation
     */
    public void stepDeactivate() {
    }

    class CourtRoomCellRenderer extends JLabel implements ListCellRenderer {
        public CourtRoomCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (((VIPDisplayConfigurationCourtRoom) value).getCourtSiteShortName() == null) {
                setText(((VIPDisplayConfigurationCourtRoom) value).getCourtRoomDisplayName());
            } else {
                setText(((VIPDisplayConfigurationCourtRoom) value).getCourtSiteShortName() + "-"
                        + ((VIPDisplayConfigurationCourtRoom) value).getCourtRoomDisplayName());
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}
