set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 120;
set pagesize 30;
set long     100000;
set null     <NULL>;

column itemtype      noprint new_value new_itemtype;
column itemdesc      noprint new_value new_itemdesc;
column identifier    noprint new_value new_identifier;
column crest_court_id noprint new_value new_crest_court_id;
column description    noprint new_value new_description;
column created noprint new_value new_created;
column expires noprint new_value new_expires;
column clob_data format a120 heading 'Message Payload';

variable results_out refcursor;

prompt
prompt This report shows details for an outbound record for a given item ID
prompt

accept itemid number prompt 'Enter item ID: '

ttitle center 'Details For Outbound Record: ' &itemid skip 3 -
left '          Type: ' new_itemtype ' - ' new_itemdesc skip 1 -
left '    Identifier: ' new_identifier skip 1 -
left 'CREST Court ID: ' new_crest_court_id skip 1 -
left '   Description: ' new_description skip 1 -
left '  Item Created: ' new_created skip 1 -
left '  Item Expires: ' new_expires skip 2;

exec exi_application_support_pkg.get_item_by_item_id(:results_out, &itemid);

print results_out;

clear columns;
clear breaks;
