package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.client.exceptions.OrdersInitialisationException;
import uk.gov.courtservice.xhibit.client.order.gui.general.OrderGUI;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: OrderGuiHelper
 * </p>
 * <p>
 * Description: Displays the Orders GUI
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

public class OrderGuiHelper {

    private static final String ORDER_GUI_TITLE1 = "order.gui.title.1";

    private static final String ORDER_GUI_TITLE2 = "order.gui.title.2";

    private static final String ORDER_GUI_TITLE3 = "order.gui.title.3";

    private static final Logger log = CSServices.getLogger(OrderGuiHelper.class);

    private static JDialog dialog;

    private OrderGuiHelper orderGuiFrame;

    // primary Panel for the Orders section of the Xhibit gui.
    private OrderGUI gui;

    /**
     * Constructor
     * 
     * @param xac
     *            the Xhibit controller
     */
    public OrderGuiHelper(XhibitApplicationController xac) {
        if (dialog == null) {
            dialog = new JDialog(xac, "", true);
            dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } else {
            log.debug("<<<<>>>> Existing Dialog before: " + dialog.getContentPane().getComponentCount());
            dialog.getContentPane().removeAll();
            log.debug("<<<<>>>> Existing Dialog after: " + dialog.getContentPane().getComponentCount());
        }
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.addWindowListener(new WindowListener() {
            public void windowDeactivated(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
                log.debug("$$$ OrderGuiHelper windowClosed");
                // if the window is closed using the Close button
                // we get this event
                cleanup();
                dialog.removeWindowListener(this);
            }

            public void windowClosing(WindowEvent e) {
                // if the window is closed using the Windows X button
                // we get this event
                log.debug("$$$ OrderGuiHelper windowClosing");
                cleanup();
                dialog.removeWindowListener(this);
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        Toolkit kit = dialog.getToolkit();
        Dimension wndSize = kit.getScreenSize();
    }
    
    public void setEnabled(boolean enabled)
    {
    	
    }

    /**
     * Displays the Orders GUI after the oredr has been constructed and
     * transformed correctly
     * 
     * @param order
     *            The XhbOrderValue
     * @param oidvo
     *            The current model OrderInitialDataVO
     * @param xac
     *            The XhibitApplicationController
     */
    public void showOrderGUI(XhbOrderValue order, OrderInitialDataVO model, XhibitApplicationController xac) {
        // make sure that the gui is reset to null
        gui = null;
        try {
            log.debug("<<>> showOrderGUI: model.getCaseID(): " + model.getCaseID());
            log.debug("<<>> showOrderGUI: model.getDefendantID(): " + model.getDefendantID());
            log.debug("<<>> showOrderGUI: model.getDefendantOnCaseID(): " + model.getDefendantOnCaseID());
            log.debug("<<>> showOrderGUI: model.getDefendantName(): " + model.getDefendantName());
            log.debug("<<>> showOrderGUI: model.getOrderType(): " + model.getOrderType());
            log.debug("<<>> showOrderGUI: order.getDefendantOnCaseId(): " + order.getDefendantOnCaseId());
            log.debug("<<>> showOrderGUI: order.getDataXml(): " + order.getDataXml());
            order.setDataXml(formatISO8859Chars(order.getDataXml()));
            StringBuffer buffer = new StringBuffer();
            buffer.append(ResourceHelper.getResourceString(ORDER_GUI_TITLE1));
            buffer.append(model.getCaseID());
            buffer.append(ResourceHelper.getResourceString(" "));
            buffer.append(ResourceHelper.getResourceString(ORDER_GUI_TITLE2));
            buffer.append(model.getDefendantName());
            buffer.append(" ");
            buffer.append(order.getXhbOrderTemplate().getXhbOrderType().getCode());

            dialog.setTitle(buffer.toString());
            gui = new OrderGUI(order, xac, this, model);
        } catch (OrdersInitialisationException ex) {
            throw new CSUnrecoverableException("Failure to initialise Orders GUI", ex);
        }
        dialog.getContentPane().add(gui, BorderLayout.CENTER);
        dialog.repaint();
        dialog.setVisible(true);
    }
    
    
    private String formatISO8859Chars(String inString) {
    	String outString = inString.replaceAll("&#xA3;", "£");
    	outString = outString.replaceAll("&amp;#xA3;", "£");
    	outString = outString.replaceAll("[ÃÂ]", "");
    	
    	return outString;
    }

    /**
     * Return the Toolkit for the dialog
     * 
     * @return the Toolkit
     */
    public Toolkit getToolkit() {
        return dialog.getToolkit();
    }

    /**
     * Sets the bounds for the dialog
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @param w
     *            width
     * @param h
     *            height
     */
    public void setBounds(int x, int y, int w, int h) {
        dialog.setBounds(x, y, w, h);
    }

    /**
     * Cleanup and remove the dialog
     */
    public void cleanup() {
        // cleanup the dialog
        if (gui != null) {
            gui.cleanup();
            gui = null;
        }
        dialog.getContentPane().removeAll();
        dialog.setSize(new Dimension(1, 1));
        dialog.dispose();
    }
}