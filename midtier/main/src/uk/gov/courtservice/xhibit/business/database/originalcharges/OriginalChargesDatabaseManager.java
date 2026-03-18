package uk.gov.courtservice.xhibit.business.database.originalcharges;

/**
 * <p>Title: OriginalChargesDatabaseManager</p>
 * <p>Description: Manages access to the XHIBIT data base for the Original Charges screen.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.database.originalcharges.processor.ChargeRowProcessor;
import uk.gov.courtservice.xhibit.business.database.originalcharges.processor.DefendantOnCaseRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class OriginalChargesDatabaseManager extends AbstractXhibitDatabase {

    private static final String GET_DEFENDANTS_ON_CASE = "{ call xhb_defendants_pkg.get_by_case_id(?,?) }";
    private static final String GET_CHARGES            = "{ call xhb_charges_pkg.get_charges(?,?,?) }";
    private static final String GET_OBSOLETE_CHARGES   = "{ call xhb_charges_pkg.get_obsolete_charges(?,?,?) }";

    /**
     * Obtains defendant on case details for a given case ID
     */
    public DefendantOnCaseVO[] getDefendantsOnCase(Integer caseId) {
        final DefendantOnCaseRowProcessor rp = new DefendantOnCaseRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_DEFENDANTS_ON_CASE);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { caseId });
        return rp.getDefendantsOnCase();
    }

    /**
     * Obtains charge details for a given defendant on case ID and optional charge type
     */
    public ChargeVO[] getCharges(Integer defendantOnCaseId, String chargeType) {
        final ChargeRowProcessor rp = new ChargeRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_CHARGES);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { defendantOnCaseId, chargeType });
        return rp.getCharges();
    }

    /**
     * Obtains charge details for a given defendant on case ID and optional charge type
     */
    public ChargeVO[] getObsoleteCharges(Integer defendantOnCaseId, String chargeType) {
        final ChargeRowProcessor rp = new ChargeRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_OBSOLETE_CHARGES);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { defendantOnCaseId, chargeType });
        return rp.getCharges();
    }
}
