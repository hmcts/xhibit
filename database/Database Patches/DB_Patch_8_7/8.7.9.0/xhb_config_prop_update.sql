/*    ---------------------------------------------------------------------
*     UPDATE XHB_CONFIG_PROP
*     CTX-4660 - Change the parameter only if the site hasn't set their own 
*/    ---------------------------------------------------------------------
BEGIN
  UPDATE XHB_CONFIG_PROP 
     SET PROPERTY_VALUE = 30
   WHERE PROPERTY_NAME = 'XHB_HK_NO_OF_DAYS_TO_HOUSEKEEP'
     AND PROPERTY_VALUE = 365;
END;
/

COMMIT;