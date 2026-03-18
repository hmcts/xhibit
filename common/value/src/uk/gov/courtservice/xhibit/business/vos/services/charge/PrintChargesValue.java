package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PrintChargesValue
 * </p>
 * <p>
 * Description: Holds the data for printing charges information
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class PrintChargesValue extends CSAbstractValue {
	
	static final long serialVersionUID = -6308680979062770260L;
	
    private ChargeCompositeValue chargeData;

    private Collection chargeLogEvents;

    private String headerLine1;

    private String headerLine2;

    private String headerLine3;

    private Integer printSelection;

    /**
     * 
     */
    public PrintChargesValue() {
    }

    /**
     * 
     * @param chargeData
     */
    public PrintChargesValue(ChargeCompositeValue chargeData) {
        this.chargeData = chargeData;
    }

    /**
     * 
     * @param chargeData
     * @param header1
     *            line one of header or null
     * @param header2
     *            line two of header or null
     * @param header3
     *            line three of header or null
     * @param printSelection
     *            what to print
     */
    public PrintChargesValue(ChargeCompositeValue chargeData, String header1, String header2, String header3,
            Integer printSelection) {
        this(chargeData);
        if (header1 != null && !header1.trim().equals(""))
            this.headerLine1 = header1;
        if (header2 != null && !header2.trim().equals(""))
            this.headerLine2 = header2;
        if (header3 != null && !header3.trim().equals(""))
            this.headerLine3 = header3;
        this.printSelection = printSelection;
    }

    /**
     * 
     * @return header line1
     */
    public String getHeaderLine1() {
        return this.headerLine1;
    }

    /**
     * 
     * @return header line 2
     */
    public String getHeaderLine2() {
        return this.headerLine2;
    }

    /**
     * 
     * @return header line 3
     */
    public String getHeaderLine3() {
        return this.headerLine3;
    }

    /**
     * 
     * @return chargedata
     */
    public ChargeCompositeValue getChargeData() {
        return this.chargeData;
    }

    /**
     * 
     * @param chargeData
     */
    public void setChargeData(ChargeCompositeValue chargeData) {
        this.chargeData = chargeData;
    }

    /**
     * 
     * @param headerLine1
     */
    public void setHeaderLine1(String headerLine1) {
        this.headerLine1 = headerLine1;
    }

    /**
     * 
     * @param headerLine2
     */
    public void setHeaderLine2(String headerLine2) {
        this.headerLine2 = headerLine2;
    }

    /**
     * 
     * @param headerLine3
     */
    public void setHeaderLine3(String headerLine3) {
        this.headerLine3 = headerLine3;
    }

    /**
     * 
     * @param chargeLogEvents
     *            Collection of PrintChargeLogValues
     */
    public void setChargeLogEvents(Collection chargeLogEvents) {
        this.chargeLogEvents = chargeLogEvents;
    }

    /**
     * 
     * @return Collection of PrintChargeLogValues
     */
    public Collection getChargeLogEvents() {
        return this.chargeLogEvents;
    }

    /**
     * 
     * @param printSelection
     */
    public void setPrintSelection(Integer printSelection) {
        this.printSelection = printSelection;
    }

    /**
     * 
     * @return printSelection
     */
    public Integer getPrintSelection() {
        return this.printSelection;
    }

}