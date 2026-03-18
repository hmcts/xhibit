-----------------------------------------------------------------------
-- Includes all changes to be made to the ExISS database 
-----------------------------------------------------------------------

--Update Custodial Order 5044C
UPDATE EXI_REF_TYPE SET INTERNAL_NAME='Young Offender Custodial Order 5044C' WHERE internal_code='COC';

--Update Custodial Order 5044D
UPDATE EXI_REF_TYPE SET INTERNAL_NAME='Young Offender Custodial Order 5044D' WHERE internal_code='COD';

COMMIT;