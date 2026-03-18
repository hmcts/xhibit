insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'CPPX_SchemaWP','CPPX_InternetWebPage_V1-0.xsd');
insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'CPPX_SchemaPD','CPPX_PublicDisplay_V1-0.xsd');
insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'CPPX_SchemaDL','DailyList-v1-0.xsd');
insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'CPPX_SchemaFL','FirmList-v1-0.xsd');
insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'CPPX_SchemaWL','WarnedList-v1-0.xsd');
insert into xhb_config_prop (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) values((select max(config_prop_id)+1 from xhb_config_prop),'STAGING_DOCS_TO_PROCESS',1);


UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest_as1_midM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest1midM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest1midM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest1midM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.DevTest1midM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.NLEmidM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.NLEmidM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.NLEmidM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.NLEmidM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.NLEmidM5';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.LIVEmidM1';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.LIVEmidM2';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.LIVEmidM3';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.LIVEmidM4';
UPDATE xhb_config_prop SET property_value = property_value || ',cppstagingtask' WHERE property_name = 'scheduledtasks.LIVEmidM5';

commit;
