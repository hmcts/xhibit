package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: AllCourtValueHelper
 * </p>
 * <p>
 * Description: This class represents the root node of the Court Tree in the Unauthorised Case Status panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: James Powell
 * </p>
 * 
 * @author James Powell
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