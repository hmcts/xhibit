package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.TextLimitedJTextField;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * Created by IntelliJ IDEA. User: gz257j Date: Feb 25, 2003 Time: 9:02:16 AM To
 * change this template use Options | File Templates.
 */
public class OrderConviction extends AbstractOrderComponent {

    TextLimitedJTextField textFieldText;

    public void initComponent() throws OrderComponentException {

        textFieldText = new TextLimitedJTextField(getHelper().getValue(), 10, "10");

        if (textFieldText.getText().length() == 0) {
            textFieldText.setEnabled(true);
        } else {
            textFieldText.setEnabled(false);
        }

        setVisualComponent(textFieldText);
    }
}
