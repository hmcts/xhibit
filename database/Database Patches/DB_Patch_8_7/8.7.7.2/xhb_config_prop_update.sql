/*    ------------------------------------------------------------------
*     Updating XHB_CONFIG_PROP with new scheduled job
*     CPP-70
*/    ------------------------------------------------------------------
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest1midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest1midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest1midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.DevTest1midM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.NLEmidM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.NLEmidM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.NLEmidM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.NLEmidM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.NLEmidM5';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.LIVEmidM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.LIVEmidM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.LIVEmidM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.LIVEmidM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppformattingtask' WHERE property_name = 'scheduledtasks.LIVEmidM5';

COMMIT;