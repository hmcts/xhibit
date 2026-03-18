package uk.gov.courtservice.xhibit.courtlog.exceptions;

/**
 * Exception class used when an attempt has been made to swear in a witness
 * before the previous witness has been released.
 * 
 * @author tz0d5m
 */
public class WitnessSwornException extends CourtLogBusinessException {
    
	private static final long serialVersionUID = -6650830087303864834L;

	private static final String KEY = "Court_Log.Business_Logic.Witness_Sworn";

    private static final String MESSAGE = "Can't swear in witness before a previous witness is released";

    /**
     * Constructor that initialises this exception with the key and message
     * defined as constants in this class.
     */
    public WitnessSwornException() {
        super(KEY, MESSAGE);
    }
}
