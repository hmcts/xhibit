package uk.gov.courtservice.xhibit.business.database.results;

import java.sql.Clob;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.LoggedStoredProcedure;
import uk.gov.courtservice.framework.jdbc.core.RecursiveReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.RecursiveRowProcessorConfiguration;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.business.database.classes.CTLRLCase;
import uk.gov.courtservice.xhibit.business.database.classes.CTLRLExpiry;
import uk.gov.courtservice.xhibit.business.database.classes.CTLRLExpiryInfo;
import uk.gov.courtservice.xhibit.business.database.classes.Case;
import uk.gov.courtservice.xhibit.business.database.classes.Diary;
import uk.gov.courtservice.xhibit.business.database.classes.DiaryInfo;
import uk.gov.courtservice.xhibit.business.database.classes.LFIXCase;
import uk.gov.courtservice.xhibit.business.database.classes.LFIXCaseDiaryFixture;
import uk.gov.courtservice.xhibit.business.database.classes.LFIXDefendant;
import uk.gov.courtservice.xhibit.business.database.classes.LFIXHearing;
import uk.gov.courtservice.xhibit.business.database.classes.RRCADetailBand;
import uk.gov.courtservice.xhibit.business.database.classes.RRCADetailCase;
import uk.gov.courtservice.xhibit.business.database.classes.RRCADetailSite;
import uk.gov.courtservice.xhibit.business.database.classes.RRCADetailStatus;
import uk.gov.courtservice.xhibit.business.database.classes.RRCASummaryBand;
import uk.gov.courtservice.xhibit.business.database.classes.RRCASummaryBandRows;
import uk.gov.courtservice.xhibit.business.database.classes.RRCASummaryDetail;
import uk.gov.courtservice.xhibit.business.database.classes.RRCASummarySite;
import uk.gov.courtservice.xhibit.business.database.classes.RRECCase;
import uk.gov.courtservice.xhibit.business.database.classes.RRECCaseSubheading;
import uk.gov.courtservice.xhibit.business.database.classes.RRECDetailSite;
import uk.gov.courtservice.xhibit.business.database.classes.RRECSite;
import uk.gov.courtservice.xhibit.business.database.classes.RRECSummaryDetail;
import uk.gov.courtservice.xhibit.business.database.classes.RRECType;
import uk.gov.courtservice.xhibit.business.database.classes.RRECTypeRows;
import uk.gov.courtservice.xhibit.business.database.classes.RSITRoom;
import uk.gov.courtservice.xhibit.business.database.classes.RSITSite;
import uk.gov.courtservice.xhibit.business.database.classes.RSITTime;
import uk.gov.courtservice.xhibit.business.database.results.helper.ReportHelper;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSReportList;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRLReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPExReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPExReportValue;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPReportValue;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCCaseNumReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCReport;
import uk.gov.courtservice.xhibit.common.results.vos.ISingleRunLetterReport;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDate;
import uk.gov.courtservice.xhibit.common.results.vos.LODReport;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXDefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXFixtureValue;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXSolicitorValue;
import uk.gov.courtservice.xhibit.common.results.vos.NHACaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.NHAObjectorValue;
import uk.gov.courtservice.xhibit.common.results.vos.NHAReport;
import uk.gov.courtservice.xhibit.common.results.vos.NTRSFReport;
import uk.gov.courtservice.xhibit.common.results.vos.NTRSFReportValue;
import uk.gov.courtservice.xhibit.common.results.vos.OBWPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.OUTCReport;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISReport;
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReport;
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReportValue;
import uk.gov.courtservice.xhibit.common.results.vos.RELCJReport;
import uk.gov.courtservice.xhibit.common.results.vos.RJSJudgeType;
import uk.gov.courtservice.xhibit.common.results.vos.RJSJudges;
import uk.gov.courtservice.xhibit.common.results.vos.RJSReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRCAReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRECReport;
import uk.gov.courtservice.xhibit.common.results.vos.RSITReport;
import uk.gov.courtservice.xhibit.common.results.vos.RUMOReport;
import uk.gov.courtservice.xhibit.common.results.vos.UNLCReport;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class ReportDatabaseManager extends AbstractXhibitDatabase{

	public DOCARPrintValue getDOCARValues(Date formsSentDate, Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getDOCARValues(formsSentDate=%s, courtId=%d)", formsSentDate, courtId));
		}
		String GET_DOCAR_REPORT = "{ call xhb_report_pkg.get_docar_report(?,?,?) }";
		final DOCARRowProcessor rp = new DOCARRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_DOCAR_REPORT);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {new java.sql.Date(formsSentDate.getTime()) , courtId });
        DOCARPrintValue docarPrintValue = rp.getDOCARValues();
        // Set Report Parameters
        docarPrintValue.setDateOfRequest(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
        docarPrintValue.setPriorToDate(XDateFormat.format(formsSentDate, XDateFormat.DATEFORMAT));
        
        logResult(docarPrintValue);
        return docarPrintValue;
	}

	public ADJSSReportList getListOfDefendantsPutBackReport(Integer courtId, String putBackType, String reportName) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getListOfDefendantsPutBackReport(courtId=%d, putBackType=%s, reportName=%s)", courtId, putBackType, reportName));
		}
		String defssReportQueryString = "{ call xhb_report_pkg.get_defendants_put_back_report(?,?,?,?) }";
		final ADJSSRowProcessor rp = new ADJSSRowProcessor();
		final StoredProcedure sp = createStoredProcedure(defssReportQueryString);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR });
		sp.setRowProcessor(rp);
		sp.execute(new Object[]{courtId, putBackType, reportName});
		ADJSSReportList adjssReport = rp.getADJSSReportList();
		adjssReport.setDateOfRequest(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
		
		logResult(adjssReport);
		return adjssReport;
	}
	
	public PRLISReport getPRLISReport(Integer courtId, Integer previousReportId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getPRLISReport(previousReportId=%d, courtId=%d)", previousReportId, courtId));
		}
		final PRLISRowProcessor rp = new PRLISRowProcessor();
        final StoredProcedure sp = createStoredProcedure("{ call xhb_report_pkg.get_prlis_report(?, ?, ?) }");
        sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId, previousReportId });
        PRLISReport prlisReport = rp.getPRLISReport();
        prlisReport.setDateofReport(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
        
        logResult(prlisReport);
        return prlisReport;
	}
	
	public void publishRunningList(Integer[] publishedCases, Integer courtId){
		if (log.isDebugEnabled()) {
			log.debug(String.format("publishRunningList(publishedCases=%s, courtId=%d)", Arrays.toString(publishedCases), courtId));
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_report_pkg.publish_running_list(?, ?) }");
        sp.registerInTypes(new int[] { Types.CLOB, Types.INTEGER });
        sp.executeUpdate(new Object[] { Arrays.toString(publishedCases).replaceAll("\\[|\\]|\\s", ""), courtId });
		
	}

	public NFIXReport getNFIXReport(Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getNFIXReport(courtId=%d)", courtId));
		}
		String getNFIXReport = "{ call xhb_report_pkg.get_nfix_report(?,?) }";
		
		final RecursiveRowProcessorConfiguration defendantRRPC = new RecursiveRowProcessorConfiguration("solicitorId","defendants",NFIXDefendantValue.class);
		final RecursiveRowProcessorConfiguration solicitorRRPC = new RecursiveRowProcessorConfiguration("fixtureId","nfixSolicitorValues",NFIXSolicitorValue.class, defendantRRPC);
		final RecursiveReflectionRowProcessor fixturesRRRP = new RecursiveReflectionRowProcessor(NFIXFixtureValue.class,solicitorRRPC);
		fixturesRRRP.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure(getNFIXReport);
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.setRowProcessor(fixturesRRRP);
		sp.execute(new Object[]{ courtId });
		NFIXReport nfixReport = new NFIXReport();
		nfixReport.setNfixFixtureValues(fixturesRRRP.getResults());
		nfixReport.setDateOfRequest(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
		
		logResult(nfixReport);
		return nfixReport;
	}

	public CFIXReport getCFIXReport(Integer courtId,Date hearingFromDate,Date hearingEndDate  ) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getCFIXReport(courtId=%d, hearingFromDate=%s, hearingEndDate=%s)", courtId, hearingFromDate, hearingEndDate));
		}
		String GET_CFIX_REPORT = "{ call xhb_report_pkg.get_cfix_report(?,?,?,?) }";
		final RecursiveRowProcessorConfiguration notesRRPC = new RecursiveRowProcessorConfiguration("caseDiaryFixtureId","notes",Notes.class);
		final RecursiveReflectionRowProcessor hearingsRRRP = new RecursiveReflectionRowProcessor(Hearings.class,notesRRPC);
		hearingsRRRP.registerDefaultBindings();
        final StoredProcedure sp = createStoredProcedure(GET_CFIX_REPORT);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.DATE  });
        sp.setRowProcessor(hearingsRRRP); 
        java.sql.Date sqlHearingEndDate = hearingEndDate  != null ? new java.sql.Date(hearingEndDate.getTime()) : null;
        sp.execute(new Object[] {courtId,new java.sql.Date(hearingFromDate.getTime()) , sqlHearingEndDate});
       
        @SuppressWarnings("unchecked")
		ArrayList<Hearings> hearings = (ArrayList<Hearings>)hearingsRRRP.getResults();
        CFIXReport cfixreport =  new CFIXReport ();
        cfixreport.setHearingValues(hearings);
   
        cfixreport.setHearingFromDate(XDateFormat.format(hearingFromDate, XDateFormat.DATEFORMAT));
        cfixreport.setHearingEndDate(XDateFormat.format(hearingEndDate, XDateFormat.DATEFORMAT));
        
        logResult(cfixreport);
        return cfixreport;
	}
	
	public LODReport getLODReport(Integer courtId, Date diaryDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getLODReport(courtId=%d, diaryDate = %s)", courtId, diaryDate));
		}
		String GET_LOD_REPORT = "{ call xhb_report_pkg.get_list_officers_diary_report(?,?,?) }";
		final LODRowProcessor rp = new LODRowProcessor();
		final StoredProcedure sp = createStoredProcedure(GET_LOD_REPORT);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId,new java.sql.Date(diaryDate.getTime())});
		LODReport lodReport = rp.getLodReport();
	
		logResult(lodReport);
		return lodReport;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public LODReport getLODBetweenDatesReport(Integer courtId, Date fromDate, Date toDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("LODReport(courtId=%d, fromDate=%s, toDate=%s)", courtId, fromDate, toDate));
		}
		
		String GET_LOD_REPORT_BETWEEN_DATES = "{ call xhb_report_pkg.get_lod_report_between_dates(?,?,?,?)}";
		Date parsedDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		java.sql.Date toDateParam = null;;
		LODReport lodReport = new LODReport();
		DiaryInfo diaryInfoLit;
		
		if (toDate != null) {
			toDateParam = new java.sql.Date(toDate.getTime());
		}
		final RecursiveRowProcessorConfiguration diaryRRPC  = new RecursiveRowProcessorConfiguration("caseNumber","diaries",Diary.class);
		final RecursiveRowProcessorConfiguration caseRRRP = new RecursiveRowProcessorConfiguration("diaryDate","cases",Case.class,diaryRRPC);
		final RecursiveReflectionRowProcessor diaryInfoRRRP = new RecursiveReflectionRowProcessor(DiaryInfo.class,caseRRRP);
		diaryInfoRRRP.registerDefaultBindings();
		
		final StoredProcedure sp = createStoredProcedure(GET_LOD_REPORT_BETWEEN_DATES);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.DATE });
		sp.setRowProcessor(diaryInfoRRRP);
		sp.execute(new Object[] {courtId,new java.sql.Date(fromDate.getTime()),toDateParam});
		ArrayList<DiaryInfo> diaryInfo = (ArrayList<DiaryInfo>) diaryInfoRRRP.getResults(); 
		
		ListIterator<DiaryInfo> lit = diaryInfo.listIterator();
		
		if (diaryInfo != null) {
			while (lit.hasNext()) {
				diaryInfoLit = lit.next();
				try {
					parsedDate = dateFormat.parse(diaryInfoLit.getDiaryDate());
				} catch (ParseException e) {
						 
	}	
	
				diaryInfoLit.setDiaryDate((XDateFormat.format(new java.sql.Timestamp(parsedDate.getTime()),XDateFormat.DAYOFWEEKFORMAT)));
				
				for (Case c:diaryInfoLit.getCases()) {
					for (Diary d:c.getDiaries()) {
						try {
							parsedDate = dateFormat.parse(d.getCreationDate());
						} catch (ParseException e) {
								 
						}
						d.setCreationDate(XDateFormat.format(new java.sql.Timestamp(parsedDate.getTime()),XDateFormat.DATEFORMAT));
					}
				}
			}
		}	
		
		lodReport.setLodDiaryInfo(diaryInfo);
		
		logResult(lodReport);
		return lodReport;
	}	
	
	public NHAReport getNHAReport(Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getNHAReport(courtId=%d)", courtId));
		}
		NHAObjectorValue objector;

		// Run main report content
		String getNHAReport = "{ call xhb_report_pkg.getAppealHearingNotifnRpt(?,?) }";	
		final NHARowProcessor rp = new NHARowProcessor();		
		final StoredProcedure sp = createStoredProcedure(getNHAReport);		
		sp.registerInTypes(new int[] { Types.INTEGER });		
		sp.setRowProcessor(rp);		
		sp.execute(new Object[]{ courtId });		
		NHAReport nhaReport = rp.getNHAReport();
		
		// Retrieve any objectors (may be more than one per case)
		String getNHAObjectorsReport = "{ call xhb_report_pkg.getNHAMiscAppealObjectors(?,?) }";
		final NHAObjectorsRowProcessor rp2 = new NHAObjectorsRowProcessor();		
		final StoredProcedure sp2 = createStoredProcedure(getNHAObjectorsReport);		
		sp2.registerInTypes(new int[] { Types.INTEGER });		
		sp2.setRowProcessor(rp2);	
		sp2.execute(new Object[]{ courtId });
		ArrayList<NHAObjectorValue> objectorList =  (ArrayList<NHAObjectorValue>) rp2.getNHAObjectorList();
		ListIterator<NHAObjectorValue> lit = objectorList.listIterator();
		while (lit.hasNext()) {
			// Loop through the objectors returned and attach them to the relevant cases
			objector = lit.next();
			for (NHACaseValue nhaCase : nhaReport.getNHACaseValues()) {
				if ( nhaCase.getCaseId().equals(objector.getCaseId()) ) {
					// Objector belongs to this case so add it to it's Objector list
					nhaCase.getObjectors().add(objector);
				}
			}
		}
		
		logResult(nhaReport);
		return nhaReport;			
	}
	
	public void setLettersSentFlagOnCases(ISingleRunLetterReport reportRun) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("setLettersSentFlagOnCases(casesList=%s)", Arrays.toString(reportRun.getReportedIDs())));
		}
		String setLettersSentFlagOnCasesProcedureName = "{ call " + reportRun.getDatabaseUpdateProcedureName() + "(?) }" ;
		final StoredProcedure sp = createStoredProcedure(setLettersSentFlagOnCasesProcedureName);
		sp.registerInTypes(new int[] { Types.CLOB });
		sp.executeUpdate(new Object[] {Arrays.toString(reportRun.getReportedIDs()).replaceAll("\\[|\\]|\\s", "")});
	}
	
	public LFIXRunDate getReportRunDate(Integer courtId, String reportType) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getReportRunDate(courtId=%d, reportType=%s)", courtId, reportType));
		}
		
		String GET_RUN_DATE = "{ call xhb_report_pkg.get_run_date(?,?,?) }";
		final LFIXRunDateProcessor rp = new LFIXRunDateProcessor();
		final StoredProcedure sp = createStoredProcedure(GET_RUN_DATE);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, reportType});
		LFIXRunDate lfixRunDate = rp.getLfixRunDate();
		return lfixRunDate;
	}
	
	public void updateLFIXCaseDiaryFixture(String list) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("updateLFIXCaseDiaryFixture(list=%s)", list));
		}
		String UPDATE_CASE_DIARY_RUN_DATE = "{ call xhb_report_pkg.update_case_diary_fix_run_date(?) }";
		final StoredProcedure sp = createStoredProcedure(UPDATE_CASE_DIARY_RUN_DATE);
		sp.registerInTypes(new int[] { Types.CLOB });
		sp.executeUpdate(new Object[] {list});
	}
	
	public void updateCTLRLCaseReminderPrinted(String list) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("updateCTLRLCaseReminderPrinted(list=%s)", list));
		}
		String UPDATE_CASE_REMINDER_PRINTED = "{ call xhb_report_pkg.update_case_reminder_printed(?) }";
		final StoredProcedure sp = createStoredProcedure(UPDATE_CASE_REMINDER_PRINTED);
		sp.registerInTypes(new int[] { Types.CLOB });
		sp.executeUpdate(new Object[] {list});
	}
	
	@SuppressWarnings("unchecked")
	public LFIXReport getLFIXReport(Integer courtId, Date runDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getLFIXReport(courtId=%d, runDate=%s)", courtId, runDate));
		}
		String GET_LFIX_REPORT = "{ call xhb_report_pkg.get_list_of_fixed_dates_report(?,?,?,?) }";
		final Integer runAgainInd = (null == runDate) ? 0 : 1;
		if ( null == runDate ) {
			runDate = new Date();
		}
		final LFIXReport lfixReport = new LFIXReport();
		final StoredProcedure sp = createStoredProcedure(GET_LFIX_REPORT);
		final RecursiveRowProcessorConfiguration defRRPC = new RecursiveRowProcessorConfiguration("casediaryfixture","defendants",LFIXDefendant.class);
		final RecursiveRowProcessorConfiguration fixtureRRPC = new RecursiveRowProcessorConfiguration("caseid","fixtures",LFIXCaseDiaryFixture.class,defRRPC);
		final RecursiveRowProcessorConfiguration caseRRPC = new RecursiveRowProcessorConfiguration("hearingtype","cases",LFIXCase.class,fixtureRRPC);
		final RecursiveReflectionRowProcessor hearingRRRP = new RecursiveReflectionRowProcessor(LFIXHearing.class,caseRRPC);
		String list = "";
		LFIXHearing hearing;
		hearingRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.INTEGER });
		sp.setRowProcessor(hearingRRRP);
		sp.execute(new Object[] {courtId,new java.sql.Date(runDate.getTime()),runAgainInd});
		ArrayList<LFIXHearing> results =  (ArrayList<LFIXHearing>) hearingRRRP.getResults();
		ListIterator<LFIXHearing> lit = results.listIterator();
		
		if (lit.hasNext()) {
			hearing = lit.next();
			lfixReport.setCourtTelephone(hearing.getCourttelephoneno());
			lfixReport.setCourtAddress(hearing.getCourtaddress());
		}
		
		lit = results.listIterator();
		while (lit.hasNext()) {
			hearing = lit.next();
			for (LFIXCase lc:hearing.getCases()) {
				for ( LFIXCaseDiaryFixture lcdf:lc.getFixtures() ) {
					list += lcdf.getCasediaryfixture() + ",";
				}
			}
		}
		
		if (list!="") {
			list = list.substring(0, list.length()-1);
		}
		
		lfixReport.setFixtureList(list);
		lfixReport.setLfixReportValues(results);
		
		logResult(lfixReport);
		return lfixReport;
	}
	
	public DARTSPrintValue getDARTSReport(Integer courtId, Date startDate, Date endDate) {
		
		if (log.isDebugEnabled()) {
			log.debug(String.format("getDARTSReport(courtId=%d, startDate=%s, endDate=%s)", courtId, startDate, endDate));
		}
		if ( null == startDate ) {
			startDate = new Date();
		}
		if ( null == endDate ) {
			endDate = new Date();
		}
		String SQL_STATEMENT = "{ call xhb_report_pkg.get_darts_report(?,?,?,?) }";
		final DARTSRowProcessor rp = new DARTSRowProcessor();
        final StoredProcedure sp = createStoredProcedure(SQL_STATEMENT);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.DATE});
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {courtId,new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime())});
        DARTSPrintValue reportData = rp.getDARTSValues();
        // Set Report Parameters
        reportData.setDateOfRequest(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
        reportData.setStartDate(XDateFormat.format(startDate, XDateFormat.DATEFORMAT));
        reportData.setEndDate(XDateFormat.format(endDate, XDateFormat.DATEFORMAT));
        
        logResult(reportData);
        return reportData;
	}
	
	public OBWPrintValue getOBWValues(Date bwIssueDate, Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getOBWValues(courtId=%d, bwIssueDate=%s)", courtId, bwIssueDate));
		}
		
		java.sql.Date toDate = null;
		if (bwIssueDate != null) {
			toDate = new java.sql.Date (bwIssueDate.getTime());
		}
		
		String GET_OBW_REPORT = "{ call xhb_report_pkg.get_obw_report(?,?,?) }";
		final OBWRowProcessor rp = new OBWRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_OBW_REPORT);
        sp.registerInTypes(new int[] { Types.DATE, Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {toDate , courtId });
        OBWPrintValue obwPrintValue = rp.getOBWValues();
        obwPrintValue.setDateOfRequest(XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT));
        obwPrintValue.setPriorToDate(XDateFormat.format(bwIssueDate, XDateFormat.DATEFORMAT));
        
        logResult(obwPrintValue);
        return obwPrintValue;
	}
	
	@SuppressWarnings("unchecked")
	public CTLRPReport getCTLRPReport(Integer courtId, Date timeLimitDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getCTLRPReport(courtId=%d, timeLimitDate=%s)", courtId, timeLimitDate));
		}
		String GET_CTLRP_REPORT = "{ call xhb_report_pkg.get_ctlrp_report(?,?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_CTLRP_REPORT);
		final CTLRPReport ctlrpReport = new CTLRPReport();
		final ReflectionRowProcessor ctlrpRowProcessor = new ReflectionRowProcessor(CTLRPReportValue.class);
		ctlrpRowProcessor.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(ctlrpRowProcessor);
		sp.execute(new Object[] {courtId,new java.sql.Date(timeLimitDate.getTime())});
		ArrayList<CTLRPReport> results =  (ArrayList<CTLRPReport>) ctlrpRowProcessor.getResults();
		ctlrpReport.setCtlrpValues(results);
		
		logResult(ctlrpReport);
		return ctlrpReport;
	}
	
	@SuppressWarnings("unchecked")
	public CTLRPExReport getCTLRPExReport(Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getCTLRPExReport(courtId=%d)", courtId));
		}
		String GET_CTLRPEX_REPORT = "{ call xhb_report_pkg.get_ctlrpex_report(?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_CTLRPEX_REPORT);
		final CTLRPExReport ctlrpexReport = new CTLRPExReport();
		final ReflectionRowProcessor ctlrpExRowProcessor = new ReflectionRowProcessor(CTLRPExReportValue.class);
		ctlrpExRowProcessor.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.setRowProcessor(ctlrpExRowProcessor);
		sp.execute(new Object[] {courtId});
		ArrayList<CTLRPExReport> results =  (ArrayList<CTLRPExReport>) ctlrpExRowProcessor.getResults();
		ctlrpexReport.setCtlrpexValues(results);
		
		logResult(ctlrpexReport);
		return ctlrpexReport;
	}
	
	@SuppressWarnings("unchecked")
	public RAGEReport getRAGEPReport(Integer courtId, String bcStatus, String classCode, Integer fromBetween, Integer toBetween) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRAGEPReport(courtId=%d, bcStatus=%s, classcode=%s, fromBetween=%d and toBetween=%d)", courtId, bcStatus, classCode, fromBetween, toBetween));
		}
		String GET_RAGE_REPORT = "{ call xhb_report_pkg.get_rage_report(?,?,?,?,?,?)}";
		final StoredProcedure sp = createStoredProcedure(GET_RAGE_REPORT);
		final RAGEReport rageReport = new RAGEReport();
		final ReflectionRowProcessor rageRowProcessor = new ReflectionRowProcessor(RAGEReportValue.class);
		rageRowProcessor.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(rageRowProcessor);
		sp.execute(new Object[] {courtId,bcStatus,classCode,fromBetween,toBetween});
		ArrayList<RAGEReport> results =  (ArrayList<RAGEReport>) rageRowProcessor.getResults();
		rageReport.setRageValues(results);
		
		logResult(rageReport);
		return rageReport;
	}
	
	public OUTCReport getOUTCReport(Integer courtId,String caseType,String caseClass,String bcStatus,			
			String hearingTypeCode,Integer timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,
			String SecureCourtRoom,String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy){	
		if (log.isDebugEnabled()) {
			log.debug(String.format("getOUTCReport(courtId=%d, bcStatus=%s, caseClass=%s, hearingTypeCode=%s, timesEstFrom=%d, units=%d, refJudgeType=%d," 
					+ "judgeDescription=%s, unitWeeks=%d, secureCourtRoom=%s, juvinileOnly=%s, RestrictedNotes=%s, standardNotes=%s, sortBy=%s)"
					, courtId, bcStatus, caseClass, hearingTypeCode, timeEstFrom, units, refJudgeType, judgeDescription, unitsWeeks
					, SecureCourtRoom, juvenileOnly, priorityNotes, RestrictedNotes, standardNotes, sortBy));
		}
		String GET_OUTC_REPORT = "{call xhb_report_pkg.get_outc_report(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		final RecursiveRowProcessorConfiguration outstandingcasenotesRRPC = new RecursiveRowProcessorConfiguration("caseNumber","outstandingcasenotes",OutstandingCaseNotes.class);
		final RecursiveReflectionRowProcessor outstandingscasesvaluesRRRP = new RecursiveReflectionRowProcessor(OutstandingCasesValues.class,outstandingcasenotesRRPC);
		outstandingscasesvaluesRRRP.registerDefaultBindings();
		 final StoredProcedure sp = createStoredProcedure(GET_OUTC_REPORT);
		 sp.setRowProcessor(outstandingscasesvaluesRRRP);
		 sp.registerInTypes(new int [] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		 Types.VARCHAR,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,
		 Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});	 
		 sp.execute(new Object[] {courtId, caseType, caseClass, bcStatus, 
		 hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,unitsWeeks,
		 SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
		 sortBy});	 
		 @SuppressWarnings("unchecked")
		 ArrayList<OutstandingCasesValues> outstandingscasesvalues = (ArrayList<OutstandingCasesValues>)outstandingscasesvaluesRRRP.getResults();
	     OUTCReport outcreport =  new OUTCReport ();
	     outcreport.setOutstandingCasesValues(outstandingscasesvalues);
	     outcreport.setSortBy(sortBy);
		 outcreport.setBcStatus(bcStatus);
		 outcreport.setHearingType(hearingTypeCode);
		 outcreport.setJudgeDescription(judgeDescription);
		 outcreport.setCaseType(caseType);
		 outcreport.setCaseClass(caseClass);
		 outcreport.setStandardNotes(standardNotes);
		 outcreport.setPriorityNotes(priorityNotes);
		 outcreport.setRestrictedNotes(RestrictedNotes);     
		 
		 logResult(outcreport);
		 return outcreport;	 
	}
	
	public UNLCReport getUNLCReport(Integer courtId,String caseType,String caseClass,String bcStatus,			
			String hearingTypeCode,Integer timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,
			String SecureCourtRoom,String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy){	
		if (log.isDebugEnabled()) {
			log.debug(String.format("getUNLCReport(courtId=%d, bcStatus=%s, caseClass=%s, hearingTypeCode=%s, timesEstFrom=%d, units=%d, refJudgeType=%d," 
					+ "judgeDescription=%s, unitWeeks=%d, secureCourtRoom=%s, juvinileOnly=%s, RestrictedNotes=%s, standardNotes=%s, sortBy=%s)"
					, courtId, bcStatus, caseClass, hearingTypeCode, timeEstFrom, units, refJudgeType, judgeDescription, unitsWeeks
					, SecureCourtRoom, juvenileOnly, priorityNotes, RestrictedNotes, standardNotes, sortBy));
		}
		String GET_UNLC_REPORT = "{call xhb_report_pkg.get_unlc_report(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		final RecursiveRowProcessorConfiguration unlistedcasenotesRRPC = new RecursiveRowProcessorConfiguration("caseNumber","unlistedcasenotes",UnlistedCaseNotes.class);
		final RecursiveReflectionRowProcessor unlistedscasesvaluesRRRP = new RecursiveReflectionRowProcessor(UnlistedCasesValues.class,unlistedcasenotesRRPC);
		unlistedscasesvaluesRRRP.registerDefaultBindings();
		 final StoredProcedure sp = createStoredProcedure(GET_UNLC_REPORT);
		 sp.setRowProcessor(unlistedscasesvaluesRRRP);
		 sp.registerInTypes(new int [] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		 Types.VARCHAR,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,
		 Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});	 
		 sp.execute(new Object[] {courtId, caseType, caseClass, bcStatus, 
		 hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,unitsWeeks,
		 SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
		 sortBy});	 
		 @SuppressWarnings("unchecked")
		 ArrayList<UnlistedCasesValues> unlistedscasesvalues = (ArrayList<UnlistedCasesValues>)unlistedscasesvaluesRRRP.getResults();
	     UNLCReport unlcreport =  new UNLCReport ();
	     unlcreport.setUnlistedCasesValues(unlistedscasesvalues);
		 unlcreport.setSortBy(sortBy);
		 unlcreport.setBcStatus(bcStatus);
		 unlcreport.setHearingType(hearingTypeCode);
		 unlcreport.setJudgeDescription(judgeDescription);
		 unlcreport.setCaseType(caseType);
		 unlcreport.setCaseClass(caseClass);
		 unlcreport.setStandardNotes(standardNotes);
		 unlcreport.setPriorityNotes(priorityNotes);
		 unlcreport.setRestrictedNotes(RestrictedNotes);		     
		 
		 logResult(unlcreport);
		 return unlcreport;
	}
	
	
	@SuppressWarnings("unchecked")
	public CTLRLReport getCTLRLReport(Integer courtId, Date timeLimitDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getCTLRLReport(courtId=%d, timeLimitDate=%s)", courtId, timeLimitDate));
		}
		String GET_CTLRL_REPORT = "{ call xhb_report_pkg.get_ctlrl_report(?,?,?)}";
		String casesList = "";
		final CTLRLReport ctlrlReport = new CTLRLReport();
		CTLRLCase ca;
		
		final RecursiveRowProcessorConfiguration expiryRRPC  = new RecursiveRowProcessorConfiguration("custodytimelimit","expiries",CTLRLExpiry.class);
		final RecursiveRowProcessorConfiguration expiryInfoRRPC  = new RecursiveRowProcessorConfiguration("casenumber","expiryinfos",CTLRLExpiryInfo.class,expiryRRPC);
		final RecursiveReflectionRowProcessor caseRRRP = new RecursiveReflectionRowProcessor(CTLRLCase.class,expiryInfoRRPC);
		caseRRRP.registerDefaultBindings();
		
		final StoredProcedure sp = createStoredProcedure(GET_CTLRL_REPORT);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(caseRRRP);
		sp.execute(new Object[] {courtId,new java.sql.Date(timeLimitDate.getTime())});
		ArrayList<CTLRLCase> caseData = (ArrayList<CTLRLCase>) caseRRRP.getResults(); 
		ctlrlReport.setCases(caseData);
		ListIterator<CTLRLCase> lit = caseData.listIterator();
		
		if (lit.hasNext()) {
			ca = lit.next();
			ctlrlReport.setCourtTelephone(ca.getCourttelephoneno());
			ctlrlReport.setCourtAddress(ca.getCourtaddress());
		}
		
		for ( CTLRLCase c : caseData ) {
			casesList = casesList + c.getCaseid() + ",";
		}
		
		if (casesList.length() > 0)
			casesList = casesList.substring(0, casesList.length()-1);
		
		ctlrlReport.setCasesList(casesList);
		  
		logResult(ctlrlReport);
		return ctlrlReport;
	}
	
	public DRSRReport getDRSRReport(Integer courtId, String monthPeriod,String yearPeriod ) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getDRSRReport(courtId=%d, monthPeriod=%s, yearPeriod=%s)", courtId, monthPeriod, yearPeriod));
		}
		String GET_DRSR_REPORT = "{ call xhb_report_pkg.get_drsr_report(?,?,?,?) }";
		final DRSRRowProcessor rp = new DRSRRowProcessor();
		final StoredProcedure sp = createStoredProcedure(GET_DRSR_REPORT);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR,Types.VARCHAR });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, monthPeriod, yearPeriod});
		DRSRReport drsrreport = rp.getDRSRReportValues();
		drsrreport.setTimeofReport(XDateFormat.format(Calendar.getInstance(), XDateFormat.TIMEFORMAT));	
		
		logResult(drsrreport);
		return drsrreport;
	}
	
	@SuppressWarnings("unchecked")
	public NTRSFReport getNTRSFReport(Integer courtId, Integer caseId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getNTRSFReport(courtId=%d, caseId=%d)", courtId, caseId));
		}
		String GET_NTRSF_REPORT = "{ call xhb_report_pkg.get_ntrsf_report(?,?,?)}";
		final StoredProcedure sp = createStoredProcedure(GET_NTRSF_REPORT);
		final NTRSFReport ntrsfReport = new NTRSFReport();
		final ReflectionRowProcessor ntrsfRowProcessor = new ReflectionRowProcessor(NTRSFReportValue.class);
		ntrsfRowProcessor.registerDefaultBindings();
		sp.registerInTypes(new int[] {Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(ntrsfRowProcessor);
		sp.execute(new Object[] {courtId, caseId});
		ArrayList<NTRSFReport> results =  (ArrayList<NTRSFReport>) ntrsfRowProcessor.getResults();
		ntrsfReport.setNtrsfValues(results);
	
		logResult(ntrsfReport);
		return ntrsfReport;
	}
	
	public RELCJReport getRELCJReport(Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRELCJReport(courtId=%d)", courtId));
		}
		String getRELCJReport = "{ call xhb_report_pkg.get_relcj_report(?,?)}";
		final ReflectionRowProcessor relcjRowProcessor = new ReflectionRowProcessor(RELCJValues.class);
		relcjRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure(getRELCJReport);
		sp.registerInTypes(new int[] { Types.INTEGER });	
		sp.setRowProcessor(relcjRowProcessor);
		sp.execute(new Object[]{ courtId });
		@SuppressWarnings("unchecked")
		ArrayList<RELCJValues> relcjValues = (ArrayList<RELCJValues>)relcjRowProcessor.getResults();
		RELCJReport relcjReport = new RELCJReport();
		relcjReport.setRelcjValues(relcjValues);
		
		logResult(relcjReport);
		return relcjReport;
	}
	
	public RJSReport getRJSReport(Integer courtId, Date sittingDate ) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRJSReport(courtId=%d, sittingDate=%s)", courtId, sittingDate));
		}
		String GET_RJS_REPORT = "{ call xhb_report_pkg.getRJS_Rpt(?,?,?) }";
		final RecursiveRowProcessorConfiguration rjsJudgesRRPC = new RecursiveRowProcessorConfiguration("judgeType","judges",RJSJudges.class);
		final RecursiveReflectionRowProcessor rjsJudgeTypeRRRP = new RecursiveReflectionRowProcessor(RJSJudgeType.class,rjsJudgesRRPC);
		rjsJudgeTypeRRRP.registerDefaultBindings();
		
		final StoredProcedure sp = createStoredProcedure(GET_RJS_REPORT);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(rjsJudgeTypeRRRP);
		sp.execute(new Object[] {courtId, new java.sql.Date(sittingDate.getTime())});
		
		@SuppressWarnings("unchecked")
		ArrayList<RJSJudgeType> rjsJudgeType = (ArrayList<RJSJudgeType>) rjsJudgeTypeRRRP.getResults();
		
		ListIterator<RJSJudgeType> lit = rjsJudgeType.listIterator();
		while (lit.hasNext()) {
			Integer totalSatInChambers = 0;
			Integer totalSatInCourt = 0;
			Integer totalAllSittings = 0;
			RJSJudgeType r = lit.next();
			for (RJSJudges j:r.getJudges()) {
				totalSatInChambers+=j.getSatInChambers();
				totalSatInCourt+=j.getSatInCourt();
				totalAllSittings+=j.getTotal();
			}
			
			r.setTotalSatInChambers(totalSatInChambers);
			r.setTotalSatInCourt(totalSatInCourt);
			r.setTotalAllSittings(totalAllSittings);
		}
		
		RJSReport report = new RJSReport(); 
		report.setRjsJudgeTypeValues(rjsJudgeType);
		
		logResult(report);
		return report;
	}
	
	public INFTRPCReport getINFTRPCReport(Integer courtId, String monthPeriod,String yearPeriod ) {	
		if (log.isDebugEnabled()) {
			log.debug(String.format("getINFTRPCReport(courtId=%d, monthPeriod=%s, yearPeriod=%s)", courtId, monthPeriod, yearPeriod));
		}
		String GET_INFTRPC_MAIN_REPORT = "{ call xhb_report_pkg.get_inftrpc_main_report(?,?,?,?) }";
		final INFTRPCRowProcessor rp = new INFTRPCRowProcessor();
		final StoredProcedure sp = createStoredProcedure(GET_INFTRPC_MAIN_REPORT);
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR,Types.VARCHAR });
		sp.setRowProcessor(rp);
		sp.execute(new Object[] {courtId, monthPeriod, yearPeriod});
		INFTRPCReport inftrcpReport = rp.getINFTRPCMainValues();
		inftrcpReport.setMonthOfReport(monthPeriod);
		inftrcpReport.setYearOfReport(yearPeriod);
		
		logResult(inftrcpReport);
		return inftrcpReport;
	}
	
	public INFTRPCCaseNumReport getINFTRPCCaseNumReport(Integer courtId, String monthPeriod,String yearPeriod ) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getINFTRPCCaseNumReport(courtId=%d, monthPeriod=%s, yearPeriod=%s)", courtId, monthPeriod, yearPeriod));
		}
		String GET_INFTRPC_CASE_NUM_REPORT = "{ call xhb_report_pkg.get_inftrpc_casenumbers_report(?,?,?,?) }";
		final RecursiveRowProcessorConfiguration casenumbersRRPC = new RecursiveRowProcessorConfiguration("code","casenumbers",CaseNumber.class);
		final RecursiveReflectionRowProcessor casedescriptionRRRP = new RecursiveReflectionRowProcessor(CaseDescription.class,casenumbersRRPC);
		casedescriptionRRRP.registerDefaultBindings();
        final StoredProcedure sp = createStoredProcedure(GET_INFTRPC_CASE_NUM_REPORT);
        sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR,Types.VARCHAR });
        sp.setRowProcessor(casedescriptionRRRP); 
        sp.execute(new Object[] {courtId, monthPeriod, yearPeriod});
        @SuppressWarnings("unchecked")
        ArrayList<CaseDescription>caseDescriptions = (ArrayList<CaseDescription>)casedescriptionRRRP.getResults();
        INFTRPCCaseNumReport inftrcpcaseNumReport = new INFTRPCCaseNumReport ();
        inftrcpcaseNumReport.setCaseDescriptionValues(caseDescriptions);
        for ( CaseDescription casedescription : caseDescriptions){     
        	casedescription.setTotal(casedescription.getCasenumbers().size());
        }
        inftrcpcaseNumReport.setMonthOfReport(monthPeriod);
        inftrcpcaseNumReport.setYearOfReport(yearPeriod);
        
        logResult(inftrcpcaseNumReport);
        return inftrcpcaseNumReport;
  	}
	
	public RUMOReport getRUMOReport(Integer courtId) {	
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRUMOReport(courtId=%d)", courtId));
		}
		String getRUMOReport = "{ call xhb_report_pkg.getRUMO_Rpt(?,?) }";
		final RecursiveRowProcessorConfiguration casesRRPC = new RecursiveRowProcessorConfiguration("collectCourtName","cases",Cases.class);
		final RecursiveReflectionRowProcessor collectCourtsRRRP = new RecursiveReflectionRowProcessor(CollectCourts.class,casesRRPC);
		collectCourtsRRRP.registerDefaultBindings();
				
		final StoredProcedure sp = createStoredProcedure(getRUMOReport);		
		sp.registerInTypes(new int[] { Types.INTEGER });		
		sp.setRowProcessor(collectCourtsRRRP);
		sp.execute(new Object[]{ courtId });
		
		@SuppressWarnings("unchecked")
		ArrayList<CollectCourts> collectCourts = (ArrayList<CollectCourts>) collectCourtsRRRP.getResults();
		RUMOReport rumoReport =  new RUMOReport ();
		rumoReport.setCollectCourtsValues(collectCourts);
		
		logResult(rumoReport);
		return rumoReport;			
	}

	@SuppressWarnings("unchecked")
	public RRCAReport getRRCASummaryReport(Integer courtId, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRRCASummaryReport(courtId=%d, endDate=%s)", courtId, endDate));
		}
		String GET_RRCA_SUMMARY_REPORT = "{ call xhb_report_pkg.get_rrca_summary(?,?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_RRCA_SUMMARY_REPORT);
		final RRCAReport rrcaReport = new RRCAReport();
		final RecursiveRowProcessorConfiguration bandRowsRRPC = new RecursiveRowProcessorConfiguration("age_band_code","bandrows",RRCASummaryBandRows.class);
		final RecursiveRowProcessorConfiguration bandRRRP = new RecursiveRowProcessorConfiguration("court_site_name","bands",RRCASummaryBand.class,bandRowsRRPC);
		final RecursiveReflectionRowProcessor siteRRRP = new RecursiveReflectionRowProcessor(RRCASummarySite.class,bandRRRP);
		siteRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.DATE, Types.INTEGER});
		sp.setRowProcessor(siteRRRP);
		
		sp.execute(new Object[] {new java.sql.Date(endDate.getTime()),courtId,});
		ArrayList<RRCASummarySite> results =  (ArrayList<RRCASummarySite>) siteRRRP.getResults();
		
		rrcaReport.setRrcaSummaryValues(results);
		
		logResult(rrcaReport);
		return rrcaReport;
}
	
	@SuppressWarnings("unchecked")
	public RRCAReport getRRCADetailReport(RRCAReport rrcaReport, Integer courtId, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRRCADetailReport(courtId=%d, endDate=%s)", courtId, endDate));
		}
		String GET_RRCA_REPORT = "{ call xhb_report_pkg.get_rrca_detail(?,?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_RRCA_REPORT);
		List rrcaSummaryDetail = new ArrayList<RRCADetailSite>();
		final RecursiveRowProcessorConfiguration caseRRPC = new RecursiveRowProcessorConfiguration("status","cases",RRCADetailCase.class);
		final RecursiveRowProcessorConfiguration statusRRRP = new RecursiveRowProcessorConfiguration("age_band","statuses",RRCADetailStatus.class,caseRRPC);
		final RecursiveRowProcessorConfiguration bandRRRP = new RecursiveRowProcessorConfiguration("court_site_name","bands",RRCADetailBand.class,statusRRRP);
		final RecursiveReflectionRowProcessor siteRRRP = new RecursiveReflectionRowProcessor(RRCADetailSite.class,bandRRRP);
		siteRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.DATE, Types.INTEGER});
		sp.setRowProcessor(siteRRRP);
		sp.execute(new Object[] {new java.sql.Date(endDate.getTime()),courtId});
		ArrayList<RRCADetailSite> results =  (ArrayList<RRCADetailSite>) siteRRRP.getResults();
		
		try {
			rrcaSummaryDetail = new ReportHelper<RRCASummarySite,RRCADetailSite,RRCASummaryDetail>(RRCASummarySite.class,RRCADetailSite.class,RRCASummaryDetail.class).
					buildSummaryAndDetailList(rrcaReport.getRrcaSummaryValues(),results);
		} catch (Exception e) {
			return null;
		}
		
		rrcaReport.setRrcaSummaryDetail((ArrayList) rrcaSummaryDetail);
		
		logResult(rrcaReport);
		return rrcaReport;
	}
	
	@SuppressWarnings("unchecked")
	public RRECReport getRRECDetailReport(RRECReport rrecReport, Integer courtId, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRRECDetailReport(courtId=%d, endDate=%s)", courtId, endDate));
		}
		String GET_RREC_REPORT = "{ call xhb_report_pkg.get_rrec_detail(?,?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_RREC_REPORT);
		
		final RecursiveRowProcessorConfiguration caseRRPC = new RecursiveRowProcessorConfiguration("case_subhdg","cases",RRECCase.class);
		final RecursiveRowProcessorConfiguration caseSubHeadingRRRP = new RecursiveRowProcessorConfiguration("court_site_name","subheadings",RRECCaseSubheading.class,caseRRPC);
		final RecursiveReflectionRowProcessor siteRRRP = new RecursiveReflectionRowProcessor(RRECDetailSite.class,caseSubHeadingRRRP);
		final List summaryDetail;
		siteRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE});
		sp.setRowProcessor(siteRRRP);
		sp.execute(new Object[] {courtId,endDate!=null?new java.sql.Date(endDate.getTime()):null});
		ArrayList<RRECDetailSite> results =  (ArrayList<RRECDetailSite>) siteRRRP.getResults();
		
		try {
			summaryDetail = new ReportHelper<RRECSite,RRECDetailSite,RRECSummaryDetail>(RRECSite.class,RRECDetailSite.class,RRECSummaryDetail.class).
					buildSummaryAndDetailList(rrecReport.getRrecSummaryValues(),results);
		} catch (Exception e) {
			return null;
		}
		
		rrecReport.setRrecSummaryDetail((ArrayList) summaryDetail);
		
		logResult(rrecReport);
		return rrecReport;
	}
	
	@SuppressWarnings("unchecked")
	public RRECReport getRRECSummaryReport(Integer courtId, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRRECSummaryReport(courtId=%d, endDate=%s)", courtId, endDate));
		}
		String GET_RREC_SUMMARY_REPORT = "{ call xhb_report_pkg.get_RREC_summary(?,?,?) }";
		final StoredProcedure sp = createStoredProcedure(GET_RREC_SUMMARY_REPORT);
		final RRECReport rrecReport = new RRECReport();
		final RecursiveRowProcessorConfiguration caseTypeRowsRRPC = new RecursiveRowProcessorConfiguration("case_type","typerows",RRECTypeRows.class);
		final RecursiveRowProcessorConfiguration caseTypeRRRP = new RecursiveRowProcessorConfiguration("court_site_name","types",RRECType.class,caseTypeRowsRRPC);
		final RecursiveReflectionRowProcessor siteRRRP = new RecursiveReflectionRowProcessor(RRECSite.class,caseTypeRRRP);
		siteRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE});
		sp.setRowProcessor(siteRRRP);
		
		sp.execute(new Object[] {courtId,endDate!=null?new java.sql.Date(endDate.getTime()):null});
		ArrayList<RRECSite> results =  (ArrayList<RRECSite>) siteRRRP.getResults();
		
		ListIterator<RRECSite> lit = results.listIterator();
		while (lit.hasNext()) {
			for (RRECType r:lit.next().getTypes()) {
				for (RRECTypeRows tr:r.getTyperows()) {
					tr.setSection6((tr.getSection2()+ tr.getSection3()+ tr.getSection4())- tr.getSection5());
					tr.setSection8((tr.getSection1()+ tr.getSection6())- tr.getSection7());
				}
			}
		}
		
		rrecReport.setRrecSummaryValues(results);
		
		logResult(rrecReport);
		return rrecReport;
	}
	
	@SuppressWarnings("unchecked")
	public RSITReport getRSITReport(Integer courtId, Integer courtSiteId, Date endDate) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getRSITReport(courtId=%d, courtsiteId=%d, endDate=%s)", courtId, courtSiteId, endDate));
		}
		String GET_RSIT_REPORT = "{ call xhb_report_pkg.getCtRmSittingTimes(?,?,?,?)}";
		
		final StoredProcedure sp = createStoredProcedure(GET_RSIT_REPORT);
		final RSITReport rsitReport = new RSITReport();
		final RecursiveRowProcessorConfiguration timeRRPC = new RecursiveRowProcessorConfiguration("court_room_no","times",RSITTime.class);
		final RecursiveRowProcessorConfiguration roomRRPC = new RecursiveRowProcessorConfiguration("court_site_name","rooms",RSITRoom.class,timeRRPC);
		final RecursiveReflectionRowProcessor siteRRRP = new RecursiveReflectionRowProcessor(RSITSite.class,roomRRPC);
		
		siteRRRP.registerDefaultBindings();
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.DATE});
		sp.setRowProcessor(siteRRRP);
		sp.execute(new Object[] {courtId, courtSiteId, new java.sql.Date(endDate.getTime())});
		
		ArrayList<RSITSite> results =  (ArrayList<RSITSite>) siteRRRP.getResults();
		ListIterator<RSITSite> lit = results.listIterator();
		RSITSite site;
		
		// Calculate the grand totals for each site
		Integer monday;
		Integer tuesday;
		Integer wednesday;
		Integer thursday;
		Integer friday;
		Integer saturday;
		Integer total_hours;
		Integer total_days;
		Integer total_days_inc;
		while (lit.hasNext()) {
			site = lit.next();
			monday = 0;
			tuesday = 0;
			wednesday = 0;
			thursday = 0;
			friday = 0;
			saturday = 0;
			total_hours = 0;
			total_days = 0;
			for (RSITRoom room : site.getRooms()) {
				total_days_inc = 0;
				for ( RSITTime time : room.getTimes() ) {
					if ( time.getAm_pm().equals("Total") ) {
						monday += time.getMonday();
						tuesday += time.getTuesday();
						wednesday += time.getWednesday();
						thursday += time.getThursday();
						friday += time.getFriday();
						saturday += time.getSaturday();
						total_hours += time.getTotal_hours();
						
						if (time.getMonday()>0)
							total_days_inc++;	
						
						if (time.getTuesday()>0)
							total_days_inc++;	
						
						if (time.getWednesday()>0)
							total_days_inc++;
						
						if (time.getThursday()>0)
							total_days_inc++;
						
						if (time.getFriday()>0)
							total_days_inc++;
						
						if (time.getSaturday()>0)
							total_days_inc++;
						
						time.setTotal_days(total_days_inc);
						total_days += time.getTotal_days();
					}
				}
			}
			site.setMonday(monday);
			site.setTuesday(tuesday);
			site.setWednesday(wednesday);
			site.setThursday(thursday);
			site.setFriday(friday);
			site.setSaturday(saturday);
			site.setTotal_hours(total_hours);
			site.setTotal_days(total_days);
		}
		rsitReport.setRsitValues(results);
		
		logResult(rsitReport);
		
		return rsitReport;
	}
	
	public String getAROInformation(Integer CaseId){
		if (log.isDebugEnabled()) {
			log.debug("getAROInformation(CaseId="+CaseId+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_orders_pkg.get_appeal_result_order(?) }");
		sf.registerInTypes(new int[] { Types.INTEGER });
		final String xml = StringStrategy.getValue((Clob) sf.executeFunction(new Object[] {CaseId}, Types.CLOB));
		return xml;
	}
	
	/**
     * Factory method used to create a <code>LoggedStoredProcedure</code> object to
     * represent the database function whose name is passed in.
     * 
     * @param name
     *            The name of the function we will be wanting to call.
     * @return The newly created <code>LoggedStoredProcedure</code>.
     */
	@Override
    protected StoredProcedure createStoredProcedure(final String name) {
        return new LoggedStoredProcedure(getDataSource(), name);
    }
	
	private void logResult(ReportAbsttractValue resultReportObject){		
		if(log.isTraceEnabled()){
			try{
				XMLServicesImpl xmlService = XMLServicesImpl.getInstance();
				Document xmlDocument = xmlService.createDocFromValue(resultReportObject);
				log.trace("Results received from database: " + xmlService.getStringXML(xmlDocument));	
			}
			catch(Throwable e){
				//If an error happens in here, I don't think we want to cause a fuss
				log.trace("Error whilst attempting to log results of report:", e);
			}
		}
	}
}