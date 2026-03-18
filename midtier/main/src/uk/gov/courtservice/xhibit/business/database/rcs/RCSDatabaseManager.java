package uk.gov.courtservice.xhibit.business.database.rcs;

import static uk.gov.courtservice.framework.util.DateTimeUtilities.stripTimeToSQLDate;

import java.sql.Types;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;;

/**
 * A database manager to manage the queries used to populate the
 * record courtroom statistics screen.
 * @author westalll
 *
 */
public class RCSDatabaseManager extends AbstractXhibitDatabase {
	
	/**
	 * Generates the judge usage tables based upon sittings data 
	 * @param site	Site to generate the data for
	 * @param startDate start date to use
	 * @param endDate end date to use
	 */
	public void generateCourtroomStatisticsForSiteAndDate(Integer site, Date startDate, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug("generateCourtroomStatisticsForSiteAndDate(site="+site+", startDate="+startDate+", endDate="+endDate+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_rcs_pkg.populate_judge_usage(?, ?, ?) }");
		  sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.DATE });
		  
		  java.sql.Date sqlStartDate =  stripTimeToSQLDate(startDate);
		  java.sql.Date sqlEndDate =  stripTimeToSQLDate(endDate);

		  sp.executeUpdate(new Object[] { site, sqlStartDate, sqlEndDate});
	}
	
	/**
	 * Indicates whether or not a court room usage record for a room and date already exists
	 * @param courtroomid room id
	 * @param sittingdate sitting date
	 * @return Y if exists, else false
	 */
	public String getCourtRoomUsageExists(Integer courtroomid, Date sittingdate) {
		if (log.isDebugEnabled()) {
			log.debug("getCourtRoomUsageExists(courtroomid="+courtroomid+", sittingdate="+sittingdate+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_rcs_pkg.courtroom_usage_exists_YN(?,?) }");
		sf.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		final String exists = (String) sf.executeFunction(new Object[] {courtroomid, new java.sql.Date(sittingdate.getTime()) }, Types.VARCHAR);
        return exists;
	}
	
	/**
	 * Indicates whether or not a judge usage record for a judge and date already exists
	 * @param sittingdate sitting date
	 * @param judgeid judge id
	 * @return
	 */
	public String getJudgeUsageExists(Date sittingdate, Integer judgeid) {
		if (log.isDebugEnabled()) {
			log.debug("getJudgeUsageExists(sittingdate="+sittingdate+", judgeid="+judgeid+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_rcs_pkg.judge_usage_exists_YN(?,?) }");
		sf.registerInTypes(new int[] { Types.DATE, Types.INTEGER });
		final String exists = (String) sf.executeFunction(new Object[] {new java.sql.Date(sittingdate.getTime()), judgeid }, Types.VARCHAR);
        return exists;
	}
}
