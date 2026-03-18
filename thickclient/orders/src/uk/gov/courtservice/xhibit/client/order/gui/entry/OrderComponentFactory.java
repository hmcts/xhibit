package uk.gov.courtservice.xhibit.client.order.gui.entry;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderAddressDetails;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderCheckBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderChoice;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderCollectionCentre;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderComboCourts;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderConvictionText;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderConvictionTextArea;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderCurrencyPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDate;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDurationComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderFixedList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderLabel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderSection;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderSingleDurationComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderSwitch;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderTextArea;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderTextField;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderTime;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderTranslationSection;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderXhibitComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderYesNoOption;

/**
 * <p>
 * Title: OrderComponentFactory
 * </p>
 * <p>
 * Description: OrderComponent interface to enforce desired functionality
 * requirements such as setting the visual component for display, obtaining the
 * component label and initialsing the component.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */

public class OrderComponentFactory {
    /**
     * Returns the component specified by the type parameter. If type does nor
     * exist, an OrderComponentException is thrown.
     * 
     * @param type
     *            type of component
     * @throws OrderComponentException
     */
    public static OrderComponent createComponent(String type) throws OrderComponentException {
        if (type.equals("textarea")) {
            return new OrderTextArea();
        } else if (type.equals("textfield")) {
            return new OrderTextField();
        } else if (type.equals("checkbox")) {
            return new OrderCheckBox();
        } else if (type.equals("switch")) {
            return new OrderSwitch();
        } else if (type.equals("choice")) {
            return new OrderChoice();
        } else if (type.equals("option")) {
            return new OrderOption();
        } else if (type.equals("date")) {
            return new OrderDate();
        } else if (type.equals("section")) {
            return new OrderSection();
        } else if (type.equals("label")) {
            return new OrderLabel();    
        } else if (type.equals("translationsection")) {
            return new OrderTranslationSection();
        } else if (type.equals("ordertype")) {
            return new OrderComboBox();
        } else if (type.equals("dropdown")) {
            return new OrderXhibitComboBox();
        } else if (type.equals("fixedlist")) {
            return new OrderFixedList();
        } else if (type.equals("durationdate")) {
            return new OrderDurationComboBox();
        } else if (type.equals("currency")) {
            return new OrderCurrencyPanel();
        } else if (type.equals("addresstextfields")) {
            return new OrderAddressDetails();
        } else if (type.equals("courttype")) {
            return new OrderComboCourts();
        } else if (type.equals("collectioncentre")) {
            return new OrderCollectionCentre();
        } else if (type.equals("timepanel")) {
            return new OrderTime();
        } else if (type.equals("convictiontextfield")) {
            return new OrderConvictionText();
        } else if (type.equals("yesnooption")) {
            return new OrderYesNoOption();
        } else if (type.equals("convictiontextarea")) {
            return new OrderConvictionTextArea();
        } else if (type.equals("singleduration")){
        	return new OrderSingleDurationComboBox();
        } else {
            throw new OrderComponentException("No such component " + type + ".");
        }
    }
}
