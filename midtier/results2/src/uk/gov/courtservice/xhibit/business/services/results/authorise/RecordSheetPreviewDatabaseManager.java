package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.sql.Clob;
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;

public class RecordSheetPreviewDatabaseManager extends AbstractXhibitDatabase {
	
	public String getRecordSheetPreviewGenerationStatus(Integer defendantOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("getRecordSheetPreviewGenerationStatus(defendantOnCaseId="+defendantOnCaseId+")");
		}
		String GET_RECORD_SHEET_PREVIEW_GENERATION_STATUS = "{ ? = call XHB_RECORD_SHEET_PKG.GET_PREVIEW_GENERATION_STATUS(?)}";
	    final StoredFunction sf = createStoredFunction(GET_RECORD_SHEET_PREVIEW_GENERATION_STATUS);
	    sf.registerInTypes(new int[] { Types.INTEGER });
	    final String status = (String) sf.executeFunction(new Object[] {defendantOnCaseId}, Types.VARCHAR);
	    return status;
	}
	
	public void requestRecordSheetPreviewGeneration(Integer defendantOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("requestRecordSheetPreviewGeneration(defendantOnCaseId="+defendantOnCaseId+")");
		}
		String REQUEST_RECORD_SHEET_PREVIEW_GENERATION = "{ call XHB_RECORD_SHEET_PKG.REQUEST_PREVIEW_GENERATION(?) }";
	    final StoredProcedure sp = createStoredProcedure(REQUEST_RECORD_SHEET_PREVIEW_GENERATION);
	    sp.registerInTypes(new int[] { Types.INTEGER });
	    sp.executeUpdate(new Object[] {defendantOnCaseId});
	}
	
	public void cancelRecordSheetPreviewGeneration(Integer defendantOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("cancelRecordSheetPreviewGeneration(defendantOnCaseId="+defendantOnCaseId+")");
		}
		String CANCEL_RECORD_SHEET_PREVIEW_GENERATION = "{ call XHB_RECORD_SHEET_PKG.CANCEL_PREVIEW_GENERATION(?) }";
		final StoredProcedure sp = createStoredProcedure(CANCEL_RECORD_SHEET_PREVIEW_GENERATION);
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.executeUpdate(new Object[] {defendantOnCaseId});
	}
	
	public String getGeneratedPreviewXML(Integer defendantOnCaseId) { 
		if (log.isDebugEnabled()) {
			log.debug("getGeneratedPreviewXML(defendantOnCaseId="+defendantOnCaseId+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call XHB_RECORD_SHEET_PKG.GET_GENERATED_PREVIEW_XML(?) }");
		sf.registerInTypes(new int[] { Types.INTEGER });
		final String xml = StringStrategy.getValue((Clob) sf.executeFunction(new Object[] {defendantOnCaseId}, Types.CLOB));
		return xml;
	}

}
