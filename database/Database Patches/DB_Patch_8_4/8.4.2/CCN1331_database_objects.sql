

-- Add the new column to the audit table as well
alter table AUD_DEF_HEARING_RECORD add MP_HEARING_TYPE varchar2(1);

-- And modify the update trigger to populate the new MP_HEARING_TYPE in the audit table
@XHB_DEF_HEARING_RECORD_BUR_TR.sql




