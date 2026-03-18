package uk.gov.courtservice.xhibit.courtlog.vos;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad / Paul Fitton
 * @version $Id: CourtLogViewValue.java,v 1.5 2011/05/25 14:11:44 atwells Exp $
 */
public class CourtLogViewValue extends CourtLogAbstractValue {
    
	private static final long serialVersionUID = -4292466573208039096L;
	
    public static interface EventTypes {
    	public static final Integer BW_HISTORY_ISSUE_WARRANT_EVENT = Integer.valueOf(20100);
    	public static final Integer BW_HISTORY_END_WARRANT_EVENT = Integer.valueOf(20101);
    	public static final Integer CRACKED_INEFFECTIVE_EVENT = Integer.valueOf(20918);
    }
	
	private String logEntry;
    private boolean isDateAmended;

    public CourtLogViewValue() {
        super();
    }

    public CourtLogViewValue(Integer version) {
        super(version);
    }

    public String getLogEntry() {
        return this.logEntry;
    }

    public void setLogEntry(String logEntry) {
        this.logEntry = logEntry;
    }
    
    public boolean isDateAmended() {
        return isDateAmended;
    }

    public void setDateAmended(boolean isDateAmended) {
        this.isDateAmended = isDateAmended;
    }
    
    public boolean isBWHistoryIssueWarrantEvent() {
    	return EventTypes.BW_HISTORY_ISSUE_WARRANT_EVENT.equals(getEventType());
    }

    public boolean isBWHistoryEndWarrantEvent() {
    	return EventTypes.BW_HISTORY_END_WARRANT_EVENT.equals(getEventType());
    }
    
    public boolean isCrackedIneffectiveEvent() {
    	return EventTypes.CRACKED_INEFFECTIVE_EVENT.equals(getEventType());
    }
}
