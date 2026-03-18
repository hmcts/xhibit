package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version $Id: MediumEventMLPanel.java,v 1.32 2006/06/05 12:31:10 bzjrnl Exp $
 */
public class MediumEventMLPanel extends CourtLogEventPanel {
    private static final Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());

    private final Vector pullDownList01 = new Vector();

    protected final MediumEventMLModel model;

    private Dimension cbDim;

    private JLabel optionCbLabel;

    private JComboBox optionCb;

    private int cbSize = 0;

    public MediumEventMLPanel(final XDialog parent, final MediumEventMLModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        double minCbSize = cbSize * 6.5;
        minCbSize = (minCbSize >= 260 ? minCbSize : 260);
        cbDim = new Dimension((int) minCbSize, 21);

        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
        this.add(getOptionCbLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getOptionCb(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 20));
    }

    public void stepInitialise() throws CSRecoverableException {
        Vector meTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", model
                .getSubSchema());
        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
                XhibitBundles.SimpleEvent, "select")));

        // Populate pullDownList01 from xsd
        for (int x = 0; x < meTypes.size(); x++) {
            String name = (String) meTypes.get(x);
            log.debug("name: " + name);
            String value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);

            pullDownList01.add(new PullDownListObject(x + 1, name, value));
        }

        cbSize = calculateMaxItemLength(pullDownList01, meTypes.size());
        log.debug("cbSize: " + cbSize);

        super.stepInitialise();
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        HashMap hashMap = (HashMap) propertyMap.get(model.getSchema());
        if (hashMap != null) {
            model.setSelectedItemCode(((String) hashMap.get(model.getSubSchema())));
            model.setSelectedIndex(findSelectedEntry(pullDownList01, model.getSelectedItemCode()));
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        if (model.isSelectionRequired() || getOptionCb().getSelectedIndex() > 0) {
            HashMap meMLOptionsType = new HashMap();
            meMLOptionsType.put(model.getSubSchema(), model.getSelectedItemCode());
            propertyMap.put(model.getSchema(), meMLOptionsType);
        }
    }

    protected boolean isMandatoryFieldsCompleted() {
        if (model.isSelectionRequired()) {
            return getOptionCb().getSelectedIndex() > 0;
        }

        return true;
    }

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * generally as a result of the user clicking the OK/Cancel buttons. It
     * constructs a CourtLogCRUDValue for each record to be added/updated and
     * calls the appropriate method on the business delegate.
     * 
     * @param update -
     *            true if the user clicked the OK button
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            /**
             * There may be many records to add depending on event type: - case
             * level always has 1 - defendant level has one for each selected
             * defendant, so store all CRUD values in an array and call the
             * delegate method that expects multiple records
             */
            // if (model.isInEditMode() || isCaseLevelEvent())
            if (model.isInEditMode() || model.getDefendantDisplayType() != CourtLogEventLevelPanel.DEFENDANT_LIST) {
                log.debug("\nIN EDIT MODE - 1\n");
                super.stepDeinitialise(update);
            } else {
                log.debug("\nNOT IN EDIT MODE - 2\n");
                super.getCourtLogEventLevelPanel().stepDeinitialise(update);

                final int arrayLength = model.getSelectedDefendants().length;
                final CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[arrayLength];

                for (int x = 0; x < arrayLength; x++) {
                    DefendantBasicValue dbv = (DefendantBasicValue) model.getSelectedDefendants()[x];

                    model.setDefendantId(dbv.getId());
                    model.setDefendantOnCaseId(getCourtLogEventLevelPanel().findDefOnCase(dbv.getId()).getId());
                    model.setDefendantName(PDHConstants.buildDefendantName(dbv));

                    crudArray[x] = createCRUDFromModel();
                }

                getCLCDelegate().newEntries(crudArray);
            }
        }
    }

    private JLabel getOptionCbLabel() {
        if (optionCbLabel == null) {
            optionCbLabel = new JLabel();
            optionCbLabel.setMinimumSize(labelDim);
            optionCbLabel.setPreferredSize(labelDim);
            optionCbLabel
                    .setText(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "MediumEventMLOptionLbl"));
        }
        return optionCbLabel;
    }

    private JComboBox getOptionCb() {
        if (optionCb == null) {
            optionCb = new JComboBox(pullDownList01);
            optionCb.setMinimumSize(cbDim);
            optionCb.setPreferredSize(cbDim);
            optionCb.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent,
                    "MediumEventMLOptionToolTip"));
            optionCb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return optionCb;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getOptionCb().setSelectedIndex(model.getSelectedIndex());
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        PullDownListObject plo = (PullDownListObject) pullDownList01.get(getOptionCb().getSelectedIndex());
        model.setSelectedItem(plo.toString());
        model.setSelectedIndex(plo.getId());
        model.setSelectedItemCode(plo.getCode());
    }

    private int calculateMaxItemLength(Vector pullDownList, int maxSize) {
        int maxItemLength = 0;
        for (int x = 0; x < maxSize; x++) {
            PullDownListObject plo = (PullDownListObject) pullDownList.get(x);
            maxItemLength = (plo.toString().length() > maxItemLength ? plo.toString().length() : maxItemLength);
        }
        return maxItemLength;
    }

    private int findSelectedEntry(Vector itemList, String code) {
        int returnCode = 0;

        for (int x = 0; x < itemList.size(); x++) {
            PullDownListObject pdlo = (PullDownListObject) itemList.get(x);

            if (pdlo.getCode().equalsIgnoreCase(code))
                returnCode = x;
        }

        return returnCode;
    }
}
