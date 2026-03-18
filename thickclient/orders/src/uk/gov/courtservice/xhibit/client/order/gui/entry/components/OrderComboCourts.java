package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxAgent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListFactory;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtOption;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CrownCourtOption;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.MultipleCourtListPanel;

/**
 * <p>
 * Title: OrderComboCourts. A class that displays court radio buttons and a
 * court combobox list.
 * </p>
 * <p>
 * Description: This class implements the CourtOption class to create a
 * JRadioButton component for the Magistrate Court, Crown Court and Youth Court,
 * court lists are displayed on a JComboBox component and is created by
 * instantiating the CustomComboBox class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */
public class OrderComboCourts extends AbstractOrderComponent implements ItemListener {

	
    // Default Court Name as held on blank schema
    private static final String DEFAULT_COURT_NAME = "String";

    private static final Logger log = CSServices.getLogger(OrderComboCourts.class);

    private static final String REG_EXP_POSTCODE = "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
            + "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";

    private static final String REG_EXP_TELEPHONE = "[0-9 \\-]{1,20}";

    // Component that creates a JPanel
    private MultipleCourtListPanel listPanel;
    
    /**
     * Component that creates a court list by calling the Court List Interface,
     * which methods are implemented in class CourtListMidTier. This class
     * retrieves court list data that relates to the Magistrates, Crown and
     * Youth courts.
     */
    private CourtList list = CourtListFactory.createCourtList();

    private ComboBoxAgent agent;

    private CustomComboBox boxCb;

    private JLabel courtName = new JLabel("Court Name:");
    
    
    private String dxRefNum  = "";
    
    private CourtOption opt = null;

