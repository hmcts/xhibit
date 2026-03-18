REM Need to drop the array type, before we can ammend the
REM object type

DROP type exi_jms_message_array;

CREATE OR REPLACE TYPE exi_jms_message_type AS OBJECT (
    item_id  number(16),
    target varchar2(30),
    item_type VARCHAR2(30)
 );
/


CREATE OR REPLACE TYPE exi_jms_message_array 
    AS TABLE OF exi_jms_message_type;
/


CREATE OR REPLACE package exi_jms_message_pkg AS

   TYPE jms_message_refcur_type IS
     REF CURSOR
   RETURN exi_jms_message%ROWTYPE;

   PROCEDURE create_jms_message(p_item_id      IN NUMBER,
                                p_target    IN VARCHAR2,
                                p_item_type IN VARCHAR2);

   PROCEDURE create_jms_message(p_msg IN exi_jms_message_type);

   PROCEDURE get_jms_message_array(p_msg_array  OUT exi_jms_message_array,
                                   p_count      OUT number );

   FUNCTION get_jms_message_refcur RETURN SYS_REFCURSOR;


   /*
   ** Following constants are used during lookup on exi_property
   */

   /* Following corresponds to exi_property.property_type */
   C_JMS_MESSAGE_TYPE   CONSTANT VARCHAR2(30) := 'JMS_MESSAGE_PROPERTIES';

   /* Following corresponds to exi_property.property_code */
   C_BULK_COUNT_CODE    CONSTANT VARCHAR2(30) := 'BULK COUNT';
   C_MAX_TRIES_CODE     CONSTANT VARCHAR2(30) := 'MAX TRIES';
   C_SLEEP_COUNT_CODE   CONSTANT VARCHAR2(30) := 'SLEEP COUNT';
   C_DISABLE_SCJSE_GATEWAY CONSTANT VARCHAR2(30) := 'DISABLE SCJSE GATEWAY';

   /*
   ** Assign default values to the globals.
   ** These will be initialised as part of package initialisation
   */
   G_disable_scjse_gateway VARCHAR2(10) := 'FALSE';
   G_bulk_count NUMBER := 10;
   G_max_tries   NUMBER := 3;
   G_sleep_count NUMBER := 5;   --number of secs to sleep
                                -- between retries

   G_config_read_time DATE;


END exi_jms_message_pkg;
/
show errors;