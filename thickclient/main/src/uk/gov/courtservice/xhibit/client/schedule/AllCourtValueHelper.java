package uk.gov.courtservice.xhibit.client.schedule;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class AllCourtValueHelper {

    private String allCourtText = "All Courts";

    public AllCourtValueHelper() {
        // get text from resource bundle
        ResourceBundle rb = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
        allCourtText = rb.getString("allCourtText");
    }

    public String toString() {
        return allCourtText;
    }
}