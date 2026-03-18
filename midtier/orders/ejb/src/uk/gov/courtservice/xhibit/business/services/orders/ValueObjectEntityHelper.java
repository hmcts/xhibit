package uk.gov.courtservice.xhibit.business.services.orders;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrder;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatus;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatus;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmail;
import uk.gov.courtservice.xhibit.business.vos.services.email.EmailValue;
import uk.gov.courtservice.xhibit.business.vos.services.orders.OrderDeliveryStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.orders.OrderStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.orders.OrderTemplateValue;
import uk.gov.courtservice.xhibit.business.vos.services.orders.OrderTypeValue;
import uk.gov.courtservice.xhibit.business.vos.services.orders.OrderValue;

/**
 * <p>
 * Title: Value Object Assembler implementation for the Orders Service.
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
 * @author Bob Boothby
 * @version 1.0
 */

public class ValueObjectEntityHelper {
    public static Object getValueFromEntity(Object o) {
        if (o instanceof Collection) {
            if (((Collection) o).size() != 0) {
                o = ((Collection) o).toArray()[0];
            } else {
                return null;
            }

        }

        if (o instanceof XhbOrderType) {
            XhbOrderType entity = (XhbOrderType) o;
            return new OrderTypeValue(entity.getCode(), entity.getOrderTypeId(), entity.getVersion());
        }
        if (o instanceof XhbOrderStatus) {
            XhbOrderStatus entity = (XhbOrderStatus) o;
            return new OrderStatusValue(entity.getCode(), entity.getOrderStatusId(), entity.getVersion());
        }
        if (o instanceof XhbOrderDeliveryStatus) {
            XhbOrderDeliveryStatus entity = (XhbOrderDeliveryStatus) o;
            return new OrderDeliveryStatusValue(entity.getCode(), entity.getOrderDeliveryStatusId(), entity.getVersion());
        }
        if (o instanceof XhbOrderTemplate) {
            XhbOrderTemplate entity = (XhbOrderTemplate) o;
            return new OrderTemplateValue(entity.getOrderTemplateId(), entity.getEditorTemplateName(), entity
                    .getNarrativeTemplateName(), entity.getDisplayTransformName(), entity.getVersion(),
                    (OrderTypeValue) getValueFromEntity(entity.getXhbOrderType()));
        }
        if (o instanceof XhbOrder) {
            XhbOrder entity = (XhbOrder) o;
            return new OrderValue(entity.getOrderId(), entity.getDataXml(), entity.getSigningDate(), entity
                    .getSignedBy(), entity.getDeliveryDate(), entity.getVersion(),
                    (OrderStatusValue) getValueFromEntity(entity.getOrderStatusId()),
                    (OrderDeliveryStatusValue) getValueFromEntity(entity.getOrderDeliveryStatusId()),
                    (OrderTemplateValue) getValueFromEntity(entity.getXhbOrderTemplate()), (EmailValue) getValueFromEntity(entity.getEmailId()));
        }

        return null;
    }
}