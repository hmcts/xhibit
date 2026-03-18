/*
 * Filename:    delete_training_data.sql
 *
 * Description: Procedure for deleting table rows relating to a court ID entered by
 *              user running script.
 *
 * Date:        04 September 2003
 *
 * Project:     XHIBIT2
 *
 * Author(s):   A Brown, S Sangha, Nick Sawyer
 *
 */

/*
 * Due to the foreign key constraints on tables XHB_SKELETON_SESSION, XHB_SKELETON_DAY
 * and XHB_SKELETON_SCHEDULE the deletion from XHB_SKELETON_SESSION causes problems when
 * trying to delete from XHB_SKELETON_DAY and consequently XHB_SKELETON_SCHEDULE.
 *
 * To work around this, those records that should be deleted from XHB_SKELETON_DAY which
 * cannot be having already deleted its linked data to XHB_SKELETON_SESSION, will be stored
 * in a temporary table.  The ROWIDs will be stored as this will be all that is required.
 * This temporary table MUST be populated BEFORE the deletion from XHB_SKELETON_SESSION.
 */

  DROP TABLE TMP_SKELETON_DAY_DELETE;

  CREATE TABLE TMP_SKELETON_DAY_DELETE (del_row_id ROWID);

  DROP TABLE XHB_TRAINING_COURT;

  CREATE TABLE XHB_TRAINING_COURT (court_id NUMBER(8));
  INSERT INTO XHB_TRAINING_COURT
  VALUES (3);

PROMPT 
PROMPT Creating Procedure xhb_purge_training_data

CREATE OR REPLACE PROCEDURE xhb_purge_training_data (myCOURT_ID IN NUMBER) IS

