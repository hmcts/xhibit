/**
 * Created by IntelliJ IDEA.
 * User: gz257j
 * Date: Jan 20, 2003
 * Time: 3:15:39 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.components;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CustomTextArea
 * </p>
 * <p>
 * Description: Custom text area to provide a multi-lined view of plain text,
 * supporting tab traversal.
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

public class CustomAddressFields extends JPanel {
    private static final Logger log = CSServices.getLogger(CustomAddressFields.class);

    private JTextField addressLine1 = new JTextField(20);

    private JTextField addressLine2 = new JTextField(20);

    private JTextField addressLine3 = new JTextField(20);

    private JTextField addressLine4 = new JTextField(20);

    private JTextField addressLine5 = new JTextField(20);

    private JTextField postcode = new JTextField(10);

    public CustomAddressFields() {
        // constraints.gridx = 0;
        // constraints.gridy = 0;
        log.debug("At custom address ***********");
        this.add(addressLine1);
        // constraints.gridx = 0;
        // constraints.gridy = 1;
        this.add(addressLine2);
        this.add(addressLine3);
        this.add(addressLine4);
        this.add(addressLine5);
        this.add(postcode);
    }

    public JComponent getAddress() {
        return this;
    }
}
