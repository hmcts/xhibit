/*
 * Author:      Nick Sawyer
 *
 * Description: Drops various sequences, synonyms, tables
 *              and procedures.
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */

DROP    SEQUENCE       mseq_server_id;
DROP    SEQUENCE       mseq_registry_id;
DROP    PUBLIC SYNONYM trigger_events;
DROP    PUBLIC SYNONYM trigger_registry;
DROP    PUBLIC SYNONYM trigger_catalog;
DROP    PUBLIC SYNONYM trigger_server;
DROP    TABLE          mtbl_trigger_events;
DROP    TABLE          mtbl_trigger_registry;
DROP    TABLE          mtbl_trigger_catalog;
DROP    TABLE          mtbl_trigger_server;
DROP    PUBLIC SYNONYM msp_get_server_id;
DROP    PUBLIC SYNONYM msp_register;
DROP    PUBLIC SYNONYM msp_register_srvr;
DROP    PUBLIC SYNONYM msp_shutdown_srvr;
DROP    PUBLIC SYNONYM msp_trig_exists;
DROP    PUBLIC SYNONYM msp_wait_event;
DROP    PUBLIC SYNONYM msp_signal_event;
DROP    PUBLIC SYNONYM msp_remove_event;
DROP    PUBLIC SYNONYM msp_create_trigger;
DROP    PUBLIC SYNONYM msp_drop_trigger;
DROP    PROCEDURE      msp_get_server_id1;
DROP    PROCEDURE      msp_register1;
DROP    PROCEDURE      msp_register_srvr1;
DROP    PROCEDURE      msp_shutdown_srvr1;
DROP    PROCEDURE      msp_trig_exists1;
DROP    PROCEDURE      msp_record_events1;
DROP    PROCEDURE      msp_wait_event1;
DROP    PROCEDURE      msp_signal_event1;
DROP    PROCEDURE      msp_remove_event1;
DROP    PROCEDURE      msp_create_trigger1;
DROP    PROCEDURE      msp_drop_trigger1;
