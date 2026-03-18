package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: Panel to show the Display Configuration information
 * </p>
 * <p>
 * Description: Lays out the display configuration information
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ScreenInformationPanel.java,v 1.8 2006/05/10 08:01:42 bzjrnl
 *          Exp $
 */
public class ScreenInformationPanel extends JPanel {
    private static final String LABEL_SCREEN = "pd.label.screen";

    private static final String LABEL_ROTATIONSET = "pd.label.rotationset";

    private static final String LABEL_LANGUAGE = "pd.label.language";

    private static final String LABEL_COURTROOM = "pd.label.courtroomsassigned";

    private static final String LABEL_UNASSIGNED = "pd.label.showunassigned";

    private DisplayConfiguration currentDisplayConfiguration = null;

    private JLabel lblScreen = new JLabel();

    private JLabel lblRotationSet = new JLabel();

    private JLabel lblLanguage = new JLabel();

    private JLabel lblCourtRooms = new JLabel();

    private JTextField txtScreen = new JTextField();

    private JTextField txtRotationSet = new JTextField();

    private JTextField txtLanguage = new JTextField();

    // private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtCourtRooms = JTextAreaFactory.getTextArea();

    private JCheckBox cbUnassigned;

    public ScreenInformationPanel() {
        super(new GridBagLayout());
        jbInit();
    }

    private void jbInit() {
        Dimension courtRoomDim = new Dimension(200, 75);
        lblScreen.setText(PublicDisplayUtils.getResource(LABEL_SCREEN));
        lblRotationSet.setText(PublicDisplayUtils.getResource(LABEL_ROTATIONSET));
        lblLanguage.setText(PublicDisplayUtils.getResource(LABEL_LANGUAGE));
        lblCourtRooms.setText(PublicDisplayUtils.getResource(LABEL_COURTROOM));
        JScrollPane courtRoomScrollPane = PublicDisplayUtils.getDefaultScrollPane(txtCourtRooms, courtRoomDim);
        txtScreen.setEditable(false);
        txtScreen.setBorder(courtRoomScrollPane.getBorder());
        txtRotationSet.setEditable(false);
        txtRotationSet.setBorder(courtRoomScrollPane.getBorder());
        txtLanguage.setEditable(false);
        txtLanguage.setBorder(courtRoomScrollPane.getBorder());
        txtCourtRooms.setEditable(false);
        this.add(lblScreen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblRotationSet, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblLanguage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblCourtRooms, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(txtScreen, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(txtRotationSet, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(txtLanguage, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(courtRoomScrollPane, new GridBagConstraints(1, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getUnassignedRadio(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new java.awt.Insets(0, 4, 0, 0), 0, 0));
        this.add(new JLabel(PublicDisplayUtils.getResource(LABEL_UNASSIGNED)), new GridBagConstraints(2, 4, 1, 1, 1.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 0, 0, 4), 0, 0));
    }

    /**
     * For the selected display Id, get the display configuration and display in
     * the relevant GUI components.
     */
    public void setNewDisplay(Integer displayId) {
        if (displayId != null) {
            currentDisplayConfiguration = XhibitDelegateHelper.getPDConfigurationDelegate().getDisplayConfiguration(
                    displayId);
            txtScreen.setText(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAY
                    + currentDisplayConfiguration.getDisplayBasicValue().getDescriptionCode()));
            txtRotationSet.setText(currentDisplayConfiguration.getRotationSetBasicValue().getDescription());
            txtLanguage.setText(PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_LOCALE
                    + currentDisplayConfiguration.getDisplayBasicValue().getLocale()));
            txtCourtRooms.setText(buildCourtRoomString(currentDisplayConfiguration.getCourtRoomBasicValues()));
            cbUnassigned.setSelected(currentDisplayConfiguration.getDisplayBasicValue().getShowUnassignedYn().equals(
                    DisplayConfiguration.DB_YES));
        } else {
            currentDisplayConfiguration = null;
            txtScreen.setText("");
            txtRotationSet.setText("");
            txtLanguage.setText("");
            txtCourtRooms.setText("");
            cbUnassigned.setSelected(false);
        }
    }

    private JCheckBox getUnassignedRadio() {
        if (cbUnassigned == null) {
            cbUnassigned = new JCheckBox();
            cbUnassigned.setSelected(false);
            cbUnassigned.setBorderPaintedFlat(true);
            cbUnassigned.setFocusPainted(false);
            cbUnassigned.setEnabled(false);
        }
        return cbUnassigned;
    }

    /**
     * @return The display configuration for the selected display
     */
    public DisplayConfiguration getCurrentDisplayConfiguration() {
        return currentDisplayConfiguration;
    }

    /**
     * Sort the court rooms and build into a string separated by new lines so
     * they can be displayed in a text area.
     * 
     * @param courtrooms
     * @return
     */
    private String buildCourtRoomString(XhbCourtRoomBasicValue[] courtrooms) {
        Sorter.sort(courtrooms, new String[] { PublicDisplayUtils.SORT_KEY_COURTROOM }, Sorter.ASCENDING);
        StringBuffer rooms = new StringBuffer();
        for (int i = 0; i < courtrooms.length; i++) {
            rooms.append(courtrooms[i].getDisplayName());
            if (i < courtrooms.length - 1)
                rooms.append("\n");
        }
        return rooms.toString();
    }
}