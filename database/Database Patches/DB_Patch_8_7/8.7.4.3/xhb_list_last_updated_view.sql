CREATE OR REPLACE VIEW XHB_LIST_LAST_UPDATED_V (
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
SELECT af.list_id
,af.list_type_id
,af.list_parent_id
,af.court_id
,af.draft_or_final
,af.list_number
,af.list_start_date
,af.list_end_date
,af.publish_date
,af.publish_status
,af.publish_error_reason
,af.obs_ind
,af.created_by
,af.last_updated_by
,af.creation_date
,af.last_update_date
,af.version
FROM xhb_list af,
(SELECT MAX(xl.last_update_date) AS last_update_date
,xl.court_id
,xl.list_start_date
,xl.list_end_date
,xl.list_type_id
FROM xhb_list xl
WHERE NVL(xl.obs_ind,'N') <> 'Y'
GROUP BY xl.court_id
,xl.list_start_date
,xl.list_end_date
,xl.list_type_id) sf
WHERE af.court_id = sf.court_id
AND af.last_update_date = sf.last_update_date
AND af.list_start_date = sf.list_start_date
AND af.list_end_date = sf.list_end_date
AND af.list_type_id = sf.list_type_id
AND NVL(af.obs_ind,'N') <> 'Y';