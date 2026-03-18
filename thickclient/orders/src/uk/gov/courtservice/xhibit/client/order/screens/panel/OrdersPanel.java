package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 OrdersPanel
 * </p>
 * <p>
 * Description: Default order panel. Used as a container for other panels thyat
 * make up a screen.
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

public class OrdersPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrdersPanel.class);

    private OrdersSummaryPanel oSPanel;

    private XPanel detailPanel;

    /**
     * 
     */
    public OrdersPanel() throws CSRecoverableException {
        super();
        initialisePanel();
    }

    /**
     * 
     * @param odm
     */
    public OrdersPanel(OrderInitialDataVO model) throws CSRecoverableException {
        this();
        this.model = model;
    }

    /**
     * 
     * @param odm
     */
    public void setPanelFromModel(OrderInitialDataVO odm) {
        /** @todo Implement setPanelFromModel method */
    }

    /**
     * 
     */
    public void updateModel() {
        /** @todo Implement setPanelFromModel method */
    }

    /**
     * 
     * @param osp
     */
    public void add(OrdersSummaryPanel osp, GridBagConstraints constraints) {
        oSPanel = osp;
        super.add(osp, constraints);
    }

    /**
     * 
     * @return
     */
    public OrdersSummaryPanel getSummaryPanel() {
        return oSPanel;
    }

    /**
     * 
     * @param xp
     */
    public void add(XPanel xp, GridBagConstraints constraints) {
        if (xp instanceof OrdersSummaryPanel) {
            oSPanel = (OrdersSummaryPanel) xp;
        } else {
            detailPanel = xp;
        }
        super.add(xp, constraints);
    }

    /**
     * 
     * @return
     */
    public XPanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrdersPanel:stepInitialise");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepInitialise();
        }
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrdersPanel:stepDeactivate");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepDeactivate();
        }
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     * @throws
     *             uk.gov.courtservice.framework.services.validation.CSValidationException
     */
    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        log.debug("OrdersPanel:stepValidate");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepValidate();
        }
    }

    /**
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrdersPanel:stepUpdateViewState");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepUpdateViewState();
        }
    }

    /**
     * 
     * @param parm1
     *            Indicator as to whether this was issued from an OK(true) or
     *            Cancel(false). This appears to be the only way to determine
     *            the issuing button.
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrdersPanel:stepDeinitialise");
        Component[] comps = this.getComponents();
        for (int x = 0; x < comps.length; x++) {
            ((XPanel) comps[x]).stepDeinitialise(parm1);
        }
        if (parm1 == false) {
            int rc = JOptionPane.showConfirmDialog(this.getParent(), ResourceHelper
                    .getResourceString(AbstractOrdersPanel.CANCEL_CONFIRM), ResourceHelper
                    .getResourceString(AbstractOrdersPanel.CANCEL_TITLE), JOptionPane.YES_NO_OPTION);
            if (rc != 0) {
                throw new UserCancelException();
            }
        }
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrdersPanel:stepActivate");
    }

}