    /**
     * Create a JPanel with court list components. This class instantiates the
     * CourtOption which provides 3 instances containing a court list, combobox
     * and a string label. In adition a number of dataentrytemplate xml
     * attribute values are retrieved.
     * 
     * @throws OrderComponentException
     */
    public void initComponent() throws OrderComponentException {
        String storedCourt = getHelper().getValue();
        boxCb = new CustomComboBox(list);
        agent = new ComboBoxAgent(boxCb);
        
       
        listPanel = new MultipleCourtListPanel(new CrownCourtOption(list.getCourtValues(), boxCb, "Crown Court", agent,
                getHelper().getAttribute("ref"), getHelper()), new CourtOption(list.getMagistrateNames(), boxCb,
                "Magistrates Court", agent, getHelper().getAttribute("ref"), getHelper()), new CourtOption(list
                .getYouthValues(), boxCb, "Youth Court", agent, getHelper().getAttribute("ref"), getHelper()), boxCb,
                getDefaultConstraints(), getHelper().getAttribute("crowncourt"), getHelper().getAttribute(
                        "magistratecourt"), getHelper().getAttribute("youthcourt"), getHelper()
                        .getAttribute("crownref"), getHelper().getAttribute("magistratesref"), getHelper()
                        .getAttribute("youthref"), getHelper().getAttribute("ordertypes"), getHelper().getAttribute(
                        "courtlabel"));

        // If court name retrieved from xml is not default value, set the
        // combobox to the court
        if (storedCourt != null) {
	        if (storedCourt.equalsIgnoreCase(DEFAULT_COURT_NAME) == false) {
	        	 // Set the D20 Court Type
	            if(isD20Order()) {
	            	if (Arrays.asList(list.getMagistrateNames()).contains(storedCourt)) {
	            		listPanel.setSelectedCourtType(MultipleCourtListPanel.MAGISTRATE);
	            	} else if (Arrays.asList(list.getYouthValues()).contains(storedCourt)) {
	            		listPanel.setSelectedCourtType(MultipleCourtListPanel.YOUTH);
	            	}
	            }
	            boxCb.setSelectedItem(storedCourt);
	        }
        }

        if (boxCb != null) {
            boxCb.setEditable(false);
        }
        
        
        this.setVisualComponent(listPanel);
        listPanel.getboxCb().addItemListener(this);

        // S.Bachra 16/5/03 Tracker 53183 Set the text when loaded for first
        // time
        String text = ((JTextField) listPanel.getboxCb().getEditor().getEditorComponent()).getText();
        getHelper().setValue(text);

        // S. Bachra 27/5/03 SCR 52933 & 52926 - Setting ShortName and CrestCode
        // for the Initially selected Court
        opt = listPanel.getSelectedCourt();
        XhbRefCourtValue court = (XhbRefCourtValue) list.getCourt(text.trim());
        
        getHelper().setValues(opt.getShortName().trim(), court.getCourtShortName().trim());
        getHelper().setValues(opt.getShortCode(), court.getCrestCode());
      
        if (getHelper().getAttribute("dxRequired") != null && getHelper().getAttribute("dxRequired") != "") {
        if (boxCb != null) {
     			this.boxCb.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					JComboBox courtCombo = (JComboBox) e.getSource();
    					if (courtCombo != null && courtCombo.getSelectedItem() != null) {
    						dxRefNum = getCourtRef(courtCombo.getSelectedItem().toString());
     					    setDXNumber(opt, dxRefNum);
      					}
    				}
    			});
    		}
        
        
	     getHelper().setValues(opt.getDXNumber(), dxRefNum);
        }
			
        
        // Set the court type correctly initially (i.e. not dependent what is in blank xml; actually set it according to what is selected)
        if ((opt.getRef() != null) && (opt.getType() != null)) {
            getHelper().setValues(opt.getRef(), opt.getType().trim());        	
        }
        
       
        // check to see if address needs to be populated
        updateAddress(court, opt);
        // check to see if telephone needs to be populated
        updateTelephone(court, opt);
        // check to see if dxNumber needs to be populated
        updateDXNumber(court, opt);
    }

    
   
    public String getCourtRef(String courtName) {
    	XhbRefCourtValue court = (XhbRefCourtValue)this.list.getCourt(courtName);
    	return court.getDxRef();
    }
    /**
     * Is the component labelled
     * 
     * @return
     */
    public boolean isLabelled() {
        return false;
    }

    /**
     * Changes the value of the court list.
     * 
     * @param event
     *            Invoked when an item has been selected or deselected.
     * 
     */
    public void itemStateChanged(ItemEvent event) {
        updateValues(event);
    }

    /**
     * Enable combo box and court name label
     * 
     * @param enabled
     *            Enables the combo box so that items can be selected.
     */
    public void setEnabled(boolean enabled) {
       
    	if(isInputLocked())
    		enabled = false;
       
    	super.setEnabled(enabled);
    	boxCb.setEnabled(enabled);
        this.courtName.setEnabled(enabled);
        listPanel.setEnabled(enabled);
        
        
    }

    /**
     * Updates the court list item when the user changes the court name, also
     * updates xml value that is diplayed in the preview pane.
     */
    private void updateValues(ItemEvent ie) {

        log.debug("UPDATE COURTS");
        CourtOption opt = listPanel.getSelectedCourt();

        log.debug("LIST PANEL SELECTED COURT : " + listPanel.getSelectedCourt());
        //String text = ((JTextField) listPanel.getboxCb().getEditor().getEditorComponent()).getText();
        String text = ((Object)ie.getItem()).toString();
        log.debug("TEXT : " + text);

        // SCR 52933 & 52926 - Start
        XhbRefCourtValue court = (XhbRefCourtValue) list.getCourt(text);

        if (text.equals(null)) {
            log.debug("The text value is **** NULL *** ");
        } else {
            if (court != null) {

                // concat the crest code onto the end off the courtname
                getHelper().setValue((court.getCourtFullName() + " (" + court.getCrestCode() + ")").trim());
                getHelper().setValues(opt.getRef(), opt.getType().trim());

                getHelper().setValues(opt.getShortName(), court.getCourtShortName().trim());
                getHelper().setValues(opt.getShortCode(), court.getCrestCode().trim());

                updateAddress(court, opt); // call helper to update address
                // if required

                updateTelephone(court, opt); // call helper to update
                // telephone if required

                getHelper().getValue(opt.getRef());
            }
        }
        // SCR 52933 & 52926 - End
    }

    /**
     * Check to see if the address needs populating
     */
    private void updateAddress(XhbRefCourtValue court, CourtOption option) {
        String addressRequired = getHelper().getAttribute("addressReq");
        if (addressRequired != null && addressRequired.equals("true")) {
            log.debug("Update address in the xml");
            // populate the address in xml
            setCourtAddress(court, option);
        }
    }

    /**
     * Check to see if the telephone needs populating
     */
    private void updateTelephone(XhbRefCourtValue court, CourtOption option) {
        String telephoneRequired = getHelper().getAttribute("telephoneReq");
        if (telephoneRequired != null && telephoneRequired != "") {
            log.debug("Update telephone in the xml");
            setTelephone(court, option);
        }

    }
    
    /**
     * Check to see if the DXNumber needs populating
     */
    public void updateDXNumber(XhbRefCourtValue court, CourtOption option) {
        String dxRequired = getHelper().getAttribute("dxRequired");
        if (dxRequired != null && dxRequired != "") {
            log.debug("Update DXNumber in the xml");
            setDXNumber(court, option);
        }
    }
  
    private void setDXNumber(XhbRefCourtValue court, CourtOption option) {
        // set default dxNumber
        getHelper().setValues(option.getDXNumber(), "-");
        String dxNumber=court.getDxRef();
        
        log.debug("DX Number >> " + dxNumber);
        // only set if has a value
        if (dxNumber != null && dxNumber != "") {
            getHelper().setValues(option.getDXNumber(), dxNumber);
        }
    }

    /**
     * Address Helper used to set up the address in the xml
     * 
     * @param court
     *            court value holding the addresss
     */
    private void setCourtAddress(XhbRefCourtValue court, CourtOption option) {

        // get address details for the court
        String add1 = court.getXhbAddress().getAddress1();
        String add2 = court.getXhbAddress().getAddress2();
        String add3 = court.getXhbAddress().getAddress3();
        String add4 = court.getXhbAddress().getAddress4();
        String town = court.getXhbAddress().getTown();
        String county = court.getXhbAddress().getCounty();
        String postcode = court.getXhbAddress().getPostcode();

        // set default values in the xml
        getHelper().setValues(option.getAddressLine1(), "-");
        getHelper().setValues(option.getAddressLine2(), "-");
        getHelper().setValues(option.getAddressLine3(), "-");
        getHelper().setValues(option.getAddressLine4(), "-");
        getHelper().setValues(option.getAddressLine5(), "-");
        getHelper().setValues(option.getPostCode(), "A1 1AA"); // default to
        // ensure not
        // invalidate
        // the xml

        // set address line[1]
        if (add1 != null && !add1.equals("")) {
            log.debug("AddressLine1 Added");
            getHelper().setValues(option.getAddressLine1(), add1);
        }

        // set address line[2]
        if (add2 != null && !add2.equals("")) {
            log.debug("AddressLine2 Added");
            getHelper().setValues(option.getAddressLine2(), add2);
        }

        // set address line[3]
        if (add3 != null && !add3.equals("")) {
            log.debug("AddressLine3 Added");
            getHelper().setValues(option.getAddressLine3(), add3);
        }

        // set address line[4]
        if (add4 != null && !add4.equals("")) {
            log.debug("AddressLine4 Added");
            getHelper().setValues(option.getAddressLine4(), add4);
        }

        // buffer to hold town & county
        StringBuffer buffer = new StringBuffer();

        // check status of town and county
        boolean townExists = false;
        boolean countyExists = false;

        // set boolean value according to town value
        if (town != null && !town.equals("")) {
            townExists = true;
        }

        // set boolean value according to county value
        if (county != null && !county.equals("")) {
            countyExists = true;
        }

        // set address line[5] - concat the town and county
        if (townExists && countyExists) {
            // if both the town and county exists - concat both with comma
            // between
            log.debug("AddressLine5 Town & County");
            buffer.append(town);
            buffer.append(", ");
            buffer.append(county);
        } else {
            if (townExists) {
                log.debug("AddressLine5 Town");
                buffer.append(town);
            }

            if (countyExists) {
                log.debug("AddressLine5 County");
                buffer.append(county);
            }
        }

        // check to see if anything within the buffer
        if (buffer.length() > 0) {
            getHelper().setValues(option.getAddressLine5(), buffer.toString());
        }

        // set the postcode
        if (postcode != null || !postcode.equals("")) {
            try {
                RE regexp = new RE(REG_EXP_POSTCODE);
                boolean matched = false;
                matched = regexp.match(postcode);
                if (matched) {
                    // the postcode for the court matches the REGEXP
                    log.debug("PostCode Added");
                    getHelper().setValues(option.getPostCode(), postcode);
                }
            } catch (RESyntaxException e) {
                // do nothing as the default postcode has already been set
            }
        }
    }

    /**
     * Telephone helper to set the telephone value in the xml
     */
    private void setTelephone(XhbRefCourtValue court, CourtOption option) {
        // set default telephone
        getHelper().setValues(option.getTelephone(), "-");
        Integer addressId = court.getAddressId();
        String telephone="";
        	try {
        	     telephone = OrdersReferenceControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                .getTelephoneForAddress(addressId);
        	} catch (Exception exp) {
        		exp.printStackTrace();
        	   }
        log.debug("TELEPHONE FROM DELEGATE >> " + telephone);
        // only set if has a value
        if (telephone != null && telephone != "") {
            getHelper().setValues(option.getTelephone(), telephone);
        }
    }
    

    /**
     * DXNumber helper to set the DXNumber value in the xml
     */
    private void setDXNumber(CourtOption opt, String dxNumber) {
        
        log.debug("DX Number >> " + dxNumber);
        // only set if has a value
        if (dxNumber != null && dxNumber != "") {
            getHelper().setValues(opt.getDXNumber(), dxNumber);
        } else {
        	getHelper().setValues(opt.getDXNumber(), "");
        }
    }
}