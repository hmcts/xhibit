package uk.gov.courtservice.xhibit.client.order.screens.model;

/**
 * <p>Title: Xhibit2: OrderDataModel</p>
 * <p>Description: Holds the current state of the order.
 * Passed to AbstractOrderPanel to set data on creation</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Neil Entwistle
 * @version 1.0
 */

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValidationReturnValue;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderDefendantWrapper;

public class OrderInitialDataVO {

    private static final Logger log = CSServices.getLogger(OrderInitialDataVO.class);

    /**
     * The mode to Create orders
     */
    public static final int CREATE_MODE = 0;

    /**
     * The mode to View orders
     */
    public static final int VIEW_MODE = 1;

    /**
     * The mode to Replace orders
     */
    public static final int REPLACE_MODE = 2;
    
    /**
     * The mode to Replace orders
     */
    public static final int COPY_MODE = 3;

    // Stores the case id from the order screens
    private String caseID;
    
    private Integer scheduledHearingId;
    
    private Integer xhibitCaseId;
    
    private int hearingID;

    // Stores the court id from the order screens
    private int crestCourtID; // Defined historically as the crest court id!
    
    // The XHIBIT court id
    private int xhibitCourtId;

    // Stores the court name from the order screens
    private String courtName;

    // Stores the defendant name from the order screens
    private String deftName;

    // Stores the defendant id from the order screens
    private Integer defendantID;
    
    // Stores the defendant on case id from the order screens
    private Integer defendantOnCaseID;

    // Stores the disposal ids from the order screens
    private int[] dispID;

    // Stores the order type from the order screens
    private String orderType;
    
    //	Stores whether the D20 orser is interim (Y) or Full (N)
    private String d20Interim = "N";

    // Stores the order id from the order screens
    private Integer orderID;

    // Stores the selected order from the table
    private Integer selectedOrder = null;

    // Stores the state of the order
    private String orderState;

    // Stores the saved details from the Saved Panel
    private String savedDetails;

    // Stores the date the order was signed from the signed panel
    private String signedDate;

    // Stores the time the order was signed from the signed panel
    private String signedTime;

    // Stores the signatory of the order from the signed panel
    private String signedBy;

    // Stores the Finished indicator to differentiate between that and
    // cancel
    private boolean finished = false;

    // Stores the create/view order indicator
    private String createview = null;

    // Stores the mode to open up the wizard - create(0) or view(1)
    private int mode = 0;

    // Stores the flag to create a new order from the order list screen
    // defaults to false
    private boolean createOrder = false;
    
    // Stores the flag to copy an exiting order from the order list screen
    // defaults to false
    private boolean copyOrder = false;

    // Stores a reference to the helper for the model
    private OrderInitialDataHelper helper;
    
    private boolean isMonetaryOrder = false;
    
    private boolean isD20Order = false;
    
    private boolean isBCase = false;
    
    private boolean isACase =  false;
    
    private boolean isTCase = false;
    
    private boolean isSCase=false;
    
    private String caseTitle;
    
    private String D20State = "";
    
    private boolean hasFailed = false;
    
    private OrderOffenceModel offenceData;
    
    private OffenceValidationReturnValue offenceValidationReturnValue;

	private String caseSubType;

    // getters

    public OffenceValidationReturnValue getOffenceValidationReturnValue() {
		return offenceValidationReturnValue;
	}

	public void setOffenceValidationReturnValue(OffenceValidationReturnValue offenceValidationReturnValue) {
		this.offenceValidationReturnValue = offenceValidationReturnValue;
	}

	public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    /**
     * Return the case id
     * 
     * @return the caseid
     */
    public String getCaseID() {
        return caseID;
    }

    /**
     * Get te scheduled hearing id.
     * 
     * @return The scheduledhearingId
     */
    public Integer getScheduledHearingId(){
    	return scheduledHearingId;
    }
    
    /**
     * Returns the CourtID
     * 
     * @return String courtID
     */
    public int getCrestCourtID() {
        return crestCourtID;
    }

    /**
     * Returns the CourtName
     * 
     * @return String courtName
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * Returns the Defendant Name
     * 
     * @return String deftName
     */
    public String getDefendantName() {
        return deftName;
    }

    /**
     * Returns the DefendantID
     * 
     * @return int defendantID
     */
    public Integer getDefendantID() {
        return defendantID;
    }

    /**
     * Returns the DisposalID
     * 
     * @return int[] dispID
     */
    public int[] getDisposalID() {
        return dispID;
    }

    /**
     * Returns the Order Type
     * 
     * @return String orderType
     */
    public String getOrderType() {
        return orderType;
    }

    public String getd20Interim() {
    	return d20Interim;
    }
    /**
     * Returns the OrderID
     * 
     * @return int orderID
     */
    public Integer getOrderTypeID() {
        return orderID;
    }

    /**
     * Returns the current signed state of the order
     * 
     * @return signed state
     */
    public String getOrderState() {
        return orderState;
    }

