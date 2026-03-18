package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ListResourceBundle;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Register each complex VO here.
 * <p>
 * The names in the XML file must match the static names declared here.
 * <p>
 * All complex events MUST extend FreeTextModel.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CourtLogVOBundle extends ListResourceBundle {

    public static String FreeText = "voFreeText";

    public static String BailCustody = "voBailCustody";
    
    public static String SpecialMeasures = "voSpecialMeasures";

    public static String ShortAdjournment = "voShortAdjournment";

    public static String LongAdjournment = "voLongAdjournment";

    public static String TimeEstimate = "voTimeEstimate";

    public static String WitnessSworn = "voWitnessSworn";
    
    public static String WitnessRead = "voWitnessRead";

    public static String WitnessSwornAppeal = "voWitnessSwornAppeal";
    
    public static String WitnessReadAppeal = "voWitnessReadAppeal";

    public static String LegalArgumentOptions = "voLegalArgumentOptions";

    public static String LegalArgumentOptionsAppeal = "voLegalArgumentOptionsAppeal";

    public static String LegalArgumentOptionsTrial = "voLegalArgumentOptionsTrial";

    public static String TakenIntoConsideration = "voTakenIntoConsideration";
    
    public static String JurySwornIn = "voJurySwornIn";

    public static String JuryDischarged = "voJuryDischarged";

    public static String SevenFourteenDayOrder = "voSevenFourteenDayOrder";

    public static String EndHearing = "voEndHearing";

    public static String PreliminaryHearing = "voPreliminaryHearing";

    private Object[][] contents = 
        { { FreeText, new FreeTextModel() }, 
            { BailCustody, new BailCustodyModel() },
            { ShortAdjournment, new ShortAdjournmentModel() }, 
            { LongAdjournment, new LongAdjournmentModel() },
            { TimeEstimate, new EstimateForTrialModel() }, 
            { WitnessSworn, new WitnessSwornModel() },
            { WitnessRead, new WitnessReadModel() },
            { LegalArgumentOptions, new LegalArgumentOptionsModel() },
            { LegalArgumentOptionsAppeal, new LegalArgumentOptionsModel() },
            { LegalArgumentOptionsTrial, new LegalArgumentOptionsModel() },
            { WitnessSwornAppeal, new WitnessSwornModel() },
            { WitnessReadAppeal, new WitnessReadModel() },
            { TakenIntoConsideration, new TakenIntoConsiderationModel() }, 
            { JurySwornIn, new JurySwornInModel() },
            { JuryDischarged, new JuryDischargedModel() }, 
            { SevenFourteenDayOrder, new SevenFourteenDayOrderModel() },
            { EndHearing, new EndHearingModel() }, 
            { PreliminaryHearing, new FreeTextModel() },
            { SpecialMeasures, new SpecialMeasuresApplicationModel() }};

    public CourtLogVOBundle() {
        super();
    }

    protected Object[][] getContents() {
        return contents;
    }
}