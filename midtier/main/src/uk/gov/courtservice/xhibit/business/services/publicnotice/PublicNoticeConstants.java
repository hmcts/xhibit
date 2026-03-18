package uk.gov.courtservice.xhibit.business.services.publicnotice;

/**
 * <p>
 * Title: Contains all the constants for Public notice Subsystem
 * </p>
 * <p>
 * Description: see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Pat Fox
 * @created 17 February 2003
 * @todo
 *          <p>
 * 
 * 
 */

public interface PublicNoticeConstants {

    public static final String PN_ACTIVE = "1";

    public static final String PN_INACTIVE = "0";

    public static final Integer REPORTING_RESTRICTIONS = new Integer(100);

    public static final Integer IN_CHAMBERS = new Integer(200);

    public static final Integer PUBLIC_REQUESTED_NOT_TO_ENTER = new Integer(300);

    public static final Integer TV_ENTER_QUIETLY = new Integer(400);

    public static final Integer VIDEO_ENTER_QUIETLY = new Integer(500);

    public static final Integer ENTER_QUIETLY = new Integer(600);

    public static final Integer REPORTING_RESTRICTIONS_LIFTED = new Integer(700);

    public static final Integer SWITCH_OFF_MOBILES = new Integer(800);

    public static final Integer NO_FOOD_AND_DRINK = new Integer(900);

    public static final Integer BENCH_WARRANT = new Integer(1000);

    // exception error keys
    static final String INVALID_PN_FOR_COURTROOM = "publicnotice.invalid_pn_for_courtroom";

    // Keys into Properties file for the XmlHelper
    static final String XML_BASE_PATH = "XML_BASE_PATH";

    static final String XML_CONFIG_FILE = "XML_CONFIG_FILE";

    // Max number of Notices that can be active(displayed at anytime)
    static final String MAX_NUMBER_ALLOWED_ACTIVE = "MAX_NUMBER_ALLOWED_ACTIVE";

    // Properties file for general public notice configuration
    static final String PROPERTIESFILENAME = "publicnotice";
}
