
update cji_document_type set STYLESHEET_NAME  = 'http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant-v2-2.xsl'
where document_type_id = 20;


update cji_document_type set STYLESHEET_NAME  = 'http://www.courtservice.gov.uk/transforms/courtservice/DailyList-v2.xsl'
where document_type_id = 21;
