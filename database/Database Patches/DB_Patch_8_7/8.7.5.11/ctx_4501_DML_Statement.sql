insert into xhb_config_prop VALUES ((select max(config_prop_id)+1 from xhb_config_prop),'XHB_HK_NO_OF_DAYS_BEFORE_LIST_MARKED_AS_OBSOLETE',180);

insert into xhb_config_prop VALUES ((select max(config_prop_id)+1 from xhb_config_prop),'XHB_HK_NO_OF_DAYS_BEFORE_A_LIST_IS_DELETED',730);

commit;


