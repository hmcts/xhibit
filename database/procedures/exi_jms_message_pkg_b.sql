CREATE OR REPLACE PACKAGE BODY exi_jms_message_pkg
AS
/******************************************************
** exi_jms_message_pkg: This package handles the triggers
**                      generated for the creation of
**                      JMS messages. The procedures and
**                      and functions in this package
**                      form part of the calling module's
**                      transaction. Therefore, no commit
**                      or rollback are performed.
*******************************************************/

/* ===================================================
**  read_config_params  - local function to
**                read config param into globals
** ===================================================
*/
PROCEDURE read_config_params
IS
BEGIN
   /*
   **  Set up the globals.
   ** If we are unable to determine the Global vlaues,
   ** then they will use the default value assigned to
   ** them in the package header.
   */
   BEGIN
     SELECT to_number( decode_property )
     INTO   G_bulk_count
     FROM   exi_property
     WHERE  property_type = C_JMS_MESSAGE_TYPE
     AND    property_code = C_BULK_COUNT_CODE;
  EXCEPTION
     WHEN OTHERS THEN
         null;
  END;

  BEGIN
     SELECT to_number( decode_property )
     INTO   G_max_tries
     FROM   exi_property
     WHERE  property_type = C_JMS_MESSAGE_TYPE
     AND    property_code = C_MAX_TRIES_CODE;

  EXCEPTION
     WHEN OTHERS THEN
         null;
  END;

   BEGIN
     SELECT to_number( decode_property )
     INTO   G_sleep_count
     FROM   exi_property
     WHERE  property_type = C_JMS_MESSAGE_TYPE
     AND    property_code = C_SLEEP_COUNT_CODE;
  EXCEPTION
     WHEN OTHERS THEN
         null;
  END;

  BEGIN
     SELECT decode_property
     INTO   G_disable_scjse_gateway
     FROM   exi_property
     WHERE  property_type = C_JMS_MESSAGE_TYPE
     AND    property_code = 'DISABLE SCJSE GATEWAY';
  EXCEPTION
     WHEN OTHERS THEN
         null;
  END;

  BEGIN
     SELECT last_updated
     INTO   G_config_read_time
     FROM   exi_table_timestamp
     WHERE  name = 'EXI_PROPERTY';
  EXCEPTION
     WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine last update date');
             RAISE;
  END;

END;

/* ===================================================
**  get_max_id  - local function to get max
**                item_id of the previous set
**                of rows that we tried to lock
** ===================================================
*/
   FUNCTION get_max_id( p_tries NUMBER )
      RETURN NUMBER
   IS
      v_max_id   NUMBER;
   BEGIN
      SELECT MAX (item_id)
        INTO v_max_id
        FROM
        (SELECT item_id
         FROM   exi_jms_message
         WHERE ROWNUM <= (g_bulk_count * p_tries)
         ORDER BY item_id);

      RETURN v_max_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         DBMS_OUTPUT.put_line ('failure in get_max_id');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_max_id;

/*=================================================
** create_jms_message - given item_id, type and
**                        item_type. Create a record
**                in exi_jms_message table
** ================================================
*/
   PROCEDURE create_jms_message (
      p_item_id     IN   NUMBER,
      p_target      IN   VARCHAR2,
      p_item_type   IN   VARCHAR2
   )
   IS
      no_parent   EXCEPTION;
      PRAGMA EXCEPTION_INIT (no_parent, -2291);
   BEGIN
       INSERT INTO exi_jms_message
           (item_id, target, item_type)
       select p_item_id, p_target, p_item_type
       from   exi_property
       where  property_type   = 'JMS_MESSAGE_PROPERTIES'
       and    property_code   = 'DISABLE SCJSE GATEWAY'
       and    decode_property = 'FALSE';
      
   EXCEPTION
       /*
      ** Foreign key violation.
      */
      WHEN no_parent THEN
         DBMS_OUTPUT.put_line (   'creat_jms_message: item_id '
                               || p_item_id
                               || ' parent missing'
                              );
         DBMS_OUTPUT.put_line ('No corresponding row in exi_item_outbound');
         RAISE;
      /*
      ** Duplicate primary key
      */
      WHEN DUP_VAL_ON_INDEX THEN
         DBMS_OUTPUT.put_line (   'creat_jms_message: item_id '
                               || p_item_id
                               || ' duplicate value'
                              );
          DBMS_OUTPUT.put_line ('item_id already exists in exi_jms_message');
         RAISE;
      WHEN OTHERS THEN
         DBMS_OUTPUT.put_line ('creat_jms_message: item_id ' || p_item_id);
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END create_jms_message;

/*==============================================================
** create_jms_message - given p_msg of type exi_jms_message_type,
**                      call create_jms_message passing the
**                individual item_id, target and item_type
** =============================================================
*/
   PROCEDURE create_jms_message (p_msg IN exi_jms_message_type)
   IS
   BEGIN
      create_jms_message (p_msg.item_id, p_msg.target, p_msg.item_type);
   END create_jms_message;

