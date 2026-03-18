/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 2:24:43 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import uk.gov.courtservice.xhibit.client.order.exceptions.ButtonComponentException;

/**
 * Factory class used to obtain instances of various buttons required by
 * Xhibit2.
 */
public class ButtonFactory {

    /**
     * Returns the required JButton as specified by the string type.
     * 
     * @param type
     *            type of button
     * @return JButton
     */
    public static ButtonComponent createButtonComponent(String type) {
        if (type.equals("cancel")) {
            return new Cancel();
        } else if (type.equals("print")) {
            return new Print();
        } else if (type.equals("save")) {
            return new Save();
        } else if (type.equals("sign")) {
            return new Sign();
        } else if (type.equals("send")) {
            return new Send();
        } else {
            throw new ButtonComponentException("No such button " + type + ".");
        }
    }
}
