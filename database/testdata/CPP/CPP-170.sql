DECLARE

	ln_clob_id	xhb_clob.clob_id%TYPE;

BEGIN


	SELECT XHB_CLOB_SEQ.NEXTVAL INTO ln_clob_id FROM DUAL;

	INSERT INTO XHB_CLOB (CLOB_ID, CLOB_DATA)
	VALUES(ln_clob_id, '<?xml version="1.0" encoding="UTF-8"?><?xml-stylesheet type="text/xsl" href="InternetWebPageTemplate.xsl"?>
<currentcourtstatus xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>TEST2</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>aurn</cppurn><hearingtype>Plea and Trial Preparation</hearingtype></caseDetails></cases><defendants><defendant><firstname>CUSTODY</firstname><lastname>LIZCTL</lastname></defendant></defendants><currentstatus><event><time>17:59</time><date>30/10/19</date><hearing_id>768814</hearing_id><free_text/><process_linked_cases>false</process_linked_cases><defendant_on_case_id>958892</defendant_on_case_id><type>30600</type><defendant_name>CUSTODY LIZCTL</defendant_name><scheduled_hearing_id>777462</scheduled_hearing_id><defendant_masked_name/><defendant_masked_flag>N</defendant_masked_flag></event></currentstatus><timestatusset>17:59</timestatusset><courtroomname>Court 1</courtroomname></courtroom><courtroom><currentstatus/><courtroomname>Court 13</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Friday</dayofweek><date>08</date><month>November</month><year>2019</year><hour>11</hour><min>49</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>');
	INSERT INTO XHB_CPP_STAGING_INBOUND(document_name,court_code,document_type,time_loaded,clob_id,validation_status, acknowledgment_status) VALUES
('WebPage_453_20200804154828.xml',453,'WP',SYSDATE,ln_clob_id,'NP','AS');


	COMMIT;
	
END;

/