UPDATE xhb_config_prop xcp 
SET xcp.property_value = xcp.property_value || ',pddatask'
WHERE xcp.property_name LIKE 'scheduledtasks.%'
AND xcp.property_value NOT LIKE '%pddatask%';

COMMIT;