package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;

/**
 * <p>
 * Title: OrderTime. Generic time component derived from class XTimePanel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */

public class OrderTime extends AbstractOrderComponent implements FocusListener {

    private static final Logger log = CSServices.getLogger(OrderTime.class);

    // set String to hold default value of time stored in the blank schema
    private static final String DEFAULT_TIME = "14:20:00+05:00";

    // set time delimeter to identify hours, minutes, seconds
    private static final String TIME_DELIM = ":";

    // set String to hold zero seconds
    private static final String ZERO_SECONDS = ":00";

    private OrderPanel timePanel = new OrderPanel();

    private XTimePanel ordersTimePanel = new XTimePanel(timePanel);

    private Calendar c = Calendar.getInstance();

    /**
     * Creates a new JPanel containing the time fields and sets it as a
     * VisualComponent for display.
     */
    public void initComponent() {
        log.debug("Order Time Component CREATED");
        setVisualComponent(this.createPanel());
    }

    public XTimePanel getOrdersTimePanel() {
        return ordersTimePanel;
    }

    /**
     * 
     * @return JPanel with time fields.
     */
    private JPanel createPanel() {
        // get the value of the time value from the xml
        String storedTime = getHelper().getValue();
        if (storedTime == null) {
        	storedTime = "14:20:00-05:00"; // default
        }

        // check to see if the value in the xml is the default
        if (DEFAULT_TIME.equals(storedTime)) {
            // set to default 10:00
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 00);
        } else {
            // there is a stored value in the xml
            int index = storedTime.indexOf(TIME_DELIM);

            if (index >= 0)
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(storedTime.substring(0, index)));
            index++;
            int index2 = storedTime.indexOf(TIME_DELIM, index);
            if (index2 <= index) {
                index2 = storedTime.length();
            }
            c.set(Calendar.MINUTE, Integer.parseInt(storedTime.substring(index, index2)));
        }

        ordersTimePanel.setVisible(true);
        OrderPanel op = new OrderPanel();
        ordersTimePanel.setTime(c);

        // set xml value not forgetting seconds.
        getHelper().setValue(ordersTimePanel.getText() + ZERO_SECONDS);
        createListeners();
        op.add(ordersTimePanel);
        return op;
    }

    /**
     * CreateListeners is a method that contains focus listeners on the time
     * component. When the time component has gained focus, the current xml
     * value is retrieved and displayed in the preview pane. Updated values for
     * the time component are displayed in the preview pane when the component
     * looses focus.
     */
    public void createListeners() {
        ordersTimePanel.getTimeComponent().addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent event) {
                // set the focus to the beginning of the field
                ordersTimePanel.getTimeComponent().setCaretPosition(0);
            }

            public void focusLost(FocusEvent event) {
                // set the xml value to the time component text and add
                // seconds.
                getHelper().setValue(ordersTimePanel.getTimeComponent().getText() + ZERO_SECONDS);
            }
        });
    }

    /**
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */

    public void focusLost(FocusEvent event) {
    }

    /**
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusGained(FocusEvent event) {
    }
}