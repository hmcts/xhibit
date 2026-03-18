package uk.gov.courtservice.xhibit.business.entities.refjudge;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RefJudgeHome extends EJBLocalHome {

	public RefJudge create(String judgeType, Integer crestJudgeId, String firstName, String middleName, String surname,
			String fullListTitle1, String fullListTitle2, String fullListTitle3, String statsCode, String initials,
			String honours, String judVers, String obsInd, String sourceTable, String title, Integer courtId,
			String userDisplayName) throws CreateException;

	public RefJudge findByPrimaryKey(Integer refJudgeId) throws FinderException;

	public Collection findByCourtIdAndCrestJudgeId(Integer courtId, Integer crestJudgeId) throws FinderException;

	public Collection findByCourtId(Integer courtId) throws FinderException;

	public Collection findByCourtIdAndStatsCode(Integer courtId, String statsCode) throws FinderException;
}