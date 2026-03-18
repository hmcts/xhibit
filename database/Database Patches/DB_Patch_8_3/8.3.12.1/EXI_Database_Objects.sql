SET DEFINE OFF;
UPDATE EXI_REF_TYPE SET INTERNAL_NAME = 'Guilty (by Judge alone under DVC & VA 2004)' WHERE INTERNAL_CODE = '11420';

UPDATE EXI_REF_TYPE SET INTERNAL_NAME = 'Not Guilty (by Judge alone under DVC & VA 2004)' WHERE INTERNAL_CODE = '11423';

UPDATE EXI_REF_TYPE SET INTERNAL_NAME = 'Not Guilty but Guilty (by Judge alone under DVC & & VA 2004) of Alternative Offence not charged namely ' WHERE INTERNAL_CODE = '11421';

UPDATE EXI_REF_TYPE SET INTERNAL_NAME = 'Not Guilty but Guilty (By Judge alone under DVC & VA 2004) of Lesser Offence not charged namely ' WHERE INTERNAL_CODE = '11422';

INSERT INTO EXI_REF_TYPE (OPERATION_ID,GROUP_ID,INTERNAL_CODE,INTERNAL_NAME,VERSION)
VALUES (2,23,'12909','Restraining Order without Conviction (Protection from Harassment Act 1997 s5a)',5.2);

SET DEFINE ON;
commit;