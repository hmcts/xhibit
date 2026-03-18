package uk.gov.courtservice.xhibit.client.order.screens.panel;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderPanelData
 * </p>
 * <p>
 * Description: Interface that all orders panels must implement
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

public interface OrderPanelData {
    // public void setPanelFromModel(OrderInitialDataVO orderDataModel);

    public OrderInitialDataVO getModel();

    public void updateModel();
}