alter session set nls_date_format = 'DD-MON-YYYY';

execute xhb_populate_ref_calendar;

COMMIT;