package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxAgent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxUtility;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderComboCourts;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;

/**
 * <p>
 * Title: CourtOption. A editable combobox component that displays either Crown,
 * Magistrates or Youth court names.
 * </p>
 * <p>
 * Description: A ComboBox component used to display a list of court names. This
 * class implements ChangeListener to detect changes to the selected court
 * value, and updates the underlying order data with the newly selected court
 * name. This class is instantiated in class MultipleCourtListPanel
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
public class CourtOption extends JRadioButton implements ItemListener {

	// Component that contains a court list, can be one of the following:
	// Crown, Magistrates, Youth,
	private String[] list;

	// Component to display the court list.
	private JComboBox boxCb;

	// Utility component to provide searching and auto-completion of text
	// for the ComboBox
	private ComboBoxUtility util = new ComboBoxUtility();

	private String ref;

	private String type;

	private ComboBoxAgent agent;

	private String courtCode; // SCR 52933 & 52926

	private String shortName; // SCR 52933 & 52926

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String addressLine4;

	private String addressLine5;

	private String postCode;

	private String telephone;

	private String dxNumber;

	private OrderComboCourts comboCourts;

	private String dxRefNum;

	/**
	 * Restores the list of court names to the original list.
	 * 
	 * @param list
	 *            Contains the court list.
	 * @param boxCb
	 *            Creates a JComboBox component.
	 * @param label
	 *            Creates a JLabel component.
	 * @param agent
	 *            the comboboxagent
	 * @param ref
	 *            the xpath reference
	 * @param helper
	 *            the helper
	 */
	public CourtOption(String[] list, JComboBox boxCb, String label, ComboBoxAgent agent, String ref,
			OrderComponentHelper helper) {
		super(label);
		this.type = label;
		setNewRef(ref);
		setShortName(ref); // SCR 52933 & 52926 - Start
		setCourtCode(ref); // SCR 52933 & 52926 - Start
		setAddress(ref); // set the address xpath details
		setTelephone(ref); // set the telephone xpath details
		setDXNumber(ref);
		this.list = list;
		this.boxCb = boxCb;
		this.agent = agent;
		this.addItemListener(this);
		String type = getType();
		String ref1 = getRef();
		String value = "";

		if (ref1.indexOf("CollectionCentre") <= 0) {
			value = helper.getValue(ref1);
		} else {
			value = helper.getValue(ref);
		}
		boolean selected = false;
		if (value != null) {
			value.equalsIgnoreCase(type);
		}
		this.setSelected(selected);
		comboCourts = new OrderComboCourts();
		this.addItemListener(this);

	}
	 

	/**
	 * Restores the list of court names to one of the following lists: Crown
	 * court, Magistrate court, Youth court.
	 * 
	 * @param e
	 *            Invoked when the target of the listener has changed its state.
	 */
	public void itemStateChanged(ItemEvent e) {
		boolean selected = ((JRadioButton) e.getSource()).isSelected();
		if (selected) {
			this.agent.setContents(list);
			util.restoreNames(boxCb, list);
		}
	}

	/**
	 * Set the new address Ref
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setAddress(String old) {
		String newAddressRef = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseAddress";
		this.addressLine1 = newAddressRef + "/apd:Line[1]";
		this.addressLine2 = newAddressRef + "/apd:Line[2]";
		this.addressLine3 = newAddressRef + "/apd:Line[3]";
		this.addressLine4 = newAddressRef + "/apd:Line[4]";
		this.addressLine5 = newAddressRef + "/apd:Line[5]";
		this.postCode = newAddressRef + "/apd:PostCode";
	}

	/**
	 * Sets the new reference
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setNewRef(String old) {
		String newRef = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseType";
		this.ref = newRef;
	}

	// SCR 52933 & 52926 - Start
	/**
	 * Sets the short name
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setShortName(String old) {
		String newShortName = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseCode/@CourtHouseShortName";
		this.shortName = newShortName;
	}

	/**
	 * Sets the DXNumber
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setDXNumber(String old) {
		String newDXNumber = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseDX";
		this.dxNumber = newDXNumber;
	}

	/**
	 * Sets the court code
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setCourtCode(String old) {
		String newShortCode = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseCode";
		this.courtCode = newShortCode;
	}

	// SCR 52933 & 52926 - End

	/**
	 * Set the telephone
	 * 
	 * @param old
	 *            the old reference
	 */
	public void setTelephone(String old) {
		String newTelephone = old.substring(0, old.lastIndexOf("/")) + "/ord:CourtHouseTelephone";
		this.telephone = newTelephone;

	}

	/**
	 * Return the xpath reference
	 * 
	 * @return the reference
	 */
	public String getRef() {
		return this.ref;
	}

	// SCR 52933 & 52926 - Start
	/**
	 * Return the short code
	 * 
	 * @return the short code
	 */
	public String getShortCode() {
		return this.courtCode;
	}

	/**
	 * Return the short name
	 * 
	 * @return the short name
	 */
	public String getShortName() {
		return this.shortName;
	}

	// SCR 52933 & 52926 - End

	/**
	 * Return the short name
	 * 
	 * @return the short name
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Return xpath to first address line
	 * 
	 * @return addressline1
	 */
	public String getAddressLine1() {
		return this.addressLine1;
	}

	/**
	 * Return xpath to second address line
	 * 
	 * @return addressline2
	 */
	public String getAddressLine2() {
		return this.addressLine2;
	}

	/**
	 * Return xpath to third address line
	 * 
	 * @return addressline3
	 */
	public String getAddressLine3() {
		return this.addressLine3;
	}

	/**
	 * Return xpath to fourth address line
	 * 
	 * @return addressline4
	 */
	public String getAddressLine4() {
		return this.addressLine4;
	}

	/**
	 * Return xpath to fifth address line
	 * 
	 * @return addressline5
	 */
	public String getAddressLine5() {
		return this.addressLine5;
	}

	/**
	 * Return xpath to postcode
	 * 
	 * @return postcode
	 */
	public String getPostCode() {
		return this.postCode;
	}

	/**
	 * Return xpath to telephone
	 */
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * Return xpath to dxNumber
	 */
	public String getDXNumber() {
		return this.dxNumber;
	}

	/**
	 * Return the agent
	 * 
	 * @return the agent
	 */
	protected ComboBoxAgent getAgent() {
		return agent;
	}

	/**
	 * Return the list of courts
	 * 
	 * @return the list
	 */
	protected String[] getList() {
		return list;
	}

	/**
	 * Return the combo box
	 * 
	 * @return the combo box
	 */
	protected JComboBox getBoxCb() {
		return boxCb;
	}

	/**
	 * Return the combo box utility
	 * 
	 * @return the combo box utility
	 */
	protected ComboBoxUtility getUtil() {
		return util;
	}
}
