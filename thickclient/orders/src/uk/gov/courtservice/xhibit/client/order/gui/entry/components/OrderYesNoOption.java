package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.ItemEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;

/**
 * <p>
 * Title: OrderYesNoOption
 * </p>
 * <p>
 * Description: Class to construct an OrderPanel containing single or multiple
 * radio buttons. These map to YesNoTypes within the appropriate XML Schema. The
 * default OrderOption expects boolean values, whereas this expects yes or no
 * when set.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderYesNoOption extends OrderOption {
    private static final Logger log = CSServices.getLogger(OrderYesNoOption.class);

    /**
     * Sets the parent of the component and builds the radio buttons
     * 
     * @param componentParent
     *            The parent to this component.
     */
    public void setComponentParent(OrderComponent componentParent) {
        super.setSubclassComponentParent(componentParent);
        if (componentParent instanceof OrderChoice) {
            ((OrderChoice) getComponentParent()).addButton(getButtonRadio());
            OrderComponent parent = this.getComponentParent();
            OrderComponentHelper parentHelper = parent.getHelper();
            log.debug("Parent Radio Button refererence :" + parentHelper.getOrderDataReference());
            // If this is parent the root element, then set the radio button
            // according
            // to the value retrieved from the XML.
            if (parentHelper.getOrderDataReference().equals("/")) {
                log.debug("Child Radio Button item :" + this.getHelper().getAttribute("name"));
                log.debug("Radio Button item :" + this.getHelper().getValue());
                boolean selected = isSelected();
                getButtonRadio().setSelected(selected);
                log.debug("Button selected state: " + getButtonRadio().isSelected());
            } else {
                // Set radio buttons to default values.
                setRadioButtons(parentHelper);
            }
        } else {
            throw new OrderComponentException("Parent of an OrderOption is not an OrderChoice. It is a '"
                    + componentParent.getClass().getName() + "'.");
        }
    }

    /**
     * Check if the value should be set to true or false
     * 
     * @return true if yes
     */
    private boolean isSelected() {
        boolean selected = Boolean.valueOf(this.getHelper().getValue().equals("yes") ? "true" : "false").booleanValue();
        return selected;
    }

    /**
     * Check state of radiobutton which is linked to xml attribute "value"
     */
    public void itemStateChanged(ItemEvent e) {
        boolean selected = e.getStateChange() == ItemEvent.SELECTED;
        getPanel().setEnabled(selected);

        log.debug("$$$ OrderOption: itemStateChanged.getHelper().getOrderDataReference() |"
                + getHelper().getOrderDataReference() + "|");
        // If data reference is not null, set the value to the appropriate
        // attribute
        if (getHelper().getOrderDataReference() != null) {
            if (selected) {
                log.debug("$$$ OrderOption: itemStateChanged.getHelper().getAttribute" + "(\"selectedValue\")"
                        + getHelper().getAttribute("selectedValue"));
                getHelper().setValue(getHelper().getAttribute("selectedValue"));
            } else {
                log.debug("$$$ OrderOption: itemStateChanged.getHelper().getAttribute" + "(\"unselectedValue\")"
                        + getHelper().getAttribute("unselectedValue"));
                getHelper().setValue(getHelper().getAttribute("unselectedValue"));
            }
        } else if (selected) {
            log.debug("$$$ OrderOption: REFERENCE IS NULL - setItemValue()");
            setItemValue();
        }
    }

}
