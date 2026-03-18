package uk.gov.courtservice.xhibit.courtlog.exceptions;

/**
 * Exception class used when an attempt has been made to release a witness
 * either before any witness has been sworn in, or all witnesses have already
 * been released.
 * 
 * @author tz0d5m
 */
public class WitnessReleasedException extends CourtLogBusinessException {
    
	private static final long serialVersionUID = 9141309203620797607L;

	private static final String KEY = "Court_Log.Business_Logic.Witness_Released";

    private static final String MESSAGE = "Can't release a witness before the witness is sworn in";

    /**
     * Constructor that initialises this exception with the key and message
     * defined as constants in this class.
     */
    public WitnessReleasedException() {
        super(KEY, MESSAGE);
    }
}
