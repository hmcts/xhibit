

--INSERT INTO EXISS WITH NEW REF OPERATION RECORD
INSERT INTO exi_ref_operation t
VALUES(6,'DEPORTATION','Deportation Event','DeportationEvent');


--INSERT INTO EXISS DB
INSERT INTO EXI_REF_TYPE
VALUES(417,
	6,
	23,
	13502,
	'Deportation Event',
	'',
	'http://www.courtservice.gov.uk/schemas/courtservice',
	'',
	'',
	'',
	1);



commit;

