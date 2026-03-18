package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSummary;

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
 * $Revision: 1.5 $
 */
public class CourtSummaryImpl implements CourtSummary {
    private String name;

    private Integer id;

    private String crestId;
    
    private static final long serialVersionUID = -1974860381540058486L;

    public CourtSummaryImpl(String courtName, Integer courtId, String crestId) {
        this.name = courtName;
        this.id = courtId;
        this.crestId = crestId;
    }

    /**
     * @return the court name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the court name.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the crest court id.
     */
    public String getCrestCourtId() {
        return crestId;
    }
}
