/* FILE_NAME: XHIBIT2_Standing_Orders_Status_Data.sql*/
/* ENVIRONMENT: Live System, Pre-production, System test, Integration, Development */
/* DESCRIPTION: Status values for Orders - internal status and external delivery status (used by Mercator) */
/* STATUS DESCRIPTION: required for live system */
/* DEPENDENCIES: No dependencies on other reference data */
/* OWNER: Doug Climie*/
--///////////////////////////
--Order Status standing data.
--///////////////////////////
DELETE FROM XHB_ORDER_STATUS;

INSERT INTO XHB_ORDER_STATUS
SELECT	1,
	'NEW',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_STATUS
SELECT	2,
	'SAVED',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_STATUS
SELECT	3,
	'PRINTED',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_STATUS
SELECT	4,
	'SIGNED',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;
COMMIT;

--////////////////////////////////////
--Order Delivery Status standing data.
--////////////////////////////////////
DELETE FROM XHB_ORDER_DELIVERY_STATUS;

INSERT INTO XHB_ORDER_DELIVERY_STATUS
SELECT	1,
	'NOTREADY',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_DELIVERY_STATUS
SELECT	2,
	'READY',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_DELIVERY_STATUS
SELECT	3,
	'DELIVERED',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;

INSERT INTO XHB_ORDER_DELIVERY_STATUS
SELECT	4,
	'FAILED',
	1,
	USER,
	USER,
	SYSDATE,
	SYSDATE
FROM DUAL;
COMMIT;
