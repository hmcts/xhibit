package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.text.DecimalFormat;

/**
 * <p>
 * Title: Utility class intended to hold any orders xml binding specific
 * formatting.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * All formatting related variables and methods should be placed here providing
 * a common place for formatting related functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class FormatHelper {
    /**
     * Will format a number to be padded to 8 digits using '0's as necessary.
     */
    public static final DecimalFormat EIGHT_DIGIT = new DecimalFormat("00000000");
}