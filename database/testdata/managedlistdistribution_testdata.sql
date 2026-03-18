/*****************************
 *
 * Title: Adding a warned list into the tables: XHB_XML_DOCUMENT, XHB_WLL_CONTROL, XHB_WLL_DOCUMENT
 * Description: There is a warned list and associated there are warned list letters inside 
 * xml_document which is controlled through wll_control.
 * 
 *****************************
*/

-- Empty all related tables in the Managed List Distribution Application
DELETE FROM XHB_DOCUMENT_CONTROL;
DELETE FROM XHB_DOCUMENT_DISTRIBUTION;

DELETE FROM XHB_DOCUMENT_REPLY;
DELETE FROM XHB_DOCUMENT_RECIPIENT;

DELETE FROM XHB_FORMATTING;

DELETE FROM XHB_RECIPIENT;

DELETE FROM XHB_WLL_DOCUMENT;
DELETE FROM XHB_WLL_CONTROL;
DELETE FROM XHB_WLL_RECIPIENT;

DELETE FROM XHB_XML_DOCUMENT;

COMMIT;

-- Warned List Letter 
INSERT INTO XHB_XML_DOCUMENT (XML_DOCUMENT_ID, DATE_CREATED, DOCUMENT_TITLE, XML_DOCUMENT, STATUS, EXPIRY_DATE, DOCUMENT_TYPE, COURT_ID ) VALUES
	   		 				  (1, SYSDATE,'WARNED LIST','<?xml version="1.0" encoding="UTF-8"?>
<xhibit:WarnedListLetters xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice">
	<xhibit:WarnedListLetter xmlns:xhibit="http://www.courtservice.gov.uk/schemas/courtservice/xhibit" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice">
		<xhibit:CrownCourt>
			<cs:CourtHouseType>Crown Court</cs:CourtHouseType>
			<cs:CourtHouseCode>453</cs:CourtHouseCode>
			<cs:CourtHouseName>SNARESBROOK</cs:CourtHouseName>
			<cs:CourtHouseAddress>
				<Line>THE COURT HOUSE</Line>
				<Line>75 HOLLYBUSH HILL</Line>
				<Line>SNARESBROOK</Line>
				<Line>LONDON</Line>
				<PostCode>E11 1QW</PostCode>
			</cs:CourtHouseAddress>
			<cs:CourtHouseDX>DX 98240 WANSTEAD 2</cs:CourtHouseDX>
			<cs:CourtHouseTelephone>020 8530 0000</cs:CourtHouseTelephone>
		</xhibit:CrownCourt>
		<xhibit:Recipient>
			<xhibit:Solicitor>
				<cs:Organisation>
					<cs:OrganisationCode>12389</cs:OrganisationCode>
					<cs:OrganisationName/>
				</cs:Organisation>
			</xhibit:Solicitor>
		</xhibit:Recipient>
		<xhibit:ListingInstructions>
			<xhibit:ListingInstruction>Any application to remove ANY case committed over 18 weeks ago will have to be made to one of the appointed resident Judges. Please advise the List Office immediately of any change of circumstances.</xhibit:ListingInstruction>
			<xhibit:ListingInstruction>No later than 10:00 am on 12 December 2003</xhibit:ListingInstruction>
			<xhibit:WarnedDate StartDate="2004-12-15" EndDate="2004-12-21"/>
		</xhibit:ListingInstructions>
		<xhibit:Hearing HearingType="">
			<xhibit:Case>
				<xhibit:CaseNumber/>
				<xhibit:Defendant>
					<xhibit:PersonalDetails>
						<cs:IsMasked/>
					</xhibit:PersonalDetails>
					<xhibit:CRESTdefendantID/>
				</xhibit:Defendant>
			</xhibit:Case>
			<xhibit:CourtHouseLocation/>
		</xhibit:Hearing>
	</xhibit:WarnedListLetter>
</xhibit:WarnedListLetters>', 'PR',SYSDATE + 1, 'WL', 3);

INSERT INTO XHB_WLL_CONTROL  (WLL_CONTROL_ID, STATUS, EXPIRY_DATE, XML_DOCUMENT_ID) VALUES
	                                                    (1, 'ND',SYSDATE + 1, 1);

INSERT INTO XHB_WLL_RECIPIENT ( WLL_RECIPIENT_ID, CREST_SOLICITOR_FIRM_ID, SOLICITOR_FIRM_NAME, SOLICTIOR_FIRM_ADDRESS, SOLICITOR_FIRM_FAX, SOLICITOR_FIRM_EMAIL, COURT_ID ) VALUES 
	   						  ( 1, 3415, 'SOLICITOR FIRM NAME 1', 'SOLICITOR FIRM ADDRESS 1', 'SOLICITOR FIRM FAX 1', 'SOLICITOR FIRM EMAIL 1',  3);

INSERT INTO XHB_WLL_DOCUMENT (WLL_DOCUMENT_ID, WLL_RECIPIENT_ID, WLL_CONTROL_ID, XML_DOCUMENT_ID ) VALUES
	   		                 (1, 1, 1, 1);	

INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DOC_DISTRIBUTION_ID, DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID ) VALUES 
	   								  ( 1, 'POST', 'WL', 'XML', NULL, 1, 3);
									  
INSERT INTO XHB_RECIPIENT ( RECIPIENT_ID, RECIPIENT_NAME, FAX_NUMBER, EMAIL_ADDRESS, COURT_ID ) VALUES 
	   					  ( 1, 'RECIPIENT NAME 1', 'RECIPIENT FAX NUMBER 1', 'RECIPIENT EMAIL 1', 3);
						  
COMMIT;						  
			   
			  