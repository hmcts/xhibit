/* FILE_NAME: XHIBIT2_Standing_Skeleton_Delivery_Status.sql*/
/* ENVIRONMENT: Live System, Pre-production, System test, Integration, Development*/
/* DESCRIPTION: Skeleton Schedule Delivery Status standing data. Values indicate state of record and */
/* are used by Mercator to determine status of a skeleton schedule. */
/* File renamed from WitnessStandingData on 28/4/03 */
/* STATUS DESCRIPTION: required for live system */
/* DEPENDENCIES: No dependencies on other data. */
/* OWNER: Doug Climie */

-- Author: Doug Climie 13/03/2003
--////////////////////////////////////
--Skeleton Schedule Delivery Status standing data.
--////////////////////////////////////
DELETE FROM XHB_SKELETON_DELIVERY_STATUS;

INSERT INTO XHB_SKELETON_DELIVERY_STATUS
SELECT	1,
	'NOTREADY',
	SYSDATE,
	SYSDATE,
	USER,
	USER,
	1
FROM DUAL;

INSERT INTO XHB_SKELETON_DELIVERY_STATUS
SELECT	2,
	'READY',
	SYSDATE,
	SYSDATE,
	USER,
	USER,
	1
FROM DUAL;

INSERT INTO XHB_SKELETON_DELIVERY_STATUS
SELECT	3,
	'DELIVERED',
	SYSDATE,
	SYSDATE,
	USER,
	USER,
	1
FROM DUAL;

INSERT INTO XHB_SKELETON_DELIVERY_STATUS
SELECT	4,
	'FAILED',
	SYSDATE,
	SYSDATE,
	USER,
	USER,
	1
FROM DUAL;
COMMIT;
