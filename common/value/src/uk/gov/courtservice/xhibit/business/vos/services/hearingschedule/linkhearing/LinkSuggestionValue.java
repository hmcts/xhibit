package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * Title: LinkSuggestionValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version 1.0
 */

public class LinkSuggestionValue implements Serializable {
	private static final long serialVersionUID = -7720981514306707471L;
	private Collection allSuggestions;

    private Collection previouslyLinked;

    private CaseSchedHearingValue currentCase;

    public LinkSuggestionValue() {
    }

    public Collection getAllSuggestions() {
        return allSuggestions;
    }

    public void setAllSuggestions(Collection allSuggestions) {
        this.allSuggestions = allSuggestions;
    }

    public void setPreviouslyLinked(Collection previouslyLinked) {
        this.previouslyLinked = previouslyLinked;
    }

    public Collection getPreviouslyLinked() {
        return previouslyLinked;
    }

    public CaseSchedHearingValue getCurrentCase() {
        return currentCase;
    }

    public void setCurrentCase(CaseSchedHearingValue currentCase) {
        this.currentCase = currentCase;
    }
}