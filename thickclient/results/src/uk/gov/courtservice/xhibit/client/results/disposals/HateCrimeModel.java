package uk.gov.courtservice.xhibit.client.results.disposals;

/**
 * Description: This Model holds the verious hate crime reasons
 * @author B Hingston
 * @version 1.0
 */
public class HateCrimeModel {
    private boolean hateCrimeFlag;
    private boolean generalDisability;
    private boolean victimDisability;
    private boolean racialAggravated;
    private boolean raceAndReligionAggravated;
    private boolean religionAggravated;
    private boolean generalSexual;
    private boolean victimSexual;
    private boolean generalTransgender;
    private boolean victimTransgender;
    
    /**
     * Default Constructor
     * @return
     */
    public HateCrimeModel(){
        this.hateCrimeFlag = false;
        this.generalDisability =false;
        this.victimDisability =false;
        this.racialAggravated =false;
        this.raceAndReligionAggravated =false;
        this.religionAggravated = false;
        this.generalSexual = false;
        this.victimSexual = false;
        this.generalTransgender = false;
        this.victimTransgender = false;
    }
    
    public boolean getHateCrimeFlag(){
        return hateCrimeFlag;
    }
    
    public void setHateCrimeFlag(boolean hateCrimeFlag){
        this.hateCrimeFlag = hateCrimeFlag;
    }
   
    public boolean getGeneralDisability() {
        return generalDisability;
    }
    
    public void setGeneralDisability(boolean generalDisability) {
        this.generalDisability = generalDisability;
    }
    
    public boolean getVictimDisability() {
        return victimDisability;
    }
    
    public void setVictimDisability(boolean victimDisability) {
        this.victimDisability = victimDisability;
    }
    
    public boolean getRacialAggravated() {
        return racialAggravated;
    }
    
    public void setRacialAggravated(boolean racialAggravated) {
        this.racialAggravated = racialAggravated;
    }
    
    public boolean getRaceAndReligionAggravated() {
        return raceAndReligionAggravated;
    }
    
    public void setRaceAndReligionAggravated(boolean raceAndReligionAggravated) {
        this.raceAndReligionAggravated = raceAndReligionAggravated;
    }
    
    public boolean getReligionAggravated() {
        return religionAggravated;
    }
    
    public void setReligionAggravated(boolean religionAggravated) {
        this.religionAggravated = religionAggravated;
    }
    
    public boolean getGeneralSexual() {
        return generalSexual;
    }
    
    public void setGeneralSexual(boolean generalSexual) {
        this.generalSexual = generalSexual;
    }
    
    public boolean getVictimSexual() {
        return victimSexual;
    }
    
    public void setVictimSexual(boolean victimSexual) {
        this.victimSexual = victimSexual;
    }
    
    public boolean getGeneralTransgender() {
        return generalTransgender;
    }
    
    public void setGeneralTransgender(boolean generalTransgender) {
        this.generalTransgender = generalTransgender;
    }
    
    public boolean getVictimTransgender() {
        return victimTransgender;
    }
    
    public void setVictimTransgender(boolean victimTransgender) {
        this.victimTransgender = victimTransgender;
    }
    


}
