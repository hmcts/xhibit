UPDATE xhb_def_on_case_on_list def
SET def.obs_ind = 'Y'
WHERE NVL(def.obs_ind,'N') <> 'Y'
AND def.case_on_list_id = (SELECT cas.case_on_list_id FROM xhb_case_on_list cas WHERE cas.case_on_list_id = def.case_on_list_id AND NVL(cas.obs_ind,'N') = 'Y');

COMMIT;