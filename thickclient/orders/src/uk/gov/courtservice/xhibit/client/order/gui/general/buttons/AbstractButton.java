/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 1:38:05 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Abstract button class containing useful helper methods provided by the button
 * helper class
 */
/**
 * 
 * <p>
 * Title: AbstractButton
 * </p>
 * <p>
 * Description: Abstract button class containing useful helper methods provided
 * by the button helper class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public abstract class AbstractButton extends JButton implements ActionListener, ButtonComponent {
    private static final Logger log = CSServices.getLogger(AbstractButton.class);

    private ButtonHelper helper;

    private JButton visualBtn;

    /**
     * Sets the visual button
     * 
     * @param button
     *            the button
     */
    public void setVisualButton(JButton button) {
        this.visualBtn = button;
    }

    /**
     * Return the visual button
     * 
     * @return the button
     */
    public JButton getVisualButton() {
        return this.visualBtn;
    }

    /**
     * Set the helper
     * 
     * @param helper
     *            the helper
     */
    public void setHelper(ButtonHelper helper) {
        this.helper = helper;
    }

    /**
     * Return the helper
     * 
     * @return the helper
     */
    public ButtonHelper getHelper() {
        return this.helper;
    }

    /**
     * Set the helper object and create the button.
     * 
     * @param helper
     *            button helper.
     */
    public void init(ButtonHelper helper) {
        setLookAndFeel();
        this.setHelper(helper);
        initButton();
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log.error("AbstractButton: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }

}