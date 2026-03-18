package uk.gov.courtservice.xhibit.client.results.disposals;

/**
 * Description: This Model holds the verious reason for deportation
 * @author davieskl
 * @version 1.0
 */
public class DeportationModel {
    private String custodial;
    private String suspended;
    private String seriousDrugOffence;
    private String recommendedDeportation;
    
    /**
     * Default Constructor
     * @return
     */
    public DeportationModel(){
        this.custodial ="";
        this.suspended ="";
        this.seriousDrugOffence ="";
        this.recommendedDeportation ="";
    }
   
    public String getCustodial() {
        return custodial;
    }
    
    public void setCustodial(String custodial) {
        this.custodial = custodial;
    }
    
    public String getSuspended() {
        return suspended;
    }
    
    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }

    public String getRecommendedDeportation() {
        return recommendedDeportation;
    }

    public void setRecommendedDeportation(String recommendedDeportation) {
        this.recommendedDeportation = recommendedDeportation;
    }

    public String getSeriousDrugOffence() {
        return seriousDrugOffence;
    }

    public void setSeriousDrugOffence(String seriousDrugOffence) {
        this.seriousDrugOffence = seriousDrugOffence;
    }



}
