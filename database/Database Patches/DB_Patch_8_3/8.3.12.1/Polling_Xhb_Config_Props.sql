------------------------------------------------------------------------------
-- Adds new properties for the Polling mechanism
------------------------------------------------------------------------------
INSERT INTO XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) 
                VALUES((select max(CONFIG_PROP_ID)+1 from XHB_CONFIG_PROP), 'POLL_INTERVAL','300000');

 INSERT INTO XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) 
                VALUES((select max(CONFIG_PROP_ID)+1 from XHB_CONFIG_PROP), 'CHECK_POLL_INTERVAL','600000');

commit;