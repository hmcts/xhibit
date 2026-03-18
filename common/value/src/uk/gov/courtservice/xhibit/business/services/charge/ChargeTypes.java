package uk.gov.courtservice.xhibit.business.services.charge;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeType;

/**
 * <p>
 * Title: ChargeTypes
 * </p>
 * <p>
 * Description: Defines allowed charge types
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class ChargeTypes {
    
    private static final String BREACH_DESIGNATOR = "B";
    private static final String FAIL2APPEAR_DESIGNATOR = "F";
    
    public static final ChargeType CRIMINAL_APPEAL = new ChargeType("Criminal Appeal", "C");

    public static final ChargeType CRIMINAL_APPEAL_DISPOSAL = new ChargeType("Criminal Appeal Disposal", "CD");

    public static final ChargeType MISC_APPEAL = new ChargeType("Miscelleanous Appeal", "M");

    public static final ChargeType BREACH = new ChargeType("Breach", BREACH_DESIGNATOR);

    public static final ChargeType BREACH_DISPOSAL = new ChargeType("Breach Disposal", "BD");
    
    public static final ChargeType FAIL2APPEAR = new ChargeType("Failure to Appear", FAIL2APPEAR_DESIGNATOR);

    public static final ChargeType FAIL2APPEAR_DISPOSAL = new ChargeType("Failure to Appear Disposal", "FD");

    public static final ChargeType SECTION_41 = new ChargeType("Summary Offence (section 41)", "O");

    public static final ChargeType SECTION_41_DISPOSAL = new ChargeType("Summary Offence (section 41) Disposal", "OD");

    public static final ChargeType COMMITAL_FOR_SENTENCE = new ChargeType("Committal for Sentence", "S");

    public static final ChargeType COMMITAL_FOR_SENTENCE_DISPOSAL = new ChargeType("Committal for Sentence Disposal",
            "SD");

    public static final ChargeType INDICTMENT = new ChargeType("Indictment", "I");

    public static final ChargeType INDICTMENT_DISPOSAL = new ChargeType("Indictment Disposal", "ID");
    
    public static final ChargeType ORIGINAL_CHARGE = new ChargeType("Original Charge", "G");

    /*
     * isBreachChargeType is to be used where a BREACH and FAIL2APPEAR
     * are synonymous.
     */
    public static boolean isBreachChargeType(String chargeType) {
        return chargeType.equals(BREACH_DESIGNATOR) || chargeType.equals(FAIL2APPEAR_DESIGNATOR);
    }
    
    public static ChargeType getChargeType(String chargetype) {
        if (chargetype == null) {
            return null;
        }
        String type = chargetype.trim();
        if (type.equalsIgnoreCase("C"))
            return CRIMINAL_APPEAL;
        if (type.equalsIgnoreCase("CD"))
            return CRIMINAL_APPEAL_DISPOSAL;
        if (type.equalsIgnoreCase("M"))
            return MISC_APPEAL;
        if (type.equalsIgnoreCase(BREACH_DESIGNATOR))
            return BREACH;
        if (type.equalsIgnoreCase("BD"))
            return BREACH_DISPOSAL;
        if (type.equalsIgnoreCase(FAIL2APPEAR_DESIGNATOR))
            return FAIL2APPEAR;
        if (type.equalsIgnoreCase("FD"))
            return FAIL2APPEAR_DISPOSAL;
        if (type.equalsIgnoreCase("O"))
            return SECTION_41;
        if (type.equalsIgnoreCase("OD"))
            return SECTION_41_DISPOSAL;
        if (type.equalsIgnoreCase("S"))
            return COMMITAL_FOR_SENTENCE;
        if (type.equalsIgnoreCase("SD"))
            return COMMITAL_FOR_SENTENCE_DISPOSAL;
        if (type.equalsIgnoreCase("I"))
            return INDICTMENT;
        if (type.equalsIgnoreCase("ID"))
            return INDICTMENT_DISPOSAL;
        if (type.equalsIgnoreCase("G"))
            return ORIGINAL_CHARGE;

        return null;
    }

}