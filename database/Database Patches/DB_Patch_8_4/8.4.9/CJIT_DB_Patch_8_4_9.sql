--- updates the CJI_DOCUMENT_TYPE table in order to correct the BITS Handler - defect 6529

UPDATE CJI_DOCUMENT_TYPE SET BITS_DOCUMENT_CONFIG='BITS.CustodialOrderDHandler' where INTERNAL_CODE='COD';

--Update schema version number

@CJI_Document_Type.sql

Commit;
