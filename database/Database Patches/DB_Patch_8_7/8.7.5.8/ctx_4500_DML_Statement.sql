insert into xhb_config_prop VALUES ((select max(config_prop_id)+1 from xhb_config_prop),'XHB_HK_NO_OF_DAYS_COURTEL_LISTS_RETAINED_AFTER_CASE_DEALT',180);

COMMIT;
