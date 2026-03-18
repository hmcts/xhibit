package uk.gov.courtservice.xhibit.business.terminal.interfaces;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment 2003
 * 
 */
public interface CourtSummary extends java.io.Serializable {
    /**
     * @return the court name.
     */
    public String getName();

    /**
     * @return the court name.
     */
    public Integer getId();

    /**
     * @return the crest court id.
     */
    public String getCrestCourtId();

}
