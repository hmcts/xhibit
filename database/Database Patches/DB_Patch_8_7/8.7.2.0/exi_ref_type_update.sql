-- PLEASE NOTE THIS SCRIPT MUST BE RUN UNDER THE EXISS SCHEMA (NOT XHIBIT)

UPDATE exi_ref_type
SET internal_name = 'Bench Warrant executed - Absconding admitted'
WHERE internal_code = '10404';

UPDATE exi_ref_type
SET internal_name = 'Bench Warrant executed - Absconding not admitted'
WHERE internal_code = '10405';

UPDATE exi_ref_type
SET internal_name = 'Bench Warrant executed - Absconding not put'
WHERE internal_code = '10406';

COMMIT;