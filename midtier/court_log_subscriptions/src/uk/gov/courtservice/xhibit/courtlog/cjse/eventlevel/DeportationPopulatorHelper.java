package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_nationality.XhbRefNationalityBeanHelper2;


public class DeportationPopulatorHelper {
    private static final Logger log = CSServices.getLogger(DefendantPopulatorHelper.class);

    /**
     * Description: Returns the nationality code and country name 
     * @param defendantOnCase
     * @return nationality: String
     */
    public String getNationality(XhbDefendantOnCase defendantOnCase){
        String nationality = "";
       
        //Create nationality string
        if(defendantOnCase.getNationality() != null){
            if(!defendantOnCase.getNationality().equals("")){
                nationality = defendantOnCase.getNationality().toUpperCase();
            }
        }
        
        //Assign blank nationality value
        if(nationality.equals("")){
            nationality = "";
        }
        
        return nationality;
        
    }
    
    /**
     * Description: Reetrieve the deportation reason from the defendantOnCase value.
     * @param defendantOnCase
     * @return String
     */
    public String getDeportationReason(XhbDefendantOnCase defendantOnCase) {

        String deportationReason = "";
        
        if(defendantOnCase.getCustodial() != null){
            if(defendantOnCase.getCustodial().equalsIgnoreCase("Y")){
                deportationReason = "custodial";   
            }
        }
        if(defendantOnCase.getSuspended() != null){
            if(defendantOnCase.getSuspended().equalsIgnoreCase("Y")){
                deportationReason = "suspended";   
            }
        }
        if(defendantOnCase.getSeriousDrugOffence() != null){
            if(defendantOnCase.getSeriousDrugOffence().equalsIgnoreCase("Y")){
                deportationReason = "seriousDrugOffence";   
            }
        }
        if(defendantOnCase.getRecommendedDeportation() != null){
            if(defendantOnCase.getRecommendedDeportation().equalsIgnoreCase("Y")){
                deportationReason = "recommendedDeportation";   
            }
        }

        return deportationReason;
        
    }

    
    
    
    

    
}
