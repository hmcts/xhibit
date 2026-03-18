package uk.gov.courtservice.xhibit.business.services.pdda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper.RowMapper;
import uk.gov.courtservice.framework.jdbc.core.Parameter;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;

public class PddaDatabaseManager extends AbstractXhibitDatabase {
	
	public String getGuid() {
    	final StoredFunction sf = createStoredFunction("{ ? = call xhb_public_display_pkg.get_guid() }");
    	final String result = (String) sf.executeFunction(new Object[0], Types.VARCHAR);
 		return result;
    }
	
	/**
	 * Returns at most, one record from XHB_INTERNET_HTML, non-court specific,
	 * that are eligible for sending to PDDA.
	 * 
	 * @param inDate
	 * @return
	 */
	public InternetHtmlPddaValue getNextValidInternetHtmlForPddaOld(java.util.Date inDate) {
		if (log.isDebugEnabled()) {
			log.debug("getNextValidInternetHtmlForPdda(inDate="+inDate+")");
		}
		String GET_NEXT_VALID_INTERNET_HTML_FOR_PDDA = "{ ? = call XHB_PDDA_PKG.get_iwp_next_eligible_record(?)}";
	    final StoredFunction sf = createStoredFunction(GET_NEXT_VALID_INTERNET_HTML_FOR_PDDA);
	    sf.registerInTypes(new int[] { Types.DATE });
	    Object out = sf.executeFunction(
    	    new Object[]{ new java.sql.Date(inDate.getTime()) },
    	    oracle.jdbc.OracleTypes.CURSOR);

    	InternetHtmlPddaValue nextRecord = null;
    	if (out instanceof Map) {
    	  Map<String,Object> r = (Map<String,Object>) out;
    	  nextRecord = new InternetHtmlPddaValue(
    	      (Integer) r.get("INTERNET_HTML_ID"),
    	      (String)  r.get("STATUS"),
    	      (Date) r.get("LAST_UPDATE_DATE"),
    	      (Date) r.get("CREATION_DATE"),
    	      (String)  r.get("CREATED_BY"),
    	      (String)  r.get("LAST_UPDATED_BY"),
    	      (Integer) r.get("VERSION"),
    	      (Integer) r.get("COURT_ID"),
    	      (Long)    r.get("HTML_BLOB_ID"),
    	      (Integer) r.get("RN")
    	  );
    	}

	    return nextRecord;
	}
	
	
	public InternetHtmlPddaValue getNextValidInternetHtmlForPdda(java.util.Date inDate) {

		  final String GET_NEXT_VALID_INTERNET_HTML_FOR_PDDA =
		      "{ ? = call XHB_PDDA_PKG.get_iwp_next_eligible_record(?) }";

		  final StoredFunction sf = createStoredFunction(GET_NEXT_VALID_INTERNET_HTML_FOR_PDDA);

		  // Correct param registrations
		  sf.registerInTypes(new int[] { Types.DATE });

		  // Build params as your framework expects
		  Parameter[] params = new Parameter[] {
		      Parameter.getOutParameter(oracle.jdbc.OracleTypes.CURSOR),              // return value
		      Parameter.getInParameter(Types.DATE, new java.sql.Date(inDate.getTime())) // p_for_date
		  };

		  return (InternetHtmlPddaValue) sf.executeStoredFunctionCursorSingle(params,
				  new RowMapper<InternetHtmlPddaValue>() {
		    public InternetHtmlPddaValue mapRow(ResultSet rs) throws SQLException {
		      return new InternetHtmlPddaValue(
		          rs.getInt("INTERNET_HTML_ID"),
		          rs.getString("STATUS"),
		          (java.util.Date) rs.getTimestamp("LAST_UPDATE_DATE"),
		          (java.util.Date) rs.getTimestamp("CREATION_DATE"),
		          rs.getString("CREATED_BY"),
		          rs.getString("LAST_UPDATED_BY"),
		          rs.getInt("VERSION"),
		          rs.getInt("COURT_ID"),
		          rs.getLong("HTML_BLOB_ID"),
		          rs.getInt("RN")
		      );
		    }
		  });
		}

	
	
	
	/**
	 * Returns a list of all of the records from XHB_INTERNET_HTML, max one per court,
	 * that are eligible for sending to PDDA. 
	 * 
	 * @param inDate
	 * @return
	 */
	public InternetHtmlPddaValue[] getValidInternetHtmlsForPdda(Date inDate) {
		if (log.isDebugEnabled()) {
			log.debug("getValidInternetHtmlsForPdda(inDate="+inDate+")");
		}
		String GET_VALID_INTERNET_HTMLS_FOR_PDDA = "{ ? = call XHB_PDDA_PKG.get_iwp_all_eligible_courts(?)}";
	    final StoredFunction sf = createStoredFunction(GET_VALID_INTERNET_HTMLS_FOR_PDDA);
	    sf.registerInTypes(new int[] { Types.INTEGER });
	    final InternetHtmlPddaValue[] recordsReturned = (InternetHtmlPddaValue[]) sf.executeFunction(new Object[] {inDate}, Types.DATE);
	    return recordsReturned;
	}
}
