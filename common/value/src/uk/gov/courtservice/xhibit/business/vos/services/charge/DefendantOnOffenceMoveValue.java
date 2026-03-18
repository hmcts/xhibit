package uk.gov.courtservice.xhibit.business.vos.services.charge;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: midtier to client value object for old to new
 * DefendantOnOffenceId mappings
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: DefendantOnOffenceMoveValue.java,v 1.3 2004/06/10 15:56:43
 *          czvsws Exp $
 */

public class DefendantOnOffenceMoveValue {

    private static final Logger log = CSServices.getLogger(DefendantOnOffenceMoveValue.class);

    private final Integer oldDefendantOnOffenceId;

    private final Integer newDefendantOnOffenceId;

    public DefendantOnOffenceMoveValue(Integer oldDefendantOnOffenceId, Integer newDefendantOnOffenceId) {
        this.oldDefendantOnOffenceId = oldDefendantOnOffenceId;
        this.newDefendantOnOffenceId = newDefendantOnOffenceId;
    }

    public Integer getOldDefendantOnOffenceId() {
        return oldDefendantOnOffenceId;
    }

    public Integer getNewDefendantOnOffenceId() {
        return newDefendantOnOffenceId;
    }

    public boolean equals(Object o) {
        if (o instanceof DefendantOnOffenceMoveValue) {
            return equals((DefendantOnOffenceMoveValue) o);
        } else {
            return false;
        }
    }

    public boolean equals(DefendantOnOffenceMoveValue o) {
        return getNewDefendantOnOffenceId().equals(o.getNewDefendantOnOffenceId())
                && getOldDefendantOnOffenceId().equals(o.getOldDefendantOnOffenceId());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + ((this.newDefendantOnOffenceId != null) ? this.newDefendantOnOffenceId.hashCode() : 0);
        result = 37 * result + ((this.oldDefendantOnOffenceId != null) ? this.oldDefendantOnOffenceId.hashCode() : 0);
        return result;
    }

}