    /**
     * Returns the current mode of the wizard. Create(0) or View(1)
     * 
     * @return mode
     */
    public int getMode() {
        return mode;
    }

    // setters

    /**
     * Sets the case id
     * 
     * @param caseID
     *            as retrieved from the framework
     */
    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

	public void setScheduledHearingId(Integer scheduledHearingId) {
		this.scheduledHearingId = scheduledHearingId;		
	}

    /**
     * Sets the court id
     * 
     * @param courtID
     *            as retrieved from the framework
     */
    public void setCrestCourtID(int crestCourtID) {
        this.crestCourtID = crestCourtID;
    }

    /**
     * Sets the court name
     * 
     * @param courtName
     *            as retrieved from the framework
     */
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    /**
     * Sets the defendant name as retrieved from the screens
     * 
     * @param defendantID
     *            Unique identifier for the defendant
     */
    public void setDefendantID(int defendantID) {
        log.debug("ORDERS***: OrderInitialDataVO: setDefendantID: " + defendantID);
        this.defendantID = new Integer(defendantID);
    }

    /**
     * Sets the defendant name as retrieved from the screens
     * 
     * @param defendantID
     *            Unique identifier for the defendant
     */
    public void setDefendantID(Integer defendantID) {
        log.debug("ORDERS***: OrderInitialDataVO: setDefendantID: " + defendantID);
        this.defendantID = defendantID;
    }

    /**
     * Sets the defendant name as retrieved from the screens
     * 
     * @param defendantName
     *            Name of defendant
     */
    public void setDefendantName(String defendantName) {
        log.debug("SetDefendantName: " + defendantName);
        deftName = defendantName;
        // If possible, set the defendant id or unset it if necessary too
        if (defendantName != null && defendantName.trim().length()==0) {
            setDefendantID(null);
        } else if (this.helper.getDefendants() != null) {
        	OrderDefendantWrapper[] wrapper = this.helper.getDefendants();
        	
            for (int i=0; i<wrapper.length; i++) {
                OrderDefendantWrapper odw = wrapper[i];
                String firstName = "";
                String middleName = "";
                String surname = "";
                if (odw.getDefendant() != null) { 
                    if (odw.getDefendant().getSurname() != null) {
                        surname = odw.getDefendant().getSurname().trim();
                    }
                    if (odw.getDefendant().getMiddleName() != null) {
                        middleName = odw.getDefendant().getMiddleName().trim();
                    }
                    if (odw.getDefendant().getFirstName() != null) {
                        firstName = odw.getDefendant().getFirstName().trim();
                    }
                }
                StringBuffer odwNameBuf = new StringBuffer();
                if (surname!=null) {
                    odwNameBuf.append(surname);
                }
                if (firstName!=null && !firstName.isEmpty()) {
                    odwNameBuf.append(", "+firstName);
                }
                if (middleName!=null && !middleName.isEmpty()) {
                    odwNameBuf.append(" "+middleName);
                }
                
                String odwName = odwNameBuf.toString().trim();
                log.debug("OrderInitialDataVO: setDefendantName: odwName="+odwName+" and defendantName="+defendantName);
                // Compare the name sent in with the one retrieved from the list with all spaces removed to deal with 
                // leading/trailing spaces in the defendant name. Trim alone is not enough, all spaces need to be removed
                if (defendantName.replaceAll("\\s","").equals(odwName.replaceAll("\\s",""))) {
                    setDefendantID(odw.getDefendant().getId());
                    break;
                }
            }
        }
    }

    /**
     * Sets the disposal ID(s) from the Business Delegates
     * 
     * @param disposalID
     *            Collection of disposal ids
     */
    public void setDisposalID(int[] disposalID) {
        this.dispID = disposalID;
    }

    /**
     * Sets the Order ID from the Business Delegates
     * 
     * @param orderID
     *            Unique identifier for order
     */
    private void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    /**
     * Sets the selected order type from the screens
     * 
     * @param orderType
     *            ID of templated order
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * Sets the D20 interim flag (Y - interim, N - full)
     * 
     * @param d20Interim			The new interim flag
     */
    public void setD20Interim( String d20Interim ) {
    	this.d20Interim = d20Interim;
    }
    
    /**
     * Sets the selected order type from the screens
     * 
     * @param orderType
     *            ID of templated order
     */
    public void setOrderID(String orderType) {
        log.debug("$$$ OrderInitialDataVO.setOrderID " + orderType);
        if (null != orderType && !orderType.trim().equals("")) {
            setOrderID(getHelper().getOrderType(orderType).getOrderTypeId());
        } else {
            orderID = null;
        }
    }

    /**
     * Sets the saved details from the saved screen
     * 
     * @param desc
     *            Description of the saved order
     */
    public void setSavedDetails(String desc) {
        log.debug("Updating model " + desc);
        this.savedDetails = desc;
    }

    /**
     * Sets the date the order was signed from the screens
     * 
     * @param date
     *            Date the order was signed
     */
    public void setSignedDate(String date) {
        this.signedDate = date;
    }

