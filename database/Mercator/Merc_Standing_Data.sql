INSERT INTO MTBL_MAP_ROUTE VALUES (1, 'AddChargeToCase_Route', 'I', NULL, 'Count_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (2, 'AddChargeToCase_Route', 'O', NULL, 'Offences_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (3, 'AddChargeToCase_Route', 'S', NULL, 'Committal_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (4, 'AddChargeToCase_Route', 'B', NULL, 'Breaches_Insert_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (5, 'AddOffence_Route', 'I', NULL, 'Count_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (6, 'AddOffence_Route', 'O', NULL, 'Offences_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (7, 'AddOffence_Route', 'S', NULL, 'Committal_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (8, 'AddOffence_Route', 'B', NULL, 'Breaches_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (9, 'DeleteCharge_Route', 'I', NULL, 'Indictment_Delete_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (10, 'DeleteCharge_Route', 'B', NULL, 'Breaches_Delete_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (11, 'DeleteOffence_Route', 'I', NULL, 'Count_Delete_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (12, 'DeleteOffence_Route', 'O', NULL, 'Offences_Delete_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (13, 'DeleteOffence_Route', 'S', NULL, 'Committal_Delete_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (14, 'UpdateOffence_Route', 'I', NULL, 'Count_Amend_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (15, 'UpdateOffence_Route', 'O', NULL, 'Offences_Update_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (16, 'UpdateOffence_Route', 'S', NULL, 'Committal_Update_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (17, 'UpdateOffence_Route', 'B', NULL, 'Breaches_Offence_Update_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (18, 'AddDefendant_Route', 'I', NULL, 'Defendant_Count_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (19, 'AddDefendant_Route', 'S', NULL, 'Defendant_Offence_Add_Trigger');
INSERT INTO MTBL_MAP_ROUTE VALUES (20, 'AddDefendant_Route', 'O', NULL, 'Defendant_Offence_Add_Trigger');
insert into mtbl_map_route values (21, 'DeleteOffence_Route', 'B', null, 'Breaches_Offences_Delete_Trigger');

COMMIT;

INSERT INTO MTBL_MERC_EMAIL_ALERT (MAPNAME,
                                   ERRORCODE,
                                   TOEMAILADDRESS1,
                                   TOEMAILADDRESS2,
                                   TOEMAILADDRESS3,
                                   CCEMAILADDRESS,
                                   BCCEMAILADDRESS,
                                   FROMEMAILADDRESS,
                                   SUBJECT,
                                   SERVER,
                                   PROTOCOL,
                                   ADDITIONAL1,
                                   ADDITIONAL2,
                                   ADDITIONAL3,
                                   ADDITIONAL4)
                           VALUES ('DEFAULT',
                                   12345,
                                   'toemail1@eds.com',
                                   'toemail2@eds.com',
                                   'toemail3@eds.com',
                                   'ccemail@eds.com',
                                   'bccemail@eds.com',
                                   'fromemail@eds.com',
                                   'subject:default_email',
                                   'CSA00100',
                                   'MAPI',
                                   'text_additional1',
                                   'text_additional2',
                                   'text_additional3',
                                   'text_additional4');

COMMIT;

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('Internet_Interface_Polling.mmc', TO_Date( '02/03/2004 03:09:04 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa00100', 595, 'C');

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJSE_Poll.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa01001', 55, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJIP_Event_Trigger.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa01010', 55, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJIP_Doc_DeReg_Trigger.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa100101', 55, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CSU_CJIT_Trigger.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa0101011', 25, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('AHM_Health_Check.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 295, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('BITS_Health_Check.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 295, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJIP_Health_Check.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa0100', 295, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('Timed_Audit_Process.mmc', TO_Date( '02/04/2004 11:01:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa00100', 55, 'C');

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('AHM_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('BITS_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJIP_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('DOC_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('AHM_REPLY_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('CJIP_REQUEST_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('DOC_INSERT_CJIP_HouseKeeping.mmc', TO_Date( '01/01/2004 02:14:17 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa', 86395, NULL);

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('ResetLoadFailureCases.mmc', TO_Date( '02/03/2004 03:42:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa00100', 3595, 'C');

INSERT INTO MTBL_TIME_TRIGGER_STATUS (MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS)
VALUES ('Daily_List_Data_Upload.mmc', TO_Date( '02/04/2004 10:40:49 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'csa00100', 1795, 'C');

COMMIT;
