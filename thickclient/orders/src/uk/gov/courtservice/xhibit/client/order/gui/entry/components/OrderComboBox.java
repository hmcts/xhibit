/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 6, 2003
 * Time: 8:53:55 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderComboBox
 * </p>
 * <p>
 * Description: ComboBox component used to display a list of court names. This
 * class implements ItemListener to detect changes to the selected court value,
 * and updates the underlying order data with the newly selected court name.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */

public class OrderComboBox extends AbstractOrderComponent implements ItemListener {

    private CustomComboBox box;

    /**
     * Construct the ComboBox component with default court name.
     */
    public void initComponent() {
        box = new CustomComboBox();
        box.checkOrderType("true");
        String defaultCourt = getHelper().getAttribute("default");
        if ((defaultCourt != null) && (box.getCourtNames().isCourtExists(defaultCourt))) {
            box.setSelectedItem(defaultCourt);
        }
        setVisualComponent(box);
        box.addItemListener(this);
        String text = getHelper().getValue();
        if (isD20Order() || (text != null && text.length() > 0)) {
            box.setSelectedItem(text);
            
            
        }
    }

    /**
     * Uses helper class to set the value of the court name in order data to the
     * newly selected value.
     * 
     * @param e
     *            an item event.
     */
    public void itemStateChanged(ItemEvent e) {
        setCourtName();
    }

    /**
     * Sets court name value in order data.
     */
    private void setCourtName() {
        getHelper().setValue(
                ((JTextField) ((JComboBox) getVisualComponent()).getEditor().getEditorComponent()).getText());
    }

}
