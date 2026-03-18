package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Title: PartyOnCase
 * </p>
 * <p>
 * Description: This value object contains an aggreagation of value objects for
 * uses with the assign representative screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class PartyOnCaseValue extends PartyOnCaseHeaderValue // implements
// java.io.Serializable
{

    public PartyOnCaseValue() {
    }

    // determines the role of this party : Prosecution, Defendant,
    // Reposndant, Objector or Appelant
    private String partyRole;

    // the scheduled hearing id
    private Integer scheduledHearingID;

    // a collection of LegalRepSignInValue objects representing the legal
    // reps for this party
    private Collection representatives = new ArrayList();

    // a collection of DefendantValue objects representing the defendants
    // for this party
    private Collection defendants = new ArrayList();

    // When the partyRole is Objector, Prosecution or Respondent then
    // scheduledHearingDefendantId will be null
    private Integer scheduledHearingDefendantId;

    // Name of the court clerk signed in for the scheduled hearing
    private ArrayList courtClerk;

    // Name of the usher signed in for the scheduled hearing
    private ArrayList usher;
    
    private static final long serialVersionUID = -6031921522223408796L;

    // getters
    public String getPartyRole() {
        return this.partyRole;
    }

    public Integer getScheduledHearingID() {
        return this.scheduledHearingID;
    }

    public Collection getRepresentatives() {
        return this.representatives;
    }

    public Collection getDefendants() {
        return this.defendants;
    }

    public Integer getScheduledHearingDefendantId() {
        return this.scheduledHearingDefendantId;
    }

    public ArrayList getCourtClerk() {
        return courtClerk;
    }

    public ArrayList getUsher() {
        return usher;
    }

    // setters
    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }

    public void setScheduledHearingID(Integer schedHearingId) {
        this.scheduledHearingID = schedHearingId;
    }

    public void setRepresentatives(Collection representatives) {
        this.representatives = representatives;
    }

    public void setDefendants(Collection defs) {
        this.defendants = defs;
    }

    public void setScheduledHearingDefendantId(Integer newScheduledHearingDefendantId) {
        this.scheduledHearingDefendantId = newScheduledHearingDefendantId;
    }

    public void setCourtClerk(ArrayList courtClerk) {
        this.courtClerk = courtClerk;
    }

    public void setUsher(ArrayList usher) {
        this.usher = usher;
    }
}