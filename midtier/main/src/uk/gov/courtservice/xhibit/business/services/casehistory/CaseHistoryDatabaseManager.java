package uk.gov.courtservice.xhibit.business.services.casehistory;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;

public class CaseHistoryDatabaseManager extends AbstractXhibitDatabase {

	public void deleteCase(Integer caseId, String reasonForDeletion) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCase(caseId="+caseId+", reasonForDeletion="+reasonForDeletion+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_housekeeping_pkg.delete_case_ctx(?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		sp.executeUpdate(new Object[] { caseId, reasonForDeletion });

		final StoredProcedure spInsert = createStoredProcedure(
				"{ call xhb_housekeeping_pkg.insert_case_history(?, ?) }");
		spInsert.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		spInsert.executeUpdate(new Object[] { caseId, reasonForDeletion });
	}
	
	public void writeToCaseHistory(Integer caseId, String reasonForDeletion) {
		if (log.isDebugEnabled()) {
			log.debug("writeToCaseHistory(caseId="+caseId+", reasonForDeletion="+reasonForDeletion+")");
		}
		final StoredProcedure spInsert = createStoredProcedure(
				"{ call xhb_housekeeping_pkg.insert_case_history(?, ?) }");
		spInsert.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		spInsert.executeUpdate(new Object[] { caseId, reasonForDeletion });
	}
}
