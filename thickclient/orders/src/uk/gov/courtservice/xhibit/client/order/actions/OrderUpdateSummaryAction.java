package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersSummaryPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title:
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
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderUpdateSummaryAction extends XAction {
    private OrdersSummaryPanel ordSummPan;

    public OrderUpdateSummaryAction() {
    }

    public OrderUpdateSummaryAction(OrdersSummaryPanel osp, OrderInitialDataVO model) {
        setModel(model);
        ordSummPan = osp;
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XAction
         *       abstract method
         */
        ((OrderInitialDataVO) getModel()).setDefendantName(ordSummPan.getDefendantNameData().getText());
        // ((OrderInitialDataVO)getModel()).setOrderType
        // (ordSummPan.getOrderTypeData().getText());
    }
}