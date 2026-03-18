package uk.gov.courtservice.xhibit.business.services.cppformatting;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;

public class CppFormattingDatabaseManager extends AbstractXhibitDatabase {
	
	public void updateCppFormattingStatus(final Integer cppFormattingId, final String status) {
    	
        validateParameterNotNull("cppFormattingId", cppFormattingId);

    	if (log.isDebugEnabled()) {
            log.debug("updateCppFormattingStatus() - cppFormattingId=" + cppFormattingId);
        }
    	
    	 final StoredFunction sf = createStoredFunction("{ call xhb_formatting_pkg.update_cpp_formatting_status(?,?) }");
         sf.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
         sf.executeFunction(new Object[] { cppFormattingId, status });
    }
}
