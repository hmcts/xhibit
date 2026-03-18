package uk.gov.courtservice.xhibit.client.order.gui.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderPreviewException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.util.Resource;

//Toggles content type of OrderPreviewPane...
//Solely for testing purposes...

public class ViewController extends JButton implements ActionListener {

    private boolean isXML;

    private OrderPreviewPane pane;

    public ViewController(OrderPreviewPane pane, boolean isXML) {
        super(Resource.getOrdersClientBundle("general.toggle"));
        this.isXML = isXML;
        this.pane = pane;
        this.addActionListener(this);
    }

    public ViewController(OrderPreviewPane pane) {
        this(pane, false);
    }

    public void toggleIsXML() throws OrderTransformException, OrderPreviewException {
        if (isXML) {
            this.isXML = false;
        } else {
            this.isXML = true;
        }

        pane.toggleView(isXML);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            toggleIsXML();
        } catch (OrderTransformException ote) {
            ote.printStackTrace();
        } catch (OrderPreviewException ope) {
            ope.printStackTrace();
        }
    }
}
