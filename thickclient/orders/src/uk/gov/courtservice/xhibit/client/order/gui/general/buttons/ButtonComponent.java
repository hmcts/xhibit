/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 2:25:45 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import javax.swing.JButton;

/**
 * Interface to enforce the basic functionalty requirements of each button.
 */
public interface ButtonComponent {

    public void init(ButtonHelper helper);

    public void initButton();

    public JButton getVisualButton();

    public void setVisualButton(JButton button);

    public ButtonHelper getHelper();

    public void setHelper(ButtonHelper helper);

}
