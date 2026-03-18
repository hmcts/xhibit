/*
 * Author        Version    Date         Description
 * ----------------------------------------------------------------------
 * B Hingston    0.0.1      25/Mar/2010  Fix to typo in trigger - PR6383
 * -----------------------------------------------------------------------
 *
 * Usage: This script must be run by logging as sys or system
 */

SET serveroutput on
SET echo on

CREATE OR REPLACE TRIGGER xhb_leo_adv_link_nrf_i_wsm
AFTER INSERT ON xhb_leo_adv_link FOR EACH ROW
DECLARE
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_LEO_ADV_LINK',
                                     'NEW_ROW_FLAG',
                                     :new.new_row_flag,
                                     'INSERT',
                                     :new.leo_adv_link_id);
END;
/
SHOW ERRORS;

commit;