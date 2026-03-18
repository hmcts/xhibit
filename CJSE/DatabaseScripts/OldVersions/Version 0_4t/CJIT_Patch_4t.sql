/* This script will be used to update Production from 0_4s to the latest level - 0_4t */

update cji_document_type set STYLESHEET_NAME  = 'http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-1.xsl'
where document_type_id = 14;


update cji_document_type set STYLESHEET_NAME  = 'http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-1.xsl'
where document_type_id = 15;


update cji_document_type set STYLESHEET_NAME  = 'http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-1.xsl'
where document_type_id = 16;

