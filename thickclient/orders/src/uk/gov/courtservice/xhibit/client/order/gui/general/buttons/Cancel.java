/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 1:43:43 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.util.Resource;

public class Cancel extends uk.gov.courtservice.xhibit.client.order.gui.general.buttons.AbstractButton implements
        PropertyChangeListener {
    private static final Logger log = CSServices.getLogger(Cancel.class);

    private OrderStatus status;

    /**
     * Creates the JButton, sets it as the visual component and add the 2
     * required listeners to detect any changes made to the OrderStatus and to
     * allow the user to directly alter the order status.
     */
    public void initButton() {
        this.status = getHelper().getStatus();
        status.addPropertyChangeListener(this);
        JButton cancelBtn = new JButton(Resource.getOrdersClientBundle("button.cancel"));
        setVisualButton(cancelBtn);
        cancelBtn.addActionListener(this);
        determineEnabled(cancelBtn);
        cancelBtn.setVerifyInputWhenFocusTarget(false);
    }

    /**
     * Determines whether the buttons functionality should be available or not
     * depending on the order status.
     */
    public void determineEnabled(JButton b) {
        b.setEnabled(true);
    }

    /**
     * Method to attempt to alter the status of the current order.
     * 
     * @param ae
     *            A semantic event.
     */
    public void actionPerformed(ActionEvent ae) {
        // dispose of the parent dialog
        // ((JDialog)getHelper().getGUI().getParent().getParent().getParent().getParent()).dispose();
        getHelper().getGUI().dispose();
    }

    /**
     * Upon any change to the order status, the button may become enabled or
     * disabled.
     * 
     * @param event
     *            delivered whenever order status changes.
     */
    public void propertyChange(PropertyChangeEvent event) {
        // cancel button is always enabled...
    }
}