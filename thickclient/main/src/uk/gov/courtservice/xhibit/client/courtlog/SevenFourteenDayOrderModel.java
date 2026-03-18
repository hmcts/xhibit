package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class SevenFourteenDayOrderModel extends FreeTextModel {

    private String sevenFourteenRadio;

    private Calendar orderDate;

    public SevenFourteenDayOrderModel() {
        super();
    }

    public String getSevenFourteenRadio() {
        return sevenFourteenRadio;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setSevenFourteenRadio(String code) {
        this.sevenFourteenRadio = code;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public void printModel() {
        super.printModel();

        XHIBITConstant.info("SevenFourteenDayOrderModel");
        XHIBITConstant.info("----------------------------");
        XHIBITConstant.info("Seven/Fourteen? : " + getSevenFourteenRadio());
        XHIBITConstant.info("Order Date      : " + getOrderDate());
    }

    // public Object clone( ) {
    // SevenFourteenDayOrderModel copy = new SevenFourteenDayOrderModel( );
    //
    // copy.setOrderDate( this.getOrderDate( ) );
    // copy.setSevenFourteenRadio( this.getSevenFourteenRadio( ) );
    //
    // return copy;
    // }

    public void clearmodel() {
        setSevenFourteenRadio(null);
        setOrderDate(null);
    }
}
