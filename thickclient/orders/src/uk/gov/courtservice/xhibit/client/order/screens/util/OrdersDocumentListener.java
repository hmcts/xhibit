package uk.gov.courtservice.xhibit.client.order.screens.util;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: OrdersDocumentListener
 * </p>
 * <p>
 * Description: Listens for text events on a JTextField Document and
 * enables/disables a button if the length of the text is > 0
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrdersDocumentListener implements DocumentListener {
    private JButton button;

    private int orderMode;
    
    private boolean isMonetary; // Monetary orders are special types and button enablements are different
    
    private boolean isD20; // D20 (DVLA) orders are special types and button enablements are different
    
    private boolean isACase; // A case variable to account for A case D20 

    private static final Logger log = CSServices.getLogger(OrdersDocumentListener.class);

    public OrdersDocumentListener(JButton btn, int mode, boolean isMonetaryOrder, boolean isD20Order, boolean isAcase) {
        button = btn;
        orderMode = mode;
        isMonetary = isMonetaryOrder;
        isD20 = isD20Order;
        isACase = isAcase;
    }

    public void changedUpdate(DocumentEvent e) {
        log.debug("$$$ changedUpdate Button1 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
        if (orderMode == OrderInitialDataVO.CREATE_MODE) {
            log.debug("$$$ changedUpdate Button2 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
            // Enable the button if the text field has text entered
            button.setEnabled(e.getDocument().getLength() > 0);
        }
    }

    public void removeUpdate(DocumentEvent e) {
        log.debug("$$$ removeUpdate Button1 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
        if (orderMode == OrderInitialDataVO.CREATE_MODE) {
            log.debug("$$$ removeUpdate Button2 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
            // Enable the button if the text field has text entered
            button.setEnabled(e.getDocument().getLength() > 0);
        } else if ((isMonetary) && (button.getText() != null) && (button.getText().equals("Finish"))) {
            button.setEnabled(e.getDocument().getLength() > 0);
        }
    }

    public void insertUpdate(DocumentEvent e) {
        log.debug("$$$ insertUpdate Button1 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
        if (button.getText().equals("Next >")) {
            if (orderMode == OrderInitialDataVO.CREATE_MODE) {
                boolean settingButton = true;
                if (isMonetary) {
                    if (button.getText().contains("Next")) {
                        settingButton = false;
                    }
                }
                // keep next button inactive
                if (isD20 && isACase) {
                	if (button.getText().contains("Next")) {
                        settingButton = false;
                    }
                }
                // Enable the button if the text field has text entered
                if (settingButton) {
                    log.debug("$$$ insertUpdate Button2: setting button " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
                    button.setEnabled(e.getDocument().getLength() > 0);
                }
            }
        } else if (button.getText().equals("Finish")) {
            if (orderMode == OrderInitialDataVO.CREATE_MODE) {
                if (isMonetary) {
                    log.debug("$$$ insertUpdate Button3 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
                    button.setEnabled(e.getDocument().getLength() > 0);
                }
                if (isD20 && isACase) {
            		log.debug("$$$ insertUpdate Button3 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));
                    button.setEnabled(e.getDocument().getLength() > 0);
                }
            } else if (orderMode == OrderInitialDataVO.VIEW_MODE) {
                log.debug("$$$ insertUpdate Button4 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));

                // Enable the button if the text field has text entered
                button.setEnabled(e.getDocument().getLength() > 0);
            } else if (orderMode == OrderInitialDataVO.COPY_MODE) {
                log.debug("$$$ insertUpdate Button5 " + button.getText() + " " + (e.getDocument().getLength() > 0) + "; orderMode="+getOrderMode(orderMode));

                // Enable the button if the text field has text entered
                button.setEnabled(e.getDocument().getLength() > 0);
            }
        }
        log.debug("$$$ insertUpdate Button6 " + button.getText() + ", length of text=" + (e.getDocument().getLength()) + "; orderMode="+getOrderMode(orderMode));
    }
    
    private String getOrderMode(int orderMode) {
        switch (orderMode) {
            case 0: return "Create";
            case 1: return "View";
            case 2: return "Replace";
            case 3: return "Copy";
        }
        return "Unknown - " + orderMode;
    }
}