/*==============================================================
** get_jms_message_array - lock next set of rows in exi_jms_message,
**                   delete the rows and return the ids in an
**                   array of type exi_jms_message_array along
**                   with a count of number of rows selected.
** =============================================================
*/
   PROCEDURE get_jms_message_array (
      p_msg_array   OUT   exi_jms_message_array,
      p_count       OUT   NUMBER
   )
   IS
      resource_busy     EXCEPTION;
      PRAGMA EXCEPTION_INIT (resource_busy, -54);
      rc_jms_msg        jms_message_refcur_type;
      v_tries           NUMBER                  := 0;
      v_rows_selected   BOOLEAN                 := FALSE;
      v_max_id          NUMBER                  := 0;

      v_config_read_time DATE;
   BEGIN

      BEGIN
        SELECT last_updated
        INTO   v_config_read_time
        FROM   exi_table_timestamp
        WHERE  name = 'EXI_PROPERTY';
      EXCEPTION
       WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine last update date');
             RAISE;
      END;

      IF ( v_config_read_time != G_config_read_time ) THEN
          read_config_params;
      END IF;
     
      IF ( G_disable_scjse_gateway = 'TRUE' ) THEN
          /*
          **  Gateway is disabled
          **  Initialise with an empty array 
          */
      
          SELECT     exi_jms_message_type (item_id, target, item_type)
               BULK COLLECT INTO p_msg_array
               FROM exi_jms_message
               WHERE rownum = 0;                    
      END IF;
     
      WHILE (v_tries < g_max_tries) AND (NOT v_rows_selected)
            AND ( G_disable_scjse_gateway = 'FALSE') 
      LOOP
         BEGIN
           
            IF (v_tries = 0) THEN
               /*
               ** Exception is not raised by ORACLE when no rows are
               ** selected as a result of no more rows being available
               ** for processing
               */
               SELECT     exi_jms_message_type (item_id, target, item_type)
               BULK COLLECT INTO p_msg_array
                    FROM exi_jms_message
                    WHERE ROWNUM <= g_bulk_count
                    ORDER BY item_id
               FOR UPDATE NOWAIT;
            ELSE
               SELECT     exi_jms_message_type (item_id, target, item_type)
               BULK COLLECT INTO p_msg_array
                    FROM exi_jms_message
                    WHERE ROWNUM <= g_bulk_count AND item_id > v_max_id
                    ORDER BY item_id
               FOR UPDATE NOWAIT;
            END IF;

            /*
            ** NB Set when rows successfully locked or when no more
            ** rows to process.
            */
            v_rows_selected := TRUE;
         EXCEPTION
            WHEN resource_busy THEN
               /*
               ** rows are currently locked by another session.
               ** so retry, but skip the ones that we just tried
               ** to lock, by calculating v_max_id. We are allowed
               ** G_max_tries number of tries.
               */
               /* DBMS_OUTPUT.put_line ('resource busy - get next set of rows'); */
               v_tries := v_tries + 1;
               v_max_id := get_max_id(v_tries);


               /*
               ** NB Owner needs execute priv on DBMS_LOCK in order to call sleep
               */
               /*
               IF (g_sleep_count > 0) THEN
                  DBMS_LOCK.sleep (g_sleep_count);
               END IF;
               */
            WHEN OTHERS THEN
               DBMS_OUTPUT.put_line ('get_jms_message_array failure');
               DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
               RAISE;
         END;
      END LOOP;

      IF ( v_tries = G_max_tries ) THEN
      /*
      ** Raise user defined error. We have used up the maxnimum number of
      ** retries that we are allowed.
      */
         RAISE_APPLICATION_ERROR(-20101, 'Max tries attempted.');
      END IF;

       /*
       ** We managed to successfully select and lock a set of rows. Now, before
      ** passing the details to the calling module, we need to delete the
       ** corresponding rows
      */
      FOR r IN (SELECT *
                  FROM TABLE (CAST (p_msg_array AS exi_jms_message_array)))
      LOOP
         /*    DBMS_OUTPUT.put_line (r.item_id || ', ' || r.target || ', ' || r.item_type); */
         BEGIN
            DELETE FROM exi_jms_message
                  WHERE item_id = r.item_id;
         EXCEPTION
            WHEN OTHERS THEN
               DBMS_OUTPUT.put_line
                      (   'get_jms_message_array: delete failed. item_id is '
                       || TO_CHAR (r.item_id)
                      );
               DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
               RAISE;
         END;
      END LOOP;

      /*
      ** No commit is done here. The calling module will do a commit when all the
      ** items have been successfully processed, otherwise a rollback may be
      ** issued.
      */
      
         p_count := p_msg_array.COUNT;
                          
   END get_jms_message_array;

/*==============================================================
** get_jms_message_refcur - get next set of rows by calling
**                   get_jms_message_array. Then open a ref
**                   cursor on the result.
** =============================================================
*/
   FUNCTION get_jms_message_refcur
      RETURN SYS_REFCURSOR
   IS
      v_msg_array   exi_jms_message_array;
      v_count       NUMBER;
      rc_msg        SYS_REFCURSOR;
   BEGIN
      /* First get the results into the SQL type array */
      get_jms_message_array (v_msg_array, v_count);

      OPEN rc_msg
       FOR
          SELECT *
            FROM TABLE (CAST (v_msg_array AS exi_jms_message_array));

      RETURN rc_msg;
   EXCEPTION
      WHEN OTHERS THEN
         DBMS_OUTPUT.put_line ('get_jms_message_refcur failure');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_jms_message_refcur;


/* ===================================================
**  Package Initialisation
===================================================*/
BEGIN

   read_config_params;

END exi_jms_message_pkg;
/
