--Insert event details for verdict NGJ into the Exiss database
INSERT INTO exiss.exi_ref_type t 
VALUES(null, 2, 23, '11423', 'Not Guilty (By Judge alone under DVC and VA2004)', '', 'http://www.courtservice.gov.uk/schemas/courtservice',
'', '', '', '5.2');

UPDATE EXI_REF_TYPE t SET version = '5.2' WHERE group_id = 23;
COMMIT;
