package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxAgent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CrownCourtOption. A editable combobox component that displays Crown
 * court names.
 * </p>
 * <p>
 * Description: A ComboBox component used to display a list of court names. This
 * class implements ChangeListener to detect changes to the selected court
 * value, and updates the underlying order data with the newly selected court
 * name. This class is instantiated in class MultipleCourtListPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class CrownCourtOption extends CourtOption implements ItemListener {
    private static final String PARENTHESIS_OPEN = " (";

    private static final String PARENTHESIS_CLOSE = ")";

    /**
     * Restores the list of court names to the original list.
     * 
     * @param list
     *            Contains the court list.
     * @param boxCb
     *            Creates a JComboBox component.
     * @param label
     *            Creates a JLabel component.
     * @param agent
     *            the comboboxagent
     * @param ref
     *            the xpath reference
     * @param helper
     *            the helper
     */
    public CrownCourtOption(String[] list, JComboBox boxCb, String label, ComboBoxAgent agent, String ref,
            OrderComponentHelper helper) {
        super(list, boxCb, label, agent, ref, helper);
    }

    /**
     * Restores the list of Crown court names. For Crown Courts, try to default
     * the list to match the court of the current user. If there is no match,
     * default to first in the list.
     * 
     * @param e
     *            Invoked when the target of the listener has changed its state.
     */
    public void itemStateChanged(ItemEvent e) {
        boolean selected = ((JRadioButton) e.getSource()).isSelected();
        if (selected) {
            getAgent().setContents(getList());
            getUtil().restoreNames(getBoxCb(), getList());
            try {
                CourtBasicValue courtBV = XhibitSingleton.getInstance().getCourtBasicValue();
                if (courtBV != null) {
                    String court = courtBV.getCourtName() + PARENTHESIS_OPEN + courtBV.getCrestCourtId()
                            + PARENTHESIS_CLOSE;
                    if (CourtListMidTier.getInstance().isCourtExists(courtBV.getCourtName())) {
                        getBoxCb().setSelectedItem(court);
                    }
                   
                }
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }

}
