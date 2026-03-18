package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.Insets;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: PHConstants
 * </p>
 * <p>
 * Description: Constants for the preliminary hearing events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class PHConstants {
    public static final Integer CASE_CALLED_ON = new Integer(10100);

    public static final Integer INDICTMENT_BY = new Integer(20501);

    public static final Insets INSETS = new Insets(1, 2, 1, 2);

    public static final String INDICTMENT_BY_SHORT_DESC = XHIBITConstant.getResource(XhibitBundles.PreliminaryHearing,
            "lblPanelText");

    public static final String INDICTMENT_BY_SCHEMA = "E20501_Indictment_By_Date";

    private PHConstants() {
    }
}