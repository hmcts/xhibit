---------------------------------------------------------------------
--CCN 1263 - CHANGES RELATING TO THE CREATION OF NEW VERDICT MESSAGES
---------------------------------------------------------------------


-- INSERT Corresponding events into EXISS database, using table EXI_REF_TYPE
--GJ
INSERT INTO exiss.exi_ref_type t 
VALUES(null, 2, 23, '11320', 'Guilty (By Judge alone under DVC_VA 2004)', '', 'http://www.courtservice.gov.uk/schemas/courtservice',
'', '', '', '0.22');

--GAOJ
INSERT INTO exiss.exi_ref_type t 
VALUES(null, 2, 23, '11321', 'Not Guilty but Guilty of Alternative Offence not charged namely(By Judge alone under DVC_VA 2004)', '', 'http://www.courtservice.gov.uk/schemas/courtservice',
'', '', '', '0.22');

--GLOJ
INSERT INTO exiss.exi_ref_type t 
VALUES(null, 2, 23, '11322', 'Not Guilty but Guilty of Lesser Offence not charged namely(By Judge alone under DVC_VA 2004)', '', 'http://www.courtservice.gov.uk/schemas/courtservice',
'', '', '', '0.22');

---------------------------------------------------------------------
--CCN 701 - CHANGES RELATING TO THE CREATION OF NEW VERDICT MESSAGES
--------------------------------------------------------------------
--Admitted (Bail Act Offence)
insert into exi_ref_type (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION)
values (null, 2, 23, '11317', 'Admitted (Bail Act Offence)', '', 'http://www.courtservice.gov.uk/schemas/courtservice', '', '', '', '');

--Not Admitted (Bail Act Offence)
insert into exi_ref_type (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION)
values (null, 2, 23, '11318', 'Not Admitted (Bail Act Offence)', '', 'http://www.courtservice.gov.uk/schemas/courtservice', '', '', '', '');


COMMIT;
