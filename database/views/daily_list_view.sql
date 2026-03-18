CREATE OR REPLACE VIEW daily_list_v AS
   SELECT SCHEDULED_HEARING.scheduled_hearing_id,
          trim(DECODE(CASE.case_type, 'U', CASE.case_title,
                                      'B', CASE.case_title,
                                      trim(nvl2(xd.surname, xd.surname || ',', NULL) || NVL2(xd.first_name, ' ' || xd.first_name, NULL) || NVL2(xd.middle_name, ' ' || xd.middle_name, NULL)))
          ) AS def_name,
          REF_HEARING_TYPE.HEARING_TYPE_DESC AS hearing_type,
          CASE.CASE_TYPE || CASE.CASE_NUMBER AS case_string,
          CASE.CASE_ID,
          COURT_SITE.short_name || NVL2(COURT_SITE.short_name, ' - ', NULL)  || COURT_ROOM.display_name AS court_room_name,
          NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time) AS not_before_time,
          SITTING.IS_FLOATING AS floating,
          HEARING_LIST.COURT_ID,
          HEARING_LIST.START_DATE,
          SCHEDULED_HEARING.SEQUENCE_NO,
          sitting_sequence_no,
          crest_court_room_no,
          court_site_code
   FROM   XHB_HEARING_LIST HEARING_LIST,
          XHB_SITTING SITTING,
          XHB_COURT_ROOM COURT_ROOM,
          XHB_COURT_SITE COURT_SITE,
          XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
          XHB_HEARING HEARING,
          XHB_CASE CASE,
          XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
          XHB_SCHED_HEARING_DEFENDANT xshd,
          XHB_DEFENDANT_ON_CASE xdoc,
          XHB_DEFENDANT xd
    WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
    AND   SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
    AND   SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
    AND   SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
    AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
    AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
    AND   HEARING.CASE_ID = CASE.CASE_ID
    AND   court_SITE.COURT_ID = HEARING_LIST.COURT_ID
    AND   REF_HEARING_TYPE.COURT_ID =HEARING_LIST.COURT_ID
    AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = xshd.scheduled_hearing_id(+)
    AND   xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
    AND   xdoc.defendant_id = xd.defendant_id(+);

show errors

CREATE OR REPLACE VIEW daily_list_with_judge_v AS
   SELECT dlv.*,
          NVL(rj.FULL_LIST_TITLE1, rj.SURNAME) AS judge_name
   FROM   daily_list_v dlv,
          XHB_REF_JUDGE rj
   WHERE  XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(dlv.SCHEDULED_HEARING_ID) = rj.REF_JUDGE_ID(+);

show errors