insert into xhb_config_prop VALUES ((select max(config_prop_id)+1 from xhb_config_prop),'XHB_HK_NO_OF_DAYS_TO_HOUSEKEEP',365);
