package uk.gov.courtservice.xhibit.business.services.resetdata;

/**
 * <p>Title: OriginalChargesDatabaseManager</p>
 * <p>Description: Manages access to the XHIBIT data base for the Original Charges screen.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
import java.sql.Types;

import java.sql.Date;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.services.datareset.vo.ManagedCase;
import uk.gov.courtservice.xhibit.business.services.resetdata.processor.CaseRowProcessor;
import uk.gov.courtservice.xhibit.business.services.resetdata.processor.CheckCaseAlreadyAddedResultsRowProcessor;
import uk.gov.courtservice.xhibit.business.services.resetdata.processor.CourtRowProcessor;
import uk.gov.courtservice.xhibit.business.services.resetdata.processor.CheckCaseExistsResultsRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;

public class ResetDataDatabaseManager extends AbstractXhibitDatabase {

    private static final String GET_MANAGED_CASES_BY_COURT = "{ call xhb_data_reset_pkg.get_managed_cases_by_court(?,?) }";
    private static final String GET_COURT = "{ call xhb_data_reset_pkg.get_court(?,?) }";
    private static final String UPDATE_DEFENDANT_DOB = "{ call xhb_data_reset_pkg.update_date_of_birth(?,?) }";
    private static final String ADD_CASE = "{ call xhb_data_reset_pkg.add_case(?) }";
    private static final String REMOVE_CASE = "{ call xhb_data_reset_pkg.remove_case(?) }";
    private static final String CHECK_CASE_ALREADY_ADDED = "{ call xhb_data_reset_pkg.check_case_already_added(?,?) }";
    private static final String CHECK_CASE_EXISTS = "{ call xhb_data_reset_pkg.check_case_exists(?,?,?,?) }";
    private static final String UPDATE_CREST = "{ call xhb_data_reset_pkg.update_crest() }"; // This sets a flag on XHB_CREST_IMPORT to trigger a broker update of CREST


    /**
     * Obtains court details for a given court name
     * @param courtName
     * @return
     */
    public CourtBasicValue[] getCourt(String courtName) {
    	if (log.isDebugEnabled()) {
            log.debug("getCourt() - courtName=" + courtName);
        }

        validateParameterNotNull("courtName", courtName);
        
        final CourtRowProcessor rp = new CourtRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_COURT);
        sp.registerInTypes(new int[] { Types.VARCHAR });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtName });
        return rp.getCourt();
    }

    /**
     * Obtains managed case details for a given court id
     * @param courtId
     * @return
     */
    public ManagedCase[] getManagedCasesByCourt(Integer courtId) {
    	if (log.isDebugEnabled()) {
            log.debug("getManagedCasesByCourt() - courtId=" + courtId);
        }

        validateParameterNotNull("courtId", courtId);
        
        final CaseRowProcessor rp = new CaseRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_MANAGED_CASES_BY_COURT);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId });
        return rp.getManagedCasesByCourt();
    }
    
    /**
     * Update a defendant with a new date of birth
     * 
     * @param newDefendantDOB
     * @param defendantId
     */
    public void updateDefendantDOB(Date newDefendantDOB, Integer defendantId) {
    	if (log.isDebugEnabled()) {
            log.debug("updateDefendantDOB() - newDefendantDOB=" + newDefendantDOB + "; defendantId=" + defendantId);
        }

        validateParameterNotNull("newDefendantDOB", newDefendantDOB);
        validateParameterNotNull("defendantId", defendantId);

        // now that we know the id is valid, call the stored proc...
        final StoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_DOB);
        sf.registerInTypes(new int[] { Types.DATE, Types.INTEGER });
        sf.executeFunction(new Object[] { newDefendantDOB, defendantId });
    }
    
    
    /**
     * Remove a managed case
     * @param caseId
     */
    public void removeManagedCase(Integer caseId) {
    	if (log.isDebugEnabled()) {
            log.debug("removeManagedCase() - caseId=" + caseId);
        }

        validateParameterNotNull("caseId", caseId);
        final StoredFunction sf = createStoredFunction(REMOVE_CASE);
        sf.registerInTypes(new int[] { Types.INTEGER });
        sf.executeFunction(new Object[] { caseId });
    }
    
    /**
     * Updates a flag in XHB_CONFIG_PROP which signals a Broker map
     * be called to update CREST with the changes.
     * @param caseId
     */
    public void updateCREST() {
    	if (log.isDebugEnabled()) {
            log.debug("updateCREST()");
        }

        final StoredFunction sf = createStoredFunction(UPDATE_CREST);
        sf.executeFunction(new Object[] {});
    }
    
    /**
     * Add a managed case
     * @param caseId
     */
    public void addManagedCase(Integer caseId) {
    	if (log.isDebugEnabled()) {
    		log.debug("checkCaseExists() - caseId=" + caseId);
        }

    	validateParameterNotNull("caseId", caseId);
        
        final StoredFunction sf = createStoredFunction(ADD_CASE);
        sf.registerInTypes(new int[] { Types.INTEGER });
        sf.executeFunction(new Object[] { caseId });
    }
    
    /**
     * Check whether a case actually exists or not
     * @param courtId
     * @param caseType
     * @param caseNumber
     * @return
     */
    public String checkCaseExists(Integer courtId, String caseType, Integer caseNumber) {
    	if (log.isDebugEnabled()) {
            log.debug("checkCaseExists() - courtId=" + courtId + "; caseType=" + caseType + "; caseNumber=" + caseNumber);
        }

        validateParameterNotNull("courtId", courtId);
        validateParameterNotNull("caseType", caseType);
        validateParameterNotNull("caseNumber", caseNumber);
        
        final CheckCaseExistsResultsRowProcessor rp = new CheckCaseExistsResultsRowProcessor();
        final StoredProcedure sp = createStoredProcedure(CHECK_CASE_EXISTS);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId, caseType, caseNumber });
        if (rp.getRetValue().size() == 0) {
        	return "0";
        } else {
        	if (rp.getRetValue().get(0).toString() != null) {
        		return rp.getRetValue().get(0).toString();
        	} else {
        		return "0";
        	}
        }
    }
    
    /**
     * Check if a case is already flagged as being 'managed'
     * @param caseId
     * @return
     */
    public String checkCaseAlreadyAdded(Integer caseId) {
    	if (log.isDebugEnabled()) {
            log.debug("checkCaseAlreadyAdded() - caseId=" + caseId);
        }

        validateParameterNotNull("caseId", caseId);
        
        final CheckCaseAlreadyAddedResultsRowProcessor rp = new CheckCaseAlreadyAddedResultsRowProcessor();
        final StoredProcedure sp = createStoredProcedure(CHECK_CASE_ALREADY_ADDED);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { caseId });
        if (rp.getRetValue().size() == 0) {
        	return "0";
        } else {
        	if (rp.getRetValue().get(0).toString() != null) {
        		return rp.getRetValue().get(0).toString();
        	} else {
        		return "0";
        	}
        }
    }

}
