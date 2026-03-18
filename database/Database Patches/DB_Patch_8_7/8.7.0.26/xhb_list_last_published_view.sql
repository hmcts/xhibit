CREATE OR REPLACE VIEW XHB_LIST_LAST_PUBLISHED_V (
  LIST_ID,
  LIST_TYPE_ID,
  LIST_PARENT_ID,
  COURT_ID,
  DRAFT_OR_FINAL,
  LIST_NUMBER,
  LIST_START_DATE,
  LIST_END_DATE,
  PUBLISH_DATE,
  PUBLISH_STATUS,
  PUBLISH_ERROR_REASON,
  OBS_IND,
  CREATED_BY,
  LAST_UPDATED_BY,
  CREATION_DATE,
  LAST_UPDATE_DATE,
  VERSION) AS 
WITH /* Subquery returns all data from xhb_list */
  all_fields AS (SELECT xl.*
                 FROM xhb_list xl
                 WHERE nvl(obs_ind,'N') <> 'Y')
, /* Subquery groups the list records by type, court, start date and end date and finds the latest update date */
  subset_fields AS (SELECT max(xl.last_update_date) AS last_update_date,
                    xl.court_id,
                    xl.list_start_date,
                    xl.list_end_date,
                    xl.list_type_id
                    FROM xhb_list xl
                    WHERE nvl(xl.obs_ind,'N') <> 'Y'
                    AND nvl(xl.publish_status,'~') IN ('SUCCESS','FAILURE')
                    GROUP BY xl.court_id,
                    xl.list_start_date,
                    xl.list_end_date,
                    xl.list_type_id)
SELECT af.*
FROM all_fields af,
     subset_fields sf
WHERE af.court_id = sf.court_id
AND af.last_update_date = sf.last_update_date
AND af.list_start_date = sf.list_start_date
AND af.list_end_date = sf.list_end_date
AND af.list_type_id = sf.list_type_id;
