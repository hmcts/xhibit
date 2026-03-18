package uk.gov.courtservice.xhibit.common.results.vos;

/**
 * Plea Event Constants.
 * 
 * @author tz0d5m
 * @version $Revision: 1.5 $
 */
public class PleaTypeEvent {
    public static final Integer PLEA_FOR_INDICTMENTS_EVENT = new Integer(60101);

    public static final Integer PLEA_FOR_INDICTMENTS_LESSER_OFFENCE_EVENT = new Integer(60102);

    public static final Integer PLEA_FOR_SUMMARY_OFFENCE_EVENT = new Integer(60103);

    public static final Integer PLEA_FOR_BREACH_EVENT = new Integer(60104);      

    public static final Integer PLEA_DELETE_EVENT = new Integer(60105);
    
    public static final Integer PLEA_FOR_FAIL2APPEAR_EVENT = new Integer(60106);
    
    /*
     * Not Guilty / Check verdict
     */
    public static final String  AA= "AA";
    public static final String  AC= "AC";
    public static final String  CPNG= "CPNG";
    public static final String  NPT= "NPT";
    public static final String  NG= "NG";
    public static final String  O= "O"; // Other - by default this is free text so cannot be verified...assume Not Guilty
    public static final String  P= "P";

    
    /*
     * Guilty
     */
    public static final String  CPGJ= "CPGJ";
    public static final String  CPG= "CPG";
    public static final String  G= "G";
    
    /*
     * Lesser offences here but still guilty 
     */
    public static final String  GAO= "GAO"; // Guilty of Alternate Offence
    public static final String  GLO= "GLO"; // Guilty of Lesser Offence

    private PleaTypeEvent() {
    }
}
