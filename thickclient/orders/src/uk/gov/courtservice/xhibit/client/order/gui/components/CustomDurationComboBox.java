package uk.gov.courtservice.xhibit.client.order.gui.components;

import javax.swing.JComboBox;

/**
 * <p>
 * Title: CustomDurationComboBox Custom. A ComboBox component that displays
 * either Years, Days or Hours as part of the Duration widget.
 * </p>
 * <p>
 * Description: This class will display non-editable JComboBox component and is
 * utilised by the OrderDurationComboBox class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Des Johnston
 * @version 1.0
 */
public class CustomDurationComboBox extends JComboBox {
    // Component to display the duration period.
    private JComboBox boxCb;

    // String that contains a unique reference for the combobox.
    private String reference;

    /**
     * Create a combobox with appropriate duration period values .
     * 
     * @param contents
     *            String array that populates the combbox with duration period
     *            values, can set as years, months or days.
     * @param ref
     *            Reference for the JComboBox component.
     */
    public CustomDurationComboBox(String[] contents, String ref, String initial) {
        super(contents);
        Integer numb = Integer.parseInt(initial);
        this.boxCb = new JComboBox(contents);
        this.reference = ref;
        this.setSelectedItem(numb.toString());
        
       
    }

    /**
     * @return Return a JComboBox component.
     */
    public JComboBox getComboBox() {
        return boxCb;
    }

    /**
     * @return Return a reference for a JComboBox component.
     */
    public String getRef() {
        return this.reference;
    }
}