    /**
     * Sets the time the order was signed from the screens
     * 
     * @param time
     *            Time the order was signed
     */
    public void setSignedTime(String time) {
        this.signedTime = time;
    }

    /**
     * Sets the signatory of the order from the screens
     * 
     * @param sign
     *            Signatory of the order
     */
    public void setSignedBy(String sign) {
        this.signedBy = sign;
    }

    /**
     * Sets the indicator to determine if the finish button was pressed.
     * 
     * @param fin
     */
    public void setFinished(boolean fin) {
        finished = fin;
    }

    /**
     * Returns the current state of the finished indicator
     * 
     * @return
     */
    public boolean getFinished() {
        return finished;
    }

    /**
     * Returns the current state of teh create order indicator
     * 
     * @return true if create
     */
    public boolean isCreateOrder() {
        return createOrder;
    }

    /**
     * Sets the choice of creating a new order or viewing existing orders
     * 
     * @param choice
     *            ActionCommand from the selected JRadioButton
     */
    public void setCVOption(String choice) {
        createview = choice;
    }

    /**
     * Return choice of creating a new order of the selected type or list all
     * existing orders
     * 
     * @return String
     */
    public String getCVROption() {
        return createview;
    }

    /**
     * Sets the current mode of the wizard Create(0) or View(1)
     * 
     * @param wizMode
     *            Create(0) or View(1)
     */
    public void setMode(int wizMode) {
        mode = wizMode;
    }

    /**
     * Sets the flag to indicate if an order is to be created as set on the
     * order list screen. Defaults to false
     * 
     * @param flag
     *            Defaults to false. Set to true from OrderListPanel
     */
    public void setCreateOrder(boolean flag) {
        createOrder = flag;
    }

    /**
     * Set the selected order type
     * 
     * @param ordId
     *            the id of the order type
     */
    public void setSelectedOrder(Integer ordId) {
        selectedOrder = ordId;
    }

    /**
     * Return the selected order tyep
     * 
     * @return the order type id
     * @throws OrderNotSelectedException
     */
    public Integer getSelectedOrder() throws OrderNotSelectedException {
        if (selectedOrder == null) {
            throw new OrderNotSelectedException("No Orders selected from list");
        } else {
            return selectedOrder;
        }
    }

    /**
     * Sets the state of the order
     * 
     * @param s
     *            the state
     */
    public void setOrderState(String s) {
        orderState = s;
    }

    /**
     * Sets the helper on the model
     * 
     * @param hlpr
     */
    public void setHelper(OrderInitialDataHelper hlpr) {
        this.helper = hlpr;
    }

    /**
     * Returns the helper
     * 
     * @return
     */
    public OrderInitialDataHelper getHelper() {
        return helper;
    }

    public boolean isCopyOrder() {
        return copyOrder;
    }

    public void setCopyOrder(boolean copyOrder) {
        this.copyOrder = copyOrder;
    }

    public boolean isMonetaryOrder() {
        return isMonetaryOrder;
    }

    public void setMonetaryOrder(boolean isMonetaryOrder) {
        this.isMonetaryOrder = isMonetaryOrder;
    }

    public int getXhibitCourtId() {
        return xhibitCourtId;
    }

    public void setXhibitCourtId(int xhibitCourtId) {
        this.xhibitCourtId = xhibitCourtId;
    }

    public boolean isD20Order() {
        return isD20Order;
    }

    public void setD20Order(boolean isD20Order) {
        this.isD20Order = isD20Order;
    }

    public boolean isBCase() {
        return isBCase;
    }

    public void setBCase(boolean isBCase) {
        this.isBCase = isBCase;
    }

    public Integer getXhibitCaseId() {
        return xhibitCaseId;
    }

    public void setXhibitCaseId(Integer xhibitCaseId) {
        this.xhibitCaseId = xhibitCaseId;
    }

    public Integer getDefendantOnCaseID() {
        return defendantOnCaseID;
    }

    public void setDefendantOnCaseID(Integer defendantOnCaseID) {
        this.defendantOnCaseID = defendantOnCaseID;
    }

	public String getD20State() {
		return D20State;
	}

	public void setD20State(String d20State) {
		D20State = d20State;
	}

	public boolean isACase() {
		return isACase;
	}

	public void setACase(boolean isACase) {
		this.isACase = isACase;
	}

	public int getHearingID() {
		return hearingID;
	}

	public void setHearingID(int hearingID) {
		this.hearingID = hearingID;
	}

	public boolean isHasFailed() {
		return hasFailed;
	}

	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}

	public OrderOffenceModel getOffenceData() {
		return offenceData;
	}

	public void setOffenceData(OrderOffenceModel offenceData) {
		this.offenceData = offenceData;
	}

	public boolean isTCase() {
		return isTCase;
	}

	public void setTCase(boolean isTCase) {
		this.isTCase = isTCase;
	}

	public boolean isSCase() {
		return isSCase;
	}

	public void setSCase(boolean isSCase) {
		this.isSCase = isSCase;
	}

	public String getCaseSubType(){
		return caseSubType;
	}
	
	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}

}