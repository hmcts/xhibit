package uk.gov.courtservice.xhibit.client.order.gui.components;

import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxUtility;
import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxAgent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title: CustomCourtOption.  A editable combobox component that displays either Crown, Magistrates
 * or Youth court names.</p>
 * <p>Description: A ComboBox component used to display a list of court names. This class implements
 * ChangeListener to detect changes to the selected court value, and updates the underlying
 * order data with the newly selected court name.  This class is instantiated in class MultipleCourtListPanel</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author David
 * @version 1.0
 */
public class CustomCourtOption extends JRadioButton implements ChangeListener{

    // Component that contains a court list, can be one of the following: Crown, Magistrates, Youth,
    private String[] list;
    // Component to display the court list.
    private JComboBox boxCb;
    // Utility component to provide searching and auto-completion of text for the ComboBox
    private ComboBoxUtility util = new ComboBoxUtility();

    private String ref;
    private String type;
    private ComboBoxAgent agent;
    /**
     * Restores the list of court names to the original list.
     * @param list Contains the court list.
     * @param boxCb  Creates a JComboBox component.
     * @param label  Creates a JLabel component.
     */
    public CustomCourtOption(String[] list, JComboBox boxCb,
                             String label, ComboBoxAgent agent,
                             String ref, String orderType)
    {
        super(label);
        this.type = label.toLowerCase();
        setNewRef(ref, orderType);
        this.list = list;
        this.boxCb = boxCb;
        this.agent = agent;
        this.addChangeListener(this);
    }

    /**
     * Restores the list of court names to one of the following lists:
     * Crown court, Magistrate court, Youth court.
     * @param e Invoked when the target of the listener has changed its state.
     */
    public void stateChanged(ChangeEvent e)
    {
        boolean selected = ((JRadioButton) e.getSource()).isSelected();
        if (selected)
        {
            this.agent.setContents(list);
            util.restoreNames(boxCb, list);

        }
    }

    public void setNewRef(String old, String orderTypeElement)
    {
        if(orderTypeElement.equals("false")){
            String newRef = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseType";
            this.ref = newRef;
        }else{
            String oldRef = old;
            this.ref = oldRef;
        }
    }
    public String getRef()
    {
        return this.ref;
    }

    public String getType()
    {
        return this.type;
    }
}
