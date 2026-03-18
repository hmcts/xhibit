/* This script will be used to update Production from 0_4r to the latest level - 0_4s */


update cji_document_type set bits_document_config = 'BITS.CommittalRecordSheetHandler'
where document_type_id = 14;


