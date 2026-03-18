package uk.gov.courtservice.xhibit.business.services.charge;

/**
 * <p>
 * Title: UncodedOffenceInterface
 * </p>
 * <p>
 * Description: Common variables used with Uncoded Offences
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedOffenceInterface.java,v 1.4 2005/01/06 14:50:56 xztnfq
 *          Exp $
 */
public interface UncodedOffenceInterface {
    /**
     * The reserved offence code for uncoded offences
     */
    public static final String UNCODED_OFFENCE_REFERENCE_CODE = "ZZ99999";

    /**
     * Home Office Class Default Value (excluding Indictments and Counts)
     */
    public static final String DEFAULT_CLASS = "195";

    /**
     * Home Office Subclass Default Value (excluding Indictments and Counts)
     */
    public static final String DEFAULT_SUBCLASS = "99";

    /**
     * Home Office Class Default Value (Indictments and Counts)
     */
    public static final String DEFAULT_COUNT_CLASS = "099";

    /**
     * Home Office Subclass Default Value (Indictments and Counts)
     */
    public static final String DEFAULT_COUNT_SUBCLASS = "99";

}
