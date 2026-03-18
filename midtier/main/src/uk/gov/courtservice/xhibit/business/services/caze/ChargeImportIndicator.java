package uk.gov.courtservice.xhibit.business.services.caze;

/**
 * <p>
 * Title: ChargeImportIndicator
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
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public interface ChargeImportIndicator {
    public static final String NEW = "N";

    public static final String CLOSED = "C";

    public static final String SYNCHRONIZING = "S";

    public static final String PENDING = "P";

    public static final String OPEN = "O";

    public static final String LOAD_FAILED = "LF";

    public static final String RESYNCH_FAILED = "RF";

    public static final String LOCKED = "L";

    public static final String REMOVED = "R";

    public static final String PARTIALLY_LOADED = "PL";
}