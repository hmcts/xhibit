package uk.gov.courtservice.xhibit.business.services.charge;

/**
 * <p>
 * Title:
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
 * @author Sarah Tong
 * @version $Id: CourtLogEvents.java,v 1.7 2010/02/19 11:14:36 pokalak Exp $
 */
public interface CourtLogEvents {
    public static final Integer ADD_COUNT = new Integer(40100);

    public static final Integer ADD_COUNT_TO_DEFENDANT = new Integer(40101);

    public static final Integer AMEND_COUNT = new Integer(40102);

    public static final Integer DELETE_COUNT = new Integer(40103);

    public static final Integer DELETE_RESULTS_FOR_COUNT = new Integer(21001);

    public static final Integer ADD_INDICTMENT = new Integer(40200);

    public static final Integer DELETE_INDICTMENT = new Integer(40201);

    public static final Integer AMEND_DEFENDANT = new Integer(40300);

    public static final Integer ADD_BREACH = new Integer(40400);

    public static final Integer REMOVE_BREACH = new Integer(40401);

    public static final Integer AMEND_BREACH = new Integer(40402);

    public static final Integer DELETE_BREACH = new Integer(40403);

    public static final Integer ADD_OFFENCE = new Integer(40500);

    public static final Integer AMEND_OFFENCE = new Integer(40501);

    public static final Integer DELETE_OFFENCE = new Integer(40502);

    public static final Integer SIGN_INDICTMENT = new Integer(40202);

    public static final Integer EXPORT_IND = new Integer(40410);
    
    public static final Integer REMOVE_DEFENDANT_ON_COUNT = new Integer(40475);
    public static final Integer RENUMBER_COUNTS = new Integer(40476);
    public static final Integer ADD_COUNT_TO_JOINDER = new Integer(40477);
}