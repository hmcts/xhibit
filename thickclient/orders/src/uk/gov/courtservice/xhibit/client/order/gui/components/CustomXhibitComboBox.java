package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

/**
 * Created by IntelliJ IDEA. User: tzj8k5 Date: 22-Mar-2005 Time: 11:10:02 To
 * change this template use File | Settings | File Templates.
 */
public class CustomXhibitComboBox extends JComboBox {
    private String[] list;
    

	public CustomXhibitComboBox(String[] list, Dimension size) {
        super(list);
        this.list = list;
        this.setEditable(true);
        this.setPreferredSize(size);
    }

    public String[] getList() {
        return this.list;
    }

    public void setList(String[] newList) {
        this.list = newList;
    }
}
