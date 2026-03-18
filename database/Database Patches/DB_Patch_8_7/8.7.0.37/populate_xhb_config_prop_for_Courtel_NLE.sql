REM INSERTING into XHB_CONFIG_PROP
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_USERNAME','cgi-ctc');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_PASSWORD','red#cinema');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_TIMEOUT','60');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_LIST_AMOUNT','1');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_FILE_PATH','/opt/moj/bea/user_projects/domains/XHIBIT_NLE/courtel');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_KEY_MANAGER_JKS','/opt/moj/bea/user_projects/domains/XHIBIT_NLE/common/security/courtel.jks');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_TRUST_MANAGER_JKS','/opt/moj/bea/user_projects/domains/XHIBIT_NLE/common/security/courteltrust');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_KEY_MANAGER_PASSWORD','NLEPassword');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_TRUST_MANAGER_PASSWORD','courteltrust');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_SERVER_1','https://www.courtel-rx1.net/xhibitinterfaceauth/xhibitlib/upload.php');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_SERVER_2','https://www.courtel-rx2.net/xhibitinterfaceauth/xhibitlib/upload.php');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_CGI_PROXY','192.168.200.10');
Insert into XHB_CONFIG_PROP (CONFIG_PROP_ID,PROPERTY_NAME,PROPERTY_VALUE) values ((select max(config_prop_id)+1 from xhb_config_prop),'COURTEL_CGI_PROXY_PORT','80');

commit;
