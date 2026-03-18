DECLARE

	ln_clob_id	xhb_clob.clob_id%TYPE;
	lv_full_month	VARCHAR2(15);
	lv_full_day		VARCHAR2(15);
	lv_day			VARCHAR2(2);
	lv_year			VARCHAR2(4);
	lv_date			VARCHAR2(15);

BEGIN

	SELECT TO_CHAR(SYSDATE,'Month') INTO lv_full_month FROM DUAL; 	-- Full Month Name
	SELECT TO_CHAR(SYSDATE,'Day') INTO lv_full_day FROM DUAL;		-- Name of the Day
	SELECT TO_CHAR(SYSDATE,'DD') INTO lv_day FROM DUAL;				-- Date in Month
	SELECT TO_CHAR(SYSDATE,'YYYY') INTO lv_year FROM DUAL;			-- Year
	SELECT TO_CHAR(SYSDATE,'DD/MM/YY') INTO lv_date FROM DUAL;	-- Date DD/MM/YY

	SELECT XHB_CLOB_SEQ.NEXTVAL INTO ln_clob_id FROM DUAL;

	INSERT INTO XHB_CLOB (CLOB_ID, CLOB_DATA)
	VALUES(ln_clob_id, '<?xml version="1.0" encoding="UTF-8"?><?xml-stylesheet type="text/xsl" href="InternetWebPageTemplate.xsl"?><currentcourtstatus xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">'||
	'<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms>'||
	'<courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>0</activecase><hearingtype>Plea and Trial Preparation</hearingtype><judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>'||
	'<defendants><defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname></defendant><defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>'||
	'<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>'||
	'<currentstatus><event><time>10:00</time><date>'||lv_date||'</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>'||
	'</caseDetails><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype><hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><timestatusset>09:30</timestatusset>'||
	'<defendants><defendant><firstname>HOMER</firstname><middlename/><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/><currentstatus><event><time>10:24</time><date>'||lv_date||'</date><free_text>Witness 3 sworn in</free_text><type>CPP</type></event></currentstatus><movedfromcourtroomname>Court 4</movedfromcourtroomname></caseDetails>'||
	'</cases><timestatusset>09:30</timestatusset><courtroomname>Court 3</courtroomname></courtroom></courtrooms><floating><cases>'||
	'<caseDetails><cppurn>PDCase789</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype><hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>'||
	'<defendants><defendant><firstname>CLANCY</firstname><middlename/><lastname>WIGGUM</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/><currentstatus></currentstatus></caseDetails>'||
	'</cases></floating></courtsite></courtsites></court><datetimestamp><dayofweek>'||lv_full_day||'</dayofweek><date>'||lv_day||'</date><month>'||lv_full_month||'</month><year>'||lv_year||'</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>');

	INSERT INTO XHB_CPP_FORMATTING (STAGING_TABLE_ID, DATE_IN,FORMAT_STATUS,DOCUMENT_TYPE,COURT_ID,XML_DOCUMENT_CLOB_ID)
	VALUES(1, SYSDATE, 'ND', 'PD', 81, ln_clob_id);

	COMMIT;
	
END;

/