BEGIN

  DELETE FROM XHB_DOCUMENT_REPLY
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_EMAIL
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_INTERNET_HTML 
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_LINKED_CASE LC
  WHERE  LC.LINKED_CASE_ID IN (SELECT CASE_ID
                               FROM   XHB_CASE C,
                                      XHB_COURT CT
                               WHERE  C.COURT_ID = CT.COURT_ID
                               AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_LINKED_HEARING
  WHERE  LINKED_HEARING_ID IN (SELECT LINKED_HEARING_ID
                               FROM   XHB_HEARING H
                               WHERE  H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_LINKED_SH LSH
  WHERE  LSH.LINKED_SH_ID IN (SELECT LINKED_SH_ID
                              FROM   XHB_SCHEDULED_HEARING SH,
                                     XHB_HEARING H
                              WHERE  SH.HEARING_ID = H.HEARING_ID
                              AND    H.COURT_ID = myCOURT_ID); 

  DELETE FROM XHB_OBJECT_STATUS
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_SUBSCR_EVENT_CONTROL SEC
  WHERE  SEC.CREST_COURT_ID IN (SELECT CREST_COURT_ID
                                FROM   XHB_COURT C
                                WHERE  C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DISPOSAL_REFERENCE DR
  WHERE  DR.DISPOSAL_ID IN (SELECT DISPOSAL_ID
                            FROM   XHB_DISPOSAL D,
                                   XHB_DEFENDANT_ON_OFFENCE DO,
                                   XHB_OFFENCE O,
                                   XHB_CHARGE CH,
                                   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  D.DEFENDANT_ON_OFFENCE_ID = DO.DEFENDANT_ON_OFFENCE_ID
                            AND    DO.OFFENCE_ID = O.OFFENCE_ID
                            AND    O.CHARGE_ID = CH.CHARGE_ID
                            AND    CH.CASE_ID = C.CASE_ID
                            AND    C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DISPOSAL_REFERENCE DR
  WHERE   DR.DISPOSAL_ID IN (SELECT DISPOSAL_ID
                             FROM   XHB_DISPOSAL D,
                                    XHB_DEFENDANT_ON_CASE DC,
                                    XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  D.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
                             AND    DC.CASE_ID = C.CASE_ID
                             AND    C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DISPOSAL D
  WHERE  D.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                    FROM   XHB_DEFENDANT_ON_CASE DC,
                                           XHB_CASE C,
                                           XHB_COURT CT
                                    WHERE  DC.CASE_ID = C.CASE_ID
                                    AND    C.COURT_ID = CT.COURT_ID
                                    AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DISPOSAL D
  WHERE  D.DEFENDANT_ON_OFFENCE_ID IN (SELECT DEFENDANT_ON_OFFENCE_ID
                                       FROM   XHB_DEFENDANT_ON_OFFENCE DO,
                                              XHB_OFFENCE O,
                                              XHB_CHARGE CH,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DO.OFFENCE_ID = O.OFFENCE_ID
                                       AND    O.CHARGE_ID = CH.CHARGE_ID
                                       AND    CH.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SH_LEG_REP LR
  WHERE  LR.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                     FROM   XHB_SCHEDULED_HEARING SH,
                                            XHB_HEARING H
                                     WHERE  SH.HEARING_ID = H.HEARING_ID
                                     AND    H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_PLEA P
  WHERE  P.DEFENDANT_CHARGE_ID IN (SELECT DEFENDANT_CHARGE_ID
                                   FROM   XHB_DEFENDANT_CHARGE DC,
                                          XHB_CHARGE CH,
                                          XHB_CASE C,
                                          XHB_COURT CT
                                   WHERE  DC.CHARGE_ID = CH.CHARGE_ID
                                   AND    CH.CASE_ID = C.CASE_ID
                                   AND    C.COURT_ID = CT.COURT_ID
                                   AND    CT.COURT_ID = myCOURT_ID);


  DELETE FROM XHB_PLEA P
  WHERE  P.DEFENDANT_ON_OFFENCE_ID IN (SELECT DEFENDANT_ON_OFFENCE_ID
                                       FROM   XHB_DEFENDANT_ON_OFFENCE DO,
                                              XHB_OFFENCE O,
                                              XHB_CHARGE CH,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DO.OFFENCE_ID = O.OFFENCE_ID
                                       AND    O.CHARGE_ID = CH.CHARGE_ID
                                       AND    CH.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_VERDICT V
  WHERE  V.DEFENDANT_CHARGE_ID IN (SELECT DEFENDANT_CHARGE_ID
                                   FROM   XHB_DEFENDANT_CHARGE DC,
                                          XHB_CHARGE CH,
                                          XHB_CASE C,
                                          XHB_COURT CT
                                   WHERE  DC.CHARGE_ID = CH.CHARGE_ID
                                   AND    CH.CASE_ID = C.CASE_ID
                                   AND    C.COURT_ID = CT.COURT_ID
                                   AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_VERDICT V
  WHERE  V.DEFENDANT_ON_OFFENCE_ID IN (SELECT DEFENDANT_ON_OFFENCE_ID
                                       FROM   XHB_DEFENDANT_ON_OFFENCE DO,
                                              XHB_OFFENCE O,
                                              XHB_CHARGE CH,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DO.OFFENCE_ID = O.OFFENCE_ID
                                       AND    O.CHARGE_ID = CH.CHARGE_ID
                                       AND    CH.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SH_JUDGE J
  WHERE  J.SH_ATTENDEE_ID IN (SELECT SH_ATTENDEE_ID
                              FROM   XHB_SCHED_HEARING_ATTENDEE SHA,
                                     XHB_SCHEDULED_HEARING SH,
                                     XHB_HEARING H
                              WHERE  SHA.SCHEDULED_HEARING_ID = SH.SCHEDULED_HEARING_ID
                              AND    SH.HEARING_ID = H.HEARING_ID
                              AND    H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_IMPORT_EXPORT_STATUS
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_WITNESS W
  WHERE  W.CASE_ID IN (SELECT CASE_ID
                       FROM   XHB_CASE C,
                              XHB_COURT CT
                       WHERE  C.COURT_ID = CT.COURT_ID
                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_PSR_REQUEST PRQ
  WHERE  PRQ.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                      FROM   XHB_DEFENDANT_ON_CASE DC,
                                             XHB_CASE C,
                                             XHB_COURT CT
                                      WHERE  DC.CASE_ID = C.CASE_ID
                                      AND    C.COURT_ID = CT.COURT_ID
                                      AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_JOINDER_DEFENDANT_ON_CASE JD
  WHERE  JD.defendant_on_case_id_1 IN (SELECT DEFENDANT_ON_CASE_ID
                                       FROM   XHB_DEFENDANT_ON_CASE DC,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DC.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_JOINDER_DEFENDANT_ON_CASE JD
  WHERE  JD.defendant_on_case_id_2 IN (SELECT DEFENDANT_ON_CASE_ID
                                       FROM   XHB_DEFENDANT_ON_CASE DC,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DC.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DIRECTIONS_FOR_DEFENDANT DD
  WHERE  DD.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                     FROM   XHB_DEFENDANT_ON_CASE DC,
                                            XHB_CASE C,
                                            XHB_COURT CT
                                     WHERE  DC.CASE_ID = C.CASE_ID
                                     AND    C.COURT_ID = CT.COURT_ID
                                     AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_ORDER O
  WHERE  O.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                    FROM   XHB_DEFENDANT_ON_CASE DC,
                                           XHB_CASE C,
                                           XHB_COURT CT
                                    WHERE  DC.CASE_ID = C.CASE_ID
                                    AND    C.COURT_ID = CT.COURT_ID
                                    AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_COURT_LOG_ENTRY CL
  WHERE  CL.CASE_ID IN (SELECT CASE_ID
                        FROM   XHB_CASE C,
                               XHB_COURT CT
                        WHERE  C.COURT_ID = CT.COURT_ID
                        AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DIRECTION_ATTEND DA
  WHERE  DA.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                     FROM   XHB_DEFENDANT_ON_CASE DC,
                                            XHB_CASE C,
                                            XHB_COURT CT
                                     WHERE  DC.CASE_ID = C.CASE_ID
                                     AND    C.COURT_ID = CT.COURT_ID
                                     AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SCHED_HEARING_DEFENDANT SHD
  WHERE  SHD.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                      FROM   XHB_SCHEDULED_HEARING SH,
                                             XHB_HEARING H
                                      WHERE  SH.HEARING_ID = H.HEARING_ID
                                      AND    H.COURT_ID = myCOURT_ID); 

  DELETE FROM XHB_DEFENDANT_CHARGE DC
  WHERE  DC.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                     FROM   XHB_DEFENDANT_ON_CASE DC,
                                            XHB_CASE C,
                                            XHB_COURT CT
                                     WHERE  DC.CASE_ID = C.CASE_ID
                                     AND    C.COURT_ID = CT.COURT_ID
                                     AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DEF_HEARING_RECORD DHR
  WHERE  DHR.HEARING_ID IN (SELECT HEARING_ID
                            FROM   XHB_HEARING H
                            WHERE  H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_CR_LIVE_STATUS CLS
  WHERE  CLS.COURT_ROOM_ID IN (SELECT COURT_ROOM_ID
                               FROM   XHB_COURT_ROOM XC,
                                      XHB_COURT_SITE XS,
                                      XHB_COURT C
                               WHERE  XC.COURT_SITE_ID = XS.COURT_SITE_ID
                               AND    XS.COURT_ID = C.COURT_ID
                               AND    C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DEFENDANT_ON_OFFENCE DO
  WHERE  DO.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                     FROM   XHB_DEFENDANT_ON_CASE DC,
                                            XHB_CASE C,
                                            XHB_COURT CT
                                     WHERE  DC.CASE_ID = C.CASE_ID
                                     AND    C.COURT_ID = CT.COURT_ID
                                     AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DISPOSAL_DETAIL DD
  WHERE  DD.ORIGINAL_RESULT_ID IN (SELECT ORIGINAL_RESULT_ID
                                   FROM   XHB_ORIGINAL_RESULT R,
                                          XHB_REF_COURT RC,
                                          XHB_COURT C
                                   WHERE  R.REF_COURT_ID = RC.REF_COURT_id
                                   AND    RC.COURT_ID = C.COURT_ID
                                   AND    C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SCHED_HEARING_ATTENDEE SHA
  WHERE  SHA.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                      FROM   XHB_SCHEDULED_HEARING SH,
                                             XHB_HEARING H
                                      WHERE  SH.HEARING_ID = H.HEARING_ID
                                      AND    H.COURT_ID = myCOURT_ID);

  /*
   * Due to the foreign key constraints on tables XHB_SKELETON_SESSION, XHB_SKELETON_DAY
   * and XHB_SKELETON_SCHEDULE the deletion from XHB_SKELETON_SESSION causes problems when
   * trying to delete from XHB_SKELETON_DAY and consequently XHB_SKELETON_SCHEDULE.
   *
   * To work around this, those records that should be deleted from XHB_SKELETON_DAY which
   * cannot be having already deleted its linked data to XHB_SKELETON_SESSION, will be stored
   * in a temporary table.  The ROWIDs will be stored as this will be all that is required.
   * This temporary table MUST be populated BEFORE the deletion from XHB_SKELETON_SESSION.
   */

  INSERT INTO TMP_SKELETON_DAY_DELETE (SELECT rowid
                                       FROM XHB_SKELETON_DAY SD
                                       WHERE  SD.SKELETON_DAY_ID IN (SELECT SKELETON_DAY_ID
                                                                     FROM   XHB_SKELETON_SESSION SS,
                                                                            XHB_SKELETON_SCHEDULE SSH,
                                                                            XHB_CASE C,
                                                                            XHB_COURT CT
                                                                     WHERE  SS.SKELETON_ID = SSH.SKELETON_ID
                                                                     AND    SSH.CASE_ID = C.CASE_ID
                                                                     AND    C.COURT_ID = CT.COURT_ID
                                                                     AND    CT.COURT_ID = myCOURT_ID));

  DELETE FROM XHB_SKELETON_SESSION SS
  WHERE  SS.SKELETON_ID IN (SELECT SKELETON_ID
                            FROM   XHB_SKELETON_SCHEDULE SSH,
                                   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  SSH.CASE_ID = C.CASE_ID
                            AND    C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DEFENDANT_REFERENCE DR
  WHERE  DR.DEFENDANT_ID IN (SELECT DEFENDANT_ID
                             FROM   XHB_DEFENDANT D,
                                    XHB_COURT CT
                             WHERE  D.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = myCOURT_ID);
/*
 * Should not be done as cannot link directly to court correctly
 *

  DELETE FROM XHB_PSR_RECIPIENT PR
  WHERE  PR.RECIPIENT_ID IN (SELECT PSR_RECIPIENT_ID
                             FROM   XHB_PSR_REQUEST PRQ,
                                    XHB_DEFENDANT_ON_CASE DC,
                                    XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  PRQ.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
                             AND    DC.CASE_ID = C.CASE_ID
                             AND    C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = myCOURT_ID);

 *
 *
 */

  DELETE FROM XHB_DEFENDANT_ON_CASE DC
  WHERE  DC.CASE_ID IN (SELECT CASE_ID
                        FROM   XHB_CASE C,
                               XHB_COURT CT
                        WHERE  C.COURT_ID = CT.COURT_ID
                        AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DOCUMENT_RECIPIENT DR
  WHERE  DR.DOC_CONTROL_ID IN (SELECT DOC_CONTROL_ID
                               FROM   XHB_DOCUMENT_CONTROL DC,
                                      XHB_COURT CT
                               WHERE  DC.COURT_ID = CT.COURT_ID
                               AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DIRECTIONS_FOR_CASE DC
  WHERE  DC.CASE_ID IN (SELECT CASE_ID
                        FROM   XHB_CASE C,
                               XHB_COURT CT
                        WHERE  C.COURT_ID = CT.COURT_ID
                        AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SCHEDULED_HEARING SH
  WHERE  SH.HEARING_ID IN (SELECT HEARING_ID
                           FROM   XHB_HEARING H
                           WHERE  H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_OFFENCE O
  WHERE  O.CHARGE_ID IN (SELECT CHARGE_ID
                         FROM   XHB_CHARGE CH,
                                XHB_CASE C,
                                XHB_COURT CT
                         WHERE  CH.CASE_ID = C.CASE_ID
                         AND    C.COURT_ID = CT.COURT_ID
                         AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_BREACH B
  WHERE  B.CHARGE_ID IN (SELECT CHARGE_ID
                         FROM   XHB_CHARGE CH,
                                XHB_CASE C,
                                XHB_COURT CT
                         WHERE  CH.CASE_ID = C.CASE_ID
                         AND    C.COURT_ID = CT.COURT_ID
                         AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_JOINDER_CHARGE JC
  WHERE  JC.CHARGE_ID IN (SELECT CHARGE_ID
                          FROM   XHB_CHARGE CH,
                                 XHB_CASE C,
                                 XHB_COURT CT
                          WHERE  CH.CASE_ID = C.CASE_ID
                          AND    C.COURT_ID = CT.COURT_ID
                          AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_EXPORTA E
  WHERE  E.HEARING_ID IN (SELECT HEARING_ID
                          FROM   XHB_HEARING H,
                                 XHB_COURT C
                          WHERE  H.COURT_ID = C.COURT_ID
                          AND    C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_ORIGINAL_RESULT R
  WHERE  R.REF_COURT_ID IN (SELECT REF_COURT_ID
                            FROM   XHB_REF_COURT RC,
                                   XHB_COURT C
                            WHERE  RC.COURT_ID = C.COURT_ID
                            AND    C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SH_JUSTICE J
  WHERE  J.HEARING_ID IN (SELECT HEARING_ID
                          FROM   XHB_HEARING H
                          WHERE  H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_JOINDER_XML JX
  WHERE  JX.JOINDER_ID IN (SELECT JOINDER_ID
                           FROM   XHB_JOINDER_CHARGE JC,
                                  XHB_CHARGE CH,
                                  XHB_CASE C,
                                  XHB_COURT CT
                           WHERE  JC.CHARGE_ID = CH.CHARGE_ID
                           AND    CH.CASE_ID = C.CASE_ID
                           AND    C.COURT_ID = CT.COURT_ID
                           AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SH_STAFF SS
  WHERE  SS.SH_STAFF_ID IN (SELECT SH_STAFF_ID
                            FROM   XHB_SCHED_HEARING_ATTENDEE SHA,
                                   XHB_SCHEDULED_HEARING SH,
                                   XHB_HEARING H
                            WHERE  SHA.SCHEDULED_HEARING_ID = SH.SCHEDULED_HEARING_ID 
                            AND    SH.HEARING_ID = H.HEARING_ID
                            AND    H.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SKELETON_DAY
  WHERE  ROWID IN (SELECT del_row_id
                   FROM   TMP_SKELETON_DAY_DELETE);

/*
 * Will not work once rows from XHB_SKELETON_SESSION havce been deleted so the above
 * deletion is used.
 *

  DELETE FROM XHB_SKELETON_DAY SD
  WHERE  SD.SKELETON_DAY_ID IN (SELECT SKELETON_DAY_ID
                                FROM   XHB_SKELETON_SESSION SS,
                                       XHB_SKELETON_SCHEDULE SSH,
                                       XHB_CASE C,
                                       XHB_COURT CT
                                WHERE  SS.SKELETON_ID = SSH.SKELETON_ID
                                AND    SSH.CASE_ID = C.CASE_ID
                                AND    C.COURT_ID = CT.COURT_ID
                                AND    CT.COURT_ID = myCOURT_ID);
 *
 *
 */

  DELETE FROM XHB_WLL_DOCUMENT WD
  WHERE  WD.XML_DOCUMENT_ID IN (SELECT XML_DOCUMENT_ID
                                FROM   XHB_XML_DOCUMENT XD,
                                       XHB_COURT CT
                                WHERE  XD.COURT_ID = CT.COURT_ID
                                AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DOCUMENT_DISTRIBUTION
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_DEFENDANT
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_SITTING 
  WHERE  COURT_SITE_ID IN (SELECT COURT_SITE_ID
                           FROM   XHB_COURT_SITE CS,
                                  XHB_COURT C
                           WHERE  CS.COURT_ID = C.COURT_ID
                           AND    C.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_DOCUMENT_CONTROL
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_CASE_PROSECUTOR_AGENCY CPA
  WHERE  CPA.CASE_ID IN (SELECT CASE_ID
                         FROM   XHB_CASE C,
                                XHB_COURT CT
                         WHERE  C.COURT_ID = CT.COURT_ID
                         AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_CASE_REFERENCE CR
  WHERE  CR.CASE_ID IN (SELECT CASE_ID
                        FROM   XHB_CASE C,
                               XHB_COURT CT
                        WHERE  C.COURT_ID = CT.COURT_ID
                        AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_CHARGE C
  WHERE  C.CASE_ID IN (SELECT CASE_ID
                       FROM   XHB_CASE C,
                              XHB_COURT CT
                       WHERE  C.COURT_ID = CT.COURT_ID
                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_CHARGE_DIFFERENCES 
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_HEARING 
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_TIME T
  WHERE  T.CASE_ID IN (SELECT CASE_ID
                       FROM   XHB_CASE C,
                              XHB_COURT CT
                       WHERE  C.COURT_ID = CT.COURT_ID
                       AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_JOINDER J
  WHERE  J.JOINDER_ID IN (SELECT JOINDER_ID
                          FROM   XHB_JOINDER_CHARGE JC,
                                 XHB_CHARGE CH,
                                 XHB_CASE C,
                                 XHB_COURT CT
                          WHERE  JC.CHARGE_ID = CH.CHARGE_ID
                          AND    CH.CASE_ID = C.CASE_ID
                          AND    C.COURT_ID = CT.COURT_ID
                          AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_SKELETON_SCHEDULE SSH
  WHERE  SSH.CASE_ID IN (SELECT CASE_ID
                         FROM   XHB_CASE C,
                                XHB_COURT CT
                         WHERE  C.COURT_ID = CT.COURT_ID
                         AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_WLL_CONTROL WC
  WHERE  WC.XML_DOCUMENT_ID IN (SELECT XML_DOCUMENT_ID
                                FROM   XHB_XML_DOCUMENT XD,
                                       XHB_COURT CT
                                WHERE  XD.COURT_ID = CT.COURT_ID
                                AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_WLL_RECIPIENT
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_RECIPIENT
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_ADDRESS A
  WHERE  A.ADDRESS_ID IN (SELECT R.RECIPIENT_ADDRESS_ID
                          FROM   XHB_PSR_RECIPIENT R,
                                 XHB_PSR_REQUEST PRQ,
                                 XHB_DEFENDANT_ON_CASE DC,
                                 XHB_CASE C,
                                 XHB_COURT CT
                          WHERE  R.RECIPIENT_ID = PRQ.PSR_RECIPIENT_ID
                          AND    PRQ.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
                          AND    DC.CASE_ID = C.CASE_ID
                          AND    C.COURT_ID = CT.COURT_ID
                          AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_ADDRESS A
  WHERE  A.ADDRESS_ID IN (SELECT R.ADDRESS_ID
                          FROM   XHB_DEFENDANT R,
                                 XHB_COURT CT
                          WHERE  R.COURT_ID = CT.COURT_ID
                          AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_HEARING_LIST 
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_FORMATTING
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_SKELETON_DELIVERY_STATUS SDS
  WHERE  SDS.SKELETON_DELIVERY_STATUS_ID IN (SELECT SKELETON_DELIVERY_STATUS_ID
                                             FROM   XHB_SKELETON_SCHEDULE SSH,
                                                    XHB_CASE C,
                                                    XHB_COURT CT
                                             WHERE  SSH.CASE_ID = C.CASE_ID
                                             AND    C.COURT_ID = CT.COURT_ID
                                             AND    CT.COURT_ID = myCOURT_ID);

  DELETE FROM XHB_XML_DOCUMENT
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_CASE
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM XHB_CREST_IMPORT
  WHERE  COURT_ID = myCOURT_ID;

  DELETE FROM TMP_SKELETON_DAY_DELETE;

  COMMIT;

END xhb_purge_training_data;
/
show errors
