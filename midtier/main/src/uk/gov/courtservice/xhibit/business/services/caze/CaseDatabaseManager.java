package uk.gov.courtservice.xhibit.business.services.caze;

import java.sql.Types;
import java.util.Collection;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseSummaryOffenceValue;

public class CaseDatabaseManager extends AbstractXhibitDatabase {
	
	/**
	 * Invokes the determine_case_status function on the xhb_case_pkg package to determine the status
	 * of a given case
	 * @param caseId The identifier of the case to be checked
	 * @return	the case status
	 */
	public String determineCaseStatus(Integer caseId) {
		if (log.isDebugEnabled()) {
			log.debug("determineCaseStatus(caseId=" + caseId + ")"); 
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_case_pkg.determine_case_status(?) }");
		sf.registerInTypes(new int[] { Types.INTEGER});
		final String caseStatus = (String) sf.executeFunction(new Object[] {caseId}, Types.VARCHAR);
        return caseStatus;
	}
	
	/**
	 * Returns a list of offences on a case specifically for the case summary screen
	 * @param caseId
	 * @return collection of offences
	 */
	public Collection<CaseSummaryOffenceValue> getCaseOffences(Integer caseId) {
		if (log.isDebugEnabled()) {
			log.debug("getCaseOffences(caseId=" + caseId + ")"); 
		}
		final CaseSummaryOffenceValueRowProcessor caseSummaryOffenceRowProcessor = new CaseSummaryOffenceValueRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_pkg.get_case_offences(?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(caseSummaryOffenceRowProcessor);
        sp.execute(new Object[] {caseId});
        Collection<CaseSummaryOffenceValue> caseSummaryOffences = caseSummaryOffenceRowProcessor.getResults();
       return caseSummaryOffences;
	}

}
