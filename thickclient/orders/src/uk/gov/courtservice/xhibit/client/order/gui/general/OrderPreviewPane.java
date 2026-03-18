package uk.gov.courtservice.xhibit.client.order.gui.general;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.Caret;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderPreviewException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
import uk.gov.courtservice.xhibit.client.order.io.OrderTransform;
import uk.gov.courtservice.xhibit.client.order.io.OrderWriter;

/**
 * <p>
 * Title: OrderPreviewPane
 * </p>
 * <p>
 * Description: Main container for displaying a preview of the current order
 * data. PropertyChangeListener is implemented to detect any modifications made
 * to the order data and provides automatic updates to the right hand editor
 * pane.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class OrderPreviewPane extends JEditorPane implements PropertyChangeListener {
    private static final Logger log = CSServices.getLogger(OrderPreviewPane.class);

    private OrderTransform orderTransform;

    private boolean asXML;

    private OrderData data;

    private TimerThread timer;

    /**
     * Constructs an OrderPreviewPane with default "text/html" content type.
     * 
     * @param data
     *            OrderData obtained from source xml.
     * @param is
     *            InputStream to obtain the orderTransform.
     * @throws OrderTransformException
     *             exception thrown when transforming an order.
     * @throws OrderPreviewException
     *             A general exception thrown when dealing with the Order
     *             Preview functionality.
     */
    public OrderPreviewPane(OrderData data, InputStream is) throws OrderTransformException, OrderPreviewException {
        super("text/html", "");
        this.setSize(400, 600);
        this.data = data;
        data.addPropertyChangeListener(this);
        orderTransform = OrderFactory.getTransform(is);
        setLookAndFeel();
        redisplay(data);
        setEditable(false);
    }

    /**
     * Redisplays the OrderData when either the OrderData is modified or the
     * ViewController is used to toggle the content type.
     * 
     * @param data
     *            OrderData obtained from source xml.
     * @throws OrderTransformException
     *             exception thrown when transforming an order.
     * @throws OrderPreviewException
     *             A general exception thrown when dealing with the Order
     *             Preview functionality.
     */
    protected void redisplay(OrderData data) throws OrderTransformException, OrderPreviewException {
        this.timer = null;
        Caret caret = getCaret();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // This code is really for debugging and could be factored out in
        // production?
        if (asXML) {
            try {
                OrderWriter writer = OrderFactory.getWriter(outputStream);
                writer.write(data);
                this.setText(writer.toString());
            } catch (OrderWriterException e) {
                throw new OrderPreviewException("Failed to display the order data as XML", e);
            }
            this.setContentType("text");
        } else {
            this.setContentType("text/html");
            orderTransform.transform(data, outputStream);
        }
        setText(outputStream.toString());
        setCaret(caret);

    }

    /**
     * Sets the flag to display output as xml - debug only
     * 
     * @param b
     *            true if as xml
     */
    public void setAsXML(boolean b) {
        this.asXML = b;
    }

    /**
     * Returns the order data
     * 
     * @return the data
     */
    public OrderData getData() {
        return this.data;
    }

    /**
     * Returns the as xml flag
     * 
     * @return as xml
     */
    public boolean getAsXML() {
        return this.asXML;
    }

    /**
     * Changes boolean xml value thus changing the content type of
     * OrderPreviewPane.
     * 
     * @param isXML
     *            true for xml, false for "text/html"
     * @throws OrderTransformException
     *             exception thrown when transforming an order.
     * @throws OrderPreviewException
     *             A general exception thrown when dealing with the Order
     *             Preview functionality.
     */
    protected void toggleView(boolean isXML) throws OrderTransformException, OrderPreviewException {
        this.setAsXML(isXML);
        redisplay(data);
    }

    /**
     * Upon any modification to the order data, a PropertyChangeEvent is fired,
     * executing the redisplay method to reveal the updates.
     * 
     * @param event
     *            delivered whenever OrderData changes.
     */
    public void propertyChange(PropertyChangeEvent event) {
        this.data = (OrderData) event.getSource();
        if (timer == null) {
            timer = new TimerThread("timer", this);
            timer.start();
        }
    }

    /**
     * Handles exceptions
     * 
     * @param e
     *            the exception
     */
    protected void displayCriticicalFailure(Exception e) {
        e.printStackTrace();
        throw new CSUnrecoverableException("Critical Failure with OrderPreviewPane", e);
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
        log.error("OrderPreviewPane: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }
}