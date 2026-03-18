---------------------------------------------------------------------
--CHANGES TO ADD SECURITY ROLES TO THE NEW DOCUMENTS 
---------------------------------------------------------------------


-- UPDATE the CJIT database, using table CJI_DOCUMENT_SECURITY

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     1,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     3,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     4,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     5,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     6,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     7,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     8,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     9,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     10,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     11,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     12,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWA'),
                     13,
                     sysdate);


-------------------------------------------------------------------------------------------------

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     1,
                     sysdate);


--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     3,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     4,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     5,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     6,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     7,
                     sysdate);

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     8,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     9,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     10,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     11,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     12,
                     sysdate);

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='BWB'),
                     13,
                     sysdate);


-------------------------------------------------------------------------------------------------

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     1,
                     sysdate);


--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     3,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     4,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     5,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     6,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     7,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     8,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     9,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     10,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     11,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     12,
                     sysdate);

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COC'),
                     13,
                     sysdate);

-------------------------------------------------------------------------------------------------

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE) 
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     1,
                     sysdate);


--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     3,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     4,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     5,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     6,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     7,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     8,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     9,
                     sysdate);

--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     10,
                     sysdate);
--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     11,
                     sysdate);
--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     12,
                     sysdate);
--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='COD'),
                     13,
                     sysdate);

-------------------------------------------------------------------------------------------------

--Suspended Sentence Order 
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     1,
                     sysdate);

--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     3,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     4,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     5,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     6,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     7,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     8,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     9,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     10,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     11,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     12,
                     sysdate);
--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID,
                                   DOCUMENT_TYPE_ID, 
                                   CJO_ROLE_ID,
                                   CREATION_DATE)  
              VALUES((select max(document_security_id) + 1 from cji_document_security),
                     (select document_type_id from cji_document_type where internal_code='SSO'),
                     13,
                     sysdate);

COMMIT;
