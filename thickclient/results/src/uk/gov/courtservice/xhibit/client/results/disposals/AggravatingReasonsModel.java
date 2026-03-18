package uk.gov.courtservice.xhibit.client.results.disposals;

/**
 * Description: This Model holds the aggravating reasons
 * @author Mark Harris
 * @version 1.0
 */
public class AggravatingReasonsModel {
    private boolean aggravatingFlag = false;
    private boolean assaultOnWorkers = false;
	private boolean terroristConnection = false;
    private boolean emergencyWorkers = false;
    private boolean hostility = false;
    private boolean sexualOrientation = false;
    private boolean sexualOrientationOfVictim = false;
    private boolean transgender = false;
    private boolean transgenderOfVictim = false;
    
    public AggravatingReasonsModel(){
    }
    
    public boolean isAssaultOnWorkers() {
		return assaultOnWorkers;
	}

	public boolean isTerroristConnection() {
		return terroristConnection;
	}

	public boolean isEmergencyWorkers() {
		return emergencyWorkers;
	}

	public boolean isHostility() {
		return hostility;
	}

	public boolean isSexualOrientation() {
		return sexualOrientation;
	}

	public boolean isSexualOrientationOfVictim() {
		return sexualOrientationOfVictim;
	}

	public boolean isTransgender() {
		return transgender;
	}

	public boolean isTransgenderOfVictim() {
		return transgenderOfVictim;
	}

	public void setAssaultOnWorkers(boolean assaultOnWorkers) {
		this.assaultOnWorkers = assaultOnWorkers;
	}

	public void setTerroristConnection(boolean terroristConnection) {
		this.terroristConnection = terroristConnection;
	}

	public void setEmergencyWorkers(boolean emergencyWorkers) {
		this.emergencyWorkers = emergencyWorkers;
	}

	public void setHostility(boolean hostility) {
		this.hostility = hostility;
	}

	public void setSexualOrientation(boolean sexualOrientation) {
		this.sexualOrientation = sexualOrientation;
	}

	public void setSexualOrientationOfVictim(boolean sexualOrientationOfVictim) {
		this.sexualOrientationOfVictim = sexualOrientationOfVictim;
	}

	public void setTransgender(boolean transgender) {
		this.transgender = transgender;
	}

	public void setTransgenderOfVictim(boolean transgenderOfVictim) {
		this.transgenderOfVictim = transgenderOfVictim;
	}
}
