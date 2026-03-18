package uk.gov.courtservice.xhibit.client.publicdisplayconfig.editscreen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.renderer.CourtRoomRenderer;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.comparators.CourtRoomBasicValueComparator;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: EditScreenPanel.java,v 1.10 2006/06/05 12:32:07 bzjrnl Exp $
 */

public class EditScreenPanel extends XPanel {
    // private static final String DB_YES = "Y";
    private static final String LABEL_ROTATIONSET = "pd.label.rotationset";

    private static final String LABEL_LANGUAGE = "pd.label.language";

    private static final String LABEL_COURTROOMS = "pd.label.courtrooms";

    private static final String LABEL_UNASSIGNED = "pd.label.showunassigned";

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel lblScreen = new JLabel();

    private JLabel lblRotationSet = new JLabel();

    private JLabel lblLanguage = new JLabel();

    private JLabel lblCourtRooms = new JLabel();

    private JLabel txtLanguage = new JLabel();

    private JComboBox rotationSetCombo = null;

    private JList courtRoomsList = null;

    private final Integer _displayId;

    private DisplayConfiguration displayConfig;

    private XhbRotationSetBasicValue[] availableRotationSets;

    public EditScreenPanel(Integer displayId) {
        _displayId = displayId;
        stepInitialise();
        jbInit();
    }

    protected XhbCourtRoomBasicValue[] getCourtRooms() {
        return displayConfig.getCourtRoomBasicValues();
    }

    /**
     * Get the display configuration and rotations sets Sort the court rooms by
     * crestCourtRoomNo and rotation sets alphabetically.
     */
    public void stepInitialise() {
        displayConfig = XhibitDelegateHelper.getPDConfigurationDelegate().getDisplayConfiguration(_displayId);
        availableRotationSets = XhibitDelegateHelper.getPDConfigurationDelegate().getRotationSetsForCourt(
                PublicDisplayUtils.getCourtId());
        Sorter.sort(availableRotationSets, new String[] { PublicDisplayUtils.SORT_KEY_ROTATION_SET }, Sorter.ASCENDING);
        Arrays.sort(displayConfig.getCourtRoomBasicValues(), CourtRoomBasicValueComparator.getInstance());
        // Sorter.sort(displayConfig.getCourtRoomBasicValues(),
        // new String[]{PublicDisplayUtils.SORT_KEY_COURTROOM},
        // Sorter.ASCENDING);
    }

    /**
     * @return Returns the display configuration being changed.
     */
    public DisplayConfiguration getDisplayConfiguration() {
        return displayConfig;
    }

