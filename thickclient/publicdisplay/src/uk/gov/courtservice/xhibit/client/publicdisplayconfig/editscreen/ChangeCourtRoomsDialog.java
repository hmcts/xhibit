package uk.gov.courtservice.xhibit.client.publicdisplayconfig.editscreen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Comparator;

import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.renderer.CourtRoomRenderer;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.models.SortableListModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.SelectorPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.comparators.CourtRoomBasicValueComparator;

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
 * @version $Id: ChangeCourtRoomsDialog.java,v 1.8 2006/05/10 08:01:42 bzjrnl
 *          Exp $
 */
public class ChangeCourtRoomsDialog extends XDialog {
    private static final String TITLE = "pd.title.assigncourtrooms";

    private SelectorPanel selector = null;

    public ChangeCourtRoomsDialog(java.awt.Dialog parent, XhbCourtRoomBasicValue[] currentList) {
        super(parent, PublicDisplayUtils.getResource(TITLE), true);
        Comparator courtRoomComparator = CourtRoomBasicValueComparator.getInstance();
        SortableListModel availableList = new SortableListModel(courtRoomComparator);
        SortableListModel selectedList = new SortableListModel(courtRoomComparator);

        XhbCourtRoomBasicValue[] allCourtRooms = XhibitDelegateHelper.getPDConfigurationDelegate()
                .getCourtRoomsForCourt(PublicDisplayUtils.getCourtId());

        // add court rooms to available list
        for (int i = 0; i < allCourtRooms.length; i++) {
            if (!isCourtRoomSelected(allCourtRooms[i], currentList)) {
                availableList.addElement(allCourtRooms[i]);
            }
        }

        // add court rooms to selected list
        for (int i = 0; i < currentList.length; i++) {
            selectedList.addElement(currentList[i]);
        }

        SelectorHostPanel panel = new SelectorHostPanel(PublicDisplayUtils.getResource(TITLE));

        selector = new SelectorPanel(panel, availableList, selectedList);
        selector.setAllListRenderer(new CourtRoomRenderer());
        selector.setTargetListRenderer(new CourtRoomRenderer());
        panel.addSelector(selector);

        addBodyPanel(panel);
        selector.setButtonEnable();
        pack();
    }

    private boolean isCourtRoomSelected(XhbCourtRoomBasicValue room, XhbCourtRoomBasicValue[] selectedRooms) {
        for (int i = 0; i < selectedRooms.length; i++) {
            if (room.getPrimaryKey().equals(selectedRooms[i].getPrimaryKey()))
                return true;
        }
        return false;
    }

    public XhbCourtRoomBasicValue[] getCourtRoomList() {
        int selectedSize = selector.getTargetListModel().getSize();
        XhbCourtRoomBasicValue[] courtRooms = new XhbCourtRoomBasicValue[selectedSize];

        Object[] o = new Object[selectedSize];
        o = selector.getTargetListModel().toArray();
        for (int i = 0; i < o.length; i++) {
            courtRooms[i] = (XhbCourtRoomBasicValue) o[i];
        }

        return courtRooms;
    }

    class SelectorHostPanel extends XPanel {
        public SelectorHostPanel(String title) {
            this.setLayout(new GridBagLayout());
            this.add(new JLabel(title), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        }

        public void addSelector(SelectorPanel sp) {
            this.add(sp, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        }

        public void stepInitialise() {
        }

        public void stepDeinitialise(boolean save) {
        }

        public void stepValidate() {
        }

        public void stepActivate() {
        }

        public void stepUpdateViewState() {
            // if necessary add OK button enabling here
            ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(
                    selector.getTargetList().getModel().getSize() > 0);
        }

        public void stepDeactivate() {
        }
    }
}