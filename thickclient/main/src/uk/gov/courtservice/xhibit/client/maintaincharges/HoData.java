package uk.gov.courtservice.xhibit.client.maintaincharges;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class HoData {
    String hoCode;

    String hoDescription;

    public HoData() {

    }

    public HoData(String hoCode, String hoDescription) {
        this.hoCode = hoCode;
        this.hoDescription = hoDescription;
    }

    public String getHoCode() {
        return hoCode;
    }

    public String getHoDescription() {
        return hoDescription;
    }
}