    /**
     * If the user has changed anything it attempts to save back to the
     * uk.gov.courtservice.xhibit.business.services.publicdisplay
     * 
     * @param parm1
     */
    public void stepDeinitialise(boolean save) throws CSRecoverableException {
        if (save) {
            if (displayConfig.isCourtRoomsChanged() || displayConfig.isRotationSetChanged()) {
                XhibitDelegateHelper.getPDConfigurationDelegate().updateDisplayConfiguration(displayConfig,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
        }
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        Dimension courtRoomDim = new Dimension(300, 100);
        lblScreen.setText(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAY
                + displayConfig.getDisplayBasicValue().getDescriptionCode()));
        lblRotationSet.setText(PublicDisplayUtils.getResource(LABEL_ROTATIONSET));
        lblLanguage.setText(PublicDisplayUtils.getResource(LABEL_LANGUAGE));
        lblCourtRooms.setText(PublicDisplayUtils.getResource(LABEL_COURTROOMS));
        txtLanguage.setText(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_LOCALE
                + displayConfig.getDisplayBasicValue().getLocale()));

        lblScreen.setFont(lblScreen.getFont().deriveFont(Font.BOLD));

        this.add(lblScreen, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(lblLanguage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblRotationSet, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblCourtRooms, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(txtLanguage, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getRotationSetCombo(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(PublicDisplayUtils.getDefaultScrollPane(getCourtRoomList(), courtRoomDim), new GridBagConstraints(1,
                3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets,
                0, 0));
        this.add(getUnassignedCheck(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(JButtonFactory.getButton(getChangeAction()), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

    }

    private JCheckBox unassignedCheck;

    private JCheckBox getUnassignedCheck() {
        if (unassignedCheck == null) {
            JCheckBox temp = new JCheckBox(PublicDisplayUtils.getResource(LABEL_UNASSIGNED), displayConfig
                    .getDisplayBasicValue().getShowUnassignedYn().equals(DisplayConfiguration.DB_YES));
            temp.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    boolean showUnassigned = (e.getStateChange() == ItemEvent.SELECTED);
                    displayConfig.setShowUnassigned(showUnassigned ? DisplayConfiguration.DB_YES
                            : DisplayConfiguration.DB_NO);
                }
            });
            // temp.addActionListener(new ActionListener()
            // {
            // public void actionPerformed(ActionEvent e)
            // {
            // boolean showUnassigned =
            // ((JCheckBox)e.getSource()).isSelected();
            // displayConfig.setShowUnassigned(showUnassigned?DisplayConfiguration.DB_YES:DisplayConfiguration.DB_NO);
            // }
            // });
            unassignedCheck = temp;
        }
        return unassignedCheck;
    }

    private JComboBox getRotationSetCombo() {
        if (rotationSetCombo == null) {
            rotationSetCombo = new JComboBox(availableRotationSets);
            rotationSetCombo.setRenderer(new RotationSetRenderer());
            int currentRS = displayConfig.getRotationSetId().intValue();
            for (int i = 0; i < availableRotationSets.length; i++) {
                if (availableRotationSets[i].getRotationSetId().intValue() == currentRS) {
                    rotationSetCombo.setSelectedIndex(i);
                    break;
                }
            }
            rotationSetCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayConfig.setRotationSetBasicValue((XhbRotationSetBasicValue) rotationSetCombo
                            .getSelectedItem());
                }
            });
        }
        return rotationSetCombo;
    }

    private JList getCourtRoomList() {
        if (courtRoomsList == null) {
            courtRoomsList = new JList(displayConfig.getCourtRoomBasicValues());
            courtRoomsList.setCellRenderer(new CourtRoomRenderer());
        }
        return courtRoomsList;
    }

    private void setCourtRooms(XhbCourtRoomBasicValue[] newCourtRooms) {
        displayConfig.setCourtRoomBasicValues(newCourtRooms);
        getCourtRoomList().setListData(newCourtRooms);
    }

    private XAction changeAction;

    private XAction getChangeAction() {
        if (changeAction == null) {
            changeAction = new ChangeAction(this);
        }
        return changeAction;
    }

    class ChangeAction extends XAction {
        private static final String BUNDLE = "btnChange";

        private EditScreenPanel _parent;

        public ChangeAction(EditScreenPanel parent) {
            populateFromBundle(BUNDLE);
            _parent = parent;
        }

        public void xActionPerformed(ActionEvent ae) {
            ChangeCourtRoomsDialog dialog = new ChangeCourtRoomsDialog((java.awt.Dialog) SwingUtilities
                    .getWindowAncestor(_parent), _parent.getCourtRooms());
            dialog.setVisible(true);
            if (dialog.isOkClicked()) {
                _parent.setCourtRooms(dialog.getCourtRoomList());
                // new XhbCourtRoomBasicValue[] {});
            }
        }
    }

    class RotationSetRenderer extends JLabel implements ListCellRenderer {
        public RotationSetRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setText(((XhbRotationSetBasicValue) value).getDescription());
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

    /*
     * private String buildCourtRoomString(XhbCourtRoomBasicValue[] courtrooms) {
     * Sorter.sort(courtrooms, new
     * String[]{PublicDisplayUtils.SORT_KEY_COURTROOM}, Sorter.ASCENDING);
     * StringBuffer rooms = new StringBuffer(); for (int i = 0; i <
     * courtrooms.length; i++) { rooms.append(courtrooms[i].getDisplayName());
     * if (i < courtrooms.length-1) rooms.append("\n"); } return
     * rooms.toString(); }
     */
    /**
     * Empty Implementation
     */
    public void stepDeactivate() {
    }

    /**
     * Empty Implementation
     */
    public void stepValidate() {
    }

    /**
     * Empty Implementation
     */
    public void stepUpdateViewState() {
    }

    /**
     * Empty Implementation
     */
    public void stepActivate() {
    }
}