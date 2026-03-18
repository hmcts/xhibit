set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 120;
set pagesize 50;
set long     100000;
set null     <NULL>;

column property_name  format  a40        heading 'Property Name';
column property_value format  a60        heading 'Property Value';

variable results_out refcursor;

prompt
prompt This report shows inbound properties for a given item ID
prompt

accept itemid number prompt 'Item ID: '

ttitle center 'Properties For Inbound Record: ' &itemid skip 3;

exec exi_application_support_pkg.get_inbound_props_by_item_id(:results_out, &itemid);

print results_out;

clear columns;
clear breaks;
