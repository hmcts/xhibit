package uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset.utils.DisplayDocumentBasicValueComparator;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset.views.SelectedListTableView;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.renderer.DisplayDocumentRenderer;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.AbstractSelectorPanel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.SelectorPanel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.views.SelectorJListSortableView;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

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
 * @version $Id: AddEditRotationSetPanel.java,v 1.4 2006/05/10 08:01:42 bzjrnl
 *          Exp $
 */

public class AddEditRotationSetPanel extends XPanel {
    private static final String DEFAULT_NO = "N";

    private static final String LABEL_ROTATIONSET = "pd.label.rotationset";

    private static final String LABEL_AVAILABLELIST = "pd.label.availablelist";

    private static final Dimension nameDim = new Dimension(200, XHIBITConstant.getLineHeight());

    private final XDialog _parent;

    private XAction okAction;

    private Integer _rotationSetId;

    private RotationSetComplexValue rsComplexValue;

    private RotationSetDDComplexValue[] availableList;

    private JTextField rotationSetName;

    public AddEditRotationSetPanel(XDialog parent, Integer rotationSetId) throws CSRecoverableException {
        super(new GridBagLayout());
        _parent = parent;
        _rotationSetId = rotationSetId;
        stepInitialise();
        init();
    }

    /**
     * Returns the newly created or modified rotation set.
     * 
     * @return
     */
    public RotationSetComplexValue getRotationSet() {
        return rsComplexValue;
    }

    private void init() throws CSRecoverableException {
        // name field
        this.add(new JLabel(PublicDisplayUtils.getResource(LABEL_ROTATIONSET)), new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 10, 0));

        this.add(getRotationSetName(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // selector view
        this.add(getSelectorPanel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private String checkNull(String o) {
        return (o == null ? "" : o);
    }

    private JTextField getRotationSetName() {
        if (rotationSetName == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.limitedText(100) });
            rotationSetName = JTextFieldFactory.getTextField(doc);
            rotationSetName.setText(checkNull(rsComplexValue.getRotationSetBasicValue().getDescription()));
            rotationSetName.setMinimumSize(nameDim);
            rotationSetName.setPreferredSize(nameDim);
            doc.addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    stepUpdateViewState();
                }

                public void removeUpdate(DocumentEvent e) {
                    stepUpdateViewState();
                }

                public void changedUpdate(DocumentEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return rotationSetName;
    }

    private SelectorPanel selectorPanel;

    private SelectorPanel getSelectorPanel() throws CSRecoverableException {
        if (selectorPanel == null) {
            selectorPanel = new SelectorPanel(this, getAvailableListView(), getSelectedListView());
        }
        return selectorPanel;
    }

    private AbstractSelectorPanel availableView;

    private AbstractSelectorPanel selectedView;

    private AbstractSelectorPanel getAvailableListView() {
        if (availableView == null) {
            // Create the view
            Comparator comparator = DisplayDocumentBasicValueComparator.getInstance();
            ListCellRenderer renderer = new DisplayDocumentRenderer();
            String title = PublicDisplayUtils.getResource(LABEL_AVAILABLELIST);
            SelectorJListSortableView view = new SelectorJListSortableView(comparator, renderer, title);

            // Add data to the view
            for (int i = 0; i < availableList.length; i++) {
                view.addElement(availableList[i]);
            }

            availableView = view;
        }
        return availableView;
    }

    private AbstractSelectorPanel getSelectedListView() {
        if (selectedView == null) {
            selectedView = new SelectedListTableView(_rotationSetId, rsComplexValue.getRotationSetDDComplexValues());
        }
        return selectedView;
    }

    private boolean isEdit() {
        return _rotationSetId != null;
    }

    public void stepInitialise() throws CSRecoverableException {
        // Prepare RotationSet complex value
        if (isEdit()) {
            rsComplexValue = XhibitDelegateHelper.getPDConfigurationDelegate().getRotationSet(_rotationSetId);
        } else {
            rsComplexValue = new RotationSetComplexValue();
            rsComplexValue.setRotationSetBasicValue(new XhbRotationSetBasicValue());
            rsComplexValue.getRotationSetBasicValue().setCourtId(PublicDisplayUtils.getCourtId());
            rsComplexValue.getRotationSetBasicValue().setDefaultYn(DEFAULT_NO);
        }

        // Prepare data for available lists.
        XhbDisplayDocumentBasicValue[] lists = XhibitDelegateHelper.getPDConfigurationDelegate().getDisplayDocuments();
        RotationSetDDComplexValue[] selectedLists = rsComplexValue.getRotationSetDDComplexValues();
        ArrayList arrAvailableLists = new ArrayList();
        for (int i = 0; i < lists.length; i++) {
            if (!isDisplayDocumentSelected(lists[i], selectedLists)) {
                arrAvailableLists.add(createRotationSetDDComplexValue(lists[i]));
            }
        }
        availableList = (RotationSetDDComplexValue[]) arrAvailableLists
                .toArray(new RotationSetDDComplexValue[arrAvailableLists.size()]);
    }

    private RotationSetDDComplexValue createRotationSetDDComplexValue(XhbDisplayDocumentBasicValue ddBasic) {
        XhbRotationSetDdBasicValue rsBasic = new XhbRotationSetDdBasicValue();
        rsBasic.setDisplayDocumentId(ddBasic.getPrimaryKey());
        rsBasic.setOrdering(new Integer(1));
        rsBasic.setPageDelay(ddBasic.getDefaultPageDelay());
        rsBasic.setRotationSetId(_rotationSetId);

        RotationSetDDComplexValue complex = new RotationSetDDComplexValue(rsBasic, ddBasic);
        return complex;
    }

    private boolean isDisplayDocumentSelected(XhbDisplayDocumentBasicValue list,
            RotationSetDDComplexValue[] selectedLists) {
        for (int i = 0; i < selectedLists.length; i++) {
            if (list.getPrimaryKey().equals(selectedLists[i].getDisplayDocumentId()))
                return true;
        }
        return false;
    }

    public void stepActivate() {
        stepUpdateViewState();
    }

    public void stepDeinitialise(boolean save) throws CSRecoverableException {
        if (save) {
            if (isEdit()) {
                XhibitDelegateHelper.getPDConfigurationDelegate().setDisplayDocumentsForRotationSet(rsComplexValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } else {
                XhibitDelegateHelper.getPDConfigurationDelegate().createRotationSets(rsComplexValue);
            }
        }
    }

    public void stepDeactivate() {
        Object[] data = getSelectedListView().getData();
        RotationSetDDComplexValue[] newLists = new RotationSetDDComplexValue[data.length];

        System.arraycopy(data, 0, newLists, 0, data.length);
        rsComplexValue.setRotationSetDDComplexValues(newLists);

        rsComplexValue.getRotationSetBasicValue().setDescription(getRotationSetName().getText().trim());
    }

    public void stepValidate() {
    }

    public void stepUpdateViewState() {
        if (getRotationSetName().getText().trim().length() == 0 || getSelectedListView().getModelSize() == 0) {
            getOkAction().setEnabled(false);
        } else {
            getOkAction().setEnabled(true);
        }
    }

    private XAction getOkAction() {
        if (okAction == null) {
            okAction = ((OkCancelPanel) _parent.getButtonPanel()).getOkAction();
        }
        return okAction;
    }

}