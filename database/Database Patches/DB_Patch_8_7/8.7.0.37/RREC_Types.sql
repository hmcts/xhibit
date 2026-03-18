/*    ------------------------------------------------------------------
*     User defined types for RREC report - CTX-2160
*/    ------------------------------------------------------------------
-- Have to drop this before replacing rrec_case_info due to dependancy
drop type rrec_case_array;

CREATE OR REPLACE     type rrec_totals_array  is table of number(6);
/
Show Errors

CREATE OR REPLACE     type rrec_case_info     is object (section_num integer, case_type VARCHAR2(50 byte), case_number NUMBER(8,0), case_subhdg VARCHAR2(50), case_id  NUMBER(8,0));
/
show errors
CREATE OR REPLACE     type rrec_case_array    is table of rrec_case_info;
/
Show Errors

  
