package uk.gov.courtservice.xhibit.business.services.hearingschedule;

import java.sql.Types;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;

public class HearingScheduleDatabaseManager extends AbstractXhibitDatabase {

	private static final String YES = "Y";
	
	public Boolean isJudgeSittingOnDate(Integer refJudgeId, Date date) {
		if (log.isDebugEnabled()) {
			log.debug("isJudgeSittingOnDate(refJudgeId="+refJudgeId+", date="+date+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_view_schedule_pkg.is_judge_sitting_on_date(?,?) }");
		sf.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		Object[] parameters = new Object[] {refJudgeId, new java.sql.Date(date.getTime())};
		final String result = (String) sf.executeFunction(parameters, Types.VARCHAR);
		return YES.equals(result);
	}
}
