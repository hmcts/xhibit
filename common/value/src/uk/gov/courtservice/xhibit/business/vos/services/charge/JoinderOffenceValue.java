package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.Collection;
import java.util.Vector;

/**
 * <p>
 * Title:
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
 * @author Abdul Rahim Hussain
 * @version 1.0
 * @version $Id: JoinderOffenceValue.java,v 1.7 2007/09/18 14:09:28 rzvddy Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 05/03/03 - ARH - First release.
 * </P>
 */

public class JoinderOffenceValue extends OffenceValue {
    /**
     * vector of Integer arrays. Each array element contains an originalCaseId
     * and an originalOffenceId pair. For a given offence, if offences from 3
     * different cases were being joined: cases had e.g CaseId = 1 OffenceId =
     * 10, CaseId = 2 offenceId = 11, CaseId = 3 OffenceId = 12. This would be
     * represented as: [1,10],[2,11],[3,12]
     * 
     */
    private java.util.Vector originalCaseOffences;
    
    private static final long serialVersionUID = 3552920648560797508L;
    
   
    public JoinderOffenceValue() {
    }

    /**
     * Uses the all argument constructor to copy offenceValue object
     */
    public JoinderOffenceValue(OffenceValue offence) {
        super(offence.getOffenceID(), offence.getChargeID(), offence.getRefOffenceID(), offence.getDefendantIDs(),
                offence.getCrestOffenceFreeText(), offence.getCrestOffenceID(), offence.getCrestOffenceSeqNo(), offence
                        .getMultiple(), offence.getOffenceDescription(), offence.getRefSystemCodeID(), offence
                        .getCourtID(), offence.getDefendantOnOffenceID(),offence.getCaseID(), offence.getOffenceCode(), offence.getActSection(), offence
                        .getStatute(), offence.getPlea(), offence.getCrestHOClass(), offence.getCrestHOSubclass());

        // Set the updateCount value to the version of the
        // OffenceValue. This will set the version of the new object to be
        // that of the old one, so preventing OptimisticLockExceptions
        setUpdateCount(offence.getVersion().intValue());
        setAddressId(offence.getAddressId());
        setAddressValue(offence.getAddressValue());
        setOffenceEndDateTime(offence.getOffenceEndDateTime());
        setOffenceStartDateTime(offence.getOffenceStartDateTime());
        setForceLocationCode(offence.getForceLocationCode());
    }

    public JoinderOffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs,
            String crestOffenceFreeText, Integer crestOffenceID, Integer crestOffenceSeqNo, Integer multiple,
            String offenceDescription) {
        super(offenceID, chargeID, refOffenceID, defendantIDs, crestOffenceFreeText, crestOffenceID, crestOffenceSeqNo,
                multiple, offenceDescription);
    }

    public JoinderOffenceValue(Integer offenceID, Integer chargeID, Integer refOffenceID, Collection defendantIDs) {
        super(offenceID, chargeID, refOffenceID, defendantIDs);
    }

    public JoinderOffenceValue(Integer chargeID, Integer refOffenceID, Collection defendantIDs) {
        super(chargeID, refOffenceID, defendantIDs);
    }

    public JoinderOffenceValue(Integer refOffenceID, Collection defendantIDs) {
        super(refOffenceID, defendantIDs);
    }

    public Vector getOriginalCaseOffences() {
        return originalCaseOffences;
    }

    public void setOriginalCaseOffences(Vector originalCaseOffences) {
        this.originalCaseOffences = originalCaseOffences;
    }
}