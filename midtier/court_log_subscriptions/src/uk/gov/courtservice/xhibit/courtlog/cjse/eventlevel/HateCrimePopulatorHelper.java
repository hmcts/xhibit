package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencing;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityBeanHelper2;


public class HateCrimePopulatorHelper {
    private static final Logger log = CSServices.getLogger(DefendantPopulatorHelper.class);
    
    /**
     * Description: Retrieve the hate crime reasons from the defendantOnCase value.
     * @param defendantOnCase
     * @return String
     */
    public ArrayList getHateCrimeReasons(ArrayList<XhbHateSentencing> hateSentencing) {

        ArrayList hateCrimeReasons = new ArrayList();
        for (int i=0; i<hateSentencing.size(); i++) {
            // Each element will be a row in the hate sentencing database; need to convert the id to its type
            if (hateSentencing.get(i).getHateSentencingId() != null) {
                if (hateSentencing.get(i).getObsInd() != "Y") {
                    if (hateSentencing.get(i).getHateSentencingId().intValue() == 1) {
                        hateCrimeReasons.add("general_disability");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 2) {
                        hateCrimeReasons.add("victim_disability");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 3) {
                        hateCrimeReasons.add("racially_aggravated");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 4) {
                        hateCrimeReasons.add("religiously_aggravated");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 5) {
                        hateCrimeReasons.add("racially_and_religiously_aggravated");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 6) {
                        hateCrimeReasons.add("general_sex");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 7) {
                        hateCrimeReasons.add("victim_sex");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 8) {
                        hateCrimeReasons.add("general_transgender");
                    } else if (hateSentencing.get(i).getHateSentencingId().intValue() == 9) {
                        hateCrimeReasons.add("victim_transgender");
                    }
                }
            }
        }

        return hateCrimeReasons;
    }

}
